package com.SseApplication.Entity;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Document
@NoArgsConstructor
public class RankerEntity {
	
	@Id
	String websiteTitle;
	
	float popularity;
	
	
	public RankerEntity(String websiteTitle,  float popularity) {
		// TODO Auto-generated constructor stub
		this.websiteTitle = websiteTitle;
		this. popularity =  popularity;
	}
	
	public String getWebsiteTitle() {
		return websiteTitle;
	}

	public void setWebsiteTitle(String websiteTitle) {
		this.websiteTitle = websiteTitle;
	}

	public float getPopularity() {
		return popularity;
	}

	public void setPopularity(float popularity) {
		this.popularity = popularity;
	}
	
}
