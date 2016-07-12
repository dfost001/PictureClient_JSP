package servlet;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pictureClient.ClientResponse;
import pictureClient.Customer;
import pictureClient.PicSample;
import pictureConnect.PictureConnect;
import constants.Constants;

import org.jboss.logging.Logger;

/**
 * Servlet implementation class PictureServlet
 */
/*Invalidating the session for a new browser tab 
 */

@WebServlet(name="PictureServlet", loadOnStartup = 1,
        urlPatterns = {"/welcome", "/login", "/upload2", "/nextInterval", 
		"/commands", "/logout"})
@MultipartConfig
public class PictureServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private String picRoot = ""; //  context-param in web.xml
	
	private static Logger logger = Logger.getLogger(PictureServlet.class);	
	
	private Constants constants;	
       
    
    public PictureServlet() {
        super();
        constants = new Constants();
        
    } 
    
    @Override
    public void init(ServletConfig config)throws ServletException{
    	
    	super.init(config);
    	
    	picRoot = super.getServletContext().getInitParameter("picRoot");
    	
    	//config.getServletContext().getInitParameter(arg0) -- this servlet's params
    }   

	/*
	 * Invalidate the session at login in case of back-navigation or a new tab
	 * Required to run SessionDestroy() - clean user directory
	 * Although not absolutely necessary, it is safe, in case all session variables
	 * have not been reset
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String url = "";
		
		String mapping = request.getServletPath();
		
		reset(request);
		
		String id = "";
		
		if(mapping.equals("/welcome")){
			
			request.getSession().setAttribute(constants.getShowModal(), "show");			
			url = "/index.jsp";
		}
		else if(mapping.equals("/login")) {
			request.getSession().invalidate();
			request.getSession(true);
			id = request.getParameter(constants.getCustomerId());			
			url = this.doLogin(request, id);
		}
		else if(mapping.equals("/nextInterval")){
			if(!hashSynched(request)){
				url = "/loggedOut.jsp";
			}
			else {
			  url = this.nextInterval(request);			
			}
		}
		else if(mapping.equals("/commands"))
			if(!hashSynched(request)){
				url = "/loggedOut.jsp";
			} else {
			    url = this.getCommand(request, response);
			}
		else if(mapping.equals("/logout")){
			request.getSession().invalidate(); //may be more than one tab
			url="/loggedOut.jsp";
		}
		if(url != null) //when returning a zip file to the browser -- see this.zip()
		   request.getRequestDispatcher(url).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			    throws ServletException, IOException {
		
		String forwardUrl = "";
		
		reset(request);
		
		String mapping = request.getServletPath();
		
		if(!hashSynched(request)){
			forwardUrl = "/loggedOut.jsp";
			
		}
		else if(mapping.equals("/upload2"))
			forwardUrl = this.doUpload(request);
		
		request.getRequestDispatcher(forwardUrl).forward(request, response);
	}
	
	private void reset(HttpServletRequest request){
		
        request.getSession().removeAttribute(constants.getDebugKey());		
		
		request.getSession().removeAttribute(constants.getClientError());
		
		request.getSession().removeAttribute(constants.getIdNumberFormat());
		
		request.getSession().setAttribute(constants.getShowModal(), "hide");
	}
	
	private boolean hashSynched(HttpServletRequest request){
		String sessionHash = (String)request.getSession().getAttribute(Constants.sessionHash);
		String viewHash = request.getParameter(Constants.sessionHash);		
		return viewHash.equals(sessionHash);
	}
	
	
	
	private String doLogin(HttpServletRequest request, String customerId)
	    throws ServletException, IOException{
		
		Integer id = null;
		String forwardUrl = "";
		
		try {
		    id = Integer.valueOf(customerId);
		    
		    PictureConnect connect = new PictureConnect();
		    
		    ClientResponse clientResponse = null;
		
		    clientResponse = connect.firstInterval(id);
		    
		    new SessionAttributes().addSessionAttributes(request, connect, clientResponse);
		    
		    forwardUrl = "/index.jsp";
		    
		}
		catch(NumberFormatException fmt){
			request.getSession().setAttribute(constants.getIdNumberFormat(), 
					"Customer id '" + customerId + "' does not contain all digits");
			request.getSession().setAttribute(constants.getShowModal(), "show");
			forwardUrl = "/index.jsp";  
			
		}
		catch(Throwable t){
			
			forwardUrl = new ErrorUtil(request, t).getErrPage(); //puts error objects in the session
		}
		return forwardUrl;
	}//
	
	private String doUpload(HttpServletRequest request) throws ServletException {
		
		String forwardUrl = "";

		Customer customer = (Customer) request.getSession().getAttribute(
				constants.getCustomerKey());
		
		String userAgent = request.getHeader("User-Agent");
		
		logger.info("PictureServlet#doUpload: User-Agent=" + userAgent);
				
		try {
			PicSample sample = new PartsUtil(userAgent).processParts(request.getParts());
			
			PictureConnect connect = new PictureConnect();
			
			/*if(request.getParameter("debugInsert") != null) {
				logger.info("PictureServlet#doUpload debugInsert is OK");
				return doDebugInsert(request, sample);
			}*/
			
			ClientResponse client = connect.insertPicSample(
					customer.getId(), sample);
			
			new SessionAttributes().addSessionAttributes(request, connect, client);
			
			forwardUrl = "/index.jsp";
		} catch(Throwable t){
			forwardUrl = new ErrorUtil(request, t).getErrPage();
		}
		return forwardUrl;
	}
	
	/*private String doDebugInsert(HttpServletRequest request, PicSample sample) {
		
		String forwardUrl = "";
		
		try {
		    new PictureConnect().debugInsert(sample);
		    forwardUrl = "/index.jsp";
		    
		}
		catch(Throwable t){
			forwardUrl = new ErrorUtil(request, t).getErrPage();
		}
		return forwardUrl;
	}*/
	
	private String nextInterval(HttpServletRequest request) {

		String url = "";
		ClientResponse client = (ClientResponse) request.getSession()
				.getAttribute(constants.getClientResponseKey());
		Customer customer = (Customer) request.getSession().getAttribute(
				constants.getCustomerKey());

		PictureConnect connect = new PictureConnect();
		try {
			client = connect.nextInterval(customer.getId(),
					client.getIndexFrom(), client.getIndexTo());

			new SessionAttributes().addSessionAttributes(request, connect,
					client);

			url = "/index.jsp";
		} catch (Throwable t) {
			url = new ErrorUtil(request, t).getErrPage();
		}

		return url;
	}
	
	private String getCommand(HttpServletRequest request, HttpServletResponse response){
		
		if(request.getParameter(constants.getBtnDelete()) != null)
			return doDelete(request);
		
		boolean all = true;
		if(request.getParameter(constants.getBtnZipSelect()) != null) {
			all = false;		    
		}
		return this.zip(request, response, all);
	}
	
	private String doDelete(HttpServletRequest request){
		
		String url = "";
		
		String [] ids = request.getParameterValues(constants.getChkDeletePic());
		
		String names = SessionAttributes.getPicNames(request, ids);
		
		PictureConnect picConnect = new PictureConnect();
		
		Customer customer = (Customer)request.getSession().getAttribute(constants.getCustomerKey());
		
		try {
		
		     ClientResponse client = picConnect.deleteList(customer.getId(),ids);
		     
		     new SessionAttributes().addSessionAttributes(request, picConnect, client);
		     
		     request.setAttribute(constants.getShowDeleteSuccess(), true);
		     
		     request.setAttribute(Constants.constDeletedPicNames, names);		    		 

			 url = "/index.jsp";			 
			 
		}
		catch(Throwable t) {
			return new ErrorUtil(request, t).getErrPage();
		}
		
		return url;
		
	}
	
	private String zip(HttpServletRequest request, HttpServletResponse response, boolean all) {
		
		Customer customer = (Customer)request.getSession()
				.getAttribute(constants.getCustomerKey());
		
				
		//String root = request.getServletContext().getInitParameter("relativePictureRoot"); --null
		
		//logger.info("picRoot param = " + root ); -- null
		
		String root = request.getServletContext().getRealPath(this.picRoot); //abs. path to pic folder
		
		String userDir = FileUtil.getUserDirectory(root, 
				customer.getId(), request.getSession().getId());	//absolute path to customer's dir
		
		List<String> select = null;
		
		if(!all)
			select = this.getZipSelection(request);
		
		ZipUtil util= new ZipUtil(root, userDir, select);
		
		try {
		  util.zip();
		  response.setHeader("Content-Disposition", "attachment; filename=" + util.zipName);
		  FileUtil.writeZipResponse(util.zipFile, response);
		}
		catch(IOException io) {
			return new ErrorUtil(request, io).getErrPage();
		}
		
		return null;
	}
	
	private List<String> getZipSelection(HttpServletRequest request) {
		String [] select = request.getParameterValues(constants.getChkDownloadPic());
		List<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(select));
		logger.info("getZipSelection#select=" + select.length);
		logger.info("getZipSelection#list=" + list.size());
		for(String s : list){
			logger.info("filename=" + s);
		}
		return list;
	}
	

}
