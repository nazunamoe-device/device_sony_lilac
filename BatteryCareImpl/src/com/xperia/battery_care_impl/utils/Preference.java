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

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {

    private static final String PREF_PKG = "BatteryCareImpl";

    public static final String BATTERY_CARE_ENABLE_TIME = "battery_care_enable_time";
    public static final String EXPECTED_FULL_CHARGE_TIME = "expected_full_charge_time";
    public static final String BATTERY_CARE_ENABLED = "battery_care_enabled";

    public static final long INVALID_TIME = 108L;

    private static SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences(PREF_PKG, Context.MODE_PRIVATE);
    }

    public static long getBatteryCareEnabledTime(Context context) {
        return getPreference(context).getLong(BATTERY_CARE_ENABLE_TIME, INVALID_TIME);
    }

    public static void setBatteryCareEnabledTime(long value, Context context) {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putLong(BATTERY_CARE_ENABLE_TIME, value);
        editor.apply();
    }

    public static long getExpectedFullChargeTime(Context context) {
        return getPreference(context).getLong(EXPECTED_FULL_CHARGE_TIME, INVALID_TIME);
    }

    public static void setExpectedFullChargeTime(long value, Context context) {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putLong(EXPECTED_FULL_CHARGE_TIME, value);
        editor.apply();
    }

    public static boolean getBatteryCareEnabled(Context context) {
        return getPreference(context).getBoolean(BATTERY_CARE_ENABLED, false);
    }

    public static void setBatteryCareEnabled(boolean value, Context context) {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putBoolean(BATTERY_CARE_ENABLED, value);
        editor.apply();
    }
}
