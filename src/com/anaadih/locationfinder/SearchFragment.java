package com.anaadih.locationfinder;

import java.util.ArrayList;
import java.util.List;

import com.anaadih.locationfinder.adapter.SearchListAdapter;
import com.anaadih.locationfinder.dto.SearchDataItem;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchFragment extends Fragment {
	
	private ListView lvSearchAllList ;
	private ImageView ivSearchBtn ;
	private Spinner spnrSearch ;
	private SearchListAdapter adapter ;
	private List<SearchDataItem> rowItemsList ;
	private String[] name ;
	private int[] profImage ;
	
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
        
        initializer(rootView);
        
        rowItemsList = new ArrayList<SearchDataItem>() ;
		name = new String [] { "Amit Groch", "Joginder Sharma", "Pramod Kumar Varma", "Jitendra Kumar Yadav", "Pradeep Singh Gusian",
				"Anil Kumar Vishwakarma", "Pankaj Kumar Sharma", "Rajesh Kumar", "Ashok Kumar", "Pradeep Pandey" } ;

		profImage = new int[] { R.drawable.images, R.drawable.saab, R.drawable.bmw, R.drawable.images, R.drawable.saab, 
				R.drawable.bmw, R.drawable.images, R.drawable.saab, R.drawable.bmw, R.drawable.images } ;
        
        for (int i = 0 ; i < name .length ; i ++)
		{
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
		spnrSearch = (Spinner) rootView.findViewById(R.id.spnrSearch);
    }

}
