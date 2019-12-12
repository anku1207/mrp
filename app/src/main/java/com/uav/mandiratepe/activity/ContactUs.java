package com.uav.mandiratepe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uav.mandiratepe.R;
import com.uav.mandiratepe.bo.ContactUsBO;
import com.uav.mandiratepe.volley.VolleyResponseListener;
import com.uav.mandiratepe.volley.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactUs extends Base_Activity implements View.OnClickListener {
    TextView emailid,mobileno,timing;
    ImageView back_activity_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        mobileno=findViewById(R.id.mobileno);
        emailid=findViewById(R.id.emailid);
        timing=findViewById(R.id.timing);
        back_activity_button = findViewById(R.id.back_activity_button);
        back_activity_button.setOnClickListener(this);

        getcontactdetail();
    }
    private void getcontactdetail(){
            try {
                VolleyUtils.makeJsonObjectRequest(this, ContactUsBO.getContactus(), new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                    }
                    @Override
                    public void onResponse(Object resp) throws JSONException {
                        JSONObject response = (JSONObject) resp;

                        Log.w("object ",response.toString());

                        mobileno.setText(response.getString("phonenumber"));
                        emailid.setText(response.getString("emailId"));
                        timing.setText(response.getString("days") +" (" + response.getString("timings") +" )");


                        }



                });
            }catch (Exception e){
                Log.e("Error",e.getMessage());
            }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(ContactUs.this,Product_List.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_activity_button:
                startActivity(new Intent(ContactUs.this,Product_List.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
                break;
        }

    }
}
