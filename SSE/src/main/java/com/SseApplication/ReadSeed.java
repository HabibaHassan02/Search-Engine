package com.SseApplication;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadSeed {

    Queue<String> seedsQeue = new LinkedList<> ();
    Queue<String> contiuneQeue = new LinkedList<> ();
    Queue<String> hashSetQeue = new LinkedList<> ();
    int count;
    BufferedReader reader;
    public  ReadSeed(){
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
        }
////////////////////////////////////////////////////////////
        try {
            reader = new BufferedReader(new FileReader("saveQeue.txt"));
            String line = reader.readLine();
            contiuneQeue.add(line);

            while (line != null) {
                //System.out.println(line);
                // read next line
                line = reader.readLine();
                contiuneQeue.add(line);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

///////////////////////////////////////////////////////////////

        try {
            reader = new BufferedReader(new FileReader("saveHashSet.txt"));
            String line = reader.readLine();
            hashSetQeue.add(line);

            while (line != null) {
                //System.out.println(line);
                // read next line
                line = reader.readLine();
                hashSetQeue.add(line);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ////////////////////////////////////////////////////
        try {
            reader = new BufferedReader(new FileReader("counter.txt"));
            String line = reader.readLine();
            if(line!=null)
            count=Integer.valueOf(line);

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
public void setSeedsQeue(Queue<String> contiuneQeue){
        this.seedsQeue=contiuneQeue;


}

}
