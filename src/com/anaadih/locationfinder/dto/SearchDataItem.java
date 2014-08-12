package com.anaadih.locationfinder.dto;

public class SearchDataItem {
	
	private String name;
	private int userId;
	private String image;
	
	public SearchDataItem() {
		super();
	}
	
	public SearchDataItem(int userId, String name, String image) {
		super();
		this.name = name;
		this.image = image;
		this.userId = userId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
