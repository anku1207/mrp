package com.uav.mandiratepe.constant;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.regex.Pattern;

public class ApplicationConstant{



    public static final String MOBILENO_VALIDATION="{\"pattern\":\"^[6-9][0-9]{9}$\", \"msg\":  \"Mobile No. accepts only  numbers and length should be 10 (first number to start with [6-9])}\"}";
    public static final String SOMETHINGWRONG = "Something went wrong. Please Try Again";
    public static final String EMAIL_VALIDATION="{\"pattern\":\"^[a-zA-Z0-9][a-zA-Z0-9._-]+@[a-zA-Z0-9][a-zA-Z0-9.-]+.[a-zA-Z]{2,6}$\",  \"msg\": \"Enter a valid email address\"}";


    public static final Pattern pancard = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

    public final static String AUTHKEY= "G4s4cCMx2aM7lky1";

    public final static  boolean IS_PRODUCTION_ENVIRONMENT=true;


    public final static String URL_ADDRESS = getServerAddress();
    public final static String HTTPURL = URL_ADDRESS +":8080/mandiratepay/rest/stateless/";




    public  static final int SOCKET_TIMEOUT_MILLISEC = 60000;
    public static final String SHAREDPREFENCE = "mandipayrate";

    public final static String HTTP_APK_DOWNLOAD_URL="https://play.google.com/store/apps/details?id=com.uav.mandiratepay";


    public static final String CACHE_PORT="port";
    public static final String CACHE_IPADDRESS="ipaddress";
    public static final String CACHE_PROTOCOL="protocol";

    public static final String INTENT_EXTRA_CONNECTION = "connection";

    public static final String CACHE_TITLE="title";

    public  static String getHttpURL(Context context){
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(ApplicationConstant.SHAREDPREFENCE,  Context.MODE_PRIVATE);
        String protocol= (String)sharedPreferences.getString( ApplicationConstant.CACHE_PROTOCOL,null);
        String ipAddress= (String)sharedPreferences.getString( ApplicationConstant.CACHE_IPADDRESS,null);
        String port= (String)sharedPreferences.getString( ApplicationConstant.CACHE_PORT,null);

        if(protocol!=null && ipAddress != null && port!=null){
            return protocol+"://"+ipAddress + ":" + port + "/rof/rest/stateless/";
        }else{
            return HTTPURL;
        }

    }


    private static String getServerAddress(){
        if(IS_PRODUCTION_ENVIRONMENT){
            return  "http://app.mandiratepe.com" ;
        }else{
            //return "http://192.168.1.12";
            return "http://192.168.1.111"; //vipin shakay
        }
    }
}
