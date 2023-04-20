package com.bct.fuelpay.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.basewin.aidl.OnBarcodeCallBack;
import com.basewin.services.ServiceManager;
import com.bct.fuelpay.R;
import com.bct.fuelpay.adapter.GridAdapter;
import com.bct.fuelpay.adapter.PumpAuthAdapter;
import com.bct.fuelpay.databinding.FragmentPumpAuthBinding;
import com.bct.fuelpay.utils.FragmentUtils;
import com.bct.fuelpay.view_model.PumpAuthViewModel;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pos.sdk.cardreader.PosCardReaderInfo;
import com.pos.sdk.cardreader.PosCardReaderManager;
import com.pos.sdk.cardreader.PosMifareCardReader;
import com.pos.sdk.cardreader.PosPiccCardReader;
import com.pos.sdk.utils.PosByteArray;
import com.pos.sdk.utils.PosUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class PumpAuthFragment extends Fragment implements PumpAuthAdapter.ItemListener {

    FragmentPumpAuthBinding binding;
    PumpAuthAdapter pumpAuthAdapter;
    Bundle b;
    private PosMifareCardReader mMifareCardReader = null;
    private final static String TAG = "PUMPAUTH";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_pump_auth,container,false);
        setUpWidget();

        return binding.getRoot();
    }

    private void setUpWidget(){
        b = new Bundle();
        b= getArguments();
        mMifareCardReader = PosCardReaderManager.getDefault(getActivity()).getMifareCardReader();


        ArrayList items = new ArrayList<>();
        items.add(new PumpAuthViewModel("Scan QR",R.drawable.ic_qr));
        items.add(new PumpAuthViewModel("NFC Card",R.drawable.ic_nfc));
        items.add(new PumpAuthViewModel("RFID Tag",R.drawable.ic_rfid));
        items.add(new PumpAuthViewModel("Manuel",R.drawable.ic_manuel));

        pumpAuthAdapter = new PumpAuthAdapter(getActivity(),items);
        binding.recylerview.setLayoutManager(new GridLayoutManager(getActivity(),2));
        binding.recylerview.setAdapter(pumpAuthAdapter);
        pumpAuthAdapter.setClickListener(this);

    }

    @Override
    public void onItemClick(View v, int position) {
        switch (position){
            case 0:
                openScanner();
                break;
            case 1:
                mMifareCardReader.open();
                detectMifareCard(60);
                break;
            case 2:
                break;
            case 3:
                FragmentUtils.replaceFragment((AppCompatActivity) getActivity(),new PumpFragment(),b,
                        R.id.fragContainer,true,FragmentUtils.TRANSITION_POP);
                break;
        }
    }

    private void openScanner() {
        try {
            ServiceManager.getInstence().init(getActivity().getApplicationContext());
            ServiceManager.getInstence().getScan().startScan(60, new OnBarcodeCallBack() {
                @Override
                public void onScanResult(String s) {
                    Log.e("OnScanResult",""+s);
                    FragmentUtils.replaceFragment((AppCompatActivity) getActivity(),new PumpFragment(),b,
                            R.id.fragContainer,true,FragmentUtils.TRANSITION_POP);
                }

                @Override
                public void onFinish(int code,String msg) {
                    Log.e("Finish",""+msg);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void detectMifareCard(int timeout){
        Long t1 = System.currentTimeMillis();
        Long t2 ;
        while (true) {
            t2 = System.currentTimeMillis();
            if(t2 - t1 > timeout*1000){
                Log.d(TAG, "TimeOut");
                mMifareCardReader.close();
                break;
            }

            if (mMifareCardReader != null) {
                Log.d(TAG, "detectMifareCard PosMifareCardReader");
                //CARD_TYPE_MIFARE_ULTRALIGHT
                Log.d(TAG, "Set CARD_TYPE_MIFARE_ULTRALIGHT");
                mMifareCardReader.setCardType(PosMifareCardReader.CARD_TYPE_MIFARE_ULTRALIGHT);
                if (mMifareCardReader.detect() == 0) {
                    Log.d(TAG, "find mifare ULTRALIGHT **********************");
                    Log.e(TAG,""+Arrays.copyOf(mMifareCardReader.getCardReaderInfo().mSerialNum, mMifareCardReader.getCardReaderInfo().mSerialNum.length));
                    Log.e(TAG,""+bytesToHexString(mMifareCardReader.getCardReaderInfo().mSerialNum));

                    //                    mHandler.sendMessage(mHandler.obtainMessage(PosMifareCardReader.CARD_TYPE_MIFARE_ULTRALIGHT, mMifareCardReader));
                    break;

                }

                //CARD_TYPE_MIFARE_CLASSIC
                Log.d(TAG, "Set CARD_TYPE_MIFARE_CLASSIC");
                mMifareCardReader.setCardType(PosMifareCardReader.CARD_TYPE_MIFARE_CLASSIC);

                if (mMifareCardReader.detect() == 0) {

                    Log.d(TAG, "find mifare classic **********************");
                    Log.e(TAG,""+bytesToHexString(mMifareCardReader.getCardReaderInfo().mSerialNum));
//                    Arrays.copyOf(mMifareCardReader.getCardReaderInfo().mSerialNum, mMifareCardReader.getCardReaderInfo().mSerialNum.length);
//                    mHandler.sendMessage(mHandler.obtainMessage(PosMifareCardReader.CARD_TYPE_MIFARE_CLASSIC, mMifareCardReader));
                    break;

                }

                //CARD_TYPE_MIFARE_PLUS
                Log.d(TAG, "Set CARD_TYPE_MIFARE_PLUS");
                mMifareCardReader.setCardType(PosMifareCardReader.CARD_TYPE_MIFARE_PLUS);
                if (mMifareCardReader.detect() == 0) {
                    Log.d(TAG, "find mifare plus **********************");
//                    mHandler.sendMessage(mHandler.obtainMessage(PosMifareCardReader.CARD_TYPE_MIFARE_PLUS, mMifareCardReader));
                    break;

                }

                //CARD_TYPE_MIFARE_DESFIRE
                Log.d(TAG, "Set CARD_TYPE_MIFARE_DESFIRE");
                mMifareCardReader.setCardType(PosMifareCardReader.CARD_TYPE_MIFARE_DESFIRE);
                if (mMifareCardReader.detect() == 0) {

                    Log.d(TAG, "find mifare desfire **********************");
//                    mHandler.sendMessage(mHandler.obtainMessage(PosMifareCardReader.CARD_TYPE_MIFARE_DESFIRE, mMifareCardReader));
                    break;
                }
            }
        }

    }

    public static String bytesToHexString(byte[] bytes, int offset, int len) {
        if (bytes == null) {
            return "null!";
        } else {
            StringBuilder ret = new StringBuilder(2 * len);

            for(int i = 0; i < len; ++i) {
                int b = 15 & bytes[offset + i] >> 4;
                ret.append("0123456789abcdef".charAt(b));
                b = 15 & bytes[offset + i];
                ret.append("0123456789abcdef".charAt(b));
            }

            return ret.toString();
        }
    }

    public static String bytesToHexString(byte[] bytes, int len) {
        return bytes == null ? "null!" : bytesToHexString(bytes, 0, len);
    }

    public static String bytesToHexString(byte[] bytes) {
        return bytes == null ? "null!" : bytesToHexString(bytes, bytes.length);
    }


}