package com.SseApplication.Entity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PageData {
//	private String title;
	private String title;
//	private float tf_idf;
	private String instancesInPage[];
	private float tf;
	private float popularity;
	
	public float getTf() {
		return tf;
	}
	public void setTf(float tf) {
		this.tf = tf;
	}

//	public float getTf_idf() {
//		return tf_idf;
//	}
//	public void setTf_idf(float tf_idf) {
//		this.tf_idf = tf_idf;
//	}

	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public float getPopularity() {
		return popularity;
	}
	public void setPopularity(float popularity) {
		this.popularity = popularity;
	}
	public String[] getInstancesInPage() {
		return instancesInPage;
	}
	public void setInstancesInPage(String[] instancesInPage) {
		this.instancesInPage = instancesInPage;
	}
}
