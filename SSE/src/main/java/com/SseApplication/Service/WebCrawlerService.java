package com.SseApplication.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SseApplication.Entity.WebCrawlerEntity;
import com.SseApplication.Repository.WebCrawlerRepository;

@Service
public class WebCrawlerService {
	@Autowired
	WebCrawlerRepository crawlerRepo;
	
	Queue<String> seedsQeue = new LinkedList<> ();
    BufferedReader reader;
    private int ReadSeed(){
        try {
            reader = new BufferedReader(new FileReader("seed.txt"));
            String line = reader.readLine();
            seedsQeue.add(line);

            while (line != null) {
                //System.out.println(line);
                // read next line
                line = reader.readLine();
                seedsQeue.add(line);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
   }
    
   public int crawling() throws IOException {
	   //ReadSeed reader=new ReadSeed ();
	   int status = ReadSeed();
	   if (status != 0) return 1;
       int counter=0;
//       System.out.println(seedsQeue);
       PrintWriter pw = new PrintWriter(new FileWriter("out.txt"));

       while(counter<=6000||seedsQeue.isEmpty()) {
           String url = seedsQeue.remove();
           if(url==null||url.isEmpty()) {
               continue;
           }
           if(url=="#") {
               continue;
           }
           try {
               Document doc = Jsoup.connect(url).get();
               Elements links = doc.select("a[href]");
               for (Element link : links) {
                   String href = link.attr("href");
                   if(href.charAt(0)=='/') {
                       href=url+href;
                   }
//                   System.out.println(Integer.toString(counter)+":"+href);
                   counter=counter+1;
                   if(counter==100) break;
                   seedsQeue.add(href);
                   pw.write(href+"\n");
                   crawlerRepo.save(new WebCrawlerEntity(href, href, false));
                   ///////////////////////////////
                   if(counter==687) {

                       System.out.println("test");
                   }
               }
           } catch (IOException e) {
               e.printStackTrace();
           }

       }
//       System.out.println(counter);
       return 0;
   }
   
   public List<WebCrawlerEntity> findAll() {
	   return crawlerRepo.findAll();
   }
}
