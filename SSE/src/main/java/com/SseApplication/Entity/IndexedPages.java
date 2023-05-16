package com.SseApplication.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;

@Document
//@AllArgsConstructor
public class IndexedPages {
	@Id
	private String webSiteIndexed;
	
	public IndexedPages (String webSiteIndexed){
		this.webSiteIndexed = webSiteIndexed;
	}
	public String getWebSiteIndexed() {
		return webSiteIndexed;
	}

	public void setWebSiteIndexed(String webSiteIndexed) {
		this.webSiteIndexed = webSiteIndexed;
	}
}
