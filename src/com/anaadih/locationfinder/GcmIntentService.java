package com.anaadih.locationfinder;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GcmIntentService extends IntentService {
    static final String TAG = "GcmIntentService =>";
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    
    public GcmIntentService() {
    
    	super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    	Log.e(TAG,"onHandleIntent Called"); 
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.e("Error=>", extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.e("Error=>","Deleted messages on server=>"+extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            	Log.e("Received the GCM=>",extras.toString());
               
            	if(extras.getString("event") != null && extras.getString("event").equals("locRequest")) {
                	Log.e(TAG, "We get a request for LatLong.");
                	try {
                			String requestId =  extras.getString("requestId");
                			Log.e("inSideGcmIntentService","requestId is => "+requestId);
                			Intent serviceIntent = new Intent(this, GPSTracker.class);     
                        	serviceIntent.putExtra("getLocRequestId", requestId);
                        	startService(serviceIntent);
                		} catch(Exception e) {
                			Log.e("inSideGcmIntentService", e.getMessage());
                			e.printStackTrace();
                		}
                }
                
                if(extras.getString("event") != null && extras.getString("event").equals("locResponse")){
                	Log.e(TAG,"We get the response for your Get Location Request.");
                	if(extras.getString("latitude") != null) {
                		
                		String latitude =  extras.getString("latitude");
                		String longitude =  extras.getString("longitude");
                		int friendId =  Integer.parseInt(extras.getString("user_id"));
                		String friendFname =  extras.getString("user_firstname");
                		String friendLname =  extras.getString("user_lastname");
                		String friendImageUrl =  extras.getString("image_name");
                		String friendfullAddress =extras.getString("fullAddress");
                		String responseTime =extras.getString("updated_at");
                		
                		/*Intent trakIntent = new Intent(this,GPSTracker.class);     
                		
                		trakIntent.putExtra("latitude", latitude);
                		trakIntent.putExtra("longitude", longitude);
                		trakIntent.putExtra("user_id", friendId);
                		trakIntent.putExtra("user_firstname", friendFname);
                		trakIntent.putExtra("user_lastname", friendLname);
                		trakIntent.putExtra("image_name", friendImageUrl);
                		trakIntent.putExtra("friendfullAddress", friendfullAddress);
                		trakIntent.putExtra("responseTime", responseTime);
                    	
                		startService(trakIntent);
                		*/
                		sendNotification(latitude, longitude, friendId, friendFname, friendLname, friendImageUrl, friendfullAddress, responseTime);
                    	Log.e("inSideGcmIntentService Response","finally send"+friendId);
                	} else {
                		Log.e(TAG,"Known Event");
                	}
                }
            }
        }
        
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
    
    private void sendNotification(String latitude,String longitude, int user_id,String user_firstname,String user_lastname,String image_name,String friendfullAddress,String responseTime) {
        mNotificationManager = (NotificationManager)
        this.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.e("ReceivedData=>",longitude);
        
        Intent myintent = new Intent(this, Home.class);
        
        myintent.putExtra("latitude", Double.parseDouble(latitude));
        myintent.putExtra("longitude", Double.parseDouble(longitude));
        myintent.putExtra("user_id", user_id);
        myintent.putExtra("user_firstname", user_firstname);
        myintent.putExtra("user_lastname", user_lastname);
        myintent.putExtra("image_name", image_name);
        myintent.putExtra("friendfullAddress", friendfullAddress);
        myintent.putExtra("responseTime", responseTime);
    	
        
/*      PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, myintent), 0);*/
        
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
        		myintent, PendingIntent.FLAG_UPDATE_CURRENT |  PendingIntent.FLAG_ONE_SHOT);
        
        

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("Location Finder Notification")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText("Response for your Get Location Request"))
        .setContentText(friendfullAddress);
         mBuilder.setContentIntent(contentIntent);
         mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
         mBuilder.setAutoCancel(true);
         mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}