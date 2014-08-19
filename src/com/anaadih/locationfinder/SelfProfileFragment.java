package com.anaadih.locationfinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import com.anaadih.locationfinder.networking.NetworkStatus;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SelfProfileFragment extends Fragment implements OnClickListener {

	ImageView imUserProfile,ivUserProfileFNameEdit,ivUserProfileLNameEdit,ivUserProfileEmailEdit,ivUserProfileMobEdit;
	TextView tvFnameUser,tvLnameUser,tvEmailUser,tvPhoneUser,tvCodeUser;
	Button btnNewCode,btnUserProfileSave;
	String responseString;
	JSONObject jsonResult;
	Usergetdtailsinterface usergetDetailsObj;
	UserCodeGenerateinterface userCodeGenerateObj;
	ProfileUpdateInterface profileUpdateObj ;
	RestAdapter restAdapter;
	String TAG = "UserProfileFragment";
	Context context;
	
	Dialog profileUpdateDialog ;
	LinearLayout llProfileUpdateFNameCancel, llprofileUpdateFNameSave ;
	LinearLayout llProfileUpdateLNameCancel, llprofileUpdateLNameSave ;
	LinearLayout llProfileUpdateEmailCancel, llprofileUpdateEmailSave ;
	LinearLayout llProfileUpdateMobNoCancel, llprofileUpdateMobNoSave ;
	EditText etProfileUpdate ;
	TextView tvProfileUpdateHeader, tvProfileUpdateHint ;
	CheckBox chbxUserProfileCode, chbxUserProfileName, chbxUserProfileEmail, chbxUserProfileMobNo ;
	int searchOptionId;
	String fNameUpdatedValue, lNameUpdatedValue, emailUpdatedValue ;
	double phoneUpdatedValue ;
	
	private ImageLoader imageLoader ;
	private DisplayImageOptions options ;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 View rootView = inflater.inflate(R.layout.activity_user_profile, container, false);
		 context = container.getContext();
		 initization(rootView);		 
		 rootView.setClickable(true);
		 getProfile();
		 
		 //Profile Image Loader
		 	imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration.createDefault(context));

			options = new DisplayImageOptions.Builder()
			.displayer(new RoundedBitmapDisplayer((int) 27.5f))
			.showStubImage(R.drawable.ic_launcher) //this is the image that will be displayed if download fails
		    .cacheInMemory()
			.cacheOnDisc()
			.build();
		 
		return rootView;
	}
	
	public void initization(View rootview) {
		imUserProfile = (ImageView) rootview.findViewById(R.id.ivUserProfilePic);
		ivUserProfileFNameEdit= (ImageView) rootview.findViewById(R.id.ivUserProfileFNameEdit);
		ivUserProfileLNameEdit= (ImageView) rootview.findViewById(R.id.ivUserProfileLNameEdit);
		ivUserProfileEmailEdit= (ImageView) rootview.findViewById(R.id.ivUserProfileEmailEdit);
		ivUserProfileMobEdit= (ImageView) rootview.findViewById(R.id.ivUserProfileMobEdit);
		tvFnameUser = (TextView) rootview.findViewById(R.id.tvUserProfileFirstName);
		tvLnameUser = (TextView) rootview.findViewById(R.id.tvUserProfileLastName);
		tvEmailUser = (TextView) rootview.findViewById(R.id.tvUserProfileEmail);
		tvPhoneUser = (TextView) rootview.findViewById(R.id.tvUserProfileMob);
		tvCodeUser = (TextView) rootview.findViewById(R.id.tvUserProfileCode);
		btnNewCode = (Button) rootview.findViewById(R.id.btnUserProfileNewCode);
		
		btnUserProfileSave = (Button) rootview.findViewById(R.id.btnUserProfileSave);
		chbxUserProfileCode = (CheckBox) rootview.findViewById(R.id.chbxUserProfileCode);
		chbxUserProfileName = (CheckBox) rootview.findViewById(R.id.chbxUserProfileName);
		chbxUserProfileEmail = (CheckBox) rootview.findViewById(R.id.chbxUserProfileEmail);
		chbxUserProfileMobNo = (CheckBox) rootview.findViewById(R.id.chbxUserProfileMobNo);
		/*btnNewCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	newCodeGen();
            }
        });*/
		btnNewCode.setOnClickListener(this);
		ivUserProfileEmailEdit.setOnClickListener(this);
		ivUserProfileFNameEdit.setOnClickListener(this);
		ivUserProfileLNameEdit.setOnClickListener(this);
		ivUserProfileMobEdit.setOnClickListener(this);
		imUserProfile.setOnClickListener(this);
		btnUserProfileSave.setOnClickListener(this);
		
	}
	
	interface Usergetdtailsinterface{
		@FormUrlEncoded
		@POST(StaticStrings.GET_USER_DETAILS)
		void userSelfProfile(
				@Field("userId") Integer userEmailId, 
				Callback<Response> userProfileCallback);
	}
	
	public void getProfile(){
		
		SharedPreferences prefUserId =CustomUtil.getInstance(Home.context).getSharedPrefObj();
		int userId= prefUserId.getInt(StaticStrings.USER_ID, 0);
		restAdapter = new RestAdapter.Builder()
        .setEndpoint(StaticStrings.SITE_URL)
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .build();
		if(userId!=0){
		
		 if (NetworkStatus.getInstance(Home.context).isInternetAvailable(Home.context)) {
			 
			 	Log.e(TAG, "Internet is available");
	    		CustomUtil.getInstance(Home.context).showDialogBox("User Profile", "Loading your profile...");
	    		
	    		usergetDetailsObj = restAdapter.create(Usergetdtailsinterface.class);
	    		usergetDetailsObj.userSelfProfile(userId, userProfileCallback);
			 
		 }else{
				Log.e(TAG, "##########You are not online!!!!");
	    		NetworkStatus.getInstance(Home.context).showDefaultNoInternetDialog(Home.context);
		 }
		}else{
			// move to login screen
			Log.e(TAG, "goToLoginPage");
			Intent intent = new Intent(Home.context,Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Home.context.startActivity(intent);
			((Activity) Home.context).finish();
		}
		
	}
	
	Callback<Response> userProfileCallback = new Callback<Response>() {
  	  
  	  @Override
  	  public void failure(RetrofitError result) {
  		  Log.e("Retrofit Error ","Error in fetching Your Profile Data. Try Again ...");
  		  NetworkStatus.getInstance(context).showDefaultAlertDialog(context, "Error", "Error in fetching Your Profile Data. Try Again ...");
  		CustomUtil.getInstance(Home.context).hideDialogBox();
  	  }
  	  
  	  @Override
  	  public void success(Response result, Response response) {
  		  BufferedReader reader = null;
  	      StringBuilder sb = new StringBuilder();
  	      try {
  	          reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
  	          String line;
  	          try {
  	              while ((line = reader.readLine()) != null) {
  	                  sb.append(line);
  	              }
  	          } catch (IOException e) {
  	              e.printStackTrace();
  	          }
  	      } catch (IOException e) {
  	          e.printStackTrace();
  	      }
  	      responseString = sb.toString();
  	      if(CustomUtil.getInstance(Home.context).isJSONValid(responseString)) {
  	    	Log.e("SeverResponse=>", responseString);
	  	    	try {
					jsonResult = new JSONObject(responseString);
					String success = jsonResult.getString("success");
					if(success == null) {
						Log.e("SeverResponse=>", "success variable is null");
					} else if(success.equalsIgnoreCase("0")) {
						String message = jsonResult.getString("message");
						Toast.makeText(Home.context, message, Toast.LENGTH_LONG).show();
					} else if(success.equalsIgnoreCase("1")){
						JSONObject userInfoJsonObj= jsonResult.getJSONObject("userInfo");
						
						tvFnameUser.setText(userInfoJsonObj.getString("user_firstname").toString());
						tvLnameUser.setText(userInfoJsonObj.getString("user_lastname").toString());
						tvEmailUser.setText(userInfoJsonObj.getString("user_email_id").toString());
						tvPhoneUser.setText(userInfoJsonObj.getString("mobile_no").toString());
						tvCodeUser.setText(userInfoJsonObj.getString("user_code").toString());
						searchOptionId = userInfoJsonObj.getInt("search_option");
						updateSearchOption ();
  						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
  	      } else {
  	    	Log.e("SeverResponse=>", "is not a JSON =>"+responseString);
  	      }
  	    CustomUtil.getInstance(Home.context).hideDialogBox();
  	  }
  	};
  	
  	// Generate code function
  	
  	interface UserCodeGenerateinterface{
		@FormUrlEncoded
		@POST(StaticStrings.UPDATE_USER_DETAILS)
		void userCodeGenerate(
				@Field("userId") Integer userEmailId, 
				@Field("codeType") String usercode,
				Callback<Response> userCodeGenerateCallback);
	}
  	
  	private void newCodeGen() {
  		SharedPreferences prefUserId =CustomUtil.getInstance(Home.context).getSharedPrefObj();
		int userId= prefUserId.getInt(StaticStrings.USER_ID, 0);
		restAdapter = new RestAdapter.Builder()
        .setEndpoint(StaticStrings.SITE_URL)
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .build();
		if(userId!=0){
		
		 if (NetworkStatus.getInstance(Home.context).isInternetAvailable(Home.context)) {
			 
			 	Log.e(TAG, "Internet is available");
	    		CustomUtil.getInstance(Home.context).showDialogBox("New Code", "Wait for new code...");
	    		
	    		userCodeGenerateObj = restAdapter.create(UserCodeGenerateinterface.class);
	    		userCodeGenerateObj.userCodeGenerate(userId,"code", userCodeGenerateCallback);
			 
		 }else{
				Log.e(TAG, "##########You are not online!!!!");
	    		NetworkStatus.getInstance(Home.context).showDefaultNoInternetDialog(Home.context);
		 }
		}else{
			// move to login screen
			Log.e(TAG, "goToLoginPage");
			Intent intent = new Intent(Home.context,Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Home.context.startActivity(intent);
			((Activity) Home.context).finish();
		}
	}
  	
  	Callback<Response> userCodeGenerateCallback = new Callback<Response>() {
    	  
    	  @Override
    	  public void failure(RetrofitError result) {
    		  Log.e("Retrofit Error ",result.getMessage());
    		CustomUtil.getInstance(Home.context).hideDialogBox();
    	  }
    	  
    	  @Override
    	  public void success(Response result, Response response) {
    		  BufferedReader reader = null;
    	      StringBuilder sb = new StringBuilder();
    	      try {
    	          reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
    	          String line;
    	          try {
    	              while ((line = reader.readLine()) != null) {
    	                  sb.append(line);
    	              }
    	          } catch (IOException e) {
    	              e.printStackTrace();
    	          }
    	      } catch (IOException e) {
    	          e.printStackTrace();
    	      }
    	      responseString = sb.toString();
    	      if(CustomUtil.getInstance(Home.context).isJSONValid(responseString)) {
    	    	Log.e("SeverResponse=>", responseString);
  	  	    	try {
  					jsonResult = new JSONObject(responseString);
  					String success = jsonResult.getString("success");
  					if(success == null) {
  						Log.e("SeverResponse=>", "success variable is null");
  					} else if(success.equalsIgnoreCase("0")) {
  						String message = jsonResult.getString("message");
  						Toast.makeText(Home.context, message, Toast.LENGTH_LONG).show();
  					} else if(success.equalsIgnoreCase("1")){
  						Toast.makeText(Home.context, jsonResult.getString("message").toString(), Toast.LENGTH_LONG).show();
  						JSONObject userInfoJsonObj= jsonResult.getJSONObject("userInfo");
						
						tvCodeUser.setText(userInfoJsonObj.getString("user_code").toString());
  					}
  				} catch (JSONException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
    	      } else {
    	    	Log.e("SeverResponse=>", "is not a JSON =>"+responseString);
    	      }
    	    CustomUtil.getInstance(Home.context).hideDialogBox();
    	  }
    	};
    	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btnUserProfileNewCode:
				newCodeGen();	
				break;
			case R.id.ivUserProfilePic:
				Intent changePic = new Intent(Home.context,ProfileImageUpdater.class);
				startActivityForResult(changePic, 99);
				break;
			case R.id.ivUserProfileEmailEdit:
				//Toast.makeText(Home.context, "email", Toast.LENGTH_SHORT).show();
				showEmailDialog(getActivity(), "Email Update", "Enter Valid Email-Id");
				break;
			case R.id.ivUserProfileFNameEdit:
				//Toast.makeText(Home.context, "First name", Toast.LENGTH_SHORT).show();
				
				showFNameDialog(getActivity(), "First Name Update", "Enter First Name");
				break;
			case R.id.ivUserProfileLNameEdit:
				//Toast.makeText(Home.context, "Last name", Toast.LENGTH_SHORT).show();
				showLNameDialog(getActivity(), "Last Name Update", "Enter Last Name");
				
				break;
			case R.id.ivUserProfileMobEdit:
				//Toast.makeText(Home.context, "clcik", Toast.LENGTH_SHORT).show();
				showMobNoDialog(getActivity(), "Mobile No Update", "Enter Valid Mobile No");
				break;		
			case R.id.btnUserProfileSave:
				
				updateUserProfileOnServer();
				break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 99 && resultCode == getActivity().RESULT_OK && data != null) {
			 String profilePicUrl=data.getStringExtra("profilePicUrl");
			 imageLoader.displayImage(profilePicUrl, imUserProfile, options);
			 Log.e(TAG, "profilePicUrl =>"+profilePicUrl);
		}
	}
	
	private void updateSearchOption () {
		if (searchOptionId == 0) {
			chbxUserProfileName.setChecked(true);
			chbxUserProfileEmail.setChecked(true);
			chbxUserProfileMobNo.setChecked(true);
		} else if (searchOptionId == 1) {
			chbxUserProfileName.setChecked(true);
			chbxUserProfileEmail.setChecked(false);
			chbxUserProfileMobNo.setChecked(false);
		} else if (searchOptionId == 2) {
			chbxUserProfileName.setChecked(false);
			chbxUserProfileEmail.setChecked(true);
			chbxUserProfileMobNo.setChecked(false);
		} else if (searchOptionId == 3) {
			chbxUserProfileName.setChecked(false);
			chbxUserProfileEmail.setChecked(false);
			chbxUserProfileMobNo.setChecked(true);
		} else if (searchOptionId == 4) {
			chbxUserProfileName.setChecked(false);
			chbxUserProfileEmail.setChecked(false);
			chbxUserProfileMobNo.setChecked(false);
		} else if (searchOptionId == 5) {
			chbxUserProfileName.setChecked(true);
			chbxUserProfileEmail.setChecked(true);
			chbxUserProfileMobNo.setChecked(false);
		} else if (searchOptionId == 6) {
			chbxUserProfileName.setChecked(false);
			chbxUserProfileEmail.setChecked(true);
			chbxUserProfileMobNo.setChecked(true);
		} else if (searchOptionId == 7) {
			chbxUserProfileName.setChecked(true);
			chbxUserProfileEmail.setChecked(false);
			chbxUserProfileMobNo.setChecked(true);
		}
	}
	
	public int getSearchOption() {
		if (chbxUserProfileName.isChecked() && chbxUserProfileEmail.isChecked() 
				&& chbxUserProfileMobNo.isChecked()) {
			return 0 ;
		} else if (chbxUserProfileName.isChecked() && !chbxUserProfileEmail.isChecked() 
				&& !chbxUserProfileMobNo.isChecked()) {
			return 1 ;
		} else if (!chbxUserProfileName.isChecked() && chbxUserProfileEmail.isChecked() 
				&& !chbxUserProfileMobNo.isChecked()) {
			return 2 ;
		} else if (!chbxUserProfileName.isChecked() && !chbxUserProfileEmail.isChecked() 
				&& chbxUserProfileMobNo.isChecked()) {
			return 3 ;
		} else if (!chbxUserProfileName.isChecked() && !chbxUserProfileEmail.isChecked() 
				&& !chbxUserProfileMobNo.isChecked()) {
			return 4 ;
		} else if (chbxUserProfileName.isChecked() && chbxUserProfileEmail.isChecked() 
				&& !chbxUserProfileMobNo.isChecked()) {
			return 5 ;
		} else if (!chbxUserProfileName.isChecked() && chbxUserProfileEmail.isChecked() 
				&& chbxUserProfileMobNo.isChecked()) {
			return 6 ;
		} else if (chbxUserProfileName.isChecked() && !chbxUserProfileEmail.isChecked() 
				&& chbxUserProfileMobNo.isChecked()) {
			return 7 ;
		}
		return 9;
	}
	
	/*   =================Dialog Boxes   ==============*/
	
	protected void showFNameDialog(Context context, String header, String hint) {

    	profileUpdateDialog = new Dialog(context);
    	profileUpdateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	profileUpdateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    	profileUpdateDialog.setContentView(R.layout.profile_update_popup);
    	profileUpdateDialog.setCanceledOnTouchOutside(false);
 
    	llProfileUpdateFNameCancel = (LinearLayout) profileUpdateDialog.findViewById(R.id.llProfileUpdateCancel);
    	llprofileUpdateFNameSave = (LinearLayout) profileUpdateDialog.findViewById(R.id.llProfileUpdateSave);
    	etProfileUpdate = (EditText) profileUpdateDialog.findViewById(R.id.etProfileUpdateInput);
    	tvProfileUpdateHeader = (TextView) profileUpdateDialog.findViewById(R.id.tvProfileUpdateHeader);
    	tvProfileUpdateHint = (TextView) profileUpdateDialog.findViewById(R.id.tvProfileUpdateHint);
    	
    	tvProfileUpdateHeader.setText(header);
    	tvProfileUpdateHint.setText(hint);
 		
 		llProfileUpdateFNameCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				profileUpdateDialog.dismiss(); 
			}
		});
 		
 		llprofileUpdateFNameSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tvFnameUser.setText(etProfileUpdate.getText().toString());
				profileUpdateDialog.dismiss(); 
			}
		});

 		profileUpdateDialog.show();
 	}
	
	protected void showLNameDialog(Context context, String header, String hint) {

    	profileUpdateDialog = new Dialog(context);
    	profileUpdateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	profileUpdateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    	profileUpdateDialog.setContentView(R.layout.profile_update_popup);
    	profileUpdateDialog.setCanceledOnTouchOutside(false);
 
    	llProfileUpdateLNameCancel = (LinearLayout) profileUpdateDialog.findViewById(R.id.llProfileUpdateCancel);
    	llprofileUpdateLNameSave = (LinearLayout) profileUpdateDialog.findViewById(R.id.llProfileUpdateSave);
    	etProfileUpdate = (EditText) profileUpdateDialog.findViewById(R.id.etProfileUpdateInput);
    	tvProfileUpdateHeader = (TextView) profileUpdateDialog.findViewById(R.id.tvProfileUpdateHeader);
    	tvProfileUpdateHint = (TextView) profileUpdateDialog.findViewById(R.id.tvProfileUpdateHint);
    	
    	tvProfileUpdateHeader.setText(header);
    	tvProfileUpdateHint.setText(hint);
 		
        llProfileUpdateLNameCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				profileUpdateDialog.dismiss(); 
			}
		});
 		
 		llprofileUpdateLNameSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tvLnameUser.setText(etProfileUpdate.getText().toString());
				profileUpdateDialog.dismiss();
			}
		});

 		profileUpdateDialog.show();
 	}
	
	protected void showEmailDialog(Context context, String header, String hint) {

    	profileUpdateDialog = new Dialog(context);
    	profileUpdateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	profileUpdateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    	profileUpdateDialog.setContentView(R.layout.profile_update_popup);
    	profileUpdateDialog.setCanceledOnTouchOutside(false);
 
    	llProfileUpdateEmailCancel = (LinearLayout) profileUpdateDialog.findViewById(R.id.llProfileUpdateCancel);
    	llprofileUpdateEmailSave = (LinearLayout) profileUpdateDialog.findViewById(R.id.llProfileUpdateSave);
    	etProfileUpdate = (EditText) profileUpdateDialog.findViewById(R.id.etProfileUpdateInput);
    	tvProfileUpdateHeader = (TextView) profileUpdateDialog.findViewById(R.id.tvProfileUpdateHeader);
    	tvProfileUpdateHint = (TextView) profileUpdateDialog.findViewById(R.id.tvProfileUpdateHint);
    	
    	tvProfileUpdateHeader.setText(header);
    	tvProfileUpdateHint.setText(hint);
 		
    	llProfileUpdateEmailCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				profileUpdateDialog.dismiss(); 
			}
		});
 		
    	llprofileUpdateEmailSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tvEmailUser.setText(etProfileUpdate.getText().toString());
				profileUpdateDialog.dismiss();
			}
		});

 		profileUpdateDialog.show();
 	}
	
	protected void showMobNoDialog(Context context, String header, String hint) {

    	profileUpdateDialog = new Dialog(context);
    	profileUpdateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	profileUpdateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    	profileUpdateDialog.setContentView(R.layout.profile_update_popup);
    	profileUpdateDialog.setCanceledOnTouchOutside(false);
 
    	llProfileUpdateMobNoCancel = (LinearLayout) profileUpdateDialog.findViewById(R.id.llProfileUpdateCancel);
    	llprofileUpdateMobNoSave = (LinearLayout) profileUpdateDialog.findViewById(R.id.llProfileUpdateSave);
    	etProfileUpdate = (EditText) profileUpdateDialog.findViewById(R.id.etProfileUpdateInput);
    	tvProfileUpdateHeader = (TextView) profileUpdateDialog.findViewById(R.id.tvProfileUpdateHeader);
    	tvProfileUpdateHint = (TextView) profileUpdateDialog.findViewById(R.id.tvProfileUpdateHint);
    	
    	tvProfileUpdateHeader.setText(header);
    	tvProfileUpdateHint.setText(hint);
 		
        llProfileUpdateMobNoCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				profileUpdateDialog.dismiss(); 
			}
		});
 		
        llprofileUpdateMobNoSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tvPhoneUser.setText(etProfileUpdate.getText().toString());
				profileUpdateDialog.dismiss();
			}
		});

 		profileUpdateDialog.show();
 	}
	
	// Update User Profile
  	
  	interface ProfileUpdateInterface{
		@FormUrlEncoded
		@POST(StaticStrings.UPDATE_USER_DETAILS)
		void userProfileUpdate(
				@Field("userId") Integer userId, 
				@Field("optionId") Integer optionId,
				@Field("userFName") String userFName, 
				@Field("userLName") String userLName, 
				@Field("userEmail") String userEmail, 
				@Field("userMobNo") Double userMobNo,
				Callback<Response> profileUpdateCallback);
	}
  	
  	private void updateUserProfileOnServer() {
		// TODO Auto-generated method stub
  		SharedPreferences prefUserId =CustomUtil.getInstance(Home.context).getSharedPrefObj();
		int userId= prefUserId.getInt(StaticStrings.USER_ID, 0);
		
		restAdapter = new RestAdapter.Builder()
        .setEndpoint(StaticStrings.SITE_URL)
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .build();
		
		if(userId!=0){
		
		 if (NetworkStatus.getInstance(Home.context).isInternetAvailable(Home.context)) {
			 
			 	Log.e(TAG, "Internet is available");
	    		CustomUtil.getInstance(Home.context).showDialogBox("Prifile Update", "Profile Updating...");
	    		
	    		searchOptionId =  getSearchOption();
	    		fNameUpdatedValue = tvFnameUser.getText().toString();
	    		lNameUpdatedValue = tvLnameUser.getText().toString();
	    		emailUpdatedValue = tvEmailUser.getText().toString();
	    		phoneUpdatedValue = Double.parseDouble(tvPhoneUser.getText().toString());
	    		
	    		profileUpdateObj = restAdapter.create(ProfileUpdateInterface.class);
	    		profileUpdateObj.userProfileUpdate(userId, searchOptionId, fNameUpdatedValue, lNameUpdatedValue, 
	    				emailUpdatedValue, phoneUpdatedValue, profileUpdateCallback);
			 
		 }else{
				Log.e(TAG, "##########You are not online!!!!");
	    		NetworkStatus.getInstance(Home.context).showDefaultNoInternetDialog(Home.context);
		 }
		}else{
			// move to login screen
			Log.e(TAG, "goToLoginPage");
			Intent intent = new Intent(Home.context, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Home.context.startActivity(intent);
			((Activity) Home.context).finish();
		}
	}
  	
  	Callback<Response> profileUpdateCallback = new Callback<Response>() {
    	  
    	  @Override
    	  public void failure(RetrofitError result) {
    		  Log.e("Retrofit Error ","Error Updating User Profile");
    		CustomUtil.getInstance(Home.context).hideDialogBox();
    	  }
    	  
    	  @Override
    	  public void success(Response result, Response response) {
    		  BufferedReader reader = null;
    	      StringBuilder sb = new StringBuilder();
    	      try {
    	          reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
    	          String line;
    	          try {
    	              while ((line = reader.readLine()) != null) {
    	                  sb.append(line);
    	              }
    	          } catch (IOException e) {
    	              e.printStackTrace();
    	          }
    	      } catch (IOException e) {
    	          e.printStackTrace();
    	      }
    	      responseString = sb.toString();
    	      if(CustomUtil.getInstance(Home.context).isJSONValid(responseString)) {
    	    	Log.e("SeverResponse=>", responseString);
  	  	    	try {
  					jsonResult = new JSONObject(responseString);
  					String success = jsonResult.getString("success");
  					if(success == null) {
  						Log.e("SeverResponse=>", "success variable is null");
  					} else if(success.equalsIgnoreCase("0")) {
  						String message = jsonResult.getString("message");
  						Toast.makeText(Home.context, message, Toast.LENGTH_LONG).show();
  					} else if(success.equalsIgnoreCase("1")){
  						Toast.makeText(Home.context, jsonResult.getString("message").toString(), Toast.LENGTH_LONG).show();
  					//	JSONObject userInfoJsonObj= jsonResult.getJSONObject("userInfo");
  					}
  				} catch (JSONException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
    	      } else {
    	    	Log.e("SeverResponse=>", "is not a JSON =>"+responseString);
    	      }
    	    CustomUtil.getInstance(Home.context).hideDialogBox();
    	  }
    	};
}