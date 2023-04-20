package com.bct.fuelpay.view.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bct.fuelpay.R;
import com.bct.fuelpay.adapter.SettingAdapter;
import com.bct.fuelpay.databinding.FragmentSettingBinding;
import com.bct.fuelpay.utils.FragmentUtils;
import com.bct.fuelpay.view_model.SettingViewModel;

import java.util.ArrayList;

public class SettingFragment extends Fragment implements SettingAdapter.ItemListener {

    FragmentSettingBinding binding;
    SettingAdapter settingAdapter;
    SettingViewModel settingViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_setting,container,false);
        binding.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        setupListView();

        return binding.getRoot();
    }

    private void setupListView(){
        ArrayList items = new ArrayList<>();
        items.add(new SettingViewModel("FCC Configurations",R.drawable.ic_printer));
        items.add(new SettingViewModel("Printer Settings",R.drawable.ic_printer));
        items.add(new SettingViewModel("Contact Us",R.drawable.ic_contact_support));
        items.add(new SettingViewModel("Privacy Policy",R.drawable.ic_privacy));


        settingAdapter = new SettingAdapter(getActivity(),items);
        binding.recylerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recylerview.setAdapter(settingAdapter);
        settingAdapter.setClickListener(this);



    }

    @Override
    public void onSettingClick(View v, int position) {

        switch (position){
            case 0:
                FragmentUtils.replaceFragment((AppCompatActivity) getActivity(),new FccSettingFragment(),
                        R.id.fragContainer,true,FragmentUtils.TRANSITION_NONE);
                break;
        }


    }
}