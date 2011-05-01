package edu.illinois.geosight.maps;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import edu.illinois.geosight.R;

/**
 * View widget which is capable of showing remote images
 * @author Steven Kabbes
 * @author Yo Han Ko
 *
 */
public class RemoteImageView extends LinearLayout {
	protected ProgressBar mProgressBar;
	protected ImageView mImageView;
	
	/**
	 * Create a remote image view based off an XML attribute set
	 * @param context
	 * @param attrs
	 */
	public RemoteImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mProgressBar = new ProgressBar(context);
		mProgressBar.setIndeterminate(true);
		
		// pass along all the attributes to the underlying imageView
		mImageView = new ImageView(context, attrs);
		
		mImageView.setVisibility(GONE);
		this.addView(mProgressBar);
		this.addView(mImageView);
	}
	
	/**
	 * Set the image url associated with this view
	 * This will consequently download the image in the background
	 * @param url the url to download an image from
	 */
	public void setImageUrl(URL url){
		if( url != null ){
			mImageView.setVisibility(GONE);
			mProgressBar.setVisibility(VISIBLE);
			(new DownloadImageTask()).execute(url);
		}
	}
	
	/**
	 * Private class for downloading images in the background
	 * @author Steven Kabbes
	 *
	 */
	 private class DownloadImageTask extends AsyncTask<URL, Integer, Bitmap > {
	     protected Bitmap doInBackground(URL... urls) {
	    	URL url = urls[0];
	    	
	    	try{
		        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
		        conn.setDoInput(true);
		        conn.connect();  
		        InputStream is = conn.getInputStream();	
		        BufferedInputStream bis = new BufferedInputStream(is);
		        
		        // fix bug with decoding images from streams
		        ByteArrayOutputStream buf = new ByteArrayOutputStream();
		        int result = bis.read();
		        while(result !=-1)
		        {
			        byte b = (byte)result;
			        buf.write(b);
			        result = bis.read();
		        }
		        byte[] content  = buf.toByteArray();
		        
		        // decode the bitmap from the stream
		        Bitmap bm = BitmapFactory.decodeByteArray(content, 0, content.length);  
		        
		        is.close();  
		        bis.close();
		        return bm;
			} catch (IOException ex){
				ex.printStackTrace();
			}
			
			return null;
	     }
	     
	     protected void onPostExecute(Bitmap bmp) {
	    	 mProgressBar.setVisibility(GONE);
	    	 mImageView.setVisibility(VISIBLE);
	    	 
	    	 if (bmp == null){
	    		 mImageView.setImageResource(R.drawable.error_icon);
	    	 } else {
	    		 mImageView.setImageBitmap(bmp);
	    	 }
	     }
	 }

}
