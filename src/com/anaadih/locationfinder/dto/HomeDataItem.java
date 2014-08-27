package com.anaadih.locationfinder.dto;

public class HomeDataItem {
	
	private String name ;
	private String address ;
	private String dateTime ;
	private String imageUrl ;
	private String latitude;
	private String longitude;
	private String friendId;
	private String fName;
	private String lName;
	
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}
	
	public HomeDataItem() {
		super();
	}

	public HomeDataItem(String name, String address,
			String dateTime, String imageUrl,String latitude,String longitude,String friendId,String fName,String lName) {
		super();
		this.name = name;
		this.address = address;
		this.dateTime = dateTime;
		this.imageUrl = imageUrl;
		this.fName = fName;
		this.lName=lName;
		this.latitude=latitude;
		this.longitude=longitude;
		this.friendId=friendId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
    
}
