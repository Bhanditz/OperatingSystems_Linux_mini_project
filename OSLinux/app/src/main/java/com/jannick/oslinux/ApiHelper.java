package com.jannick.oslinux;

import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.jannick.oslinux.tokens.KaKuToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Jannick on 5-4-2016.
 */
public class ApiHelper {

    private static ApiHelper apiHelper = null;
    public final static String TAG = "OSLinux";

    private String baseURL = "http://145.48.227.148:8081/";
    private String ipAdress,port;
    public final String get = "",
                   add = "add",
                   getAll = "getKakus";

    private ApiHelper(){

    }

    public static ApiHelper getInstance(){
        if(apiHelper == null)
            apiHelper = new ApiHelper();
        return apiHelper;
    }

    public String getBaseURL(){
        return baseURL;
    }

    public void setBaseURL(String ipAdress, String port){
        this.ipAdress = ipAdress;
        this.port = port;
        baseURL = "http://" + ipAdress + ":" + port + "/";
        Log.d(TAG,baseURL);
    }

    public String getIpAdress() {
        return ipAdress;
    }

    public String getPort() {
        return port;
    }

}
