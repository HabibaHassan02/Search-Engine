package com.SseApplication.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.SseApplication.Entity.User;
import com.SseApplication.Repository.UserRepository;

public class IndexerController {
	@Autowired
	private UserRepository userRepo;

	@PostMapping("/addUser")
	public User addUser(@RequestBody User user) {
		return userRepo.save(user);
	}
	
	
}
