package edu.illinois.geosight.maps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

/**
 * Receive broadcast notification relating to proximity
 * @author Yo Han Ko
 *
 */
public class ProximityBroadcastReceiver extends BroadcastReceiver {
	
	private static final int VIBRATE_TIME = 2000;
	
	/**
	 * Just show the user a simple notification that they are close to a sight
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "You are 1km away from your destination!", Toast.LENGTH_LONG).show();
		Vibrator alertVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		alertVibrator.vibrate(VIBRATE_TIME);
	}

}
