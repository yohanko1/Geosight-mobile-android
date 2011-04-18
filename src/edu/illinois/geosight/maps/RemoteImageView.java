package edu.illinois.geosight.maps;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.illinois.geosight.R;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * View widget which is capable of showing remote images
 * @author Steven Kabbes
 *
 */
public class RemoteImageView extends LinearLayout {
	protected ProgressBar bar;
	protected ImageView image;
	protected DownloadImageTask downloadTask = new DownloadImageTask();
	
	public RemoteImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		bar = new ProgressBar(context);
		bar.setIndeterminate(true);
		
		image = new ImageView(context, attrs);
		image.setVisibility(GONE);
		this.addView(bar);
		this.addView(image);
	}
	
	/**
	 * Set the image url associated with this view
	 * This will consequently download the image in the background
	 * @param url the url to download an image from
	 */
	public void setImageUrl(URL url){
		if( url != null ){
			image.setVisibility(GONE);
			bar.setVisibility(VISIBLE);
			downloadTask.execute(url);
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
	    	 bar.setVisibility(GONE);
	    	 image.setVisibility(VISIBLE);
	    	 
	    	 if (bmp == null){
	    		 image.setImageResource(R.drawable.error_icon);
	    	 } else {
	    		 image.setImageBitmap(bmp);
	    	 }
	     }
	 }

}
