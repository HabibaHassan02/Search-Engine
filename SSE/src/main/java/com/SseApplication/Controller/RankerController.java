package com.SseApplication.Controller;

import java.io.IOException;
import java.util.List;

import com.SseApplication.Entity.Indexer;
import com.SseApplication.Entity.RankerEntity;
import com.SseApplication.Repository.IndexerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SseApplication.Service.IndexerService;
import com.SseApplication.Service.RankerService;

@RestController
public class RankerController {
	@Autowired
	private RankerService rankerServ;
	@Autowired
	IndexerRepository indexerRepo;

	@GetMapping("/ranker/popularity")
	public void popularity() throws IOException {
		 rankerServ.setPopularity();
	}

	@GetMapping("/ranker/relevance")
	public void calculateRank() throws IOException {
		List<Indexer> doc= indexerRepo.findAll();
		rankerServ.Relevance(doc);
	}
}
