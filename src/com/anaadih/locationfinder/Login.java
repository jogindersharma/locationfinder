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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
    
	Context context;
	
	EditText loginEmailId;
    EditText loginPassword;
    Button loginSubmitBtn;
    Button loginRegisterBtn;
    
    String loginEmailVal="";
    String loginPassVal="";
    String TAG = "LoginActivity";
    
    Response responseObj;
	String responseString;
	JSONObject jsonResult;
	RestAdapter restAdapter;
	UserLoginInterface userRegisterObj;
    
    interface UserLoginInterface {
		@FormUrlEncoded
		@POST(StaticStrings.USER_LOGIN_URL)
		void userLogin(
				@Field("userEmailId") String userEmailId, 
				@Field("userPassword") String userPassword,
				@Field("deviceGcmId") String deviceGcmId,
				Callback<Response> userLoginCallback);
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        context = this;
        init();
        
       }
    
    public void init() {
    	
    	loginEmailId = (EditText) findViewById(R.id.edt_LoginEmail);
    	loginPassword = (EditText) findViewById(R.id.edt_LoginPass);
    	loginSubmitBtn = (Button) findViewById(R.id.btn_LoginSubmit);
    	loginRegisterBtn = (Button) findViewById(R.id.btn_LoginRegister);
        
    	loginSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	userLogin();
            }
        });
        
    	loginRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	goToRegisterPage();
            }
        });
    }
    
    public void userLogin() {
    	getRegFormValues();

        restAdapter = new RestAdapter.Builder()
        .setEndpoint(StaticStrings.SITE_URL)
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .build();
        
        if (NetworkStatus.getInstance(this).isInternetAvailable(this)) {
    		Log.e(TAG, "Internet is available");
    		CustomUtil.getInstance(this).showDialogBox("User Login", "Login ...");
    		String deviceGcmId = CustomUtil.getInstance(this).getSharedPrefObj()
    				.getString(StaticStrings.DEVICE_GCM_ID, "");
    		userRegisterObj = restAdapter.create(UserLoginInterface.class);
    		userRegisterObj.userLogin(loginEmailVal, loginPassVal,deviceGcmId, userLoginCallback);
    	} else {
    		Log.e(TAG, "##########You are not online!!!!");
    		NetworkStatus.getInstance(this).showDefaultNoInternetDialog(this);
    	}
    }
    
    Callback<Response> userLoginCallback = new Callback<Response>() {
    	  
    	  @Override
    	  public void failure(RetrofitError result) {
    		  //Log.e("Retrofit Error ",result.getMessage());
    		  Log.e("Retrofit Error ","Error in Server Response");
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
  					} else if(success.equalsIgnoreCase("1")){
  						String message = jsonResult.getString("message");
  						int userId = jsonResult.getInt("userId");
  						storeUserId(userId);
  						CustomUtil.getInstance(context).goToUserHome();
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
    
    public void getRegFormValues() {
    	loginEmailVal =loginEmailId.getText().toString();
    	loginPassVal = loginPassword.getText().toString();
    }
    
    public void goToRegisterPage() {
		Log.e(TAG, "goToLoginPage");
		Intent intent = new Intent(context,Register.class);
		startActivity(intent);
        finish();
	}
    
    private void storeUserId(int userId) {
    	SharedPreferences.Editor editor = CustomUtil.getInstance(this).getSharedPrefObj().edit();
	    editor.putInt(StaticStrings.USER_ID, userId);
	    editor.commit();
    }
}