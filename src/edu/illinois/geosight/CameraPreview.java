package edu.illinois.geosight;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;

/**
 * This class is the activity for taking a picture using the camera.
 */
public class CameraPreview extends Activity {
	private Preview mPreview;
	private GPSLocationListener mListener;
	private LocationManager mManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		mListener = new GPSLocationListener();
		mManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

		// Hide the window title.
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Create our Preview view and set it as the content of our activity.
		mPreview = new Preview(this);
		
		
		// camera call-back
		mPreview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				takePic();
			}
		});

		setContentView(mPreview);
	}
	

	private void takePic() {
		final Location currentLocation = mListener.getLastLocation();
		if( currentLocation != null ){
			mPreview.mParam.setGpsLatitude( currentLocation.getLatitude() );
			mPreview.mParam.setGpsLongitude( currentLocation.getLongitude() );
			mPreview.mParam.setGpsAltitude( currentLocation.getAltitude() );
			mPreview.mParam.setGpsTimestamp(currentLocation.getTime() );
			
			mPreview.mCamera.setParameters(mPreview.mParam);
			mPreview.mCamera.takePicture(null, null, mPicCallBack);
		}
	}
	
	BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			takePic();
		}
	};
	
	protected void onResume() {
		super.onResume();
		
		mManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 5.0f, mListener);
		IntentFilter filter = new IntentFilter("android.intent.action.CAMERA_BUTTON");
		this.registerReceiver(receiver, filter);
	}

	protected void onPause() {
		super.onPause();
		mManager.removeUpdates(mListener);
		this.unregisterReceiver(receiver);
	}

	PictureCallback mPicCallBack = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream outStream = null;
			try {
				String filePath = "/mnt/sdcard/dcim/Camera"
						+ String.format("/%d.jpg", System.currentTimeMillis());
				outStream = new FileOutputStream(filePath);
				outStream.write(data);
				outStream.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			setResult(RESULT_OK);

			// go back immediately
			finish();
		}
	};
}

// ----------------------------------------------------------------------

class Preview extends SurfaceView implements SurfaceHolder.Callback {
	SurfaceHolder mHolder;
	Camera mCamera;
	Parameters mParam;

	Preview(Context context) {
		super(context);

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it where
		// to draw.
		mCamera = Camera.open();
		mParam = mCamera.getParameters();

		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException exception) {
			mCamera.release();
			mCamera = null;
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the preview.
		// Because the CameraDevice object is not a shared resource, it's very
		// important to release it when the activity is paused.
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
	}

	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.05;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;

			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;

			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;

			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// Now that the size is known, set up the camera parameters and begin
		// the preview.
		Camera.Parameters parameters = mCamera.getParameters();

		List<Size> sizes = parameters.getSupportedPreviewSizes();
		Size optimalSize = getOptimalPreviewSize(sizes, w, h);
		parameters.setPreviewSize(optimalSize.width, optimalSize.height);

		mCamera.setParameters(parameters);
		mCamera.startPreview();
	}
}