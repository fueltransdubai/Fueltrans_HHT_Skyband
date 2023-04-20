package com.bct.fuelpay.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bct.fuelpay.R;
import com.bct.fuelpay.databinding.CartItemChildBinding;
import com.bct.fuelpay.databinding.CstoreItemBinding;
import com.bct.fuelpay.view_model.CartItemViewModel;
import com.bct.fuelpay.view_model.CstoreItemViewModel;

import java.util.ArrayList;

public class CstoreItemAdapter extends RecyclerView.Adapter<CstoreItemAdapter.CustomView> {

    private LayoutInflater layoutInflater;
    private ArrayList<CstoreItemViewModel> arrayList;
    private Context context;
    protected ItemListener mListener;

    public CstoreItemAdapter(Context context , ArrayList<CstoreItemViewModel> arrayList) {
        this.arrayList = arrayList;
        this.context =  context;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        CstoreItemBinding cstoreItemBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.c_store_item_child_view,parent,false);
        return new CustomView(cstoreItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
        CstoreItemViewModel cartItemViewModel = arrayList.get(position);

     holder.bind(cartItemViewModel);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {

        CstoreItemBinding cstoreItemBinding;

        public CustomView(CstoreItemBinding cardBinding) {
            super(cardBinding.getRoot());
            this.cstoreItemBinding = cardBinding;

            cardBinding.getRoot().setOnClickListener(this);
        }


        public void bind(CstoreItemViewModel cstoreItemViewModel) {
            this.cstoreItemBinding.setCstoreItemModel(cstoreItemViewModel);

//            cstoreItemBinding.tvQuantity.setText(arrayList.get(getAdapterPosition()).quantity);
            this.cstoreItemBinding.ivPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    arrayList.get(getAdapterPosition()).quantity++;
                    notifyDataSetChanged();
                }
            });
            cstoreItemBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) mListener.onCstoreItemClick(v,getAdapterPosition());
        }



        public CstoreItemBinding getCstoreItemBinding()
        {
            return cstoreItemBinding;
        }

    }

    // allows clicks events to be caught
    public void setClickListener(ItemListener itemClickListener) {
        this.mListener = itemClickListener;
    }

    public interface ItemListener {
        void onCstoreItemClick(View v, int position);
    }

}
