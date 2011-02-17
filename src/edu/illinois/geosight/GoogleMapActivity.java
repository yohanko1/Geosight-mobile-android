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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);
		
		MapView mapview = (MapView)findViewById(R.id.mapview);
		
		mapview.setBuiltInZoomControls(true);
		
		GeoPoint siebelPoint = new GeoPoint(40113849, -88224282);
		OverlayItem overlayitem2 = new OverlayItem(siebelPoint, "Hello", " from siebel!");

		GeoPoint point = new GeoPoint(19240000,-99120000);
		OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");

		Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);

		LocationOverlay overlay = new LocationOverlay(drawable, GoogleMapActivity.this);
		
		overlay.addOverlay(overlayitem);
		overlay.addOverlay(overlayitem2);
		
		MyLocationOverlay gpsOverlay = new MyLocationOverlay(GoogleMapActivity.this, mapview);
		gpsOverlay.enableMyLocation();
		gpsOverlay.enableCompass();
		
		mapview.getOverlays().add( gpsOverlay );
		mapview.getOverlays().add( overlay );
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
