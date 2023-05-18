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
	public PriorityQueue<Pair<Double, String[]>> Relevance(List<Indexer> documents){
		List<RankerEntity> ranker= rankerRepo.findAll();
		PriorityQueue<Pair<Double, String[]>> pq = new PriorityQueue<>(new Comparator<Pair<Double, String[]>>() {
			@Override
			public int compare(Pair<Double, String[]> p1, Pair<Double, String[]> p2) {
				return Double.compare(p2.getKey(), p1.getKey()); //sort priority queue in descending order according to the Rank value in the key
			}
		});
		for (Indexer word:documents){
			double idf= word.getIdf();
			for (Map.Entry<String,PageData> entry : word.getHm().entrySet()){
				String link = entry.getKey(); //get the link that this word is mentioned in it
				PageData data= entry.getValue(); //get the data of the website: tf, title, popularity and instances
				int pri_acc_to_place=1;
				String[] instances= data.getInstancesInPage();
				String stringOfInstance="";
				for (int i=0; i<8 ;i++){
					if (instances[i]!=null) //the highest pri is h1: of index 0, so priority = 9 minus index so the highest priority=9 and the smallest=1
					{
						pri_acc_to_place = 9 - i;
						stringOfInstance=instances[i];
						break;
					}
				}
				List<RankerEntity> rankentity=rankerRepo.findByWebsiteTitle(replaceHashTagsByDots(link));
				if (rankentity.size()>0) {
					float popularity = rankentity.get(0).getPopularity();
					double rank = (double)(idf * data.getTf() + popularity)*pri_acc_to_place;
					String[]dataArray={replaceHashTagsByDots(link),data.getTitle(),stringOfInstance};
					pq.add(new Pair<Double,String[]>(rank,dataArray));
				}
			}
		}
		List<String> resultURL = new ArrayList<>();
		List<String> resultTitle = new ArrayList<>();
		List<String> resultInstance = new ArrayList<>();
		System.out.println(pq.size());

//		if(!pq.isEmpty()) {
//
//			for (Pair<Double, String[]> pairobject : pq) {
//				resultURL.add(replaceHashTagsByDots(pairobject.getValue()));
//				//resultTitle.add();
//			}
//		}
//		org.bson.Document docToInsert = new org.bson.Document();
//		docToInsert.append("links",resultURL);
//		docToInsert.append("titles",resultTitle);


		return pq;
	}
}

