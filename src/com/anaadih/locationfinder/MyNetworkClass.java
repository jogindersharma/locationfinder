package com.anaadih.locationfinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.anaadih.locationfinder.networking.NetworkStatus;

public class MyNetworkClass {
	
	private static MyNetworkClass instance = new MyNetworkClass();
	static Context context;
	String TAG = "MyNetworkClass";
	static SharedPreferences prefs;
	ProgressDialog pDialog;
	
	// Retrofit Variables
	Response responseObj;
	String responseString;
	JSONObject jsonResult;
	static RestAdapter restAdapter;
	
	// Interface Variablee
	TestInterface testInterfaceObj;
	RequestResponseInterface reqResObj;
	
	
	public static MyNetworkClass getInstance(Context ctx) {
        context = ctx;
        restAdapter = new RestAdapter.Builder()
        .setEndpoint(StaticStrings.SITE_URL)
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .build();
        return instance;
    }
	

	// 
	
	interface TestInterface {
		@FormUrlEncoded
		@POST(StaticStrings.SEND_FRIENDS_REQUEST_URL)
		void sendRequest(
				@Field("userId") int userId, 
				@Field("friendId") int friendId,
				Callback<Response> requestCallback);
	}
	
/* ++++++++++++++++ ============  Received Friend Request Response ==========*/
	
	interface RequestResponseInterface {
		@FormUrlEncoded
		@POST(StaticStrings.REQUEST_RESPONSE_URL)
		void sendReqResponse(
				@Field("userId") int userId,
				@Field("friendId") int friendId,
				@Field("responseCode") int responseCode,
				Callback<Response> requestCallback);
	}
	
	public void requestResponse(int friendId, int responseCode) {
		if (NetworkStatus.getInstance(context).isInternetAvailable(context)) {
    		Log.e(TAG, "Internet is available");
    		CustomUtil.getInstance(context).showDialogBox("Server Connection", "Updating Your Response on Server...");
    		int userId = CustomUtil.getInstance(context).getUserId();
    		
    		reqResObj = restAdapter.create(RequestResponseInterface.class);
    		reqResObj.sendReqResponse(userId, friendId, responseCode, requestCallback);
    	} else {
    		Log.e(TAG, "##########You are not online!!!!");
    		NetworkStatus.getInstance(context).showDefaultNoInternetDialog(context);
    	}
	}
	
	Callback<Response> requestCallback = new Callback<Response>() {
	  	  
	  	  @Override
	  	  public void failure(RetrofitError result) {
	  		  Log.e("Retrofit Error ","Error in server response.");
	  		CustomUtil.getInstance(context).hideDialogBox();
	  	  }
	  	  
	  	  @Override
	  	  public void success(Response result, Response response) {
	  		  BufferedReader reader = null;
	  	      StringBuilder sb = new StringBuilder();
	  	      try {
	  	          reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
	  	          String line;
	  	          try {
	  	              while ((line = reader.readLine()) != null) {
	  	                  sb.append(line);
	  	              }
	  	          } catch (IOException e) {
	  	              e.printStackTrace();
	  	          }
	  	      } catch (IOException e) {
	  	          e.printStackTrace();
	  	      }
	  	      responseString = sb.toString();
	  	      if(CustomUtil.getInstance(context).isJSONValid(responseString)) {
	  	    	Log.e("SeverResponse=>", responseString);
		  	    	try {
						jsonResult = new JSONObject(responseString);
						String success = jsonResult.getString("success");
						if(success == null) {
							Log.e("SeverResponse=>", "success variable is null");
						} else if(success.equalsIgnoreCase("0")) {
							String message = jsonResult.getString("message");
							Toast.makeText(context, message, Toast.LENGTH_LONG).show();
							NetworkStatus.getInstance(context).showDefaultAlertDialog(context, "Error", message);
						} else if(success.equalsIgnoreCase("1")){
							String message = jsonResult.getString("message");
							//int userId = jsonResult.getInt("userId");
							//storeUserId(userId);
							//CustomUtil.getInstance(context).goToUserHome();
							NetworkStatus.getInstance(context).showDefaultAlertDialog(context, "Success", message);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	  	      } else {
	  	    	Log.e("SeverResponse=>", "is not a JSON =>"+responseString);
	  	      }
	  	    CustomUtil.getInstance(context).hideDialogBox();
	  	  }
	  	};
	  	

}
