package com.anaadih.locationfinder.dto;

public class GroupDto {

	int group_id;
	String group_name;
	public GroupDto() {
		super();
	}
	
	public GroupDto(int group_id, String group_name) {
		super();
		this.group_id = group_id;
		this.group_name = group_name;
	}
	
	public int getGroup_id() {
		return group_id;
	}
	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
}
