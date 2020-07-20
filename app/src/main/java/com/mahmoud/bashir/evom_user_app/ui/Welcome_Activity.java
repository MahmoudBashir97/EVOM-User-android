package com.mahmoud.bashir.evom_user_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mahmoud.bashir.evom_user_app.Adapters.V_Pager_adpt;
import com.mahmoud.bashir.evom_user_app.Maps.Home_Maps_Activity;
import com.mahmoud.bashir.evom_user_app.R;

public class Welcome_Activity extends AppCompatActivity {

    private ViewPager viewPager;
    private V_Pager_adpt pager_adpt;
    private TextView[] dots;
    private int[] images ;

    LinearLayout dotsLayout;
    Button btnSkip,btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_welcome_);

        viewPager = findViewById(R.id.view_pager);

        pager_adpt = new V_Pager_adpt(this);

        dotsLayout = findViewById(R.id.layoutDots);
        btnSkip = findViewById(R.id.btn_skip);
        btnNext =  findViewById(R.id.btn_next);


        images = new int[]{
                R.drawable.undraw_navigator_a479,
                R.drawable.undraw_location_tracking_46qb,
                R.drawable.undraw_mail1_uab6
        };

        // adding bottom dots
        addBottomDots(0);


        viewPager.setAdapter(pager_adpt);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnNext.setOnClickListener(view -> {
            // checking for last page
            // if last page home screen will be launched
            int current = getItem(+1);
            if (current < images.length) {
                // move to next screen
                viewPager.setCurrentItem(current);
            } else {
                launchHomeScreen();
            }
        });

        btnSkip.setOnClickListener(view -> {
            launchHomeScreen();
        });


    }

    private void launchHomeScreen() {

        Intent i = new Intent(Welcome_Activity.this, UI_Activity.class);
        startActivity(i);
        finish();
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }


     ViewPager.OnPageChangeListener viewPagerPageChangeListener =  new  ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == images.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText("Start");
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText("Next");
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



    private void addBottomDots(int currentPage) {
         dots = new TextView[images.length];

            int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
            int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

            dotsLayout.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new TextView(this);
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(35);
                dots[i].setTextColor(colorsInactive[currentPage]);
                dotsLayout.addView(dots[i]);
            }

            if (dots.length > 0)
                dots[currentPage].setTextColor(colorsActive[currentPage]);
        }
    }