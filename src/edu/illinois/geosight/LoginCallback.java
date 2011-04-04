package edu.illinois.geosight;

import edu.illinois.geosight.servercom.User;

public interface LoginCallback{
	public void onSuccessfulLogin(User user);
}