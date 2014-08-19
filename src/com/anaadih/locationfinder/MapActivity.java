package com.anaadih.locationfinder;

import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MapActivity extends Activity {

	// Google Map
		private GoogleMap googleMap;
		Context context;
		//GPSTracker gps;
		double latitude ;
		double longitude ;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.map_location);
			this.context=this;
			
			
			
			
			
			
			
			/* gps = new GPSTracker(MainActivity.this);
			try {
				// Loading map
				
				// check if GPS enabled		
		        if(gps.canGetLocation()){
		        	
		        	 latitude = gps.getLatitude();
		        	 longitude = gps.getLongitude();
		        	 System.out.println("latitude"+latitude);
		        	 System.out.println("longgitude"+longitude);
		        	
		        	// \n is for new line
		        	//Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();	
		        }else{
		        	// can't get location
		        	// GPS or Network is not enabled
		        	// Ask user to enable GPS/network in settings
		        	gps.showSettingsAlert();
		        }
				
				
*/				
				initilizeMap();

				// Changing map type
				googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				// googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				// googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				// googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				// googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

				// Showing / hiding your current location
				googleMap.setMyLocationEnabled(true);

				// Enable / Disable zooming controls
				googleMap.getUiSettings().setZoomControlsEnabled(true);

				// Enable / Disable my location button
				googleMap.getUiSettings().setMyLocationButtonEnabled(true);

				// Enable / Disable Compass icon
				googleMap.getUiSettings().setCompassEnabled(true);

				// Enable / Disable Rotate gesture
				googleMap.getUiSettings().setRotateGesturesEnabled(true);

				// Enable / Disable zooming functionality
				googleMap.getUiSettings().setZoomGesturesEnabled(true);

				/*double latitude = 17.385044;
				double longitude = 78.486671;*/
				
				/*Marker myLocMarker = googleMap.addMarker(new MarkerOptions()
	            .position(new LatLng(latitude, longitude))
	            .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.chat, "your text goes here"))));*/
				/*
				 Geocoder geocoder = new Geocoder(this, Locale.getDefault());
				 List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
				 String cityname=" Unknow location name";
				 String StateName="";
				 if (addresses!= null && addresses.size() > 0 ) {
					 cityname = addresses.get(0).getAddressLine(0);
					 StateName= addresses.get(0).getAddressLine(1);
					 System.out.println("cityname "+cityname);
				 }
				 System.out.println("complete location"+addresses);*/
				 
				 /*
				 StateName = addresses.get(0).getAddressLine(1);
				 CountryName = addresses.get(0).getAddressLine(2);*/
				// latitude and longitude
				 latitude = 17.385044;
				 longitude = 78.486671;
				Marker melbourne = googleMap.addMarker(new MarkerOptions()
				                          .position(new LatLng(latitude, longitude))
				                          .title("Amit Groch")
				                          .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
				                          .snippet("nodia"));
				melbourne.showInfoWindow();
				
				//MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps ");
				//marker.icon(BitmapDescriptorFactory
						//.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
				//marker.snippet("amit");
				// adding marker
				
				
				//googleMap.addMarker(marker);
				
				//markerinfo is an HTML formatted string
				
				CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(latitude,
						longitude)).zoom(15).build();

				googleMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
				

			} /*catch (Exception e) {
				e.printStackTrace();
			}

		}
*/
		@Override
		protected void onResume() {
			super.onResume();
			initilizeMap();
		}

		
		
		/**
		 * function to load map If map is not created it will create it for you
		 * */
		private void initilizeMap() {
			if (googleMap == null) {
				googleMap = ((MapFragment) getFragmentManager().findFragmentById(
						R.id.map)).getMap();

				// check if map is created successfully or not
				if (googleMap == null) {
					Toast.makeText(getApplicationContext(),
							"Sorry! unable to create maps", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
		
		@Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }
	 
	    boolean canAddItem = false;
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	 
	    	Intent map = new Intent(this,Home.class);
			startActivity(map);
	        return super.onOptionsItemSelected(item);
		
	    }	
}
