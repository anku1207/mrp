package com.uav.mandiratepe.bo;

import com.uav.mandiratepe.vo.ConnectionVO;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

public class PincodeBO implements Serializable {

    public static ConnectionVO isPincodeServiceable(String pincode,int servingGeoLocationId) {
        ConnectionVO connectionVO = new ConnectionVO();

        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            JSONObject servingGeoLocationjson= new JSONObject();
            servingGeoLocationjson.put("servingGeoLocationId",servingGeoLocationId);
            params.put("servingGeoLocation",servingGeoLocationjson);
            params.put("pincode",pincode);

            connectionVO.setMethodName("isPincodeServiceable");
            connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
            connectionVO.setParams(params);


        } catch (Exception e) {
            e.printStackTrace();
        }



        return connectionVO;
    }

}
