package com.bct.fuelpay.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.bct.fuelpay.GetPortBroadcastReceiver;
import com.bct.fuelpay.model.ActiveTxnData;

public class ECRUtils {

    private static GetPortBroadcastReceiver getPortBroadcastReceiver;

    public static void sendAndReceiveBroadcast(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.skyband.pos.app.send.ecr.port");
        // send ecr connection mode to payment app for its usage.
        intent.putExtra("ConnectionMode", "AppToApp(Intent)");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setComponent(
                new ComponentName("com.skyband.pos.app", "com.skyband.pos.app.ui.ecr.ECRPortBroadcastReceiver"));
        context.sendBroadcast(intent);
        getPortBroadcastReceiver = new GetPortBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter("com.skyband.pos.perform.port");
        context.registerReceiver(getPortBroadcastReceiver, intentFilter);
    }

    public static void unRegisterBroadcast(Context context) {
        if (getPortBroadcastReceiver != null) {
            context.unregisterReceiver(getPortBroadcastReceiver);
            getPortBroadcastReceiver = null;
        }
    }
}
