package com.uav.mandiratepe.MRPService;

import android.app.AlertDialog;
import android.app.IntentService;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;

import com.uav.mandiratepe.activity.Base_Activity;
import com.uav.mandiratepe.activity.SplashScreen;

public class LogoutService extends Service {




    public static CountDownTimer timer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();



        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {

                Log.w("onTick",String.valueOf(l));

            }

            @Override
            public void onFinish() {
                Log.w("logount","logout");
                Intent intent =new Intent(getApplicationContext(), SplashScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        };
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timer.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
