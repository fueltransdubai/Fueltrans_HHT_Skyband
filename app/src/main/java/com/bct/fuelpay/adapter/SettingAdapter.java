package com.bct.fuelpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bct.fuelpay.R;
import com.bct.fuelpay.databinding.SettingChildBinding;
import com.bct.fuelpay.view_model.SettingViewModel;

import java.util.ArrayList;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.CustomView> {

    private LayoutInflater layoutInflater;
    private ArrayList<SettingViewModel> arrayList;
    private Context context;
    protected ItemListener mListener;

    public SettingAdapter(Context context , ArrayList<SettingViewModel> arrayList) {
        this.arrayList = arrayList;
        this.context =  context;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        SettingChildBinding pumpBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.setting_child_view,parent,false);
        return new CustomView(pumpBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
     SettingViewModel settingViewModel = arrayList.get(position);
     holder.bind(settingViewModel);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {

        SettingChildBinding settingChildBinding;

        public CustomView(SettingChildBinding cardBinding) {
            super(cardBinding.getRoot());
            this.settingChildBinding = cardBinding;
            cardBinding.getRoot().setOnClickListener(this);
        }


        public void bind(SettingViewModel settingViewModel) {
            this.settingChildBinding.setSettingModel(settingViewModel);
            settingChildBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) mListener.onSettingClick(v,getAdapterPosition());
        }



        public SettingChildBinding  getPumpBinding()
        {
            return  settingChildBinding;
        }

    }

    // allows clicks events to be caught
    public void setClickListener(ItemListener itemClickListener) {
        this.mListener = itemClickListener;
    }

    public interface ItemListener {
        void onSettingClick(View v, int position);
    }

}
