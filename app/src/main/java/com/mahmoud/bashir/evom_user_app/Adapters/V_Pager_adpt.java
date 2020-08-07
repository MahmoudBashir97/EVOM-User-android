package com.mahmoud.bashir.evom_user_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.mahmoud.bashir.evom_user_app.Maps.Home_Maps_Activity;
import com.mahmoud.bashir.evom_user_app.R;
import com.mahmoud.bashir.evom_user_app.ui.Welcome_Activity;

public class V_Pager_adpt extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public V_Pager_adpt(Context context) {
        this.context = context;
    }


    int[] images = {
      R.drawable.undraw_navigator_a479,
      R.drawable.undraw_location_tracking_46qb,
      R.drawable.undraw_mail1_uab6
    };

    public String[] texts = {
      "DEVOM will deliver you inside Cairo ",
            "With DEVOM ,\nu will not be late to ur work",
      "Mark your destination , start your trip",

    };

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==(RelativeLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v=layoutInflater.inflate(R.layout.intro_slide,container,false);
        RelativeLayout slide_relative=v.findViewById(R.id.slide_Relative);
        ImageView intro_logo=v.findViewById(R.id.img_v);
        TextView txt_1=v.findViewById(R.id.txt_1);
        TextView txt_2=v.findViewById(R.id.txt_2);

        intro_logo.setImageResource(images[position]);
        txt_1.setText(texts[position]);
        //txt_2.setText(lst_desc[position]);

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
