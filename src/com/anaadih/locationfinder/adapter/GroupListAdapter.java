package com.anaadih.locationfinder.adapter;

import java.util.List;

import com.anaadih.locationfinder.R;
import com.anaadih.locationfinder.dto.GroupsDataItem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupListAdapter extends BaseAdapter {
	
	private Context context ;
	private List<GroupsDataItem> rowItems ;
	
	private OnEditButtonClickedListener onEditListener ;
	private OnDeleteButtonClickedListener onDeleteListener ;
	
	public interface OnEditButtonClickedListener {
        public void OnEdit(String id, int groupId, String groupName);
    }
	
	public interface OnDeleteButtonClickedListener {
        public void OnDelete(String id, int groupId);
    }
	
	public GroupListAdapter(Context context, List<GroupsDataItem> rowItems) {
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
		final GroupsDataItem rowItem = rowItems.get(position);
	         
	    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	    if (convertView == null) { 	
	    	convertView = mInflater.inflate(R.layout.groups_item, null);
	        holder = new ViewHolder();
	    
	        holder.tvGroupsItemGroupName = (TextView) convertView.findViewById(R.id.tvGroupsItemGroupName);
	       /* holder.btnGroupsItemEdit = (Button) convertView.findViewById(R.id.btnGroupsItemEdit);
	        holder.btnGroupsItemDelete = (Button) convertView.findViewById(R.id.btnGroupsItemDelete);*/
	        holder.ivGroupsItemEdit = (ImageView) convertView.findViewById(R.id.ivGroupsItemEdit);
	        holder.ivGroupsItemDelete = (ImageView) convertView.findViewById(R.id.ivGroupsItemDelete);
	         
	        convertView.setTag(holder);        
	    } else {
	    	holder = (ViewHolder) convertView.getTag();	                 
	    }
	    
	    holder.tvGroupsItemGroupName.setText(rowItem.getGroupName());
	    
	    holder.ivGroupsItemEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onEditListener.OnEdit(""+v.getId(), rowItem.getGroupId(), rowItem.getGroupName() );
			
			}
		});
	    
	    holder.ivGroupsItemDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onDeleteListener.OnDelete(""+v.getId(), rowItem.getGroupId());
			}
		});
	    
	    return convertView;
	}
	
	//private view holder class 
	private class ViewHolder {
	    TextView tvGroupsItemGroupName ;	
		//Button btnGroupsItemEdit, btnGroupsItemDelete ;	
		ImageView ivGroupsItemEdit, ivGroupsItemDelete ;	
	}
	
	public void setEditButtonClickedListener(OnEditButtonClickedListener onEditListener) {
        this.onEditListener = onEditListener ;
    }
	
	public void setDeleteButtonClickedListener(OnDeleteButtonClickedListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener ;
    }
}