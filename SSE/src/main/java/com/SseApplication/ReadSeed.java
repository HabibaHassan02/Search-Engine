package com.SseApplication;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadSeed {

    Queue<String> seedsQeue = new LinkedList<> ();
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



    }


}
