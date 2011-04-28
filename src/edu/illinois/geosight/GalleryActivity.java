package edu.illinois.geosight;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import edu.illinois.geosight.servercom.GeosightEntity;
import edu.illinois.geosight.servercom.User;

/**
 * 
 * GalleryActivity class to view geotagged photos stored on local device.
 * 
 * @author Yo Han Ko
 * @author Steven Kabbes
 * 
 */
public class GalleryActivity extends Activity {

	private static final int BITMAP_SCALE_FACTOR = 500;
	private ImageAdapter mImgAdapter;
	private LoadImagesFromSDCard mLoadImagesTask = new LoadImagesFromSDCard();
	private UploadImageTask mUploadTask = new UploadImageTask();
	private ArrayList<String> mImgPaths = new ArrayList<String>();

	private Gallery mGallery;
	private ImageView mImg;
	private ProgressBar mProgress;
	private Button mUploadButton;
	@SuppressWarnings("unused")
	private Button mCancelButton;
	private String mCurrentImgPath = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery);
		setView();

		// set call-back for thumbnails
		mGallery.setOnItemClickListener(new OnItemClickListener() {
			Bitmap bm = null;

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (bm != null)
					bm.recycle();
				mCurrentImgPath = mImgPaths.get(position);
				try {
					bm = new BitmapScaler(new File(mCurrentImgPath),
							BITMAP_SCALE_FACTOR).getScaled();
				} catch (IOException e) {
					e.printStackTrace();
				}
				mImg.setImageBitmap(bm);
				mUploadButton.setEnabled(true);
			}
		});
		loadThumbnailImages();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mLoadImagesTask.cancel(true);
		mUploadTask.cancel(true);
	};

	/**
	 * Callback for cancel button
	 * 
	 * @param v
	 *            current view
	 */
	public void onCancelClick(View v) {
		mUploadTask.cancel(true);
		finish();
	}

	/**
	 * Callback for upload button
	 * 
	 * @param v
	 *            current view
	 */
	public void onUploadClick(View v) {
		if (User.current != null) {
			if (mCurrentImgPath == null) {
				Toast.makeText(this, R.string.please_select_an_image,
						Toast.LENGTH_SHORT).show();
			} else {
				mProgress.setVisibility(View.VISIBLE);
				mUploadButton.setEnabled(false);
				mUploadTask.execute(new File(mCurrentImgPath));
				mUploadTask = new UploadImageTask(); // for subsequent upload
			}
		} else {
			LoginDialog.show(this);
		}
	}

	/**
	 * Set up components for view
	 */
	private void setView() {
		mGallery = (Gallery) findViewById(R.id.gallery);
		mImg = (ImageView) findViewById(R.id.galleryImage);
		mProgress = (ProgressBar) findViewById(R.id.galleryUploadProgress);
		mUploadButton = (Button) findViewById(R.id.galleryUploadButton);
		mCancelButton = (Button) findViewById(R.id.galleryCancelButton);
		mImgAdapter = new ImageAdapter(getApplicationContext());
		mGallery.setAdapter(mImgAdapter);
	}

	/**
	 * Load images.
	 */
	private void loadThumbnailImages() {
		final Object data = getLastNonConfigurationInstance();
		if (data == null) {
			mLoadImagesTask.execute();
		} else {
			final Bitmap[] photos = (Bitmap[]) data;
			if (photos.length == 0) {
				mLoadImagesTask.execute();
			}
			for (Bitmap photo : photos) {
				addImage(photo);
			}
		}
	}

	/**
	 * Add image(s) to the grid view adapter.
	 * 
	 * @param value
	 *            Array of LoadedImages references
	 */
	private void addImage(Bitmap... value) {
		for (Bitmap image : value) {
			mImgAdapter.addPhoto(image);
			mImgAdapter.notifyDataSetChanged(); // display images
		}
	}

	/**
	 * Adapter for image files.
	 */
	private class ImageAdapter extends BaseAdapter {

		private Context mContext;
		private ArrayList<Bitmap> photos = new ArrayList<Bitmap>();

		/**
		 * Constructor
		 * 
		 * @param context
		 *            current app context
		 */
		public ImageAdapter(Context context) {
			mContext = context;
		}

		/**
		 * Add photo to array of photos
		 * 
		 * @param photo
		 *            photo to be added
		 */
		public void addPhoto(Bitmap photo) {
			photos.add(photo);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		public int getCount() {
			return photos.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 */
		public Object getItem(int position) {
			return photos.get(position);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItemId(int)
		 */
		public long getItemId(int position) {
			return position;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			final ImageView imgView;
			if (convertView == null) {
				imgView = new ImageView(mContext);
			} else {
				imgView = (ImageView) convertView;
			}
			imgView.setImageBitmap(photos.get(position));
			imgView.setLayoutParams(new Gallery.LayoutParams(150, 120));
			imgView.setAdjustViewBounds(true);
			return imgView;
		}
	}

	/**
	 * Asynchronous task for loading the images from the SD card.
	 */
	private class LoadImagesFromSDCard extends
			AsyncTask<Object, Bitmap, Object> {

		private static final int THUMBNAIL_DIMENSION = 70;

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Object doInBackground(Object... params) {
			String[] projection = { MediaStore.Images.Thumbnails._ID,
					MediaStore.Images.Thumbnails.IMAGE_ID };

			// Create the cursor pointing to the SDCard
			Cursor cursor = managedQuery(
					MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
					projection, // Which columns to return
					null, // Return all rows
					null, null);
			if (cursor.getCount() == 0) {
				Toast.makeText(getApplicationContext(),
						R.string.no_images_avaiable, Toast.LENGTH_LONG).show();
			} else {
				iterateImages(cursor);
			}
			cursor.close();
			return null;
		}

		private void iterateImages(Cursor cursor) {
			int imgIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID);
			int thumIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
			int size = cursor.getCount();

			for (int i = 0; i < size; i++) {
				cursor.moveToPosition(i);
				try {
					Uri imageUri = getUri(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							cursor.getInt(imgIndex));
					String imgFilepath = getRealPathFromURI(imageUri);
					if (imgFilepath != null)
						updateThumbnail(cursor.getInt(thumIndex), imgFilepath);
				} catch (IOException e) {
					// ignore missing photos
				}
			}
		}

		private void updateThumbnail(int thumbID, String imgFilepath)
				throws IOException, FileNotFoundException {
			Bitmap bitmap;
			Bitmap newBitmap;
			if (isGeotagged(imgFilepath) && isJPEG(imgFilepath)) {

				Uri thumbURI = getUri(
						MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
						thumbID);
				InputStream thumbIS = getContentResolver().openInputStream(
						thumbURI);
				bitmap = BitmapFactory.decodeStream(thumbIS);

				if (bitmap != null) {
					newBitmap = Bitmap.createScaledBitmap(bitmap,
							THUMBNAIL_DIMENSION, THUMBNAIL_DIMENSION, true);
					bitmap.recycle();
					if (newBitmap != null) {
						mImgPaths.add(imgFilepath);
						publishProgress(newBitmap);
					}
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		public void onProgressUpdate(Bitmap... value) {
			addImage(value);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Object result) {
			setProgressBarIndeterminateVisibility(false);
		}

		/**
		 * Simple helper to get full URI
		 * 
		 * @param type
		 *            Uri type
		 * @param imageID
		 *            image id in database
		 * @return full Uri of image
		 */
		private Uri getUri(Uri type, int imageID) {
			return Uri.withAppendedPath(type, "" + imageID);
		}
	}

	/**
	 * Asynchronous task for loading the images from the SD card.
	 */
	private class UploadImageTask extends AsyncTask<File, Double, Object> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Object doInBackground(File... files) {
			GeosightEntity.uploadImage(files[0], new ProgressCallback() {
				@Override
				public void onProgress(double progress) {
					publishProgress(progress * 100);
				}
			});

			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		public void onProgressUpdate(Double... progress) {
			for (int i = 0; i < progress.length; i++) {
				mProgress.setProgress(progress[i].intValue());
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Object result) {
			GalleryActivity.this.finish();
			Toast.makeText(GalleryActivity.this, R.string.upload_complete,
					Toast.LENGTH_LONG).show();
			mProgress.setVisibility(View.GONE);
		}
	}

	/**
	 * Gets the filepath from uri
	 * 
	 * @param contentUri
	 *            target path
	 * @return path corresponding to uri; null if uri is invalid
	 */
	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		String rv = null;
		Cursor cursor = managedQuery(contentUri, proj, // Which columns
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		try {
			rv = cursor.getString(column_index);
		} catch (Exception e) {
			return null;
		}
		return rv;
	}

	/**
	 * Check if given image on filepath is geotagged.
	 * 
	 * @param filepath
	 *            path to the file
	 * @return true if it contains both latitude and longitude. false otherwise
	 * @throws IOException
	 *             thrown when file doens't exist at filepath
	 */
	public boolean isGeotagged(String filepath) throws IOException {
		if (filepath != null) {
			ExifInterface e = new ExifInterface(filepath);
			return (e.getAttribute(ExifInterface.TAG_GPS_LATITUDE) != null)
					&& (e.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) != null);
		}
		return false;
	}

	/**
	 * Check if the file is in jpeg format
	 * 
	 * @param filepath
	 *            where file is
	 * @return true if the filename has .jpeg postfix
	 */
	public boolean isJPEG(String filepath) {
		String filenameArray[] = filepath.split("\\.");
		String extension = filenameArray[filenameArray.length - 1];
		return (extension.equalsIgnoreCase("JPEG") || extension
				.equalsIgnoreCase("JPG"));
	}

}
