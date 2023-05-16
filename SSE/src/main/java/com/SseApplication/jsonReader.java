package com.SseApplication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class jsonReader {
    public static void main(String[] args) {
        File jsonFile = new File("Crawler.json");
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Read the JSON file into a JsonNode object
            JsonNode rootNode = objectMapper.readTree(jsonFile);

            // Iterate over each child node and print its properties
            for (JsonNode childNode : rootNode) {
                String id = childNode.get("link").asText();
                String name = childNode.get("html").asText();


                System.out.println(id);
                System.out.println(name);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}