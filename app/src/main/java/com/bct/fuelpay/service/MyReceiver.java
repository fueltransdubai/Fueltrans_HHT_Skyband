package com.bct.fuelpay.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean hasResponse = intent.getExtras().getBoolean("hasResponseReceipt");
        String result = intent.getExtras().getString("RESULT");
        Log.e("RESULT", "==================> " + result);

        if (hasResponse) {
//            MadaResponseModel topic = new Gson().fromJson(result, MadaResponseModel.class);
            if (result != null) {
                Intent i = new Intent("android.intent.action.MAIN").
                        putExtra("some_msg", result);
                context.sendBroadcast(i);

            }
        }
    }


}
