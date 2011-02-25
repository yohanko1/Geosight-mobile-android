package edu.illinois.geosight.servercom;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.android.maps.GeoPoint;

public class Sight extends GeosightEntity{
	protected long id;
	protected long user_id;
	
	protected String name;
	protected double radius;
	protected Date created_at;
	protected Date updated_at;
	protected GeoPoint location;
	
	public Sight(long id) throws ClientProtocolException, IOException, JSONException, java.text.ParseException{
		super();
		go( String.format("/sights/%d.json", id), Method.GET);
		populate( jObj.getJSONObject("sight") );
	}
	
	protected Sight(JSONObject jSight) throws JSONException, java.text.ParseException{
		populate(jSight);
	}
	
	public String getName(){
		return name;
	}
	
	public static List<Sight> getAllSights() throws JSONException, ParseException, IOException, java.text.ParseException{
		List<Sight> sights = new ArrayList<Sight>();
		GeosightEntity allSights = new GeosightEntity();
		allSights.go("/sights.json", Method.GET);
		
		JSONArray result = allSights.jArr;
		for(int i=0;i<result.length();i++){
			allSights.changeContext( result.get(i) );
			sights.add( new Sight( allSights.getJSONObject("sight") ) );
		}
		
		return sights;
	}

	public String toString(){
		return name;
	}
	
	private void populate(JSONObject jSight) throws JSONException, java.text.ParseException{
		changeContext(jSight);
		this.id = getLong("id");
		user_id = getLong("user_id");
		name = getString("name");
		radius = getDouble("radius");
		created_at  = getDate("created_at");
		updated_at = getDate("updated_at");
		location = getGeoPoint("latitude", "longitude");
	}

	public GeoPoint getLocation() {
		return location;
	}

	public int getLatitudeE6() {
		return location.getLatitudeE6();
	}

	public int getLongitudeE6() {
		return location.getLongitudeE6();
	}
}
