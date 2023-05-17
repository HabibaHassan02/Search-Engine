package com.SseApplication.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.SseApplication.Entity.User;
import com.SseApplication.Repository.UserRepository;
import com.SseApplication.Service.IndexerService;

@RestController
public class IndexerController {
	@Autowired
	private IndexerService indexerServ;

	@GetMapping("/indexer/addIndex")
	public void addToIndexer() throws IOException {
		 indexerServ.indexingProcess();
	}
	
	@GetMapping("/indexer/testResponse")
	public void test() throws IOException {
		 indexerServ.gettingValueIndexer();
	}
	
	@GetMapping("/indexer/addIdf")
	public void addIdf() throws IOException {
		 indexerServ.addIDF();
	}
}
