package edu.illinois.geosight.maps;

import java.net.URL;

import com.google.android.maps.OverlayItem;

import edu.illinois.geosight.R;

import android.content.Context;

/**
 * Class for overlay view displaying image on inflated balloon
 * @author Steven Kabbes
 */
public class ImageBalloonOverlayView extends BalloonOverlayView {
	protected RemoteImageView remoteView;
	
	public ImageBalloonOverlayView(Context context, int balloonBottomOffset) {
		super(context, balloonBottomOffset);
		remoteView = (RemoteImageView)findViewById(R.id.remoteImageView);
	}
	
	@Override
	public void setData(OverlayItem item) {
		super.setData(item);
		if( item instanceof SightOverlayItem ){
			URL imageUrl = ((SightOverlayItem)item).getSight().getRandomImageUrl();
			remoteView.setImageUrl(imageUrl);
		}
	}
}
