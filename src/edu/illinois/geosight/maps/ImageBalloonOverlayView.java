package edu.illinois.geosight.maps;

import java.net.URL;

import com.google.android.maps.OverlayItem;

import edu.illinois.geosight.R;

import android.content.Context;

/**
 * Class for overlay view displaying image on inflated balloon
 * @author Steven Kabbes
 * @author Yo Han Ko
 */
public class ImageBalloonOverlayView extends BalloonOverlayView {
	
	// The remote image view showing a thumbnail
	protected RemoteImageView mRemoteView;
	
	/**
	 * 
	 * @param context Android's context with which to instatiate the View
	 * @param balloonBottomOffset the number of pixels the bottom of the balloon should be offset
	 */
	public ImageBalloonOverlayView(Context context, int balloonBottomOffset) {
		super(context, balloonBottomOffset);
		mRemoteView = (RemoteImageView)findViewById(R.id.remoteImageView);
	}
	
	@Override
	/**
	 * This method sneaks into the chain of events in the BalloonOverlayView enabling
	 * the image data to be injected
	 */
	public void setData(OverlayItem item) {
		super.setData(item);
		if( item instanceof SightOverlayItem ){
			URL imageUrl = ((SightOverlayItem)item).getSight().getImageUrl();
			mRemoteView.setImageUrl(imageUrl);
		}
	}
}
