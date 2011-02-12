package edu.illinois.geosight;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.location.Location;

public class CameraObject {
	protected Camera mCamera;
	protected Parameters mParameters;
	public String filename;
	
	public CameraObject(Camera camera){
		mCamera = camera;
	}
	
	public PictureCallback mPicCallBack = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream outStream = null;
			try {
				String filePath = "/mnt/sdcard/dcim/Camera"
						+ String.format("/%d.jpg", System.currentTimeMillis());
				
				filename = filePath;
				
				outStream = new FileOutputStream(filePath);
				outStream.write(data);
				outStream.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};
	
	public void takePicture(PictureCallback callback){
		mCamera.takePicture(null, null, callback);
	}
	
	public void takePicture(){
		mCamera.takePicture(null, null, mPicCallBack);
	}
	
	public void takePicture(String filename){
		throw new RuntimeException("Not Implemented");
	}
	
	public void takePicture(Location loc){
		throw new RuntimeException("Not Implemented");
	}
}
