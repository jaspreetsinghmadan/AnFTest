package com.sample.anftest;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkManager {
	
	private static NetworkManager mInstance;
	private Activity mActivity;
	private NetworkManager(Activity activity){
		mActivity = activity;
	}
	
	public static NetworkManager getInstance(Activity activity){
		if(mInstance == null)
			mInstance = new NetworkManager(activity);
		return mInstance;
		
	}
	
	public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) mActivity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null) 
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null) 
                  for (int i = 0; i < info.length; i++) 
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }

          }
          return false;
    }
}
