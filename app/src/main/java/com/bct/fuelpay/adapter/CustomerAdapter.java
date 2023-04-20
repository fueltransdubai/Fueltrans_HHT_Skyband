package com.bct.fuelpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bct.fuelpay.R;
import com.bct.fuelpay.databinding.CustomerChildBinding;
import com.bct.fuelpay.view_model.CustomerViewModel;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomView> {

    private LayoutInflater layoutInflater;
    private ArrayList<CustomerViewModel> arrayList;
    private Context context;
    protected ItemListener mListener;

    public CustomerAdapter(Context context , ArrayList<CustomerViewModel> arrayList) {
        this.arrayList = arrayList;
        this.context =  context;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        CustomerChildBinding customerChildBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.customer_item_view,parent,false);
        return new CustomView(customerChildBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
     CustomerViewModel menuViewModel = arrayList.get(position);
     holder.bind(menuViewModel);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {

        CustomerChildBinding customerChildBinding;

        public CustomView(CustomerChildBinding cardBinding) {
            super(cardBinding.getRoot());
            this.customerChildBinding = cardBinding;
            cardBinding.getRoot().setOnClickListener(this);
        }


        public void bind(CustomerViewModel customerViewModel) {
            this.customerChildBinding.setCustomerModel(customerViewModel);
            this.customerChildBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) mListener.onCustomerClick(v,getAdapterPosition());
        }



        public CustomerChildBinding  getCustomerChildBinding()
        {
            return customerChildBinding;
        }

    }

    // allows clicks events to be caught
    public void setClickListener(ItemListener itemClickListener) {
        this.mListener = itemClickListener;
    }

    public interface ItemListener {
        void onCustomerClick(View v, int position);
    }

}
