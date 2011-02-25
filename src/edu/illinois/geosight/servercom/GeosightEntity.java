package edu.illinois.geosight.servercom;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.android.maps.GeoPoint;

public class GeosightEntity {
	
	// Base url for all requests on Geosight
	protected static final String BASE_URL = "http://geosight.heroku.com";
	
	// the http client to use for all requests.  Also handles cookies
	protected static HttpClient client = null;
	
	// the http context used for doing queries.  This is to preserve cookies
	protected static HttpContext httpContext = null;
		
	protected JSONObject jObj = null;
	protected JSONArray jArr = null;
	
	public enum Method { GET, POST };
	
	public GeosightEntity(){
		if( client == null ){
			client = new DefaultHttpClient();

			httpContext = new BasicHttpContext();
			httpContext.setAttribute(ClientContext.COOKIE_STORE, new BasicCookieStore() );
		}
	}
	
	protected void go(String relativeURL, Method method) throws JSONException, ParseException, IOException{
		
		go( relativeURL, method, null);
	}
	
	protected void go(String relativeURL, Method method, List<NameValuePair> pairs) throws JSONException, ParseException, IOException{
		
		HttpUriRequest request;
		if( method == Method.GET){
			request = new HttpGet(BASE_URL + relativeURL);
		} else {
			HttpPost temp = new HttpPost(BASE_URL + relativeURL);
			temp.setEntity(new UrlEncodedFormEntity(pairs));
			request = temp;
		}
		
		HttpResponse response = client.execute(request, httpContext);

		String str = EntityUtils.toString(response.getEntity());
		
		Object temp = new JSONTokener(str).nextValue();
		changeContext(temp);
	}
	
	protected void login(String email, String password) throws ClientProtocolException, IOException, JSONException{
		
		GeosightEntity temp = new GeosightEntity();
		List<NameValuePair> pairs = new ArrayList<NameValuePair>(2);
		pairs.add( new BasicNameValuePair("user_session[email]", email));
		pairs.add( new BasicNameValuePair("user_session[password]", password));
		
		temp.go("/login.json", Method.POST, pairs);
	}
	
	protected void changeContext(Object o){
		if( o instanceof JSONObject ){
			jObj = (JSONObject)o;
			jArr = null;
		} else if (o instanceof JSONArray ){
			jArr = (JSONArray)o;
			jObj = null;
		}
	}
	
	protected Date getDate(String key) throws java.text.ParseException, JSONException{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
		return df.parse( jObj.getString(key) );
	}
	
	protected long getLong(String key) throws JSONException{
		return jObj.getLong(key);
	}
	
	protected boolean getBoolean(String key) throws JSONException{
		return jObj.getBoolean(key);
	}
	
	protected Double getDouble(String key) throws JSONException{
		return jObj.getDouble(key);
	}
	
	protected int getInt(String key) throws JSONException{
		return jObj.getInt(key);
	}
	
	protected String getString(String key) throws JSONException{
		return jObj.getString(key);
	}
	
	protected JSONArray getJSONArray(String key) throws JSONException{
		return jObj.getJSONArray(key);
	}
	
	protected JSONObject getJSONObject(String key) throws JSONException{
		return jObj.getJSONObject(key);
	}
	
	protected GeoPoint getGeoPoint(String lat, String lon) throws JSONException{
		return new GeoPoint( (int)(getDouble(lat) * 1000000), (int)(getDouble(lon) * 1000000) );
	}
}
