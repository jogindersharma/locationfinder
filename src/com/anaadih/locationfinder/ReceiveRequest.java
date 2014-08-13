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
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.anaadih.locationfinder.adapter.ReceiveRequestAdapter;
import com.anaadih.locationfinder.dto.ReceiveRequestDto;
import com.anaadih.locationfinder.networking.NetworkStatus;

public class ReceiveRequest extends Fragment {
	
	private ListView lvreceivefrdrequest ;
	private ReceiveRequestAdapter adapter ;
	private List<ReceiveRequestDto> receiveRequestlist ;
	private String[] name ;
	private int[] profImage ;
	
	String TAG ="inside ReceiverequestFragment";
	String searchInputVal;
	Context context;
	int searchByOptionsId;
	JSONArray friendListJsonArray;
	
	Response responseObj;
	String responseString;
	JSONObject jsonResult;
	RestAdapter restAdapter;
	ReceiveRequestInterface findFriendObj;
	
	interface ReceiveRequestInterface {
		@FormUrlEncoded
		@POST(StaticStrings.RECEIVE_REQUEST_URL)
		void fetchFriends(
				@Field("userId") int userId, 
				Callback<Response> receiveRequestCallback);
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);		
	    context=Home.context;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.receive_request, container, false);
        rootView.setClickable(true);
        
        initializer(rootView);
        findFriendsTask();
        /*rowItemsList = new ArrayList<SearchDataItem>() ;
		name = new String [] { "Amit Groch", "Joginder Sharma", "Pramod Kumar Varma", "Jitendra Kumar Yadav", "Pradeep Singh Gusian",
				"Anil Kumar Vishwakarma", "Pankaj Kumar Sharma", "Rajesh Kumar", "Ashok Kumar", "Pradeep Pandey" } ;

		profImage = new int[] { R.drawable.images, R.drawable.saab, R.drawable.bmw, R.drawable.images, R.drawable.saab, 
				R.drawable.bmw, R.drawable.images, R.drawable.saab, R.drawable.bmw, R.drawable.images } ;
        
        for (int i = 0 ; i < name .length ; i ++)
		{
        	SearchDataItem items = new SearchDataItem(name[i], profImage[i]) ;
			rowItemsList.add(items) ;
		}		
		adapter = new ReceiveRequestAdapter(getActivity(), rowItemsList);
		lvreceivefrdrequest.setAdapter(adapter);
		adapter.setOnViewButtonClickedListener(new ReceiveRequestAdapter.OnViewButtonClickedListener() {
			
			@Override
			public void OnAdd(String id) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "Add Friend Here...", Toast.LENGTH_SHORT).show();
			}
		});*/
		
        return rootView;    
    }
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
	    super.onActivityCreated(savedInstanceState);		    
    }
	
	public void initializer(View rootView) {
		lvreceivefrdrequest = (ListView) rootView.findViewById(R.id.lvreceivefrdrequest);
    }
		public void findFriendsTask() {
    	
        restAdapter = new RestAdapter.Builder()
        .setEndpoint(StaticStrings.SITE_URL)
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .build();
        
        if (NetworkStatus.getInstance(context).isInternetAvailable(context)) {
    		Log.e(TAG, "Internet is available");
    		CustomUtil.getInstance(context).showDialogBox("Fetching Request(S)", "Fetching User Request(S) ...");
    		int userId = CustomUtil.getInstance(context).getUserId();
    		
    		
    		findFriendObj = restAdapter.create(ReceiveRequestInterface.class);
    		findFriendObj.fetchFriends(userId, receiveRequestCallback);
    	} else {
    		Log.e(TAG, "##########You are not online!!!!");
    		NetworkStatus.getInstance(context).showDefaultNoInternetDialog(context);
    	}
    }
    
    Callback<Response> receiveRequestCallback = new Callback<Response>() {
    	  
    	  @Override
    	  public void failure(RetrofitError result) {
    		  //Log.e("Retrofit Error ",result.getMessage());
    		  Log.e("Retrofit Error ",result.toString());
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
  						friendListJsonArray = jsonResult.getJSONArray("requestList");
  						addDataToList();
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
    public void addDataToList() throws JSONException {
    	if(friendListJsonArray != null) {
    		
    		int totalArrayCount = friendListJsonArray.length();
    		
    		receiveRequestlist = new ArrayList<ReceiveRequestDto>() ;
    		
            for (int i = 0 ; i < totalArrayCount; i ++) {
            	
            	JSONObject currUserJsonObj = friendListJsonArray.getJSONObject(i);
            	
            	int currUserId = currUserJsonObj.getInt("user_id");
            	String currUserName = currUserJsonObj.getString("user_firstname")+" "+
            						currUserJsonObj.getString("user_lastname");
            	String currImageName = currUserJsonObj.getString("image_name");
            		
            	ReceiveRequestDto items = new ReceiveRequestDto(currUserId,currUserName,
            			currImageName);
            	receiveRequestlist.add(items) ;
    		}	
            
    		adapter = new ReceiveRequestAdapter(getActivity(), receiveRequestlist);
    		lvreceivefrdrequest.setAdapter(adapter);
    	}
    }
}

