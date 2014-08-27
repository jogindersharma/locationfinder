package com.anaadih.locationfinder;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TrackFrdLocationFragment extends Fragment {

	ImageView ivTrackFriendPic;
	TextView tvTrackFriendName,tvTrackFriendDateTime,tvTrackFriendAddress;
	Button btnTrackFriendMap;
	String friendFname,friendLname,friendImageUrl,friendfullAddress,responseTime;
	int friendId;
	Double latitude,longitude;
	private ImageLoader imageLoader ;
	private DisplayImageOptions options ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 View rootView = inflater.inflate(R.layout.track_friend_location, container, false);
	        rootView.setClickable(true);
	        initilization(rootView);
	        
	       /* Intent intent = getActivity().getIntent();
	        Bundle extras = intent.getExtras();*/
	        Bundle extras = this.getArguments();
	        Log.e("bundle in trak friend location", ""+extras);
	        if(extras.getDouble("latitude") !=0.0) {
	        	latitude =  extras.getDouble("latitude");
        		longitude =  extras.getDouble("longitude");
        		friendId =  extras.getInt("user_id");
        		friendFname =  extras.getString("user_firstname");
        		friendLname =  extras.getString("user_lastname");
        		friendImageUrl =  extras.getString("image_name");
        		friendfullAddress=extras.getString("friendfullAddress");
        		responseTime=extras.getString("responseTime");
        		
        		tvTrackFriendName.setText(friendFname+" "+friendLname);
        		tvTrackFriendAddress.setText(friendfullAddress);
        		tvTrackFriendDateTime.setText(responseTime);
        		imageLoader = ImageLoader.getInstance();
        		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

        		options = new DisplayImageOptions.Builder()
        		.displayer(new RoundedBitmapDisplayer((int) 27.5f))
        		.showStubImage(R.drawable.ic_launcher) //this is the image that will be displayed if download fails
        	    .cacheInMemory()
        		.cacheOnDisc()
        		.build();
        		 imageLoader.displayImage(friendImageUrl, ivTrackFriendPic, options);
        		
			}
	        
		return rootView;
	}
	private void initilization(View rootView) {
		// TODO Auto-generated method stub
		ivTrackFriendPic=(ImageView) rootView.findViewById(R.id.ivTrackFriendPic);
		tvTrackFriendAddress=(TextView) rootView.findViewById(R.id.tvTrackFriendAddress);
		tvTrackFriendDateTime=(TextView) rootView.findViewById(R.id.tvTrackFriendDateTime);
		tvTrackFriendName=(TextView) rootView.findViewById(R.id.tvTrackFriendName);
		btnTrackFriendMap=(Button) rootView.findViewById(R.id.btnTrackFriendMap);
		btnTrackFriendMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent map = new Intent(Home.context,MapActivity.class);
				map.putExtra("latitude", latitude);
				map.putExtra("longitude", longitude);
				map.putExtra("friendFname", friendFname+" "+friendLname);
				map.putExtra("friendfullAddress", friendfullAddress);
				
				startActivity(map);
			}
		});
		
	}

}
