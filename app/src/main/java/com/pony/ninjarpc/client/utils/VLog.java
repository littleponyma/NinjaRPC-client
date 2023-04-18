package com.pony.ninjarpc.client.utils;


import android.annotation.SuppressLint;
import android.text.TextUtils;

public class VLog {
    public static String TAG = "NinjaRPC";

    @SuppressLint("DefaultLocale")
    private static String generateTag() {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String tag = "%s.%s(Line:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(TAG) ? tag : TAG + ":" + tag;
        return tag;
    }

    public static void d(String msg) {
        String tag = generateTag();
        android.util.Log.d(tag, msg);
    }

    public static void e(String msg) {
        String tag = generateTag();
        android.util.Log.e(tag, msg);
    }

    public static void e(String msg, Exception e) {
        String tag = generateTag();
        android.util.Log.e(tag, msg, e);
    }

    public static void i(String msg) {
        String tag = generateTag();
        android.util.Log.i(tag, msg);
    }

    public static void w(String msg) {
        String tag = generateTag();
        android.util.Log.w(tag, msg);
    }

}
