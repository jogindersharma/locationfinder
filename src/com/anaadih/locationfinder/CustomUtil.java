package com.anaadih.locationfinder;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.anaadih.locationfinder.networking.NetworkStatus;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class CustomUtil {
	
	private static CustomUtil instance = new CustomUtil();
	static Context context;
	String TAG = "CustomUtil";
	static SharedPreferences prefs;
	ProgressDialog pDialog;
	
	// Retrofit Variables
	Response responseObj;
	String responseString;
	JSONObject jsonResult;
	RestAdapter restAdapter;
	SendFriendRequestInterface sendRequestObj;
	
	public static CustomUtil getInstance(Context ctx) {
        context = ctx;
        return instance;
    }
	
	public SharedPreferences getSharedPrefObj() {
		prefs = context.getSharedPreferences(StaticStrings.MyPREFERENCES,Context.MODE_PRIVATE);
		return prefs;
	}
	
	public boolean isJSONValid(String test) {
	    try {
	        new JSONObject(test);
	    } 
	    catch(JSONException ex) {
	        try {
	            new JSONArray(test);
	        } catch(JSONException e) {
	            return false;
	        }
	    }
	    return true;
	}
	
	public void goToUserHome() {
		if(isUserLogin()) {
			goToHomePage();
    	} else {
    		goToLoginPage();
    	}
	}
	
	public void goToLoginPage() {
		Log.e(TAG, "goToLoginPage");
		Intent intent = new Intent(context,Login.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		((Activity) context).finish();
	}
	
	public void goToHomePage() {
		Log.e(TAG, "goToHomePage");
		Intent intent = new Intent(context,Home.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		((Activity) context).finish();
	}
	
	public boolean isUserLogin() {
		Log.e(TAG, "Try to check if user is Login.");
        int userId = prefs.getInt(StaticStrings.USER_ID, 0);
        if (userId ==0) {
            Log.e(TAG, "User is not Login.");
            return false;
        } else {
        	return true;
        }
	}
	
	public int getUserId() {
		Log.e(TAG, "Try to get User Id.");
        int userId = prefs.getInt(StaticStrings.USER_ID, 0);
        return userId;
	}
	
	public void showDialogBox(String tital, String message) {
    	if(pDialog != null && pDialog.isShowing()) {
    		changeTitleDialog(tital, message);
    	} else {
	    	pDialog = new ProgressDialog(context);
	        pDialog.setTitle(tital);
	        pDialog.setMessage(message);
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(false);
	        pDialog.show();
    	}
    }
    
    public void changeTitleDialog(String tital, String message) {
    	if(pDialog != null && pDialog.isShowing()) {
    		pDialog.setTitle(tital);
            pDialog.setMessage(message);
    	}
    }

    public void hideDialogBox() {
    	if(pDialog != null && pDialog.isShowing()) {
    		pDialog.dismiss();
    	}
    }
	
    public void sendFriendRequest(int userId, int friendId) {
    	Log.e("inside sendFriendRequest", String.valueOf(userId)+" "+String.valueOf(friendId));
    	
    	restAdapter = new RestAdapter.Builder()
        .setEndpoint(StaticStrings.SITE_URL)
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .build();
        
        if (NetworkStatus.getInstance(context).isInternetAvailable(context)) {
    		Log.e(TAG, "Internet is available");
    		CustomUtil.getInstance(context).showDialogBox("Friend Request", "Sending Friend Request...");
    		
    		sendRequestObj = restAdapter.create(SendFriendRequestInterface.class);
    		sendRequestObj.sendRequest(userId, friendId, sendFriendRequestCallback);
    	} else {
    		Log.e(TAG, "##########You are not online!!!!");
    		NetworkStatus.getInstance(context).showDefaultNoInternetDialog(context);
    	}
    }
    
    interface SendFriendRequestInterface {
		@FormUrlEncoded
		@POST(StaticStrings.SEND_FRIENDS_REQUEST_URL)
		void sendRequest(
				@Field("userId") int userId, 
				@Field("friendId") int friendId,
				Callback<Response> sendFriendRequestCallback);
	}
    
    Callback<Response> sendFriendRequestCallback = new Callback<Response>() {
  	  
  	  @Override
  	  public void failure(RetrofitError result) {
  		  Log.e("Retrofit Error ","Error in sending Friend Request.");
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
						NetworkStatus.getInstance(context).showDefaultAlertDialog(context, "Error", "Please try after Some Time.");
					} else if(success.equalsIgnoreCase("1")){
						//String message = jsonResult.getString("message");
						//int userId = jsonResult.getInt("userId");
						//storeUserId(userId);
						//CustomUtil.getInstance(context).goToUserHome();
						NetworkStatus.getInstance(context).showDefaultAlertDialog(context, "Success", "Friend Request Sent Successfully.");
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
  	
  	public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream ByteStream=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, ByteStream);
        byte [] b=ByteStream.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;      
    }
    
    public Bitmap StringToBitMap(String encodedString) {
        try {
        	byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;           
        } catch(Exception e) {
        	e.getMessage();
            return null;           
        }       
    }
    public void showNetworkErrorAlertBox(RetrofitError result) {
    	if(result.isNetworkError()) {
  			NetworkStatus.getInstance(context).showDefaultAlertDialog(context, "Network Error", 
  					"Network Error: Please check your Internet is working Properly.");
  		}
    }
}
