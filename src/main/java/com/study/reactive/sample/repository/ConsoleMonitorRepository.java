package com.study.reactive.sample.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.reactive.sample.model.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public class ConsoleMonitorRepository implements MonitorRepository {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void sendLog(Metrics metrics) {
        try {
            System.out.println(objectMapper.writeValueAsString(metrics));
        } catch (JsonProcessingException e) {
            // TODO LOG
            e.printStackTrace();
        }
    }
}
