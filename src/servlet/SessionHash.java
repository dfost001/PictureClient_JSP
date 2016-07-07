package servlet;

import java.util.List;

import pictureClient.*;


public class SessionHash {
	
	private static final String space = Character.valueOf((char)32).toString();
	
	public static String createHash(ClientResponse client, Customer customer, List<PicSample> pics) {
		
		String hashline = "";
		String indexFrom = client.getIndexFrom().toString();
		String indexTo = client.getIndexTo().toString();
		int id = customer.getId();
		String content = getContent(pics);
		hashline = "indexFrom=" + indexFrom + space 
				+ "indexTo=" + indexTo + space
				+ "id=" + id + space
				+ "names=" + content;
		return hashline;
		
	}
	
   private static String getContent(List<PicSample> list){
		
		String line = "";
		
		if(list == null || list.isEmpty())
			return line;
		
		for(PicSample pic : list)
			line += pic.getPicName() + space;
		
		return  line;
			
	}

}
