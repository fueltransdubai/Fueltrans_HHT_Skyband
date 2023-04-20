package com.bct.fuelpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bct.fuelpay.R;
import com.bct.fuelpay.databinding.GridChildBinding;
import com.bct.fuelpay.databinding.PumpAuthChildBinding;
import com.bct.fuelpay.view_model.PumpAuthViewModel;

import java.util.ArrayList;

public class PumpAuthAdapter extends RecyclerView.Adapter<PumpAuthAdapter.CustomView> {

    private LayoutInflater layoutInflater;
    private ArrayList<PumpAuthViewModel> arrayList;
    private Context context;
    protected ItemListener mListener;

    public PumpAuthAdapter(Context context , ArrayList<PumpAuthViewModel> arrayList) {
        this.arrayList = arrayList;
        this.context =  context;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        PumpAuthChildBinding pumpAuthChildBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.pumpauth_child_view,parent,false);
        return new CustomView(pumpAuthChildBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
        PumpAuthViewModel pumpAuthViewModel = arrayList.get(position);
     holder.bind(pumpAuthViewModel);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {

        PumpAuthChildBinding cardBinding;

        public CustomView(PumpAuthChildBinding cardBinding) {
            super(cardBinding.getRoot());
            this.cardBinding = cardBinding;
            cardBinding.getRoot().setOnClickListener(this);
        }

        public void bind(PumpAuthViewModel pumpAuthViewModel) {
            this.cardBinding.setAuthTypeModel(pumpAuthViewModel);
            cardBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) mListener.onItemClick(v,getAdapterPosition());
        }

        public PumpAuthChildBinding  getCardBinding()
        {
            return  cardBinding;
        }

    }

    // allows clicks events to be caught
    public void setClickListener(ItemListener itemClickListener) {
        this.mListener = itemClickListener;
    }

    public interface ItemListener {
        void onItemClick(View v, int position);
    }

}
