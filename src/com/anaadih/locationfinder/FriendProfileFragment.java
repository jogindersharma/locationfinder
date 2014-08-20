package com.anaadih.locationfinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.anaadih.locationfinder.dto.HomeDataItem;
import com.anaadih.locationfinder.networking.NetworkStatus;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	String TAG = "FriendProfileFragment";
	String typebtn="";
	String message="";
	private List<HomeDataItem> rowItemsList ;
	private String[] name, group, address, datetime ;
	ArrayAdapter<String> reg_adp;
	List<String> regions;
	int grp_id=0;

	int friendId=0;
	
	private String[] userGroups ;
	
	Context context ;
	GroupListInterface getGroupsObj ;
	FriendProfileInterface firendprofileobj;
	SendSMSInterface sendSMSObj ;
	BlockFriendInterface blockFriendObj ;
	AddFriendToGroupInterface addFriendToGroupObj ;
	JSONObject jsonResult;
	RestAdapter restAdapter;
	String responseString;
	
	Dialog smsDialog ;
	LinearLayout llSendSmsCancel, llSendSmsSend ;
	EditText etSendSmsMessage ;
	String smsContent ;


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.friend_profile, container, false);
		rootView.setClickable(true);
		context = container.getContext() ;
		inilization(rootView);
		
		getGroupList();
		
		friendId = getArguments().getInt("friendId");
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

		return rootView;
	}
	
	private Intent getIntent() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void addDataToSpinner () {
		ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(Home.context,
			    android.R.layout.simple_spinner_item, userGroups);
			  adapter_state
			    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			  spnrFriendProfileGrp.setAdapter(adapter_state);
			  spnrFriendProfileGrp.setOnItemSelectedListener(new MyCustomListener());
	}

	private class MyCustomListener implements OnItemSelectedListener {
		@Override
	    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {			
	        // Write your logic what you want to do on selecting the item
		    if (arg2!=0) {
		    	message=rowItemsList.get(arg2-1).getName();
		        Toast.makeText(getActivity(), message, 200).show();		        
		    } else {
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
			sendSMSDialog(getActivity());
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
  		  Log.e("Retrofit Error ", "FriendProfileFragment");
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
  	
    //<<<<<<<<<<============ Getting User Group List ==========>>>>>>>>>>>
  	
  	interface GroupListInterface {
  		@FormUrlEncoded
		@POST(StaticStrings.GET_GROUP_URL)
		void getGroup(
				@Field("userId") int userId,
				Callback<Response> getGroupListCallback); 		
  	}
		
	public void getGroupList() {
		if (NetworkStatus.getInstance(context).isInternetAvailable(context)) {
			Log.e(TAG, "Internet is available");
	    		
	    	restAdapter = new RestAdapter.Builder()
	        .setEndpoint(StaticStrings.SITE_URL)
	        .setLogLevel(RestAdapter.LogLevel.FULL)
	        .build();
	    		
	        CustomUtil.getInstance(context).showDialogBox("Server Connection", "Getting Group List...");
	    	int userId = CustomUtil.getInstance(context).getUserId();
	    		
	    	getGroupsObj = restAdapter.create(GroupListInterface.class);
	    	getGroupsObj.getGroup(userId, getGroupListCallback);
	    	
		} else {
			Log.e(TAG, "##########You are not online!!!!");
	    	NetworkStatus.getInstance(context).showDefaultNoInternetDialog(context);	    	
		}	
	}
		
	Callback<Response> getGroupListCallback = new Callback<Response>() {
		@Override
		public void failure(RetrofitError result) {
			Log.e("Retrofit Error ","Error in Groups Getting...");
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
					} else if (success.equalsIgnoreCase("0")) {
						String message = jsonResult.getString("message");
						Toast.makeText(context, message, Toast.LENGTH_LONG).show();
						NetworkStatus.getInstance(context).showDefaultAlertDialog(context, "Error", message);
					} else if (success.equalsIgnoreCase("1")) {
						String message = jsonResult.getString("message");
						JSONArray groupList = jsonResult.getJSONArray("groupList");
						int groupLength = groupList.length() ;
						userGroups = new String[groupLength] ;
						
						for (int i = 0; i < groupLength; i++) {
							userGroups[i] = groupList.getJSONObject(i).getString("group_name"); 
							Log.i("Group Name", userGroups[i]);
						}
						addDataToSpinner();
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
  	
  	//<<<<<<<<<<============Send SMS ==========>>>>>>>>>>>
  	
  	interface SendSMSInterface {
  		@FormUrlEncoded
		@POST(StaticStrings.SEND_SMS_URL)
		void sendSMS(
				@Field("userId") int userId,
				@Field("friendId") int friendId,
				@Field("sms") String sms,
				Callback<Response> sendSMSCallback); 		
  	}
		
	public void sendSMSToFriend() {
		if (NetworkStatus.getInstance(context).isInternetAvailable(context)) {
			Log.e(TAG, "Internet is available");
	    		
	    	restAdapter = new RestAdapter.Builder()
	        .setEndpoint(StaticStrings.SITE_URL)
	        .setLogLevel(RestAdapter.LogLevel.FULL)
	        .build();
	    		
	        CustomUtil.getInstance(context).showDialogBox("Server Connection", "SMS Sending...");
	    	int userId = CustomUtil.getInstance(context).getUserId();
	    		
	    	sendSMSObj = restAdapter.create(SendSMSInterface.class);
	    	sendSMSObj.sendSMS(userId, friendId, smsContent, sendSMSCallback);
	    	
		} else {
			Log.e(TAG, "##########You are not online!!!!");
	    	NetworkStatus.getInstance(context).showDefaultNoInternetDialog(context);	    	
		}	
	}
		
	Callback<Response> sendSMSCallback = new Callback<Response>() {
		@Override
		public void failure(RetrofitError result) {
			Log.e("Retrofit Error ","Error in SMS Sending...");
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
					} else if (success.equalsIgnoreCase("0")) {
						String message = jsonResult.getString("message");
						Toast.makeText(context, message, Toast.LENGTH_LONG).show();
						NetworkStatus.getInstance(context).showDefaultAlertDialog(context, "Error", message);
					} else if (success.equalsIgnoreCase("1")) {
						String message = jsonResult.getString("message");
						//JSONArray friendList = jsonResult.getJSONArray("friendList");
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
	
    // <<<<<<<<<<============ Block Friend ==========>>>>>>>>>>>
  
  	interface BlockFriendInterface {
  		@FormUrlEncoded
		@POST(StaticStrings.GET_FRIEND_LIST)
		void block(
				@Field("userId") int userId,
				Callback<Response> blockFriendCallback); 		
  	}
		
	public void blockFriend() {
		if (NetworkStatus.getInstance(context).isInternetAvailable(context)) {
			Log.e(TAG, "Internet is available");
	    		
	    	restAdapter = new RestAdapter.Builder()
	        .setEndpoint(StaticStrings.SITE_URL)
	        .setLogLevel(RestAdapter.LogLevel.FULL)
	        .build();
	    		
	        CustomUtil.getInstance(context).showDialogBox("Server Connection", "Friend Blocking...");
	    	int userId = CustomUtil.getInstance(context).getUserId();
	    		
	    	blockFriendObj = restAdapter.create(BlockFriendInterface.class);
	    	blockFriendObj.block(userId, blockFriendCallback);
	    	
		} else {
			Log.e(TAG, "##########You are not online!!!!");
	    	NetworkStatus.getInstance(context).showDefaultNoInternetDialog(context);	    	
		}	
	}
		
	Callback<Response> blockFriendCallback = new Callback<Response>() {
		@Override
		public void failure(RetrofitError result) {
			Log.e("Retrofit Error ","Error in Friend Blocking...");
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
					} else if (success.equalsIgnoreCase("0")) {
						String message = jsonResult.getString("message");
						Toast.makeText(context, message, Toast.LENGTH_LONG).show();
						NetworkStatus.getInstance(context).showDefaultAlertDialog(context, "Error", message);
					} else if (success.equalsIgnoreCase("1")) {
						String message = jsonResult.getString("message");
						//JSONArray friendList = jsonResult.getJSONArray("friendList");
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
	
	//<<<<<<<<<<<=========== Add friend to group ==========>>>>>>>>>>

	  	interface AddFriendToGroupInterface {
	  		@FormUrlEncoded
			@POST(StaticStrings.GET_FRIEND_LIST)
			void addGroup(
					@Field("userId") int userId,
					Callback<Response> addFriendToGroupCallback); 		
	  	}
			
		public void addFriendToGroup() {
			if (NetworkStatus.getInstance(context).isInternetAvailable(context)) {
				Log.e(TAG, "Internet is available");
		    		
		    	restAdapter = new RestAdapter.Builder()
		        .setEndpoint(StaticStrings.SITE_URL)
		        .setLogLevel(RestAdapter.LogLevel.FULL)
		        .build();
		    		
		        CustomUtil.getInstance(context).showDialogBox("Server Connection", "Adding in group...");
		    	int userId = CustomUtil.getInstance(context).getUserId();
		    		
		    	addFriendToGroupObj = restAdapter.create(AddFriendToGroupInterface.class);
		    	addFriendToGroupObj.addGroup(userId, addFriendToGroupCallback);
		    	
			} else {
				Log.e(TAG, "##########You are not online!!!!");
		    	NetworkStatus.getInstance(context).showDefaultNoInternetDialog(context);	    	
			}	
		}
			
		Callback<Response> addFriendToGroupCallback = new Callback<Response>() {
			@Override
			public void failure(RetrofitError result) {
				Log.e("Retrofit Error ","Error in Friend Blocking...");
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
						} else if (success.equalsIgnoreCase("0")) {
							String message = jsonResult.getString("message");
							Toast.makeText(context, message, Toast.LENGTH_LONG).show();
							NetworkStatus.getInstance(context).showDefaultAlertDialog(context, "Error", message);
						} else if (success.equalsIgnoreCase("1")) {
							String message = jsonResult.getString("message");
							//JSONArray friendList = jsonResult.getJSONArray("friendList");
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
		
		protected void sendSMSDialog(Context context) {

	    	smsDialog = new Dialog(context);
	    	smsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    	smsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	    	smsDialog.setContentView(R.layout.send_sms_dialog);
	    	smsDialog.setCanceledOnTouchOutside(false);
	 
	    	llSendSmsCancel = (LinearLayout) smsDialog.findViewById(R.id.llSendSmsCancel);
	    	llSendSmsSend = (LinearLayout) smsDialog.findViewById(R.id.llSendSmsSend);
	    	etSendSmsMessage = (EditText) smsDialog.findViewById(R.id.etSendSmsMessage);
	    	
	 		
	    	llSendSmsCancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					smsDialog.dismiss(); 
				}
			});
	 		
	    	llSendSmsSend.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					smsContent = etSendSmsMessage.getText().toString() ;
					if (smsContent.length() > 0 ) {
						sendSMSToFriend();
						smsDialog.dismiss();
					} else {
						
					}
				}
			});

	 		smsDialog.show();
	 	}
}