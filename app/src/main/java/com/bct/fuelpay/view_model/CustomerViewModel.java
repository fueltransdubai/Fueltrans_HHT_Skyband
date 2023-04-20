package com.bct.fuelpay.view_model;

import androidx.lifecycle.ViewModel;

public class CustomerViewModel extends ViewModel {

    public String name;
    public String mobile;


    public CustomerViewModel(){}

    public CustomerViewModel(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }





}
