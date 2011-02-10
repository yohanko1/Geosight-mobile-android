/**
 * 
 */
package edu.illinois.geosight.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import edu.illinois.geosight.CameraActivity;
import edu.illinois.geosight.CameraObject;

/**
 * @author steven
 *
 */
public class CameraActivityTest extends
		ActivityInstrumentationTestCase2<CameraActivity> {

	protected CameraActivity mActivity;
	protected CameraObject mCamObject;
	
	public CameraActivityTest() {
		super("edu.illinois.geosight.CameraActivity", CameraActivity.class);
	}
	
   @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = this.getActivity();
        // use the Preview's camera object
        mCamObject = new CameraObject(mActivity.getCamera());
    }
   
    /**
     * Ensures that when taking a picture, that the photo file is actually created
     * @throws InterruptedException
     */
    public void testTakePic() throws InterruptedException{
    	mCamObject.takePicture();
    	
    	while( mCamObject.filename == null ){
    		Thread.sleep(500);
    	}
    	
    	// file wasn't created
    	try {
			FileOutputStream os = new FileOutputStream(mCamObject.filename);
		} catch (FileNotFoundException e) {
			fail();
		}
    	
    }

}
