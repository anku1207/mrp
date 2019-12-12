package com.uav.mandiratepe.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.uav.mandiratepe.LocationUtil.PermissionUtils;
import com.uav.mandiratepe.R;
import com.uav.mandiratepe.bo.LocationBO;
import com.uav.mandiratepe.constant.GlobalApplication;
import com.uav.mandiratepe.permission.PermissionHandler;
import com.uav.mandiratepe.permission.Session;
import com.uav.mandiratepe.util.ExceptionHandler;
import com.uav.mandiratepe.util.Utility;
import com.uav.mandiratepe.volley.GetAppLatestVersion;
import com.uav.mandiratepe.volley.GetAppLatestVersionInterface;
import com.uav.mandiratepe.volley.VolleyResponseListener;
import com.uav.mandiratepe.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class SplashScreen extends AppCompatActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback, Google_Services_Interface , PermissionUtils.PermissionResultCallback{
    ProgressBar progressBar;



    LinearLayout vegitables;

    SpotsDialog dialog ;


    double latitude;
    double longitude;
    Google_Services google_services;
    private Location mLastLocation;
    boolean isPermissionGranted=false;

    PermissionUtils permissionUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        ImageView imageView = (ImageView) findViewById( R.id.appstarticon);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        dialog = new SpotsDialog(SplashScreen.this, R.style.CustomDialogloading);
        google_services = new Google_Services(SplashScreen.this, SplashScreen.this);

        if(!Utility.isNetworkAvailable(SplashScreen.this)){
            AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
            builder.setMessage("Sorry, no Internet Connectivity detected. Please reconnect and try again ")
                    .setTitle("No Internet Connection!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setCanceledOnTouchOutside(false);
            alert.setCancelable(false);
            alert.show();
        }else {
            FirebaseMessaging.getInstance().subscribeToTopic("genaral");
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w( "getInstanceId failed", task.getException());
                                return;
                            }
                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            Session.set_Data_Sharedprefence(SplashScreen.this,Session.CACHE_TOKENID,token);
                            Log.w("token",token);
                        }
                    });


            permissionUtils=new PermissionUtils(SplashScreen.this);
            permissionUtils.check_permission(PermissionHandler.gpsLocationPermission(SplashScreen.this),"Need GPS permission for getting your location",1);
            dialog.show();

        }




    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        dialog.show();
        google_services.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vegitables: {

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        google_services.checkPlayServices();
    }




    @Override
    public void getLastLocation(Location location) {
        dialog.dismiss();


        TextView address=findViewById(R.id.address);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            verifylocation(latitude,longitude);
            // getAddress(address);

        } else {

            showToast("Couldn't get the location. Make sure location is enabled on the device");
        }
    }


    public void  verifylocation(final Double latitude , final Double longitude){
        VolleyUtils.makeJsonObjectRequest(this, LocationBO.isGeoLocationServiceable(latitude,longitude), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;


                Log.w("verifylocation",response.toString());

                if(response.getString("status").equals("fail")){
                    JSONArray error =response.getJSONArray("error");
                    StringBuilder sb = new StringBuilder();
                    for(int i=0; i<error.length(); i++){
                        sb.append(error.get(i)).append("\n");
                    }
                    showSingleButtonDialog(SplashScreen.this,"",sb.toString(),false);



                }else {
                    JSONObject data=response.getJSONObject("dataList");

                    Session.set_Data_Sharedprefence(SplashScreen.this,Session.CACHE_LOCATION,data.toString());

                    FirebaseMessaging.getInstance().subscribeToTopic("mandi"+data.getString("mandiId"));

                    GlobalApplication.SessionOutTime=(data.getInt("aapSessionTimeOutMinutes")*60*1000);
                    GlobalApplication.ScreenOffSessionOutTime=data.getInt("aapSessionTimeOutMinutes");

                    GlobalApplication.updateMsg=data.getString("updateVersion");


                    Log.w("SessionOutTime", String.valueOf(GlobalApplication.SessionOutTime));
                    Log.w("ScreenOffSessionOutTime", String.valueOf(GlobalApplication.ScreenOffSessionOutTime));


                    if(response.getString("mandiStatus").equals("Closed")){

                        Intent i = new Intent(SplashScreen.this,MandiClosed.class);
                        i.putExtra("msg",response.getString("mandiClosedMsg"));
                        startActivity(i);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();
                    }else {
                        startActivity(new Intent(SplashScreen.this,Product_List.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();
                    }

                }
            }
        });

    }

    public  void showSingleButtonDialog(final Context var1, String error, String var2 , final boolean activityfinish ) {
        final Dialog var3 = new Dialog(var1);
        var3.requestWindowFeature(1);
        var3.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        var3.setContentView(var1.getResources().getIdentifier("singlebuttondialog", "layout", var1.getPackageName()));
        var3.setCanceledOnTouchOutside(false);
        TextView var4 = (TextView)var3.findViewById(var1.getResources().getIdentifier("dialog_one_tv_title", "id", var1.getPackageName()));
        var4.setText(error);
        TextView var6 = (TextView)var3.findViewById(var1.getResources().getIdentifier("dialog_one_tv_text", "id", var1.getPackageName()));

        var6.setText(var2);
        Button var5 = (Button)var3.findViewById(var1.getResources().getIdentifier("dialog_one_btn", "id", var1.getPackageName()));
        var5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View var) {
                var3.dismiss();

                Intent intent =new Intent(SplashScreen.this,Geo_Map.class);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }
        });
        var3.show();
    }

    @Override
    public void onFailure(Exception e) {

    }

    private void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        permissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);


    }

    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION","GRANTED");
        dialog.dismiss();
        google_services.google_Service_LocationRequest();
        isPermissionGranted=true;

    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY","GRANTED");
        startgeoactivity();
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION","DENIED");
        startgeoactivity();
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION","NEVER ASK AGAIN");
        startgeoactivity();
    }

    public void startgeoactivity(){
        dialog.dismiss();
        startActivity(new Intent(SplashScreen.this,Geo_Map.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
        fileList();
    }


}


