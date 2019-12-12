package com.uav.mandiratepe.bo;

import com.uav.mandiratepe.vo.ConnectionVO;

import java.io.Serializable;
import java.util.HashMap;

public class LocationBO implements Serializable{

    public static ConnectionVO isGeoLocationServiceable(Double latitude,Double longitude) {

        ConnectionVO connectionVO = new ConnectionVO();
            HashMap<String, Object> params = new HashMap<String, Object>();


            params.put("latitude",latitude);
            params.put("longitude",longitude);
            connectionVO.setMethodName("isGeoLocationServiceable");
            connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
            connectionVO.setLoaderAvoided(true);
            connectionVO.setParams(params);




        return connectionVO;
    }
}
