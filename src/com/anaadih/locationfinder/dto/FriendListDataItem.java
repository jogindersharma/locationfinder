package com.anaadih.locationfinder.dto;

public class FriendListDataItem {
	
	private int userId ;
	private String name ;
	private String group ;
	private String image ;
	
	public FriendListDataItem() {
		super();
	}
	
	public FriendListDataItem(int userId, String name, String group, String image) {
		super();
		this.userId = userId;
		this.name = name;
		this.group = group;
		this.image = image;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
