package com.SseApplication.Entity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Document
@AllArgsConstructor
@NoArgsConstructor
public class Indexer {
	@Id
	String word;
	Map<String, PageData> hm;
	public Indexer(String word, Map<String, PageData> hm) {
		// TODO Auto-generated constructor stub
		this.word = word;
		this.hm = hm;
	}
//	public Indexer() {
//		hm = new HashMap<>();
//	}
}
