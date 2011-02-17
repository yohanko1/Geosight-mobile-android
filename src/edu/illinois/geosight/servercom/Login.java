package edu.illinois.geosight.servercom;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.util.Log;

public class Login {
	
	public static void populate(long id) throws ClientProtocolException, IOException, JSONException, ParseException {
		HttpClient client = new DefaultHttpClient();
		// TODO make request depend on ID
		HttpPost request = new HttpPost("http://geosight.heroku.com/user_sessions.json");
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		
		pairs.add( new BasicNameValuePair("email", "stevenkabbes@gmail.com") );
		pairs.add( new BasicNameValuePair("password", "steven") );
		request.setEntity( new UrlEncodedFormEntity(pairs) );
		
		HttpResponse response = client.execute(request);
		
		if( response == null ) return;
		
		String str = EntityUtils.toString(response.getEntity());
		Log.v("JSON LOGIN", str);
		/*
		JSONObject jResult = (JSONObject) new JSONTokener(str).nextValue();
		
		JSONObject jSight = jResult.getJSONObject("sight");
		
		String pattern="yyyy-MM-ddThh:mm:ssZ";
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		
		this.id = jSight.getLong("id");
		user_id = jSight.getLong("user_id");
		name = jSight.getString("name");
		radius = jSight.getDouble("radius");
		
		created_at  = dateFormat.parse( jSight.getString("created_at") );
		updated_at = dateFormat.parse( jSight.getString("updated_at") );

		
		
		int lat = (int)(jSight.getDouble("latitude") * 1000000);
		int lon = (int)(jSight.getDouble("longitude") * 1000000);
		location = new GeoPoint(lat, lon);
		*/
	}
}
