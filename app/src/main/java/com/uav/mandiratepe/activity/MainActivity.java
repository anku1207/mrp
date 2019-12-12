package com.uav.mandiratepe.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import dmax.dialog.SpotsDialog;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MainActivity extends Base_Activity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback, Google_Services_Interface , PermissionUtils.PermissionResultCallback {

    LinearLayout vegitables;

    AlertDialog dialog ;


    double latitude;
    double longitude;
    Google_Services google_services;
    private Location mLastLocation;
    boolean isPermissionGranted=false;

    PermissionUtils permissionUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = (GridView) findViewById(R.id.grid);
        vegitables = findViewById(R.id.vegitables);
        vegitables.setOnClickListener(this);

        dialog = new SpotsDialog(this);
        google_services = new Google_Services(this, MainActivity.this);

        permissionUtils=new PermissionUtils(MainActivity.this);
        permissionUtils.check_permission(PermissionHandler.gpsLocationPermission(MainActivity.this),"Need GPS permission for getting your location",1);

        dialog.show();

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

                if(response.getString("status").equals("fail")){
                    JSONArray error =response.getJSONArray("error");
                    StringBuilder sb = new StringBuilder();
                    for(int i=0; i<error.length(); i++){
                        sb.append(error.get(i)).append("\n");
                    }
                    showSingleButtonDialog(MainActivity.this,"Error !",sb.toString(),false);



                }else {

                    Session.set_Data_Sharedprefence(MainActivity.this,Session.CACHE_LOCATION,response.getJSONObject("dataList").toString());
                    finish();
                    startActivity(new Intent(MainActivity.this,Product_List.class));
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
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

                Intent intent =new Intent(MainActivity.this,Geo_Map.class);
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

      /*  switch (requestCode){
            case 100:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //If user presses allow
                        // check availability of play services
                    dialog.show();
                     google_services.google_Service_LocationRequest();
                } else {
                    //If user presses deny
                    PermissionHandler.gpsLocationPermission(MainActivity.this);

                }
                break;
        }*/
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
        startActivity(new Intent(MainActivity.this,Geo_Map.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        fileList();
    }
}
