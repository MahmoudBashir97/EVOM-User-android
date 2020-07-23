package com.mahmoud.bashir.evom_user_app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import com.mahmoud.bashir.evom_user_app.R;

import org.objectweb.asm.Handle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UI_Activity extends AppCompatActivity {

    @BindView(R.id.rell)
    RelativeLayout rell;


    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rell.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_i_);

        ButterKnife.bind(this);

        handler.postDelayed(runnable,4000);


    }
}