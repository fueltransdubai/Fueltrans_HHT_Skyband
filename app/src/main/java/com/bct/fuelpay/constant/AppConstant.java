package com.bct.fuelpay.constant;

import static android.content.Context.WIFI_SERVICE;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.format.Formatter;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bct.fuelpay.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AppConstant {

    public static String BASE_URL = "";
    public static String mobilePref = "MOBILE_NUMBER";
    public static String ipPref = "IP";
    public static String portPref = "PORT";
    public static String userNamePref = "USERNAME";
    public static String passwordPref = "PASSWORD";

    public static String whichMenu = "WHICHMENU";
    public static String fuelSale = "FUELSALE";
    public static String pumpAuthorize = "PUMPAUTHORIZE";
    public static String cStore = "CSTORE";
    public static String customer = "CUSTOMER";
    public static String transaction = "TRANSACTION";

    public static final String PREV_ECR_NO = "prevECRNO";
    public static final String APPLICATION_NAME = "AppName";
    public static final String SOFTWARE_VERSION = "AppVersion";
    public static final String PROVIDER_ID = "Provider";
    public static final String ECR_NUMBER = "EcrNumber";
    public static final String MADA_CONNECTION_TYPE = "ConnectionType";
    public static final String PORT = "Port";


    public static String user = "USER";
    public static String merchant = "MERCHANT";
    public static String token = "TOKEN";
    public static String products = "PRODUCTS";
    public static String prefName = "USERPREF";
    public static String category = "CATEGORY";
    public static String preference = "Preference";
    public static String firstName = "FIRSTNAME";
    public static String lastName = "LASTNAME";
    public static String mobile = "MOBILE";
    public static String vehicle = "VEHICLE";

    public static String siteId = "SITEID";
    public static String roleId = "ROLEID";
    public static String userID = "USERID";


    public static String generateSecureRandom() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static boolean isValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static void appendLog(String text)
    {
        File logFile = new File("sdcard/log.file");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void showMessageDialog(final Context mContext, final String message) {
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

    public static String addCommasToNumericString(String digits) {
        String result = "";
        String splitDigit;
        String temp;
        if (digits.contains(".")) {
            splitDigit = digits.split("\\.")[0];
            temp = "." + digits.split("\\.")[1];
        } else {
            splitDigit = digits;
            temp = "";
        }
        int len = splitDigit.length();
        int nDigits = 0;


        for (int i = len - 1; i >= 0; i--) {
            result = splitDigit.charAt(i) + result;
            nDigits++;
            if (((nDigits % 3) == 0) && (i > 0)) {
                result = "," + result;
            }
        }
        return (result + temp);
    }

    public static String GetDeviceipMobileData() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {

        }
        return null;
    }

    public static String GetDeviceipWiFiData(Context c) {

        WifiManager wm = (WifiManager) c.getSystemService(WIFI_SERVICE);

        @SuppressWarnings("deprecation")

        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        return ip;

    }

    public static void hideKeyboard(Context mContext, AppCompatActivity activity, View view) {

        try {
            if (activity != null) {

                if (view != null) {
                    try {
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Service.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }

            }
        } catch (Exception e) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    public static String getRoundOffValue(String value) {
        double number = Double.parseDouble(value);
//		DecimalFormat df = new DecimalFormat("###,###,###,###,###.##");
//		DecimalFormat df = new DecimalFormat("#0.00");


        java.text.DecimalFormat df = new DecimalFormat("###,###,###,###,###.00");
        try {
            String strValue = String.valueOf(df.format(number));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(df.format(number));

    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}
