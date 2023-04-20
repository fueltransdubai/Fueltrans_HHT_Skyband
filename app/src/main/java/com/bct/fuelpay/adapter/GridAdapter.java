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
import com.bct.fuelpay.view_model.MenuViewModel;

import java.util.ArrayList;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.CustomView> {

    private LayoutInflater layoutInflater;
    private ArrayList<MenuViewModel> arrayList;
    private Context context;
    protected ItemListener mListener;

    public GridAdapter(Context context , ArrayList<MenuViewModel> arrayList) {
        this.arrayList = arrayList;
        this.context =  context;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        GridChildBinding cardBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.grid_child_view,parent,false);
        return new CustomView(cardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
     MenuViewModel menuViewModel = arrayList.get(position);
     holder.bind(menuViewModel);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {

        GridChildBinding cardBinding;

        public CustomView(GridChildBinding cardBinding) {
            super(cardBinding.getRoot());
            this.cardBinding = cardBinding;
            cardBinding.getRoot().setOnClickListener(this);
        }

        public void bind(MenuViewModel menuViewModel) {
            this.cardBinding.setMenuModel(menuViewModel);
            cardBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) mListener.onMenuClick(v,getAdapterPosition());
        }

        public GridChildBinding  getCardBinding()
        {
            return  cardBinding;
        }

    }

    // allows clicks events to be caught
    public void setClickListener(ItemListener itemClickListener) {
        this.mListener = itemClickListener;
    }

    public interface ItemListener {
        void onMenuClick(View v, int position);
    }

}
