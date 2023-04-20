package com.bct.fuelpay.view.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bct.fuelpay.R;
import com.bct.fuelpay.adapter.GridAdapter;
import com.bct.fuelpay.constant.AppConstant;
import com.bct.fuelpay.databinding.FragmentHomeBinding;
import com.bct.fuelpay.model.Menu;
import com.bct.fuelpay.utils.FragmentUtils;
import com.bct.fuelpay.view_model.MenuViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements GridAdapter.ItemListener {

    FragmentHomeBinding binding;
    MenuViewModel menuViewModel;
    GridAdapter customAdapter;
    Bundle b;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);

        menuViewModel = ViewModelProviders.of(this).get(MenuViewModel.class);
        b = new Bundle();
        setupGridView();

        return binding.getRoot();
    }

    private void setupGridView(){
        ArrayList items = new ArrayList<>();
        items.add(new MenuViewModel(new Menu("Transactions","Print & View Bill",
                R.drawable.ic_transactions ,getActivity().getResources().getColor(R.color.maroon))));
        items.add(new MenuViewModel(new Menu("Fuel Sales","Wet Stock Sale",
                R.drawable.ic_authorize ,getActivity().getResources().getColor(R.color.indigo))));
        items.add(new MenuViewModel(new Menu("Pump Authorise","Pump Authorise",
                 R.drawable.ic_authorize ,getActivity().getResources().getColor(R.color.blue))));
        items.add(new MenuViewModel(new Menu("C-Store","Dry Stock Sale",
                R.drawable.ic_shop ,getActivity().getResources().getColor(R.color.green))));
        items.add(new MenuViewModel(new Menu("Customers","Manage Customer",
                R.drawable.ic_user_plus_rounded ,getActivity().getResources().getColor(R.color.yellow))));
        items.add(new MenuViewModel(new Menu("Settings","HHT Configuration",
                R.drawable.ic_settings ,getActivity().getResources().getColor(R.color.purple))));

        customAdapter = new GridAdapter(getActivity(),items);
        binding.recylerview.setLayoutManager(new GridLayoutManager(getActivity(),2));
        binding.recylerview.setAdapter(customAdapter);
        customAdapter.setClickListener(this);

    }


    @Override
    public void onMenuClick(View v, int position) {
        switch (position){
            case 0:
                b.putString(AppConstant.whichMenu,AppConstant.transaction);
                FragmentUtils.replaceFragment((AppCompatActivity) getActivity(),new PumpFragment(),b,
                        R.id.fragContainer,true,FragmentUtils.TRANSITION_NONE);
                break;
            case 1:
                b.putString(AppConstant.whichMenu,AppConstant.fuelSale);
                FragmentUtils.replaceFragment((AppCompatActivity) getActivity(),new PumpFragment(),b,
                        R.id.fragContainer,true,FragmentUtils.TRANSITION_NONE);
                break;
            case 2:
                b.putString(AppConstant.whichMenu,AppConstant.pumpAuthorize);
                FragmentUtils.replaceFragment((AppCompatActivity) getActivity(),new PumpAuthFragment(),b,
                        R.id.fragContainer,true,FragmentUtils.TRANSITION_NONE);
                break;
            case 3:
                b.putString(AppConstant.whichMenu,AppConstant.cStore);
                FragmentUtils.replaceFragment((AppCompatActivity) getActivity(),new CStoreFragment(),b,
                        R.id.fragContainer,true,FragmentUtils.TRANSITION_NONE);
                break;
            case 4:
                b.putString(AppConstant.whichMenu,AppConstant.customer);
                FragmentUtils.replaceFragment((AppCompatActivity) getActivity(),new CustomerFragment(),b,
                        R.id.fragContainer,true,FragmentUtils.TRANSITION_NONE);
                break;
            case 5:
                FragmentUtils.replaceFragment((AppCompatActivity) getActivity(),new SettingFragment(),
                        R.id.fragContainer,true,FragmentUtils.TRANSITION_NONE);
                break;
        }
    }
}