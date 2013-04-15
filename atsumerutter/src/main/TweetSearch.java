package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
//import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
///import java.util.regex.Matcher;
//import java.util.regex.Pattern;

import net.arnx.jsonic.JSON;

public class TweetSearch {

	//private String[] query;
	private String  query  ;

	private long    max_id;
	private String  next_page;
	//private String  reflesh_url;
	private int     page;

	private String  keyword;
	//private int     timing;
	private boolean json;
	private boolean csv;
	private String  lang;
	private long    since_id;
	private long    min_id;
	private String  fname;




	//コンストラクタ内で検索クエリを作成
	public  TweetSearch(String keyword,int timing,boolean json,boolean csv, String lang,long since_id,String fname){

		this.keyword=keyword;
		//this.timing=timing;
		this.json=json;
		this.csv=csv;
		this.lang=lang;
		this.since_id=since_id;
		this.min_id=since_id;
		this.page=1;
		this.fname=fname;
		//ツイート収集の開始
		//doTweetSearch();
	}


	public long doTweetSearch(){
		//検索条件の確認

		/*
		char[] c={'\u3000'};
		String wspace=new String(c);
		keyword=keyword.replaceAll(wspace," ");

		String regex = "\\s+OR\\s+";
		String regex2 ="\\s+";
		String[] strList=null;
	    Pattern p = Pattern.compile(regex);
	    Matcher m = p.matcher(keyword);
	    Pattern p2 = Pattern.compile(regex2);

	    StringBuilder sb=new StringBuilder();
	    if(m.find()){
	    	strList=query.split("\\s+OR\\s+");
	    	for(int i=0;i<strList.length;i++){
	    		 Matcher m2 = p2.matcher(strList[i]);
	    		 if(m2.find()){
	    			 System.out.println("error");
	    		 }
	    		 try {
					strList[i]=URLEncoder.encode(strList[i] , "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
	    		 sb.append("+OR+");
	    	}
	    	sb.delete(sb.length() - 4, sb.length());
	    }else{
	    	try {
				sb.append(URLEncoder.encode(query , "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

	    }
	    //キーワードの更新
	    keyword=sb.toString();

*/

		this.page=1;
		query="http://search.twitter.com/search.json?q="+keyword+"&rpp=100&lang="+this.lang+"&since_id="+this.since_id;
	    this.since_id=this.getData(query);
	    //最初の検索では過去100件を取得
	    if(this.min_id==0){
			this.min_id=this.since_id;
		}
		while(this.next_page!=null){
				//クエリ設定
			//System.out.println("nextpagefr"+this.next_page);
	    		query = "http://search.twitter.com/search.json"+this.next_page;
	    		//System.out.println("query_while="+query);
	    		this.getData(query);

	    		//検索終了
	    		if(this.min_id>=this.max_id ||this.next_page==null){
	    			System.out.println("break");
	    			break;
	    		}

	    		try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}	// １秒停止後復帰
	    }

		return this.since_id;

	}



	public long  getData(String query){
	try{
		URL url;
		url = new URL( query );
		URLConnection connection = url.openConnection();
		BufferedReader reader =
            new BufferedReader(new InputStreamReader
                (connection.getInputStream(), "JISAutoDetect"));

		//twitter.comにアクセス
		String line = null;

		while ( null != ( line = reader.readLine() ) ) {
			 ParseJson tweetList = JSON.decode(line,ParseJson.class);
			 List<Results> result =tweetList.getResults();
			// System.out.println("reflesh_url=" + tweetList.getRefresh_url());
			// System.out.println("next_page="+tweetList.getNext_page());
			 this.next_page=tweetList.getNext_page();
			 //this.reflesh_url=tweetList.getRefresh_url();
			 this.max_id=tweetList.getMax_id();



			 //Json保存

			 if(this.json){
				 File file = new File("./data/json/"+this.fname+".json");
				 FileWriter filewriter = new FileWriter(file,true);
				 filewriter.write(line);
				 filewriter.write("\n");
				 filewriter.close();
			 }

			 if(this.csv){
				 File file = new File("./data/csv/"+this.fname+".csv");
				 FileWriter filewriter = new FileWriter(file,true);
				 for(int i=0;i<result.size();i++){
					 Results cont = result.get(i);
					  System.out.println("id="+cont.getID()+"created_at=" + cont.getCreated_at()+"  user="+cont.getFrom_user()+   "  text=" +cont.getText());
					 //csv出力
					 filewriter.write(cont.getID()+","+ cont.getCreated_at()+","+cont.getFrom_user()+","+cont.getText());
					 filewriter.write("\n");
				 }
				 filewriter.close();
			 }
			 System.out.println("thispage="+this.page +" query="+query);
			 if(this.page==15){
					System.out.println("maxid更新");
					this.max_id=Long.parseLong(result.get(result.size()-1).getID());
					this.next_page="?page=1&max_id="+ this.max_id+"&rpp=100&lang="+this.lang+"&q="+this.keyword;
					this.page=1;
			 }else{
				 this.page++;
			 }

		 }

		} catch (MalformedURLException e) {
		// TODO 自動生成された catch ブロック
		e.printStackTrace();
		} // URLオブジェクトの生成
		catch (IOException e) {
		// TODO 自動生成された catch ブロック
		e.printStackTrace();
		}
		//System.out.println("nextpageend="+this.next_page);
		return this.max_id;
	}



}
