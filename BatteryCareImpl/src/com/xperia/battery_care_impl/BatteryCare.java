/*
 * Copyright (c) 2020, Shashank Verma (shank03) <shashank.verma2002@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 */

package com.xperia.battery_care_impl;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.os.SystemProperties;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.xperia.battery_care_impl.utils.NotificationHelper;
import com.xperia.battery_care_impl.utils.Preference;
import com.xperia.battery_care_impl.utils.Utils;

import java.util.Calendar;

/**
 * How this service works ?
 * <p>
 * It disables charging when the battery is at 100% after a minute.
 * It re-enables charging when the battery drops <= 99%
 * <p>
 * Battery Care:
 * Once {@link Intent#ACTION_POWER_CONNECTED} is received, the battery care process starts based on the selected/stored {@link Preference#BATTERY_CARE_ENABLE_TIME}.
 * If battery percentage is less than 15, it will charge battery until it's 15% and maintain it.
 * Once ^time is closer to the {@link Preference#EXPECTED_FULL_CHARGE_TIME}, device will start charging.
 * <p>
 * ^Time assumed here is 2 hours before [Preference.EXPECTED_FULL_CHARGE_TIME] if battery is at 15%
 * ^Time is inversely proportional to the battery percentage above or equal to 15%
 */
public class BatteryCare extends Service {

    private static final String TAG = "BatteryCare";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static boolean started = false;
    public static boolean mBatteryCareAllowed = true;

    // Global variables specific to battery care
    boolean _batteryCareStarted = false;
    boolean _chargingStart = false;
    long mMinToCharge = 2 * 60L;
    float _batPct = -1F;

    NotificationHelper helper;

    /**
     * Handler variables :
     * {@link #handler} to work in background -> {@link Process#THREAD_PRIORITY_BACKGROUND}
     * {@link #runnable} to constantly loop (every 60 sec) if either {@link #mBatteryCareAllowed} or {@link Preference#getBatteryCareEnabled(Context)} is changed;
     * if so then {@link #stopBatteryCare()}
     */
    HandlerThread handlerThread;
    Handler handler;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (mBatteryCareAllowed && Preference.getBatteryCareEnabled(getApplicationContext())) {
                    // Check if device is charging at the start of service
                    if (isCharging()) {
                        startBatteryCare(null);
                    }
                    shouldChargingStart();
                } else {
                    stopBatteryCare();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                handler.postDelayed(this, 60 * 1000L);
            }
        }
    };

    /**
     * Broadcast receiver for Battery Actions
     */
    BroadcastReceiver batteryStatsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
                    Utils.log(TAG, "onReceive: Power Connected", getApplicationContext());
                    startBatteryCare(intent);
                }
                if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
                    Utils.log(TAG, "onReceive: Power Disconnected", getApplicationContext());
                    stopBatteryCare();
                }
                if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                    updateBatteryPercentage(intent);

                    if (!mBatteryCareAllowed) {
                        stopBatteryCare();
                    }
                    if (_batteryCareStarted && mBatteryCareAllowed) {
                        if (!_chargingStart) {
                            if (_batPct >= 15) {
                                disableCharging();
                            } else {
                                enableCharging();
                            }
                        }
                    }
                    if (_batPct == 100F) {
                        Utils.log(TAG, "onReceive: Battery Full. Stopping all", getApplicationContext());
                        if (_batteryCareStarted) {
                            Utils.log(TAG, "onReceive: Completed at " + Utils.getTimeString(System.currentTimeMillis(), getApplicationContext()), getApplicationContext());
                        }
                        disableCharging();
                        stopBatteryCare();
                    } else {
                        enableCharging();
                    }
                }
            }
        }
    };

    @Override
    public void onCreate() {
        Utils.log(TAG, "onCreate: Service Started", getApplicationContext());

        helper = new NotificationHelper(getApplicationContext());
        handlerThread = new HandlerThread("BatteryCareThread", Process.THREAD_PRIORITY_BACKGROUND);
        if (!handlerThread.isAlive()) {
            handlerThread.start();
        }
        handler = new Handler(handlerThread.getLooper());

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryStatsReceiver, filter);

        Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_SHORT).show();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!started) {
            handler.post(runnable);
            started = true;
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Utils.log(TAG, "onDestroy: Called", getApplicationContext());
        unregisterReceiver(batteryStatsReceiver);
        stopBatteryCare();

        handler.removeCallbacks(runnable);
        handlerThread.quitSafely();
        handler = null;

        started = false;
        System.gc();
        super.onDestroy();
    }

    /**
     * Method name explains itself
     */
    private void startBatteryCare(@Nullable Intent intent) {
        if (!_batteryCareStarted) {
            if (batteryCareShouldStart()) {
                Utils.log(TAG, "startBatteryCare: Battery Care should start = TRUE", getApplicationContext());

                _batteryCareStarted = true;
                updateBatteryPercentage(intent);

                helper.getManager().notify(1, helper.batteryCareNotification());
            } else {
                Utils.log(TAG, "startBatteryCare: Battery Care should start = FALSE", getApplicationContext());
            }
        } else {
            Utils.log(TAG, "startBatteryCare: Already started", getApplicationContext());
        }
    }

    /**
     * Method name explains itself
     */
    private void stopBatteryCare() {
        if (_batteryCareStarted) {
            Utils.log(TAG, "stopBatteryCare: Stopping", getApplicationContext());
            _batteryCareStarted = false;
            _chargingStart = false;
            _batPct = -1F;
            mBatteryCareAllowed = true;
            enableCharging();
        } else {
            Utils.log(TAG, "stopBatteryCare: Already stopped", getApplicationContext());
        }
    }

    /**
     * Checks timings, preferences and {@link #mBatteryCareAllowed} to
     * @return whether battery care should start
     */
    private boolean batteryCareShouldStart() {
        long milli = Preference.getBatteryCareEnabledTime(getApplicationContext());
        long expChT = Preference.getExpectedFullChargeTime(getApplicationContext());
        if (milli == Preference.INVALID_TIME && expChT == Preference.INVALID_TIME) {
            return false;
        }

        Calendar old = Calendar.getInstance();
        old.setTimeInMillis(milli);

        // Get latest milli from pref
        Calendar n = Calendar.getInstance();
        n.set(Calendar.HOUR_OF_DAY, old.get(Calendar.HOUR_OF_DAY));
        n.set(Calendar.MINUTE, old.get(Calendar.MINUTE));

        return System.currentTimeMillis() >= n.getTimeInMillis() && mBatteryCareAllowed && Preference.getBatteryCareEnabled(getApplicationContext());
    }

    /**
     * Method to update {@link #_chargingStart} with whether charging should start now
     * <p>
     * The formula for getting the right time to start is:
     * Get percentage of battery to charge: 100 - {@link #_batPct}
     * Some maths topping:
     *        85              120 (min)
     * ----------------   =   --------
     * {@link #_batPct}        x (min)
     * <p>
     * 85 comes from default battery charging based on assumption -> 15 to 100 % takes 2 hours
     */
    private void shouldChargingStart() {
        long milli = Preference.getExpectedFullChargeTime(getApplicationContext());

        Calendar old = Calendar.getInstance();
        old.setTimeInMillis(milli);

        // Get latest milli from pref
        Calendar n = Calendar.getInstance();
        n.set(Calendar.HOUR_OF_DAY, old.get(Calendar.HOUR_OF_DAY));
        n.set(Calendar.MINUTE, old.get(Calendar.MINUTE));

        milli = n.getTimeInMillis();
        if (milli < System.currentTimeMillis()) {
            Utils.log(TAG, "shouldChargingStart: milli was past", getApplicationContext());
            milli += 24 * 60 * 60 * 1000L;
        }

        float thresh = 100 - _batPct;
        float minToCharge = (thresh * mMinToCharge) / 85F;
        long milliToCharge = (long) (minToCharge * 60 * 1000L);

        _chargingStart = milli - System.currentTimeMillis() <= milliToCharge;
        Utils.log(TAG, "shouldChargingStart: " + _chargingStart, getApplicationContext());
        if (_chargingStart) {
            Utils.log(TAG, "shouldChargingStart: charging started at " + Utils.getTimeString(System.currentTimeMillis(), getApplicationContext()), getApplicationContext());
            enableCharging();
        }
    }

    /**
     * Method name explains itself
     */
    private void updateBatteryPercentage(@Nullable Intent intent) {
        Intent batteryStatus;
        if (intent == null) {
            batteryStatus = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        } else {
            batteryStatus = intent;
        }

        if (batteryStatus != null) {
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            _batPct = level * 100 / (float) scale;
        }
    }

    /**
     * Method name explains itself
     */
    private boolean isCharging() {
        Intent batteryStatus = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        if (batteryStatus != null) {
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean charging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;

            Utils.log(TAG, "isCharging: " + charging, getApplicationContext());
            return charging;
        } else {
            return false;
        }
    }

    /**
     * Method name explains itself
     */
    private void enableCharging() {
        // Check last lines in init.yoshino.rc
        SystemProperties.set("persist.sys.yoshino.battery.care", "1");
    }

    /**
     * Method name explains itself
     */
    private void disableCharging() {
        // Disable charging after 30 seconds after call
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                SystemProperties.set("persist.sys.yoshino.battery.care", "0");
            }
        }, 30 * 1000L);
    }
}
