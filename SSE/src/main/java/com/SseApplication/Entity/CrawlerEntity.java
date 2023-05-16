package com.SseApplication.Entity;

import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;


@AllArgsConstructor
@Document
public class CrawlerEntity {
	
	@Id
	ObjectId _id;
	String link;
	String Document;
	
	public ObjectId getId() {
		return _id;
	}


	public void setId(ObjectId id) {
		this._id = id;
	}


	public String getLink() {
		return link;
	}


	public void setLink(String link) {
		this.link = link;
	}


	public String getDocument() {
		return Document;
	}


	public void setDocument(String document) {
		Document = document;
	}




	
//	public CrawlerEntity(int _id,String link, String Document) {
//		// TODO Auto-generated constructor stub
//		System.out.println("hdddddddddddddddddddddddddddd");
//		this.link = link;
//		this.Document = Document;
//		this._id = _id;
//	}


}
