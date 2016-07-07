package servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import pictureClient.ClientResponse;
import pictureClient.Customer;
import pictureClient.PicSample;
import pictureConnect.PictureConnect;
import constants.Constants;

import javax.servlet.http.HttpSession;

public class SessionAttributes {
	
	private List<PicSample> picSampleList;
	private List<String> imageUrlList;
	private List<PicSample> tmpSample;
	private List<String> tmpUrl;
	private Customer customer;
	private Constants constants;
	private ClientResponse client;
	
	
	private static final String picFolder = "tempPictures";
	
	public SessionAttributes(){
		constants = new Constants();
	}
	
	@SuppressWarnings("unchecked")
	public void addSessionAttributes(HttpServletRequest request,
			PictureConnect connect,
			ClientResponse clientResponse) throws FileNotFoundException, IOException{
		
		client = clientResponse;
		picSampleList = (List<PicSample>)request.getSession().getAttribute(constants.getSampleListKey());
		imageUrlList = (List<String>)request.getSession().getAttribute(constants.getImgUrlKey());
		customer = connect.customer;
		tmpSample = connect.pictureList;
		if(tmpSample != null && tmpSample.size() > 0){
			tmpUrl = writeToFile(request,connect);			
		}
		assignLists();
		assignSession(request);
		
		
	}
	
	private List<String> writeToFile(HttpServletRequest request, PictureConnect connect)
	      throws FileNotFoundException, IOException {
		
		List<byte[]> picBytes = connect.pictureBytes;
		
        String realPath = request.getServletContext().getRealPath("/" + picFolder);		
        
		FileUtil filutil = new FileUtil(realPath, customer.getId(), request.getSession().getId());
		
		filutil.processWrite(tmpSample, picBytes);
		
		List<String>imgUrl = filutil.initImageUrl(tmpSample, picFolder);			
		
		return imgUrl;
	}
	
	/*
	 * Assumes that if indexFrom > 0, then picSampleList is not null
	 */
	private void assignLists(){
		if(client.getIndexFrom() == -1) {
			this.picSampleList = null;
			this.imageUrlList = null;
		}
		else if(client.getIndexFrom()==0) {
			
			this.picSampleList = tmpSample;
			this.imageUrlList = tmpUrl;
			
		} else {
			
			picSampleList.addAll(tmpSample);
			imageUrlList.addAll(tmpUrl);
			
		}
	}
	
	private void assignSession(HttpServletRequest request) {
		
		int count = picSampleList == null ? 0 : picSampleList.size();
		HttpSession session = request.getSession();
		session.setAttribute(constants.getClientResponseKey(), client);
		session.setAttribute(constants.getImgUrlKey(), this.imageUrlList);
		session.setAttribute(constants.getSampleListKey(), this.picSampleList);
		session.setAttribute(constants.getCount(), count);
		session.setAttribute(constants.getCustomerKey(), customer);
		session.setAttribute(constants.getShowModal(), "hide");
		session.setAttribute(Constants.sessionHash, SessionHash.createHash
				(client, customer, picSampleList));
		
	}
	
	@SuppressWarnings("unchecked")
	public static String getPicNames(HttpServletRequest request, String[] ids) {
		
		String names = "";
		
		List<PicSample> sampleList = (List<PicSample>)request.getSession()
				.getAttribute(Constants.constSampleListKey);
		
		for(PicSample pic : sampleList)
			for(String id : ids)
				if(pic.getPhotoId() == Integer.parseInt(id))
					names += pic.getOriginalPicName() +
					"," + Character.valueOf((char)32).toString();
		return names.substring(0, names.length() - 2);
	}
	
	
	/*private int accumulate(HttpServletRequest request){
		Integer total = (Integer)request.getSession().getAttribute(constants.getCount());
		if(total == null)
			total = new Integer(0);
		int current = tmpSample.size();
		total += current;
		return total;		
	}*/	

} //end class
