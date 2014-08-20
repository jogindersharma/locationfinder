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

import com.anaadih.locationfinder.FriendFragment.GetFriendListInterface;
import com.anaadih.locationfinder.adapter.HomeListAdapter;
import com.anaadih.locationfinder.dto.HomeDataItem;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class HomeFragment extends Fragment {
	
	private ListView lvHomeAllList ;
	private Button btnHomeFilter ;
	private Spinner spnrHome ;
	private HomeListAdapter adapter ;
	private List<HomeDataItem> rowItemsList ;
	private String[] name, group, address, datetime ;
	private int[] profImage ;
	private FragmentManager fragmentManager ;
	Context context;
	String TAG ="HomeFragment";
	
	// Retrofit Variables
	Response responseObj;
	String responseString;
	JSONObject jsonResult;
	GetFriendLatestLocationInterface getFriendLatestLocObj;
	RestAdapter restAdapter;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);		    
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        rootView.setClickable(true);
        context = container.getContext();
        fragmentManager = getFragmentManager();
        
        initializer(rootView);
        //getFriendsLatestLocationList();
        
        rowItemsList = new ArrayList<HomeDataItem>() ;
		name = new String [] { "Amit Groch", "Joginder Sharma", "Pramod Kumar Varma", "Jitendra Kumar Yadav", "Pradeep Singh Gusian",
				"Anil Kumar Vishwakarma", "Pankaj Kumar Sharma", "Rajesh Kumar", "Ashok Kumar", "Pradeep Pandey" } ;
		group = new String [] {"Faimily", "Friend", "Collegue", "faimily", "Friend", "Office", "Relative", "Nighbour", "Friend", "Faimily" } ;
		address = new String [] { "Indira Puram", "Anaadih Softech", "C-279 New Ashok Nagar", "nKaps Intellect", "Subharti University",
				"Migital Magic", "Clavax India", "Innovative AIIMS", "Fransccicon India", "Tata Consultancy Services (TCS)" } ;
		datetime = new String [] {"11 May 2014 (6:30 PM)", "11 May 2014 (6:30 PM)", "11 May 2014 (6:30 PM)", "11 May 2014 (6:30 PM)", "11 May 2014 (6:30 PM)",
				"11 May 2014 (6:30 PM)", "11 May 2014 (6:30 PM)", "11 May 2014 (6:30 PM)", "11 May 2014 (6:30 PM)", "11 May 2014 (6:30 PM)"} ;
		profImage = new int[] { R.drawable.images, R.drawable.saab, R.drawable.bmw, R.drawable.images, R.drawable.saab, 
				R.drawable.bmw, R.drawable.images, R.drawable.saab, R.drawable.bmw, R.drawable.images } ;
        
        for (int i = 0 ; i < name .length ; i ++)
		{
        	HomeDataItem items = new  HomeDataItem(name[i], group[i], address[i], datetime[i], profImage[i]) ;
			rowItemsList.add(items) ;
		}		
		adapter = new HomeListAdapter(getActivity(), rowItemsList);
		lvHomeAllList.setAdapter(adapter);
		
		lvHomeAllList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				SearchResultFragment fr = new SearchResultFragment();
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.replace(R.id.frame_container, fr);
				ft.addToBackStack(SearchResultFragment.class.getName());
				ft.commit();
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
		lvHomeAllList = (ListView) rootView.findViewById(R.id.lvHomeAllList);
		btnHomeFilter = (Button) rootView.findViewById(R.id.btnHomeFilter);
		spnrHome = (Spinner) rootView.findViewById(R.id.spnrHome);
    }
	
/* ++++++++++++++++ ============  GET Friends Latest Location   ==========*/
	
	interface GetFriendLatestLocationInterface {
		@FormUrlEncoded
		@POST(StaticStrings.GET_FRIENDS_LATEST_LOCATION)
		void getFriendsLatestLocs(
				@Field("userId") int userId,
				Callback<Response> getFriendsLatestLocCallback);
	}
	
	public void getFriendsLatestLocationList() {
		if (NetworkStatus.getInstance(context).isInternetAvailable(context)) {
    		Log.e(TAG, "Internet is available");
    		
    		restAdapter = new RestAdapter.Builder()
            .setEndpoint(StaticStrings.SITE_URL)
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build();
    		
    		CustomUtil.getInstance(context).showDialogBox("Server Connection", "Updating Your Response on Server...");
    		int userId = CustomUtil.getInstance(context).getUserId();
    		
    		getFriendLatestLocObj = restAdapter.create(GetFriendLatestLocationInterface.class);
    		getFriendLatestLocObj.getFriendsLatestLocs(userId, getFriendsLatestLocCallback);
    	} else {
    		Log.e(TAG, "##########You are not online!!!!");
    		NetworkStatus.getInstance(context).showDefaultNoInternetDialog(context);
    	}
	}
	
	Callback<Response> getFriendsLatestLocCallback = new Callback<Response>() {
	  	  
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
							JSONArray friendsLocsList = jsonResult.getJSONArray("friendsLocsList");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
	  	      } else {
	  	    	Log.e(TAG, "SeverResponse is not a JSON =>"+responseString);
	  	      }
	  	    CustomUtil.getInstance(context).hideDialogBox();
	  	  }
	  	};
}