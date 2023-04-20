package com.bct.fuelpay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bct.fuelpay.cache.GeneralParamCache;
import com.bct.fuelpay.constant.AppConstant;
import com.bct.fuelpay.model.ActiveTxnData;


public class GetPortBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String portNo = intent.getStringExtra("PortNo");
        String connectionType = intent.getStringExtra("ConnectionType");
        // Receive and save payment app response
        byte[] receivedDataByte = intent.getByteArrayExtra("app-to-app-response");
        if (receivedDataByte != null && receivedDataByte.length > 0) {
//            Log.e("GetPortBrod RES",""+ new String(receivedDataByte));
            String receivedIntentData = new String(receivedDataByte);
            Log.e("Received ECR Response",""+ receivedIntentData);
            receivedIntentData = receivedIntentData.replace("�", ";");
            ActiveTxnData.getInstance().setReceivedIntentData(receivedIntentData);

        }
        System.out.println("Getting Port No" + portNo);
        System.out.println("Getting Connection Type" + connectionType);
        GeneralParamCache.getInstance().putString(AppConstant.PORT,portNo);
        GeneralParamCache.getInstance().putString(AppConstant.MADA_CONNECTION_TYPE,connectionType);

    }
}