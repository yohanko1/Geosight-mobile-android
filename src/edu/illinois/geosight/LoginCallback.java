package edu.illinois.geosight;

import edu.illinois.geosight.servercom.User;

public interface LoginCallback{
	
	// code to call after login has been successfully completed
	public void onSuccessfulLogin(User user);
}