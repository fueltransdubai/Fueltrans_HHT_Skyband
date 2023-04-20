package com.bct.fuelpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bct.fuelpay.R;
import com.bct.fuelpay.databinding.PumpChildBinding;
import com.bct.fuelpay.view_model.PumpViewModel;

import java.util.ArrayList;

public class PumpAdapter extends RecyclerView.Adapter<PumpAdapter.CustomView> {

    private LayoutInflater layoutInflater;
    private ArrayList<PumpViewModel> arrayList;
    private Context context;
    protected ItemListener mListener;

    public PumpAdapter(Context context , ArrayList<PumpViewModel> arrayList) {
        this.arrayList = arrayList;
        this.context =  context;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        PumpChildBinding pumpBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.pump_child_view,parent,false);
        return new CustomView(pumpBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
     PumpViewModel menuViewModel = arrayList.get(position);
     holder.bind(menuViewModel);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {

        PumpChildBinding pumpChildBinding;

        public CustomView(PumpChildBinding cardBinding) {
            super(cardBinding.getRoot());
            this.pumpChildBinding = cardBinding;
            cardBinding.getRoot().setOnClickListener(this);
        }


        public void bind(PumpViewModel pumpViewModel) {
            this.pumpChildBinding.setPumpModel(pumpViewModel);
            pumpChildBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) mListener.onPumpClick(v,getAdapterPosition());
        }



        public PumpChildBinding  getPumpBinding()
        {
            return  pumpChildBinding;
        }

    }

    // allows clicks events to be caught
    public void setClickListener(ItemListener itemClickListener) {
        this.mListener = itemClickListener;
    }

    public interface ItemListener {
        void onPumpClick(View v, int position);
    }

}
