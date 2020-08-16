package com.mahmoud.bashir.evom_user_app.ui;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoud.bashir.evom_user_app.R;
import com.mahmoud.bashir.evom_user_app.Storage.SharedPrefranceManager;
import com.mahmoud.bashir.evom_user_app.pojo.SendRequest.trip_details_Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class Trips_act  extends AppCompatActivity {

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.rel_big)
    RelativeLayout rel_big;
    @BindView(R.id.rec_trips)
    RecyclerView rec_trips;
    List<trip_details_Model> mlist = new ArrayList<>();
    trip_details_Model tripModel;
    String userPhone;
    DatabaseReference reference;
    FirebaseAuth auth;

    trips_adpt tripsAdpt;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips__activiy);

        ButterKnife.bind(this);



        Paper.init(this);
        if (Paper.book().read("language").equals("ar")){
            rel_big.setBackgroundResource(R.drawable.back_common_color);
        }else if (Paper.book().read("language").equals("en")){
            rel_big.setBackgroundResource(R.drawable.txt_color);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        rec_trips.setHasFixedSize(true);
        rec_trips.setLayoutManager(new LinearLayoutManager(this));


        //init firebase
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("History");


        userPhone = SharedPrefranceManager.getInastance(this).getUserPhone();
        reference.child("Customers").child(userPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.hasChildren()){
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            String date   = snapshot.child("time").getValue().toString();
                            Double userlat   = Double.valueOf(snapshot.child("dest_lat").getValue().toString());
                            Double userlng  = Double.valueOf(snapshot.child("dest_lng").getValue().toString());
                            Double userdestlat  = Double.valueOf(snapshot.child("dest_lat").getValue().toString());
                            Double userdestlng  = Double.valueOf(snapshot.child("dest_lng").getValue().toString());

                            String[]  d = date.split(" ");
                            tripModel = new trip_details_Model(d[0],d[1],userlat,userlng,userdestlat,userdestlng,"30");
                            mlist.add(tripModel);
                        }
                        tripsAdpt = new trips_adpt(Trips_act.this,mlist);
                        rec_trips.setAdapter(tripsAdpt);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

class trips_adpt extends RecyclerView.Adapter<trips_adpt.ViewHolder>{

    Context context;
    List<trip_details_Model> modelList;

    public trips_adpt(Context context, List<trip_details_Model> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_trips,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        trip_details_Model m = modelList.get(position);

        holder.date_txt.setText(m.getDate());
        holder.time_txt.setText(m.getTime());
        holder.userplace_txt.setText(getCompleteAddressString(m.getUserplace_lat(),m.getUserplace_lng()));
        holder.userdestination_txt.setText(getCompleteAddressString(m.getUserdest_lat(),m.getUserdest_lng()));
        holder.txt_tripPrice.setText("30");
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView date_txt,time_txt,userplace_txt,userdestination_txt,txt_tripPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

                 date_txt = itemView.findViewById(R.id.date_txt);
                 time_txt = itemView.findViewById(R.id.time_txt);
                 userplace_txt = itemView.findViewById(R.id.userplace_txt);
                 userdestination_txt = itemView.findViewById(R.id.userdestination_txt);
                 txt_tripPrice = itemView.findViewById(R.id.txt_tripPrice);
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        Paper.init(context);
        String  lang = "en";
        if (Paper.book().read("language").equals("ar")){
            lang = "ar";
        }else if (Paper.book().read("language").equals("en")){
            lang = "en";
        }
        Locale mLocale = new Locale(lang);
        String strAdd = "";


        Geocoder geocoder = new Geocoder(context, mLocale);
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);


                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {

                    String loc= returnedAddress.getLocality()+ ",";
                    //String subloc = returnedAddress.getSubLocality()+",";
                    //String ad = returnedAddress.getAdminArea();
                    String s_ad =  returnedAddress.getSubAdminArea();
                    String street =returnedAddress.getThoroughfare()+",";
                    strReturnedAddress.append( street+s_ad);
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction add", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction add", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction add", "Canont get Address!");
        }
        return strAdd;
    }
}
