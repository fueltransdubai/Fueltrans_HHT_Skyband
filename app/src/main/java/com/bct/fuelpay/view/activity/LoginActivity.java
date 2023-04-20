package com.bct.fuelpay.view.activity;

import static com.bct.fuelpay.utils.FragmentUtils.TRANSITION_NONE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.bct.fuelpay.R;
import com.bct.fuelpay.utils.FragmentUtils;
import com.bct.fuelpay.view.fragment.LoginFragment;

import java.security.MessageDigest;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FragmentUtils.replaceFragment(this,new LoginFragment(),
                R.id.fragContainer, false, TRANSITION_NONE);
//        Log.e("Signature",getPackageSignature(this));
    }


    /*private String getPackageSignature(Context context) {
        // replace this with your package name
        String packageName = "com.bct.fuelpay";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                SigningInfo sig = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES).signingInfo;
                if (sig.hasMultipleSigners()) {
                    Signature[] apkContentsSigners = sig.getApkContentsSigners();
                    for (Signature s : apkContentsSigners) {
                        MessageDigest sha = MessageDigest.getInstance("SHA");
                        sha.update(s.toByteArray());
                        final String signatureBase64 = new String(Base64.encode(sha.digest(), Base64.DEFAULT));
                        return signatureBase64;
                    }
                } else {
                    Signature[] signingCertificateHistory = sig.getSigningCertificateHistory();
                    for (Signature s : signingCertificateHistory) {
                        MessageDigest sha = MessageDigest.getInstance("SHA");
                        sha.update(s.toByteArray());
                        final String signatureBase64 = new String(Base64.encode(sha.digest(), Base64.DEFAULT));
                        return signatureBase64;
                    }
                }
            } else {
                Signature[] sig = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures;
                for (Signature s : sig) {
                    MessageDigest sha = MessageDigest.getInstance("SHA");
                    sha.update(s.toByteArray());
                    final String signatureBase64 = new String(Base64.encode(sha.digest(), Base64.DEFAULT));
                    return signatureBase64;
                }
            }
        } catch (Exception e) {
            Log.e("Ex", " " + e.getMessage());
        }
        return null;
    }*/

//    private boolean checkPlayServices() {
//        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (apiAvailability.isUserResolvableError(resultCode)) {
//                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
//                        .show();
//            } else {
//                Log.i("TAG", "This device is not supported.");
//                finish();
//            }
//            return false;
//        }
//        return true;
//    }

}