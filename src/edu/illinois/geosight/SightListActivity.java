package edu.illinois.geosight;

import java.io.IOException;

import org.apache.http.ParseException;
import org.json.JSONException;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import edu.illinois.geosight.servercom.Sight;

public class SightListActivity extends ListActivity {
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sights);
		
		try {
			setListAdapter( new ArrayAdapter<Sight>(this, android.R.layout.simple_list_item_1, Sight.getAllSights()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
