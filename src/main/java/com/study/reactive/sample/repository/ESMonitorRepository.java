package com.study.reactive.sample.repository;

import org.springframework.stereotype.Repository;

@Repository
public class ESMonitorRepository implements MonitorRepository {

    @Override
    public void sendLog(String log) {

    }
}
