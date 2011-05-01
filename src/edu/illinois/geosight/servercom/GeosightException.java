package edu.illinois.geosight.servercom;

/**
 * Exception for Geosight.  Allows runtime exceptions to be handled more elegantly
 * 
 * @author Steven Kabbes
 * @author Yo Han Ko
 *
 */
public class GeosightException extends RuntimeException {

	private static final long serialVersionUID = -2565389840965634769L;
	protected Exception underlyingException;
	/**
	 * Constructor
	 * 
	 * @param message to be displayed
	 */
	public GeosightException(String message){
		super();
	}
	
	/**
	 * Constructor
	 * 
	 * @param ex exception to be passed
	 */
	public GeosightException(Exception ex){
		super();
		underlyingException = ex;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage(){
		return underlyingException.getMessage();
	}
}
