package main;

import java.util.List;

//import main.Results;

/**
 * @author shakezo_
 *
 */

public class ParseJson {
	
	 private List<Results> results;
	 private String  refresh_url;
	 private String  next_page;
	 private Long  max_id;
	 private Long  since_id;
	
	 /**
	 * @param results
	 */
	public void setResults(List<Results> results){
		 //System.out.println("setresults");
		 this.results=results;
	 }
	
	 
	 public List<Results> getResults(){
		 return this.results;
	 }
	 
	 public void setRefresh_url(String url){
		 // System.out.println("REFLESH");
		 this.refresh_url=url;
	 }
	 public String getRefresh_url(){
		 return this.refresh_url;
	 }
		 
	 public void setNext_page(String page){
		 this.next_page=page;
	 }
	 public String getNext_page(){
		 return this.next_page;
	 }
		 
	 public void setMax_id(String maxid){
		 this.max_id=Long.parseLong(maxid);
	 }
	 
	 public Long getMax_id(){
		 return this.max_id;
	 }
		 
	 public void setSince_id(String sinceid){
		 this.since_id=Long.parseLong(sinceid);
	 }
	 public Long getSince_id(){
		 return this.since_id;
	 }
	 
	 
}
class Results{
	private String  from_user_id_str;
	 private String  from_user;
	 private String  profile_image_url;
	 private String  created_at;
	 private String  id_str;
	 private String  text;
	 
	 
	 public void setId_str(String id ){
		// System.out.println("id");
		 this.id_str=id;
	 }
	 public String  getID(){
		 return this.id_str;
	 }
	
	 public void setCreated_at(String created_at){
		 this.created_at=created_at;
	 }
	 
	 public String getCreated_at(){
		 return this.created_at;
	 }
	 
	 public void setProfile_image_url(String profile){
		 //System.out.println("profile");
		 this.profile_image_url=profile;
	 }
	 
	 public String setProfile_image_url(){
		 //System.out.println("profile");
		 return this.profile_image_url;
	 }
	 
	 public void setFrom_user_id_str(String from_user_id_str){
		 //System.out.println("userid");
		 this.from_user_id_str=from_user_id_str;
	 }
	 
	 public String GetFrom_user_id_str(){
		 return this.from_user_id_str;
	 }
	 
	 public void setFrom_user(String user){
		 //System.out.println("username");
		 this.from_user=user;
	 }
	 public String getFrom_user(){
		 return this.from_user;
	 }
	 
	 public void setText(String text){
		 StringBuffer buf = new StringBuffer();
		 for(int i=0; i<text.length(); i++){
			 char ch = text.charAt(i);
			 if(ch > 0x1f && ch != 0x7f){
			      buf.append(ch);
			 }
		 }
		 this.text= buf.toString();
	 }
	 
	 public String getText(){
		 return this.text;
	 }
	
}