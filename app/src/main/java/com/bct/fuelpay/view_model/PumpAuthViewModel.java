package com.bct.fuelpay.view_model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;

import com.bct.fuelpay.model.Menu;

public class PumpAuthViewModel extends ViewModel {

    public String title;
    public int image;

    public PumpAuthViewModel(){
    }


    public PumpAuthViewModel(String title,int image){
        this.title = title;
        this.image = image;;

    }

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resource){
        imageView.setImageResource(resource);
    }


}
