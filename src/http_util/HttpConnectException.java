/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http_util;

/**
 *
 * @author Dinah3
 */
public class HttpConnectException extends HttpException {  
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HttpConnectException(Throwable cause, String message, String friendly, String method){
        
        super(cause,  message, friendly,  method);
       
        
    }    
    
    
} //end class
