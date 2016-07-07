package servlet;

import http_util.HttpClientException;
import http_util.HttpConnectException;
import http_util.JaxbUtil;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import constants.Constants;

import org.jboss.logging.Logger;

import pictureClient.error.ClientErrorResponse;
import pictureClient.error.ErrorEnum;

public class ErrorUtil {
	
	private Throwable t;
	private HttpClientException httpClient;
	private HttpServletRequest request;
	private Constants constants;
	private static Logger logger;
	private ClientErrorResponse clientError = null;
	private boolean showAlert = false;
	
	public ErrorUtil(HttpServletRequest request, Throwable t){
		this.t = t;
		this.request = request;
		constants = new Constants();
		logger = Logger.getLogger(ErrorUtil.class);
		request.getSession().setAttribute(constants.getStackTrace(), this.getStackTrace(t));
	}
	
	public String getErrPage(){
		String url = "";
		logger.info("Inside ErrorUtil#getErrPage");
		if(t.getClass() == HttpClientException.class){
			httpClient = (HttpClientException)t;
			url = this.processHttpClientError();
			if(this.showAlert) { //set to false in servlet doGet/doPost 
				request.getSession().setAttribute(constants.getShowAlert(), true);				
		    }			
		}
		else if(t.getClass() == HttpConnectException.class) {
			request.getSession().setAttribute(constants.getHttpErrKey(), t);
			url = "/error_pages/connect_error.jsp";
		}
		else if(t.getClass() == JAXBException.class){
			request.getSession().setAttribute(constants.getJavaErrKey(), t);
			url = "/error_pages/java_error.jsp";
		}
		else if(t.getClass() == IOException.class){
			request.getSession().setAttribute(constants.getJavaErrKey(), t);
			url = "/error_pages/java_error.jsp";
		}
		else if(t.getClass() == Exception.class){
			request.getSession().setAttribute(constants.getJavaErrKey(), t);
			url = "/error_pages/java_error.jsp";
		}
		else if(t.getClass()== Error.class){
			request.getSession().setAttribute(constants.getJavaErrKey(), t);
			url = "/error_pages/java_error.jsp";
		}
		else {
			request.getSession().setAttribute(constants.getJavaErrKey(), t);
			url = "/error_pages/java_error.jsp";
		}
		return url;
	}
	
	private String processHttpClientError() {
		
		try {
			if (this.httpClient.getContentType().equals("xml")) {
				deserializeClientError();
				request.getSession().setAttribute(constants.getClientError(), this.clientError);
				return evalClientError(); //either index.jsp for user-entry or badRequest.jsp for debug
			}
			else{
				request.getSession().setAttribute(constants.getHttpErrKey(), this.httpClient);
				return "/error_pages/httpErr.jsp"; //internal error on remote service
			}
		} catch (JAXBException jax) {
			
             request.getSession().setAttribute(constants.getJavaErrKey(), jax);
             return "/error_pages/java_error.jsp";
		}
	}
	
	private String evalClientError(){
		
		
		
		ErrorEnum err = this.clientError.getErrorEnum();
		
		if(err == ErrorEnum.INVALID_CUSTOMER_ID ||
				err == ErrorEnum.TEMPORARY_DATABASE_ERROR ||
				err == ErrorEnum.TEMPORARY_IO_ERROR ||
				err == ErrorEnum.UNSUPPORTED_IMAGE_TYPE ||
				err == ErrorEnum.EMPTY_UPLOAD  || 
				err == ErrorEnum.DELETE_ERROR || 
				err == ErrorEnum.INVALID_COLUMN ){
			this.showAlert = true;
			return "/index.jsp";
		}
		
		return "/error_pages/badRequestHttp.jsp";
		
	}
	
    private void deserializeClientError() throws JAXBException{
    	
    	JaxbUtil util = new JaxbUtil("pictureClient.error", null);
    	
    	String entity = this.httpClient.getRawResponse();
    	
    	this.clientError = (ClientErrorResponse) util.unmarshall(entity, ClientErrorResponse.class);
    	
    }
    
    private String getStackTrace(Throwable t) {
    	String line = "";
    	StringBuilder sb = new StringBuilder(); //single thread
    	for(StackTraceElement el : t.getStackTrace()){
    		line = MessageFormat.format("{0}.{1} ({2}:{3})",
    				el.getClassName(),el.getMethodName(),el.getFileName(),el.getLineNumber());
    		sb.append(line);
    		sb.append("<br/>");
    	}
    	return sb.toString();
    }
}
