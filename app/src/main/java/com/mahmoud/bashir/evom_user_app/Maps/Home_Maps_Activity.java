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
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoud.bashir.evom_user_app.Adapters.available_drivers_adpt;
import com.mahmoud.bashir.evom_user_app.Maps.Direction_route.TaskLoadedCallback;
import com.mahmoud.bashir.evom_user_app.R;
import com.mahmoud.bashir.evom_user_app.loadingAlertdialog.LoadingDialog;
import com.mahmoud.bashir.evom_user_app.pojo.driver_Info_Model;
import com.mahmoud.bashir.evom_user_app.ui.Settings_Activity;
import com.mahmoud.bashir.evom_user_app.ui.Wallet_Activity;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Home_Maps_Activity extends AppCompatActivity implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener
    , GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener ,
        TaskLoadedCallback {

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
    ImageView open_drawer;
    TextView nav_trips,nav_wallet,nav_payment,nav_packages,nav_settings;
    View bottom_sheet;
    RecyclerView rec_btsheet;

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
    DatabaseReference reference;
    FirebaseAuth auth;
    String CUID;
    available_drivers_adpt available_drivers_adpt;
    driver_Info_Model driver_info_model;
    List<driver_Info_Model> driver_info_modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        loadingDialog=new LoadingDialog(this);

        //ButterKnife.bind(this);
        checkPermissions();

        //firebase initialization
        FirebaseApp.initializeApp(getApplicationContext());
        database = FirebaseDatabase.getInstance();

        //init Views
        open_drawer = findViewById(R.id.open_drawer);
        edt_to_destination = findViewById(R.id.edt_to_destination);
        bottom_sheet=findViewById(R.id.bottom);
        mbottomSheetBehavior=BottomSheetBehavior.from(bottom_sheet);
        mbottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        rec_btsheet = findViewById(R.id.rec_btsheet);
        rec_btsheet.setHasFixedSize(true);
        rec_btsheet.setLayoutManager(new LinearLayoutManager(this));



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        nav_trips =headerView.findViewById(R.id.nav_trips);
        nav_wallet =headerView.findViewById(R.id.nav_wallet);
        nav_payment =headerView.findViewById(R.id.nav_payment);
        nav_packages =headerView.findViewById(R.id.nav_packages);
        nav_settings =headerView.findViewById(R.id.nav_settings);
        nav_trips.setOnClickListener(view -> {
            Toast.makeText(this, "yrysgfdhjtyyhj", Toast.LENGTH_SHORT).show();
            drawer.closeDrawer(GravityCompat.START);
        });
        nav_wallet.setOnClickListener(view -> {
            nt = new Intent(Home_Maps_Activity.this, Wallet_Activity.class);
            startActivity(nt);
            drawer.closeDrawer(GravityCompat.START);
        });
        nav_payment.setOnClickListener(view -> {
            Toast.makeText(this, "payment", Toast.LENGTH_SHORT).show();
            drawer.closeDrawer(GravityCompat.START);
        });
        nav_packages.setOnClickListener(view -> {
            Toast.makeText(this, "packages", Toast.LENGTH_SHORT).show();
            drawer.closeDrawer(GravityCompat.START);
        });
        nav_settings.setOnClickListener(view -> {
            startActivity(new Intent(Home_Maps_Activity.this, Settings_Activity.class));
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

        GetAvailableDrivers();
    }

    private void GetAvailableDrivers() {



        reference = FirebaseDatabase.getInstance().getReference().child("Nearby_drivers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String id = snapshot.child("id").getValue().toString();
                        Double lat = Double.valueOf(snapshot.child("lat").getValue().toString());
                        Double lng = Double.valueOf(snapshot.child("lng").getValue().toString());
                        //Toast.makeText(Home_Maps_Activity.this, ""+id, Toast.LENGTH_SHORT).show();
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
                                        available_drivers_adpt = new available_drivers_adpt(Home_Maps_Activity.this,driver_info_modelList);
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
                rec_btsheet.setAdapter(available_drivers_adpt);
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
            mMap.addMarker(new MarkerOptions().position(placelatlng).title("destination").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location)));
        }
       // track_curLocation();
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
                        bottom_sheet.setVisibility(View.VISIBLE);
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
                                    mbottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                                    mMap.addMarker(new MarkerOptions().position(placelatlng).title("destination").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location)));
                                    GetAvailableDrivers();
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


}