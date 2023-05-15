package com.SseApplication.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Service;

import com.SseApplication.Entity.Indexer;
import com.SseApplication.Entity.PageData;
import com.SseApplication.Repository.IndexerRepository;
import com.SseApplication.Repository.WebCrawlerRepository;

@Service
public class IndexerService {
	@Autowired
	IndexerRepository indexerRepo;
	
	@Autowired
	WebCrawlerRepository crawlerRepo;
	// open stop words here
	// stop words file contain words that are seperated by comma
	//TODO
	
	public boolean clean_str(String str) {
		Pattern pattern = Pattern.compile("^.*\\..*$", Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(str);
	    boolean matchFound = matcher.find();
	    return matchFound;
	}
	public  void addToIndexer() throws IOException {
		String tags []= {"p","span","h1","h2","h3","h4","h5","h6"};
		Map<String,PageData> urlHash = new HashMap<String,PageData>();
		Map<String,Map<String,PageData>> wordsHash = new HashMap<String,Map<String,PageData>>();
		Set<String> set = new HashSet<>(); //set ensures that word is not repeated twice
		
		String URL = "https://www.wikipedia.org/";
		Document doc = Jsoup.connect(URL).get(); 
		// get the title of the current webpage
		String title = doc.title();
		Elements e = doc.select("body");
		
		String s = e.text();
		String[] arrOfStr = s.split(" ", 0); // o makes the split applied as many times as possible
		
		urlHash.put(title, null);
		PageData p = new PageData();
		p.setUrl(URL);
		ArrayList<String> a = new ArrayList<String>();
		a.add("hhf");	
		p.setInstancesInPage(a);
		p.setTf_idf(12);
		urlHash.put(title, p);
		
		int tf;
		Map<String,Integer> termFrequency = new HashMap<String,Integer>();
		
		// get set of words from the database
		
		for (String tag:tags) {
			doc.getElementsByTag(tag);
			System.out.println(tag + "**************************");
//			Elements e = doc.select("body");
			for (Element es: e) {
//				System.out.println(es.text());
				s = es.text();
				String[] wordsTemp = s.split(" ", 0); 
				for (String str: wordsTemp) {
					if (clean_str(str)) continue;  // key must not contain dot and intuitively words that contain dot must be splitted

					if (set.contains(str)) {
						if (termFrequency.get(str) != null) {
							termFrequency.put(str, termFrequency.get(str) + 1);
						}else {
							termFrequency.put(str, 0);
						}
					}else {
						set.add(str);
					}
				
				}
			}
			System.out.println("--------------------------------------------------");
		}
		
		
//		System.out.println(urlHash);
		for (String str: arrOfStr) {
			if (clean_str(str)) continue;  // key must not contain dot and intuitively words that contain dot must be splitted

			if (set.contains(str)) {
				if (termFrequency.get(str) != null) {
					termFrequency.put(str, termFrequency.get(str) + 1);
				}else {
					termFrequency.put(str, 0);
				}
			}else {
				set.add(str);
			}
		
		}
		// using for each loop
		System.out.println(set.size());
		System.out.println("-----------------------------");
		for (String word : set) {
			
			Indexer inde = new Indexer(word,urlHash);
			indexerRepo.insert(inde);
		}
			
			
//		indexerRepo.insert(wordsHash,"map-collection");
	    System.out.println(wordsHash);
//		hm2.put(URL, null);
//		indexerRepo.insert();
//		indexerRepo.save(new Indexer());
		
	}
	
	public void gettingValueIndexer() {
		List<Indexer> list =  indexerRepo.findAll();
		Iterator<Indexer> itr = list.iterator();
		indexerRepo.findAll();
		////// making priority queue and put page data that has the higher prioor title, url, instance in the webpage
//		while (itr.hasNext()) {
//			System.out.println(itr.next());
//		}
	}
	
}
