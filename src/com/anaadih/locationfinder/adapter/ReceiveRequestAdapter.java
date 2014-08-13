package com.anaadih.locationfinder.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.anaadih.locationfinder.MyNetworkClass;
import com.anaadih.locationfinder.R;
import com.anaadih.locationfinder.dto.ReceiveRequestDto;

public class ReceiveRequestAdapter extends BaseAdapter {
	
	private Context context ;
	private List<ReceiveRequestDto> rowItems ;
	
	public interface OnViewButtonClickedListener {
        public void OnAdd(String id);
    }

	public ReceiveRequestAdapter(Context context, List<ReceiveRequestDto> rowItems) {
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
		ReceiveRequestDto rowItem = rowItems.get(position);
	         
	    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	    if (convertView == null) {
	    	
	    	convertView = mInflater.inflate(R.layout.receive_request_items, null);
	        holder = new ViewHolder();
	         
	        holder.tvreceiverequestName = (TextView) convertView.findViewById(R.id.tvreceiverequestName);
	        holder.ivreceiverequestPic = (ImageView) convertView.findViewById(R.id.ivreceiverequestPic);
	        holder.btnreceiverequestadd = (Button) convertView.findViewById(R.id.btnreceiverequestadd);
	        holder.btnreceiverequestblock = (Button) convertView.findViewById(R.id.btnreceiverequestblock);
	        holder.btnreceiverequestreject=(Button)convertView.findViewById(R.id.btnreceiverequestreject); 
	        convertView.setTag(holder);        
	    } else
	    	holder = (ViewHolder) convertView.getTag();	                 
	        
	    	holder.friendId = rowItem.getUserId();
	    	holder.tvreceiverequestName.setText(rowItem.getName());
	        holder.ivreceiverequestPic.setImageResource(R.drawable.bmw);
	        
	        final ViewHolder fianlHolder = holder;
	        
	        holder.btnreceiverequestadd.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MyNetworkClass.getInstance(context).requestResponse(fianlHolder.friendId, 3);
				}
			});
	        holder.btnreceiverequestreject.setOnClickListener(new OnClickListener() {
	        	@Override
	        	public void onClick(View v) {
	        		MyNetworkClass.getInstance(context).requestResponse(fianlHolder.friendId, 4);
	        	}
	        });
	        
	        holder.btnreceiverequestblock.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MyNetworkClass.getInstance(context).requestResponse(fianlHolder.friendId, 5);
				}
			});
	        
	        
	        return convertView;
	}
	
	//private view holder class 
	private class ViewHolder {
		ImageView ivreceiverequestPic ;
		TextView tvreceiverequestName ;	
		Button btnreceiverequestadd ;
		Button btnreceiverequestblock;
		Button btnreceiverequestreject;
		int friendId;
	}
}
