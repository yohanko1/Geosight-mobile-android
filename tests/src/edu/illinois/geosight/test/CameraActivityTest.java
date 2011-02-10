/**
 * 
 */
package edu.illinois.geosight.test;

import edu.illinois.geosight.CameraActivity;
import android.test.ActivityInstrumentationTestCase2;

/**
 * @author steven
 *
 */
public class CameraActivityTest extends
		ActivityInstrumentationTestCase2<CameraActivity> {

	protected CameraActivity mActivity;
	public CameraActivityTest() {
		super("edu.illinois.geosight.CameraActivity", CameraActivity.class);
	}
	
   @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = this.getActivity();
    }
   
    public void testPreconditions() {
    	assert(true);
    }
    
    public void testText() {
      assertEquals("a", "a");
    }

}
