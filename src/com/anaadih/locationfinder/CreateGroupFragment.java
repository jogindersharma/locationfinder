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

import com.anaadih.locationfinder.HomeFragment.GetFriendLatestLocationInterface;
import com.anaadih.locationfinder.networking.NetworkStatus;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateGroupFragment extends Fragment { 
	
	Button btnCreateGroupCancel, btnCreateGroupSubmit ;
	EditText etCreateGroupName ;
	String groupName ;
	Context context;
	String TAG ="GroupFragment";
	
	// Retrofit Variables
	Response responseObj;
	String responseString;
	JSONObject jsonResult;
	GroupCreationInterface createGroupObj;
	RestAdapter restAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.create_group, container, false);
        rootView.setClickable(true);
        context = container.getContext();
        
        btnCreateGroupCancel = (Button) rootView.findViewById(R.id.btnCreateGroupCancel);
        btnCreateGroupSubmit = (Button) rootView.findViewById(R.id.btnCreateGroupSubmit);
        etCreateGroupName = (EditText) rootView.findViewById(R.id.etCreateGroupName);
        
        btnCreateGroupSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				groupName = etCreateGroupName.getText().toString();
				if (groupName.length() > 0) {
					createGroup();
				}
				else {
					Toast.makeText(getActivity(), "Group Name Should Not Null...", Toast.LENGTH_SHORT).show();
				}
			}
		});
        
        btnCreateGroupCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});       
		return rootView;
	}
	
	// <<<<<<<<<<=========== Create group using retrofit ==========>>>>>>>>>
	
	interface GroupCreationInterface {
		@FormUrlEncoded
		@POST(StaticStrings.CREATE_GROUP_URL)
		void getFriends(
				@Field("userId") int userId,
				@Field("groupName") String groupName,
				Callback<Response> createGroupCallback);
	}
	
	public void createGroup() {
		if (NetworkStatus.getInstance(context).isInternetAvailable(context)) {
    		Log.e(TAG, "Internet is available");
    		
    		restAdapter = new RestAdapter.Builder()
            .setEndpoint(StaticStrings.SITE_URL)
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build();
    		
    		CustomUtil.getInstance(context).showDialogBox("Server Connection", "Creating Group on Server...");
    		int userId = CustomUtil.getInstance(context).getUserId();
    		
    		createGroupObj = restAdapter.create(GroupCreationInterface.class);
    		createGroupObj.getFriends(userId, groupName, groupCreationCallback);
    	} else {
    		Log.e(TAG, "##########You are not online!!!!");
    		NetworkStatus.getInstance(context).showDefaultNoInternetDialog(context);
    	}
	}
	
	Callback<Response> groupCreationCallback = new Callback<Response>() {
	  	  
	  	  @Override
	  	  public void failure(RetrofitError result) {
	  		  Log.e("Retrofit Error ","Error in Creating Group...");
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
							
							NetworkStatus.getInstance(context).showDefaultAlertDialog(context, "Error", message);
						} else if(success.equalsIgnoreCase("1")){
							String message = jsonResult.getString("message");
							NetworkStatus.getInstance(context).showDefaultAlertDialog(context, "Server Response", message);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
	  	      } else {
	  	    	Log.e("SeverResponse=>", "is not a JSON =>"+responseString);
	  	      }
	  	    CustomUtil.getInstance(context).hideDialogBox();
	  	  }
	};	
}