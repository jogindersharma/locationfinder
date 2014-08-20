package com.anaadih.locationfinder;

import java.io.IOException;

import com.anaadih.locationfinder.networking.NetworkStatus;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	String SENDER_ID = "645875822828";
	static final String TAG = "Location Finder";
	GoogleCloudMessaging gcm;
    
    SharedPreferences sharedpreferences;
    String regid="";
    Context context;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		
		
		if (checkPlayServices()) {
            Log.i(TAG, "Google Play Services APK found");
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(this);
            
            if (regid.isEmpty()) {
            	Log.e(TAG, "Device is not Registered with GCM now Trying to Register");
            	if (NetworkStatus.getInstance(this).isInternetAvailable(this)) {
            			Log.e(TAG, "Internet is active");
            			registerInBackground();
        		} else {
        			Log.e(TAG, "Internet is not active");
        			NetworkStatus.getInstance(this).showDefaultNoInternetDialog(this);
        			
        		}
            } else {
            	Log.e("GCM is already Registered", "RegId=>"+regid);
            	CustomUtil.getInstance(this).goToUserHome();
            }
        } else {
        	Log.i(TAG, "Google Play Services not Found");
        }
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            Log.i(TAG, "This device is not supported for GooglePlayServicesUtil.");
	            finish();
	        }
	        return false;
	    }
	    return true;
	}
	
	private void registerInBackground() {
	    new AsyncTask<String, String, String>() {

	    	@Override
	        protected String doInBackground(String... params) {
	    		Log.e(TAG, "registerInBackground");
	            try {
	                if (gcm == null) {
	                    gcm = GoogleCloudMessaging.getInstance(context);
	                }
	                Log.e(TAG, "doInBackground");
		            regid = gcm.register(SENDER_ID);
		            Log.e("GCM Registration","Device registered successfully =>" + regid);
		            
		            storeRegistrationId(context, regid);
		            
	            } catch (IOException ex) {
	                Log.e("GCM Registration Failed", ex.getMessage());
	                ex.printStackTrace();
	            }
	            return regid;
	        }

	        @Override
	        protected void onPostExecute(String regid) {
	        	Log.e("onPostExecute", "regid=>"+regid);
	        	if(regid != "" && regid != null) {
	        		CustomUtil.getInstance(context).goToUserHome();
	        	} else {
	        		Log.e(TAG, "regid is blank or null");
	        		//NetworkStatus.getInstance(context).showDefaultAlertDialog(context, "Server Error", "Error: Please Re-Open this Application.");
	        		Toast.makeText(context,"Error: Please Re-Open this Application", Toast.LENGTH_LONG).show();
	        		finish();
	        	}
	        }
	    }.execute(null, null, null);
	}
	
	private String getRegistrationId(Context context) {
		Log.e(TAG, "Try to get device GCM id");
        String registrationId = CustomUtil.getInstance(this).getSharedPrefObj()
        		.getString(StaticStrings.DEVICE_GCM_ID, "");
        if (registrationId.isEmpty()) {
            Log.e(TAG, "Device is not Registered.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = CustomUtil.getInstance(this).getSharedPrefObj().getInt(StaticStrings.LOCATION_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.e(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }
	
	private void storeRegistrationId(Context context, String regId) {
	    int appVersion = getAppVersion(context);
	    Log.e("Saving regId on appVersion " + appVersion,"deviceGCMId=>"+regId);
	    SharedPreferences.Editor editor = CustomUtil.getInstance(this).getSharedPrefObj().edit();
	    editor.putString(StaticStrings.DEVICE_GCM_ID, regId);
	    editor.putInt(StaticStrings.LOCATION_APP_VERSION, appVersion);
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
	
	
	
	
	/*
	protected void showNoInternetDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.on_internet_dialog);
        
        Button button = (Button)dialog.findViewById(R.id.btnNoInternetOk);    
        button.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });      
        dialog.show();
    }
	*/
	
	/*public void showNoInternetDialog2() {
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("Network Status");
		builder.setMessage("Internet is not Working...");
		builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {	 
				
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getApplicationContext(), "Check WiFi Setting.", Toast.LENGTH_LONG).show();
			}
		});
		builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {			 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.setNeutralButton("NO",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		
		AlertDialog alertdialog=builder.create();
		alertdialog.show();
	}*/
	
	
}