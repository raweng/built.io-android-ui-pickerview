package com.buitio.builtfilepicker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.raweng.built.userInterface.BuiltUIPickerController;
import com.raweng.built.userInterface.BuiltUIPickerController.PickerResultCode;

/**
 * This is built.io android tutorial.
 * 
 * Short introduction of some classes with some methods.
 * Contain classes: 1.BuiltUIPickerController 
 * 
 * For quick start with built.io refer "http://docs.built.io/quickstart/index.html#android"
 * 
 * @author raw engineering, Inc
 *
 */
public class PickerActivity extends Activity {

	/*
	 * Declaring BuiltUIPickerController object for fetching files from the device.
	 * 
	 * Note: 
	 * 1. Before use of this class UIAndroidExplorerScreen need to add in manifest
	 * <activity android:name="com.raweng.built.userInterface.UIAndroidExplorerScreen"></activity>
	 * 
	 * 2. Allows an application to write to external memory. "http://developer.android.com/reference/android/Manifest.permission.html#WRITE_EXTERNAL_STORAGE"
	 * 
	 */
	BuiltUIPickerController pickerObject;

	ImageView imageView;
	VideoView videoView;
	EditText  pathEditText;
	EditText  sizeEditText;
	EditText  fileNameEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picker_layout);

		imageView          = (ImageView)findViewById(R.id.imageView);
		videoView          = (VideoView)findViewById(R.id.videoView);
		pathEditText       = (EditText)findViewById(R.id.pathEditText);
		sizeEditText       = (EditText)findViewById(R.id.sizeEditText);
		fileNameEditText   = (EditText)findViewById(R.id.filenameEditText);

		/*
		 * Initializing the BuiltUIPickerController object.
		 */
		pickerObject = new BuiltUIPickerController(PickerActivity.this);

		((Button)findViewById(R.id.pickerButton)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				/*
				 * Displays the file selection dialogue.
				 * File and file path can get in onActivityResult method.
				 */
				try {
					pickerObject.showPicker(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		/*
		 * Checking requestCode and data separately for different options.
		 * 1. file system 
		 * 2. gallery
		 * 3. capture image
		 * 4. capture video
		 * 
		 * This becomes easier using PickerResultCode which is static enum of BuiltUIPickerController class.
		 * 
		 */
		if(requestCode == PickerResultCode.SELECT_FROM_FILE_SYSTEM_REQUEST_CODE.getValue() && data != null){


			if(resultCode == RESULT_OK){
				/*
				 * Receiving file path using a BuiltUIPickerController object to upload from file system.
				 */
				String filePath = (String) pickerObject.getFileInfoForMediaFile(data ,requestCode).get("filePath");
				String fileName = (String) pickerObject.getFileInfoForMediaFile(data ,requestCode).get("fileName");
				String size     = Long.toString((Long) pickerObject.getFileInfoForMediaFile(data ,requestCode).get("fileSize"));

				setMediaFiles(filePath, fileName, size);

				if(!TextUtils.isEmpty(fileName)){
					String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

					if(extension.equalsIgnoreCase("3gp") || extension.equalsIgnoreCase("mp4") || extension.equalsIgnoreCase("avi")){

						imageView.setVisibility(View.GONE);
						videoView.setVisibility(View.VISIBLE);

						setVideo(filePath);

					}else if(extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("gif")){

						imageView.setVisibility(View.VISIBLE);
						videoView.setVisibility(View.GONE);

						Uri uri = Uri.parse(filePath);
						imageView.setImageURI(uri);
					}
				}
			}else if(resultCode == RESULT_CANCELED){
				Log.i("BuiltUIPickerController", "Nothing selected");
			}


		}else if(requestCode == PickerResultCode.SELECT_IMAGE_FROM_GALLERY_REQUEST_CODE.getValue() && data != null){

			if(resultCode == RESULT_OK){
				/*
				 * Receiving file path using a BuiltUIPickerController object to upload from gallery.
				 */
				String filePath = (String) pickerObject.getFileInfoForMediaFile(data ,requestCode).get("filePath");
				String fileName = (String) pickerObject.getFileInfoForMediaFile(data ,requestCode).get("fileName");
				String size     = Long.toString((Long) pickerObject.getFileInfoForMediaFile(data ,requestCode).get("fileSize"));

				setMediaFiles(filePath, fileName, size);

				imageView.setVisibility(View.VISIBLE);
				videoView.setVisibility(View.GONE);

				if(!TextUtils.isEmpty(filePath)){
					Uri uri = Uri.parse(filePath);
					imageView.setImageURI(uri);
				}

			}else if(resultCode == RESULT_CANCELED){
				Log.i("BuiltUIPickerController", "Nothing selected");
			}


		}else if(requestCode == PickerResultCode.CAPTURE_IMAGE_REQUEST_CODE.getValue()){

			if(resultCode == RESULT_OK){
				/*
				 * Receiving file path using a BuiltUIPickerController object to upload from camera.
				 */
				String filePath = (String) pickerObject.getFileInfoForMediaFile(data ,requestCode).get("filePath");
				String fileName = (String) pickerObject.getFileInfoForMediaFile(data ,requestCode).get("fileName");
				String size     = Long.toString((Long) pickerObject.getFileInfoForMediaFile(data ,requestCode).get("fileSize"));

				setMediaFiles(filePath, fileName, size);

				imageView.setVisibility(View.VISIBLE);
				videoView.setVisibility(View.GONE);

				Uri uri = Uri.parse(filePath);
				imageView.setImageURI(uri);

			}else if(resultCode == RESULT_CANCELED){
				Log.i("BuiltUIPickerController", "Nothing selected");
			}


		}else if(requestCode == PickerResultCode.CAPTURE_VIDEO_REQUEST_CODE.getValue() && data != null){

			if(resultCode == RESULT_OK){

				/*
				 * Receiving file path using a BuiltUIPickerController object to upload from recording video.
				 */
				String filePath = (String) pickerObject.getFileInfoForMediaFile(data ,requestCode).get("filePath");
				String fileName = (String) pickerObject.getFileInfoForMediaFile(data ,requestCode).get("fileName");
				String size     = Long.toString((Long) pickerObject.getFileInfoForMediaFile(data ,requestCode).get("fileSize"));

				setMediaFiles(filePath, fileName, size);

				imageView.setVisibility(View.GONE);
				videoView.setVisibility(View.VISIBLE);

				setVideo(filePath);

			}else if(resultCode == RESULT_CANCELED){
				Log.i("BuiltUIPickerController", "Nothing selected");
			}
		}

	}

	private void setMediaFiles(String filePath, String fileName, String size) {

		if(!TextUtils.isEmpty(filePath)){
			fileNameEditText.setText(fileName);
		}

		if(!TextUtils.isEmpty(fileName)){
			pathEditText.setText(filePath);
		}

		if(!TextUtils.isEmpty(size)){
			sizeEditText.setText(size);
		}

	}

	public void setVideo(String filePath) {
		MediaController media_Controller = new MediaController(this);
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int height = dm.heightPixels;
		int width = dm.widthPixels;
		videoView.setMinimumWidth(width);
		videoView.setMinimumHeight(height);
		videoView.setMediaController(media_Controller);
		videoView.setVideoPath(filePath);
		videoView.start();
	}



}
