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

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.*;
import javafx.util.Pair;


public class WebCrawler implements Runnable {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection;
    private HashSet<String> mapForLinksAndBodys;
    private ReadSeed reader;
    private PrintWriter pw;
    public Counter counter;

    public WebCrawler(MongoClient mongoClient, MongoClientURI uri, HashSet<String> mapForLinksAndBodys, ReadSeed reader, Counter counter) {
        this.mongoClient = mongoClient;
        this.mongoDatabase = mongoClient.getDatabase("Crawler");
        this.mongoCollection = mongoDatabase.getCollection("testing22");
        this.mapForLinksAndBodys = mapForLinksAndBodys;
        this.reader = reader;
        this.counter = counter;
    }

    @Override
    public void run() {
        int collesions = 0;
        synchronized (reader){
        if(!reader.contiuneQeue.isEmpty()&&reader.contiuneQeue.peek()!=null){
            reader.setSeedsQeue(reader.contiuneQeue);
            for(String code: reader.hashSetQeue){
                if(code==null)
                    continue;
                mapForLinksAndBodys.add(code);



            }
            counter.setCount(reader.count);
        }}

        while (counter.getCount() <= 6000|| reader.seedsQeue.isEmpty()) {
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
                url=UrlNormalizer.normalize(url);


                org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
                Elements links = doc.select("a[href]");
                //String title = doc.title();
                Element body = doc.body();
                String bodyText = body.text();
                //Elements paragraph = doc.select("p");
                //String paragraphText=paragraph.text();
                //Elements span = doc.select("span");
                //String spanText=span.text();
                //Elements headers = doc.select("h1,h2,h3,h4,h5,h6");
                //List<String> headerList = new ArrayList<>();
                //for (Element header : headers) {
                //  headerList.add(header.text());
                //}
                int compactUrl = bodyText.hashCode();

                    synchronized (mapForLinksAndBodys) {
                        if (mapForLinksAndBodys.contains(Integer.toString(compactUrl))) {
                            continue;
                        }
                        if (mapForLinksAndBodys.contains(url)) {
                            continue;
                        }

                        mapForLinksAndBodys.add(Integer.toString(compactUrl));

                        if (!mapForLinksAndBodys.contains(url)) {
                            mapForLinksAndBodys.add(url);
                    }
                    }
                List<String> disallowedLinks=new ArrayList<>();

                Document docToInsert = new Document();
                docToInsert.append("link", url);
                //docToInsert.append("title", title);
                //docToInsert.append("body", bodyText);
                //docToInsert.append("headers", headerList);
                //docToInsert.append("span", spanText);
                // docToInsert.append("p",paragraphText);
                //doc.select("*:not(h1):not(h2):not(h3):not(h4):not(h5):not(h6):not(header):no    t(p):not(span)").remove();
                //doc.select("link").remove();
                 /// doc.select("title").remove();
                //doc.select("meta").remove();
                doc.select("style").remove();
                doc.select("IMG").remove();
                doc.select("script").remove();
                doc.select("picture").remove();
                doc.select("footer").remove();
                //doc.select("li").remove();
                //doc.select("ul").remove();
                doc.select("pre").remove();
                doc.select("code").remove();
                doc.select("svg").remove();
                //doc.select("a[href]").remove();
                doc.select("nav").remove();
                //doc.select("head").remove();



                docToInsert.append("html",doc.html());

                //docToInsert.append("Document",doc.normalise().html()    );

                try(BufferedReader in = new BufferedReader(
                        new InputStreamReader(new URL(url+"/robots.txt").openStream()))) {
                    String line = null;
                    while((line = in.readLine()) != null) {
                        //System.out.println(line);
                        if (line.startsWith("Disallow: ")){
                            disallowedLinks.add(line.substring(10));
                        }
                    }
                } catch (IOException e) {
                    ///e.printStackTrace();
                }

                int limitLinksGot = 0;
                System.out.println(disallowedLinks);
                List<String>ListofLinks=new ArrayList<>();


                for (Element link : links) {
                    String href = link.attr("href");
                    if(disallowedLinks.contains(href)){

                        continue;
                    }

//                    synchronized (mapForLinksAndBodys) {
//                        mapForLinksAndBodys.add(href);
//                    }

                    if (href.isEmpty()) {
                        continue;
                    }

                    if (href.charAt(0)=='/') {
                        href = url + href;
                    }
                    if (href.charAt(0)=='?'||href.charAt(0)=='#'||href=="#") {
                        continue;
                    }
                    try {
                      href=UrlNormalizer.normalize(href);
                    }catch (Exception e){

                    }
//                    int compactUrl2 = href.hashCode();
                    if (mapForLinksAndBodys.contains(href)) {
                        collesions++;
                        continue;
                    }

//                    if (mapForLinksAndBodys.contains(compactUrl2)) {
//                        collesions++;
//                        continue;
//                    }
                    if (collesions > 10) {
                        break;
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
                    if(ListofLinks.contains(href))
                        continue;
                    ListofLinks.add(href);
                }



                docToInsert.append("listOfLinks",ListofLinks);
                mongoCollection.insertOne(docToInsert);








                collesions = 0;

                synchronized (counter) {
                    counter.increment();
                    System.out.println(Integer.toString(counter.getCount()) + ":" + url);
                }

            } catch (HttpStatusException e) {
                if (e.getStatusCode() == 429) {
                    System.err.println("429 error for URL: " + url);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } else if (e.getStatusCode() >= 400) {
                    collesions++;
                    System.err.println("404 error for URL: " + url);
                } else {
                    //  e.printStackTrace();
                }
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws IOException {

        MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017");
        MongoClient mongoClient;
        mongoClient = new MongoClient(uri);
        ReadSeed reader = new ReadSeed();
        HashSet<String> mapForLinksAndBodys = new HashSet<String>();
        Counter counter = new Counter();

        int numThreads = 10; // number of threads to create
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new WebCrawler(mongoClient, uri, mapForLinksAndBodys, reader, counter));
            threads[i].start();
        }
        Runtime.getRuntime().addShutdownHook(new shutdown(mapForLinksAndBodys,reader.seedsQeue,counter));


        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
        mongoClient.close();
        PriorityQueue<Pair<Integer, String>> pq =
                new PriorityQueue<>(Comparator.comparingInt(Pair::getKey));



    }
}