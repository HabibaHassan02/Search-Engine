package com.SseApplication.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SseApplication.Service.IndexerService;
import com.SseApplication.Service.RankerService;

@RestController
public class RankerController {
	@Autowired
	private RankerService rankerServ;

	@GetMapping("/ranker/testRanking")
	public void addToIndexer() throws IOException {
		 rankerServ.setPopularity();
	}
}
