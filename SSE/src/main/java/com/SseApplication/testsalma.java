package com.SseApplication;

import com.SseApplication.Service.QueryProcessorService;

public class testsalma {
    public static void main(String[] args) {

        QueryProcessorService q = new QueryProcessorService();
        q.search_in_indexer("cancel");
    }
}
