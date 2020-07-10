package com.mahmoud.bashir.evom_user_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.mahmoud.bashir.evom_user_app.R;
import butterknife.ButterKnife;

import butterknife.BindView;

public class Settings_Activity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
}