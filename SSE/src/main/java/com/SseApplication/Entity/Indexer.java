package com.SseApplication.Entity;

import java.util.HashMap;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Indexer {
	@Id
	String indexerId;
	HashMap<String, String> hm;
	public Indexer() {
		hm = new HashMap<>();
	}
}
