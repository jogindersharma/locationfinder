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

import com.anaadih.locationfinder.adapter.HomeListAdapter;
import com.anaadih.locationfinder.dto.HomeDataItem;
import com.anaadih.locationfinder.networking.NetworkStatus;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
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
	private FragmentManager fragmentManager ;
	Context context;
	String TAG ="HomeFragment";
	
	// Retrofit Variables
	Response responseObj;
	String responseString;
	JSONObject jsonResult;
	GetFriendLatestLocationInterface getFriendLatestLocObj;
	RestAdapter restAdapter;
	JSONArray friendsLocsList ;
	
	
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
        getFriendsLatestLocationList();
		
		lvHomeAllList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				// TODO Auto-generated method stub
				 HomeDataItem getdate = rowItemsList.get(position);
				/*Intent myintent= new Intent(Home.context,Home.class);
				
			
	            myintent.putExtra("latitude", Double.parseDouble(getdate.getLatitude()));
	            myintent.putExtra("longitude", Double.parseDouble(getdate.getLongitude()));;
	            myintent.putExtra("user_id", Integer.parseInt(getdate.getFriendId()));
	            myintent.putExtra("user_firstname", getdate.getfName());
	            myintent.putExtra("user_lastname", getdate.getlName());
	            myintent.putExtra("image_name", getdate.getImageUrl());
	            myintent.putExtra("friendfullAddress", getdate.getAddress());
	            myintent.putExtra("responseTime", getdate.getDateTime());
	           startActivity(myintent);*/
	            
				 if(getdate.getAddress()!=null && !getdate.getAddress().isEmpty() && getdate.getAddress()!="" && getdate.getLatitude()!=null && getdate.getLatitude().length()>0){
				 
	       	Fragment fragment = null;
    		fragment = new TrackFrdLocationFragment();
    		Bundle myintent = new Bundle();
            
            //Bundle bundle = new Bundle();
          
           try {
			
        	   myintent.putDouble("latitude", Double.parseDouble(getdate.getLatitude()));
               myintent.putDouble("longitude", Double.parseDouble(getdate.getLongitude()));
               myintent.putInt("user_id", Integer.parseInt(getdate.getFriendId()));
               myintent.putString("user_firstname", getdate.getfName());
               myintent.putString("user_lastname", getdate.getlName());
               myintent.putString("image_name", getdate.getImageUrl());
               myintent.putString("friendfullAddress", getdate.getAddress());
               myintent.putString("responseTime", getdate.getDateTime());
               fragment.setArguments(myintent);
               FragmentManager fragmentManager = getFragmentManager();
   			  fragmentManager.beginTransaction()
   					.replace(R.id.frame_container, fragment).commit();
   	        
           } catch (Exception e) {
        	   Toast.makeText(getActivity(), "No Address for this User", Toast.LENGTH_SHORT).show();
		}
            
               
	          /*
				SearchResultFragment fr = new SearchResultFragment();
				FragmentTransaction ft = fragmentManager.beginTransaction();
				 fr.setArguments(myintent);
				ft.replace(R.id.frame_container, fr);
				ft.addToBackStack(SearchResultFragment.class.getName());
				ft.commit();*/
				 }else{
					 Toast.makeText(getActivity(), "No Address for this User", Toast.LENGTH_SHORT).show();
				 }
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
    		
    		CustomUtil.getInstance(context).showDialogBox("Server Connection", "Loading Data from Server...");
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
						} else if(success.equalsIgnoreCase("0")) {
							String message = jsonResult.getString("message");
							Toast.makeText(context, message, Toast.LENGTH_LONG).show();
							NetworkStatus.getInstance(context).showDefaultAlertDialog(context, "Error", message);
						} else if(success.equalsIgnoreCase("1")){
							String message = jsonResult.getString("message");
							friendsLocsList = jsonResult.getJSONArray("friendsLocsList");
							addDataToList() ;
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
	  	
	  	public void addDataToList() throws JSONException {
        	if(friendsLocsList != null) {
        		
        		int totalArrayCount = friendsLocsList.length();
        		
        		rowItemsList = new ArrayList<HomeDataItem>() ;
        		
                for (int i = 0 ; i < totalArrayCount; i ++) {
                	
                	JSONObject currUserJsonObj = friendsLocsList.getJSONObject(i);
                	
                	//int currUserId = currUserJsonObj.getInt("user_id");
                	/*String currUserName = currUserJsonObj.getString("user_firstname")+" "+
                						currUserJsonObj.getString("user_lastname");*/
                	String currImageUrl = currUserJsonObj.getString("image_name");
                	String currAddress = currUserJsonObj.getString("address");
                	String currUpdatedDateTime = currUserJsonObj.getString("updated_at");
                	String currentlatitue=currUserJsonObj.getString("latitude");
                	String currentlongtude=currUserJsonObj.getString("longitude");
                	String currentFriendId=currUserJsonObj.getString("user_id");
                	String Fname=currUserJsonObj.getString("user_firstname");
                	String Lname=currUserJsonObj.getString("user_lastname");
                	String currUserName=Fname+" "+Lname;
                	
                		
                	HomeDataItem items = new HomeDataItem(currUserName, currAddress, currUpdatedDateTime, currImageUrl,currentlatitue,currentlongtude,currentFriendId,Fname,Lname);
        			rowItemsList.add(items) ;
        		}	
                
        		adapter = new HomeListAdapter(getActivity(), rowItemsList);
        		lvHomeAllList.setAdapter(adapter);
        	}
        }
}