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
	
	/**
	 * Constructs a Sight overlay item at a point
	 * @param point the center of the sight
	 * @param sight the sight itself
	 * @param title the title of the overlay item
	 * @param snippet the snipped of the overlay item
	 */
	public SightOverlayItem(GeoPoint point, Sight sight, String title, String snippet) {
		super(point, title, snippet);
		this.sight = sight;
	}
	
	/**
	 * pull out the associated sight
	 * @return
	 */
	public Sight getSight(){
		return sight;
	}
}