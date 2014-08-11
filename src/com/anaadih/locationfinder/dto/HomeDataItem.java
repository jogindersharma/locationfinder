package com.anaadih.locationfinder.dto;

public class HomeDataItem {
	
	private String name ;
	private String group ;
	private String address ;
	private String dateTime ;
	private int image ;
	
	public HomeDataItem() {
		super();
	}
	
	public HomeDataItem(String name, String group, String address,
			String dateTime, int image) {
		super();
		this.name = name;
		this.group = group;
		this.address = address;
		this.dateTime = dateTime;
		this.image = image;
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
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getDateTime() {
		return dateTime;
	}
	
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	public int getImage() {
		return image;
	}
	
	public void setImage(int image) {
		this.image = image;
	}
}
