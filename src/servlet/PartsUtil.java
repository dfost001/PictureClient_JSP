package servlet;

import pictureClient.PicSample;
import pictureClient.ObjectFactory;


import javax.servlet.http.Part;
import javax.xml.bind.JAXBElement;

import constants.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.jboss.logging.Logger;

/*
 * Need check for empty upload
 */
public class PartsUtil {
	
	private PicSample sample = null;
	
	private Constants constants = null;
	
	private Logger logger = null;
	
	private Part imgPart = null;
	
	private String userAgent = "";  //get file path separator on client's browser
	
	public PartsUtil(String userAgent){
		constants = new Constants();
		sample = new PicSample();
		this.userAgent = userAgent;
		logger = Logger.getLogger(PartsUtil.class);
	}
	
	public PicSample processParts(Collection<Part> parts) throws IOException{
		
		boolean OK = parts == null;
		logger.info("Parts is null:" + OK );
		logger.info("parts size:" + parts.size());
		sample.setPhoto(processPicture(parts));
		sample.setComment(processComment(parts));
		if(imgPart != null)
		    sample.setPicName(this.getFileName());
		
		return sample;
	}
	
	private JAXBElement<byte[]> processPicture(Collection<Part> parts) throws IOException {
		
		this.imgPart = this.getPart(parts, constants.getUploadImgKey());		
		
		if(imgPart == null)
			return null;
		
		byte[] picBytes = getPartBytes(imgPart.getInputStream());
		
		if(picBytes == null || picBytes.length == 0)
			return null;
		
		JAXBElement<byte[]> el = new ObjectFactory().createPicSamplePhoto(picBytes);
		
		return el;		
		
	}
	
	private String processComment(Collection<Part> parts) throws IOException {
		
		Part textPart = this.getPart(parts, constants.getUploadTextKey());
		
		if(textPart == null)
			return null;
		byte[] temp = getPartBytes(textPart.getInputStream());
		
		if(temp == null || temp.length == 0)
			return null;
		
		return new String(temp);
	}
	
	private String getFileName() {
		String name = "";
		int pos = -1;
		String content = imgPart.getHeader("Content-Disposition");
		String[] props = content.split(";");
		for(String s : props)
			if(s.trim().startsWith("filename")){
				 pos = s.indexOf("=");
				 name = s.substring(pos + 1);
			}
		name = this.removeQuotes(name);
		name = this.truncatePath(name);
		name = name.replace((char)32, (char)95);//spaces to underscore
		logger.info("Picture Name:" + name);
		return name;
	}
	
	private byte[] getPartBytes(InputStream is) throws IOException{
		
		
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int ct = 0;
		try {
			while((ct = is.read(buffer)) != -1)		
			    bos.write(buffer, 0, ct);
		}
		catch(IOException ex){
			throw new IOException("PartsUtil#getPartBytes:" + ex.getMessage(),ex);
		} finally {
			if(is != null)
				is.close();
		}
		byte[] pic = bos.toByteArray();
		bos.close();
		return pic;
		
	}
	
	private Part getPart(Collection<Part>parts, String key){
		
		Part p = null;
		
		for(Part part : parts){
			
			if(part.getName().equals(key)){
				p = part;				
			}
		}
		return p;
	}
	
	private String removeQuotes(String name){
		if(name.charAt(0)== (char)34)
			return name.substring(1,name.length()-1);
		return name;
	}
	
	private String truncatePath(String name){
		String filSep = "";
		if(this.userAgent.toLowerCase().contains("windows"))
			filSep = "\\";
		else	
			filSep = "/";
		int pos = name.lastIndexOf(filSep);
		if(pos > -1)
		   name = name.substring(pos + 1);
		return name;
	}

}
