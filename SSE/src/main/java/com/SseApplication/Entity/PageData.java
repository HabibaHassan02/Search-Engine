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
	private String url;
	private float tf_idf;
	private String instancesInPage[];
	private float tf;
	private float popularity;
	
	public float getTf() {
		return tf;
	}
	public void setTf(float tf) {
		this.tf = tf;
	}
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

	
	public String[] getInstancesInPage() {
		return instancesInPage;
	}
	public void setInstancesInPage(String[] instancesInPage) {
		this.instancesInPage = instancesInPage;
	}
	public void print() {
		System.out.println(this.url);
		System.out.println(this.tf_idf);
	}
}
