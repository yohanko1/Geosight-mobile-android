package edu.illinois.geosight.test;


import edu.illinois.geosight.servercom.Sight;
import junit.framework.TestCase;

public class SightTest extends TestCase {
	protected Sight sight;
	
	protected void setUp(){
		try{
			sight = new Sight(1);
		} catch(Exception ex){
			fail("Exception thrown");
		}
	}
	
	public void testConstructor(){
		try{
			sight = new Sight(1);
		} catch(Exception ex){
			fail("Exception thrown");
		}
	}

	public void testGetName() {
		assertEquals( sight.getName(), "Siebel Center");
	}

	public void testGetLatitudeE6() {
		assertEquals( sight.getLatitudeE6(), 40113974);
	}

	public void testGetLongitudeE6() {
		assertEquals( sight.getLongitudeE6(), -88224305);

	}

}
