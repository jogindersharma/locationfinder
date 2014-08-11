package com.anaadih.locationfinder;

import java.util.ArrayList;
import java.util.List;

import com.anaadih.locationfinder.adapter.HomeListAdapter;
import com.anaadih.locationfinder.dto.HomeDataItem;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

public class HomeFragment extends Fragment {
	
	private ListView lvHomeAllList ;
	private Button btnHomeFilter ;
	private Spinner spnrHome ;
	private HomeListAdapter adapter ;
	private List<HomeDataItem> rowItemsList ;
	private String[] name, group, address, datetime ;
	private int[] profImage ;
	private FragmentManager fragmentManager ;
	
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
        
        fragmentManager = getFragmentManager();
        
        initializer(rootView);
        
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
}