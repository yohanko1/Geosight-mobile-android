/**
 * 
 */
package edu.illinois.geosight;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import edu.illinois.geosight.servercom.GeosightEntity;

/**
 * GPS Camera activity handles launching the camera and uploading photos to Geosight
 * It uses the native camera App, and injects GPS coordinates manually
 * @author Steven Kabbes
 * @author Yo Han Ko
 */
public class GPSCameraActivity extends Activity implements LocationListener{
	
	private LocationManager mManager;
	private Uri imageUri;
	private Location mLocation = null;
	
	private static final int CAMERA_ACTIVITY_REQUEST_CODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps_camera);
		
		mManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		launchCamera();
	}

	/**
	 * Called when activity is hidden
	 */
	protected void onPause() {
		super.onPause();
		turnOffGPS();
	}
	
	/**
	 * Called when activity is shown
	 */
	protected void onResume() {
		super.onResume();
		turnOnGPS();
	}
	
	/**
	 * Get the result of the picture taking
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.v("CAMERA", "on activity result");

		if (requestCode == CAMERA_ACTIVITY_REQUEST_CODE) {
	    	Log.v("CAMERA", "Request code captured");

		    if (resultCode == RESULT_OK) {
		        //use imageUri here to access the image
		    	Log.v("CAMERA", "Photo was taken successfully");
		        Toast.makeText(this, "Picture was taken", Toast.LENGTH_LONG);
		        
		        File file = convertImageUriToFile(imageUri, this);
		        
		        GeosightEntity.uploadImage( file );
		        
		        Toast.makeText(this, "Picture was uploaded", Toast.LENGTH_LONG);
		        
		    } else if (resultCode == RESULT_CANCELED) {
		    	Log.v("CAMERA", "Photo was not taken successfully");
		        Toast.makeText(this, "Picture was not taken", Toast.LENGTH_LONG);
		        
		    } else {
		    	Log.v("CAMERA", "Photo was not taken successfully");	
		        Toast.makeText(this, "Picture was not taken", Toast.LENGTH_LONG);
		    }
		    finish();
		}
	}


	/**
	 * Launch native camera application, and inject GPS coordinates
	 */
	protected void launchCamera() {
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.DESCRIPTION, "Geosight Image");

//		// wait until we get current location
//		mManager.requestLocationUpdates(LocationManager, minTime, minDistance, listener)
//		ProgressDialog dialog = ProgressDialog.show(this, "", "Waiting for Current GPS Location...", true);
//		while(mLocation == null){
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		dialog.dismiss();
			
		values.put(MediaStore.Images.Media.LATITUDE, 40.114044 );
		values.put(MediaStore.Images.Media.LONGITUDE,-88.22485 );
		
		//imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
		imageUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		
		//create new Intent
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(cameraIntent, CAMERA_ACTIVITY_REQUEST_CODE);
	}
	
	/**
	 * Gets GPS updates while this activity is active
	 */
	public void turnOnGPS(){
		// get location updates as frequently as possible
		mManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		mManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
	}
	
	/**
	 * Unregistes GPS updates to conserve battery
	 */
	public void turnOffGPS(){
		mManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location newLocation) {
		//launch the camera on first location fix
		if( mLocation == null ){
			mLocation = newLocation;
		}
		mLocation = newLocation;
	}

	@Override
	public void onProviderDisabled(String arg0) {
		Toast.makeText(getApplicationContext(), "GPS Provider not available", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// do nothing
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		Log.v("GPS Status", arg0);
	}
	
	// Shameless taken from http://achorniy.wordpress.com/2010/04/26/howto-launch-android-camera-using-intents/
	public static File convertImageUriToFile (Uri imageUri, Activity activity)  {
		Cursor cursor = null;
		try {
		    String [] proj={MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.ImageColumns.ORIENTATION};
		    cursor = activity.managedQuery( imageUri,
		            proj, // Which columns to return
		            null,       // WHERE clause; which rows to return (all rows)
		            null,       // WHERE clause selection arguments (none)
		            null); // Order-by clause (ascending by name)
		    int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		    int orientation_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);
		    if (cursor.moveToFirst()) {
		        //String orientation =  cursor.getString(orientation_ColumnIndex);
		        return new File(cursor.getString(file_ColumnIndex));
		    }
		    return null;
		} finally {
		    if (cursor != null) {
		        cursor.close();
		    }
		}
	}

}
