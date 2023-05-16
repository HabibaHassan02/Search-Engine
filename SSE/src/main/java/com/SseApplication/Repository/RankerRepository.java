package com.SseApplication.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.SseApplication.Entity.RankerEntity;

public interface RankerRepository extends MongoRepository<RankerEntity, String>{

}
