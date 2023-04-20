package com.bct.fuelpay.view_model;
import android.widget.ImageView;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;
import com.bct.fuelpay.model.Menu;

public class MenuViewModel extends ViewModel {

    public String title;
    public String subTitle;
    public int image;
    public int color;

    public MenuViewModel(){
    }


    public MenuViewModel(Menu menu){
        this.title = menu.title;
        this.subTitle = menu.subTitle;
        this.image = menu.image;;
        this.color = menu.color;

    }

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resource){
        imageView.setImageResource(resource);
    }


}
