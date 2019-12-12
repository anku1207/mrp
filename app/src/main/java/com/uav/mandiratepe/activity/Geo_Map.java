package com.uav.mandiratepe.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uav.mandiratepe.LocationUtil.PermissionUtils;
import com.uav.mandiratepe.R;
import com.uav.mandiratepe.bo.LocationBO;
import com.uav.mandiratepe.permission.PermissionHandler;
import com.uav.mandiratepe.permission.Session;
import com.uav.mandiratepe.volley.VolleyResponseListener;
import com.uav.mandiratepe.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.uav.mandiratepe.util.Utility.showSingleButtonDialog;

public class Geo_Map extends FragmentActivity implements OnMapReadyCallback ,GoogleMap.OnMapClickListener, PermissionUtils.PermissionResultCallback,Google_Services_Interface {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;


    Double latitude;
    Double longitude;
    PermissionUtils permissionUtils;

    Button button;

    Google_Services google_services;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo__map);
        button = findViewById(R.id.button);

        button.setTextColor(Color.RED);
        google_services = new Google_Services(this, Geo_Map.this);

        startmaponactivitystart();

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            latitude = intent.getDoubleExtra("latitude", 0.0000000000000000);
            longitude = intent.getDoubleExtra("longitude", 0.0000000000000000);

        } else {
            button.setVisibility(View.VISIBLE);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionUtils = new PermissionUtils(Geo_Map.this);
                permissionUtils.check_permission(PermissionHandler.gpsLocationPermission(Geo_Map.this), "Need GPS permission for getting your location", 1);

            }
        });


    }
    public void startmaponactivitystart(){

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setOnMapClickListener(this);


        if (latitude != null && longitude != null) {
            //Place current location marker
            LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Your Location");
            markerOptions.snippet(Google_Services.getAddress(latLng));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
            // mGoogleMap.setMyLocationEnabled(true);


            float zoom = 14f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
            mGoogleMap.animateCamera(cameraUpdate);

            mCurrLocationMarker.showInfoWindow();

            //move map camera
        }else {
            //Place current location marker
            LatLng latLng = new LatLng(20.5937, 78.9629);

            float zoom = 4.5f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
            mGoogleMap.animateCamera(cameraUpdate);


        }



        //  LatLng location = new LatLng(28.395403, 77.315292);
       /* LatLng location = new LatLng(29.918679, 73.875079);




        markerForGeofence(location);
        drawGeofence();*/


    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    private Marker geoFenceMarker;

    private static final float GEOFENCE_RADIUS = 1000.0f; // in meters

    private void markerForGeofence(LatLng latLng) {
        Log.d("TAG", "markerForGeofence(" + latLng + ")");
        String title = latLng.latitude + ", " + latLng.longitude;
        // Define marker options
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title(title);


        if (mGoogleMap != null) {
            // Remove last geoFenceMarker
            if (geoFenceMarker != null)
                geoFenceMarker.remove();

            geoFenceMarker = mGoogleMap.addMarker(markerOptions);
        }
    }

    private Circle geoFenceLimits;

    private void drawGeofence() {
        Log.d("TAG", "drawGeofence()");

        if (geoFenceLimits != null)
            geoFenceLimits.remove();

        CircleOptions circleOptions = new CircleOptions()
                .center(geoFenceMarker.getPosition())
                .strokeColor(Color.argb(50, 70, 70, 70))
                .fillColor(Color.argb(100, 150, 150, 150))
                .radius(GEOFENCE_RADIUS);
        geoFenceLimits = mGoogleMap.addCircle(circleOptions);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION", "GRANTED");

        button.setVisibility(View.GONE);
        google_services.google_Service_LocationRequest();
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY", "GRANTED");

    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION", "DENIED");

    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION", "NEVER ASK AGAIN");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    @Override
    public void getLastLocation(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            verifylocation(latitude, longitude);
            // getAddress(address);

        } else {

            showToast("Couldn't get the location. Make sure location is enabled on the device");
        }
    }

    @Override
    public void onFailure(Exception e) {

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void verifylocation(final Double latitude, final Double longitude) {
        VolleyUtils.makeJsonObjectRequest(this, LocationBO.isGeoLocationServiceable(latitude, longitude), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;

                if (response.getString("status").equals("fail")) {


                    JSONArray error = response.getJSONArray("error");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < error.length(); i++) {
                        sb.append(error.get(i)).append("\n");
                    }

                    startmaponactivitystart();
                    showSingleButtonDialog(Geo_Map.this, "Error !", sb.toString(), false);
                } else {
                    Session.set_Data_Sharedprefence(Geo_Map.this, Session.CACHE_LOCATION, response.getJSONObject("dataList").toString());
                    finish();
                    startActivity(new Intent(Geo_Map.this, Product_List.class));
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            }
        });

    }
}