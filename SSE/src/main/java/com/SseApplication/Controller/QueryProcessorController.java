package com.SseApplication.Controller;

import com.SseApplication.Entity.Indexer;
import com.SseApplication.Service.QueryProcessorService;
import com.SseApplication.Service.RankerService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.PriorityQueue;

@RestController
public class QueryProcessorController {
    @Autowired
    private QueryProcessorService queryservice;

    @Autowired
    private RankerService rankerserv;
    @GetMapping("/queryprocessor/search/{query}")
    public PriorityQueue<Pair<Double, String>> search(@PathVariable String query) {
        List<Indexer> result = queryservice.search_in_indexer(query);
        PriorityQueue<Pair<Double, String>> res= rankerserv.Relevance(result);
        return res;
    }




}
