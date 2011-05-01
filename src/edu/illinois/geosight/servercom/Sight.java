package edu.illinois.geosight.servercom;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

import android.util.Log;

import com.google.android.maps.GeoPoint;

/**
 * Class for sight representation
 * 
 * @author Steven Kabbes
 * @author Yo Han Ko
 * 
 */
public class Sight {
	
	protected long sight_id;
	protected long user_id;
	protected String name;
	protected double radius;
	protected Date created_at;
	protected Date updated_at;
	protected GeoPoint location;
	protected String thumb;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            sight id
	 * @throws GeosightException
	 */
	public Sight(long id) throws GeosightException {

		GeosightEntity sight = GeosightEntity.jsonFromGet(String.format(
				"/sights/%d.json", id));

		try {
			populate(sight);
		} catch (JSONException e) {
			throw new GeosightException(e);
		} catch (ParseException e) {
			throw new GeosightException(e);
		}
	}

	/**
	 * Sight constructor
	 * 
	 * @param sight
	 *            target sight
	 * @throws GeosightException
	 */
	protected Sight(GeosightEntity sight) throws GeosightException {
		try {
			populate(sight);
		} catch (JSONException e) {
			throw new GeosightException(e);
		} catch (ParseException e) {
			throw new GeosightException(e);
		}
	}

	/**
	 * Get random image url
	 * 
	 * @return url for random image
	 */
	public URL getImageUrl() {
		try {
			return new URL(thumb);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get all sight
	 * 
	 * @return List of all sight
	 * @throws GeosightException
	 */
	public static List<Sight> getAllSights() throws GeosightException {
		List<Sight> sights = new ArrayList<Sight>();
		List<GeosightEntity> allSights = GeosightEntity
				.jsonArrayFromGet("/sights.json");
		Log.v("JSONSIGHTS", allSights.size() + "");

		for (int i = 0; i < allSights.size(); i++) {
			sights.add(new Sight(allSights.get(i)));
		}

		return sights;
	}

	/**
	 * Populate Sight from JSON
	 * 
	 * @param sight
	 *            to be populated
	 * @throws JSONException
	 * @throws ParseException
	 */
	private void populate(GeosightEntity sight) throws JSONException,
			ParseException {
		sight_id = sight.getLong(GeosightEntity.JSON_ID);
		user_id = sight.getLong(GeosightEntity.JSON_USER_ID);
		name = sight.getString(GeosightEntity.JSON_NAME);
		radius = sight.getDouble(GeosightEntity.JSON_RADIUS);
		location = sight.getGeoPoint(GeosightEntity.JSON_LATITUDE, GeosightEntity.JSON_LONGITUDE);

		if (sight.jObj.has(GeosightEntity.JSON_THUMBNAIL)) {
			thumb = sight.getString(GeosightEntity.JSON_THUMBNAIL);
		}
	}

	/**
	 * Get name of the sight
	 * 
	 * @return name of the sight
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get location
	 * 
	 * @return location of the sight
	 */
	public GeoPoint getLocation() {
		return location;
	}

	/**
	 * Get radius
	 * 
	 * @return radius of the sight
	 */
	public double getRadius() {
		return radius;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}

}
