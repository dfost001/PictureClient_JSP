package pictureConnect;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import http_util.*;
import pictureClient.ClientResponse;
import pictureClient.Customer;
import pictureClient.ObjectFactory;
import pictureClient.PicSample;

import org.jboss.logging.Logger;

public class PictureConnect {
     
	private final String baseUrl = "http://localhost:7070/PicturesREST/resources" ;
	
	private final String jaxPackage = "pictureClient";
	
	public List<PicSample> pictureList = null;
	
	public List<byte[]> pictureBytes = null;
	
	public Customer customer = null;	
	
	private static Logger logger = Logger.getLogger(PictureConnect.class);
	
	
	public ClientResponse firstInterval(Integer customerId)
	   throws HttpConnectException, HttpClientException, JAXBException {
		
		ClientResponse clientResponse = null;
		
		String path = baseUrl + "/pictures2/firstInterval/" + customerId;	
		
		ApacheConnectBean2 apache = new ApacheConnectBean2();
		
		byte[] bresult = apache.doConnectGet(path);
		
		this.processResponse(apache, bresult);
		
		clientResponse = this.deserialize(bresult);
		
		this.extract(clientResponse);
		
		//init another object from pictureList<PicSample> ?
		
		
		return clientResponse;
		
		
	}
	
	public ClientResponse nextInterval(Integer customerId, Integer from, Integer to) 
	   throws HttpConnectException, HttpClientException, JAXBException{
		
        ClientResponse clientResponse = null;
		
		String path = baseUrl + "/pictures2/nextInterval/" 
		   + customerId + "/"
		   + from + "/"
		   + to;	
		
		ApacheConnectBean2 apache = new ApacheConnectBean2();
		
		byte[] bresult = apache.doConnectGet(path);
		
		this.processResponse(apache, bresult); //throws errors
		
		clientResponse = this.deserialize(bresult);
		
		this.extract(clientResponse);
		
		//init another object from pictureList<PicSample> ?
		
		
		return clientResponse;	
		
	}
	
	public ClientResponse deleteList(Integer customerId, String[] idList)
	          throws HttpConnectException, HttpClientException, JAXBException {
		
		ClientResponse client = null;
		
		String query = "?";
		for(String id : idList){
			String param = "delete=" + id + "&";
			query += param;
		}
		query = query.substring(0, query.length()-1);
		
		String path = baseUrl + "/pictures2/deleteById/" + customerId + query;
		
        ApacheConnectBean2 apache = new ApacheConnectBean2();
		
		byte[] bresult = apache.doConnectDelete(path);
		
		this.processResponse(apache, bresult); //throws errors
		
		client = this.deserialize(bresult);
		
		this.extract(client);
		
		return client;
		
	}
	
	public void debugInsert(PicSample sample) throws HttpException, JAXBException {
		
		String path = baseUrl + "/pictures2/debugInsert";	
		
		JAXBElement<PicSample> el = new ObjectFactory().createPicSample(sample);
		
		try {
			
			   String xml = JaxbUtil.marshal(el, jaxPackage, null);
			   
			   ApacheConnectBean2 apache = new ApacheConnectBean2();
			   
			   byte[] result = apache.doConnectPut(path, xml.getBytes(), "application/xml");
			   
			   this.processResponse(apache, result);			   
			   
			} catch(JAXBException jax){
				throw new JAXBException("PictureConnect#debugInsert: " + jax.getMessage(), jax);
			} 	
		
	}
	
	public ClientResponse insertPicSample(Integer customerId, PicSample sample)
	        throws JAXBException, HttpException {
		
		logger.info("Inside PictureConnect#insertPicSample");
		
		ClientResponse client = null;
		
		String path = baseUrl + "/pictures2/insert2/" + customerId;	
		
		JAXBElement<PicSample> el = new ObjectFactory().createPicSample(sample);
		
		try {
			
		   String xml = JaxbUtil.marshal(el, jaxPackage, null);
		   
		   ApacheConnectBean2 apache = new ApacheConnectBean2();
		   
		   byte[] result = apache.doConnectPut(path, xml.getBytes(), "application/xml");
		   
		   this.processResponse(apache, result);
			
		   client = this.deserialize(result);	
		   
		   this.extract(client);
		   
		   
		} catch(JAXBException jax){
			throw new JAXBException("PictureConnect#insertPicSample: " + jax.getMessage(), jax);
		} 
		
		
		return client;
	}
	
	private void processResponse(ApacheConnectBean2 apache, byte[] bresult)
	     throws HttpClientException {
		
		ResponseUtil util = new ResponseUtil("PictureConnect#processResponse",
				apache.getHeaders(), bresult);
		
		util.setErrorOnEmptyResponse(true);
		
		util.processResponseCode(apache.getResponseCode());
		
	}
	
	private ClientResponse deserialize(byte[] bresult) throws JAXBException{
		
		String xml = new String(bresult);
		JaxbUtil util = new JaxbUtil(this.jaxPackage, null);
		ClientResponse response = (ClientResponse)util.unmarshall(xml, ClientResponse.class);
		
		return response;
	}
	
	private void extract(ClientResponse client) {
		
		this.customer = client.getCustomer();
		
		JAXBElement<ClientResponse.Pictures> el = client.getPictures();
		ClientResponse.Pictures pictures = el.getValue();
		if(pictures == null)
			return;
		
		this.pictureList = pictures.getPicture();		
		
		pictureBytes = new ArrayList<byte[]>();
		
		for(PicSample pic : pictureList) {
			@SuppressWarnings("rawtypes")
			JAXBElement e = pic.getPhoto();
			byte[] b = (byte[]) e.getValue();
			pictureBytes.add(b);
			pic.setPhoto(null);
		}
		
		
	}
	
}
