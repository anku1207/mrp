package com.uav.mandiratepe.bo;

import com.uav.mandiratepe.vo.ConnectionVO;

import java.io.Serializable;

public class ContactUsBO implements Serializable {

    public static ConnectionVO getContactus() {
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getContactus");
        connectionVO.setRequestType(ConnectionVO.REQUEST_GET);
        return connectionVO;
    }

    public static ConnectionVO updateVersion() {
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("updateVersion");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        return connectionVO;
    }
}