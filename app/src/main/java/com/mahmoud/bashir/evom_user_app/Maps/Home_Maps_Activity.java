package com.mahmoud.bashir.evom_user_app.Maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Camera;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.provider.Settings;
import android.service.autofill.Dataset;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoud.bashir.evom_user_app.Adapters.RequestOnClickInterface;
import com.mahmoud.bashir.evom_user_app.Adapters.available_drivers_adpt;
import com.mahmoud.bashir.evom_user_app.Api_Interface.api_Interface;
import com.mahmoud.bashir.evom_user_app.Maps.Direction_route.TaskLoadedCallback;
import com.mahmoud.bashir.evom_user_app.R;
import com.mahmoud.bashir.evom_user_app.Services.Notification_Receiver;
import com.mahmoud.bashir.evom_user_app.Storage.SharedPrefranceManager;
import com.mahmoud.bashir.evom_user_app.loadingAlertdialog.LoadingDialog;
import com.mahmoud.bashir.evom_user_app.pojo.SendRequest.data_request;
import com.mahmoud.bashir.evom_user_app.pojo.driver_Info_Model;
import com.mahmoud.bashir.evom_user_app.ui.Payment_Activity;
import com.mahmoud.bashir.evom_user_app.ui.Settings_Activity;
import com.mahmoud.bashir.evom_user_app.ui.TripsActiviy;
import com.mahmoud.bashir.evom_user_app.ui.Wallet_Activity;
import com.squareup.picasso.Picasso;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Home_Maps_Activity extends AppCompatActivity implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener
    , GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener ,
        TaskLoadedCallback ,
        RequestOnClickInterface {

    private static final String TAG = "Home_Maps_Activity";
    private GoogleMap mMap , DMap;
    GoogleApiClient googleApiClient;
    Location lastLocation;
    LocationRequest locationRequest;


    LatLng mylatLng;
    LatLng placelatlng;
    Double dest_lat,dest_lng;


    //initviews
    EditText edt_to_destination;
    ImageView open_drawer,call_to_driver;
    TextView nav_user_name,nav_trips,nav_wallet,nav_payment,nav_packages,nav_settings,txt_request_status,trip_price,distance_v,Number_Car,txt_dr_name;
    View bottom_sheet;
    RecyclerView rec_btsheet,rec_available_drivers;
    RelativeLayout rel_dest,pending_rel,aft_rel,Info_dr_rel,cl_rel;
    CircleImageView dr_img;

    int PLACE_PICKER_REQUEST = 1;
    Intent nt;


    private BottomSheetBehavior mbottomSheetBehavior;
    LoadingDialog loadingDialog;
    private CountDownTimer timer;

    /**
     * permissions request code
     */
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };




    FirebaseDatabase database;
    DatabaseReference reference,request_ref;
    FirebaseAuth auth;
    String CUID;
    available_drivers_adpt available_drivers_adpt;
    driver_Info_Model driver_info_model;
    List<driver_Info_Model> driver_info_modelList;

    LatLng driver_latlng;



    String request_st="",MyPhone="",Driver_id,My_InRequest="out";


    //FCM section
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAWTfqi_A:APA91bHiuXVv9PZxm24FNqKLHN1Te6qz4OH9KsgJ9Vdzv-BoA-rrc8Sow2mo0E4AFHPbBamwgugD7vczzur1S-n1vKN58QbWMrXgBXHmb1osHnZbpX82ThaE7SP_n2wohQH67vx9c_Ma";
    final private String contentType = "application/json";
    final String TAGI = "NOTIFICATION TAG";
    String BaseURL="https://fcm.googleapis.com/";
    String driver_device_Token;



    //animation
    Animation topAnim,bottomAnim;
    AlarmManager manager;
    Intent myintent;
    PendingIntent pendingIntent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        loadingDialog=new LoadingDialog(this);

        checkGPSStatus();
        checkPermissions();

        //firebase initialization
        FirebaseApp.initializeApp(getApplicationContext());
        database = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Nearby_drivers");
        request_ref = FirebaseDatabase.getInstance().getReference().child("Requests");
        auth = FirebaseAuth.getInstance();
        CUID = auth.getCurrentUser().getUid();



        //init Views
        open_drawer = findViewById(R.id.open_drawer);
        edt_to_destination = findViewById(R.id.edt_to_destination);
        aft_rel = findViewById(R.id.aft_rel);
        Info_dr_rel = findViewById(R.id.Info_dr_rel);
        dr_img = findViewById(R.id.dr_img);
        txt_dr_name = findViewById(R.id.txt_dr_name);
        call_to_driver = findViewById(R.id.call_to_driver);
        Number_Car = findViewById(R.id.Number_Car);
        distance_v = findViewById(R.id.distance_v);
        trip_price = findViewById(R.id.trip_price);
        pending_rel = findViewById(R.id.pending_rel);
        cl_rel = findViewById(R.id.cl_rel);
        txt_request_status = findViewById(R.id.txt_request_status);




        //setAnimation
        edt_to_destination.setAnimation(topAnim);



        //recyclerView to view all available drivers
        rec_available_drivers = findViewById(R.id.rec_available_drivers);
        rec_available_drivers.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rec_available_drivers.setLayoutManager(horizontalLayoutManager);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //init for Nav Header
        View headerView = navigationView.getHeaderView(0);
        nav_user_name =headerView.findViewById(R.id.nav_user_name);
        nav_trips =headerView.findViewById(R.id.nav_trips);
        nav_wallet =headerView.findViewById(R.id.nav_wallet);
        nav_payment =headerView.findViewById(R.id.nav_payment);
        nav_packages =headerView.findViewById(R.id.nav_packages);
        nav_settings =headerView.findViewById(R.id.nav_settings);
        nav_user_name.setText(SharedPrefranceManager.getInastance(Home_Maps_Activity.this).getUsername());
        nav_trips.setOnClickListener(view -> {
            nt = new Intent(Home_Maps_Activity.this, TripsActiviy.class);
            startActivity(nt);
            drawer.closeDrawer(GravityCompat.START);
        });
        nav_wallet.setOnClickListener(view -> {
            nt = new Intent(Home_Maps_Activity.this, Wallet_Activity.class);
            startActivity(nt);
            drawer.closeDrawer(GravityCompat.START);
        });
        nav_payment.setOnClickListener(view -> {
            nt = new Intent(Home_Maps_Activity.this, Payment_Activity.class);
            startActivity(nt);
            drawer.closeDrawer(GravityCompat.START);
        });
        nav_packages.setOnClickListener(view -> {
            Toast.makeText(this, "packages", Toast.LENGTH_SHORT).show();
            drawer.closeDrawer(GravityCompat.START);
        });
        nav_settings.setOnClickListener(view -> {
            nt = new Intent(Home_Maps_Activity.this, Settings_Activity.class);
            startActivity(nt);
            drawer.closeDrawer(GravityCompat.START);
        });
        open_drawer.setOnClickListener(view -> {
            drawer.open();
        });





        edt_to_destination.setOnClickListener(v -> {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            if (lastLocation != null) {
                Intent intent = new PlacePicker.IntentBuilder()
                        .setLatLong(lastLocation.getLatitude(), lastLocation.getLongitude())
                        .showLatLong(true)
                        .setMarkerImageImageColor(R.color.colorRed)
                        .setMapRawResourceStyle(R.raw.drive_map_style)
                        .setMapType(MapType.NORMAL)
                        .build(Home_Maps_Activity.this);


                /*Intent intent = new PlacePicker.IntentBuilder()
                 .setLatLong(latLng.latitude, latLng.longitude)
                 .showLatLong(true)
                 .setAddressRequired(true)
                 .hideMarkerShadow(true)
                 .setMarkerImageImageColor(R.color.colorPrimary)
                 .setMapRawResourceStyle(R.raw.map_style)
                 .disableBootomSheetAnimation(true)
                  .onlyCoordinates(true)
                  .build(getBaseActivity());
                startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);*/

            if (isNetworkConnected()) {
                loadingDialog.startLoadingDialog();

                timer = new CountDownTimer(2000, 500) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        try{
                            loadingDialog.dismissDialog();
                            startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);
                        }catch(Exception e){
                            Log.e("Error", "Error: " + e.toString());
                        }
                    }
                }.start();

            } else {
                Toast.makeText(this, "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
            }

        }
        });

        MyPhone = SharedPrefranceManager.getInastance(this).getUserPhone();

        //check if user related with request or not
        CheckUserRequest(MyPhone);

       GetAvailableDrivers();
    }

    private void checkGPSStatus() {
        LocationManager locationManager = null;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        if ( locationManager == null ) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex){}
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex){}
        if ( !gps_enabled && !network_enabled ){
            AlertDialog.Builder dialog = new AlertDialog.Builder(Home_Maps_Activity.this);
            dialog.setMessage("GPS not enabled");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //this will navigate user to the device location settings screen
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            AlertDialog alert = dialog.create();
            alert.show();
        }
    }

    private void CheckUserRequest(String MyPhone) {

        My_InRequest = SharedPrefranceManager.getInastance(this).getStateRequestInOut();
        Driver_id = SharedPrefranceManager.getInastance(this).getDriverID();

        if (My_InRequest.equals("out")){
            pending_rel.setVisibility(View.GONE);
        }else if (My_InRequest.equals("in")){
            if (!Driver_id.equals("")) {
                request_ref.child("Drivers").child(Driver_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String UserPhone = snapshot.child("phone").getValue().toString();

                                if (MyPhone.equals(UserPhone)) {
                                    //Toast.makeText(Home_Maps_Activity.this, ""+Driver_id, Toast.LENGTH_SHORT).show();
                                    request_st = snapshot.child("requestStatus").getValue().toString();
                                    SharedPrefranceManager.getInastance(Home_Maps_Activity.this).save_RequestState("in", request_st, Driver_id);

                                    request_ref.child("Customers").child(MyPhone).child(Driver_id).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {


                                                    //Toast.makeText(Home_Maps_Activity.this, "worked", Toast.LENGTH_LONG).show();

                                                    String driverImage = dataSnapshot.child("driverImage").getValue().toString();
                                                    String driverName = dataSnapshot.child("driverName").getValue().toString();
                                                    String driverCarNumber = dataSnapshot.child("driverCarNumber").getValue().toString();
                                                    String driverPhone = dataSnapshot.child("driverPhone").getValue().toString();

                                                    dest_lat = Double.valueOf(dataSnapshot.child("dest_lat").getValue().toString());
                                                    dest_lng = Double.valueOf(dataSnapshot.child("dest_lng").getValue().toString());


                                                    edt_to_destination.setVisibility(View.GONE);
                                                    rec_available_drivers.setVisibility(View.GONE);
                                                    Info_dr_rel.setVisibility(View.VISIBLE);
                                                    Info_dr_rel.setAnimation(topAnim);
                                                    aft_rel.setVisibility(View.VISIBLE);
                                                    aft_rel.setAnimation(inFromRightAnimation());

                                                    Picasso.get().load(driverImage).into(dr_img);
                                                    txt_dr_name.setText(driverName);
                                                    Number_Car.setText(driverCarNumber);

                                                    call_to_driver.setOnClickListener(view -> {
                                                        dialContactPhone(driverPhone);
                                                    });

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    private void GetAvailableDrivers() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String id = snapshot.child("id").getValue().toString();
                        Double lat = Double.valueOf(snapshot.child("lat").getValue().toString());
                        Double lng = Double.valueOf(snapshot.child("lng").getValue().toString());

                        driver_latlng = new LatLng(lat,lng);


                        reference = FirebaseDatabase.getInstance().getReference().child("Users");
                        reference.child("Drivers").child(id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    if (dataSnapshot.hasChildren()){
                                        String email = dataSnapshot.child("email").getValue().toString();
                                        String full_name = dataSnapshot.child("full_name").getValue().toString();
                                        String phone_no = dataSnapshot.child("phone_no").getValue().toString();
                                        String driver_image = dataSnapshot.child("driver_image").getValue().toString();
                                        String device_Tokens = dataSnapshot.child("device_Tokens").getValue().toString();
                                        String CarNumber = dataSnapshot.child("CarNumber").getValue().toString();
                                        String CarModel = dataSnapshot.child("CarModel").getValue().toString();

                                        driver_info_model = new driver_Info_Model(id,lat,lng,full_name,phone_no,driver_image,CarModel,CarNumber,device_Tokens);
                                        driver_info_modelList = new ArrayList<>();
                                        driver_info_modelList.add(driver_info_model);
                                        //Toast.makeText(Home_Maps_Activity.this, ""+driver_info_modelList.get(0).getId(), Toast.LENGTH_SHORT).show();
                                        available_drivers_adpt = new available_drivers_adpt(Home_Maps_Activity.this,driver_info_modelList,Home_Maps_Activity.this);
                                        rec_available_drivers.setAdapter(available_drivers_adpt);
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                     }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.white_map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            // get current location
            /*if (lastLocation !=null) {
                LatLng mylatLng = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(mylatLng).title("موقعي الحالي").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mylatLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            }*/


        }


    }


    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        // بيعمل تحديث للوكيشن كل كام ثانية
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000); // 5 second
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY); // بتخليه يقرا اللوكيشن اسرع
        //track_curLocation();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED){
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;

        mylatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(mylatLng).title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        if (dest_lat != null){
            placelatlng= new LatLng(dest_lat,dest_lng);
            mMap.addMarker(new MarkerOptions().position(placelatlng).title("destination").icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));

            request_st = SharedPrefranceManager.getInastance(Home_Maps_Activity.this).getRequestWaitingResponse();

            if (request_st.equals("Pending")){


                request_ref.child("Customers").child(MyPhone).child(Driver_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                            request_st = dataSnapshot.child("requestStatus").getValue().toString();


                            if(request_st.equals("accept")){

                                txt_request_status.setText("accepted");
                                cl_rel.setBackgroundColor(Color.RED);
                                pending_rel.setVisibility(View.GONE);


                            }else if (request_st.equals("reject")){
                                SharedPrefranceManager.getInastance(Home_Maps_Activity.this).save_RequestState("out","","");
                                request_st = "" ;
                                txt_request_status.setText("rejected");
                                cl_rel.setBackgroundColor(Color.RED);
                                edt_to_destination.setAnimation(inFromLeftAnimation());
                                edt_to_destination.setVisibility(View.VISIBLE);
                                Info_dr_rel.setAnimation(outToRightAnimation());
                                Info_dr_rel.setVisibility(View.GONE);
                                aft_rel.setVisibility(View.GONE);
                                pending_rel.setVisibility(View.GONE);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }



        }

        if (driver_latlng != null){


            mMap.addMarker(new MarkerOptions().position(driver_latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_vehicle_icon_black)));





           /* CameraPosition cameraPosition = mMap.getCameraPosition();
            Toast.makeText(this, ""+cameraPosition.bearing + " " + cameraPosition.tilt, Toast.LENGTH_SHORT).show();
            CameraPosition newpos = new CameraPosition(
                    cameraPosition.target,
                    cameraPosition.zoom,
                    cameraPosition.tilt,
                    cameraPosition.bearing);
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(newpos));*/
        }
    }


    private void track_curLocation (){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            Location mloc=LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (mloc != null) {
                LatLng loc = new LatLng(mloc.getLatitude(),mloc.getLongitude());
                mMap.addMarker(new MarkerOptions().position(loc).title("موقعي الحالي").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                try {
                    AddressData addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);
                    String address = String.format("Place: %s",addressData.toString());
                    dest_lat = addressData.getLatitude();
                    dest_lng = addressData.getLongitude();
                    if (dest_lat != null){
                        rec_available_drivers.setVisibility(View.VISIBLE);
                        loadingDialog.startLoadingDialog();
                        timer = new CountDownTimer(2000, 500) {

                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                try{
                                    loadingDialog.dismissDialog();
                                    edt_to_destination.setVisibility(View.GONE);
                                  //  mbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                                    mMap.addMarker(new MarkerOptions().position(placelatlng).title("destination").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location)));
                                    //GetAvailableDrivers();
                                }catch(Exception e){
                                    Log.e("Error", "Error: " + e.toString());
                                }
                            }
                        }.start();
                    }
                } catch (Exception e) {
                    Log.e("MainActivity", e.getMessage());
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onTaskDone(Object... values) {

    }

   /* private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters+ "&key=" + "AIzaSyD7V1ZnulyCoKh6TRLo0HsdyGPjipIVhnY";
        String URL = "https://maps.googleapis.com/maps/api/directions/json?\n" +
                "origin=Boston,MA&destination=Concord,MA\n" +
                "&waypoints=Charlestown,MA|via:Lexington,MA"+
                "&key=" + "AIzaSyD7V1ZnulyCoKh6TRLo0HsdyGPjipIVhnY";
        return url;
    }*/


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }


    /**
     * Checks the dynamically-controlled permissions and requests missing permissions from end user.
     */
    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                // all permissions were granted
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
       /* Animation anim = AnimationUtils.loadAnimation(Home_Maps_Activity.this, R.anim.edittext_animation);
        edt_to_destination.startAnimation(anim);*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void OnClick(int pos,String driverName,String driverImg,String driverPhone,String driverToken,String driverID,String driverCarNumber,String driverCarModel,double driver_lat,double driver_lng) {

       /* Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api_Interface iterfaceCall=retrofit.create(api_Interface.class);*/

        if (lastLocation != null && dest_lat != null ) {


            // get current date and time with request
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = df.format(new Date());
            // get User Data to send
            String dToken = SharedPrefranceManager.getInastance(this).getUserToken();
            String name = SharedPrefranceManager.getInastance(this).getUsername();
            String phone = SharedPrefranceManager.getInastance(this).getUserPhone();

            data_request data = new data_request(CUID, dToken, name, phone, lastLocation.getLatitude(), lastLocation.getLongitude(), dest_lat, dest_lng, date,"Pending");

            //need to send request to firebase also
            HashMap<String,Object> map = new HashMap<>();
            map.put("driverId",driverID);
            map.put("driverName",driverName);
            map.put("driverPhone",driverPhone);
            map.put("driverImage",driverImg);
            map.put("driverCarNumber",driverCarNumber);
            map.put("driverToken",dToken);
            map.put("driver_lat",driver_lat);
            map.put("driver_lng",driver_lng);
            map.put("dest_lat",dest_lat);
            map.put("dest_lng",dest_lng);
            map.put("time",date);
            map.put("requestStatus","Pending");

           // double user_lat;
            //double user_lng;

            //added to User Database
            request_ref.child("Customers").child(phone).child(driverID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                    }
                }
            });

            request_ref.child("Drivers").child(driverID).child(phone).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                        request_st = "Pending";
                        My_InRequest = "in";
                        Driver_id = driverID;

                        SharedPrefranceManager.getInastance(Home_Maps_Activity.this).save_RequestState(My_InRequest,request_st,Driver_id);


                        Toast.makeText(Home_Maps_Activity.this, "Request Sent Successfully", Toast.LENGTH_SHORT).show();

                        edt_to_destination.setVisibility(View.GONE);
                        rec_available_drivers.setVisibility(View.GONE);
                        Info_dr_rel.setVisibility(View.VISIBLE);
                        Info_dr_rel.setAnimation(topAnim);
                        aft_rel.setVisibility(View.VISIBLE);
                        pending_rel.setVisibility(View.VISIBLE);
                        pending_rel.setAnimation(inFromLeftAnimation());
                        aft_rel.setAnimation(inFromRightAnimation());

                        Picasso.get().load(driverImg).into(dr_img);
                        txt_dr_name.setText(driverName);
                        Number_Car.setText(driverCarNumber);

                        setNotify(phone,driverID);


                        call_to_driver.setOnClickListener(view -> {
                            dialContactPhone(driverPhone);
                        });
                    }
                }
            });

        }
    }


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


    public void setNotify(String myPh,String DriverId) {

        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        myintent = new Intent(Home_Maps_Activity.this, Notification_Receiver.class);

        myintent.putExtra("myPh",myPh);
        myintent.putExtra("DriverId",DriverId);


        pendingIntent = PendingIntent.getBroadcast(Home_Maps_Activity.this, 0, myintent, 0);


        manager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 6000, 6000, pendingIntent);

        //manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+6000,pendingIntent);

    }
}