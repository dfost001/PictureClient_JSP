/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http_util;

/**
 *
 * @author Dinah3
 */
public class HttpClientException extends HttpException{
    
   
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String rawResponse = "";
	
    private String contentType = ""; 
    
    private String text = "";    
    
    public HttpClientException(Throwable cause, String message, String friendly, String method){
        
        super(cause, message, friendly, method);       
        
    }   


	public String getText() {
		return text;
	}




	public void setText(String text) {
		this.text = text;
	}




	public String getRawResponse() {
		return rawResponse;
	}


	public void setRawResponse(String rawResponse) {
		this.rawResponse = rawResponse;
	}
	

	public String getContentType() {
		return contentType;
	}


	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
    
   
    
} //end class
