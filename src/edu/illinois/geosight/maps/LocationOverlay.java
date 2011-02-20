package edu.illinois.geosight.maps;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class LocationOverlay extends ItemizedOverlay {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	
	public LocationOverlay(Drawable defaultMarker, Context context){
		super( boundCenterBottom(defaultMarker) );
		mContext = context;
	}
	
	public LocationOverlay(Drawable defaultMarker) {
		super( boundCenterBottom(defaultMarker) );
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

}
