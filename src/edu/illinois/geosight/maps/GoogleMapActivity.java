package edu.illinois.geosight.maps;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

import edu.illinois.geosight.R;
import edu.illinois.geosight.servercom.GeosightException;
import edu.illinois.geosight.servercom.Sight;

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
		mController = mMapView.getController();
		mLocOverlay = new MyLocationOverlay(this, mMapView);
		mLocOverlay.enableMyLocation();
		mLocOverlay.enableCompass();
		mMapView.getOverlays().add(mLocOverlay);
		mMapView.setBuiltInZoomControls(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		markCurrentLoc();
		markNearestSights();
	};

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
			Drawable markers = getResources()
					.getDrawable(R.drawable.pinkmarker);
			mSightMarkers = new LocationOverlay(markers, mMapView);
			for (Sight s : nearSights) {
				GeoPoint p = s.getLocation();
				OverlayItem i = new OverlayItem(p, s.getName(), s.toString());
				mSightMarkers.addOverlay(i, s);
			}
			mMapView.getOverlays().add(mSightMarkers);
		}
	}

	private void markCurrentLoc() {
		/*
		 * TODO: Need to set current location marker to the last set location
		 * how to get last-set location?
		 */
		mLocOverlay.runOnFirstFix(new Runnable() {
			@Override
			public void run() {
				mController.animateTo(mLocOverlay.getMyLocation());
			}
		});
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
