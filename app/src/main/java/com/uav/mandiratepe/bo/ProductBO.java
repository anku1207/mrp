package com.uav.mandiratepe.bo;

import android.util.Log;

import com.uav.mandiratepe.vo.ConnectionVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

public class ProductBO implements Serializable {


    public static ConnectionVO getCategoryList() {

        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getCategoryList");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        return connectionVO;
    }


    public static ConnectionVO getTodayProductList(Integer categoryId,Integer mandiid ,String orderid) {
        ConnectionVO connectionVO = new ConnectionVO();

        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            JSONObject object= new JSONObject();
            JSONObject object1=new JSONObject();
            object.put("categoryId",categoryId);
            object1.put("category",object);
            JSONObject mondiobj=new JSONObject();
            mondiobj.put("mandiId",mandiid);
            params.put("product",object1);
            params.put("mandi",mondiobj);
            params.put("orderId",orderid!=null?Integer.parseInt(orderid):null);
            Log.w("jsonobj",params.toString());
            connectionVO.setMethodName("getTodayProductList");
            connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
            connectionVO.setParams(params);


        } catch (JSONException e) {
            e.printStackTrace();
        }



        return connectionVO;
    }


    public static ConnectionVO checkout() {
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("checkout");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        return connectionVO;
    }

    public static ConnectionVO placeOrder(){

        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("placeOrder");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        return connectionVO;
    }

    public static ConnectionVO orderHistory(int customerid){
        HashMap<String, Object> params = new HashMap<String, Object>();
            ConnectionVO connectionVO = new ConnectionVO();
            try {
                JSONObject objectjson =new JSONObject();
                objectjson.put("customerId",customerid);
                params.put("customer",objectjson);
                connectionVO.setMethodName("orderHistory");
                connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
                connectionVO.setParams(params);

            }catch (Exception e){
        }
        return connectionVO;
    }

    public static ConnectionVO getOrderByIdProductList(int orderid){
        HashMap<String, Object> params = new HashMap<String, Object>();
        ConnectionVO connectionVO = new ConnectionVO();
        params.put("orderId",orderid);
        connectionVO.setMethodName("getOrderByIdProductList");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        return connectionVO;
    }

    public static ConnectionVO setOrderCancel(int orderid){
        HashMap<String, Object> params = new HashMap<String, Object>();
        ConnectionVO connectionVO = new ConnectionVO();
        params.put("orderId",orderid);
        connectionVO.setMethodName("setOrderCancel");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        return connectionVO;
    }

}
