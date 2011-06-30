package com.cameratest;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ImageCaptureActivity extends Activity implements SurfaceHolder.Callback {

	private Camera camera;
	private SurfaceView surfaceView;  
	private SurfaceHolder surfaceHolder;
	private boolean isPreviewRunning = false;
	private SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
	private Handler mHandler = null;
	private boolean focus = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagecapture);
		
		surfaceView = (SurfaceView)findViewById(R.id.surfaceViewPreview);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		mHandler = new Handler(){
			public void handleMessage(Message msg){
				String fileLocation = msg.getData().getString("fileLocation");
				Intent result = getIntent();
				result.putExtra("fileLocation", fileLocation);
				setResult(RESULT_OK,result);
				finish();
			}
		};
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (isPreviewRunning){
			camera.stopPreview();
		}
		
		Camera.Parameters parameters = camera.getParameters();
		List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
		Camera.Size previewSize = previewSizes.get(0);
		parameters.setPreviewSize(previewSize.width, previewSize.height);
		camera.setParameters(parameters);
		camera.startPreview();
		
		try{
			camera.setPreviewDisplay(surfaceHolder);
		}catch(IOException e){
			e.printStackTrace();
		}
		camera.startPreview();
		isPreviewRunning = true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		super.onKeyDown(keyCode, event);
		
		if(keyCode == KeyEvent.KEYCODE_MENU){
			camera.autoFocus(new AutoFocusCallback(){
				@Override
				public void onAutoFocus(boolean success, Camera camera){
					if(success)
						focus = true;
				}
			});
		}
		else if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
			if(focus){
				ImageCaptureCallback iccb = null;
				String filename = timeStampFormat.format(new Date());
				ContentValues values =  new ContentValues();
				values.put(Media.TITLE, filename);
				values.put(Media.DESCRIPTION,"Image capture by camera");
				Uri uri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
				try{
					iccb = new ImageCaptureCallback(getContentResolver().openOutputStream(uri),uri);
					camera.takePicture(mShutterCallback, mPictureCallbackRaw, iccb);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
				focus = false;
			}
		}
		
		return true;
	}


	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		isPreviewRunning = false;
		camera.release();
	}
	
	Camera.PictureCallback mPictureCallbackRaw = new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.e(getClass().getSimpleName(), "Picture callback Raw: " + data);
		}
	};
	
	Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		@Override
		public void onShutter() {
			Log.e(getClass().getSimpleName(),"Shutter callback");
			
		}
	};
	
	private class ImageCaptureCallback implements PictureCallback{
		private OutputStream fileOutputStream;
		private Uri uri;
		
		public ImageCaptureCallback(OutputStream fileOutputStream, Uri uri){
			this.fileOutputStream = fileOutputStream;
			this.uri = uri;
		}

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			try{
				Log.v(getClass().getSimpleName(), "onPictureTaken="+data+" length = " + data.length);
				fileOutputStream.write(data);
				fileOutputStream.flush();
				fileOutputStream.close();
				
				Bundle bundle = new Bundle();
				bundle.putString("fileLocation", uri.toString());
				Message msg = new Message();
				msg.setData(bundle);
				mHandler.dispatchMessage(msg);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	

}
