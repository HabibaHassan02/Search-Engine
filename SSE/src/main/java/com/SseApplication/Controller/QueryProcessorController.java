package com.SseApplication.Controller;

import com.SseApplication.Entity.Indexer;
import com.SseApplication.Service.QueryProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QueryProcessorController {
    @Autowired
    private QueryProcessorService queryservice;

    @GetMapping("/queryprocessor/search/{query}")
    public List<Indexer> search(@PathVariable String query) {
        List<Indexer> result = queryservice.search_in_indexer(query);
        return result;
    }

}
