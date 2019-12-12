package com.uav.mandiratepe.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.uav.mandiratepe.R;

public class MandiClosed extends Base_Activity {
    TextView msgtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandi_closed);

        msgtext=findViewById(R.id.msgtext);
        msgtext.setText(getIntent().getStringExtra("msg"));
    }
}
