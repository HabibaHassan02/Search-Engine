package com.SseApplication.Entity;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document
//@AllArgsConstructor
@NoArgsConstructor
//@Data
public class Indexer {
	@Id
	String word;
	Map<String, PageData> hm;  // map title to the data of the page title
	public Indexer(String word, Map<String, PageData> hm) {
		// TODO Auto-generated constructor stub
		this.word = word;
		this.hm = hm;
	}
//	public Indexer() {
//		hm = new HashMap<>();
//	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public Map<String, PageData> getHm() {
		return hm;
	}
	public void setHm(Map<String, PageData> hm) {
		this.hm = hm;
	}
}
