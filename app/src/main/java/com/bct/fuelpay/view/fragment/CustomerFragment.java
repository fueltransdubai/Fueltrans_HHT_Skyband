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
import com.bct.fuelpay.adapter.CustomerAdapter;
import com.bct.fuelpay.databinding.FragmentCustomerBinding;
import com.bct.fuelpay.utils.FragmentUtils;
import com.bct.fuelpay.view_model.CustomerViewModel;

import java.util.ArrayList;

public class CustomerFragment extends Fragment implements CustomerAdapter.ItemListener {

    FragmentCustomerBinding binding;
    CustomerAdapter customerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_customer,container,false);
        setUpWidgets();

        return binding.getRoot();
    }

    private void setUpWidgets(){
        ArrayList items = new ArrayList<>();
        items.add(new CustomerViewModel("Adison Botosh","(406) 555-0923"));
        items.add(new CustomerViewModel("Alenia Franci","(406) 555-3223"));
        items.add(new CustomerViewModel("Zaira Kanter","(406) 555-0127"));
        items.add(new CustomerViewModel("Talan Botosh","(406) 555-0923"));
        items.add(new CustomerViewModel("Lincon Septimus","(406) 555-0923"));
        items.add(new CustomerViewModel("Adison Botosh","(406) 555-0923"));

        customerAdapter = new CustomerAdapter(getActivity(),items);
        binding.recylerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recylerview.setAdapter(customerAdapter);
        customerAdapter.setClickListener(this);

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtils.replaceFragment((AppCompatActivity) getActivity(),new AddCustomerFragment(),
                        R.id.fragContainer,true,FragmentUtils.TRANSITION_NONE);
            }
        });

    }

    @Override
    public void onCustomerClick(View v, int position) {

    }
}