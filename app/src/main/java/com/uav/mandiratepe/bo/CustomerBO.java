package com.uav.mandiratepe.bo;

import com.uav.mandiratepe.vo.ConnectionVO;

import java.io.Serializable;
import java.util.HashMap;

public class CustomerBO  implements Serializable {

    public static ConnectionVO getCustomerByMobileNumber(String mobileno) {

        ConnectionVO connectionVO = new ConnectionVO();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("mobileNumber",mobileno);

        connectionVO.setMethodName("getCustomerByMobileNumber");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);

        return connectionVO;
    }

    public static ConnectionVO verifiyOTP(String mobileno,String otp,String token) {

        ConnectionVO connectionVO = new ConnectionVO();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("mobileNumber",mobileno);
        params.put("gcmTokenId",token);
        params.put("anonymousString",otp);
        connectionVO.setMethodName("verifiyOTP");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);

        return connectionVO;
    }

    public static ConnectionVO resendOTP(String mobileno) {

        ConnectionVO connectionVO = new ConnectionVO();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("mobileNumber",mobileno);
        connectionVO.setMethodName("resendOTP");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);

        return connectionVO;
    }
}
