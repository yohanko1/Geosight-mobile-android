package edu.illinois.geosight.maps;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

/**
 * @author Yo Han Ko
 * Overlay for displaying multiple sights, represented by balloons, on the map
 */
public class LocationOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private MapView mMapView;
	private Context mContext;
	private BalloonOverlayView balloonView;
	private View clickRegion;
	private MapController mMapController;
	
	public LocationOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenterBottom(defaultMarker));
		this.mMapView = mapView;
		this.mContext = mapView.getContext();
		this.mMapController = mapView.getController();
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	@Override
	protected boolean onTap(int index) {
		return super.onTap(index);
	}
	
}














