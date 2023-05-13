package com.SseApplication.Entity;

import java.lang.reflect.Array;
import java.util.ArrayList;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PageData {
	private String title;
	private String url;
	private int tf_idf;
	private ArrayList<String> cars;
}
