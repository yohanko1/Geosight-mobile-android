package edu.illinois.geosight;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;

import edu.illinois.geosight.maps.GoogleMapActivity;
import edu.illinois.geosight.maps.ProximityBroadcastReceiver;
import edu.illinois.geosight.servercom.GeosightException;
import edu.illinois.geosight.servercom.Sight;


/**
 * List View showing the list of current sights on the website
 * @author Steven Kabbes
 */
public class SightListActivity extends ListActivity {
	protected View errorView;
	protected View loadingView;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sights);
		errorView = findViewById(R.id.sightsListError);		
		loadingView = findViewById(R.id.sightListLoading);
		
		(new DownloadSightsTask()).execute();
	}
	
	/**
	 * Launch a mapview showing this particular sight
	 */
	protected void onListItemClick (ListView l, View v, int position, long id){
		
		Sight current = (Sight)l.getItemAtPosition(position);
		Toast.makeText(this, "Navigating to " + current.getName(), Toast.LENGTH_LONG).show();

		GeoPoint dest = current.getLocation();
		float latitude = dest.getLatitudeE6() / (float) 1E6;
		float longitude = dest.getLongitudeE6() / (float) 1E6;
		
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(
				"google.navigation:q=%f,%f", latitude, longitude)));
		
		startActivity(intent);
	}
	
	public void errorLoadingSights() {
		loadingView.setVisibility(View.GONE);
		errorView.setVisibility(View.VISIBLE);
	}

	
	/**
	 * Task used for downloading the sights from the server,  It will also set the listAdapter
	 * on completion
	 * @author Steven Kabbes
	 */
	 private class DownloadSightsTask extends AsyncTask<Integer, Integer, List<Sight> > {
	     /**
	      * Code which is executed on the background thread
	      */
		 protected List<Sight> doInBackground(Integer... doesnotmatter) {
	    	 try{
	    		 return Sight.getAllSights();
	    	 } catch(GeosightException ex){
	    		 return new ArrayList<Sight>();
	    	 }
	     }
	     
	     /**
	      * The code which is called on the UI Thread
	      */
	     protected void onPostExecute(List<Sight> sights) {
	    	 if( sights.size() == 0){
	    		 SightListActivity.this.errorLoadingSights();
	    	 } else {
	    		 SightListActivity.this.setListAdapter( 
	    			 new ArrayAdapter<Sight>(SightListActivity.this, android.R.layout.simple_list_item_1, sights));
	    	 }
	     }
	 }
}
