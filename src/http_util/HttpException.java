/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http_util;

/**
 *
 * @author Dinah3
 */
public class HttpException extends Exception{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String friendly = "";
    private String method = "";
     
   
    
    public HttpException(Throwable cause, String message, String friendly, String method){
        super(message,cause,true,true);
        this.friendly = friendly;
        this.method = method;
    }

    public String getFriendly() {
        return friendly;
    }

    public String getMethod() {
        return method;
    }

    public void setFriendly(String friendly) {
        this.friendly = friendly;
    }

    public void setMethod(String method) {
        this.method = method;
    }    
} //end class
