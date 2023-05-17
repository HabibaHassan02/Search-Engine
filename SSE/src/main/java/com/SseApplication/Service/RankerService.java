package com.SseApplication.Service;

import java.util.*;

import javafx.util.Pair;

import com.SseApplication.Entity.Indexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SseApplication.Entity.CrawlerEntity;
import com.SseApplication.Entity.PageData;
import com.SseApplication.Entity.RankerEntity;
import com.SseApplication.Repository.IndexerRepository;
import com.SseApplication.Repository.RankerRepository;
import com.SseApplication.Repository.WebCrawlerRepository;

@Service
public class RankerService {
	@Autowired
	IndexerRepository indexerRepo;
	
	@Autowired
	WebCrawlerRepository crawlerRepo;
	
	@Autowired
	RankerRepository rankerRepo;
	
	public void setPopularity() {
		List<CrawlerEntity> crawledPages= crawlerRepo.findAll();
		
		Map<String,Float> popularityMap = new  HashMap<String,Float>() ; 
		
		Map<String,Float> oldPopularityMap = new  HashMap<String,Float>() ; 
		
		float initialValue = 1;
		int numberIterations = 100;
		
		for (int i = 0 ; i < numberIterations ; i++) {
			if (i==0) {
				for (CrawlerEntity page : crawledPages) {
						for (String pointedToPages : page.getListOfLinks()) {
//							popularityMap.put(,initialValue)
							float popularityToBeInserted =  initialValue / (float) page.getListOfLinks().length;
							if (! popularityMap.containsKey(pointedToPages)) {
								popularityMap.put(pointedToPages,  popularityToBeInserted);	
							}else {
								float lastValue = 	popularityMap.get(pointedToPages);
								popularityMap.put(pointedToPages, lastValue + popularityToBeInserted);
							}
						}
				}
			}else {
				// get from the map calculated from previous iterations
				for (CrawlerEntity page : crawledPages) {
					for (String pointedToPages : page.getListOfLinks()) {
						float popularityToBeInserted;
						if (oldPopularityMap.containsKey(page.getLink()))
							popularityToBeInserted =  oldPopularityMap.get(page.getLink()) / (float) page.getListOfLinks().length;
						else
							popularityToBeInserted =  1 / (float) page.getListOfLinks().length;
						if (! popularityMap.containsKey(pointedToPages)) {
							popularityMap.put(pointedToPages,  popularityToBeInserted);	
						}else {
							float lastValue = 	popularityMap.get(pointedToPages);
							popularityMap.put(pointedToPages, lastValue + popularityToBeInserted);
						}
				}				
				}
			}
			// new iteration
			oldPopularityMap.putAll(popularityMap);
			popularityMap = new HashMap<String,Float>();
		}
		
		for (Map.Entry<String,Float> entry : oldPopularityMap.entrySet() ) {
			String word = entry.getKey();
			float popularity = (float) entry.getValue();
				
			rankerRepo.insert(new RankerEntity(word,popularity));
			
		}
		
		
	}
	public static String replaceHashTagsByDots(String str) {
		String returnedStr = str.replaceAll("#", "\\.");
		return returnedStr;
	}
	public PriorityQueue<Pair<Double, String>> Relevance(List<Indexer> documents){
		List<RankerEntity> ranker= rankerRepo.findAll();
		PriorityQueue<Pair<Double, String>> pq = new PriorityQueue<>(new Comparator<Pair<Double, String>>() {
			@Override
			public int compare(Pair<Double, String> p1, Pair<Double, String> p2) {
				return p1.getValue().compareTo(p2.getValue());
			}
		});
		for (Indexer word:documents){
			//double idf= word.getIdf();
			double idf=1;
			for (Map.Entry<String,PageData> entry : word.getHm().entrySet()){
				String link = entry.getKey();
				PageData data= entry.getValue();
				List<RankerEntity> rankentity=rankerRepo.findByWebsiteTitle(replaceHashTagsByDots(link));
				if (rankentity.size()>0) {
					float popularity = rankentity.get(0).getPopularity();
					double rank = idf * data.getTf() + popularity;
					pq.add(new Pair<Double,String>(rank, link));
				}
			}
		}
		return pq;
	}
}

