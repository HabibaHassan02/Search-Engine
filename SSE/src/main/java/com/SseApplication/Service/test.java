package com.SseApplication.Service;
import java.util.regex.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
//import org.springframework.data.mongodb.core.mapping.Document;
import org.jsoup.select.Elements;

import com.SseApplication.Entity.PageData;


public class test {
//    urlLinks.add(URL);
	public static String clean_str(String str) {
		
		String returnedStr = str.replaceAll(">", "");
		returnedStr = returnedStr.replaceAll("[\\]\\|\\[\\@\\,\\$\\%\\*\\&\\\\\\(\\)\\\":]", "");
		returnedStr = returnedStr.replaceAll("\\.+", "");
		returnedStr = returnedStr.replaceAll("^\s+", "");
		
	    return returnedStr.toLowerCase();
	}
	
	
	public static void main(String[] args) throws IOException {
		String tags []= {"p","span","h1","h2","h3","h4","h5","h6"};
		System.out.println(clean_str("4 > 5 @ [] |"));
		
		String myHtml = "<!DOCTYPE html>\r\n"
		+ "<html>\r\n"
		+ "<head>\r\n"
		+ "<title>Page Title</title>\r\n"
		+ "</head>\r\n"
		+ "<body>\r\n"
		+ "\r\n"
		+ "<h1>My First Heading</h1>\r\n"
		+ "<p>My first paragraph.</p>\r\n"
		+ "\r\n"
		+ "</body>\r\n"
		+ "</html>";
		
		Document d = Jsoup.parse(myHtml);
		Elements els = d.getElementsByTag("h1");
		System.out.println(d.title());
		String URL = "https://en.wikipedia.org/wiki/Main_Page";
		Map<String,PageData> urlHash = new HashMap<String,PageData>();
		Map<String,Map<String,PageData>> wordsHash = new HashMap<String,Map<String,PageData>>();
	    // fetch the HTML code of the given URL by using the connect() and get() method and store the result in Document  
	    Document doc = Jsoup.connect(URL).get();   
//	    String URL = "https://www.wikipedia.org/";
//	    doc.getElementsByTag("h1")
		urlHash.put(URL, null);
		System.out.println(urlHash);
		PageData p = new PageData();
		p.setUrl(URL);
		ArrayList<String> a = new ArrayList<String>();
		a.add("hhf");	
//		p.setInstancesInPage(a);
		urlHash.putIfAbsent(URL, p);
		System.out.println(urlHash);
//		Document doc = Jsoup.connect(URL).get(); 
		for (String tag:tags) {
			doc.getElementsByTag(tag);
			System.out.println(tag + "**************************");
			Elements e = doc.select(tag);
			for (Element es: e) {
				System.out.println(es.text());
			}
			System.out.println("--------------------------------------------------");
		}
		
		
		
//		String s = e.text();
//		String[] arrOfStr = s.split(" ", 0); // o makes the split applied as many times as possible
//		for (String str: arrOfStr) {
//			wordsHash.put(str, urlHash);
//		}
//		
//		System.out.println(doc.title());
//		URL = "https://en.wikipedia.org/wiki/Wiki";
//	    // fetch the HTML code of the given URL by using the connect() and get() method and store the result in Document  
//	    doc = Jsoup.connect(URL).get();   
//	    System.out.println(doc.title());
//		
//	    System.out.println(clean_str("https://www.wikipedia.org/"));
	     // we use the select() method to parse the HTML code for extracting links of other URLs and store them into Elements  
	}
	  
	
}
