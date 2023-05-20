package com.SseApplication;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Queue;

class shutdown extends Thread {
    HashSet<String> mapForLinksAndBodys;
    Queue<String> seedsQeue ;
    Counter counter;
    public shutdown(HashSet<String> mapForLinksAndBodys, Queue<String> seedsQeue, Counter counter){
 this.mapForLinksAndBodys=mapForLinksAndBodys;
 this.seedsQeue=seedsQeue;
 this.counter=counter;


    }
    @Override
    public void run() {


        System.out.println("In shutdown hook");
        try {
            PrintWriter saveQeue = new PrintWriter(new FileWriter("saveQeue.txt"));
            synchronized (seedsQeue) {
                for (String link : seedsQeue) {
                    saveQeue.write(link + "\n");
                }
            }
                saveQeue.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            PrintWriter saveHashSet = new PrintWriter(new FileWriter("saveHashSet.txt"));
           synchronized (mapForLinksAndBodys){
            for(String link :mapForLinksAndBodys){
                saveHashSet.write(link+"\n");
            }}
            saveHashSet.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            PrintWriter saveCounter = new PrintWriter(new FileWriter("counter.txt"));
            synchronized (mapForLinksAndBodys){
                    saveCounter.write(counter.getCount()+"\n");
                }
            saveCounter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }




    }
}