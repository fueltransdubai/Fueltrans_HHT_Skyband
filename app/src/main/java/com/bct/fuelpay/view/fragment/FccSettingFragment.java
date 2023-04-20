package com.bct.fuelpay.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bct.fuelpay.R;
import com.bct.fuelpay.constant.AppConstant;
import com.bct.fuelpay.databinding.FragmentFccSettingBinding;
import com.bct.fuelpay.utils.SavePrefs;


public class FccSettingFragment extends Fragment implements View.OnClickListener {

    FragmentFccSettingBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_fcc_setting,container,false);
        setUpWidgets();

        return binding.getRoot();
    }

    private void setUpWidgets(){

        String strServerIp = SavePrefs.getString(AppConstant.prefName, getActivity(), AppConstant.ipPref, null);

        String strServerPort = SavePrefs.getString(AppConstant.prefName, getActivity(), AppConstant.portPref, null);

        String strServerUsername = SavePrefs.getString(AppConstant.prefName, getActivity(), AppConstant.userNamePref, null);

        String strServerPassword = SavePrefs.getString(AppConstant.prefName, getActivity(), AppConstant.passwordPref, null);


        try {
            if (strServerIp != null) {
                binding.etServerIp.setText(strServerIp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (strServerPort != null) {
                binding.etPort.setText(strServerPort);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (strServerUsername != null) {
                binding.etUsername.setText(strServerUsername);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (strServerPassword != null) {
                binding.etPassword.setText(strServerPassword);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.btnSubmit.setOnClickListener(this);
        binding.tvTitle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String MESSAGE;
        switch (view.getId()){
            case R.id.btnSubmit:
                if (binding.etServerIp.getText().toString().trim().isEmpty()) {
                    MESSAGE = "Enter Server IP address.";
                    AppConstant.showMessageDialog(getActivity(), MESSAGE);

                } else if (binding.etPort.getText().toString().trim().isEmpty()) {
                    MESSAGE = "Enter Server Port.";
                    AppConstant.showMessageDialog(getActivity(), MESSAGE);

                } else if (binding.etUsername.getText().toString().isEmpty()) {
                    MESSAGE = "Enter Server Username.";
                    AppConstant.showMessageDialog(getActivity(), MESSAGE);

                } else if (binding.etPassword.getText().toString().isEmpty()) {
                    MESSAGE = "Enter Server Password.";
                    AppConstant.showMessageDialog(getActivity(), MESSAGE);

                } else {

                    showRegistrationSuccessDialog();

                }
                break;

            case R.id.tvTitle:
                getActivity().getSupportFragmentManager().popBackStack();
                break;

        }
    }

    void showRegistrationSuccessDialog() {
        SavePrefs.saveString(AppConstant.prefName,
                getActivity(), AppConstant.ipPref, binding.etServerIp.getText().toString().trim());
        SavePrefs.saveString(AppConstant.prefName,
                getActivity(), AppConstant.portPref, binding.etPort.getText().toString().trim());
        SavePrefs.saveString(AppConstant.prefName,
                getActivity(), AppConstant.userNamePref, binding.etUsername.getText().toString().trim());
        SavePrefs.saveString(AppConstant.prefName,
                getActivity(), AppConstant.passwordPref, binding.etPassword.getText().toString().trim());



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);

        TextView msg = new TextView(getActivity());
        msg.setText("Merchant configured successfully");
        msg.setPadding(10, 35, 10, 10);
        msg.setGravity(Gravity.CENTER);
        msg.setTextSize(15);
        builder.setView(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }

}