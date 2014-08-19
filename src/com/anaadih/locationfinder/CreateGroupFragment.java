package com.anaadih.locationfinder;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateGroupFragment extends Fragment { 
	
	Button btnCreateGroupCancel, btnCreateGroupSubmit ;
	EditText etCreateGroupName ;
	String groupName ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.create_group, container, false);
        rootView.setClickable(true);
        
        btnCreateGroupCancel = (Button) rootView.findViewById(R.id.btnCreateGroupCancel);
        btnCreateGroupSubmit = (Button) rootView.findViewById(R.id.btnCreateGroupSubmit);
        etCreateGroupName = (EditText) rootView.findViewById(R.id.etCreateGroupName);
        
        btnCreateGroupSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				groupName = etCreateGroupName.getText().toString();
				if (groupName.length() > 0) {
					
				}
				else {
					Toast.makeText(getActivity(), "Group Name Should Not Null...", Toast.LENGTH_SHORT).show();
				}
			}
		});
        
        btnCreateGroupCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});       
		return rootView;
	}
}