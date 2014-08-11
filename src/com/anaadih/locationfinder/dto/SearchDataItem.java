package com.anaadih.locationfinder.dto;

public class SearchDataItem {
	
	private String name ;
	private int image ;
	
	public SearchDataItem() {
		super();
	}
	
	public SearchDataItem(String name, int image) {
		super();
		this.name = name;
		this.image = image;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getImage() {
		return image;
	}
	
	public void setImage(int image) {
		this.image = image;
	}
}
