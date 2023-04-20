package com.bct.fuelpay.view.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bct.fuelpay.R;
import com.bct.fuelpay.databinding.FragmentAddCustomerBinding;

public class AddCustomerFragment extends Fragment {

    FragmentAddCustomerBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_customer,container,false);


        return binding.getRoot();
    }

    private void setUpWidgets(){

    }

}