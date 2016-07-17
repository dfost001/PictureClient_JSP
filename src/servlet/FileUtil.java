package servlet;

/*
 * Create a new instance for each picture list download
 */
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
//import java.util.Set;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.nio.file.attribute.FileAttribute;
//import java.nio.file.attribute.PosixFilePermission;
//import java.nio.file.attribute.PosixFilePermissions;

//import org.jboss.logging.Logger;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import pictureClient.PicSample;

/*
 * Assumes file extension contained in picName
 */

public class FileUtil {
	
	  //private Logger logger = Logger.getLogger(FileUtil.class) ; 
	
	  public String dirPath ;	 
	  
	  public static String filSep = System.getProperty("file.separator");
	  
	  private List<String> imageUrl = null;
	  
	  /*private Set<PosixFilePermission> permissions;
	  
	  private final String posix = "rw-rw-rw-";
	  
	  private FileAttribute<Set<PosixFilePermission>> attr ;*/
	  
	
      public FileUtil(String realPath, Integer customer, String session)
                throws IOException {
    	  
    	 // permissions = PosixFilePermissions.fromString(posix);
    	  
    	 // attr = PosixFilePermissions.asFileAttribute(permissions);    	  
    	  
    	  dirPath = realPath + filSep + session + filSep + "customer" + customer;    	  
    	  
    	  initDirectories();
    	  
      }
      
      public static String getUserDirectory(String realPath, Integer customer, String session){
    	  return realPath + filSep + session + filSep + "customer" + customer; 
      }
      
      private void initDirectories() throws IOException{
    	  try {
    	      Path path = Paths.get(dirPath);
    	      //Files.createDirectories(path, attr);
    	      Files.createDirectories(path);
    	      
    	  }
    	  catch(IOException io){
    		  throw new IOException("FileUtil#initDirectories:Can't create directories '" + dirPath
    	         +  "' :" + io.getMessage());
    	  }
      }
      
      
      public void processWrite(List<PicSample> pic, List<byte[]> content)
           throws IOException, FileNotFoundException{
    	  
    	  FileOutputStream fos = null;
    	  
    	  String path = "";
    	  
    	 
    	  try {
    	  for(int i=0; i < pic.size(); i++) {
    		  path = dirPath + filSep + pic.get(i).getPicName();
    		  fos = new FileOutputStream(path);
    		  fos.write(content.get(i));
    	  }
    	  }
    	  catch(FileNotFoundException fnf){
    		  throw new FileNotFoundException("FileUtil#processWrite:FileNotFoundException: " + fnf.getMessage());
    	  }
    	  catch(IOException io){
    		  throw new IOException("FileUtil#processWrite:cannot write file '" + path + "' : " + io.getMessage());
    	  }    	  
      }
      
      public List<String> initImageUrl(List<PicSample> sample, String rootFolder) {
    	  imageUrl = new ArrayList<String>();
    	  int pos = dirPath.indexOf(rootFolder); //position of root folder in real path
    	  String context = dirPath.substring(pos); //obtain root plus the user directory
    	  context = this.replaceSep(context); //replace back-slash with forward slash
    	  for(PicSample s : sample){
    		  imageUrl.add(context + "/" + s.getPicName());
    	  }
    	  return imageUrl;
      }
      
     
      private String replaceSep(String path){
    	  
    	  char oldChar = (char)92; //back-slash
    	  char newChar = (char)47; //forward-slash
    	  return path.replace(oldChar, newChar);
    	  
      }
      
      public static void writeZipResponse(String path, HttpServletResponse response)
                    throws IOException {
    	  byte[] buffer = new byte[1024];
    	  ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	  ServletOutputStream out = response.getOutputStream();
    	  InputStream is = null;
    	  int count = -1;
    	  byte[] bytes = null;
    	  try {
    	        is = new FileInputStream(path);
    	        while((count = is.read(buffer)) != -1)
    	            bos.write(buffer, 0, count);
    	        bytes = bos.toByteArray();
    	        out.write(bytes);
    	  }
    	  catch (FileNotFoundException ex) {
             throw new IOException("writeZipResponse: FileNotFoundException:" + ex.getMessage(), ex);
    	  }
    	  finally {
    		  if(is != null)
    			  is.close();
    	  }
      }
	  
	 /* public FileUtil(String realPath, Integer customer, String session)
              throws IOException {
  	  
  	  permissions = PosixFilePermissions.fromString(posix);
  	  
  	  attr = PosixFilePermissions.asFileAttribute(permissions);
  	  
  	  filSep = System.getProperty("file.separator");
  	  
  	  dirPath = realPath + filSep + "cust" + customer + filSep + session;
  	  
  	  initDirectories();
  	  
    }*/
    
   /* private void initDirectories() {
  	  
  	      Path path = Paths.get(dirPath);
  	      logger.info("initDirectories:" + path.toString());
  	     // Files.createDirectories(path, attr);  	      
  	  
    }*/
    
    
   /* public void processWrite(List<PicSample> pic, List<byte[]> content)  {
  	  
  	 // FileOutputStream fos = null;
  	  
  	  String path = ""; 	  
  	 
  	 
  	  for(int i=0; i < pic.size(); i++) {
  		  path = dirPath + filSep + pic.get(i).getPicName();
  		  //fos = new FileOutputStream(path);
  		  //fos.write(content.get(i));
  		  logger.info("processWrite:" + path);
  	  }
  	   
  	  
    }*/
} //end class
