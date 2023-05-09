package dev.se.SSE;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org. jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawler {

    public static void main(String[] args) throws IOException {

        ReadSeed reader=new ReadSeed ();
        int counter=0;
        System.out.println(reader.seedsQeue);
        PrintWriter pw = new PrintWriter(new FileWriter("out.txt"));

        while(counter<=6000||reader.seedsQeue.isEmpty()) {
            String url = reader.seedsQeue.remove();


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

                    System.out.println(Integer.toString(counter)+":"+href);

                    counter=counter+1;
                    if(counter==6000)break;
                    reader.seedsQeue.add(href);
                    pw.write(href+"\n");


                    if(counter==687) {

                        System.out.println("test");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // TODO Auto-generated method stub

        }
        System.out.println(counter);

    }

}
