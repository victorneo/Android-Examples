package com.cameratest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class CameraTestActivity extends Activity {
	
	protected static final int CAMERA_PIC_REQUEST = 0;
	protected static final int USE_CAMERA_REQUEST = 1;
	
	Button buttonIntent = null;
	Button buttonCamera = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        buttonIntent = (Button) findViewById(R.id.LaunchIntent);
        buttonIntent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, CAMERA_PIC_REQUEST);
			}
		});
        
        buttonCamera = (Button) findViewById(R.id.buttonImageCapture);
        buttonCamera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),ImageCaptureActivity.class);
				startActivityForResult(intent,USE_CAMERA_REQUEST);
			}
		});
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode == CAMERA_PIC_REQUEST && data != null){
    		Object o = data.getExtras().get("data");
    		if(o != null){
    			Bitmap thumbnail = (Bitmap)o;
        		ImageView image = (ImageView) findViewById(R.id.photoGalleryImageView);
        		image.setImageBitmap(thumbnail);
    		}
    	}
    }
    
    
}