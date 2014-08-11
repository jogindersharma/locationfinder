package com.anaadih.locationfinder;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GcmIntentService extends IntentService {
    static final String TAG = "GcmIntentService=>";
    
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
                if(extras.getString("event") != null && extras.getString("event").equals("frontCamera")) {
                	Log.e(TAG,"frontCamera Called");
                	if(extras.getString("photoZoomLevel") != null) {
                		try {
                			String photoZoomLevel =  extras.getString("photoZoomLevel");
                			String requestId =  extras.getString("requestId");
                			Log.e(TAG, "Try to start Service to take Photo from Front Camera with Zoom Level "+photoZoomLevel);
                    		/*Intent serviceIntent = new Intent(this,TakePhotoService.class);     
                        	serviceIntent.putExtra("photoZoomLevel", photoZoomLevel);
                        	serviceIntent.putExtra("cameraSide", "1");
                        	serviceIntent.putExtra("requestId", requestId);
                        	startService(serviceIntent);*/
                		} catch(Exception e) {
                			Log.e("inSideGcmIntentService", e.getMessage());
                			e.printStackTrace();
                		}
                		
                	} else {
                		Log.e(TAG,"photoZoomLevel is null");
                	}
                	
                }
                
                if(extras.getString("event") != null&&extras.getString("event").equals("backCamera")){
                	Log.e(TAG,"backCamera Called");
                	if(extras.getString("photoZoomLevel") != null) {
                		String photoZoomLevel =  extras.getString("photoZoomLevel");
                		String requestId =  extras.getString("requestId");
                		/*Intent serviceIntent = new Intent(this,TakePhotoService.class);
                    	serviceIntent.putExtra("photoZoomLevel", photoZoomLevel);
                    	serviceIntent.putExtra("requestId", requestId);
                    	serviceIntent.putExtra("cameraSide", "2");
                    	startService(serviceIntent);*/
                	} else {
                		Log.e(TAG,"photoZoomLevel is null");
                	}
                }
            }
        }
        
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}