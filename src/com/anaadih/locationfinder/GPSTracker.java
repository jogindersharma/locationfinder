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
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener {

	// flag for GPS status
	boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;

	// flag for GPS status
	boolean canGetLocation = false;

	Location location,locationGps,locationNetwork; // location
	double latitude; // latitude
	double longitude; // longitude
	String getLocRequestId;
	int userId;
	Context context;
	String address;
	int getLocStatus;
	String message;
	LocationInterface locationobj;
	RestAdapter restAdapter;
	String TAG = "GPSTracker";
	JSONObject jsonResult;
	String responseString;
	
	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

	// Declaring a Location Manager
	protected LocationManager locationManager;

	public GPSTracker() {
		context = this;
	}
	
	@Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("onCreate","Called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy","Called");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e("onLowMemory","Called");
    }

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.e("inGPSTACKERService","onstartcommand");
		if(intent.getExtras().getString("getLocRequestId") !="") {
			getLocRequestId = intent.getExtras().getString("getLocRequestId");
			Log.e("inGPSTACKERService","onstatcommand getLocRequestId"+getLocRequestId);
			getLocation();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	public void getLocation() {
		try {
			locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
				 getLocStatus=0;
				 message="GPS and NETWORK_PROVIDER is not enabled.";
			} else {
				getLocStatus=1;
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network"+locationManager);
					if (locationManager != null) {
						locationNetwork = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						message="NETWORK_PROVIDER is enabled";
						if (locationNetwork != null) {
							latitude = locationNetwork.getLatitude();
							longitude = locationNetwork.getLongitude();
						}
						
						Log.e(TAG, " network latitude and longitude"+latitude+"----"+longitude);
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				System.out.println("isGPSEnabled"+isGPSEnabled);
				if (isGPSEnabled) {
					Log.d("in GPS", "GPS IS ENABLE");
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled"+locationManager);
						if (locationManager != null) {
							locationGps = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							message=message+"  GPS  is  enabled.";
							if (locationGps != null) {
								latitude = locationGps.getLatitude();
								longitude = locationGps.getLongitude();
							}
							Log.e(TAG, " GPS latitude and longitude"+latitude+"----"+longitude);
						}
					}
				}
				// Latest code by amit start
				if(locationNetwork!=null && locationGps!=null){
					if(locationNetwork.getTime()>locationGps.getTime())
	                {latitude = locationNetwork.getLatitude();
	                longitude = locationNetwork.getLongitude();
	                 
	                }
	                 else
	                {latitude = locationGps.getLatitude();
	                longitude = locationGps.getLongitude();
	                
	                }
					
					
				}
				
				// Latest code by amit end
			}
			if(latitude!=0.0){
			sendLocation();
			}else{
				Log.e(TAG, "lat long is empty and we are trying to get");
				Thread.sleep(9000);
				getLocation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
		//return location;
		
		
		
	}
	
	/**
	 * Stop using GPS listener
	 * Calling this function will stop using GPS in your app
	 * */
	public void stopUsingGPS(){
		if(locationManager != null){
			locationManager.removeUpdates(GPSTracker.this);
		}		
	}
	
	/**
	 * Function to get latitude
	 * */
	public double getLatitude(){
		if(location != null){
			latitude = location.getLatitude();
		}
		
		// return latitude
		return latitude;
	}
	
	/**
	 * Function to get longitude
	 * */
	public double getLongitude(){
		if(location != null){
			longitude = location.getLongitude();
		}
		
		// return longitude
		return longitude;
	}
	
	/**
	 * Function to check GPS/wifi enabled
	 * @return boolean
	 * */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}
	
	/**
	 * Function to show settings alert dialog
	 * On pressing Settings button will lauch Settings Options
	 * */
	/*public void showSettingsAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
   	 
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
 
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
 
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            	mContext.startActivity(intent);
            }
        });
 
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
	}*/

	@Override
	public void onLocationChanged(Location location) {
		if (location != null)
	    {
	        Log.i("SuperMap", "Location changed : Lat: " + location.getLatitude() + " Lng: " + location.getLongitude());
	        latitude = location.getLatitude();
	        longitude = location.getLongitude();
	        Log.i("latitude,longitude", ""+latitude+","+longitude);


	    } 
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	
	
	// send the details on server 
	
	interface LocationInterface {
		@FormUrlEncoded
		@POST(StaticStrings.GET_LOCATION_RESPONSE)
		void getLocation(
				@Field("latitude") Double latitude,
				@Field("longitude") Double longitude,
				@Field("getLocRequestId") int getLocRequestId,
				@Field("getLocResponseStatus") int getLocStatus,
				@Field("getLocResponseMessage") String message,
				Callback<Response> LocationCallback);
	}
	
		public void sendLocation(){
		
		restAdapter = new RestAdapter.Builder()
        .setEndpoint(StaticStrings.SITE_URL)
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .build();
		 if (NetworkStatus.getInstance(context).isInternetAvailable(context)) {
			 
			 	Log.e(TAG, "Internet is available");
	    		//CustomUtil.getInstance(Home.context).hideDialogBox();
			 	Log.e(TAG, "send back latitude and longitude"+latitude+"----"+longitude);
	    		locationobj = restAdapter.create(LocationInterface.class);
	    		locationobj.getLocation(latitude,longitude,Integer.parseInt(getLocRequestId),getLocStatus,message,LocationCallback);
			 
		 }else{
				Log.e(TAG, "##########You are not online!!!!");
	    		NetworkStatus.getInstance(context).showDefaultNoInternetDialog(context);
		 }
		
	}
	Callback<Response> LocationCallback = new Callback<Response>() {
  	  
  	  @Override
  	  public void failure(RetrofitError result) {
  		  Log.e("Retrofit Error ","Error in sending the LatLong to Server. We are trying again ...");
  		  sendLocation();
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
						//sendLocation();
					} else if(success.equalsIgnoreCase("0")) {
						String message = jsonResult.getString("message");
						//sendLocation();
					} else if(success.equalsIgnoreCase("1")){
						String message = jsonResult.getString("message");
						Log.e(TAG,"Success ==> "+message);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
	      } else {
	    	Log.e(TAG, "SeverResponse is not a JSON =>"+responseString+" We are trying again ...");
	    	//sendLocation();
	      }
	    CustomUtil.getInstance(context).hideDialogBox();
	  }
  	};
	
	
}