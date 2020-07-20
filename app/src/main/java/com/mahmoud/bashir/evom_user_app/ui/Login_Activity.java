package com.mahmoud.bashir.evom_user_app.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.errors.ApiException;
import com.mahmoud.bashir.evom_user_app.R;
import com.mahmoud.bashir.evom_user_app.pojo.UsersInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.v2)
    View v2;
    @BindView(R.id.back_view)
    ImageView back_view;

    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference send_user_info_to_databaserealtime;
    private FirebaseAuth mAuth;

    UsersInfo usersInfo;
    String CUID="";

    private static final int RC_SIGN_IN= 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);


        ButterKnife.bind(this);


        //init Firebase
        send_user_info_to_databaserealtime = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");
        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        txt_enter_ph.setOnClickListener(view -> {

            //social visibility
            link_with_facebook.setVisibility(View.GONE);
            link_with_google.setVisibility(View.GONE);
            //views visibility
            txt_social.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            l_in.setVisibility(View.GONE);
            back_view.setVisibility(View.VISIBLE);
            lin_phone.setVisibility(View.VISIBLE);



        });


        //init Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        link_with_google.setOnClickListener(view -> {
            signIn();
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
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
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

                            prog_bar.setVisibility(View.GONE);

                           /* Intent n=new Intent(Login_Activity.this,Verify_phone.class);
                            n.putExtra("email",user.getEmail());
                            n.putExtra("name",user.getDisplayName());
                            n.putExtra("sort","google");
                            startActivity(n);
                            finish();*/


                           /* usersInfo=new UsersInfo(CUID,user.getDisplayName(),user.getEmail(),user.getPhoneNumber()+"");
                            send_user_info_to_databaserealtime.child(CUID).setValue(usersInfo);*/


                        } else {
                            prog_bar.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            // Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
    }
}