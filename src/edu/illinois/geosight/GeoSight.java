package edu.illinois.geosight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GeoSight extends Activity implements OnClickListener {
	
	private Button cameraPreview;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        cameraPreview = (Button) findViewById(R.id.CameraPreview);
        cameraPreview.setOnClickListener(this);
    }
	@Override
	public void onClick(View v) {

		Intent cameraPreviewIntent = new Intent(this, CameraPreview.class);
		startActivity(cameraPreviewIntent);
	}
}