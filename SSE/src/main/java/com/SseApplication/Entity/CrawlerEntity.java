package com.SseApplication.Entity;

import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Document
public class CrawlerEntity {
	@Id
	int id;
	String link;
	myDocument Document;

	
	public CrawlerEntity(String link, myDocument Document) {
		// TODO Auto-generated constructor stub
		System.out.println("hdddddddddddddddddddddddddddd");
		this.link = link;
		this.Document = Document;
	}


}
