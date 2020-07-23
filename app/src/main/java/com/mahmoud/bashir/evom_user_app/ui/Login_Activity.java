package com.mahmoud.bashir.evom_user_app.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.maps.errors.ApiException;
import com.hbb20.CountryCodePicker;
import com.mahmoud.bashir.evom_user_app.Maps.Home_Maps_Activity;
import com.mahmoud.bashir.evom_user_app.R;
import com.mahmoud.bashir.evom_user_app.Storage.SharedPrefranceManager;
import com.mahmoud.bashir.evom_user_app.pojo.UsersInfo;


import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Login_Activity extends AppCompatActivity {


    private static final String TAG = "Login_Activity";

    @BindView(R.id.txt_enter_ph)
    TextView txt_enter_ph;
    @BindView(R.id.txt_social)
    TextView txt_social;
    @BindView(R.id.lin_phone)
    LinearLayout lin_phone;
    @BindView(R.id.l_in)
    LinearLayout l_in;
    @BindView(R.id.link_with_facebook)
    LinearLayout link_with_facebook;
    @BindView(R.id.link_with_google)
    LinearLayout link_with_google;
    @BindView(R.id.prog_bar)
    ProgressBar prog_bar;
    @BindView(R.id.im)
    ImageView im;
    @BindView(R.id.back_view)
    ImageView back_view;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    @BindView(R.id.txt_c_code)
    TextView txt_c_code;
    @BindView(R.id.ln11)
    LinearLayout ln11;
    @BindView(R.id.rel_social)
    RelativeLayout rel_social;
    @BindView(R.id.rel_ph)
    RelativeLayout rel_ph;
    @BindView(R.id.send_btn)
    CircleImageView send_btn;
    @BindView(R.id.edt_phone_no)
    EditText edt_phone_no;
    @BindView(R.id.rel_pass)
    RelativeLayout rel_pass;
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.txt_Invalid_pass)
    TextView txt_Invalid_pass;
    @BindView(R.id.txt_forgot_pass)
    TextView txt_forgot_pass;
    @BindView(R.id.otp_view)
    PinView otpView;
    @BindView(R.id.rel_verify_c)
    RelativeLayout rel_verify_c;
    @BindView(R.id.lin_reset)LinearLayout lin_reset;
    @BindView(R.id.edt_new_pass)EditText edt_new_pass;
    @BindView(R.id.edt_confirm_new_pass)EditText edt_confirm_new_pass;
    @BindView(R.id.reset_btn) Button reset_btn;




    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference send_user_info_to_databaserealtime,check_USERS_PH_ref;
    private FirebaseAuth mAuth;

    UsersInfo usersInfo;
    String CUID="";
    String selectedCode="+20";
    String user_email,user_name,user_phone;
    String status = "",root="",check_eq="";
    private String verificationID;
    String full_phone;

    private static final int RC_SIGN_IN= 101;


    //animation
    Animation topAnim,bottomAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(SharedPrefranceManager.getInastance(this).isLoggedIn()){
            startActivity(new Intent(this,Home_Maps_Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }

        setContentView(R.layout.activity_login_);


        ButterKnife.bind(this);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        rel_social.setAnimation(bottomAnim);
        lin_phone.setAnimation(topAnim);
        im.startAnimation(inFromRightAnimation());

        //init Firebase
        send_user_info_to_databaserealtime = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");
        check_USERS_PH_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");

        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        CUID = mAuth.getCurrentUser().getUid();

        txt_enter_ph.setOnClickListener(view -> {

            //social visibility
            link_with_facebook.setVisibility(View.GONE);
            link_with_google.setVisibility(View.GONE);
            //views visibility
            txt_social.setVisibility(View.GONE);
            ln11.setVisibility(View.GONE);
            im.setVisibility(View.GONE);
            l_in.setVisibility(View.GONE);
            back_view.setVisibility(View.VISIBLE);
            lin_phone.setVisibility(View.VISIBLE);
            send_btn.setVisibility(View.VISIBLE);

            lin_phone.setAnimation(topAnim);
            rel_ph.setAnimation(bottomAnim);
            send_btn.setAnimation(bottomAnim);
            status = "Enter ph";

        });

        back_view.setOnClickListener(view -> {

            if (status.equals("Enter ph")){
            //social visibility
            link_with_facebook.setVisibility(View.VISIBLE);
            link_with_google.setVisibility(View.VISIBLE);
            //views visibility
            txt_social.setVisibility(View.VISIBLE);
            ln11.setVisibility(View.VISIBLE);
            im.setVisibility(View.VISIBLE);
            l_in.setVisibility(View.VISIBLE);

            back_view.setVisibility(View.GONE);
            lin_phone.setVisibility(View.GONE);
            send_btn.setVisibility(View.GONE);

            //lin_phone.setAnimation(topAnim);
            rel_ph.setAnimation(bottomAnim);

            rel_social.setAnimation(bottomAnim);
            lin_phone.setAnimation(topAnim);
            im.startAnimation(inFromRightAnimation());

            edt_phone_no.setText("");

            } else if (status.equals("in password")) {

                rel_pass.startAnimation(outToRightAnimation());
                lin_phone.setVisibility(View.VISIBLE);
                lin_phone.startAnimation(inFromLeftAnimation());
                rel_pass.setVisibility(View.GONE);
                status = "Enter ph";
                edt_password.setText("");
                txt_Invalid_pass.setVisibility(View.GONE);
            } else if (status.equals("in verify code")) {

                rel_verify_c.startAnimation(outToRightAnimation());
                lin_phone.setVisibility(View.VISIBLE);
                lin_phone.startAnimation(inFromLeftAnimation());
                rel_verify_c.setVisibility(View.GONE);
                status = "Enter ph";
            }

        });

        send_btn.setOnClickListener(view -> {

            if (TextUtils.isEmpty(edt_phone_no.getText().toString())){
                edt_phone_no.setError("Please Enter your Phone number!");
                edt_phone_no.requestFocus();
            }else if (edt_phone_no.length() < 10 || edt_phone_no.length() > 10){
                edt_phone_no.setError("Please Enter right Phone number!");
                edt_phone_no.requestFocus();
            }else {

                if(status.equals("Enter ph")){

                    full_phone = selectedCode + edt_phone_no.getText().toString();
                    Check_existing_ph_no(full_phone);

                }else if (status.equals("in password")){

                    if (TextUtils.isEmpty(edt_password.getText().toString())){
                        txt_Invalid_pass.setVisibility(View.VISIBLE);
                    }else if (edt_password.length() < 6 ){
                        edt_password.setError("Please enter correct password with more than 6 characters");
                        edt_password.requestFocus();
                    }else {
                        signIn_existing(full_phone,edt_password.getText().toString());
                    }

                }else if (status.equals( "in verify code")){

                    if (TextUtils.isEmpty(otpView.getText().toString())){
                        otpView.setError("Please Enter right Code");
                        otpView.requestFocus();
                    }else {
                        String Code = otpView.getText().toString();
                        verifycode(Code, full_phone);
                        Toast.makeText(this, ""+full_phone, Toast.LENGTH_SHORT).show();
                    }
                }

               }
        });

        txt_forgot_pass.setOnClickListener(view -> {

            AlertDialog.Builder builder =new AlertDialog.Builder(this);
            builder.setTitle("Configuration");
            builder.setMessage("want to reset your password");
            builder.setCancelable(true);

            builder.setPositiveButton("reset", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    rel_pass.setAnimation(outToLeftAnimation());
                    rel_verify_c.setVisibility(View.VISIBLE);
                    rel_verify_c.setAnimation(inFromRightAnimation());
                    rel_pass.setVisibility(View.GONE);

                    sendVerificationCode(full_phone);
                    status = "in verify code";
                    root = "to reset";
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    builder.setCancelable(true);
                }
            });
            builder.show();

        });

        reset_btn.setOnClickListener(view -> {
            if (TextUtils.isEmpty(edt_new_pass.getText().toString())){
                edt_new_pass.setError("Please enter a valid password");
                edt_new_pass.requestFocus();
            }else if (TextUtils.isEmpty(edt_confirm_new_pass.getText().toString())){
                edt_new_pass.setError("Please enter a confirm password");
                edt_new_pass.requestFocus();
            }else if (!edt_confirm_new_pass.getText().toString().equals(edt_new_pass.getText().toString())){
                edt_new_pass.setError("Please enter a valid confirm password that match new password");
                edt_new_pass.requestFocus();
            }else {
                HashMap<String,Object> map = new HashMap<>();
                map.put("password",edt_confirm_new_pass.getText().toString());
                send_user_info_to_databaserealtime.child(CUID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            lin_reset.setAnimation(outToRightAnimation());
                            lin_phone.setVisibility(View.VISIBLE);
                            lin_phone.setAnimation(inFromLeftAnimation());
                            lin_reset.setVisibility(View.GONE);

                            status = "Enter ph";

                            if (status.equals("Enter ph")) {
                                send_btn.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                });
            }
        });


        // to select country code
       // ccp.setDefaultCountryUsingNameCode("(EG) +20");
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                selectedCode = ccp.getSelectedCountryCodeWithPlus();
                txt_c_code.setText(selectedCode);
            }
        });


        //init Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                //.requestIdToken("383190207472-ebhrqc83ab65b8dptb9b92khq5tr79tf.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        link_with_google.setOnClickListener(view -> {
            signIn();
        });
}

    private void signIn_existing(String full_phone, String p_ass) {

        send_user_info_to_databaserealtime.child(CUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()){

                    String pass = dataSnapshot.child("password").getValue().toString();
                    if (p_ass.equals(pass)){

                        String name = dataSnapshot.child("name").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();
                        String id = dataSnapshot.child("id").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String deviceToken = dataSnapshot.child("deviceToken").getValue().toString();

                        txt_Invalid_pass.setVisibility(View.GONE);
                        Intent i = new Intent(Login_Activity.this,Home_Maps_Activity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();

                        SharedPrefranceManager.getInastance(Login_Activity.this).saveUser(name,email,phone,deviceToken);

                    }else {
                        txt_Invalid_pass.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void signIn() {
        prog_bar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        if (resultCode != RESULT_CANCELED) {
            if (resultCode == RESULT_OK && requestCode == RC_SIGN_IN) {

                try {

                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);

                } catch (ApiException e) {

                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e);
                    // ...
                }
            }
        }else {
            prog_bar.setVisibility(View.GONE);
        }
    }


    private void Check_existing_ph_no (String ph){
        send_user_info_to_databaserealtime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                   // if (dataSnapshot.hasChildren()){
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            String phone = snapshot.child("phone").getValue().toString();

                            if (ph.equals(phone)){
                                check_eq = "equals";
                            }
                        }

                        if (check_eq.equals("equals")){

                        lin_phone.startAnimation(outToLeftAnimation());
                        rel_pass.setVisibility(View.VISIBLE);
                        rel_pass.startAnimation(inFromRightAnimation());
                        lin_phone.setVisibility(View.GONE);

                        status = "in password";

                    } else {

                        sendVerificationCode(ph);

                        lin_phone.startAnimation(outToLeftAnimation());
                        rel_verify_c.setVisibility(View.VISIBLE);
                        rel_verify_c.startAnimation(inFromRightAnimation());
                        lin_phone.setVisibility(View.GONE);

                        status = "in verify code";
                        root = "from phone";
                    }
                    }
                }
            //}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            CUID=user.getUid();
                            user_name = account.getDisplayName();
                            user_email = account.getEmail();
                            root = "from google";

                            prog_bar.setVisibility(View.GONE);

                            //social visibility
                            link_with_facebook.setVisibility(View.GONE);
                            link_with_google.setVisibility(View.GONE);
                            //views visibility
                            txt_social.setVisibility(View.GONE);
                            ln11.setVisibility(View.GONE);
                            im.setVisibility(View.GONE);
                            l_in.setVisibility(View.GONE);

                            back_view.setVisibility(View.VISIBLE);
                            lin_phone.setVisibility(View.VISIBLE);
                            send_btn.setVisibility(View.VISIBLE);

                            lin_phone.setAnimation(topAnim);
                            rel_ph.setAnimation(bottomAnim);
                            send_btn.setAnimation(bottomAnim);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }


    private void verifycode(String code,String phone){
        try { PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationID,code);
            signWithCredential(credential,phone);
        }catch (Exception e){
            Toast toast = Toast.makeText(this, "Verification Code is wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }

    private void signWithCredential(PhoneAuthCredential credential, final String phone) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                             CUID = firebaseUser.getUid();
                             String deviceToken = FirebaseInstanceId.getInstance().getToken();

                             if (root.equals("from phone")){
                                 Toast.makeText(Login_Activity.this, ""+phone, Toast.LENGTH_SHORT).show();
                                 Intent intent = new Intent(Login_Activity.this, Verify_phone_Activity.class);
                                 intent.putExtra("id",CUID);
                                 intent.putExtra("phone_no",phone);
                                 intent.putExtra("deviceToken",deviceToken);
                                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                 startActivity(intent);
                                 finish();

                             }else if (root.equals("from google")){

                                 Intent intent = new Intent(Login_Activity.this, Home_Maps_Activity.class);
                                 intent.putExtra("id",CUID);
                                 intent.putExtra("phone_no",phone);
                                 intent.putExtra("email",user_email);
                                 intent.putExtra("name",user_name);
                                 intent.putExtra("deviceToken",deviceToken);

                                 usersInfo = new UsersInfo(CUID,user_name,user_email,null,phone,deviceToken);
                                 send_user_info_to_databaserealtime.child(CUID).setValue(usersInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task) {
                                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                         startActivity(intent);
                                         finish();
                                         SharedPrefranceManager.getInastance(Login_Activity.this).saveUser(user_name,user_email,phone,deviceToken);
                                     }
                                 });
                             }else if (root.equals("to reset")){

                                 send_btn.setVisibility(View.GONE);
                                 back_view.setVisibility(View.GONE);

                                 rel_verify_c.setAnimation(outToLeftAnimation());
                                 lin_reset.setVisibility(View.VISIBLE);
                                 lin_reset.setAnimation(inFromRightAnimation());
                                 rel_verify_c.setVisibility(View.GONE);
                             }

                        }else {
                            Toast.makeText(Login_Activity.this, "You can not register with this phone number!!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void sendVerificationCode(String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationID=s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String codesms=phoneAuthCredential.getSmsCode();
            if (codesms !=null){
                verifycode(codesms,null);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Login_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(1500);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }


    private Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(1500);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    private Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(1500);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }


    private Animation outToRightAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(1500);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }
}