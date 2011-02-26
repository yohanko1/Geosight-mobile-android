package edu.illinois.geosight.servercom;

public class GeosightException extends Exception {

	private static final long serialVersionUID = -2565389840965634769L;
	
	protected Exception underlyingException;
	
	public GeosightException(Exception ex){
		super();
		underlyingException = ex;
	}
	
	public String getMessage(){
		return underlyingException.getMessage();
	}
}
