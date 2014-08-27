package com.anaadih.locationfinder.dto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	public static final String TAG="DATABASE" ;
	public static final String DATABASE_NAME = "LocationFinder" ;
	
	public static final String TB_NAME = "latestlocation" ;
	public static final String KEY_ID = "_id" ;
	public static final String KEY_FRIEND_ID = "friend_id" ;
	public static final String KEY_FRIEND_NAME = "friend_name" ;
	public static final String KEY_FRIEND_IMAGE_URL = "friend_img_url" ;
	public static final String KEY_LATITUDE = "latitude" ;
	public static final String KEY_LONGITUDE = "longitude" ;
	public static final String KEY_LOC_ADDRESS = "address" ;
	public static final String KEY_LOC_DATETIME = "datetime" ;
	
	public static final int DATABASE_VERSION = 1;
	public static final int NEW_VERSION = 2;
	public static final int READ_MODE = 1;
	public static final int WRITE_MODE = 2;
	
	SQLiteDatabase db;
	
	public static final String CREATE_LATEST_LOC_TABLE = "create table "+TB_NAME+"("+KEY_ID+" integer primary key autoincrement, "+KEY_FRIEND_ID+" integer,"
			+ " "+KEY_FRIEND_NAME+" text, "+KEY_FRIEND_IMAGE_URL+" text, "+KEY_LATITUDE+" real, "+KEY_LONGITUDE+" real, "+KEY_LOC_ADDRESS+" text, "+KEY_LOC_DATETIME+" text);";
	

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_LATEST_LOC_TABLE);
		Log.i(TAG, "Table is created with name : "+TB_NAME);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(TAG, "Upgrading database from version " + DATABASE_VERSION + " to "
				+ NEW_VERSION + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
		onCreate(db);
	}
	
	public void open (int mode) {
		if( mode == READ_MODE ) {
			db = getReadableDatabase();
		    Log.i(TAG, "Database opened in readable mode...");		    
		} else {
			db = getReadableDatabase();
			Log.i(TAG, "Database opened in writable mode...");			
		}		
	}
	
	public void close () {
		db.close();
	}
	
	public long addLatestLocation ( int friendId, String friendName, String friendImgUrl, 
			double latitude, double longitude, String address, String dateTime ) {
		
		db = this.getWritableDatabase();
		ContentValues v = new ContentValues();
		v.put(KEY_FRIEND_ID, friendId);
		v.put(KEY_FRIEND_NAME, friendName);
		v.put(KEY_FRIEND_IMAGE_URL, friendImgUrl);
		v.put(KEY_LATITUDE, latitude);
		v.put(KEY_LATITUDE, longitude);
		v.put(KEY_LOC_ADDRESS, address);
		v.put(KEY_LOC_DATETIME, dateTime);
		long row = db.insert(TB_NAME, null, v);
		Log.i(TAG, "One Location added successfully...");
		db.close();
		return row;
	}

	public Cursor getAllLatestLocation() {
		Cursor cursor = db.query( TB_NAME, null, null, null, null, null, null );
		return cursor;
	}
	
	/*public Cursor getPersonByAge(int age)
	{
		Cursor cursor=db.query(TB_NAME, null, "age=?", new String[] {""+age}, null, null, null);
		return cursor;
	}*/
	
	/*public Cursor getPersonByName(String name)
	{
		Cursor cursor=db.query(TB_NAME, null, "name=?", new String[] {""+name}, null, null, null);
		return cursor;
	}*/
	
	/*public Cursor getPersonByCountry(String country)
	{
		Cursor cursor=db.query(TB_NAME, null, "country=?", new String[] {""+country}, null, null, null);
		return cursor;
	}*/
	
	/*public int updatePerson(Person_DTO pdto)
	{
		int row=0;
		ContentValues values=new ContentValues();
		values.put("_id", pdto.getKEY_ID());
		values.put("name", pdto.getKEY_NAME());
		values.put("age", pdto.getKEY_AGE());
		values.put("country", pdto.getKEY_COUNTRY());
		row=db.update(TB_NAME, values, "_id=?", new String[] {""+pdto.getKEY_ID()});
		return row;
	}*/
	
	/*public int deleteRecord(int id)
	{
		int row=0;
		row=db.delete(TB_NAME, "_id=?", new String[] {""+id});
		return row;
	}*/
}
