package com.bct.fuelpay.view.activity;

import static com.bct.fuelpay.utils.FragmentUtils.TRANSITION_NONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basewin.services.ServiceManager;
import com.bct.fuelpay.R;
import com.bct.fuelpay.utils.FragmentUtils;
import com.bct.fuelpay.view.fragment.CartFragment;
import com.bct.fuelpay.view.fragment.HomeFragment;
import com.bct.fuelpay.view.fragment.ReceiptFragment;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class HomeActivity extends AppCompatActivity implements  EasyPermissions.PermissionCallbacks{

    public static final int REQUEST_PERMISSION=0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        permission();

    }

    @AfterPermissionGranted(REQUEST_PERMISSION)
    private void permission(){
        String[] perms = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.SEND_SMS,
                "com.pos.permission.SECURITY",
                "com.pos.permission.ACCESSORY_DATETIME",
                "com.pos.permission.ACCESSORY_LED",
                "com.pos.permission.ACCESSORY_BEEP",
                "com.pos.permission.ACCESSORY_RFREGISTER",
                "com.pos.permission.CARD_READER_ICC",
                "com.pos.permission.CARD_READER_PICC",
                "com.pos.permission.CARD_READER_MAG",
                "com.pos.permission.COMMUNICATION",
                "com.pos.permission.PRINTER",
                "com.pos.permission.ACCESSORY_RFREGISTER",
                "com.pos.permission.EMVCORE"
        };
        if (EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this,"Already Permission",Toast.LENGTH_SHORT).show();
            ServiceManager.getInstence().init(getApplicationContext());
            FragmentUtils.replaceFragment(this,new HomeFragment(),
                    R.id.fragContainer, false, TRANSITION_NONE);
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                    new PermissionRequest
                            .Builder(this,REQUEST_PERMISSION,perms)
                            .setRationale("Dear users\n need to apply for storage Permissions for\n your better use of this application")
                            .setNegativeButtonText("NO")
                            .setPositiveButtonText("YES")
                            .build()
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Granted", "onRequestPermissionsResult:" + requestCode);
        if(requestCode == 1){
            //VIzpay
//            ServiceManager.getInstence().init(getApplicationContext());
            FragmentUtils.replaceFragment(this,new HomeFragment(),
                    R.id.fragContainer, false, TRANSITION_NONE);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.e("Granted", "onPermissionsGranted:" + requestCode + ":" + perms.toString());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.e("Denied", "onPermissionsDenied:" + requestCode + ":" + perms.toString());
        if (EasyPermissions.somePermissionPermanentlyDenied(HomeActivity.this, perms)) {
            new AppSettingsDialog
                    .Builder(this)
                    .setTitle("Tips")
                    .setRationale("Dear users, in order to make better use of this application, you need to apply for storage permissions.")
                    .setNegativeButton("Refuse")
                    .setPositiveButton("Go To Set")
                    .setRequestCode(0x001)
                    .build()
                    .show();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragContainer);
        if (f instanceof ReceiptFragment){
            FragmentManager fm = getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            FragmentUtils.replaceFragment(this,new HomeFragment(),
                    R.id.fragContainer, false, TRANSITION_NONE);
        }else if (f instanceof HomeFragment){
            showLogoutDialog();
        }else if (f instanceof CartFragment){
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().popBackStack();
        }else {
            getSupportFragmentManager().popBackStack();
        }
    }

    private void showLogoutDialog() {
        try {
            androidx.appcompat.app.AlertDialog.Builder builder =
                    new androidx.appcompat.app.AlertDialog.Builder(this, R.style.MyDialogTheme);
//

            TextView resultMessage = new TextView(this);
            resultMessage.setTextSize(15);
            resultMessage.setText("Do you want to Exit?");
            resultMessage.setTextColor(getResources().getColor(R.color.black));
            resultMessage.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 20, 0, 0);
            resultMessage.setPadding(0, 35, 0, 0);
            resultMessage.setLayoutParams(lp);
            builder.setView(resultMessage);
            builder.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    try {
                        finish();
                    } catch (Exception e) {
                    }
                }

            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            androidx.appcompat.app.AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
        }
    }
}