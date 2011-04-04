package edu.illinois.geosight;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import edu.illinois.geosight.maps.GoogleMapActivity;
import edu.illinois.geosight.servercom.User;

/*
 * This is the homescreen activity, which serves as a landing page for the rest of the applicaiton
 */
public class GeoSight extends Activity implements OnClickListener, LoginCallback{
	private ImageView mLogo;
	private TextView mStatus;
	
	// Buttons for menu
	private Button loginButton;
	private Button cameraPreview;
	private Button sightsButton;
	private Button mapButton;
	private Button galleryButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// allows smooth alpha on this window
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.main);

		// set up private member variable for the view
		mLogo = (ImageView) findViewById(R.id.logo);
		loginButton = (Button) findViewById(R.id.LoginButton);
		cameraPreview = (Button) findViewById(R.id.CameraPreview);
		sightsButton = (Button) findViewById(R.id.SightsButton);
		mapButton = (Button) findViewById(R.id.MapButton);
		galleryButton = (Button) findViewById(R.id.GalleryButton);

		// register the click events for each button
		loginButton.setOnClickListener(this);
		cameraPreview.setOnClickListener(this);
		sightsButton.setOnClickListener(this);
		mapButton.setOnClickListener(this);
		galleryButton.setOnClickListener(this);

		// start up the entrance animations
		startAnimations();

	}

	// Let's go ahead and start the entrance animation for the buttons and logo
	private void startAnimations() {
		Animation slideInRight = AnimationUtils.loadAnimation(this,
				R.anim.slide_in_from_right);

		mLogo.startAnimation(slideInRight);

		// set up animations for the buttons
		for (int i = 0; i < 5; i++) {
			Animation slideInLeft = AnimationUtils.loadAnimation(this,
					R.anim.slide_in_from_left);

			// each slide in left will be delayed slightly from the previous
			slideInLeft.setStartOffset(slideInRight.getDuration() + i
					* slideInLeft.getDuration() / 5);

			if (i == 0) {
				loginButton.startAnimation(slideInLeft);
			} else if (i == 1) {
				cameraPreview.startAnimation(slideInLeft);
			} else if (i == 2) {
				sightsButton.startAnimation(slideInLeft);
			} else if (i == 3) {
				mapButton.startAnimation(slideInLeft);
			} else if (i == 4) {
				galleryButton.startAnimation(slideInLeft);
			}
		}
	}

	
    /**
     * Handle button clicks for the 4 main buttons on the screen
     */
    @Override
	public void onClick(View v) {
		Intent intent = null;
		int id = v.getId();
		if (id == R.id.CameraPreview) {
			intent = new Intent(this, GPSCameraActivity.class);
		} else if ( v.getId() == R.id.LoginButton ){
			LoginDialog.show(this, this);
			return;
		} else if (id == R.id.MapButton) {
			intent = new Intent(GeoSight.this, GoogleMapActivity.class);
		} else if (id == R.id.SightsButton) {
			intent = new Intent(GeoSight.this, SightListActivity.class);
		} else if (id == R.id.GalleryButton) {
			intent = new Intent(GeoSight.this, GalleryActivity.class);
		}
		startActivity(intent);
	}

	// simply here for conveniance later
	@Override
	protected void onResume() {
		super.onResume();
	}

	// simply here for conveniance later
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onSuccessfulLogin(User user) {
		mStatus.setText( "Logged in as " + user.getName() );
	}
}