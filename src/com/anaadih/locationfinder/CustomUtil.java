package com.anaadih.locationfinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class CustomUtil {
	
	private static CustomUtil instance = new CustomUtil();
	static Context context;
	String TAG = "CustomUtil";
	static SharedPreferences prefs;
	ProgressDialog pDialog;
	
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
        String userId = prefs.getString(StaticStrings.USER_ID, "");
        if (userId.isEmpty()) {
            Log.e(TAG, "User is not Login.");
            return false;
        } else {
        	return true;
        }
	}
	
	public String getUserId() {
		Log.e(TAG, "Try to get User Id.");
        String userId = prefs.getString(StaticStrings.USER_ID, "");
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
	
}
