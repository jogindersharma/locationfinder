package com.anaadih.locationfinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.anaadih.locationfinder.SelfProfileFragment.Usergetdtailsinterface;
import com.anaadih.locationfinder.dto.HomeDataItem;
import com.anaadih.locationfinder.networking.NetworkStatus;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import android.R.integer;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FriendProfileFragment extends Fragment implements OnClickListener {

	ImageView ivFriendProfilePic;
	TextView tvFriendProfileName, tvFriendProfileDateTime,
			tvFriendProfileAddress;
	Button btnFriendProfileGetLoc, btnFriendProfileSendSMS,
			btnFriendProfileBlock, btnFriendProfileAddGrp;
	Spinner spnrFriendProfileGrp;
	RestAdapter restAdapter;
	String TAG = "FriendProfileFragment";
	String typebtn="";
	String message="";
	private List<HomeDataItem> rowItemsList ;
	private String[] name, group, address, datetime ;
	ArrayAdapter<String> reg_adp;
	List<String> regions;
	int grp_id=0;
	
	FriendProfileInterface firendprofileobj;
	JSONObject jsonResult;
	String responseString;
	int friendId=0;
	private String[] state = { "Select Group", "Donut", "Eclair", "Froyo",
			   "Gingerbread", "HoneyComb", "IceCream Sandwich", "Jellybean",
			   "kitkat" };

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.friend_profile, container,
				false);
		rootView.setClickable(true);
		inilization(rootView);
		
		friendId=getArguments().getInt("friendId");
		Log.e("Frient id", ""+friendId);
		
		 rowItemsList = new ArrayList<HomeDataItem>() ;
			name = new String [] { "Amit Groch", "Joginder Sharma", "Pramod Kumar Varma", "Jitendra Kumar Yadav", "Pradeep Singh Gusian",
					"Anil Kumar Vishwakarma", "Pankaj Kumar Sharma", "Rajesh Kumar", "Ashok Kumar", "Pradeep Pandey" } ;
			group = new String [] {"Faimily", "Friend", "Collegue", "faimily", "Friend", "Office", "Relative", "Nighbour", "Friend", "Faimily" } ;
			address = new String [] { "Indira Puram", "Anaadih Softech", "C-279 New Ashok Nagar", "nKaps Intellect", "Subharti University",
					"Migital Magic", "Clavax India", "Innovative AIIMS", "Fransccicon India", "Tata Consultancy Services (TCS)" } ;
			datetime = new String [] {"11 May 2014 (6:30 PM)", "11 May 2014 (6:30 PM)", "11 May 2014 (6:30 PM)", "11 May 2014 (6:30 PM)", "11 May 2014 (6:30 PM)",
					"11 May 2014 (6:30 PM)", "11 May 2014 (6:30 PM)", "11 May 2014 (6:30 PM)", "11 May 2014 (6:30 PM)", "11 May 2014 (6:30 PM)"} ;
			
	        
	        for (int i = 0 ; i < name .length ; i ++)
			{
	        	HomeDataItem items = new  HomeDataItem(name[i], group[i], address[i], datetime[i], 1) ;
				rowItemsList.add(items) ;
			}	
		
		
		 ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(Home.context,
				    android.R.layout.simple_spinner_item, state);
				  adapter_state
				    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				  spnrFriendProfileGrp.setAdapter(adapter_state);
				  spnrFriendProfileGrp.setOnItemSelectedListener(new MyCustomListener());
				
		return rootView;
	}
	
	private Intent getIntent() {
		// TODO Auto-generated method stub
		return null;
	}

	private class MyCustomListener implements OnItemSelectedListener
	{
	 @Override
	    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3){
	        // Write your logic what you want to do on selecting the item
		 if(arg2!=0){
		 message=rowItemsList.get(arg2-1).getName();
		 Toast.makeText(getActivity(), message, 200).show();
		 }else{
			 message=null;
		 }
	    }
	    @Override
	    public void onNothingSelected(AdapterView<?> arg0) {

	    }
	}
	
	

	private void inilization(View rootView) {
		// TODO Auto-generated method stub
		ivFriendProfilePic = (ImageView) rootView
				.findViewById(R.id.ivFriendProfilePic);
		tvFriendProfileName = (TextView) rootView
				.findViewById(R.id.tvFriendProfileName);
		tvFriendProfileDateTime = (TextView) rootView
				.findViewById(R.id.tvFriendProfileDateTime);
		tvFriendProfileAddress = (TextView) rootView
				.findViewById(R.id.tvFriendProfileAddress);
		btnFriendProfileGetLoc = (Button) rootView
				.findViewById(R.id.btnFriendProfileGetLoc);
		btnFriendProfileSendSMS = (Button) rootView
				.findViewById(R.id.btnFriendProfileSendSMS);
		btnFriendProfileBlock = (Button) rootView
				.findViewById(R.id.btnFriendProfileBlock);
		btnFriendProfileAddGrp = (Button) rootView
				.findViewById(R.id.btnFriendProfileAddGrp);
		spnrFriendProfileGrp = (Spinner) rootView
				.findViewById(R.id.spnrFriendProfileGrp);

		btnFriendProfileGetLoc.setOnClickListener(this);
		btnFriendProfileSendSMS.setOnClickListener(this);
		btnFriendProfileBlock.setOnClickListener(this);
		btnFriendProfileAddGrp.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btnFriendProfileGetLoc:
			 getFriendLocation();			
			break;
		case R.id.btnFriendProfileSendSMS:
			Toast.makeText(Home.context, "Send sms", 200).show();
			break;
		case R.id.btnFriendProfileBlock:
			Toast.makeText(Home.context, "Block", 200).show();
			break;
		case R.id.btnFriendProfileAddGrp:
			if(message!=null){
			Toast.makeText(Home.context, message, 200).show();
			}else{
				Toast.makeText(Home.context, "select group name", 200).show();
			}
			break;

		}
	}
	

	/* ++++++++++++++++ ============  GET  Friend Location  ==========*/
	
	 interface FriendProfileInterface {
			@FormUrlEncoded
			@POST(StaticStrings.GET_LOCATION_REQUEST)
			void updateFriendProfile(
					@Field("userId") int userId,
					@Field("friendId") int grp_id,
					Callback<Response> FriendProfileCallback);
		}
	 
	
		public void getFriendLocation(){
		
		SharedPreferences prefUserId =CustomUtil.getInstance(Home.context).getSharedPrefObj();
		int userId= prefUserId.getInt(StaticStrings.USER_ID, 0);
		restAdapter = new RestAdapter.Builder()
        .setEndpoint(StaticStrings.SITE_URL)
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .build();
		if(userId!=0){
		
		 if (NetworkStatus.getInstance(Home.context).isInternetAvailable(Home.context)) {
			 
			 	Log.e(TAG, "Internet is available");
	    		CustomUtil.getInstance(Home.context).showDialogBox("Find Location", "Sending Request for Location...");
	    		
	    		firendprofileobj = restAdapter.create(FriendProfileInterface.class);
	    		firendprofileobj.updateFriendProfile(userId,friendId,FriendProfileCallback);
			 
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
	Callback<Response> FriendProfileCallback = new Callback<Response>() {
  	  
  	  @Override
  	  public void failure(RetrofitError result) {
  		  Log.e("Retrofit Error ","FriendProfileFragment");
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
						String message= jsonResult.getString("message");
						Log.e("message", message);
						Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
  						
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
	
	
	

}
