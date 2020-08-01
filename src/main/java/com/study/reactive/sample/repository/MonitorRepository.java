package com.study.reactive.sample.repository;

import com.study.reactive.sample.model.Metrics;

public interface MonitorRepository {

    void sendLog(Metrics metrics);
}
