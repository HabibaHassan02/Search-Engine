package com.SseApplication.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.SseApplication.Entity.RankerEntity;

import java.util.List;

public interface RankerRepository extends MongoRepository<RankerEntity, String>{
    List<RankerEntity> findByWebsiteTitle(String websiteTitle);
}
