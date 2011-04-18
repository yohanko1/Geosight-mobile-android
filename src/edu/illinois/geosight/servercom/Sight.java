package edu.illinois.geosight.servercom;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.json.JSONException;

import android.util.Log;

import com.google.android.maps.GeoPoint;

/**
 * Class for sight representation
 * @author Steven Kabbes
 * @author Yo Han Ko
 *
 */
public class Sight {
	protected long id;
	protected long user_id;

	protected String name;
	protected double radius;
	protected Date created_at;
	protected Date updated_at;
	protected GeoPoint location;
	protected String thumb;

	public Sight(long _id) throws GeosightException {

		GeosightEntity sight = GeosightEntity.jsonFromGet(String.format("/sights/%d.json", _id));

		try {
			populate( sight );
		} catch (JSONException e) {
			throw new GeosightException(e);
		} catch (java.text.ParseException e) {
			throw new GeosightException(e);
		}
	}
	
	public URL getRandomImageUrl(){
		try {
			return new URL(thumb);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected Sight(GeosightEntity sight) throws GeosightException {
		try {
			populate( sight );
		} catch (JSONException e) {
			throw new GeosightException(e);
		} catch (java.text.ParseException e) {
			throw new GeosightException(e);
		}
	}

	public static List<Sight> getAllSights() throws GeosightException {
		List<Sight> sights = new ArrayList<Sight>();
		List<GeosightEntity> allSights = GeosightEntity.jsonArrayFromGet("/sights.json");
		Log.v("JSONSIGHTS", allSights.size() + "" );
		
		for (int i = 0; i < allSights.size(); i++) {
			sights.add(new Sight(allSights.get(i)));
		}

		return sights;
	}

	public String toString() {
		return name;
	}

	private void populate(GeosightEntity sight) throws JSONException, ParseException {
		id = sight.getLong("id");
		user_id = sight.getLong("user_id");
		name = sight.getString("name");
		radius = sight.getDouble("radius");
		//created_at = sight.getDate("created_at");
		//updated_at = sight.getDate("updated_at");
		location = sight.getGeoPoint("latitude", "longitude");
		
		if( sight.jObj.has("thumbnail") ){
			thumb = sight.getString("thumbnail");
		}
	}

	public String getName() {
		return name;
	}

	public GeoPoint getLocation() {
		return location;
	}

	public double getRadius() {
		return radius;
	}
}
