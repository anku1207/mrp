package com.uav.mandiratepe.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.uav.mandiratepe.R;
import com.uav.mandiratepe.adpater.Order_History_Adapter;
import com.uav.mandiratepe.bo.ProductBO;
import com.uav.mandiratepe.permission.Session;
import com.uav.mandiratepe.util.Utility;
import com.uav.mandiratepe.vo.OrderVO;
import com.uav.mandiratepe.volley.VolleyResponseListener;
import com.uav.mandiratepe.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class Order_History extends Base_Activity implements View.OnClickListener {
    ImageView back_activity_button;
    RecyclerView historyrecy;
    SpotsDialog  spotsDialog;
    LinearLayout notfound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__history);
        spotsDialog = new SpotsDialog(this, R.style.CustomDialogloading);

        back_activity_button=findViewById(R.id.back_activity_button);
        notfound=findViewById(R.id.notfound);
        notfound.setVisibility(View.GONE);

        historyrecy=findViewById(R.id.historyrecy);

        historyrecy.setHasFixedSize(true);
        historyrecy.setNestedScrollingEnabled(true);
        historyrecy.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));




        back_activity_button.setOnClickListener(this);

        getOrderHistoryByCustomerId();

    }




    public void getOrderHistoryByCustomerId(){
        try {
                VolleyUtils.makeJsonObjectRequest(this,ProductBO.orderHistory(Session.getCustomerId(Order_History.this)), new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                }
                @Override
                public void onResponse(Object resp) throws JSONException {
                    JSONObject response = (JSONObject) resp;

                    if(!response.getString("status").equals("success")){

                        JSONArray error =response.getJSONArray("error");
                        StringBuilder sb = new StringBuilder();
                        for(int i=0; i<error.length(); i++){
                            sb.append(error.get(i)).append("\n");
                        }
                        Utility.showSingleButtonDialog(Order_History.this,null,sb.toString(),false);


                    }else {
                        JSONArray jsonArray=response.getJSONArray("dataList");
                        Log.w("object ",jsonArray.toString());

                        int data_length=jsonArray.length();

                        List<OrderVO> orderVOS =new ArrayList<>();

                        for(int i=0;i<data_length ;i++){
                            JSONObject object =jsonArray.getJSONObject(i);
                            OrderVO orderVO =new OrderVO();
                            orderVO.setDeliveryCharges(Double.valueOf(object.get("deliveryCharges").toString()));
                            orderVO.setTotalItem(Double.valueOf(object.get("totalItem").toString()));
                            orderVO.setNetAmount(Double.valueOf(object.get("netAmount").toString()));
                            orderVO.setOrderId(object.getInt("orderId"));
                            orderVO.setGrossTotal(Double.valueOf(object.get("grossTotal").toString()));
                            orderVO.setTotalWeight(Double.valueOf(object.get("totalWeight").toString()));
                            orderVO.setStatusName(object.getString("statusName")+" ("+object.getString("orderDate")+")");
                            orderVO.setStatusId(object.getInt("statusId"));
                            orderVO.setColorCode(object.getString("colorCode"));
                            orderVOS.add(orderVO);
                        }

                        if(orderVOS.size()>0){
                            Order_History_Adapter single_textView_recyclerViewAdapter =new Order_History_Adapter(Order_History.this,orderVOS ,R.layout.order_history_design);
                            historyrecy.setAdapter(single_textView_recyclerViewAdapter);
                        }else {
                            notfound.setVisibility(View.VISIBLE);
                        }





                    }



                }
            });
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            if(requestCode==201){
                getOrderHistoryByCustomerId();
            }
        }

        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(Order_History.this,Product_List.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_activity_button:
                startActivity(new Intent(Order_History.this,Product_List.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
                break;
        }
    }
}
