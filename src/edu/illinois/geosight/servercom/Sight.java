package edu.illinois.geosight.servercom;

import java.io.IOException;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.text.format.DateFormat;

import com.google.android.maps.GeoPoint;

public class Sight {
	protected long id;
	protected long user_id;
	
	protected String name;
	protected double radius;
	protected String created_at;
	protected String updated_at;
	protected GeoPoint location;
	
	public Sight(long id) throws ClientProtocolException, IOException, JSONException{
		populate(id);
	}
	
	public String getName(){
		return name;
	}

	private void populate(long id) throws ClientProtocolException, IOException, JSONException {
		HttpClient client = new DefaultHttpClient();
		// TODO make request depend on ID
		HttpGet request = new HttpGet("http://geosight.heroku.com/sights/" + Long.toString(id) + ".json");
		
		HttpResponse response = client.execute(request);
		
		if( response == null ) return;
		
		String str = EntityUtils.toString(response.getEntity());
		JSONObject jResult = (JSONObject) new JSONTokener(str).nextValue();
		
		JSONObject jSight = jResult.getJSONObject("sight");
		
		this.id = jSight.getLong("id");
		user_id = jSight.getLong("user_id");
		name = jSight.getString("name");
		radius = jSight.getDouble("radius");
		created_at  = jSight.getString("created_at");
		updated_at = jSight.getString("updated_at");
		
		int lat = (int)(jSight.getDouble("latitude") * 1000000);
		int lon = (int)(jSight.getDouble("longitude") * 1000000);
		location = new GeoPoint(lat, lon);
	}
}
