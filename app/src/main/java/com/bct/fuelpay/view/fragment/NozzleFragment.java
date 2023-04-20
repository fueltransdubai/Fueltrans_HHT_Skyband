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
import com.bct.fuelpay.adapter.NozzleAdapter;
import com.bct.fuelpay.databinding.FragmentNozzleBinding;
import com.bct.fuelpay.utils.FragmentUtils;
import com.bct.fuelpay.view_model.NozzleViewModel;

import java.util.ArrayList;

public class NozzleFragment extends Fragment implements NozzleAdapter.ItemListener {

    FragmentNozzleBinding binding;
    NozzleViewModel nozzleViewModel;
    NozzleAdapter nozzleAdapter;
    Bundle b;
    ArrayList<NozzleViewModel> items;
    String pump;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_nozzle,container,false);

        nozzleViewModel = ViewModelProviders.of(this).get(NozzleViewModel.class);
        b = new Bundle();
        b = getArguments();
        pump = b.getString("Pump");
        binding.title.setText("Pump "+ pump);
        binding.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        setupGridView();

        return binding.getRoot();
    }


    private void setupGridView(){
        items = new ArrayList<>();
        items.add(new NozzleViewModel("Nozzle 1","MS",getActivity().getResources().
                getColor(R.color.grade_orange)));
        items.add(new NozzleViewModel("Nozzle 2","HSD",getActivity().getResources().
                getColor(R.color.grade_green)));
        items.add(new NozzleViewModel("Nozzle 3","XTR95",getActivity().getResources().
                getColor(R.color.grade_pink)));
        items.add(new NozzleViewModel("Nozzle 4","MS",getActivity().getResources().
                getColor(R.color.grade_orange)));


        nozzleAdapter = new NozzleAdapter(getActivity(),items);
        binding.recylerview.setLayoutManager(new GridLayoutManager(getActivity(),2));
        binding.recylerview.setAdapter(nozzleAdapter);
        nozzleAdapter.setClickListener(this);

    }

    @Override
    public void onNozzleClick(View v, int position) {
        Bundle b = new Bundle();
        b.putString("Pump",pump);
        b.putString("Nozzle",String.valueOf(position+1));
        b.putString("Grade",items.get(0).grade);
        FragmentUtils.replaceFragment((AppCompatActivity) getActivity(),new FccTransactionFragment(),b,
                R.id.fragContainer,true,FragmentUtils.TRANSITION_NONE);
    }
}