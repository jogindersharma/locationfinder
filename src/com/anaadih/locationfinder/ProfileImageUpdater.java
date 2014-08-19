package com.anaadih.locationfinder;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.anaadih.locationfinder.ReceiveRequest.ReceiveRequestInterface;
import com.anaadih.locationfinder.networking.NetworkStatus;
import com.edmodo.cropper.CropImageView;

public class ProfileImageUpdater extends Activity {

    // Static final constants
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
    private static final int ROTATE_NINETY_DEGREES = 90;
    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
    private static final int ON_TOUCH = 1;
    String TAG ="ProfileImageUpdater";
    Context context;
    boolean isImageSelected = false;
    boolean isImageCroped = false;

    // Instance variables
    private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
    private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;

    Bitmap croppedImage;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_LOAD_CAMERA = 2002;
    CropImageView cropImageView;

    // Saves the state upon rotating the screen/restarting the activity
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
        bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
    }

    // Restores the state upon rotating the screen/restarting the activity
    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
        mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.profile_image_update);
        context = this;
        // Sets fonts for all
        /*Typeface mFont = Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf");
        ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
        setFont(root, mFont);*/

        // Initialize components of the app
     //   cropImageView = (CropImageView) findViewById(R.id.CropImageView);
        cropImageView = (CropImageView) findViewById(R.id.ivProfileImageUpdate);
        /*final SeekBar aspectRatioXSeek = (SeekBar) findViewById(R.id.aspectRatioXSeek);
        final SeekBar aspectRatioYSeek = (SeekBar) findViewById(R.id.aspectRatioYSeek);
        final ToggleButton fixedAspectRatioToggle = (ToggleButton) findViewById(R.id.fixedAspectRatioToggle);*/
        //Spinner showGuidelinesSpin = (Spinner) findViewById(R.id.showGuidelinesSpin);
        
        // Sets sliders to be disabled until fixedAspectRatio is set
        /*aspectRatioXSeek.setEnabled(false);
        aspectRatioYSeek.setEnabled(false);*/

        // Set initial spinner value
        //showGuidelinesSpin.setSelection(ON_TOUCH);
        
        //Sets the rotate button
       // final Button rotateButton = (Button) findViewById(R.id.Button_rotate);
        final Button rotateButton = (Button) findViewById(R.id.btnProfileImageUpdateRotate);
        rotateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(ROTATE_NINETY_DEGREES);
            }
        });
        
        /*// Sets fixedAspectRatio
        fixedAspectRatioToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cropImageView.setFixedAspectRatio(isChecked);
                if (isChecked) {
                    aspectRatioXSeek.setEnabled(true);
                    aspectRatioYSeek.setEnabled(true);
                }
                else {
                    aspectRatioXSeek.setEnabled(false);
                    aspectRatioYSeek.setEnabled(false);
                }
            }
        });*/
        
        // Sets initial aspect ratio to 10/10, for demonstration purposes
        cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);

        // Sets aspectRatioX
        /*final TextView aspectRatioX = (TextView) findViewById(R.id.aspectRatioX);

        aspectRatioXSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar aspectRatioXSeek, int progress, boolean fromUser) {
                try {
                    mAspectRatioX = progress;
                    cropImageView.setAspectRatio(progress, mAspectRatioY);
                    aspectRatioX.setText(" " + progress);
                } catch (IllegalArgumentException e) {
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });*/

        // Sets aspectRatioY
       /* final TextView aspectRatioY = (TextView) findViewById(R.id.aspectRatioY);

        aspectRatioYSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar aspectRatioYSeek, int progress, boolean fromUser) {
                try {
                    mAspectRatioY = progress;
                    cropImageView.setAspectRatio(mAspectRatioX, progress);
                    aspectRatioY.setText(" " + progress);
                } catch (IllegalArgumentException e) {
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });*/
		
        // Sets up the Spinner
        /*showGuidelinesSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cropImageView.setGuidelines(i);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
		*/
        //final Button cropButton = (Button) findViewById(R.id.Button_crop);
        final Button cropButton = (Button) findViewById(R.id.btnProfileImageUpdatePreview);
        cropButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	if(cropSelectedeImage()) {
            		ImageView croppedImageView = (ImageView) findViewById(R.id.ivProfileImageUpdatePreview);
            		croppedImageView.setImageBitmap(croppedImage);
            	}
            }
        });
        
        final Button saveProfilePicBtn = (Button) findViewById(R.id.btnProfileImageUpdateSetProfile);
        saveProfilePicBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	if(isImageCroped) {
            		updateUserPicOnServerTask();
            	} else {
            		NetworkStatus.getInstance(context).showDefaultAlertDialog(context, "Alert", "Please Select Image and Click on Preview Button.");
            	}
            }
        });
        
        
        //Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
        Button buttonLoadImage = (Button) findViewById(R.id.btnProfileImageUpdateGallery);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
        
        //Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
        Button buttonLoadCamera = (Button) findViewById(R.id.btnProfileImageUpdateCamera);
        buttonLoadCamera.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                /*File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));*/
                startActivityForResult(intent, RESULT_LOAD_CAMERA);
				/*Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_CAMERA);*/
			}
		});
    }

    public boolean cropSelectedeImage() {
    	if(isImageSelected) {
    		croppedImage = cropImageView.getCroppedImage();
    		isImageCroped = true;
    		return true;    		
    	} else {
    		NetworkStatus.getInstance(context).showDefaultAlertDialog(context, "Alert", "Please Select the Image");
    		return false;
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			
			decodeFile(picturePath);
			
		}
		
		if (requestCode == RESULT_LOAD_CAMERA && resultCode == RESULT_OK && data != null) {
			Bitmap photo = (Bitmap) data.getExtras().get("data"); 
			cropImageView.setImageBitmap(photo);
			isImageSelected =true;
		}
    }
    
    public void decodeFile(String filePath) {
   	 
		 // Decode image size
		 BitmapFactory.Options o = new BitmapFactory.Options();
		 o.inJustDecodeBounds = true;
		        BitmapFactory.decodeFile(filePath, o);
		        
		 // The new size we want to scale to
		 final int REQUIRED_SIZE = 1024;
		
		 // Find the correct scale value. It should be the power of 2.
		 int width_tmp = o.outWidth, height_tmp = o.outHeight;
		 int scale = 1;
		 while (true) {
		  if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
		          break;
		  width_tmp /= 2;
		  height_tmp /= 2;
		  scale *= 2;
		 }
		
		 // Decode with inSampleSize
		 BitmapFactory.Options o2 = new BitmapFactory.Options();
		 o2.inSampleSize = scale;
		
		 cropImageView.setImageBitmap(BitmapFactory.decodeFile(filePath, o2));
		 isImageSelected =true;
   }
    
    /* ===================  Update user Image on Server =====================*/
    
    Response responseObj;
	String responseString;
	JSONObject jsonResult;
	RestAdapter restAdapter;
	UpdateUserImageInterface updateUserImageObj;
	
	interface UpdateUserImageInterface {
		@FormUrlEncoded
		@POST(StaticStrings.UPDATE_USER_PIC)
		void updateUserPic(
				@Field("userId") int userId,
				@Field("userPicBase64") String userPicBase64,
				Callback<Response> receiveRequestCallback);
	}
	
	public void updateUserPicOnServerTask() {
    	
        restAdapter = new RestAdapter.Builder()
        .setEndpoint(StaticStrings.SITE_URL)
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .build();
        
        if (NetworkStatus.getInstance(context).isInternetAvailable(context)) {
    		Log.e(TAG, "Internet is available");
    		
    		CustomUtil.getInstance(context).showDialogBox("Updating Profile Image", "Updating Your Profile Image on Server...");
    		int userId = CustomUtil.getInstance(context).getUserId();
    		String userPicBase64 = CustomUtil.getInstance(context).BitMapToString(croppedImage);

    		updateUserImageObj = restAdapter.create(UpdateUserImageInterface.class);
    		updateUserImageObj.updateUserPic(userId, userPicBase64, receiveRequestCallback);
    	} else {
    		Log.e(TAG, "##########You are not online!!!!");
    		NetworkStatus.getInstance(context).showDefaultNoInternetDialog(context);
    	}
    }
    
    Callback<Response> receiveRequestCallback = new Callback<Response>() {
    	  
    	  @Override
    	  public void failure(RetrofitError result) {
    		
    		//Log.e("Retrofit Error ",result.getMessage());
    		Log.e("Retrofit Error ","Error in updating User Profile Pic on Server");
    		NetworkStatus.getInstance(context).showDefaultAlertDialog(context, "Error", "Error updating your Profile Pic. Please use low memory image.");
    		CustomUtil.getInstance(context).hideDialogBox();
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
    	      if(CustomUtil.getInstance(context).isJSONValid(responseString)) {
    	    	Log.e("SeverResponse=>", responseString);
  	  	    	try {
  					jsonResult = new JSONObject(responseString);
  					String success = jsonResult.getString("success");
  					if(success == null) {
  						Log.e("SeverResponse=>", "success variable is null");
  					} else if(success.equalsIgnoreCase("0")) {
  						String message = jsonResult.getString("message");
  						Toast.makeText(context, message, Toast.LENGTH_LONG).show();
  					} else if(success.equalsIgnoreCase("1")) {
  						goToUserProfile();
  					}
  				} catch (JSONException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
    	      } else {
    	    	Log.e("SeverResponse=>", "is not a JSON =>"+responseString);
    	      }
    	    CustomUtil.getInstance(context).hideDialogBox();
    	  }
    };
    
    public void goToUserProfile() {
    	
    }
}
