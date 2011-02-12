package edu.illinois.geosight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.illinois.geosight.servercom.Sight;

public class GeoSight extends Activity implements OnClickListener {
	
	private Button cameraPreview;
	private Button mapButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        cameraPreview = (Button) findViewById(R.id.CameraPreview);
        cameraPreview.setOnClickListener(this);
        
        mapButton = (Button) findViewById(R.id.MapButton);
        mapButton.setOnClickListener(this);
        
        try {
			Sight testSight = new Sight(1);
			Log.v("JSON", testSight.getName() );
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	@Override
	public void onClick(View v) {
		Intent intent = null;
		
		if( v.getId() == R.id.CameraPreview){
			intent = new Intent(this, CameraActivity.class);

		} else if( v.getId() == R.id.MapButton ){
			intent = new Intent(GeoSight.this, GoogleMapActivity.class);
		}
		
		startActivity(intent);
	}
}