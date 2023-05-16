//package com.SseApplication.Controller;
//
//import java.io.IOException;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
////import com.SseApplication.Entity.CrawlerEntity;
//import com.SseApplication.Entity.User;
//import com.SseApplication.Repository.WebCrawlerRepository;
//import com.SseApplication.Service.WebCrawlerService;
//
//@RestController
//public class WebCrawlerController {
//	@Autowired
//	private WebCrawlerService crawlerService;
//	
//	// Save method is predefine method in Mongo Repository
//	// with this method we will save user in our database
//	
//	@GetMapping("/crawler/start")
//	public int startCrawling() throws IOException {
//		return crawlerService.crawling();
//	}
//	
//	// findAll method is predefine method in Mongo Repository
//	// with this method we will all user that is save in our database
////	@GetMapping("/getAllCrawls")
////	public List<CrawlerEntity> getAllCrawls(){
////		return crawlerService.findAll();
////	}
//}
