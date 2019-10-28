package com.example.dingcan.util;

import android.os.Looper;
import okhttp3.*;
import org.json.JSONException;

import java.io.IOException;

public class HttpUtil {

    private static final String baseUrl="http://w27d388452.wicp.vip:26738/android/";

    public static final String loginUrl = baseUrl+"login";
    public static final String registerUrl = baseUrl+"signIn";
    public static final String forgetUrl = baseUrl+"forgetPassword";
    public static String updateUrl = baseUrl+"updatePersonalInfo";
    public static String showAllGoods = baseUrl+"showAllProduct";
    public static String showCustomerAllOrder = baseUrl+"showCustomerAllOrder";
    public static String submitOrderUrl = baseUrl+"submitOrder";

}
