package com.anaadih.locationfinder.dto;

public class Person_DTO {
	public int KEY_ID;
	public String KEY_NAME;
	public int KEY_AGE;
	public String KEY_COUNTRY;
	
	
	
	public int getKEY_ID() {
		return KEY_ID;
	}



	public void setKEY_ID(int kEY_ID) {
		KEY_ID = kEY_ID;
	}



	public String getKEY_NAME() {
		return KEY_NAME;
	}



	public void setKEY_NAME(String kEY_NAME) {
		KEY_NAME = kEY_NAME;
	}



	public int getKEY_AGE() {
		return KEY_AGE;
	}



	public void setKEY_AGE(int kEY_AGE) {
		KEY_AGE = kEY_AGE;
	}



	public String getKEY_COUNTRY() {
		return KEY_COUNTRY;
	}



	public void setKEY_COUNTRY(String kEY_COUNTRY) {
		KEY_COUNTRY = kEY_COUNTRY;
	}

	
	public Person_DTO(int KEY_ID, String KEY_NAME, int KEY_AGE, String KEY_COUNTRY)
	{
		this.KEY_ID=KEY_ID;
		this.KEY_NAME=KEY_NAME;
		this.KEY_AGE=KEY_AGE;
		this.KEY_COUNTRY=KEY_COUNTRY;
	}


	public Person_DTO(String KEY_NAME, int KEY_AGE, String KEY_COUNTRY)
	{
		this.KEY_NAME=KEY_NAME;
		this.KEY_AGE=KEY_AGE;
		this.KEY_COUNTRY=KEY_COUNTRY;
	}
	
	public Person_DTO(int KEY_ID)
	{
		this.KEY_ID=KEY_ID;
	}
	
}
