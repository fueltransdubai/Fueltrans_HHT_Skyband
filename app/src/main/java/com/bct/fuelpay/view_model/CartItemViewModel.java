package com.bct.fuelpay.view_model;


import android.bluetooth.BluetoothDevice;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.bct.fuelpay.cache.GeneralParamCache;
import com.bct.fuelpay.constant.AppConstant;
import com.bct.fuelpay.model.ActiveTxnData;
import com.skyband.ecr.sdk.CLibraryLoad;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CartItemViewModel extends ViewModel {

    public int wetStock;
    public int dryStock;
    public String name;
    public String quantity;
    public String amount;




    public CartItemViewModel() {
    }


    public CartItemViewModel(int wetStock, int dryStock, String name, String quantity, String amount) {
        this.wetStock = wetStock;
        this.dryStock = dryStock;
        this.name = name;
        this.quantity = quantity;
        this.amount = amount;
    }





    public String computeSha256Hash(String combinedValue) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashInBytes = md.digest(combinedValue.getBytes());

        StringBuilder sb = new StringBuilder();

        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }

        String resultSHA = sb.toString();

        return resultSHA;
    }


}
