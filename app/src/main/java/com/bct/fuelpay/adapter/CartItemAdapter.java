package com.bct.fuelpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.bct.fuelpay.R;
import com.bct.fuelpay.databinding.CartItemChildBinding;
import com.bct.fuelpay.databinding.FCCTranChildBinding;
import com.bct.fuelpay.view_model.CartItemViewModel;

import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CustomView> {

    private LayoutInflater layoutInflater;
    private ArrayList<CartItemViewModel> arrayList;
    private Context context;
    protected ItemListener mListener;

    public CartItemAdapter(Context context , ArrayList<CartItemViewModel> arrayList) {
        this.arrayList = arrayList;
        this.context =  context;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        CartItemChildBinding cartItemChildBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.cartitem_child_view,parent,false);
        return new CustomView(cartItemChildBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
     CartItemViewModel cartItemViewModel = arrayList.get(position);

     holder.bind(cartItemViewModel);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {

        CartItemChildBinding cartItemChildBinding;

        public CustomView(CartItemChildBinding cardBinding) {
            super(cardBinding.getRoot());
            this.cartItemChildBinding = cardBinding;
            cardBinding.cardViewWetStock.setVisibility(View.VISIBLE);
            cardBinding.getRoot().setOnClickListener(this);
        }


        public void bind(CartItemViewModel cartItemViewModel) {
            this.cartItemChildBinding.setCartItemModel(cartItemViewModel);

            cartItemChildBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) mListener.onTransactionClick(v,getAdapterPosition());
        }



        public CartItemChildBinding  getCartItemChildBinding()
        {
            return  cartItemChildBinding;
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
