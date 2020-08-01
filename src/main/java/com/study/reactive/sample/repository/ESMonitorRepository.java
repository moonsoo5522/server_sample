package com.study.reactive.sample.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.reactive.sample.model.Metrics;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ESMonitorRepository implements MonitorRepository {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void sendLog(Metrics metrics) {
        IndexRequest indexRequest = new IndexRequest("textindex");
        try {
            indexRequest.source(objectMapper.writeValueAsString(metrics), XContentType.JSON);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("main : " +  Thread.currentThread());
        restHighLevelClient.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                System.out.println("worker : " + Thread.currentThread());
                System.out.println(indexResponse.status() + ", " + indexResponse.toString());
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
