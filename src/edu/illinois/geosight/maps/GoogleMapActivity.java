package edu.illinois.geosight.maps;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import edu.illinois.geosight.R;
import edu.illinois.geosight.servercom.GeosightException;
import edu.illinois.geosight.servercom.Sight;

/**
 * Class for launching Google map canvas as activity
 * @author Steven Kabbes
 * @author Yo Han Ko
 */
public class GoogleMapActivity extends MapActivity {
	MapView mMapView;
	MapController mController;
	LocationOverlay mSightMarkers;
	MyLocationOverlay mLocOverlay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);
		
		mMapView = (MapView) findViewById(R.id.mapview);
		
		// set up the map view
		mController = mMapView.getController();
		mLocOverlay = new MyLocationOverlay(this, mMapView);
		mMapView.getOverlays().add(mLocOverlay);
		mMapView.setBuiltInZoomControls(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mLocOverlay.enableMyLocation();
		mLocOverlay.enableCompass();
		markCurrentLoc();
		markNearestSights();
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		mLocOverlay.disableMyLocation();
		mLocOverlay.disableCompass();
	}

	private void markNearestSights() {
		List<Sight> nearSights = null;
		try {
			// Assumes we are getting nearest sights from the server...
			nearSights = Sight.getAllSights();
		} catch (GeosightException e) {
			e.printStackTrace();
		}

		if (nearSights != null) {
			// mark sights on the map
			Drawable markers = getResources().getDrawable(R.drawable.pinkmarker);
			mSightMarkers = new LocationOverlay(markers, mMapView);
			for (Sight s : nearSights) {
				GeoPoint p = s.getLocation();
				SightOverlayItem i = new SightOverlayItem(p, s, s.getName(), s.toString());
				mSightMarkers.addOverlay(i);
			}
			mMapView.getOverlays().add(mSightMarkers);
		}
	}

	private void markCurrentLoc() {
		mLocOverlay.runOnFirstFix(new Runnable() {
			@Override
			public void run() {
				mController.animateTo(mLocOverlay.getMyLocation());
			}
		});
	}

	/* 
	 *	necessary function for Google to collect data or something.
	 *	return false if we aren't showing a route
	 * @see com.google.android.maps.MapActivity#isRouteDisplayed()
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	/*
	 * necessary function to tell Google whether we're displaying a location or not(non-Javadoc)
	 * @see com.google.android.maps.MapActivity#isLocationDisplayed()
	 */
	@Override
	protected boolean isLocationDisplayed(){
		return true;
	}
}
