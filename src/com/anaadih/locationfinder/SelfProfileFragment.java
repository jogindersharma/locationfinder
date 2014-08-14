package com.anaadih.locationfinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SelfProfileFragment extends Fragment implements OnClickListener {

	ImageView imUserProfile,ivUserProfileFNameEdit,ivUserProfileLNameEdit,ivUserProfileEmailEdit,ivUserProfileMobEdit;
	TextView tvFnameUser,tvLnameUser,tvEmailUser,tvPhoneUser,tvCodeUser;
	Button btnNewCode;
	String responseString;
	JSONObject jsonResult;
	Usergetdtailsinterface usergetDetailsObj;
	UserCodeGenerateinterface userCodeGenerateObj;
	RestAdapter restAdapter;
	String TAG = "UserProfileFragment";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 View rootView = inflater.inflate(R.layout.activity_user_profile, container, false);
		 initization(rootView);		 
		 rootView.setClickable(true);
		 getProfile();
		return rootView;
	}
	
	public void initization(View rootview) {
		imUserProfile = (ImageView) rootview.findViewById(R.id.ivUserProfilePic);
		ivUserProfileFNameEdit= (ImageView) rootview.findViewById(R.id.ivUserProfileFNameEdit);
		ivUserProfileLNameEdit= (ImageView) rootview.findViewById(R.id.ivUserProfileLNameEdit);
		ivUserProfileEmailEdit= (ImageView) rootview.findViewById(R.id.ivUserProfileEmailEdit);
		ivUserProfileMobEdit= (ImageView) rootview.findViewById(R.id.ivUserProfileMobEdit);
		tvFnameUser = (TextView) rootview.findViewById(R.id.tvUserProfileFirstName);
		tvLnameUser = (TextView) rootview.findViewById(R.id.tvUserProfileLastName);
		tvEmailUser = (TextView) rootview.findViewById(R.id.tvUserProfileEmail);
		tvPhoneUser = (TextView) rootview.findViewById(R.id.tvUserProfileMob);
		tvCodeUser = (TextView) rootview.findViewById(R.id.tvUserProfileCode);
		btnNewCode = (Button) rootview.findViewById(R.id.btnUserProfileNewCode);
		
		
		/*btnNewCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	newCodeGen();
            }
        });*/
		btnNewCode.setOnClickListener(this);
		ivUserProfileEmailEdit.setOnClickListener(this);
		ivUserProfileFNameEdit.setOnClickListener(this);
		ivUserProfileLNameEdit.setOnClickListener(this);
		ivUserProfileMobEdit.setOnClickListener(this);
		imUserProfile.setOnClickListener(this);
		
	}
	
	interface Usergetdtailsinterface{
		@FormUrlEncoded
		@POST(StaticStrings.GET_USER_DETAILS)
		void userSelfProfile(
				@Field("userId") Integer userEmailId, 
				Callback<Response> userProfileCallback);
	}
	
	
	
	public void getProfile(){
		
		SharedPreferences prefUserId =CustomUtil.getInstance(Home.context).getSharedPrefObj();
		int userId= prefUserId.getInt(StaticStrings.USER_ID, 0);
		restAdapter = new RestAdapter.Builder()
        .setEndpoint(StaticStrings.SITE_URL)
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .build();
		if(userId!=0){
		
		 if (NetworkStatus.getInstance(Home.context).isInternetAvailable(Home.context)) {
			 
			 	Log.e(TAG, "Internet is available");
	    		CustomUtil.getInstance(Home.context).showDialogBox("User Profile", "Loading your profile...");
	    		
	    		usergetDetailsObj = restAdapter.create(Usergetdtailsinterface.class);
	    		usergetDetailsObj.userSelfProfile(userId, userProfileCallback);
			 
		 }else{
				Log.e(TAG, "##########You are not online!!!!");
	    		NetworkStatus.getInstance(Home.context).showDefaultNoInternetDialog(Home.context);
		 }
		}else{
			// move to login screen
			Log.e(TAG, "goToLoginPage");
			Intent intent = new Intent(Home.context,Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Home.context.startActivity(intent);
			((Activity) Home.context).finish();
		}
		
	}
	Callback<Response> userProfileCallback = new Callback<Response>() {
  	  
  	  @Override
  	  public void failure(RetrofitError result) {
  		  Log.e("Retrofit Error ",result.getMessage());
  		CustomUtil.getInstance(Home.context).hideDialogBox();
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
  	      if(CustomUtil.getInstance(Home.context).isJSONValid(responseString)) {
  	    	Log.e("SeverResponse=>", responseString);
	  	    	try {
					jsonResult = new JSONObject(responseString);
					String success = jsonResult.getString("success");
					if(success == null) {
						Log.e("SeverResponse=>", "success variable is null");
					} else if(success.equalsIgnoreCase("0")) {
						String message = jsonResult.getString("message");
						Toast.makeText(Home.context, message, Toast.LENGTH_LONG).show();
					} else if(success.equalsIgnoreCase("1")){
						JSONObject userInfoJsonObj= jsonResult.getJSONObject("userInfo");
						
						tvFnameUser.setText(userInfoJsonObj.getString("user_firstname").toString());
						tvLnameUser.setText(userInfoJsonObj.getString("user_lastname").toString());
						tvEmailUser.setText(userInfoJsonObj.getString("user_email_id").toString());
						tvPhoneUser.setText(userInfoJsonObj.getString("mobile_no").toString());
						tvCodeUser.setText(userInfoJsonObj.getString("user_code").toString());
  						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
  	      } else {
  	    	Log.e("SeverResponse=>", "is not a JSON =>"+responseString);
  	      }
  	    CustomUtil.getInstance(Home.context).hideDialogBox();
  	  }
  	};
  	
  	// Generate code function
  	
  	interface UserCodeGenerateinterface{
		@FormUrlEncoded
		@POST(StaticStrings.UPDATE_USER_DETAILS)
		void userCodeGenerate(
				@Field("userId") Integer userEmailId, 
				@Field("codeType") String usercode,
				Callback<Response> userCodeGenerateCallback);
	}
  	
  	private void newCodeGen() {
		// TODO Auto-generated method stub
  		SharedPreferences prefUserId =CustomUtil.getInstance(Home.context).getSharedPrefObj();
		int userId= prefUserId.getInt(StaticStrings.USER_ID, 0);
		restAdapter = new RestAdapter.Builder()
        .setEndpoint(StaticStrings.SITE_URL)
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .build();
		if(userId!=0){
		
		 if (NetworkStatus.getInstance(Home.context).isInternetAvailable(Home.context)) {
			 
			 	Log.e(TAG, "Internet is available");
	    		CustomUtil.getInstance(Home.context).showDialogBox("New Code", "Wait for new code...");
	    		
	    		userCodeGenerateObj = restAdapter.create(UserCodeGenerateinterface.class);
	    		userCodeGenerateObj.userCodeGenerate(userId,"code", userCodeGenerateCallback);
			 
		 }else{
				Log.e(TAG, "##########You are not online!!!!");
	    		NetworkStatus.getInstance(Home.context).showDefaultNoInternetDialog(Home.context);
		 }
		}else{
			// move to login screen
			Log.e(TAG, "goToLoginPage");
			Intent intent = new Intent(Home.context,Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Home.context.startActivity(intent);
			((Activity) Home.context).finish();
		}
	}
  	Callback<Response> userCodeGenerateCallback = new Callback<Response>() {
    	  
    	  @Override
    	  public void failure(RetrofitError result) {
    		  Log.e("Retrofit Error ",result.getMessage());
    		CustomUtil.getInstance(Home.context).hideDialogBox();
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
    	      if(CustomUtil.getInstance(Home.context).isJSONValid(responseString)) {
    	    	Log.e("SeverResponse=>", responseString);
  	  	    	try {
  					jsonResult = new JSONObject(responseString);
  					String success = jsonResult.getString("success");
  					if(success == null) {
  						Log.e("SeverResponse=>", "success variable is null");
  					} else if(success.equalsIgnoreCase("0")) {
  						String message = jsonResult.getString("message");
  						Toast.makeText(Home.context, message, Toast.LENGTH_LONG).show();
  					} else if(success.equalsIgnoreCase("1")){
  						Toast.makeText(Home.context, jsonResult.getString("message").toString(), Toast.LENGTH_LONG).show();
  						JSONObject userInfoJsonObj= jsonResult.getJSONObject("userInfo");
						
						tvCodeUser.setText(userInfoJsonObj.getString("user_code").toString());
  					}
  				} catch (JSONException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
    	      } else {
    	    	Log.e("SeverResponse=>", "is not a JSON =>"+responseString);
    	      }
    	    CustomUtil.getInstance(Home.context).hideDialogBox();
    	  }
    	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	switch (v.getId()) {
	case R.id.btnUserProfileNewCode:
		newCodeGen();	
		break;

	case R.id.ivUserProfilePic:
		Toast.makeText(Home.context, "pic", Toast.LENGTH_SHORT).show();
		Intent changePic = new Intent(Home.context,ProfileImageUpdater.class);
		startActivity(changePic);
		break;
	case R.id.ivUserProfileEmailEdit:
		Toast.makeText(Home.context, "email", Toast.LENGTH_SHORT).show();
		break;
	case R.id.ivUserProfileFNameEdit:
		Toast.makeText(Home.context, "First name", Toast.LENGTH_SHORT).show();
		break;
	case R.id.ivUserProfileLNameEdit:
		Toast.makeText(Home.context, "Last name", Toast.LENGTH_SHORT).show();
		break;
	case R.id.ivUserProfileMobEdit:
		Toast.makeText(Home.context, "clcik", Toast.LENGTH_SHORT).show();
		break;
	}
	
	}
}
