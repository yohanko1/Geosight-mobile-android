package edu.illinois.geosight;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.ParseException;
import org.json.JSONException;

import com.google.android.maps.GeoPoint;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.illinois.geosight.maps.GoogleMapActivity;
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
		Intent intent = new Intent(SightListActivity.this, GoogleMapActivity.class);
		
		GeoPoint loc = current.getLocation();
		intent.putExtra("latitude", loc.getLatitudeE6() );
		intent.putExtra("longitude", loc.getLongitudeE6() );
		
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
	     protected List<Sight> doInBackground(Integer... doesnotmatter) {
	    	 try{
	    		 return Sight.getAllSights();
	    	 } catch(GeosightException ex){
	    		 return new ArrayList<Sight>();
	    	 }
	     }
	     
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
