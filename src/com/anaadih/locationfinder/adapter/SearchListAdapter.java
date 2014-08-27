package com.anaadih.locationfinder.adapter;

import java.util.List;

import com.anaadih.locationfinder.CustomUtil;
import com.anaadih.locationfinder.R;
import com.anaadih.locationfinder.dto.SearchDataItem;
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

public class SearchListAdapter extends BaseAdapter {
	
	private Context context ;
	private List<SearchDataItem> rowItems ;
	
	private OnViewButtonClickedListener listener;
	
	private ImageLoader imageLoader ;
	private DisplayImageOptions options ;
	
	public interface OnViewButtonClickedListener {
        public void OnAdd(String id);
    }

	public SearchListAdapter(Context context, List<SearchDataItem> rowItems) {
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
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		SearchDataItem rowItem = rowItems.get(position);
	         
	    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	    if (convertView == null) {
	    	
	    	convertView = mInflater.inflate(R.layout.search_item, null);
	        holder = new ViewHolder();
	    
	        holder.tvSearchItemName = (TextView) convertView.findViewById(R.id.tvSearchItemName);
	        holder.ivSearchItemPic = (ImageView) convertView.findViewById(R.id.ivSearchItemPic);
	        holder.btnSearchItemAdd = (Button) convertView.findViewById(R.id.btnSearchItemAdd);
	      //  holder.tgbtnSearchItemBlock = (ToggleButton) convertView.findViewById(R.id.tgbtnSearchItemBlock);
	         
	        convertView.setTag(holder);        
	    } else {
	    	holder = (ViewHolder) convertView.getTag();	                 
	    }
	    	holder.friendId = rowItem.getUserId();
	    	holder.tvSearchItemName.setText(rowItem.getName());
	        //holder.ivSearchItemPic.setImageResource(R.drawable.bmw);
	        imageLoader.displayImage(rowItem.getImage(), holder.ivSearchItemPic, options);
	        
	        final ViewHolder fianlHolder = holder;
	        
	        holder.btnSearchItemAdd.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//listener.OnAdd(""+v.getId());
					Log.e("inSideAdapter", "Add Btn");
					int userId = CustomUtil.getInstance(context).getUserId();
					CustomUtil.getInstance(context).sendFriendRequest(userId,fianlHolder.friendId);
				}
			});
	        
	        /*holder.tgbtnSearchItemBlock.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.e("inSideAdapter", "Block Btn");
				}
			});*/
	        
	        return convertView;
	}
	
	public void setOnViewButtonClickedListener(OnViewButtonClickedListener listener) {
        this.listener = listener ;
    }
	
	//private view holder class 
	private class ViewHolder {
		ImageView ivSearchItemPic ;
		TextView tvSearchItemName ;	
		Button btnSearchItemAdd ;
		//ToggleButton tgbtnSearchItemBlock ;
		int friendId;
	}
}
