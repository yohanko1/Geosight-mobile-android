package edu.illinois.geosight;

import java.io.File;
import java.io.IOException;

import edu.illinois.geosight.servercom.GeosightEntity;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class UploadActivity extends Activity implements OnClickListener {
	
	private ImageView mImg;
	private ProgressBar mProgress;
	private Button mUploadButton;
	private Button mCancelButton;
	
	private UploadImageTask uploadTask = new UploadImageTask();
	private String imagePath;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload);
		assignViewMembers();
		
		Bundle bundle = getIntent().getExtras();
		
		if( bundle != null ){
			imagePath = bundle.getString("image");
			Bitmap bmp = BitmapFactory.decodeFile( imagePath );
			mImg.setImageBitmap( bmp );
		}
	}

	/**
	 * Assigns class members from the view
	 */
	private void assignViewMembers() {
		// set up private member variable for the view
		mImg = (ImageView) findViewById(R.id.uploadImage);
		mProgress = (ProgressBar) findViewById(R.id.uploadProgress);
		mUploadButton = (Button) findViewById( R.id.uploadButton );
		mCancelButton = (Button) findViewById( R.id.cancelButton );
		
		mUploadButton.setOnClickListener( this );
		mCancelButton.setOnClickListener( this );
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
	public void onClick(View v) {
		if( v.getId() == R.id.uploadButton ){
			mProgress.setVisibility(View.VISIBLE);
			mUploadButton.setEnabled( false );
			uploadTask.execute( new File(imagePath) );
		} else if ( v.getId() == R.id.cancelButton ){
			uploadTask.cancel(true);
			finish();
		}
	}
	
	/**
	 * Asynchronous task for loading the images from the SD card.
	 */
	private class UploadImageTask extends AsyncTask<File, Double, Object> {

		@Override
		protected Object doInBackground(File... files) {
			GeosightEntity.uploadImage(files[0], new ProgressCallback(){	
				@Override
				public void onProgress(double progress) {
					publishProgress( progress * 100 );	
				}
			});
			
			return null;
		}

		@Override
		public void onProgressUpdate(Double... progress) {
			for(int i=0;i<progress.length;i++){
				Log.v("ASDF", "" + progress[i].intValue() );
				mProgress.setProgress( progress[i].intValue() );
			}
		}

		@Override
		protected void onPostExecute(Object result) {
			Toast.makeText(UploadActivity.this, "Upload Complete", Toast.LENGTH_LONG).show();
			UploadActivity.this.finish();
		}
	}
	
}