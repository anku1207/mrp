package com.uav.mandiratepe.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.uav.mandiratepe.R;
import com.uav.mandiratepe.adpater.AreaSpinAdapter;
import com.uav.mandiratepe.adpater.Customer_Address_RecyclerViewAdapter;
import com.uav.mandiratepe.bo.PincodeBO;
import com.uav.mandiratepe.bo.ProductBO;
import com.uav.mandiratepe.constant.ApplicationConstant;
import com.uav.mandiratepe.constant.ErrorConstant;
import com.uav.mandiratepe.permission.Session;
import com.uav.mandiratepe.util.Utility;
import com.uav.mandiratepe.vo.AreaVO;
import com.uav.mandiratepe.vo.CityVO;
import com.uav.mandiratepe.vo.ConnectionVO;
import com.uav.mandiratepe.vo.CustomerAddressVO;
import com.uav.mandiratepe.vo.ProductVO;
import com.uav.mandiratepe.vo.StateVO;
import com.uav.mandiratepe.volley.VolleyResponseListener;
import com.uav.mandiratepe.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class Check_Out extends Base_Activity implements View.OnClickListener {
    public static RecyclerView recyclerView;
    RadioGroup radiogroup;

    public static Integer addressposstion;

    TextView amount,totalamount,deliveryamount,totalweight,totalunit ,deliverytitle;

    public static LinearLayout addresslayout;
    TextView moreshopping;
    Button placeorder;
    ImageView deliveryinfo;


    public static EditText username,usermobile,useraddress,userlandmark,pincode,city,state, area;



    public static List<AreaVO> areaVOS =new ArrayList<AreaVO>();
    public static AreaVO areaVO;
    public static ArrayAdapter<AreaVO> areaAdapter;

    SpotsDialog spotsDialog;
    CheckBox checkdetail;
    SharedPreferences sharedPreferences;
    Integer deliverytimepos;
    JSONArray deliverytimearry;
    JSONObject location;

    SpotsDialog dialog ;
    String Deliveryinfo;
    int REQ_AREA=1001;
    public static Integer areaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check__out);

        amount=findViewById(R.id.amount);
        totalamount=findViewById(R.id.totalamount);
        deliveryamount=findViewById(R.id.deliveryamount);
        radiogroup=findViewById(R.id.radiogroup);
        deliveryinfo=findViewById(R.id.deliveryinfo);

        dialog = new SpotsDialog(this, R.style.CustomDialogloading);

        areaId=null;
        addressposstion=null;
        deliverytimepos=null;

        username=findViewById(R.id.username);
        usermobile=findViewById(R.id.usermobile);
        useraddress=findViewById(R.id.useraddress);
        userlandmark=findViewById(R.id.userlandmark);
        area =findViewById(R.id.area);
        pincode=findViewById(R.id.pincode);
        city=findViewById(R.id.city);
        state=findViewById(R.id.state);
        placeorder=findViewById(R.id.placeorder);
        checkdetail=findViewById(R.id.checkdetail);
        moreshopping=findViewById(R.id.moreshopping);

        totalweight=findViewById(R.id.totalweight);
        totalunit=findViewById(R.id.totalunit);
        deliverytitle=findViewById(R.id.deliverytitle);

        addresslayout=findViewById(R.id.addresslayout);

        spotsDialog = new SpotsDialog(this, R.style.CustomDialogloading);
        spotsDialog.show();


        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        sharedPreferences = Check_Out.this.getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);



        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                deliverytimepos=i;
            }
        });

        deliveryinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeliveryInfo();
            }
        });


        placeorder.setOnClickListener(this);
        moreshopping.setOnClickListener(this);
        pincode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean gainFocus) {
                //onFocus
                if (gainFocus) {
                    //set the row background to a different color
                }
                //onBlur
                else {
                    if (pincode.length() < 6) {
                        pincode.setError(Utility.getErrorSpannableString(getApplicationContext(),  "Plz enter at least 6 characters"));
                        city.setText("");
                        state.setText("");
                    }
                }
            }
        });

        pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(addressposstion==null){
                    if (pincode.length() == 6) {
                        pincodebycity();
                    }
                }

            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        area.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {
                    ConnectionVO connectionVO = new ConnectionVO();
                    connectionVO.setTitle("Area");
                    connectionVO.setSharedPreferenceKey(ApplicationConstant.CACHE_TITLE);
                    connectionVO.setEntityIdKey("value");
                    connectionVO.setEntityTextKey("name");
                    Intent intent = new Intent(getApplicationContext(),ListViewSingleText.class);
                    intent.putExtra(ApplicationConstant.INTENT_EXTRA_CONNECTION,  connectionVO);
                    startActivityForResult(intent,REQ_AREA);
                }
                return true; // return is important...
            }
        });



        getCheckOutDataInServer();
    }


    public void showDeliveryInfo(){





        final Dialog var3 = new Dialog(this);
        var3.requestWindowFeature(1);
        var3.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        var3.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        var3.setContentView(this.getResources().getIdentifier("singlebuttondialog", "layout", this.getPackageName()));
        var3.setCanceledOnTouchOutside(false);
        TextView var4 = (TextView)var3.findViewById(this.getResources().getIdentifier("dialog_one_tv_title", "id", this.getPackageName()));
        var4.setText(null);
        TextView var6 = (TextView)var3.findViewById(this.getResources().getIdentifier("dialog_one_tv_text", "id", this.getPackageName()));

        var6.setText(Deliveryinfo);
        Button var5 = (Button)var3.findViewById(this.getResources().getIdentifier("dialog_one_btn", "id", this.getPackageName()));
        var5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View var) {
                var3.dismiss();

            }
        });
        var3.show();

    }

    public  void pincodebycity(){
        VolleyUtils.makeJsonObjectRequest(this, PincodeBO.isPincodeServiceable(pincode.getText().toString(),Session.getServingGeoLocationId(Check_Out.this)), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;
                Log.w("pincoderesp",response.toString());

                if(!response.getString("status").equals("success")){
                    JSONArray error =response.getJSONArray("error");
                    StringBuilder sb = new StringBuilder();
                    for(int i=0; i<error.length(); i++){
                        sb.append(error.get(i)).append("\n");
                    }
                    Utility.showSingleButtonDialog(Check_Out.this,null,sb.toString(),false);
                    pincode.setEnabled(true);
                    city.setTag(null);
                    state.setTag(null);

                }else {
                    JSONObject object=response.getJSONObject("dataList");
                    city.setText(object.getString("cityName"));
                    state.setText(object.getString("stateName"));
                    city.setTag(object.getString("cityId"));
                    state.setTag(object.getString("stateId"));

                    city.setError(null);
                    state.setError(null);
                    pincode.setEnabled(false);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {

                if(requestCode==REQ_AREA){
                    area.setText(data.getStringExtra("valueName"));
                    areaId= Integer.parseInt(data.getStringExtra("valueId"));

                    area.setError(null);
                }
            }
        } catch (Exception e) {
            Utility.exceptionAlertDialog(Check_Out.this,"Alert!","Something went wrong, Please try again!","Report",Utility.getStackTrace(e));        }
    }


    public void getCheckOutDataInServer(){
        try {

            location=new JSONObject(Session.getSessionByKey(Check_Out.this,Session.CACHE_LOCATION));

            ArrayList<ProductVO> productVOS = Session.getItemList(Check_Out.this);

            JSONArray productarr=new JSONArray();
            for(ProductVO productVO : productVOS){
                JSONObject object =new JSONObject();
                object.put("qty",productVO.getQty());
                JSONObject projson=new JSONObject();
                projson.put("productId",productVO.getProductID());
                object.put("product",projson);
                productarr.put(object);
            }


            JSONObject customerjson=new JSONObject();
            customerjson.put("customerId",Session.getCustomerId(Check_Out.this));
            JSONObject mandijson=new JSONObject();
            mandijson.put("mandiId",Session.getMandiId(Check_Out.this));


            ConnectionVO  connectionVO=ProductBO.checkout();
            HashMap<String, Object> params = new HashMap<String, Object>();

            params.put("customer", customerjson);
            params.put("mandi",mandijson);
            params.put("orderProductVOs",productarr.toString());

            connectionVO.setParams(params);
            VolleyUtils.makeJsonObjectRequest(this,connectionVO, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                }
                @Override
                public void onResponse(Object resp) throws JSONException {
                    JSONObject response = (JSONObject) resp;

                    if(!response.getString("status").equals("success")){

                    }else {
                        JSONObject object=response.getJSONObject("dataList");
                        Log.w("object ",object.toString());



                        JSONObject orderidjaon=object.getJSONObject("orderDetail");
                        Session.set_Data_Sharedprefence(Check_Out.this,Session.CACHE_ORDERID,orderidjaon.getString("orderId"));

                        JSONObject jsonObject =object.getJSONObject("cartAmountDetail");
                        amount.setText(getApplication().getString(R.string.Rs)+" "+jsonObject.getInt("grossTotal"));
                        totalamount.setText(getApplication().getString(R.string.Rs)+" "+jsonObject.getInt("netAmount"));
                        deliveryamount.setText(getApplication().getString(R.string.Rs)+" "+jsonObject.getInt("deliveryCharges"));
                        totalunit.setText(""+jsonObject.getInt("totalItem"));
                        totalweight.setText(""+(jsonObject.getString("totalWeight")));

                        List<CustomerAddressVO> customerAddressVOS =new ArrayList<>();
                        JSONArray  jsonArray=object.getJSONArray("customerAddressList");

                        customerAddressVOS.add(null);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object1=jsonArray.getJSONObject(i);

                            CustomerAddressVO customerAddressVO =new CustomerAddressVO();
                            customerAddressVO.setFullAddress(object1.getString("customerName")+"\n"+object1.getString("address1")+"\n"+object1.getString("landMark")+"\n"+object1.getString("stateRegionName")+"\n"+object1.getString("cityName")+"\n"+object1.getString("pincode")+"\n"+object1.getString("mobileNumber"));

                            customerAddressVO.setMobileNumber(object1.getString("mobileNumber"));
                            customerAddressVO.setAddress1(object1.getString("address1"));
                            customerAddressVO.setAddress2("");
                            customerAddressVO.setLandMark(object1.getString("landMark"));
                            customerAddressVO.setPincode(object1.getString("pincode"));
                            customerAddressVO.setCustomerAddressId(object1.getInt("customerAddressId"));
                            customerAddressVO.setCustomerName(object1.getString("customerName"));
                            customerAddressVO.setAreaId( object1.has("areaId")? object1.getInt("areaId"):null);
                            customerAddressVO.setAreaName( object1.has("areaName")? object1.getString("areaName"):null);

                            CityVO cityVO =new CityVO();
                            cityVO.setCityName(object1.getString("cityName"));
                            customerAddressVO.setCity(cityVO);

                            StateVO stateVO =new StateVO();
                            stateVO.setStateRegionName(object1.getString("stateRegionName"));
                            customerAddressVO.setStateRegion(stateVO);

                            customerAddressVOS.add(customerAddressVO);
                        }
                        /*set Areas which are covered against mandi*/


                        JSONArray  areaJsonArray=object.getJSONArray("areaList");

                        JSONArray areaDTOs= new JSONArray();
                        for(int i=0;i<areaJsonArray.length();i++){
                            JSONObject areaJSON=areaJsonArray.getJSONObject(i);
                            JSONObject areaDTO=new JSONObject();

                            areaDTO.put("name",areaJSON.getString("areaName"));
                            areaDTO.put("value",areaJSON.getInt("areaId"));
                            areaDTOs.put(areaDTO);

                        }

                        JSONObject areaJSON = new JSONObject();
                        areaJSON.put("data",areaDTOs);
                        SharedPreferences.Editor edit= sharedPreferences.edit();
                        edit.putString( ApplicationConstant.CACHE_TITLE, areaJSON.toString());
                        edit.apply();
                        edit.commit();


                        Customer_Address_RecyclerViewAdapter single_textView_recyclerViewAdapter =new Customer_Address_RecyclerViewAdapter(Check_Out.this,customerAddressVOS ,R.layout.address_design);
                        recyclerView.setAdapter(single_textView_recyclerViewAdapter);

                        Deliveryinfo=object.getString("delChrgDetail");

                        deliverytimearry =object.getJSONArray("delTime");
                        for(int j=0;j<deliverytimearry.length();j++){

                            JSONObject object1 =deliverytimearry.getJSONObject(j);

                            Date date=new Date();
                            date =new Date(object1.getLong("from"));
                            String dt=Utility.convertDate2String(date,"dd/MM/yyyy");

                            String tdt=Utility.convertDate2String(date,"hh:mm a");


                            date =new Date(object1.getLong("To"));
                            String fdt=Utility.convertDate2String(date,"hh:mm a");


                            object1.put("deliveryTime",dt +"  ("+tdt+" - "+fdt  +")");
                            RadioButton rdbtn = new RadioButton(Check_Out.this);
                            rdbtn.setId(j);

                            rdbtn.setText(object1.getString("deliveryTime"));
                            rdbtn.setChecked(j==0);


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                rdbtn.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(Check_Out.this, R.color.colorPrimary)));
                            }
                            radiogroup.addView(rdbtn);
                        }




                        spotsDialog.dismiss();

                    }
                }
            });
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.placeorder:

                dialog.show();

                if(checkdetail.isChecked()){

                    checkdetail.setTextColor(getResources().getColor(R.color.textColor));
                    if(addressposstion==null){
                        if(checkEmptyEle()){
                           placeOrder(addressposstion);
                        }

                    }else {
                        placeOrder(addressposstion);
                    }

                }else{
                    checkdetail.setTextColor(Color.RED);
                    dialog.dismiss();
                }
                break;

            case R.id.moreshopping:
                startActivity(new Intent(Check_Out.this,Product_List.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
                break;
        }
    }

    public boolean checkEmptyEle(){
        if(addresslayout.getVisibility()!= View.VISIBLE){
            addresslayout.setVisibility(View.VISIBLE);
            TranslateAnimation animate = new TranslateAnimation(
                    0,
                    0,
                    30,
                    0);
            animate.setDuration(500);
            animate.setFillAfter(true);
            Check_Out.addresslayout.startAnimation(animate);
        }
        deliverytitle.setTextColor(getResources().getColor(R.color.textColor));
        boolean verify=true;
        if(username.getText().toString().equals("")){
            username.setError(ErrorConstant.FIELD_REQUIRED);
            verify=false;
        }
        if(usermobile.getText().toString().equals("")){
            usermobile.setError(ErrorConstant.FIELD_REQUIRED);
            verify=false;
        }
        if(useraddress.getText().toString().equals("")){
            useraddress.setError(ErrorConstant.FIELD_REQUIRED);
            verify=false;
        }

        if(areaId==null){
            area.setError(ErrorConstant.FIELD_REQUIRED);
            verify=false;
        }
        /*if(userlandmark.getText().toString().equals("")){
            userlandmark.setError("this filed is required");
            verify=false;
        }*/
        if(pincode.getText().toString().equals("")){
            pincode.setError(ErrorConstant.FIELD_REQUIRED);
            verify=false;
        }
        if(city.getText().toString().equals("")){
            city.setError(ErrorConstant.FIELD_REQUIRED);
            verify=false;
        }
        if(state.getText().toString().equals("")){
            state.setError(ErrorConstant.FIELD_REQUIRED);
            verify=false;
        }
        if(deliverytimepos==null){
            deliverytitle.setTextColor(Color.RED);
            verify=false;

        }

        if (!usermobile.getText().toString().equals("") &&  Utility.validatePattern(usermobile.getText().toString().trim(), ApplicationConstant.MOBILENO_VALIDATION)!=null){
            usermobile.setError(Utility.validatePattern(usermobile.getText().toString().trim(),ApplicationConstant.MOBILENO_VALIDATION));
            verify=false;
        }

        if(!verify){
            dialog.dismiss();
        }

        return verify;

    }

    public void placeOrder(Integer address_type){
        boolean valid=true;

        try{


            if( Session.getMandiId(Check_Out.this)==null){
                showSingleButtonDialog(Check_Out.this,"Error !","mandi id not found",true);
                valid=false;
            }else if(Session.getCustomerId(Check_Out.this)==null){
                showSingleButtonDialog(Check_Out.this,"Error !","Customer  not found",true);
                valid=false;
            }else if(Session.getOrderId(Check_Out.this)==null){
                showSingleButtonDialog(Check_Out.this,"Error !","Order id not found",true);
                valid=false;
            }else if(location==null){
                showSingleButtonDialog(Check_Out.this,"Error !","Location not found",true);
                valid=false;
            }else if(deliverytimearry.length()==0){
                deliverytitle.setTextColor(getResources().getColor(R.color.redColor));
                valid=false;
            }
            if(!valid){
                dialog.dismiss();
                return;
            }

            HashMap<String, Object> params = new HashMap<String, Object>();
            ConnectionVO connectionVO =ProductBO.placeOrder();

            params.put("orderId",Session.getOrderId(Check_Out.this));

            JSONObject customerid=new JSONObject();
            customerid.put("customerId",Session.getCustomerId(Check_Out.this));
            params.put("customer",customerid);

            JSONObject mandiidjson=new JSONObject();
            mandiidjson.put("mandiId",Session.getMandiId(Check_Out.this));
            params.put("mandi",mandiidjson);


            params.put("deliveryTime",deliverytimearry.getJSONObject(deliverytimepos).getLong("from"));



            JSONObject addressobj=new JSONObject();
            if(address_type!=null){
                addressobj.put("customerAddressId",address_type);
            }else {
                addressobj.put("customerName",username.getText().toString());
                addressobj.put("address1",useraddress.getText().toString());
                addressobj.put("address2","");
                addressobj.put("landMark",userlandmark.getText().toString());

                addressobj.put("mobileNumber",usermobile.getText().toString());
                addressobj.put("pincode",pincode.getText().toString());

                JSONObject areaObj = new JSONObject();
                areaObj.put("areaId",areaId);
                addressobj.put("area", areaObj );

                JSONObject stateRegionobj= new JSONObject();
                stateRegionobj.put("stateRegionId",state.getTag());
                addressobj.put("stateRegion",stateRegionobj);

                JSONObject cityRegionobj= new JSONObject();
                cityRegionobj.put("cityId",city.getTag());
                addressobj.put("city",cityRegionobj);
            }



            LatLng latLng = new LatLng(location.getDouble("latitude"), location.getDouble("longitude"));
            params.put("latitude",location.getDouble("latitude"));
            params.put("longitude",location.getDouble("longitude"));
            params.put("googleMapAddress",Google_Services.getAddress(latLng));

            params.put("customerAddress",addressobj);
            connectionVO.setParams(params);

            Log.w("placeorderreq",params.toString());

            dialog.dismiss();
            VolleyUtils.makeJsonObjectRequest(this, connectionVO, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                }
                @Override
                public void onResponse(Object resp) throws JSONException {
                    JSONObject response = (JSONObject) resp;
                    Log.w("placeorderresp",response.toString());

                    if(!response.getString("status").equals("success")){
                        JSONArray error =response.getJSONArray("error");
                        StringBuilder sb = new StringBuilder();
                        for(int i=0; i<error.length(); i++){
                            sb.append(error.get(i)).append("\n");
                        }
                        Utility.showSingleButtonDialog(Check_Out.this,null,sb.toString(),false);

                    }else {
                        JSONObject object=response.getJSONObject("dataList");
                        JSONObject orderobj=object.getJSONObject("orderDetail");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(Session.CACHE_ORDERID);
                        editor.commit();

                        Intent intent =new Intent(Check_Out.this,Order_Complete.class);
                        intent.putExtra("orderid",String.valueOf(orderobj.getString("orderNumber")));
                        intent.putExtra("deliveryTime",  deliverytimearry.getJSONObject(deliverytimepos).getString("deliveryTime"));
                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();
                    }
                }
            });






        }catch (Exception e){
            dialog.dismiss();

            Log.e("error",e.getMessage());

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
                Activity activity = (Activity) var1;
                if(activityfinish){

                    startActivity(new Intent(Check_Out.this,Product_List.class));
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();
                }
            }
        });
        var3.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();


        startActivity(new Intent(Check_Out.this,Product_List.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }
}
