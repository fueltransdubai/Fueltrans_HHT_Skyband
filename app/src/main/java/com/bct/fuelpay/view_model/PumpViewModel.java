package com.bct.fuelpay.view_model;

import static com.bct.fuelpay.utils.FragmentUtils.TRANSITION_NONE;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import com.bct.fuelpay.R;
import com.bct.fuelpay.constant.AppConstant;
import com.bct.fuelpay.utils.FragmentUtils;
import com.bct.fuelpay.utils.SavePrefs;
import com.bct.fuelpay.view.fragment.FccSettingFragment;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class PumpViewModel extends ViewModel {

    public String title;
    public int color;
    private Context context;



    public PumpViewModel() {

    }

    public PumpViewModel(Context mcontext, String title, int color) {
        this.context = mcontext;
        this.title = title;
        this.color = color;
    }




}
