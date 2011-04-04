package edu.illinois.geosight.servercom;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;

public class User {
	protected long id;

	protected String first_name;
	protected String last_name;
	protected String email;
	
	protected Date created_at;
	protected Date updated_at;
	
	public static User current = null;

	public User(long _id) throws GeosightException {

		GeosightEntity user = GeosightEntity.jsonFromGet(String.format("/users/%d.json", _id));

		try {
			populate( user.getObject("user") );
		} catch (JSONException e) {
			throw new GeosightException(e);
		} catch (java.text.ParseException e) {
			throw new GeosightException(e);
		}
	}

	protected User(GeosightEntity user) throws GeosightException {
		try {
			populate( user.getObject("user") );
		} catch (JSONException e) {
			throw new GeosightException(e);
		} catch (java.text.ParseException e) {
			throw new GeosightException(e);
		}
	}

	private void populate(GeosightEntity sight) throws JSONException, ParseException {
		id = sight.getLong("id");
		first_name = sight.getString("first_name");
		last_name = sight.getString("last_name");
		created_at = sight.getDate("created_at");
		updated_at = sight.getDate("updated_at");
	}

	public String getName() {
		return first_name + " " + last_name;
	}
}
