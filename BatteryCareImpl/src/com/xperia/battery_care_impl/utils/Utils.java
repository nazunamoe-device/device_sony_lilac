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
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

public class Utils {

    public static String getTimeString(long time, Context context) {
        String usual = DateFormat.getTimeInstance(DateFormat.SHORT).format(time);
        if (android.text.format.DateFormat.is24HourFormat(context)) {
            try {
                usual = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new SimpleDateFormat("hh:mm aa", Locale.getDefault()).parse(usual));
            } catch (ParseException e) {
                e.printStackTrace();
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(time);
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int min = c.get(Calendar.MINUTE);
                usual = (hour < 10 ? "0" + hour : "" + hour) + ":" + (min < 10 ? "0" + min : "" + min);
            }
        }

        return usual;
    }

    private static final String LOG_FILE = "/data/data/com.xperia.battery_care/files/log.log";

    public static void log(String tag, String msg, Context context) {
        Log.d(tag, msg);
        File file = new File(LOG_FILE);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, true);
            String log = getTimeString(System.currentTimeMillis(), context) + " [Impl] " + tag + ": " + msg + "\r\n";
            fos.write(log.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
