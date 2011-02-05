package edu.illinois.geosight;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class GPSLocationListener implements LocationListener {
	protected Location lastLocation;
	
	public Location getLastLocation(){
		return lastLocation;
	}
	
	public void clearLocation(){
		lastLocation = null;
	}
	
	@Override
	public void onLocationChanged(Location loc) {
		Log.v("GPS", "Got a GPS coordinate");
		lastLocation = loc;
	}

	@Override
	public void onProviderDisabled(String arg0) {
		lastLocation = null;
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// nothing to be done here...
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// not sure what to do when the status is changed
	}

}
