package com.bct.fuelpay.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bct.fuelpay.BuildConfig;
import com.bct.fuelpay.R;
import com.bct.fuelpay.cache.GeneralParamCache;
import com.bct.fuelpay.constant.AppConstant;
import com.bct.fuelpay.model.ActiveTxnData;
import com.bct.fuelpay.service.MyReceiver;
import com.bct.fuelpay.utils.ECRUtils;
import com.bct.fuelpay.utils.FragmentUtils;
import com.skyband.ecr.sdk.CLibraryLoad;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppToAppFragment extends Fragment {

    View rootView;
    Bundle b;
    String amount;
    private String ecrReferenceNo = "12345678000001";
    private String reqData = "";
    private String szSignature = "";
    private String date = "";
    int transactionType = 0;
    private String terminalResponseString = "";
    private String terminalID = "";

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> {

            Log.e("terminalResponse",""+ActiveTxnData.getInstance().getReceivedIntentData());
            if (ActiveTxnData.getInstance().getReceivedIntentData() != null) {
                String terminalResponse = null;
                try {
                    terminalResponse = ActiveTxnData.getInstance().getReceivedIntentData();
                    handleTerminalResponse(terminalResponse);
                    ActiveTxnData.getInstance().setResData(terminalResponse);
                    FragmentUtils.replaceFragment((AppCompatActivity) getActivity(), new ReceiptFragment(), b,
                            R.id.fragContainer, true, FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        },1000);

        ECRUtils.sendAndReceiveBroadcast(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_app_to_app, container, false);

        b = new Bundle();
        b = getArguments();
        amount = b.getString("Amount");

        if (appInstalledOrNot()) {
            try {
                byte[] packData = getPackData(amount);
                Log.e("packData", "" + packData);
                Intent intent = requireActivity().getPackageManager().getLaunchIntentForPackage("com.skyband.pos.app");
                intent.putExtra("message", "ecr-txn-event");
                intent.putExtra("request", packData);
                intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
                startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            Log.e("Exception","CANT LAUNCH MADA APP");
        }




        return rootView;
    }

    private void initialiseData(String amount) throws NoSuchAlgorithmException {

        String combinedValue;

        date = new SimpleDateFormat("ddMMyyhhmmss", Locale.getDefault()).format(new Date());

        GeneralParamCache.getInstance().putString(AppConstant.PREV_ECR_NO, ecrReferenceNo.substring(ecrReferenceNo.length() - 6));

        combinedValue = ecrReferenceNo.substring(ecrReferenceNo.length() - 6) + ActiveTxnData.getInstance().getTerminalID();
        Log.e("Amount>>", "" + amount);
        Log.e("::Combined value>>", "" + combinedValue);
        Log.e("::ECR Ref No. Length>>", "" + ecrReferenceNo.length());
//        szSignature = computeSha256Hash(combinedValue);
        szSignature = "0000000000000000000000000000000000000000000000000000000000000000";
        reqData = date + ";" + (int) (Double.parseDouble(String.valueOf(amount)) * 100) + ";" + 0 + ";" + ecrReferenceNo + "!";
        ActiveTxnData.getInstance().setSzSignature(szSignature);

        int ecrNumber = Integer.parseInt(GeneralParamCache.getInstance().getString(AppConstant.ECR_NUMBER)) + 1;
        GeneralParamCache.getInstance().putString(AppConstant.ECR_NUMBER, String.format("%06d", ecrNumber));

    }

    public byte[] getPackData(String amount) throws Exception {

        initialiseData(amount);
        Log.e("reqData", "" + reqData);
        Log.e("transactionType", "" + transactionType);
        Log.e("szSignature", "" + szSignature);
        return CLibraryLoad.getInstance().getPackData(reqData, transactionType, szSignature);
    }

    public void handleTerminalResponse(String terminalResponse) throws Exception {

        terminalResponseString = terminalResponse;
        String[] responseArray = terminalResponseString.split(";");
        String[] splittedResponse;
        String thirdResponse;
        Log.d("FirstApicall length>> ","" + responseArray.length);

        String resp1 = arrayIntoString(responseArray);
        Log.d("Response ArrayTo String","" + resp1);


        splittedResponse = terminalResponseString.split(";");

        if (splittedResponse[1].equals("C2")) {
            String[] separateResponse = new String[responseArray.length - 4];
            int j = 0;
            for (int i = 4; i < responseArray.length - 4; i++) {
                separateResponse[j] = responseArray[i];
                j = j + 1;
            }
            splittedResponse = separateResponse;
        }

        String[] separateResponse = new String[splittedResponse.length - 1];
        int j = 0;
        for (int i = 1; i < responseArray.length - 1; i++) {
            separateResponse[j] = responseArray[i];
            j = j + 1;
        }

        terminalResponseString = arrayIntoString(separateResponse);
        splittedResponse = terminalResponseString.split(";");

        if (splittedResponse[0].equals("C1")) {
            splittedResponse[0] = "22";
        }

        ActiveTxnData.getInstance().setSummaryReportArray(splittedResponse);


        Log.d(getClass() + "::Terminal ID>>","" + terminalID);

    }

    public String arrayIntoString(String[] resp) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < resp.length; i++) {
            sb.append(resp[i]);
            sb.append(";");
        }
        return sb.toString();
    }

    private void showMessageDialog(Context mContext, String message) {
        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(mContext, R.style.MyDialogTheme);
//
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView resultMessage = new TextView(mContext);
        resultMessage.setTextSize(15);
        resultMessage.setText(message);
        resultMessage.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 20, 0, 0);
        resultMessage.setPadding(0, 55, 0, 35);
        resultMessage.setLayoutParams(lp);
        builder.setView(resultMessage);
        layout.addView(resultMessage);


        View view = new View(mContext);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                1);
        view.setBackgroundColor(mContext.getResources().getColor(R.color.grey_editext));
        view.setLayoutParams(lp1);
        view.setPadding(0, 0, 0, 0);
        layout.addView(view);
        builder.setView(layout);

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        androidx.appcompat.app.AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.show();

        final Button positiveButton = alert.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
//        positiveButton.setBackground(null);
        positiveButton.setTextColor(mContext.getResources().getColor(R.color.grey_editext));

        positiveButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
        positiveButton.setLayoutParams(positiveButtonLL);
    }

    private boolean appInstalledOrNot() {
        PackageManager pm = requireActivity().getPackageManager();
        try {
            pm.getPackageInfo("com.skyband.pos.app", PackageManager.GET_ACTIVITIES);
//            logger.info("App Installed");
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getEcrNumberString() {
        // Random rnd = new Random();
        // int number = rnd.nextInt(999999);
        int number = 1;
        // this will convert any number sequence into 6 character.
        Log.e("Ecr No Generated>>>" ,""+ String.format("%06d", number));
        return String.format("%06d", number);
    }

}