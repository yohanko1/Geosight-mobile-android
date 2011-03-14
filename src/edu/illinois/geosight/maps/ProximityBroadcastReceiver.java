package edu.illinois.geosight.maps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

public class ProximityBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "You are 1km away from your destination!", Toast.LENGTH_LONG).show();
		Vibrator alertVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		alertVibrator.vibrate(2000);
	}

}
