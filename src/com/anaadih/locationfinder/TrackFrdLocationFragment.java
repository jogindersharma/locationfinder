package com.anaadih.locationfinder;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TrackFrdLocationFragment extends Fragment {

	ImageView ivTrackFriendPic;
	TextView tvTrackFriendName,tvTrackFriendDateTime,tvTrackFriendAddress;
	Button btnTrackFriendMap;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 View rootView = inflater.inflate(R.layout.track_friend_location, container, false);
	        rootView.setClickable(true);
	        initilization(rootView);
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
				startActivity(map);
			}
		});
		
	}

}
