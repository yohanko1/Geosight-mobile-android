package edu.illinois.geosight;


import android.os.Bundle;

import com.google.android.maps.MapActivity;


public class GoogleMapActivity extends MapActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
