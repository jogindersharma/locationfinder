package com.anaadih.locationfinder.adapter;

import java.util.List;

import com.anaadih.locationfinder.R;
import com.anaadih.locationfinder.dto.HomeDataItem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeListAdapter extends BaseAdapter {
	
	private Context context ;
	private List<HomeDataItem> rowItems ;

	public HomeListAdapter(Context context, List<HomeDataItem> rowItems) {
		super();
		this.context = context;
		this.rowItems = rowItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return rowItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return rowItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return rowItems.indexOf(getItem(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		HomeDataItem rowItem = rowItems.get(position);
	         
	    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	    if (convertView == null) {
	    	
	    	convertView = mInflater.inflate(R.layout.home_item, null);
	        holder = new ViewHolder();
	         
	        holder.tvHomeItemName = (TextView) convertView.findViewById(R.id.tvHomeItemName);
	        holder.tvHomeItemAddress = (TextView) convertView.findViewById(R.id.tvHomeItemAddress);
	        holder.tvHomeItemDateTime = (TextView) convertView.findViewById(R.id.tvHomeItemDateTime);
	        holder.ivHomeItemPic = (ImageView) convertView.findViewById(R.id.ivHomeItemPic);
	         
	        convertView.setTag(holder);        
	    } else
	    	holder = (ViewHolder) convertView.getTag();	                 
	        holder.tvHomeItemName.setText(rowItem.getName());
	        holder.tvHomeItemAddress.setText(rowItem.getAddress());
	        holder.tvHomeItemDateTime.setText(""+rowItem.getDateTime());
	        holder.ivHomeItemPic.setImageResource(rowItem.getImage());
	        
	        return convertView;
	}
	
	//private view holder class 
	private class ViewHolder {
		ImageView ivHomeItemPic;
		TextView tvHomeItemName, tvHomeItemAddress, tvHomeItemDateTime;	
	}
}
