package edu.illinois.geosight.maps;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

import edu.illinois.geosight.servercom.Sight;

/**
 * Overlay for displaying multiple sights, represented by balloons, on the map
 * 
 * @author Yo Han Ko
 * 
 */
public class LocationOverlay extends BalloonItemizedOverlay<OverlayItem> {

	private ArrayList<SightOverlayItem> mOverlays = new ArrayList<SightOverlayItem>();
	private ArrayList<Sight> mSights = new ArrayList<Sight>();
	private Context mContext;
	private LocationManager mLocManager;

	/**
	 * Create a Location overlay with a default marker type on top of the given mapview
	 * @param defaultMarker the default marker to mark the items with
	 * @param mapView the mapview on which to overlay 
	 */
	public LocationOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
		this.mContext = mapView.getContext();
		this.mLocManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
	}

	/**
	 * Helper function to convert a meter radius to pixels
	 * @param meters Length in meters of the radius
	 * @param mapview The mapview to convert to
	 * @param latitude the current latitude to convert at (radius will depend on latitude)
	 * @return
	 */
	public static int metersToRadius(double meters, MapView mapview, double latitude) {
		return (int) (mapview.getProjection().metersToEquatorPixels(
				(float) meters) * (1 / Math.cos(Math.toRadians(latitude))));
	}

	/**
	 * Draw all the sights and their radii
	 */
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// don't draw shadow
		if (!shadow) {
			Projection proj = mapView.getProjection();
			Point pixelPoint = new Point();
			Paint color = new Paint();
			color.setARGB((int) (255 / 4), 255, 0, 0); // transparent red

			for (Sight s : mSights) {
				GeoPoint loc = s.getLocation();
				proj.toPixels(loc, pixelPoint);
				int radius = metersToRadius(s.getRadius(), mapView,
						loc.getLatitudeE6() / (double) 1E6);
				canvas.drawCircle(pixelPoint.x, pixelPoint.y, radius, color);
			}
		}

		super.draw(canvas, mapView, shadow);
	}

	public void addOverlay(SightOverlayItem overlay) {
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

	/**
	 * Handle what happens on a balloon tap
	 */
	@Override
	protected boolean onBalloonTap(int index) {
		Toast.makeText(mContext,
				"Navigating to " + mOverlays.get(index).getTitle(),
				Toast.LENGTH_LONG).show();

		Sight currentSight = mOverlays.get(index).getSight();
		GeoPoint dest = currentSight.getLocation();
		float latitude = dest.getLatitudeE6() / (float) 1E6;
		float longitude = dest.getLongitudeE6() / (float) 1E6;
		
		// register notifications for the user when he/she gets close
		Intent alertIntent = new Intent(mContext, ProximityBroadcastReceiver.class);
		PendingIntent proximityIntent = PendingIntent.getBroadcast(
				mContext.getApplicationContext(), 0, alertIntent, 0);
		
		mLocManager.addProximityAlert(
				latitude, longitude, (float) currentSight.getRadius(), -1,
				proximityIntent);

		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(
				"google.navigation:q=%f,%f", latitude, longitude)));
		mContext.startActivity(intent);
		mLocManager.removeUpdates(proximityIntent);
		return true;
	}
}
