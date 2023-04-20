package com.bct.fuelpay.view_model;

import androidx.lifecycle.ViewModel;

public class FccTranViewModel extends ViewModel {

    public String time;
    public String date;
    public String quantity;
    public String amount;

    public FccTranViewModel(){}


    public FccTranViewModel(String time, String date, String quantity, String amount) {
        this.time = time;
        this.date = date;
        this.quantity = quantity;
        this.amount = amount;
    }
}
