package com.bct.fuelpay.view.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bct.fuelpay.R;
import com.bct.fuelpay.adapter.CartItemAdapter;
import com.bct.fuelpay.adapter.CstoreItemAdapter;
import com.bct.fuelpay.databinding.FragmentCStoreBinding;
import com.bct.fuelpay.utils.Data;
import com.bct.fuelpay.view_model.CartItemViewModel;
import com.bct.fuelpay.view_model.CstoreItemViewModel;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CStoreFragment extends Fragment implements View.OnClickListener {

    FragmentCStoreBinding binding;
    CstoreItemAdapter cstoreItemAdapter;
    ArrayList<CstoreItemViewModel> arrayList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_c_store,container,false);
        setUpWidgets();

        return binding.getRoot();
    }

    private void setUpWidgets(){
        getItemsByCategory("Lubricants");
        binding.title.setOnClickListener(this);
        binding.btnLubricant.setOnClickListener(this);
        binding.btnBeverages.setOnClickListener(this);
        binding.btnSnacks.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title:
                getActivity().getSupportFragmentManager().popBackStack();
                break;

            case R.id.btnLubricant:
                binding.btnLubricant.setBackgroundColor(getResources().getColor(R.color.button_blue));
                binding.btnBeverages.setBackgroundColor(getResources().getColor(R.color.button_grey));
                binding.btnSnacks.setBackgroundColor(getResources().getColor(R.color.button_grey));
                getItemsByCategory("Lubricants");
                break;

            case R.id.btnBeverages:
                binding.btnLubricant.setBackgroundColor(getResources().getColor(R.color.button_grey));
                binding.btnBeverages.setBackgroundColor(getResources().getColor(R.color.button_blue));
                binding.btnSnacks.setBackgroundColor(getResources().getColor(R.color.button_grey));
                getItemsByCategory("Beverages");
                break;

            case R.id.btnSnacks:
                binding.btnLubricant.setBackgroundColor(getResources().getColor(R.color.button_grey));
                binding.btnBeverages.setBackgroundColor(getResources().getColor(R.color.button_grey));
                binding.btnSnacks.setBackgroundColor(getResources().getColor(R.color.button_blue));
                getItemsByCategory("Snacks");
                break;

            case R.id.btnSubmit:

                break;

        }
    }

    private void getItemsByCategory(String category){
        try {
            JSONObject coderollsJSONObject = new JSONObject(Data.cSTore);
            String items = coderollsJSONObject.getString(category);
            JSONArray jsonArray = new JSONArray(items);
            arrayList = new ArrayList<>();
            for (int i =0;i<jsonArray.length();i++){
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                String name = jsonobject.getString("name");
//                String quantity = jsonobject.getString("quantity");
                String price = jsonobject.getString("price");

                arrayList.add(new CstoreItemViewModel(name,00,price));
            }
            cstoreItemAdapter = new CstoreItemAdapter(getActivity(),arrayList);
            binding.recylerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.recylerview.setAdapter(cstoreItemAdapter);
            cstoreItemAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


}