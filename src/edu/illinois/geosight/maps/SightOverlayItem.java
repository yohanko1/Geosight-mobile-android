package edu.illinois.geosight.maps;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import edu.illinois.geosight.servercom.Sight;

/**
 * Simple wrapper around an Overlay item which also includes a sight
 * @author Steven Kabbes
 *
 */
public class SightOverlayItem extends OverlayItem {
	protected  Sight sight;
	
	public SightOverlayItem(GeoPoint point, Sight sight, String title, String snippet) {
		super(point, title, snippet);
		this.sight = sight;
	}
	
	// get the associated sight
	public Sight getSight(){
		return sight;
	}
}