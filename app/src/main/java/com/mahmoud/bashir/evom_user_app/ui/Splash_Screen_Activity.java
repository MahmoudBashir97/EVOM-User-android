package com.mahmoud.bashir.evom_user_app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mahmoud.bashir.evom_user_app.Maps.Home_Maps_Activity;
import com.mahmoud.bashir.evom_user_app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Splash_Screen_Activity extends AppCompatActivity {

    int pStatus = 0;
    private Handler handler = new Handler();
    @BindView(R.id.circularProgressbar)
    ProgressBar mProgress;
    @BindView(R.id.img)
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen_);

        ButterKnife.bind(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        img.animate().alpha(4000).setDuration(0);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                        Intent i = new Intent(Splash_Screen_Activity.this,Welcome_Activity.class);
                        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();


            }
        },4000);



       /* new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (pStatus < 100) {
                    pStatus += 1;

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mProgress.setProgress(pStatus);
                            if (pStatus==100){
                                Intent i = new Intent(Splash_Screen_Activity.this,Welcome_Activity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        // Just to display the progress slowly
                        Thread.sleep(80); //thread will take approx 1.5 seconds to finish
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/
    }
}