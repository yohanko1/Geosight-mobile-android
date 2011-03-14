package edu.illinois.geosight;

import java.io.IOException;

import org.apache.http.ParseException;
import org.json.JSONException;

import com.google.android.maps.GeoPoint;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.illinois.geosight.maps.GoogleMapActivity;
import edu.illinois.geosight.servercom.GeosightException;
import edu.illinois.geosight.servercom.Sight;


/**
 * List View showing the list of current sights on the website
 * @author Steven Kabbes
 */
public class SightListActivity extends ListActivity {
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sights);
		
		try {
			setListAdapter( new ArrayAdapter<Sight>(this, android.R.layout.simple_list_item_1, Sight.getAllSights()));
		} catch (GeosightException e) {
			e.printStackTrace();
		}
		
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


}
