package com.anaadih.locationfinder.adapter;

import java.util.List;

import com.anaadih.locationfinder.CustomUtil;
import com.anaadih.locationfinder.R;
import com.anaadih.locationfinder.dto.FriendListDataItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class FriendListAdapter extends BaseAdapter {
	
	private Context context ;
	private List<FriendListDataItem> rowItems ;
	
	private OnViewButtonClickedListener listener;
	
	private ImageLoader imageLoader ;
	private DisplayImageOptions options ;
	
	public interface OnViewButtonClickedListener {
        public void OnAdd(String id);
    }

	public FriendListAdapter(Context context, List<FriendListDataItem> rowItems) {
		super();
		this.context = context;
		this.rowItems = rowItems;
		
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));

		options = new DisplayImageOptions.Builder()
		.displayer(new RoundedBitmapDisplayer((int) 27.5f))
		.showStubImage(R.drawable.ic_launcher) //this is the image that will be displayed if download fails
	    .cacheInMemory()
		.cacheOnDisc()
		.build();
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
		ViewHolder holder = null;
		FriendListDataItem rowItem = rowItems.get(position);
	         
	    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	    if (convertView == null) {
	    	
	    	convertView = mInflater.inflate(R.layout.friend_list_item, null);
	        holder = new ViewHolder();
	    
	        holder.tvFriendListItemName = (TextView) convertView.findViewById(R.id.tvFriendListItemName);
	        holder.tvFriendListCircleName = (TextView) convertView.findViewById(R.id.tvFriendListCircleName);
	        holder.ivFriendListPic = (ImageView) convertView.findViewById(R.id.ivFriendListPic);
	         
	        convertView.setTag(holder);        
	    } else {
	    	holder = (ViewHolder) convertView.getTag();	                 
	    }
	    	holder.friendId = rowItem.getUserId();
	    	holder.tvFriendListItemName.setText(rowItem.getName());
	    	holder.tvFriendListCircleName.setText(rowItem.getGroup());
	        //holder.ivFriendListPic.setImageResource(R.drawable.bmw);
	        imageLoader.displayImage(rowItem.getImage(),  holder.ivFriendListPic, options);
	        return convertView;
	}
	
	public void setOnViewButtonClickedListener(OnViewButtonClickedListener listener) {
        this.listener = listener ;
    }
	
	//private view holder class 
	private class ViewHolder {
		int friendId;
		TextView tvFriendListItemName ;	
		TextView tvFriendListCircleName ;	
		ImageView ivFriendListPic ;
	}
}
