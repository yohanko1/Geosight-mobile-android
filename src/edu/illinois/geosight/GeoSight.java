package edu.illinois.geosight;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import edu.illinois.geosight.maps.GoogleMapActivity;

public class GeoSight extends Activity implements OnClickListener {
	
	private Button cameraPreview;
	private Button mapButton;
	private Button navButton;
	private Button sightsButton;
	private ImageView mLogo;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // allows smooth alpha on this window
        getWindow().setFormat(PixelFormat.RGBA_8888);
        
        setContentView(R.layout.main);
        
        mLogo = (ImageView) findViewById(R.id.logo);
        
        cameraPreview = (Button) findViewById(R.id.CameraPreview);
        cameraPreview.setOnClickListener(this);
        
        mapButton = (Button) findViewById(R.id.MapButton);
        mapButton.setOnClickListener(this);
        
        navButton = (Button) findViewById(R.id.NavButton);
        navButton.setOnClickListener(this);
        
        sightsButton = (Button) findViewById(R.id.SightsButton);
        sightsButton.setOnClickListener(this);
        
        startAnimations();

    }
    
    private void startAnimations(){
    	Animation slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right);
    	Animation slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left);
    	
        mLogo.startAnimation( slideInRight );
        
        // start these after first one finishes
        slideInLeft.setStartOffset( slideInRight.getDuration() );
        cameraPreview.startAnimation( slideInLeft );
        
    	// delay this one 1/3 the total
        slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left);
        slideInLeft.setStartOffset( slideInRight.getDuration() + slideInLeft.getDuration() / 3 );
        sightsButton.startAnimation( slideInLeft );

        // need to reload this animation, since it'll have a different offset
        slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left);
        slideInLeft.setStartOffset( slideInRight.getDuration() + 2 * slideInLeft.getDuration() / 3 );
        mapButton.startAnimation( slideInLeft );
    }
    
	@Override
	public void onClick(View v) {
		Intent intent = null;
		
		if( v.getId() == R.id.CameraPreview){
			//intent = new Intent(this, GPSCameraActivity.class);
			LoginDialog.show(this);
			return;
		} else if( v.getId() == R.id.MapButton ){
			intent = new Intent(GeoSight.this, GoogleMapActivity.class);
		} else if( v.getId() == R.id.SightsButton ){
			intent = new Intent(GeoSight.this, SightListActivity.class);
		} else if( v.getId() == R.id.NavButton ){
			
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=40.113849,-88.224282") );
			//intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.camera") );
		}
		startActivity(intent);
	}
	
	// TODO http://developer.android.com/guide/topics/location/obtaining-user-location.html
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	
	@Override
	protected void onPause() { // TODO or onBackPressed()?
		super.onPause();
		//locationManager.removeUpdates(locationListener);
	}
}