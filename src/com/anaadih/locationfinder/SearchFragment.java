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

import com.anaadih.locationfinder.Login.UserLoginInterface;
import com.anaadih.locationfinder.adapter.SearchListAdapter;
import com.anaadih.locationfinder.dto.SearchDataItem;
import com.anaadih.locationfinder.networking.NetworkStatus;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchFragment extends Fragment {
	
	private ListView lvSearchAllList ;
	private ImageView ivSearchBtn ;
	EditText etSearchInput ;
	private Spinner spnrSearch ;
	private SearchListAdapter adapter ;
	private List<SearchDataItem> rowItemsList ;
	private String[] name ;
	private int[] profImage ;
	String TAG ="inside SearchFragment";
	String searchInputVal;
	Context context;
	int searchByOptionsId;
	JSONArray friendListJsonArray;
	
	Response responseObj;
	String responseString;
	JSONObject jsonResult;
	RestAdapter restAdapter;
	FindFriendsInterface findFriendObj;
    
    interface FindFriendsInterface {
		@FormUrlEncoded
		@POST(StaticStrings.Find_FRIENDS_URL)
		void fetchFriends(
				@Field("userId") int userId, 
				@Field("searchByOptionsId") int searchByOptionsId,
				@Field("searchInput") String searchInput,
				Callback<Response> findFriendsCallback);
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);		    
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        rootView.setClickable(true);
        context = container.getContext();
        initializer(rootView);
        
        rowItemsList = new ArrayList<SearchDataItem>() ;
		name = new String [] { "Amit Groch", "Joginder Sharma", "Pramod Kumar Varma", "Jitendra Kumar Yadav", "Pradeep Singh Gusian",
				"Anil Kumar Vishwakarma", "Pankaj Kumar Sharma", "Rajesh Kumar", "Ashok Kumar", "Pradeep Pandey" } ;

		profImage = new int[] { R.drawable.images, R.drawable.saab, R.drawable.bmw, R.drawable.images, R.drawable.saab, 
				R.drawable.bmw, R.drawable.images, R.drawable.saab, R.drawable.bmw, R.drawable.images } ;
        
        for (int i = 0 ; i < name .length ; i ++) {
        	SearchDataItem items = new SearchDataItem(name[i], profImage[i]) ;
			rowItemsList.add(items) ;
		}		
		adapter = new SearchListAdapter(getActivity(), rowItemsList);
		lvSearchAllList.setAdapter(adapter);
		
		adapter.setOnViewButtonClickedListener(new SearchListAdapter.OnViewButtonClickedListener() {
			
			@Override
			public void OnAdd(String id) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "Add Friend Here...", Toast.LENGTH_SHORT).show();
			}
		});
		
        return rootView;    
    }
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
	    super.onActivityCreated(savedInstanceState);		    
    }
	
	public void initializer(View rootView) {
		lvSearchAllList = (ListView) rootView.findViewById(R.id.lvSearchAllList);
		ivSearchBtn = (ImageView) rootView.findViewById(R.id.ivSearchBtn);
		etSearchInput = (EditText) rootView.findViewById(R.id.etSearchInput);
		spnrSearch = (Spinner) rootView.findViewById(R.id.spnrSearch);
		spnrSearch.setOnItemSelectedListener(new MyOnItemSelectedListener());
		ivSearchBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				findFriendsTask();
			}
		});
		
    }
	
	class MyOnItemSelectedListener implements OnItemSelectedListener {
		 
	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    	searchByOptionsId = pos;
	        switch (pos) {
				case 0:
					etSearchInput.setHint("Enter Code");
					break;
				case 1:
					etSearchInput.setHint("Enter Name");
					break;
				case 2:
					etSearchInput.setHint("Enter Email-Id");
					break;	
				case 3:
					etSearchInput.setHint("Enter Mobile No");
					break;
				default:
					break;
			}
	    }
	
	    public void onNothingSelected(AdapterView<?> parent) {
	        // Do nothing.
	    }
	}
	
	public void findFriendsTask() {
    	
        restAdapter = new RestAdapter.Builder()
        .setEndpoint(StaticStrings.SITE_URL)
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .build();
        
        if (NetworkStatus.getInstance(context).isInternetAvailable(context)) {
    		Log.e(TAG, "Internet is available");
    		CustomUtil.getInstance(context).showDialogBox("Fetching User(S)", "Fetching User(S) ...");
    		int userId = CustomUtil.getInstance(context).getUserId();
    		String searchInputVal = etSearchInput.getText().toString();
    		
    		
    		findFriendObj = restAdapter.create(FindFriendsInterface.class);
    		findFriendObj.fetchFriends(userId, searchByOptionsId,searchInputVal, findFriendsCallback);
    	} else {
    		Log.e(TAG, "##########You are not online!!!!");
    		NetworkStatus.getInstance(context).showDefaultNoInternetDialog(context);
    	}
    }
    
    Callback<Response> findFriendsCallback = new Callback<Response>() {
    	  
    	  @Override
    	  public void failure(RetrofitError result) {
    		  Log.e("Retrofit Error ",result.getMessage());
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
  						friendListJsonArray = jsonResult.getJSONArray("friendList");
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
}

