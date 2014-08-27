package com.anaadih.locationfinder.dto;

public class GroupsDataItem {
	
	private int groupId ;
	private String groupName ;
	
	public GroupsDataItem() {
		super();
	}
	
	public GroupsDataItem(int groupId, String groupName) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
	}
	
	public int getGroupId() {
		return groupId;
	}
	
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
}
