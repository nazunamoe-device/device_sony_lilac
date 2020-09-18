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

package com.xperia.battery_care_impl.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.xperia.battery_care_impl.R;
import com.xperia.battery_care_impl.receiver.CancelBatteryCare;

public class NotificationHelper extends ContextWrapper {

    public NotificationHelper(Context base) {
        super(base);
        createChannel();
    }

    public static final String CHANNEL_ID = "Battery Care";

    private static volatile NotificationManager mManager = null;

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public void createChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH);
        channel.setLightColor(getColor(R.color.colorAccent));
        channel.setSound(null, null);
        channel.enableVibration(true);

        getManager().createNotificationChannel(channel);
    }

    public Notification batteryCareNotification() {
        Intent launch = getPackageManager().getLaunchIntentForPackage("com.xperia.battery_care");
        if (launch != null) {
            launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        PendingIntent open = PendingIntent.getActivity(getApplicationContext(), -1, launch, 0);

        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle("Battery Care")
                .setSmallIcon(R.drawable.ic_baseline_battery_charging_full_24)
                .setColorized(true)
                .setContentText("Battery Care service has been started as per schedule.\nIf you wish to switch back to normal charging, tap \"Switch\" below")
                .setStyle(new NotificationCompat.BigTextStyle())
                .setColor(getColor(R.color.colorAccent))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setSound(null)
                .setContentIntent(open)
                .addAction(R.drawable.ic_baseline_battery_charging_full_24,
                        "Switch",
                        PendingIntent.getBroadcast(getApplicationContext(), 1, new Intent(getApplicationContext(), CancelBatteryCare.class), PendingIntent.FLAG_UPDATE_CURRENT))
                .build();
    }
}
