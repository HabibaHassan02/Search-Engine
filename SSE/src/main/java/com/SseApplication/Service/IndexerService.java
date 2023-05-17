package com.SseApplication.Service;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.SseApplication.Entity.CrawlerEntity;
import com.SseApplication.Entity.IndexedPages;
import com.SseApplication.Entity.Indexer;
import com.SseApplication.Entity.PageData;
import com.SseApplication.Repository.IndexedWebPages;
import com.SseApplication.Repository.IndexerRepository;
import com.SseApplication.Repository.WebCrawlerRepository;
import com.github.hamzamemon.porterstemmer.stemming.PorterStemmer;

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
	
	private static Set<String> STOPWORDS = loadStopwordsFromFile("stop_words");

	
//	public static String stem(String word) {
//		//remove any spaces in the word
//		word= word.replaceAll("\\s", "");
//		PorterStemmer stemedWord=new PorterStemmer();
//		stemedWord.setCurrent(word);
//		stemedWord.stem();
//		System.out.println(stemedWord.getCurrent());
//		return stemedWord.getCurrent().toLowerCase();
//	}
	
    private static Set<String> loadStopwordsFromFile(String filename) {
        Set<String> stopwords = new HashSet<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                stopwords.add(line.trim());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopwords;
    }
	
	
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
	
	public static String removeDots(String str) {
		String returnedStr = str.replaceAll("\\.", "");
		return returnedStr;	
	}
	
	public static String replaceDotsByHahTags(String str) {
		String returnedStr = str.replaceAll("\\.", "#");
		return returnedStr;	
	}
	
	public  void addToIndexer_3(String URL, String documentArg) throws IOException {
		
//		 Scanner myObj = new Scanner(System.in);  // Create a Scanner object
//		    System.out.println("Enter username");
//
//		    String userName = myObj.nextLine();  // Read user input
//		    System.out.println("Username is: " + userName);
		
		Map<String,Map<String,PageData>> wordsHash = new HashMap<String,Map<String,PageData>>();  // hashmap for word and its map
		Set<String> set = new HashSet<>(); //set ensures that word is not repeated twice
		Document doc = Jsoup.parse(documentArg);
//		System.out.println(doc.text());
		String title = doc.title();
		title = removeDots(title);   ////////// Subject to  change
		Map<String,String[]> wordInstanceDocument = new HashMap<String,String[]>();  // contains the instance of the word in the document
//		System.out.println(title+"jfnththhtehe");
		if (indexedPagesRepo.existsById(URL)) return;  // return from function
		else indexedPagesRepo.insert(new IndexedPages(URL));
		PageData p = new PageData();
		int tf;
		Map<String,Integer> termFrequency = new HashMap<String,Integer>();
		String tags []= {"h1","h2","h3","h4","h5","h6","p","span"};
		// get set of words from the database
		int tempTotalNumberWords = 0;
//		 userName = myObj.nextLine();
		for (String tag:tags) {
			Elements e = doc.getElementsByTag(tag);
			System.out.println(tag + "**************************");
//			Elements e = doc.select("body");
			p = new PageData();  // new page data
			String [] tempArr = new String [8];
			p.setInstancesInPage(tempArr);
			for (Element es: e) {   //get all elements in the tag and convert to text
				String s = es.text();
//				System.out.println(es.text());
				String[] wordsTemp = s.split(" ", 0); 
				
				for (String word: wordsTemp) {
					tempTotalNumberWords += 1;
					word = PorterStemmer.stem(word);
					word = (clean_str(word));  // key must not contain dot and intuitively words that contain dot must be splitted
					if (STOPWORDS.contains(word) || word.equals("")) continue;
						if (termFrequency.get(word) != null) {
							termFrequency.put(word, termFrequency.get(word) + 1);
							String [] tempStrArr = wordInstanceDocument.get(word);
							tempStrArr[tagsLocation.get(tag)] = s;
							wordInstanceDocument.put(word, tempStrArr);
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
		
		
		URL = replaceDotsByHahTags(URL);
		for (Map.Entry<String,Integer> entry : termFrequency.entrySet() ) {
			String word = entry.getKey();
			Float localTF = (float) entry.getValue() / tempTotalNumberWords;
//			System.out.println(entry.getValue());
//			System.out.println(localTF.toString());
			String [] myInstances = wordInstanceDocument.get(word);
			// update or insert new
			p.setInstancesInPage(myInstances);
			p.setTf(localTF);
			p.setTitle(title);
			double idf = 0.0;
			if (indexerRepo.existsById(word)){
				List<Indexer> websitesMapList = indexerRepo.findByWord(word);
				Map<String,PageData> websitesMap = websitesMapList.get(0).getHm();
				websitesMap.put(URL, p);
				 indexerRepo.save(new Indexer(word,websitesMap, idf));
			}else {
				Map<String,PageData> websitesMap = new HashMap<String,PageData>();
				websitesMap.put(URL, p);
//				System.out.println("Hiliorggngd");
				indexerRepo.insert(new Indexer(word,websitesMap, idf));				
			}
		}
	}
	
	
	
	
	void getCrawledPages () throws IOException {
		List<CrawlerEntity> listCrawledPages = crawlerRepo.findAll();
		System.out.println("Meeeeeeeeeeeeeeeeeeeeeeeeeeee");
		
		int counter = 0;
		for (CrawlerEntity crawled : listCrawledPages) {
//			System.out.println("Before");
			addToIndexer_3(crawled.getLink(), crawled.getHtml());
//			System.out.println("After");
			counter += 1;

		}
		System.out.println(counter+"[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[");
	}
	
	public void indexingProcess() throws IOException {
		populateTagsLocation();
		STOPWORDS = loadStopwordsFromFile("stop_words");
		getCrawledPages();
//		addIDF();
	}
	
	public void addIDF() {
		long countCrawledPages = crawlerRepo.count();
		// iterate over indexer words to get the idf
		double idf;
		List<Indexer>  indexedPages= indexerRepo.findAll();
		for (Indexer page:indexedPages) {
			int numberPagesContainWord = page.getHm().size();
			idf =  Math.log(countCrawledPages/numberPagesContainWord);
			indexerRepo.save(new Indexer(page.getWord(),page.getHm(),idf));
		}
		
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
			p.setTitle(title);
			if (indexerRepo.existsById(word)){
				List<Indexer> websitesMapList = indexerRepo.findByWord(word);
				Map<String,PageData> websitesMap = websitesMapList.get(0).getHm();
				websitesMap.put(title, p);
				 indexerRepo.save(new Indexer(word,websitesMap,0));
			}else {
				Map<String,PageData> websitesMap = new HashMap<String,PageData>();
				websitesMap.put(title, p);
				indexerRepo.insert(new Indexer(word,websitesMap,0));		
//				indexerRepo.findStart
				indexerRepo.findByWordLike("io");
			}
		}
	}
	
	///////////////////////////////////////////////////////////////

	
	public void gettingValueIndexer() {
//		Map<String, PageData> list =  indexerRepo.findAll();
//		Iterator<Indexer> itr = list.iterator();
		
		List<Indexer> resOmar  = indexerRepo.findByWordLike("io");
		
		String m2 = resOmar.get(0).getWord();
		System.out.println(m2);
		
		
		
		List<Indexer> res = indexerRepo.findByWord("Save");
//		res.get(0)
		System.out.println(res.size());
		if (!res.isEmpty()) {
			for (int i = 0; i < res.size(); i++) {
//		for ( Map<String,PageData> m : res) {
//			System.out.println(l);
				Map<String,PageData> m = res.get(i).getHm();
				for (PageData web : m.values()) {
//					web.print();
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
