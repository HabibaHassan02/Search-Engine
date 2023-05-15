package com.SseApplication.Entity;

import java.lang.reflect.Array;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PageData {
//	private String title;
	private String url;
	private float tf_idf;
//	private int tf;
//	private float idf;
//	public int getTf() {
//		return tf;
//	}
//	public void setTf(int tf) {
//		this.tf = tf;
//	}
//	public float getIdf() {
//		return idf;
//	}
//	public void setIdf(float idf) {
//		this.idf = idf;
//	}
	private ArrayList<String> instancesInPage;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public float getTf_idf() {
		return tf_idf;
	}
	public void setTf_idf(float tf_idf) {
		this.tf_idf = tf_idf;
	}
	public ArrayList<String> getInstancesInPage() {
		return instancesInPage;
	}
	public void setInstancesInPage(ArrayList<String> instancesInPage) {
		this.instancesInPage = instancesInPage;
	}
}
