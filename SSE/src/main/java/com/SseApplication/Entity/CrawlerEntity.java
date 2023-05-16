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
	String html;
	String [] listOfLinks;
	
	public ObjectId getId() {
		return _id;
	}





	public String[] getListOfLinks() {
		return listOfLinks;
	}


	public void setListOfLinks(String[] listOfLinks) {
		this.listOfLinks = listOfLinks;
	}


	public String getLink() {
		return link;
	}


	public void setLink(String link) {
		this.link = link;
	}


	public String getHtml() {
		return html;
	}


	public void setHtml(String html) {
		html = html;
	}




	
//	public CrawlerEntity(int _id,String link, String Document) {
//		// TODO Auto-generated constructor stub
//		System.out.println("hdddddddddddddddddddddddddddd");
//		this.link = link;
//		this.Document = Document;
//		this._id = _id;
//	}


}
