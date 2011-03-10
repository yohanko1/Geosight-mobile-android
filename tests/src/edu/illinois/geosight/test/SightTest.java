package edu.illinois.geosight.test;


import junit.framework.TestCase;

import com.google.android.maps.GeoPoint;

import edu.illinois.geosight.servercom.GeosightEntity;
import edu.illinois.geosight.servercom.GeosightException;
import edu.illinois.geosight.servercom.Sight;

public class SightTest extends TestCase {
	protected Sight sight;
	
	protected void setUp(){
		try{
			sight = new Sight(1);
		} catch(Exception ex){
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}
	
	public void testConstructor(){
		try{
			sight = new Sight(1);
		} catch(Exception ex){
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}
	
	public void testGetRadius(){
		assertEquals( sight.getRadius(), 100.0);
	}
	
	public void testLogin(){
		assertTrue( GeosightEntity.login("test@example.com", "password") );
	}
	
	public void testFailedLogin(){
		assertFalse( GeosightEntity.login("test@example.com", "passworda") );
	}

	public void testGetName() {
		assertEquals( sight.getName(), "Siebel Center");
	}

	public void testLocation() {
		GeoPoint p  = sight.getLocation();
		assertEquals( p.getLatitudeE6(), 40113974);
		assertEquals( p.getLongitudeE6(), -88224305);
	}
}
