package com.SseApplication.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.SseApplication.Entity.IndexedPages;

public interface IndexedWebPages extends MongoRepository<IndexedPages,String>{

}
