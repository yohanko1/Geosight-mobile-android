/**
 * 
 */
package edu.illinois.geosight;

import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.SlidingDrawer;
import android.widget.Toast;

/**
 * @author steven
 *
 */
public class GPSCameraActivity extends Activity implements LocationListener{
	
	private LocationManager mManager;
	
	private static final int CAMERA_ACTIVITY_REQUEST_CODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		turnOnGPS();
		//launchCamera();
	}

	protected void onPause() {
		super.onPause();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.v("CAMERA", "on activity result");

		if (requestCode == CAMERA_ACTIVITY_REQUEST_CODE) {
	    	Log.v("CAMERA", "Request code captured");

		    if (resultCode == RESULT_OK) {
		        //use imageUri here to access the image
		    	Log.v("CAMERA", "Photo was taken successfully");
		        Toast.makeText(this, "Picture was taken", Toast.LENGTH_LONG);
		    } else if (resultCode == RESULT_CANCELED) {
		    	Log.v("CAMERA", "Photo was not taken successfully");

		        Toast.makeText(this, "Picture was not taken", Toast.LENGTH_LONG);
		    } else {
		    	Log.v("CAMERA", "Photo was not taken successfully");

		        Toast.makeText(this, "Picture was not taken", Toast.LENGTH_LONG);
		    }
		    try {
				Thread.sleep(5000, 0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    finish();
		}
	}


	protected void launchCamera() {
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.DESCRIPTION,"Geosight Image");
		values.put(MediaStore.Images.Media.LATITUDE, "-40.999");
		values.put(MediaStore.Images.Media.LONGITUDE, "88.999");
		
		//imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
		Uri imageUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		
		//create new Intent
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(cameraIntent, CAMERA_ACTIVITY_REQUEST_CODE);
	}
	
	public void turnOnGPS(){
		mManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, this);
		mManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 1, this);
	}
	
	public void turnOffGPS(){
		mManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location newLocation) {
		// TODO Auto-generated method stub
		Log.v("GPS", "Got new location");
		finish();
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		Log.v("GPS Status", arg0);

		
	}
}
