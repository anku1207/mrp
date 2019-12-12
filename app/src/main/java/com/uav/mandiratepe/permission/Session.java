package com.uav.mandiratepe.permission;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uav.mandiratepe.constant.ApplicationConstant;
import com.uav.mandiratepe.util.Utility;
import com.uav.mandiratepe.vo.ProductVO;

import org.json.JSONObject;

import java.util.ArrayList;


public class Session {
    static SharedPreferences sharedPreferences;
    static Gson gson = new Gson();

    public  static String CACHE_LOCATION="LOCATION";
    public final static String CACHE_ORDER_LIST = "ORDERLIST";

    public final static String CACHE_CUSTOMER_DATA = "CUSTOMERDATA";
    public final static String CACHE_APPSESSION = "APPSESSION";
    public final static String CACHE_ORDERID = "ORDERID";
    public final static String CACHE_TOKENID = "TOKENID";
    public static String CACHE_INFODIALOG="INFODIALOG";



    public static Integer getOrderId(Context context){
        Integer orderid=null;
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);

            orderid=Integer.parseInt(sharedPreferences.getString(Session.CACHE_ORDERID,null));

        }catch (Exception e){

        }
        return  orderid;
    }
    public static Integer getCustomerId(Context context){
        Integer customerid=null;
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
            JSONObject object=new JSONObject(sharedPreferences.getString(Session.CACHE_CUSTOMER_DATA,null));
            customerid=object.getInt("customerId");

        }catch (Exception e){

        }
        return  customerid;
    }

    public static Integer getServingGeoLocationId(Context context){
        Integer servingGeoLocationId=null;
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
            JSONObject object=new JSONObject(sharedPreferences.getString(Session.CACHE_LOCATION,null));
            servingGeoLocationId=object.getInt("servingGeoLocationId");

        }catch (Exception e){

        }
        return  servingGeoLocationId;
    }

    public static ArrayList<ProductVO> getItemList(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
        return (ArrayList<ProductVO>) Utility.fromJson(sharedPreferences.getString(Session.CACHE_ORDER_LIST,null), new TypeToken<ArrayList<ProductVO>>() { }.getType());
    }

    public static Integer getMandiId(Context context){
        JSONObject object=null;
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
            object=new JSONObject(sharedPreferences.getString(Session.CACHE_LOCATION,null));
            return Integer.parseInt(object.getString("mandiId"));
        }catch (Exception e){

        }
       return null;
    }

    public static void set_Data_Sharedprefence(Context context,String CacheName ,String data){
        sharedPreferences = context.getSharedPreferences(ApplicationConstant.SHAREDPREFENCE,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putString( CacheName,data);
        edit.apply();
        edit.commit();
    }



    public static boolean check_Exists_key(Context context,String key ){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
        return sharedPreferences.contains(key);
    }


    public static String getSessionByKey(Context context,String cacheKey){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
        String resp=sharedPreferences.getString(cacheKey,null);
        return resp;

    }



}
