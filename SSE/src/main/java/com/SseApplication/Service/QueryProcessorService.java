package com.SseApplication.Service;

import com.SseApplication.Entity.Indexer;
import com.SseApplication.Entity.PageData;
import com.SseApplication.Repository.IndexedWebPages;
import com.SseApplication.Repository.IndexerRepository;
import com.SseApplication.Repository.WebCrawlerRepository;
import com.github.hamzamemon.porterstemmer.stemming.PorterStemmer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QueryProcessorService {
    @Autowired
    IndexerRepository indexerRepo;
    @Autowired
    WebCrawlerRepository crawlerRepo;
    @Autowired
    IndexedWebPages indexedPagesRepo;
    public static Set<String> loadStopwordsFromFile(String filename) {
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

    static final Set<String> STOPWORDS = loadStopwordsFromFile("stop_words");

    public static String clean_str(String str) {
        String returnedStr=str.replaceAll("\\s+", " ");
        returnedStr=returnedStr.trim();
        returnedStr = returnedStr.replaceAll(">", "");
        returnedStr = returnedStr.replaceAll("[\\]\\|\\[\\@\\,\\$\\%\\*\\&\\\\\\(\\)\\\":]", "");
        returnedStr = returnedStr.replaceAll("\\.+", "");
        return returnedStr.toLowerCase();
    }
    public static List<String> tokenize_str(String str) {
        List<String> tokens = new ArrayList<>();
        for(String s : str.split("\\s+"))
        {
            if (!s.isEmpty()) {
                tokens.add(s);
            }
        }
        return tokens;
    }

    public static List<String> remove_stopwords(List<String> str) {
        List<String> filteredWords = str.stream()
                .filter(word -> !STOPWORDS.contains(word.toLowerCase()))
                .collect(Collectors.toList());
        return filteredWords;
    }


	public static String replaceHahTagsByDots(String str) {
		String returnedStr = str.replaceAll("#", "\\.");
		return returnedStr;	
	}
	
    ///////////////////////////////////////////////////////////////////////////////////////////

    public List<Indexer> search_in_indexer(String word)
    {

        if (word.startsWith("\"") && word.endsWith("\"")) {
            String phrase = word.substring(1, word.length() - 1);
            phrase=phrase.trim();
            //phrase = clean_str(phrase);
            String[] phrases = phrase.split("\\s+");

            List<String> filteredd = Arrays.stream(phrases)
                    .filter(p -> !STOPWORDS.contains(phrases))
                    .collect(Collectors.toList());
            List<Indexer> result = new ArrayList<>();

            for (Indexer indexer : indexerRepo.findAll()) {
                String keyword=indexer.getWord();
                if(keyword.equals( PorterStemmer.stem(filteredd.get(0).toLowerCase())))
                {
                    Map<String, PageData> hm = indexer.getHm();
                    System.out.println(hm.keySet());
                    Indexer Ind2 = new Indexer();
                    
                    for (Map.Entry<String,PageData> entry : hm.entrySet())
                    {
                    	String URL = entry.getKey();
                    	URL = replaceHahTagsByDots(URL);
                    	PageData pageData = entry.getValue();
                        String[] instancesInPage = pageData.getInstancesInPage();

                        PageData newPageData = new PageData();
                        boolean foundMatching = false;
                        for(int i=0 ; i< instancesInPage.length;i++)
                        {
                            if(instancesInPage[i]!=null && instancesInPage[i].contains(phrase))
                            {
                                System.out.println("yessss");
                                Ind2.setWord(filteredd.get(0).toLowerCase());

                                newPageData.setTitle(pageData.getTitle());
                                newPageData.setTf(pageData.getTf());
                                /////////////////////////////
                                String [] instanceInThisPage = new String[8];
//                                instanceInThisPage = newPageData.getInstancesInPage();
                                instanceInThisPage[i] = instancesInPage[i];
                                newPageData.setInstancesInPage(instanceInThisPage);
                                /////////////////////////////
                                newPageData.setTf(pageData.getTf());
                                foundMatching = true;
                            }
                        }
                        if (foundMatching) {
                        	Map<String, PageData> localHash = new HashMap<String, PageData>(); ;
                        	if ( Ind2.getHm() != null ) {
                        		Ind2.getHm().put(URL,newPageData);
                        	}else {
                        		localHash.put(URL, newPageData);
                        		Ind2.setHm(localHash);
                        	}
                        }
                        	
                    }
                     result.add(Ind2);
                }
            }
            return result;
        }
        
        // Not phrase searching
        
        String newword=clean_str(word);
        System.out.println("salmaaaaaaaa");
        List<String> newwords=tokenize_str(newword);
        List<String>filtered=remove_stopwords(newwords);
        List<Indexer> result = new ArrayList<>();
        for (String w : filtered)
        {
            System.out.println(w);
            String stemmedWord = PorterStemmer.stem(w);

            System.out.println("stemmed is : ");

            System.out.println(stemmedWord);
            List<Indexer> wordResult = indexerRepo.findByWord(stemmedWord);
            result.addAll(wordResult);

        }

        return result;
    }
}