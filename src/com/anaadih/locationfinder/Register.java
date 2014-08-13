package com.anaadih.locationfinder;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Register extends Activity {
    
    
    Context context;
    public String regid = "";
    String gcm_id = "";
    String deviceGMailId;
    SharedPreferences sharedPrefObj;
    
    EditText inputFirstName;
    EditText inputLastName;
    EditText inputEmail;
    EditText inputPassword;
    EditText inputRePassword;
    EditText inputMobileNo;
    Button btnRegister;
    
    String fNameVal="";
    String lNameVal="";
    String emailVal="";
    String mobileNoVal="";
    String passwordVal="";
    String rePasswordVal="";
    String TAG = "RegisterJavaFile";
    
    Response responseObj;
	String responseString;
	JSONObject jsonResult;
	RestAdapter restAdapter;
	UserRegisterInterface userRegisterObj;
    
    interface UserRegisterInterface {
		@FormUrlEncoded
		@POST(StaticStrings.USER_REGISTER_URL)
		void userRegister(
				@Field("userFirstName") String userFirstName, 
				@Field("userLastName") String userLastName, 
				@Field("userEmailId") String userEmailId, 
				@Field("deviceEmailId") String deviceEmailId, 
				@Field("userMobileNo") String userMobileNo, 
				@Field("userPassword") String userPassword,
				@Field("deviceGcmId") String deviceGcmId,
				Callback<Response> userRegisterCallback);
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.register);
        init();
        
        restAdapter = new RestAdapter.Builder()
        .setEndpoint(StaticStrings.SITE_URL)
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .build();
    }
    
    
    public void init() {
    	
    	inputFirstName = (EditText) findViewById(R.id.edt_RegFirstName);
        inputLastName = (EditText) findViewById(R.id.edt_RegLastName);
        inputEmail = (EditText) findViewById(R.id.edt_RegEmail);
        inputMobileNo = (EditText) findViewById(R.id.edt_RegMobNumber);
        inputPassword = (EditText) findViewById(R.id.edt_RegPassword);
        inputRePassword = (EditText) findViewById(R.id.edt_RegConfPassword);
        btnRegister = (Button) findViewById(R.id.btn_RegSubmit);
        
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	getRegFormValues();
                if( !fNameVal.equals("") && !lNameVal.equals("") && 
                		!emailVal.equals("") && !mobileNoVal.equals("") 
                		&& !passwordVal.equals("") && !rePasswordVal.equals("") ) {
                	if(passwordVal.equals(rePasswordVal)) {
                		userRegister();
                		} else {	
                			Log.e(TAG,"Password and Re-Password are not same");
                			Toast.makeText(context, "Password and Re-Password are not same", Toast.LENGTH_LONG).show();
                		}		
                } else {
                	Log.e(TAG,"Please Fill all the Fields");
                	Toast.makeText(context, "Please Fill all the Fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    
    public void userRegister() {
    	if (NetworkStatus.getInstance(this).isInternetAvailable(this)) {
    		Log.e(TAG, "Internet is available");
    		
    		String deviceGcmId = CustomUtil.getInstance(this).getSharedPrefObj()
    				.getString(StaticStrings.DEVICE_GCM_ID, "");
    		String deviceEmailId = UserInfo.getEmail(context);
	    	
	    	CustomUtil.getInstance(this).showDialogBox("User Register", "Registring the User...");
    		
    		userRegisterObj = restAdapter.create(UserRegisterInterface.class);
    		userRegisterObj.userRegister(fNameVal, lNameVal, emailVal,deviceEmailId,
    			    mobileNoVal, passwordVal, deviceGcmId, userRegisterCallback);
    	} else {
    		Log.e(TAG, "##########You are not online!!!!");
    		NetworkStatus.getInstance(this).showDefaultNoInternetDialog(this);
    	}
    }
    
    Callback<Response> userRegisterCallback = new Callback<Response>() {
  	  
  	  @Override
  	  public void failure(RetrofitError result) {
  		  Log.e("Retrofit Error ",result.getMessage());
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
						//Toast.makeText(context, message, Toast.LENGTH_LONG).show();
						CustomUtil.getInstance(context).goToLoginPage();
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
    	fNameVal =inputFirstName.getText().toString();
        lNameVal = inputLastName.getText().toString();
        emailVal = inputEmail.getText().toString();
        mobileNoVal = inputMobileNo.getText().toString();
        passwordVal = inputPassword.getText().toString();
        rePasswordVal = inputRePassword.getText().toString();
    }
    
    private void storeUserId(String userId) {
		Log.e(TAG, "Try to Store User Id to Shared Preference =>"+userId);
        
		SharedPreferences.Editor editor = CustomUtil.getInstance(this).getSharedPrefObj().edit();
	    editor.putString(StaticStrings.USER_ID, userId);
	    editor.commit();
    }
    
    private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
    
    
    
}