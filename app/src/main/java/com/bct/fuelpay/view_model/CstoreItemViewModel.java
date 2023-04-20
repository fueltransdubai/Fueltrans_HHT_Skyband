package com.bct.fuelpay.view_model;


import androidx.lifecycle.ViewModel;

public class CstoreItemViewModel extends ViewModel {

    public String name;
    public int quantity;
    public String amount;

    public CstoreItemViewModel(String name, int quantity, String amount) {
        this.name = name;
        this.quantity = quantity;
        this.amount = amount;
    }



}
