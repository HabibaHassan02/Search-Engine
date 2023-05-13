package dev.se.SSE;
import java.net.URL;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.HashSet;


public class WebCrawler implements Runnable {
    private HashSet<String> mapForLinksAndBodys;
    private ReadSeed reader;
    private PrintWriter pw;
    private Counter counter;

    public WebCrawler(HashSet<String> mapForLinksAndBodys, ReadSeed reader, PrintWriter pw, Counter counter) {
        this.mapForLinksAndBodys = mapForLinksAndBodys;
        this.reader = reader;
        this.pw = pw;
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
                Document doc = Jsoup.connect(url).get();
                Elements links = doc.select("a[href]");
                String title = doc.title();
                Element body = doc.body();
                String bodyText = body.text();

                synchronized (mapForLinksAndBodys) {
                    if (mapForLinksAndBodys.contains(bodyText)) {
                        continue;
                    }

                    mapForLinksAndBodys.add(bodyText);

                    if (!mapForLinksAndBodys.contains(links)) {
                        mapForLinksAndBodys.add(url);
                    }
                }

                synchronized (pw) {
                    pw.write("Link:" + url + "\n");
                    pw.write("Title: " + title + "\n");
                    pw.write("Body: " + bodyText + "\n");
                }

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
//                    URL obj = new URL(href);
//                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//                    con.setRequestMethod("HEAD");
//                    int responseCode = con.getResponseCode();
//
//                    if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
//                        // URL will return a 404 error
//                        collesions++;
//                        continue;
//
//                    }


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
    }

    public static void main(String[] args) throws IOException {
        ReadSeed reader = new ReadSeed();
        HashSet<String> mapForLinksAndBodys = new HashSet<String>();
        System.out.println(reader.seedsQeue);
        PrintWriter pw = new PrintWriter(new FileWriter("out.txt"));
        Counter counter = new Counter();

        int numThreads = 8; // number of threads to create
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new WebCrawler(mapForLinksAndBodys, reader, pw, counter));
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        pw.close();
    }
}