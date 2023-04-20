package com.bct.fuelpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bct.fuelpay.R;
import com.bct.fuelpay.databinding.NozzleChildBinding;
import com.bct.fuelpay.view_model.NozzleViewModel;

import java.util.ArrayList;

public class NozzleAdapter extends RecyclerView.Adapter<NozzleAdapter.CustomView> {

    private LayoutInflater layoutInflater;
    private ArrayList<NozzleViewModel> arrayList;
    private Context context;
    protected ItemListener mListener;

    public NozzleAdapter(Context context , ArrayList<NozzleViewModel> arrayList) {
        this.arrayList = arrayList;
        this.context =  context;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        NozzleChildBinding pumpBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.nozzle_child_view,parent,false);
        return new CustomView(pumpBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
     NozzleViewModel menuViewModel = arrayList.get(position);
     holder.bind(menuViewModel);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {

        NozzleChildBinding nozzleChildBinding;

        public CustomView(NozzleChildBinding cardBinding) {
            super(cardBinding.getRoot());
            this.nozzleChildBinding = cardBinding;
            cardBinding.getRoot().setOnClickListener(this);
        }


        public void bind(NozzleViewModel nozzleViewModel) {
            this.nozzleChildBinding.setNozzleModel(nozzleViewModel);
            nozzleChildBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) mListener.onNozzleClick(v,getAdapterPosition());
        }



        public NozzleChildBinding  getPumpBinding()
        {
            return  nozzleChildBinding;
        }

    }

    // allows clicks events to be caught
    public void setClickListener(ItemListener itemClickListener) {
        this.mListener = itemClickListener;
    }

    public interface ItemListener {
        void onNozzleClick(View v, int position);
    }

}
