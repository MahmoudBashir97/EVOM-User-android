package com.mahmoud.bashir.evom_user_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.widget.Toolbar
import com.mahmoud.bashir.evom_user_app.R
import io.paperdb.Paper

class Payment_Activity : AppCompatActivity() {

    lateinit var toolbar : Toolbar
    lateinit var rel_big : RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_)

        Paper.init(this)
        rel_big = findViewById(R.id.rel_big)

        if (Paper.book().read<String>("language").equals("ar")){
            rel_big.setBackgroundResource(R.drawable.back_common_color)
        }else if (Paper.book().read<String>("language").equals("en")){
            rel_big.setBackgroundResource(R.drawable.txt_color)
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)



    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}