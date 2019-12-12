package com.uav.mandiratepe.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uav.mandiratepe.MRPService.LogOutTimerUtil;
import com.uav.mandiratepe.MRPService.LogoutService;
import com.uav.mandiratepe.Notification.Config;
import com.uav.mandiratepe.Notification.NotificationUtils;

import com.uav.mandiratepe.R;
import com.uav.mandiratepe.constant.GlobalApplication;
import com.uav.mandiratepe.permission.Session;

import java.util.Date;

public class Base_Activity extends AppCompatActivity implements LogOutTimerUtil.LogOutListener{

    public static CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_);

        Log.w("onCreate","onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();

        LogOutTimerUtil.startLogoutTimer(this, this);
        Log.w("onStart","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("onResume","onResume");
        GlobalApplication.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.w("onPause","onPause");

        GlobalApplication.activityPaused();

        Date date =new Date();
        Session.set_Data_Sharedprefence(this,Session.CACHE_APPSESSION,String.valueOf(date.getTime()));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w("onStop","onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.w("onRestart","onRestart");

        long diffMs =new Date().getTime()- Long.parseLong(Session.getSessionByKey(this,Session.CACHE_APPSESSION));
        long diffSec = diffMs / 1000;
        long haltMinutes = diffSec / 60;
        long sec = diffSec % 60;

        if(haltMinutes>=GlobalApplication.ScreenOffSessionOutTime){
            startSessionOutActivity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w("onDestroy","onDestroy");
    }

    @Override
    public void doLogout() {
        this.runOnUiThread(new Runnable() {
            public void run() {
                 Log.w("logount","logout");
                 startSessionOutActivity();
            }
        });
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        LogOutTimerUtil.startLogoutTimer(this, this);
        Log.e("Touch", "User interacting with screen");
    }

    public void startSessionOutActivity(){
        Intent intent =new Intent(getApplicationContext(), SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
