package com.anaadih.locationfinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

import com.anaadih.locationfinder.adapter.GroupListAdapter;
import com.anaadih.locationfinder.dto.GroupsDataItem;
import com.anaadih.locationfinder.networking.NetworkStatus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GroupsFragment extends Fragment {
	
	ListView lvGroupsAllList ;
	Context context ;
	String TAG = "GroupsFragment" ;
	private List<GroupsDataItem> rowItemsList ;
	GroupListAdapter adapter ;
	
	// Dialog Box Variables
	Dialog editGroupDialog ;
	LinearLayout llEditGroupCancel, llEditGroupDone ;
	TextView tvEditGroupHeader, tvEditGroupHint ;
	EditText etEditGroupName ;
	
	// Retrofit variables	
	Response responseObj ;
	String responseString ;
	JSONObject jsonResult ;
	GroupListInterface getGroupsObj ;
	EditGroupInterface editGroupObj ;
	DeleteGroupInterface deleteGroupObj ;
	RestAdapter restAdapter ;
	JSONArray groupList ;
	int currGroupId = 0;
	String currGroupName;
	String currChangedGroupName;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.group_fragment, container, false);
        rootView.setClickable(true);
        context = container.getContext() ;
        
        getGroupList() ;
        
        lvGroupsAllList = (ListView) rootView.findViewById(R.id.lvGroupsAllList);

		return rootView;
	}
	
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
		  	CustomUtil.getInstance(context).showNetworkErrorAlertBox(result);
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
						groupList = jsonResult.getJSONArray("groupList");
						addDataToList();
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
	
	public void addDataToList() throws JSONException {
    	if(groupList != null) {
    		
    		int totalArrayCount = groupList.length();
    		
    		rowItemsList = new ArrayList<GroupsDataItem>() ;
    		
            for (int i = 0 ; i < totalArrayCount; i ++) {
            	
            	JSONObject currUserJsonObj = groupList.getJSONObject(i);
            	
            	int groupId = currUserJsonObj.getInt("group_id");
            	String groupName = currUserJsonObj.getString("group_name");

            	GroupsDataItem items = new GroupsDataItem(groupId, groupName) ;
    			rowItemsList.add(items) ;
    		}	
            
    		adapter = new GroupListAdapter(getActivity(), rowItemsList);
    		lvGroupsAllList.setAdapter(adapter);
    		
    		adapter.setEditButtonClickedListener(new GroupListAdapter.OnEditButtonClickedListener() {
    			
    			@Override
    			public void OnEdit(String id, int groupId, String groupName ) {
    				// TODO Auto-generated method stub
    				if(groupId != 1) {
	    				currGroupId = groupId;
	    				currGroupName = groupName;
	    				showEditGroupDialog(getActivity(), "Edit Group", "Enter New Group Name");
	    				Toast.makeText(getActivity(), "Edit"+groupId +" : "+groupName, Toast.LENGTH_SHORT).show();
    				} else {
    					Toast.makeText(getActivity(), "You can not Edit Default Group.", Toast.LENGTH_SHORT).show();
    				}
    			}
    		});
            
            adapter.setDeleteButtonClickedListener(new GroupListAdapter.OnDeleteButtonClickedListener() {
    			
    			@Override
    			public void OnDelete(String id, final int groupId) {
    				// TODO Auto-generated method stub
    				if(groupId != 1) {
	    				AlertDialog .Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
	    				alertDialogBuilder.setTitle("Delete Group");
	    				alertDialogBuilder.setMessage("Are you sure you want to delete this group ???")
	    				.setCancelable(true)
	    				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								deleteGroupList(groupId);
							}
						})
						.setNegativeButton("No", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});
	    				
	    				// create alert dialog
	    				AlertDialog alertDialog = alertDialogBuilder.create();
	    				alertDialog.show();
    			} else {
    				Toast.makeText(getActivity(), "You can not Delete the Default Group.", Toast.LENGTH_SHORT).show();
    			}
    			}
    		});
    	}
    }
	
    //<<<<<<<<<<============ Edit User Groups ==========>>>>>>>>>>>
  	
  	interface EditGroupInterface {
  		@FormUrlEncoded
		@POST(StaticStrings.EDIT_GROUP_URL)
		void editGroup(
				@Field("groupId") int userId,
				@Field("groupName") String groupName,
				Callback<Response> editGroupsCallback); 		
  	}
		
	public void editGroupList() {
		if (NetworkStatus.getInstance(context).isInternetAvailable(context)) {
			Log.e(TAG, "Internet is available");
	    		
	    	restAdapter = new RestAdapter.Builder()
	        .setEndpoint(StaticStrings.SITE_URL)
	        .setLogLevel(RestAdapter.LogLevel.FULL)
	        .build();
	    		
	        CustomUtil.getInstance(context).showDialogBox("Server Connection", "Editing Group...");
	    		
	    	editGroupObj = restAdapter.create(EditGroupInterface.class);
	    	editGroupObj.editGroup(currGroupId, currChangedGroupName, editGroupsCallback);
	    	
		} else {
			Log.e(TAG, "##########You are not online!!!!");
	    	NetworkStatus.getInstance(context).showDefaultNoInternetDialog(context);	    	
		}	
	}
		
	Callback<Response> editGroupsCallback = new Callback<Response>() {
		@Override
		public void failure(RetrofitError result) {
			Log.e("Retrofit Error ","Error in Groups Editing...");
		  	CustomUtil.getInstance(context).hideDialogBox();
		  	CustomUtil.getInstance(context).showNetworkErrorAlertBox(result);
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
						NetworkStatus.getInstance(context).showDefaultAlertDialog(context, "Response", message);
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
	
    //<<<<<<<<<<============ Delete User Groups ==========>>>>>>>>>>>
  	
  	interface DeleteGroupInterface {
  		@FormUrlEncoded
		@POST(StaticStrings.DELETE_GROUP_URL)
		void deleteGroup(
				@Field("groupId") int userId,
				Callback<Response> deleteGroupsCallback); 		
  	}
		
	public void deleteGroupList(int groupId) {
		if (NetworkStatus.getInstance(context).isInternetAvailable(context)) {
			Log.e(TAG, "Internet is available");
	    		
	    	restAdapter = new RestAdapter.Builder()
	        .setEndpoint(StaticStrings.SITE_URL)
	        .setLogLevel(RestAdapter.LogLevel.FULL)
	        .build();
	    		
	        CustomUtil.getInstance(context).showDialogBox("Server Connection", "Deleting Group...");
	    		
	    	deleteGroupObj = restAdapter.create(DeleteGroupInterface.class);
	    	deleteGroupObj.deleteGroup(groupId, deleteGroupsCallback);
	    	
		} else {
			Log.e(TAG, "##########You are not online!!!!");
	    	NetworkStatus.getInstance(context).showDefaultNoInternetDialog(context);	    	
		}	
	}
		
	Callback<Response> deleteGroupsCallback = new Callback<Response>() {
		@Override
		public void failure(RetrofitError result) {
			Log.e("Retrofit Error ","Error in Groups Editing...");
		  	CustomUtil.getInstance(context).hideDialogBox();
		  	CustomUtil.getInstance(context).showNetworkErrorAlertBox(result);
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
						NetworkStatus.getInstance(context).showDefaultAlertDialog(context, "Response", message);
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
	
	// Edit Group Dialog Box...
	
	protected void showEditGroupDialog(Context context, String header, String hint) {

    	editGroupDialog = new Dialog(context);
    	editGroupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	editGroupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    	editGroupDialog.setContentView(R.layout.profile_update_popup);
    	editGroupDialog.setCanceledOnTouchOutside(false);
 
    	llEditGroupCancel = (LinearLayout) editGroupDialog.findViewById(R.id.llProfileUpdateCancel);
    	llEditGroupDone = (LinearLayout) editGroupDialog.findViewById(R.id.llProfileUpdateSave);
    	etEditGroupName = (EditText) editGroupDialog.findViewById(R.id.etProfileUpdateInput);
    	tvEditGroupHeader = (TextView) editGroupDialog.findViewById(R.id.tvProfileUpdateHeader);
    	tvEditGroupHint = (TextView) editGroupDialog.findViewById(R.id.tvProfileUpdateHint);
    	
    	tvEditGroupHeader.setText(header);
    	tvEditGroupHint.setText(hint);
    	etEditGroupName.setText(currGroupName);
 		
 		llEditGroupCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editGroupDialog.dismiss(); 
			}
		});
 		
 		llEditGroupDone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currChangedGroupName = etEditGroupName.getText().toString();
				if(currChangedGroupName != null && currChangedGroupName.length() > 0 && currGroupId != 0) {
					editGroupList();
					adapter.notifyDataSetChanged();
				}
				editGroupDialog.dismiss(); 
			}
		});

 		editGroupDialog.show();
 	}
}
