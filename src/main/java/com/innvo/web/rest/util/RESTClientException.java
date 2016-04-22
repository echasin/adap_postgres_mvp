package com.innvo.web.rest.util;


public class RESTClientException extends Exception {

	private static final long serialVersionUID = -1819628919075584009L;

	public RESTClientException() {
		super(); 
	}
	
	public RESTClientException(String msg) { 
		super(msg); 
	}
	
	public RESTClientException(Throwable t) { 
		super(t); 
	}
	
	public RESTClientException(String msg, Throwable t) { 
		super(msg, t);  
	}
}
