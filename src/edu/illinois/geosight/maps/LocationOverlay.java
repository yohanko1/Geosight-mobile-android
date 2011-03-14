package edu.illinois.geosight.maps;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
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

	private static final float CLOSE_THRESHOLD_RADIUS = 1000000; // in meters
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private ArrayList<Sight> mSights = new ArrayList<Sight>();
	private Context mContext;
	private LocationManager mLocManager;

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

	public void addOverlay(OverlayItem overlay, Sight currentSight) {
		mOverlays.add(overlay);
		mSights.add(currentSight);
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
				"Navigating to " + mSights.get(index).getName(),
				Toast.LENGTH_LONG).show();

		Sight currentSight = mSights.get(index);
		GeoPoint dest = currentSight.getLocation();
		float latitude = dest.getLatitudeE6() / (float) 1E6;
		float longitude = dest.getLongitudeE6() / (float) 1E6;
		
		Intent alertIntent = new Intent(mContext, ProximityBroadcastReceiver.class);
		PendingIntent proximityIntent = PendingIntent.getBroadcast(
				mContext.getApplicationContext(), 0, alertIntent, 0);
		
		mLocManager.addProximityAlert(
				latitude, longitude, CLOSE_THRESHOLD_RADIUS, -1,
				proximityIntent);

		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(
				"google.navigation:q=%f,%f", latitude, longitude)));
		mContext.startActivity(intent);

		return true;
	}
}
