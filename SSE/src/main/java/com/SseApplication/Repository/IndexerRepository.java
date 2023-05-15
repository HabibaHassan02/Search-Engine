package com.SseApplication.Repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.SseApplication.Entity.Indexer;
import com.SseApplication.Entity.PageData;

public interface IndexerRepository extends MongoRepository<Indexer,String>{
	@Query(fields="{'_id' : 1}")
    List<Indexer> findIndexers();
//	void insert(Map<String, Map<String, PageData>> wordsHash, String string);

}
