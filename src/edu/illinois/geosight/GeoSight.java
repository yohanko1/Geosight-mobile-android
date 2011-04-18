package edu.illinois.geosight;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
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
public class GeoSight extends Activity implements LoginCallback{
	private ImageView mLogo;
	private TextView mStatus;
	
	// Buttons for menu
	private Button loginButton;
	private Button cameraPreview;
	private Button sightsButton;
	private Button mapButton;
	private Button galleryButton;
	private Button uploadButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// allows smooth alpha on this window
		getWindow().setFormat(PixelFormat.RGBA_8888);
		
		setContentView(R.layout.main);

		assignViewMembers();

		// start up the entrance animations
		startAnimations();
	}

	/**
	 * Assigns class members from the view
	 */
	private void assignViewMembers() {
		// set up private member variable for the view
		mLogo = (ImageView) findViewById(R.id.logo);
		loginButton = (Button) findViewById(R.id.LoginButton);
		cameraPreview = (Button) findViewById(R.id.CameraPreview);
		sightsButton = (Button) findViewById(R.id.SightsButton);
		mapButton = (Button) findViewById(R.id.MapButton);
		galleryButton = (Button) findViewById(R.id.GalleryButton);
		mStatus = (TextView) findViewById(R.id.homeStatus);
		uploadButton = (Button) findViewById( R.id.UploadTestButton );
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
			slideInLeft.setStartOffset(slideInRight.getDuration() + i * slideInLeft.getDuration() / 5);

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
	
	public void onCameraClicked(View v){
		Intent intent = new Intent(this, GPSCameraActivity.class);
		startActivity(intent);
	}
	
	public void onSightsClicked(View v){
		Intent intent = new Intent(this, SightListActivity.class);
		startActivity(intent);
	}
	
	public void onMapClicked(View v){
		Intent intent = new Intent(this, GoogleMapActivity.class);
		startActivity(intent);
	}
	
	public void onGalleryClicked(View v){
		Intent intent = new Intent(this, GalleryActivity.class);
		startActivity(intent);
	}
	
	public void onLoginClicked(View v){
		LoginDialog.show(this, this);
	}
	

	// simply here for conveniance later
	@Override
	protected void onResume() {
		super.onResume();
		if( User.current != null){
			onSuccessfulLogin( User.current );
		}
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