package com.example.vitikcorp.miniproject.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.vitikcorp.miniproject.Model.User;

import java.security.cert.TrustAnchor;

public class Common {
    public static User currentUser;
    public static String convertCodeToStatus(String status){
        if(status.equals("0"))
            return "Placed";
        else if(status.equals("1"))
            return "Set for Delivery";

        else
            return "Delivered";
    }

    public static final String DELETE="Delete";
    public static boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager!=null)
        {
            NetworkInfo[] info=connectivityManager.getAllNetworkInfo();
            if(info!= null)
            {
                for(int i=0;i<info.length;i++){
                    if(info[i].getState()==NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

}
