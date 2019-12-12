package com.uav.mandiratepe.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uav.mandiratepe.R;
import com.uav.mandiratepe.adpater.Order_History_Adapter;
import com.uav.mandiratepe.adpater.Order_Summary_Adapter;
import com.uav.mandiratepe.bo.ProductBO;
import com.uav.mandiratepe.util.Utility;
import com.uav.mandiratepe.vo.OrderVO;
import com.uav.mandiratepe.vo.ProductVO;
import com.uav.mandiratepe.volley.VolleyResponseListener;
import com.uav.mandiratepe.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Order_Summary extends Base_Activity implements View.OnClickListener {
    RecyclerView ordredetail,productsummary;
    TextView address,ordernumber;
    Integer orderid=null;
    LinearLayout mainlayout;
    CardView addresslayout;
    ImageView back_activity_button;
    Button cancelorder;
    OrderVO orderVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__summary);

        productsummary=findViewById(R.id.productsummary);
        ordredetail=findViewById(R.id.ordredetail);
        address=findViewById(R.id.address);
        mainlayout=findViewById(R.id.mainlayout);
        mainlayout.setVisibility(View.GONE);

        addresslayout=findViewById(R.id.addresslayout);
        cancelorder=findViewById(R.id.cancelorder);
        ordernumber=findViewById(R.id.ordernumber);

        cancelorder.setVisibility(View.GONE);


        orderid= Integer.parseInt(getIntent().getStringExtra("orderid"));

        ordredetail.setHasFixedSize(true);
        ordredetail.setNestedScrollingEnabled(true);
        ordredetail.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        productsummary.setHasFixedSize(true);
        productsummary.setNestedScrollingEnabled(true);
        productsummary.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));



        back_activity_button =  findViewById(R.id.back_activity_button);
        back_activity_button.setOnClickListener(this);
        cancelorder.setOnClickListener(this);


        getOrderHistoryByCustomerId();
    }


    public void getOrderHistoryByCustomerId(){
        try {
            VolleyUtils.makeJsonObjectRequest(this, ProductBO.getOrderByIdProductList(orderid), new VolleyResponseListener() {
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
                        Utility.showSingleButtonDialog(Order_Summary.this,null,sb.toString(),false);


                    }else {
                        mainlayout.setVisibility(View.VISIBLE);
                        JSONObject jsonObject=response.getJSONObject("dataList");
                        Log.w("object ",jsonObject.toString());

                        List<OrderVO> orderVOS =new ArrayList<>();
                        orderVO =new OrderVO();
                        orderVO.setDeliveryCharges(Double.valueOf(jsonObject.get("deliveryCharges").toString()));
                        orderVO.setTotalItem(Double.valueOf(jsonObject.get("totalItem").toString()));
                        orderVO.setNetAmount(Double.valueOf(jsonObject.get("netAmount").toString()));
                        orderVO.setOrderId(jsonObject.getInt("orderId"));
                        orderVO.setGrossTotal(Double.valueOf(jsonObject.get("grossTotal").toString()));
                        orderVO.setTotalWeight(Double.valueOf(jsonObject.get("totalWeight").toString()));
                        orderVO.setDesc("order");
                        orderVO.setStatusName(jsonObject.getString("statusName")+" ("+jsonObject.getString("orderDate")+")");
                        orderVO.setStatusId(jsonObject.getInt("statusId"));
                        orderVO.setColorCode(jsonObject.getString("colorCode"));
                        orderVO.setOrderNo(jsonObject.getString("orderNo"));
                        orderVO.setOrderDate(jsonObject.getString("orderDate"));
                        ordernumber.setText("Order Number : "+orderVO.getOrderNo());

                        orderVOS.add(orderVO);



                        if(jsonObject.getInt("statusId")== OrderVO.BOOKED && jsonObject.getString("batchClose").equals("no")){
                            cancelorder.setVisibility(View.VISIBLE);
                        }

                        Order_History_Adapter single_textView_recyclerViewAdapter =new Order_History_Adapter(Order_Summary.this,orderVOS ,R.layout.order_history_design);
                        ordredetail.setAdapter(single_textView_recyclerViewAdapter);

                        if(jsonObject.has("customerAddress")){
                            address.setText(jsonObject.getString("customerAddress"));
                            addresslayout.setVisibility(View.VISIBLE);
                        }


                        JSONArray productarry=jsonObject.getJSONArray("productList");

                        int productlength =productarry.length();


                        List<ProductVO> productVOS =new ArrayList<>();
                        for(int i=0;i<productlength;i++){
                            JSONObject object =productarry.getJSONObject(i);
                            ProductVO productVO =new ProductVO();
                            productVO.setTotal(String.valueOf(object.get("totalAmount")));
                            productVO.setProductName(object.getString("productNameEnglish"));
                            productVO.setUtfCode(object.getString("productNameHindi"));
                            productVO.setDescription(object.getInt("qty")+" "+object.getString("unitTypeName")+" "+" @ "+getApplication().getString(R.string.Rs)+" "+object.getString("rate"));
                            productVOS.add(productVO);

                        }


                        Order_Summary_Adapter aVoid =new Order_Summary_Adapter(Order_Summary.this,productVOS ,R.layout.order_sammary_design);

                        productsummary.setAdapter(aVoid);



                    }



                }
            });
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_activity_button:
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
            case R.id.cancelorder :
                showDoubleButtonDialog(Order_Summary.this,
                        "I would like to kindly ask you to cancel our order number " + orderVO.getOrderNo() +" which we made on "+ orderVO.getOrderDate() );
                break;
        }
    }

    public  void showDoubleButtonDialog(final Context var1, String textmsg) {
        final Dialog var3 = new Dialog(var1);
        var3.requestWindowFeature(1);
        var3.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        var3.setContentView(R.layout.dialog_two_button);
        var3.setCanceledOnTouchOutside(false);
        TextView msg = (TextView)var3.findViewById(R.id.msg);
        msg.setText(textmsg);

        Button cancel = (Button)var3.findViewById(R.id.btn_cancel);
        Button ok = (Button)var3.findViewById(R.id.btn_ok);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View var) {
                var3.dismiss();

            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View var) {
                var3.dismiss();

                cancelorder(var3);

            }
        });
        var3.show();
    }
    public  void cancelorder(final Dialog dialog){

        try {
            VolleyUtils.makeJsonObjectRequest(this, ProductBO.setOrderCancel(orderVO.getOrderId()), new VolleyResponseListener() {
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
                        Utility.showSingleButtonDialog(Order_Summary.this,null,sb.toString(),false);
                    }else {
                        dialog.dismiss();
                        Log.w("object ",response.toString());
                        showSingleButtonDialog(Order_Summary.this,"",response.getString("message"),true);
                    }



                }
            });
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }

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

                    Intent intent12 = new Intent();
                    intent12.putExtra("value","cancel");
                    setResult(RESULT_OK,intent12);
                    finish() ;
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
        var3.show();
    }
}
