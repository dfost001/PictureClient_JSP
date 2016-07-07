package http_util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class JaxbUtil {
	
	@SuppressWarnings("rawtypes")
	Class[] contextClasses = null;
	String jaxPackage = "";
	 
	public JaxbUtil(String jaxPackage, @SuppressWarnings("rawtypes") Class[] context)  {
		if(jaxPackage == null || jaxPackage.isEmpty())
			contextClasses = context;
		else this.jaxPackage = jaxPackage;
	}
	
	 /*
	  * Not tested
	  */
	private InputStream getStreamResult(byte[] buffer) {
    	ByteArrayInputStream stream = null;
    	stream = new ByteArrayInputStream(buffer);
    	return stream;
    }
	
	/*
	 * To do: normalize byte[] and return as bytes
	 * Not tested
	 */
	public Object unmarshall(byte[] response, @SuppressWarnings("rawtypes") Class t) 
			throws JAXBException{
		JAXBContext context = null;
		Object ob = null;
		
		if(contextClasses == null)
		  context = JAXBContext.newInstance(jaxPackage);
		else
		  context = JAXBContext.newInstance(contextClasses) ;	
		
		Unmarshaller u = context.createUnmarshaller();
		InputStream is = getStreamResult(response);
		ob = u.unmarshal(is);
		if(ob.getClass() != t)
		   throw new JAXBException("Entity cannot be cast to" + t.getClass().getName());
		return ob;
		
	}
	
	public Object unmarshall(String response, @SuppressWarnings("rawtypes") Class t)
			throws JAXBException {   	 
   	 
   	 Object ob = null; 
      
      if(response == null || response.isEmpty())
           throw new JAXBException("Entity is empty");
             
      String entity = this.normalizeXmlDoc(response);
      
      if(entity == null)
    	  throw new JAXBException("Xml header is missing");
      
       StringReader sr = new StringReader(entity);           
       
       JAXBContext context = null;       
       
       	if(this.jaxPackage.isEmpty())
       		context = JAXBContext.newInstance(this.contextClasses);
       	else
            context = JAXBContext.newInstance(this.jaxPackage);
       	
        Unmarshaller u = context.createUnmarshaller();
          
        @SuppressWarnings("rawtypes")
		JAXBElement el =  (JAXBElement)u.unmarshal(new StreamSource(sr)); 
        
        ob = el.getValue();
           
        if(ob.getClass() != t) {
           	throw new JAXBException("Entity cannot be cast to" + t.getClass().getName());           	
        }
          
        return ob;
       
   }
	
	 private String normalizeXmlDoc(String response){		   
		   String edited = response;
	       int pos = edited.indexOf("<?xml");
	       if(pos == -1)
	           return null;
	       return edited.substring(pos);       
	  }
	
	 @SuppressWarnings("rawtypes")
	public static String marshal(JAXBElement<?> el, String context, Class[] ctxArr) throws JAXBException{
	        
		    JAXBContext ctx = null;
	        String xml = "";
	        StringWriter sw = new StringWriter();
	              
	      
	    	    if(ctxArr == null)
	              ctx = JAXBContext.newInstance(context);
	    	    else
	    	    	ctx = JAXBContext.newInstance(ctxArr);
	            Marshaller m = ctx.createMarshaller();
	            m.marshal(el,
	                    new StreamResult(sw));
	            xml = sw.toString();
	            return xml;	      

	    }

}//end class
