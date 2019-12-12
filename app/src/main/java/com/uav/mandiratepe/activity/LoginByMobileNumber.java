package com.uav.mandiratepe.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.uav.mandiratepe.R;

public class LoginByMobileNumber extends AppCompatActivity implements View.OnClickListener {

    EditText mobileno;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_mobile_number);
        mobileno=findViewById(R.id.mobileno);
        login=findViewById(R.id.login);
        login.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:




                break;
        }
    }


}
