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
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.xperia.battery_care_impl.BatteryCare;
import com.xperia.battery_care_impl.utils.NotificationHelper;

public class CancelBatteryCare extends BroadcastReceiver {

    private static final String TAG = "CancelBatteryCare";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: mBatteryCareAllowed set FALSE");
        BatteryCare.mBatteryCareAllowed = false;

        NotificationHelper helper = new NotificationHelper(context);
        helper.getManager().cancel(1);

        Toast.makeText(context, "Switched to Normal charging", Toast.LENGTH_SHORT).show();
    }
}
