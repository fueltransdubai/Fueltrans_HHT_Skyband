package com.bct.fuelpay.view_model;

import androidx.lifecycle.ViewModel;

public class NozzleViewModel extends ViewModel {

    public String title;
    public String grade;
    public int color;

    public NozzleViewModel(){}

    public NozzleViewModel(String title, String grade, int color) {
        this.title = title;
        this.grade = grade;
        this.color = color;
    }
}
