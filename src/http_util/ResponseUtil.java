/*
 * 
 * 
 * Util cannot be used for JSON unless content-type response header is set
 */
package http_util;

//import java.lang.reflect.Field;



import java.nio.charset.CharacterCodingException;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;

/**
 *
 * @author Dinah3
 * Not thread safe -- construct for each use
 * To Do: Static method for return header value
 */

public class ResponseUtil {   
        
    
    private Map<String,List<String>> headers = null;
    private byte[] response = null;
    private String method = "";
    
    private boolean errorOnEmptyResponse = false;
   
    private Logger logger;
    
    public ResponseUtil(String method, Map<String,List<String>> headers, byte[] httpEntity){
        this.method = method + "->" + this.getClass().getSimpleName();
        this.headers = headers;
        this.response = httpEntity;
        logger=Logger.getLogger(ResponseUtil.class);
    }     
    
    
   public void setErrorOnEmptyResponse(Boolean errorOnEmptyResponse) {
		this.errorOnEmptyResponse = errorOnEmptyResponse;
	}



public void processResponseCode(int respCode) throws HttpClientException {       
       
       
       if(respCode >= 200 && respCode < 400) {
    	   if(this.errorOnEmptyResponse && (this.response == null || response.length == 0))
               throw this.initResponseEx();
    	   else return;
       }
       else if(respCode >= 400) {  
    	   throw this.initResponseEx();
       }
   }   
    
    private  String makeDebuggingMsg(){
        String msg = "";
      
              
        if(response == null || response.length == 0)
            msg = "Empty http response";
        else msg = new String(response);       
       
        return msg;
        
    }   
   
    
    /*
     * To do: only print if entity can be decoded (can an image be decoded?)
     */
    private String makeErrorText() {
    	
    	String decodedEntity = "";
            
        String msg = headers.get(null).get(0);   	
    	
        
        if(this.response == null || this.response.length == 0)
            return msg += ": Empty http response entity";             
                  
        String type = this.findContentType();
        
        if(type.equals("html"))
            msg = msg + ":Internal error report from service (html)";
        else if(type.equals("xml"))
            msg = msg + ":Custom application content (xml)";
        else if(type.equals("json"))
            msg = msg + ":Custom application content (json)";
        else if(type.equals("text") || type.equals("unknown")){
        	decodedEntity = new String(response);
        	msg = msg + ":" + decodedEntity;
        }
                             
        return msg;
      
    }
  


	/*
     * End-user message
     * To do: get Retry-After if present on 503
     */
    private String makeReasonPhrase(){
        String phrase = headers.get(null).get(0);
        String message = "Error receiving data from remote service";
        phrase = message + ": " + phrase;       
        return phrase;
    } 
    
    private String findHtml(){
    	String entity = "";
    	if(this.response == null || this.response.length != 0)
    		return "";
    	entity = new String(this.response);
    	int posStart = -1;
    	int posEnd = -1;
    	posStart = entity.toLowerCase().indexOf("<html>");
    	posEnd = entity.toLowerCase().indexOf("</html>");
    	if(posStart > -1 && posEnd > -1)
    		return "html";
        return "";				
    }
    
    private String parseContentHeader(String value){
    	String type="";
    	int pos = value.indexOf(";");
    	if(pos > -1){
    	   type = value.substring(0,pos);
    	   logger.info("parseContentHeader:" + type);
    	}
    	else type = value;
    	return type.trim();
    }
    /*
     * Note: If there is no header, plain text or json will return UNKNOWN
     */
    private  String findContentType(){          
    	
    	if(this.response == null || this.response.length == 0)
    		return "";
        
        List<String> content = headers.get("Content-Type");
        String type = "";
        if(content != null && content.get(0) != null){
        	//logger.info("content=" + content.get(0));
        	type = this.parseContentHeader(content.get(0));
            if(type.equals("text/plain"))
                return "text";
            else if(type.equals("application/xml") ||
                    type.equals("text/xml"))
                return "xml";
            else if(type.equals("application/json") ||
                    type.equals("text/json"))
                return "json";
            else if(type.equals("text/html"))
                return "html";
            else return type;
       }
        else {
        	//not tested, and probably not needed
        	if(findHtml().equals("html")) 
        		 return "html";
        }        	
        
        return "unknown";
    }
   
      
    /*
     * To do: Scan text until body tag is found using "<" as delimiter
     * Keep scanning until </body> is found
     * 
     */
    private String parseHtmlBody(String result){
        String body = "";
        int posStart = result.indexOf("<body>");
        int posEnd = result.indexOf("</body>");
      
        if(posStart > -1){
          
           body = result.substring(posStart + 6, posEnd );
        }
        else body = result;
        return body;
    }   
   
    
   private HttpClientException initResponseEx()  {
	   
        HttpClientException ex = null;
        
        String message = "";        
        
        String friendly = this.makeReasonPhrase();
        
      //returns empty string if empty response
        String type = this.findContentType(); 
        
        String text = this.makeErrorText();
        
        if(type.equals("html"))
        	message = parseHtmlBody(new String(response));
        else message = text;        
       
        String rawResponse = this.makeDebuggingMsg();//raw response  
        
        ex = new HttpClientException(null,message,friendly,this.method); //null cause
        
        ex.setContentType(type);
      
        ex.setRawResponse(rawResponse);      
        
        ex.setText(text);
        
        return ex;
   }
   
   public static String decodeHttpEntity(byte[] response) {
	   String decodedEntity = null;
 
       if(response == null || response.length == 0){
           return null;
       }
       try {
           decodedEntity = new DecoderUtil2().decode(response);
       }
       catch(CharacterCodingException ex) {
    	   // throw this.initApplicationEx(ex,"decodeHttpEntity");
       }
           
       return decodedEntity;
   }
   
   /*private  String makeErrorMsg_ParseHtml() {
	
   String msg = "";               
           
   String type = this.findContentType();        
  
   
   if(response != null && response.length != 0 && type.equals("html")) 
       msg = parseHtmlBody(new String(response));
   else
   	msg = "Empty response entity";
   
   return msg;
}*/ 
    
    
}//end class
