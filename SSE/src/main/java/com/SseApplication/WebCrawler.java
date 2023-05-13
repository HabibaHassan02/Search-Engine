package com.SseApplication;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class WebCrawler implements Runnable {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection;
    private HashSet<String> mapForLinksAndBodys;
    private ReadSeed reader;
    private PrintWriter pw;
    private Counter counter;

    public WebCrawler(MongoClient mongoClient, MongoClientURI uri, HashSet<String> mapForLinksAndBodys, ReadSeed reader, Counter counter) {
        this.mongoClient = mongoClient;
        this.mongoDatabase = mongoClient.getDatabase("search-engine");
        this.mongoCollection = mongoDatabase.getCollection("Crawler");
        this.mapForLinksAndBodys = mapForLinksAndBodys;
        this.reader = reader;
        this.counter = counter;
    }

    @Override
    public void run() {
        int collesions = 0;

        while (counter.getCount() <= 6000 || reader.seedsQeue.isEmpty()) {
            String url = null;

            synchronized (reader) {
                if (!reader.seedsQeue.isEmpty()) {
                    url = reader.seedsQeue.remove();
                }
            }

            if (url == null || url.isEmpty() || !url.startsWith("http")) {
                continue;
            }

            if (url.equals("#")) {
                continue;
            }

            try {
                collesions = 0;
                org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
                Elements links = doc.select("a[href]");
                String title = doc.title();
                Element body = doc.body();
                String bodyText = body.text();
                Elements headers = doc.select("h1,h2,h3");
                List<String> headerList = new ArrayList<>();
                for (Element header : headers) {
                    headerList.add(header.text());
                }


                synchronized (mapForLinksAndBodys) {
                    if (mapForLinksAndBodys.contains(bodyText)) {
                        continue;
                    }

                    mapForLinksAndBodys.add(bodyText);

                    if (!mapForLinksAndBodys.contains(links)) {
                        mapForLinksAndBodys.add(url);
                    }
                }

                Document docToInsert = new Document();
                docToInsert.append("link", url);
                docToInsert.append("title", title);
                docToInsert.append("body", bodyText);
                docToInsert.append("headers", headerList);
                mongoCollection.insertOne(docToInsert);


                int limitLinksGot = 0;

                for (Element link : links) {
                    String href = link.attr("href");
                    if (mapForLinksAndBodys.contains(href)) {
                        collesions++;
                        continue;
                    }
                    if (collesions > 10) {
                        break;
                    }

                    synchronized (mapForLinksAndBodys) {
                        mapForLinksAndBodys.add(href);
                    }

                    if (href.isEmpty()) {
                        continue;
                    }

                    if (href.charAt(0) == '/') {
                        href = url + href;
                    }

                    if (counter.getCount() == 6000) {
                        break;
                    }

                    synchronized (reader) {
                        reader.seedsQeue.add(href);
                    }

                    limitLinksGot++;

                    if (limitLinksGot == 300) {
                        break;
                    }
                }

                collesions = 0;
                synchronized (counter) {
                    counter.increment();
                    System.out.println(Integer.toString(counter.getCount()) + ":" + url);
                }

            } catch (HttpStatusException e) {
                if (e.getStatusCode() == 429) {
                    System.err.println("429 error for URL: " + url);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } else if (e.getStatusCode() >= 400) {
                    collesions++;
                    System.err.println("404 error for URL: " + url);
                } else {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mongoClient.close();
    }

    public static void main(String[] args) throws IOException {
        MongoClientURI uri = new MongoClientURI("mongodb+srv://habibaelhussieny11:jOOr15g8khGvRca0@cluster0.eqvx01j.mongodb.net/");
        MongoClient mongoClient;
        mongoClient = new MongoClient(uri);
        ReadSeed reader = new ReadSeed();
        HashSet<String> mapForLinksAndBodys = new HashSet<String>();
        Counter counter = new Counter();

        int numThreads = 8; // number of threads to create
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new WebCrawler(mongoClient, uri, mapForLinksAndBodys, reader, counter));
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}