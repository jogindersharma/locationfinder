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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.anaadih.locationfinder.R;
import com.anaadih.locationfinder.adapter.HomeListAdapter;
import com.anaadih.locationfinder.adapter.NavDrawerListAdapter;
import com.anaadih.locationfinder.dto.HomeDataItem;
import com.anaadih.locationfinder.dto.NavDrawerItem;
import com.anaadih.locationfinder.networking.NetworkStatus;

public class Home extends Activity {
	
	//<<<<<<<<<+++++++++ Home Fragment Start +++++++++>>>>>>>>>>
	
	
	private ListView lvHomeAllList ;
	private HomeListAdapter adapter1 ;
	private List<HomeDataItem> rowItemsList ;
	private FragmentManager fragmentManager ;
	String TAG ="HomePage";
	
	// Retrofit Variables
	Response responseObj;
	String responseString;
	JSONObject jsonResult;
	GetFriendLatestLocationInterface getFriendLatestLocObj;
	RestAdapter restAdapter;
	JSONArray friendsLocsList ;
	
	
	//<<<<<<<<<+++++++++ Home Fragment End +++++++++>>>>>>>>>>
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	public static Context context;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	String friendFname,friendLname,friendImageUrl,friendfullAddress,responseTime;
	int friendId;
	Double latitude,longitude;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homeactivity);
		this.context=this;
		fragmentManager = getFragmentManager();
		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		
		lvHomeAllList = (ListView) findViewById(R.id.lvHomeAllList);
		
		
		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		// Recieve Request of friends ;
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// Create Group 
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
		// Group List 
	    navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
		
		
		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
/*
		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}*/
		
		//Send nofication to view location on map
		Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        
        if ( extras != null ) {
        	
        	if ( intent.getExtras().getString("latitude") !="" ) {
        		Log.e("Home page activity bundle in trak friend location", ""+extras);
        	    latitude =  extras.getDouble("latitude");
    		    longitude =  extras.getDouble("longitude");
    		    friendId =  extras.getInt("user_id");
    		    friendFname =  extras.getString("user_firstname");
    		    friendLname =  extras.getString("user_lastname");
    		    friendImageUrl =  extras.getString("image_name");
    		    friendfullAddress=extras.getString("friendfullAddress");
    		    responseTime=extras.getString("responseTime");
    		
    		    Fragment fragment = null;
    		    fragment = new TrackFrdLocationFragment();
    		    Bundle bundle = new Bundle();
            
    		    bundle.putDouble("latitude", latitude);
    		    bundle.putDouble("longitude", longitude);
    		    bundle.putInt("user_id", friendId);
    		    bundle.putString("user_firstname", friendFname);
    		    bundle.putString("user_lastname", friendLname);
    		    bundle.putString("image_name", friendImageUrl);
    		    bundle.putString("friendfullAddress", friendfullAddress);
    		    bundle.putString("responseTime", responseTime);
                
                fragment.setArguments(bundle);  			         
			    FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.replace(R.id.frame_container, fragment, "fragment");
				ft.addToBackStack(null);
				ft.commit();				
        	} 
        	
        } else {
        	getFriendsLatestLocationList();
        }
        
        lvHomeAllList.setOnItemClickListener(new OnItemClickListener() {
        	
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
								
				// TODO Auto-generated method stub
				HomeDataItem getdate = rowItemsList.get(position);
	            
				if ( getdate.getAddress() != null && !getdate.getAddress().isEmpty() 
						&& getdate.getAddress() !="" && getdate.getLatitude() != null 
						&& getdate.getLatitude().length() > 0 ) {
					
				 Fragment fragment = null;
    		     fragment = new TrackFrdLocationFragment();
    		     Bundle bundle = new Bundle();
          
                 try {
                	 bundle.putDouble("latitude", Double.parseDouble(getdate.getLatitude()));
                	 bundle.putDouble("longitude", Double.parseDouble(getdate.getLongitude()));
                	 bundle.putInt("user_id", Integer.parseInt(getdate.getFriendId()));
                	 bundle.putString("user_firstname", getdate.getfName());
                	 bundle.putString("user_lastname", getdate.getlName());
                	 bundle.putString("image_name", getdate.getImageUrl());
                	 bundle.putString("friendfullAddress", getdate.getAddress());
                	 bundle.putString("responseTime", getdate.getDateTime());
                     
                     fragment.setArguments(bundle);  			         
   			         FragmentTransaction ft = fragmentManager.beginTransaction();
   				     ft.replace(R.id.frame_container, fragment, "fragment");
   				     ft.addToBackStack(null);
   				     ft.commit();
   			         
                 } catch (Exception e) {
                	 Toast.makeText(context, "No Address for this User", Toast.LENGTH_SHORT).show();              	 
                 }
                 
				} else {
					Toast.makeText(context, "No Address for this User", Toast.LENGTH_SHORT).show();					
				}				
			}			
        });
	}
	
	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	@SuppressLint("NewApi")
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			//fragment = new HomeFragment();
			Intent homeIntent = new Intent(context,Home.class);
			homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(homeIntent);
			break;
		case 1:
			fragment = new SearchFragment();
			break;
		case 2:
			fragment = new FriendFragment();
			break;
		case 3:
			fragment = new SelfProfileFragment();
			break;
		
		case 4:
			fragment = new ReceiveRequest();
			break;
			
		case 5:
			fragment = new CreateGroupFragment();
			break;
			
		case 6:
			fragment = new GroupsFragment();
			break;
			
		default:
			break;
		}

		if (fragment != null) {			
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.frame_container, fragment, "fragment");
			ft.addToBackStack(null);
			ft.commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
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
                
        		adapter1 = new HomeListAdapter(context, rowItemsList);
        		lvHomeAllList.setAdapter(adapter1);
        	}
        }
	  	
	  	@Override
	  	public void onBackPressed() {
	  		// TODO Auto-generated method stub
	  		//super.onBackPressed();
	  		if (fragmentManager.getBackStackEntryCount() > 0) {
	  			fragmentManager.popBackStackImmediate();
	  		} else {
	  			AlertDialog .Builder alertDialogBuilder = new AlertDialog.Builder(context);
				alertDialogBuilder.setTitle("Exit Application");
				alertDialogBuilder.setMessage("Are you sure you want to exit this app ?")
				.setCancelable(true)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//super.onBackPressed();
						finish();
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
	  		}
	  	}
}