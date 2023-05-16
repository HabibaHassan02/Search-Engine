package com.SseApplication.Service;

import com.SseApplication.Entity.Indexer;
import com.SseApplication.Repository.IndexedWebPages;
import com.SseApplication.Repository.IndexerRepository;
import com.SseApplication.Repository.WebCrawlerRepository;
import com.github.hamzamemon.porterstemmer.stemming.PorterStemmer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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



    ///////////////////////////////////////////////////////////////////////////////////////////

  public List<Indexer> search_in_indexer(String word)
  {
      if (word.startsWith("\"") && word.endsWith("\"")) {
          String phrase = word.substring(1, word.length() - 1);
          List<Indexer> result = indexerRepo.findByWord(phrase);
          return result;
      }
      String newword=clean_str(word);
      System.out.println("salmaaaaaaaa");
     List<String> newwords=tokenize_str(newword);
     List<String>filtered=remove_stopwords(newwords);
      List<Indexer> result = new ArrayList<>();
      for (String w : filtered)
      {
          System.out.println(w);
          String stemmedWord = PorterStemmer.stem(w);
          System.out.println(stemmedWord);
          List<Indexer> wordResult = indexerRepo.findByWord(stemmedWord);
          result.addAll(wordResult);
          
      }
      System.out.println("salmaaaaaaaa222222");
      return result;
  }



}
