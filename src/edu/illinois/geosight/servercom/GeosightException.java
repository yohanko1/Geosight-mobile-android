package edu.illinois.geosight.servercom;

public class GeosightException extends RuntimeException {

	private static final long serialVersionUID = -2565389840965634769L;
	
	protected Exception underlyingException;
	
	public GeosightException(String message){
		super();
	}
	
	public GeosightException(Exception ex){
		super();
		underlyingException = ex;
	}
	
	public String getMessage(){
		return underlyingException.getMessage();
	}
}
