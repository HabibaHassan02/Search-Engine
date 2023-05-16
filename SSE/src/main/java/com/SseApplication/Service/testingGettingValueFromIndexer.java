package com.SseApplication.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.SseApplication.Entity.PageData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class testingGettingValueFromIndexer {
	public static void main(String[] args) throws IOException {
		File jsonFile = new File("Crawler.json");
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Read the JSON file into a JsonNode object
            JsonNode rootNode = objectMapper.readTree(jsonFile);

            // Iterate over each child node and print its properties
            for (JsonNode childNode : rootNode) {
                String id = childNode.get("link").asText();
                String name = childNode.get("html").asText();


//                System.out.println(id);
//                System.out.println(name);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	  
}
