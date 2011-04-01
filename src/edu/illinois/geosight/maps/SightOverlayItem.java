package edu.illinois.geosight.maps;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import edu.illinois.geosight.servercom.Sight;


public class SightOverlayItem extends OverlayItem {
	protected  Sight sight;
	
	public SightOverlayItem(GeoPoint point, Sight sight, String title, String snippet) {
		super(point, title, snippet);
		this.sight = sight;
	}
	
	public Sight getSight(){
		return sight;
	}
}
