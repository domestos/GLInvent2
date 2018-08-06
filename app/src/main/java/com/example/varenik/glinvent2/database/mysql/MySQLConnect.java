package com.example.varenik.glinvent2.database.mysql;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by valera.pelenskyi on 25.10.17.
 * Singelton class
 */

public class MySQLConnect {

    private static MySQLConnect mySQLConnect;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private MySQLConnect(Context context){
        mCtx = context;
        requestQueue = getRequestQue();
    }


    public RequestQueue getRequestQue(){

        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return  requestQueue;
    }

    public static  synchronized MySQLConnect getInstance(Context context){
        if(mySQLConnect == null){
            mySQLConnect = new MySQLConnect(context);
        }
            return mySQLConnect;
    }


    public<T> void  addToRequestque(Request<T> request) {
        requestQueue.add(request);

    }

}
