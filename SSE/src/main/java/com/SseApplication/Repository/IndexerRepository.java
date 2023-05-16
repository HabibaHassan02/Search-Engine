package com.SseApplication.Repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.SseApplication.Entity.Indexer;
import com.SseApplication.Entity.PageData;

public interface IndexerRepository extends MongoRepository<Indexer,String>{
	
	@Query(value="{word:'?0'}", fields="{'hm' : 1}")
    List<Indexer> findByWord(String word);
//	void insert(Map<String, Map<String, PageData>> wordsHash, String string);

}
