package com.study.reactive.sample.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public class ConsoleMonitorRepository implements MonitorRepository {

    @Override
    public void sendLog(String log) {
        System.out.println(log);
    }
}
