package com.SseApplication.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.SseApplication.Entity.User;

public interface UserRepository extends MongoRepository<User,String>{

}
