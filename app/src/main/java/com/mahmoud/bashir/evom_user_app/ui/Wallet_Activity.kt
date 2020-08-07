package com.mahmoud.bashir.evom_user_app.ui

import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mahmoud.bashir.evom_user_app.R
import com.mahmoud.bashir.evom_user_app.Storage.SharedPrefranceManager
import io.paperdb.Paper

class Wallet_Activity : AppCompatActivity() {

    lateinit var txt_usercredit : TextView
    lateinit var toolbar : Toolbar
    lateinit var reference : DatabaseReference
    lateinit var auth : FirebaseAuth
    var user_phone : String =""
    var wallet_value : String =""
    lateinit var rel_big : RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_)


        Paper.init(this)

        rel_big = findViewById(R.id.rel_big)
        if (Paper.book().read<String>("language").equals("ar")){
            rel_big.setBackgroundResource(R.drawable.back_common_color)
        }else if (Paper.book().read<String>("language").equals("en")){
            rel_big.setBackgroundResource(R.drawable.txt_color)
        }



        // init views
        txt_usercredit = findViewById(R.id.txt_usercredit)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        user_phone = SharedPrefranceManager.getInastance(this).userPhone

        //init firebase
        auth = FirebaseAuth.getInstance()
        reference = FirebaseDatabase.getInstance().reference.child("Wallet")

        reference.child("Customers").child(user_phone).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(dataSnapshot: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){
                    wallet_value = dataSnapshot.child("wallet_value").value.toString()
                    txt_usercredit.setText(wallet_value)
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}