package com.bct.fuelpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bct.fuelpay.R;
import com.bct.fuelpay.databinding.AuthValueBinding;
import com.bct.fuelpay.view_model.AuthValueViewModel;

import java.util.ArrayList;

public class AuthValueAdapter extends RecyclerView.Adapter<AuthValueAdapter.CustomView> {

    private LayoutInflater layoutInflater;
    private ArrayList<AuthValueViewModel> arrayList;
    private Context context;
    protected ItemListener mListener;

    public AuthValueAdapter(Context context , ArrayList<AuthValueViewModel> arrayList) {
        this.arrayList = arrayList;
        this.context =  context;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        AuthValueBinding authValueBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.auth_value_child_view,parent,false);
        return new CustomView(authValueBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
     AuthValueViewModel authValueViewModel = arrayList.get(position);
     holder.bind(authValueViewModel);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {

        AuthValueBinding authValueBinding;

        public CustomView(AuthValueBinding cardBinding) {
            super(cardBinding.getRoot());
            this.authValueBinding = cardBinding;
            cardBinding.getRoot().setOnClickListener(this);
        }


        public void bind(AuthValueViewModel authValueViewModel) {
            this.authValueBinding.setAuthValueModel(authValueViewModel);
            authValueBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) mListener.onAuthValueClick(v,getAdapterPosition());
        }



        public AuthValueBinding  getPumpBinding()
        {
            return  authValueBinding;
        }

    }

    // allows clicks events to be caught
    public void setClickListener(ItemListener itemClickListener) {
        this.mListener = itemClickListener;
    }

    public interface ItemListener {
        void onAuthValueClick(View v, int position);
    }

}
