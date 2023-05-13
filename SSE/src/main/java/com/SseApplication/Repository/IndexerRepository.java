package com.SseApplication.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.SseApplication.Entity.Indexer;

public interface IndexerRepository extends MongoRepository<Indexer,String>{

}
