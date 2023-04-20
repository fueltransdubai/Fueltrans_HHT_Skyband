package com.bct.fuelpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bct.fuelpay.R;
import com.bct.fuelpay.databinding.FCCTranChildBinding;
import com.bct.fuelpay.view_model.FccTranViewModel;

import java.util.ArrayList;

public class FccTranAdapter extends RecyclerView.Adapter<FccTranAdapter.CustomView> {

    private LayoutInflater layoutInflater;
    private ArrayList<FccTranViewModel> arrayList;
    private Context context;
    protected ItemListener mListener;

    public FccTranAdapter(Context context , ArrayList<FccTranViewModel> arrayList) {
        this.arrayList = arrayList;
        this.context =  context;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        FCCTranChildBinding fccTransactionBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.fcctran_child_view,parent,false);
        return new CustomView(fccTransactionBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
     FccTranViewModel fccTranViewModel = arrayList.get(position);
     holder.bind(fccTranViewModel);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {

        FCCTranChildBinding fccTranChildBinding;

        public CustomView(FCCTranChildBinding cardBinding) {
            super(cardBinding.getRoot());
            this.fccTranChildBinding = cardBinding;
            cardBinding.getRoot().setOnClickListener(this);
        }


        public void bind(FccTranViewModel fccTranViewModel) {
            this.fccTranChildBinding.setFccTranModel(fccTranViewModel);

            fccTranChildBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) mListener.onTransactionClick(v,getAdapterPosition());
        }



        public FCCTranChildBinding  getPumpBinding()
        {
            return  fccTranChildBinding;
        }

    }

    // allows clicks events to be caught
    public void setClickListener(ItemListener itemClickListener) {
        this.mListener = itemClickListener;
    }

    public interface ItemListener {
        void onTransactionClick(View v, int position);
    }

}
