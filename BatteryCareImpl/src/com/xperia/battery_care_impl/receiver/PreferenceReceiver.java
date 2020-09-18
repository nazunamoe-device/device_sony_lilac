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

import com.xperia.battery_care_impl.utils.Preference;
import com.xperia.battery_care_impl.utils.Utils;

public class PreferenceReceiver extends BroadcastReceiver {

    private static final String TAG = "PreferenceReceiver";

    private static final String PREFERENCE = "com.xperia.battery_care.UPDATE_PREFERENCE";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            if (action.equals(PREFERENCE)) {
                if (intent.getExtras() != null) {
                    String key = intent.getExtras().getString("key", "#&#");

                    if (key.equals(Preference.BATTERY_CARE_ENABLE_TIME)) {
                        long value = intent.getExtras().getLong("val", 108L);
                        Preference.setBatteryCareEnabledTime(value, context);

                        Utils.log(TAG, "onReceive: Preference received; key = " + key + ", val = " + value, context);
                    }
                    if (key.equals(Preference.EXPECTED_FULL_CHARGE_TIME)) {
                        long value = intent.getExtras().getLong("val", 108L);
                        Preference.setExpectedFullChargeTime(value, context);

                        Utils.log(TAG, "onReceive: Preference received; key = " + key + ", val = " + value, context);
                    }
                    if (key.equals(Preference.BATTERY_CARE_ENABLED)) {
                        boolean value = intent.getExtras().getBoolean("val", false);
                        Preference.setBatteryCareEnabled(value, context);

                        Utils.log(TAG, "onReceive: Preference received; key = " + key + ", val = " + value, context);
                    }
                }
            }
        }
    }
}
