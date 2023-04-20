package com.bct.fuelpay.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bct.fuelpay.BuildConfig;
import com.bct.fuelpay.R;
import com.bct.fuelpay.adapter.CartItemAdapter;
import com.bct.fuelpay.cache.GeneralParamCache;
import com.bct.fuelpay.constant.AppConstant;
import com.bct.fuelpay.databinding.FragmentCartBinding;
import com.bct.fuelpay.model.ActiveTxnData;
import com.bct.fuelpay.model.TransactionType;
import com.bct.fuelpay.service.MyReceiver;
import com.bct.fuelpay.utils.ECRUtils;
import com.bct.fuelpay.utils.FragmentUtils;
import com.bct.fuelpay.view_model.CartItemViewModel;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    FragmentCartBinding binding;
    CartItemViewModel cartItemViewModel;
    Bundle b;
    String name,quantity,amount;
    ArrayList<CartItemViewModel> arrayList;
    CartItemAdapter cartItemAdapter;
    IntentFilter filter;
    MyReceiver myReceiver;
    private BroadcastReceiver surepayResult;

    @Override
    public void onStart() {
        super.onStart();
        if (GeneralParamCache.getInstance().getString(AppConstant.ECR_NUMBER) == null) {
            GeneralParamCache.getInstance().putString(AppConstant.ECR_NUMBER, getEcrNumberString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cart,container,false);
        setUpWidget();

        return binding.getRoot();
    }

    private void setUpWidget(){

        cartItemViewModel = ViewModelProviders.of(this).get(CartItemViewModel.class);
        binding.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        b = new Bundle();
        b = getArguments();
        name = b.getString("product");
        quantity = b.getString("volume");
        amount = b.getString("Amount");
        arrayList = new ArrayList<>();
        arrayList.add(new CartItemViewModel(View.VISIBLE,View.INVISIBLE,name,quantity,amount));

        binding.tvTotalItem.setText(arrayList.size()+" items");
        binding.tvTotal.setText(requireActivity().getString(R.string.currency)+" "+arrayList.get(0).amount);


        binding.btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtils.replaceFragment((AppCompatActivity) getActivity(), new AppToAppFragment(), b,
                        R.id.fragContainer, true, FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);
            }
        });

        cartItemAdapter = new CartItemAdapter(getActivity(),arrayList);
        binding.recylerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recylerview.setAdapter(cartItemAdapter);


    }

    @Override
    public void onResume() {
        super.onResume();



    }

    @Override
    public void onPause() {
        super.onPause();
//        ECRUtils.unRegisterBroadcast(getActivity());
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


    private boolean isMadaAppInstalled() {
        PackageManager pm = getActivity().getPackageManager();
        try {
            pm.getPackageInfo("com.surepay.mada", PackageManager.GET_ACTIVITIES); return true;
        } catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

}