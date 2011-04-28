package edu.illinois.geosight.servercom;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;

/**
 * An abstraction for working with Geosight Users
 * 
 * @author Steven Kabbes
 * @author Yo Han Ko
 * 
 */
public class User {
	protected long user_id;
	protected String first_name;
	protected String last_name;
	protected String email;
	protected Date created_at;
	protected Date updated_at;
	public static User current = null;

	/**
	 * Constructor
	 * @param id user id
	 * @throws GeosightException
	 */
	public User(long id) throws GeosightException {
		GeosightEntity user = GeosightEntity.jsonFromGet(String.format(
				"/users/%d.json", id));

		try {
			populate(user.getObject(GeosightEntity.JSON_USER));
		} catch (JSONException e) {
			throw new GeosightException(e);
		} catch (java.text.ParseException e) {
			throw new GeosightException(e);
		}
	}

	/**
	 * Constructor 
	 * @param user user as GeosightEntity
	 * @throws GeosightException
	 */
	protected User(GeosightEntity user) throws GeosightException {
		try {
			populate(user.getObject(GeosightEntity.JSON_USER));
		} catch (JSONException e) {
			throw new GeosightException(e);
		} catch (ParseException e) {
			throw new GeosightException(e);
		}
	}

	private void populate(GeosightEntity sight) throws JSONException,
			ParseException {
		user_id = sight.getLong(GeosightEntity.JSON_ID);
		first_name = sight.getString(GeosightEntity.JSON_FIRST_NAME);
		last_name = sight.getString(GeosightEntity.JSON_LAST_NAME);
		created_at = sight.getDate(GeosightEntity.JSON_CREATED_AT);
		updated_at = sight.getDate(GeosightEntity.JSON_UPDATED_AT);
	}

	/**
	 * Get name
	 * @return name of user
	 */
	public String getName() {
		return first_name + " " + last_name;
	}

	/**
	 * Get id
	 * @return user id 
	 */
	public long getId() {
		return user_id;
	}
}
