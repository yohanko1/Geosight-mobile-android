package edu.illinois.geosight;

/**
 * ProgressCallback
 * used to provide progress updates on an Async task 
 * @author Steven Kabbes
 */
public interface ProgressCallback{
	
	/**
	 * Method that must be implemented to receive progress events
	 * @param progress percentage of progress completed
	 */
	public void onProgress(double progress);
}