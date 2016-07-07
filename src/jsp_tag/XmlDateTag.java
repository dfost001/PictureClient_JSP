package jsp_tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import javax.xml.datatype.XMLGregorianCalendar;
import org.jboss.logging.Logger;

public class XmlDateTag extends TagSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6586390316214876458L;
	
	private XMLGregorianCalendar field;

	public void setField(XMLGregorianCalendar xmlDate) {
		
		this.field = xmlDate;
	}
	
	public int doStartTag() throws JspException {
		
		try {
			JspWriter out = pageContext.getOut();
			out.write(field.toString());
		}
       
		catch(IOException io){
			Logger.getLogger(this.getClass()).info("XmlDateTag error " + io.getMessage());
		}
		
		return SKIP_BODY;
	}
	
	

}
