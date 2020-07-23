package com.mahmoud.bashir.evom_user_app.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mahmoud.bashir.evom_user_app.Maps.Home_Maps_Activity;
import com.mahmoud.bashir.evom_user_app.R;
import com.mahmoud.bashir.evom_user_app.Storage.SharedPrefranceManager;
import com.mahmoud.bashir.evom_user_app.pojo.UsersInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Verify_phone_Activity extends AppCompatActivity {

    private static final String TAG = "Verify_phone_Activity";
    
    
    @BindView(R.id.btn_send)
    Button btn_send;
    @BindView(R.id.edt_usenName)
    TextInputEditText edt_usenName;
    @BindView(R.id.edt_usenEmail)
    TextInputEditText edt_usenEmail;
    @BindView(R.id.edt_usenPass)
    TextInputEditText edt_usenPass;
    @BindView(R.id.edt_usenPh)
    TextInputEditText edt_usenPh;

    String CUID , phone , deviceToken;
    UsersInfo usersInfo;

    //firebase initialize
    DatabaseReference reference;
    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_);

        ButterKnife.bind(this);

        //get Intent Data
        CUID = getIntent().getStringExtra("id");
        phone = getIntent().getStringExtra("phone_no");
        deviceToken = getIntent().getStringExtra("deviceToken");

        edt_usenPh.setText(phone);



        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");



        btn_send.setOnClickListener(view -> {
            if (TextUtils.isEmpty(edt_usenName.getText().toString())){
                edt_usenName.setError("Please Enter a valid name");
                edt_usenName.requestFocus();
            }else  if (TextUtils.isEmpty(edt_usenEmail.getText().toString())){
                edt_usenEmail.setError("Please Enter a valid Email");
                edt_usenEmail.requestFocus();
            }else  if (TextUtils.isEmpty(edt_usenPass.getText().toString())){
                edt_usenPass.setError("Please Enter a valid Password");
                edt_usenPass.requestFocus();
            }else {
                                    FirebaseUser user = auth.getCurrentUser();
                                    Intent intent = new Intent(Verify_phone_Activity.this, Home_Maps_Activity.class);
                                    intent.putExtra("id",CUID);
                                    intent.putExtra("phone_no",phone);
                                    intent.putExtra("email",edt_usenEmail.getText().toString());
                                    intent.putExtra("name",edt_usenName.getText().toString());
                                    intent.putExtra("deviceToken",deviceToken);

                                    usersInfo = new UsersInfo(CUID,
                                            edt_usenName.getText().toString(),
                                            edt_usenEmail.getText().toString(),
                                            edt_usenPass.getText().toString(),
                                            phone,
                                            deviceToken);

                                    reference.child(phone).setValue(usersInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                            SharedPrefranceManager.getInastance(Verify_phone_Activity.this).saveUser(edt_usenName.getText().toString(),
                                                    edt_usenEmail.getText().toString(),phone,deviceToken);
                                        }
            });
            }
        });


    }
}