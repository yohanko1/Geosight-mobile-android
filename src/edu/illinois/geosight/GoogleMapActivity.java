package edu.illinois.geosight;


import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

import edu.illinois.geosight.maps.LocationOverlay;


public class GoogleMapActivity extends MapActivity {
	MapView mapView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);
		
		mapView = (MapView) findViewById(R.id.mapview);
		
		if( getIntent().getExtras() != null ){
			Bundle extras = getIntent().getExtras();
			GeoPoint p = new GeoPoint( extras.getInt("latitude"), extras.getInt("longitude") );
			mapView.getController().animateTo(p);
			
			OverlayItem destination = new OverlayItem(p, "Your Sight", "Get Going");
			
			MapView mapview = (MapView)findViewById(R.id.mapview);
			
			mapview.setBuiltInZoomControls(true);

			Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
	
			LocationOverlay overlay = new LocationOverlay(drawable, GoogleMapActivity.this);
			
			overlay.addOverlay(destination);
			
			MyLocationOverlay gpsOverlay = new MyLocationOverlay(GoogleMapActivity.this, mapview);
			gpsOverlay.enableMyLocation();
			gpsOverlay.enableCompass();
			
			mapview.getOverlays().add( gpsOverlay );
			mapview.getOverlays().add( overlay );
		}
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
