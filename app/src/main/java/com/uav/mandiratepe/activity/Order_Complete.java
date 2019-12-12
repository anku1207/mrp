package com.uav.mandiratepe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Animatable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.uav.mandiratepe.R;
import com.uav.mandiratepe.constant.ApplicationConstant;
import com.uav.mandiratepe.permission.PermissionHandler;
import com.uav.mandiratepe.permission.Session;

import dmax.dialog.SpotsDialog;

public class Order_Complete extends Base_Activity implements View.OnClickListener {

    ImageView imageview;
    Button continueshopping;
    TextView orderid;
    SpotsDialog spotsDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__complete);
        continueshopping=findViewById(R.id.continueshopping);

        imageview=findViewById(R.id.imageview);

        orderid =findViewById(R.id.orderid);

        ((Animatable) imageview.getDrawable()).start();

        continueshopping.setOnClickListener(this);
        //orderid.setText("Order Number : " +getIntent().getStringExtra("orderid"));
        orderid.setText("\nThe delivery schedule time of your order number " +getIntent().getStringExtra("orderid") +" is " + getIntent().getStringExtra("deliveryTime") +"." + "\n\n"+ "You can view your orders by using click on three vertical dots(top right) on home screen." );




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.continueshopping:
                startActivity(new Intent(Order_Complete.this,Product_List.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Order_Complete.this,Product_List.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();

    }
}
