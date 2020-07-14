package com.mahmoud.bashir.evom_user_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mahmoud.bashir.evom_user_app.R;
import com.mahmoud.bashir.evom_user_app.paperdb.helper.LocaleHelper;

import java.util.Locale;

import butterknife.ButterKnife;

import butterknife.BindView;
import io.paperdb.Paper;

public class Settings_Activity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.txt_signOut) TextView txt_signOut;
    @BindView(R.id.txt_email) TextView txt_email;
    @BindView(R.id.txtphonenumber) TextView txtphonenumber;
    @BindView(R.id.txt_fname) TextView txt_fname;
    @BindView(R.id.spin_lang)
    Spinner spin_lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        //to Sign Out
        txt_signOut.setOnClickListener(view -> {
            Toast.makeText(this, "OUT!!!", Toast.LENGTH_SHORT).show();
        });


        // init paper first
        Paper.init(this);

        //Default language is English
        String language = Paper.book().read("language");
        if (language == null){
            Paper.book().write("language","en");}

        updateView((String) Paper.book().read("language"));


        spin_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0){
                    Toast.makeText(Settings_Activity.this, "arabic", Toast.LENGTH_SHORT).show();
                    Paper.book().write("language","ar");

                    // setLocale("ar");
                    updateView((String) Paper.book().read("language"));
                    Intent intent = new Intent(Settings_Activity.this,Splash_Screen_Activity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);
                }else if (i == 1 ){

                    //setLocale("en");

                    Paper.book().write("language","en");
                    updateView((String) Paper.book().read("language"));
                    Toast.makeText(Settings_Activity.this, "en", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings_Activity.this,Splash_Screen_Activity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateView(String language) {

        Context context = LocaleHelper.setLocale(this,language);
        Resources resources = context.getResources();

        if (language.equals("ar")){
            spin_lang.setSelection(0);
        }else {
            spin_lang.setSelection(1);
        }
       // sl.setText(resources.getString(R.string.verify_hint_w));
    }

    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        Intent intent = new Intent(Settings_Activity.this,Splash_Screen_Activity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}