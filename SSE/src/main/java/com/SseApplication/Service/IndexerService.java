package com.SseApplication.Service;

import com.SseApplication.Entity.CrawlerEntity;
import com.SseApplication.Entity.IndexedPages;
import com.SseApplication.Entity.Indexer;
import com.SseApplication.Entity.PageData;
import com.SseApplication.Repository.IndexedWebPages;
import com.SseApplication.Repository.IndexerRepository;
import com.SseApplication.Repository.WebCrawlerRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class IndexerService {
	@Autowired
	IndexerRepository indexerRepo;
	
	@Autowired
	WebCrawlerRepository crawlerRepo;
	
	@Autowired
	IndexedWebPages indexedPagesRepo;
	
	PageData p0;
	
	
	enum myTags{
		h1,
		h2,
		h3,
		h4,
		h5,
		h6,
		p,
		span
	}
	Map <String,Integer> tagsLocation = new HashMap<String,Integer>();
	
	// open stop words here
	// stop words file contain words that are seperated by comma
	//TODO
	void populateTagsLocation() {
		tagsLocation.put("h1", 0);
		tagsLocation.put("h2", 1);
		tagsLocation.put("h3", 2);
		tagsLocation.put("h4", 3);
		tagsLocation.put("h5", 4);
		tagsLocation.put("h6", 5);
		tagsLocation.put("p", 6);
		tagsLocation.put("span", 7);
	}
	
	public static String clean_str(String str) {
		
		String returnedStr = str.replaceAll(">", "");
		returnedStr = returnedStr.replaceAll("[\\]\\|\\[\\@\\,\\$\\%\\*\\&\\\\\\(\\)\\\":]", "");
		returnedStr = returnedStr.replaceAll("\\.+", "");
		
	    return returnedStr.toLowerCase();
	}
	
	public  void addToIndexer_3(String URL, String documentArg) throws IOException {
		
		Map<String,Map<String,PageData>> wordsHash = new HashMap<String,Map<String,PageData>>();  // hashmap for word and its map
		Set<String> set = new HashSet<>(); //set ensures that word is not repeated twice
		Document doc = Jsoup.parse(documentArg);
		System.out.println();
		
		Map<String,String[]> wordInstanceDocument = new HashMap<String,String[]>();  // contains the instance of the word in the document
		String title = doc.title();
		if (indexedPagesRepo.existsById(title)) return;  // return from function
		else indexedPagesRepo.insert(new IndexedPages(title));
		PageData p = new PageData();
		int tf;
		Map<String,Integer> termFrequency = new HashMap<String,Integer>();
		String tags []= {"h1","h2","h3","h4","h5","h6","p","span"};
		// get set of words from the database
		int tempTotalNumberWords = 0;
		for (String tag:tags) {
			Elements e = doc.getElementsByTag(tag);
			System.out.println(tag + "**************************");
//			Elements e = doc.select("body");
			p = new PageData();  // new page data
			String [] tempArr = new String [8];
			p.setInstancesInPage(tempArr);
			for (Element es: e) {   //get all elements in the tag and convert to text
				String s = es.text();
				System.out.println(es.text());
				String[] wordsTemp = s.split(" ", 0); 
				
				for (String word: wordsTemp) {
					tempTotalNumberWords += 1;
					word = (clean_str(word));  // key must not contain dot and intuitively words that contain dot must be splitted
						
						if (termFrequency.get(word) != null) {
							termFrequency.put(word, termFrequency.get(word) + 1);
							String [] tempStrArr = wordInstanceDocument.get(word);
							tempStrArr[tagsLocation.get(tag)] = s;
							wordInstanceDocument.put(title, tempStrArr);
						}else {
							termFrequency.put(word, 1);
							String [] tempStrArr = new String[8];
							tempStrArr[tagsLocation.get(tag)] = s;
							wordInstanceDocument.put(word, tempStrArr);
						}	
				}
			}
			System.out.println("--------------------------------------------------");
		}
		
		for (Map.Entry<String,Integer> entry : termFrequency.entrySet() ) {
			String word = entry.getKey();
			Float localTF = (float) entry.getValue() / tempTotalNumberWords;
//			System.out.println(entry.getValue());
//			System.out.println(localTF.toString());
			String [] myInstances = wordInstanceDocument.get(word);
			// update or insert new
			p.setInstancesInPage(myInstances);
			p.setTf(localTF);
			if (indexerRepo.existsById(word)){
				List<Indexer> websitesMapList = indexerRepo.findByWord(word);
				Map<String,PageData> websitesMap = websitesMapList.get(0).getHm();
				websitesMap.put(title, p);
				 indexerRepo.save(new Indexer(word,websitesMap));
			}else {
				Map<String,PageData> websitesMap = new HashMap<String,PageData>();
				websitesMap.put(title, p);
				indexerRepo.insert(new Indexer(word,websitesMap));				
			}
		}
//		 Scanner myObj = new Scanner(System.in);  // Create a Scanner object
//		    System.out.println("Enter username");
//
//		    String userName = myObj.nextLine();  // Read user input
//		    System.out.println("Username is: " + userName);
	}
	
	
	
	
	void getCrawledPages () throws IOException {
		List<CrawlerEntity> listCrawledPages = crawlerRepo.findAll();
		System.out.println("Meeeeeeeeeeeeeeeeeeeeeeeeeeee");
		
		for (CrawlerEntity crawled : listCrawledPages) {
			System.out.println("Before");
			addToIndexer_3(crawled.getLink(), crawled.getDocument());
			System.out.println("After");

		}
	}
	
	public void indexingProcess() throws IOException {
		populateTagsLocation();
		getCrawledPages();
	}
	
	/////////////////////////
	/////////////////////////
	/////////////////////////
	/////////////////////////
	public  void addToIndexer_2() throws IOException {
		
//		getCrawledPages();
		String tags []= {"p","span","h1","h2","h3","h4","h5","h6"};
//		Map<String,PageData> websitesMap = new HashMap<String,PageData>();
		Map<String,Map<String,PageData>> wordsHash = new HashMap<String,Map<String,PageData>>();  // hashmap for word and its map
		Set<String> set = new HashSet<>(); //set ensures that word is not repeated twice
		String URL = "https://www.wikipedia.org/";
		Document doc2 = Jsoup.connect(URL).get(); 
//		crawlerRepo.insert(new CrawlerEntity(URL,doc2));
		Document doc = Jsoup.connect(URL).get(); 
		Map<String,String[]> wordInstanceDocument = new HashMap<String,String[]>();  // contains the instance of the word in the document
		String title = doc.title();
		if (indexedPagesRepo.existsById(title)) return;  // return from function
		else indexedPagesRepo.insert(new IndexedPages(title));
		
//		int totalNumberWords = doc.text().split(" ").length;  // total number of words in a document
		
		PageData p = new PageData();
		
		int tf;
		Map<String,Integer> termFrequency = new HashMap<String,Integer>();
		
		// get set of words from the database
		int tempTotalNumberWords = 0;
		for (String tag:tags) {
			Elements e = doc.getElementsByTag(tag);
			System.out.println(tag + "**************************");
//			Elements e = doc.select("body");
			p = new PageData();  // new page data
			String [] tempArr = new String [8];
			p.setInstancesInPage(tempArr);
			for (Element es: e) {   //get all elements in the tag and convert to text
//				System.out.println(es.text());
				String s = es.text();
				String[] wordsTemp = s.split(" ", 0); 
				
				for (String word: wordsTemp) {
					tempTotalNumberWords += 1;
					word = (clean_str(word));  // key must not contain dot and intuitively words that contain dot must be splitted
						
						if (termFrequency.get(word) != null) {
							termFrequency.put(word, termFrequency.get(word) + 1);
							String [] tempStrArr = wordInstanceDocument.get(word);
							tempStrArr[tagsLocation.get(tag)] = s;
							wordInstanceDocument.put(title, tempStrArr);
						}else {
							termFrequency.put(word, 1);
							String [] tempStrArr = new String[8];
							tempStrArr[tagsLocation.get(tag)] = s;
							wordInstanceDocument.put(word, tempStrArr);
						}
						
				}
			}
			System.out.println("--------------------------------------------------");
		}
		
		for (Map.Entry<String,Integer> entry : termFrequency.entrySet() ) {
			String word = entry.getKey();
			Float localTF = (float) entry.getValue() / tempTotalNumberWords;
//			System.out.println(entry.getValue());
//			System.out.println(localTF.toString());
			String [] myInstances = wordInstanceDocument.get(word);
			// update or insert new
			p.setInstancesInPage(myInstances);
			p.setTf(localTF);
			p.setUrl(URL);
			if (indexerRepo.existsById(word)){
				List<Indexer> websitesMapList = indexerRepo.findByWord(word);
				Map<String,PageData> websitesMap = websitesMapList.get(0).getHm();
				websitesMap.put(title, p);
				 indexerRepo.save(new Indexer(word,websitesMap));
			}else {
				Map<String,PageData> websitesMap = new HashMap<String,PageData>();
				websitesMap.put(title, p);
				indexerRepo.insert(new Indexer(word,websitesMap));				
			}
		}
	}
	
	///////////////////////////////////////////////////////////////
	public  void addToIndexer() throws IOException {
		String tags []= {"p","span","h1","h2","h3","h4","h5","h6"};
		Map<String,PageData> websitesMap = new HashMap<String,PageData>();
		Map<String,Map<String,PageData>> wordsHash = new HashMap<String,Map<String,PageData>>();  // hashmap for word and its map
		Set<String> set = new HashSet<>(); //set ensures that word is not repeated twice
		
		
		String URL = "https://www.wikipedia.org/";
		Document doc = Jsoup.connect(URL).get(); 
		// get the title of the current webpage
		String title = doc.title();
		
		websitesMap.put(title, null);
		
		PageData p = new PageData();
		p.setUrl(URL);
//		ArrayList<String> a = new ArrayList<String>();
//		a.add("hhf");	
//		p.setInstancesInPage(a);
		p.setTf_idf(12);
		
		websitesMap.put(title, p);
		
		int tf;
		Map<String,Integer> termFrequency = new HashMap<String,Integer>();
		
		// get set of words from the database
		
		for (String tag:tags) {
			Elements e = doc.getElementsByTag(tag);
			System.out.println(tag + "**************************");
//			Elements e = doc.select("body");
			p = new PageData();  // new page data
			String [] tempArr = new String [6];
			p.setInstancesInPage(tempArr);
			for (Element es: e) {   //get all elements in the tag and convert to text
//				System.out.println(es.text());
				String s = es.text();
				String[] wordsTemp = s.split(" ", 0); 
				
				for (String str: wordsTemp) {
					str = (clean_str(str));  // key must not contain dot and intuitively words that contain dot must be splitted
					List<Indexer> response = indexerRepo.findByWord(str);
					if (response.isEmpty()) {
						// not found then insert in database-
						String instance = e.text();
						
						if (termFrequency.get(str) != null) {
							termFrequency.put(str, termFrequency.get(str) + 1);
						}else {
							termFrequency.put(str, 1);
						}
						
						p.setInstancesInPage(tags);
						tempArr[myTags.h1.ordinal()] = instance;
						websitesMap.get(title).setInstancesInPage(tempArr);
//						indexerRepo.insert(new Indexer(str,websitesMap));
					}else {
//						Map<String,PageData> currentWordIndex   = response.get(0).getHm();
//						wordsHash.put(str, currentWordIndex);
						termFrequency.put(str, 1);
					}
					
				
				}
			}
			System.out.println("--------------------------------------------------");
		}
		// calculate tf 

//		for (String str: arrOfStr) {
//			if (clean_str(str)) continue;  // key must not contain dot and intuitively words that contain dot must be splitted
//
//			if (set.contains(str)) {
//				if (termFrequency.get(str) != null) {
//					termFrequency.put(str, termFrequency.get(str) + 1);
//				}else {
//					termFrequency.put(str, 0);
//				}
//			}else {
//				set.add(str);
//			}	
//		}
//		// using for each loop
//		System.out.println(set.size());
//		System.out.println("-----------------------------");
//		for (String word : set) {
//			
//			Indexer inde = new Indexer(word,urlHash);
//			indexerRepo.insert(inde);
//		}
//			
//			
////		indexerRepo.insert(wordsHash,"map-collection");
//	    System.out.println(wordsHash);
//		hm2.put(URL, null);
//		indexerRepo.insert();
//		indexerRepo.save(new Indexer());
		
	}
	
	public void gettingValueIndexer() {
//		Map<String, PageData> list =  indexerRepo.findAll();
//		Iterator<Indexer> itr = list.iterator();
		List<Indexer> res = indexerRepo.findByWord("Save");
//		res.get(0)
		System.out.println(res.size());
		if (!res.isEmpty()) {
			for (int i = 0; i < res.size(); i++) {
//		for ( Map<String,PageData> m : res) {
//			System.out.println(l);
				Map<String,PageData> m = res.get(i).getHm();
				for (PageData web : m.values()) {
					web.print();
				}
			
		}
		}else {
			System.out.println("NOOOOOOOOOOOOO");
		}
		////// making priority queue and put page data that has the higher prioor title, url, instance in the webpage
//		while (itr.hasNext()) {
//			System.out.println(itr.next());
//		}
	}
	
}
