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

package com.xperia.battery_care_impl.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.util.Log;

import com.xperia.battery_care_impl.BatteryCare;
import com.xperia.battery_care_impl.utils.Preference;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            if (action.equals(Intent.ACTION_BOOT_COMPLETED) || action.equals("android.intent.action.QUICKBOOT_POWERON")) {
                Log.d(TAG, "onReceive: Boot Received");
                context.startServiceAsUser(new Intent(context, BatteryCare.class), UserHandle.CURRENT);

                // Sync pref to main App no matter what
                context.sendBroadcast(new Intent().setAction("com.xperia.battery_care.SYNC_PREF")
                        .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                        .putExtra(Preference.BATTERY_CARE_ENABLE_TIME, Preference.getBatteryCareEnabledTime(context))
                        .putExtra(Preference.EXPECTED_FULL_CHARGE_TIME, Preference.getExpectedFullChargeTime(context))
                        .putExtra(Preference.BATTERY_CARE_ENABLED, Preference.getBatteryCareEnabled(context))
                        .setComponent(new ComponentName("com.xperia.battery_care", "com.xperia.battery_care.receiver.SyncPreference")));
            }
        }
    }
}
