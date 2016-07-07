package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.jboss.logging.Logger;
	
	public class ZipUtil {
	
	//absolute path to directory to iterate
	//written in initZipSource	 
	private String srcDirPath = ""; 
	
	//absolute path to root of all users (tempPictures)
	//arg passed to constructor
	private String rootFolderPath; 
	
	//absolute path to given user's directory
	//arg passed to constructor
	private String userDirectoryPath;
	
	private static Logger logger = null;	
	private String filSep = "";
	private List<String>fileList = null;	
	private List<String>select = null;
	
	public String zipName = ""; //simple name with date extension
	public String zipFile = ""; //absolute path to zip file 
	
	public ZipUtil(String rootFolder, String userFolder, List<String> select) {
		
		logger = Logger.getLogger(this.getClass());
		
		logger.info("ZipUtil:rootFolder " + rootFolder);	
		
		logger.info("ZipUtil:userFolder:" + userFolder);	
		
		filSep = System.getProperty("file.separator");
		
		fileList = new ArrayList<String>();	
		
		this.rootFolderPath = rootFolder;
		
		this.userDirectoryPath = userFolder;
		
		this.select = select;
		if(this.select == null)
			logger.info("select list is null");
		else logger.info("select list length=" + select.size());
		
		this.initZipSource();
		
		this.initZipTarget();
		
	}
	
	public void zip() throws IOException{
		
		File sourceDir = new File(this.srcDirPath);
		generateFileList2(sourceDir);
		zipIt();
		
	}
	
	
	private void generateFileList2(File file){
		
		String[] dirContent = null;		
		
		if(file.isFile() && file.canRead() && this.includeFile(file))
			addToList(file);
		else if(file.isDirectory()){			
			dirContent = file.list();
		    for(String name : dirContent){
			   File child = new File(file, name);
			   generateFileList2(child);
		    }   
		}		
	}
	
	private boolean includeFile(File file) {
		if(select == null)
			return true;
		String name = file.getName();
		logger.info("ZipUtil#includeFile=" + name);
		for(String n : select){
			if(name.equals(n))
				return true;
		}
		return false;	
	}
	
	private void addToList(File file) {
		
		String absPath = file.getAbsolutePath();
		String path = absPath.substring(this.srcDirPath.length() + 1);
		this.fileList.add(path);
		logger.info("addToList:" + path);
	}
	
	private void zipIt() throws IOException{
		try {
			   FileOutputStream fos = new FileOutputStream(this.zipFile);
			   ZipOutputStream zos = new ZipOutputStream(fos);
			   for(String fil : this.fileList){
				   ZipEntry entry = new ZipEntry(fil);
				   zos.putNextEntry(entry);
				   FileInputStream in = new FileInputStream(this.srcDirPath + File.separator + fil);
				   logger.info("zipIt: InputStream " + this.srcDirPath + File.separator + fil);
				   writeZipFile(in,zos);
				   in.close();
				   
			   }
			   zos.closeEntry();
			   zos.flush();
			   zos.close();
			  
			}
			
			catch(IOException ex){
				throw new IOException("ZipUtil#zipIt ", ex);
			}
		}
    
	private void writeZipFile(FileInputStream in, ZipOutputStream out) throws IOException{
		
		byte[] buffer = new byte[1024];
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		int count = -1;
		
		while((count = in.read(buffer,0, 1024)) != -1){
			bos.write(buffer,0,count);
		}
		
		byte[] bytes = bos.toByteArray();
		
		out.write(bytes);
	}
	
	
	
	private void initZipTarget(){
		
		
		String outDir = this.srcDirPath; //place zip file in session subdirectory
		this.zipName = "pictures" + "-" + dateExt() + ".zip";
		this.zipFile = outDir + filSep + this.zipName;
		logger.info("initZipTarget: " + this.zipFile);
	}
	/*
	 * To do: Use an algorithm to find parent/session directory of customer subdirectory
	 */
	private void initZipSource(){		
		
		int start = rootFolderPath.length() + 1;
		
		//find the next separator after the server root -- this will be the customer session folder
		int pos = this.userDirectoryPath.indexOf(this.filSep, start); //position of customer's folder beginning with session id
		
		this.srcDirPath = userDirectoryPath.substring(0,pos); //absolute path to customer's session folder
		
		logger.info("initZipSource:" + this.srcDirPath);
		
	}
	
	private String dateExt() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fmt = sdf.format(new Date());
		return fmt;
	}
	
	
   
}
