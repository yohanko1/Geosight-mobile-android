package edu.illinois.geosight;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 
 * GalleryActivity class to view geotagged photos stored on local device.
 * 
 * @author Yo Han
 * 
 */
public class GalleryActivity extends Activity {

	private static final int GRID_COLUMN_WIDTH = 90;
	private Display displayWidth;
	private ImageAdapter ia;
	private GridView img;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setProgressBarIndeterminateVisibility(true);
		setContentView(R.layout.sdcard);
		displayWidth = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		setGridView();
		// asynchronous load images
		loadImages();
	}

	/**
	 * Setup the grid view.
	 */
	private void setGridView() {
		img = (GridView) findViewById(R.id.sdcard_img);
		img.setNumColumns(displayWidth.getWidth() / GRID_COLUMN_WIDTH);
		ia = new ImageAdapter(getApplicationContext());
		img.setAdapter(ia);
	}

	/**
	 * Load images.
	 */
	private void loadImages() {
		final Object data = getLastNonConfigurationInstance();
		if (data == null) {
			new LoadImagesFromSDCard().execute();
		} else {
			final Bitmap[] photos = (Bitmap[]) data;
			if (photos.length == 0) {
				new LoadImagesFromSDCard().execute();
			}
			for (Bitmap photo : photos) {
				addImage(photo);
			}
		}
	}

	/**
	 * Add image(s) to the grid view adapter.
	 * 
	 * @param value Array of LoadedImages references
	 */
	private void addImage(Bitmap... value) {
		for (Bitmap image : value) {
			ia.addPhoto(image);
			ia.notifyDataSetChanged(); // display images
		}
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		final GridView grid = img;
		final int count = grid.getChildCount();
		final Bitmap[] list = new Bitmap[count];

		for (int i = 0; i < count; i++) {
			final ImageView v = (ImageView) grid.getChildAt(i);
			list[i] = (Bitmap) ((BitmapDrawable) v.getDrawable()).getBitmap();
		}
		return list;
	}

	/**
	 * Adapter for image files.
	 */
	class ImageAdapter extends BaseAdapter {

		private Context mContext;
		private ArrayList<Bitmap> photos = new ArrayList<Bitmap>();

		public ImageAdapter(Context context) {
			mContext = context;
		}

		public void addPhoto(Bitmap photo) {
			photos.add(photo);
		}

		public int getCount() {
			return photos.size();
		}

		public Object getItem(int position) {
			return photos.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final ImageView imageView;
			if (convertView == null) {
				imageView = new ImageView(mContext);
			} else {
				imageView = (ImageView) convertView;
			}
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setImageBitmap(photos.get(position));
			return imageView;
		}
	}

	/**
	 * Asynchronous task for loading the images from the SD card.
	 */
	class LoadImagesFromSDCard extends AsyncTask<Object, Bitmap, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			Bitmap bitmap = null;
			Bitmap newBitmap = null;

			// Set up an array of the Thumbnail Image ID column we want
			String[] projection = { MediaStore.Images.Thumbnails._ID };
			// Create the cursor pointing to the SDCard
			Cursor cursor = managedQuery(
					MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
					projection, // Which columns to return
					null, // Return all rows
					null, null);
			int columnIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
			int size = cursor.getCount();
		
			if (size == 0) {
				Toast.makeText(getApplicationContext(), "No image avaiable",
						Toast.LENGTH_LONG).show();
			} else {
				int imageID = 0;
				for (int i = 0; i < size; i++) {
					cursor.moveToPosition(i);
					imageID = cursor.getInt(columnIndex);
					try {
						// check if the image is geotagged. don't display if it's not.
						if (isGeotagged(getRealPathFromURI(Uri
								.withAppendedPath(
										MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
										"" + imageID)))) {
							bitmap = BitmapFactory
									.decodeStream(getContentResolver()
											.openInputStream(
													Uri.withAppendedPath(
															MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
															"" + imageID)));
							if (bitmap != null) {
								newBitmap = Bitmap.createScaledBitmap(bitmap,
										70, 70, true);
								bitmap.recycle();
								if (newBitmap != null) {
									publishProgress(newBitmap);
								}
							}
						}
					} catch (IOException e) {
					}
				}
			}
			cursor.close();
			return null;
		}

		@Override
		public void onProgressUpdate(Bitmap... value) {
			addImage(value);
		}

		@Override
		protected void onPostExecute(Object result) {
			setProgressBarIndeterminateVisibility(false);
		}
	}

	/**
	 * Gets the filepath from uri
	 * @param contentUri target path
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
		Log.v("index", column_index + "");
		try {
			rv = cursor.getString(column_index);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rv;
	}

	/**
	 * Check if given image on filepath is geotagged.
	 * @param filepath path to the file
	 * @return true if it contains both latitude and longitude. false otherwise
	 * @throws IOException thrown when file doens't exist at filepath
	 */
	public boolean isGeotagged(String filepath) throws IOException {
		if (filepath != null) {
			ExifInterface e = new ExifInterface(filepath);
			Log.v("file", filepath);
			return ((null != e.getAttribute(ExifInterface.TAG_GPS_LATITUDE)) && (null != e
					.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)));
		}
		return false;
	}
}
