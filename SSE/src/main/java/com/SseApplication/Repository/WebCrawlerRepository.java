package com.SseApplication.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.SseApplication.Entity.CrawlerEntity;
//import com.SseApplication.Entity.WebCrawlerEntity;

public interface WebCrawlerRepository extends MongoRepository<CrawlerEntity,String>{

}
