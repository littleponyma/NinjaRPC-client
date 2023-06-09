package com.pony.ninjarpc.aweme.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import java.nio.charset.StandardCharsets;


/**
 * Create by 3kyo0 on 2021/10/19.
 * 获取设备imei,androidid,手机号 2022/09/01
 */
public class DeviceUtil {
    @SuppressLint("HardwareIds")
    public static String serialNumber() {
        ShellUtil.CommandResult commandResult = ShellUtil.execCommand("getprop ro.serialno", ShellUtil.checkRootPermission());
        return new String(commandResult.successMsg.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 获取设备android_id
     *
     * @param mContext 上下文
     * @return android_id
     */
    public static String getAndroidId(Context mContext) {
        try {
            return Settings.System.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
