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

import com.anaadih.locationfinder.adapter.FriendListAdapter;
import com.anaadih.locationfinder.adapter.HomeListAdapter;
import com.anaadih.locationfinder.adapter.SearchListAdapter;
import com.anaadih.locationfinder.dto.FriendListDataItem;
import com.anaadih.locationfinder.dto.HomeDataItem;
import com.anaadih.locationfinder.dto.SearchDataItem;
import com.anaadih.locationfinder.networking.NetworkStatus;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FriendFragment extends Fragment {
	
	private ListView lvFriendList ;
	private Button btnHomeFilter ;
	private Spinner spnrHome ;
	private FriendListAdapter adapter ;
	private List<FriendListDataItem> rowItemsList ;
	private String[] name, group, address, datetime ;
	private int[] profImage ;
	private FragmentManager fragmentManager ;
	
	// Retrofit Variables
	Response responseObj;
	String responseString;
	JSONObject jsonResult;
	GetFriendListInterface getFriendListObj;
	String TAG = "FriendFragment";
	Context context;
	RestAdapter restAdapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);		    
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_friend, container, false);
        rootView.setClickable(true);
        context = container.getContext();
        fragmentManager = getFragmentManager();
        
        initializer(rootView);
        getFriendList();
        
        return rootView;    
    }
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);		    
    }
	
	public void initializer(View rootView) {
		lvFriendList = (ListView) rootView.findViewById(R.id.lvFriendList);
		btnHomeFilter = (Button) rootView.findViewById(R.id.btnHomeFilter);
		spnrHome = (Spinner) rootView.findViewById(R.id.spnrHome);
		
		lvFriendList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int userid=rowItemsList.get(position).getUserId();
				Bundle bundle=new Bundle();
				bundle.putInt("friendId", userid);
				
				FriendProfileFragment fr = new FriendProfileFragment();
				FragmentTransaction ft = fragmentManager.beginTransaction();
				fr.setArguments(bundle);
				ft.replace(R.id.frame_container, fr);
				ft.addToBackStack(FriendProfileFragment.class.getName());
				ft.commit();
				//Toast.makeText(getActivity(), ""+userid, 300).show();
			}
		});
    }
	/* ++++++++++++++++ ============  GET Friend List  ==========*/
	
	interface GetFriendListInterface {
		@FormUrlEncoded
		@POST(StaticStrings.GET_FRIEND_LIST)
		void getFriends(
				@Field("userId") int userId,
				Callback<Response> getFriendListCallback);
	}
	
	public void getFriendList() {
		if (NetworkStatus.getInstance(context).isInternetAvailable(context)) {
    		Log.e(TAG, "Internet is available");
    		
    		restAdapter = new RestAdapter.Builder()
            .setEndpoint(StaticStrings.SITE_URL)
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build();
    		
    		CustomUtil.getInstance(context).showDialogBox("Server Connection", "Updating Your Response on Server...");
    		int userId = CustomUtil.getInstance(context).getUserId();
    		
    		getFriendListObj = restAdapter.create(GetFriendListInterface.class);
    		getFriendListObj.getFriends(userId, getFriendListCallback);
    	} else {
    		Log.e(TAG, "##########You are not online!!!!");
    		NetworkStatus.getInstance(context).showDefaultNoInternetDialog(context);
    	}
	}
	
	Callback<Response> getFriendListCallback = new Callback<Response>() {
	  	  
	  	  @Override
	  	  public void failure(RetrofitError result) {
	  		  Log.e("Retrofit Error ","Error in fetching Friend list.");
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
							NetworkStatus.getInstance(context).showDefaultAlertDialog(context, "Error", message);
						} else if(success.equalsIgnoreCase("1")){
							String message = jsonResult.getString("message");
							JSONArray friendList = jsonResult.getJSONArray("friendList");
							sendFriendListToAdapter(friendList);
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
	  	
	  	public void sendFriendListToAdapter(JSONArray friendList) throws JSONException {
	  		if(friendList != null) {
	  			rowItemsList = new ArrayList<FriendListDataItem>() ;
	    		int totalArrayCount = friendList.length();
	            for (int i = 0 ; i < totalArrayCount; i ++) {
	            	JSONObject currUserJsonObj = friendList.getJSONObject(i);
	            	
	            	int currUserId = currUserJsonObj.getInt("user_id");
	            	String currUserName = currUserJsonObj.getString("user_firstname")+" "+
	            						currUserJsonObj.getString("user_lastname");
	            	String currImageName = currUserJsonObj.getString("image_name");
	            	String currGroupName = currUserJsonObj.getString("group_name");
	            	FriendListDataItem items = new FriendListDataItem(currUserId,currUserName,currGroupName,
	            			currImageName);
	    			rowItemsList.add(items) ;
	    		}
	    		adapter = new FriendListAdapter(getActivity(), rowItemsList);
	    		lvFriendList.setAdapter(adapter);
	    	}
	  	}
}