package com.study.reactive.sample.repository;

import com.study.reactive.sample.model.Metrics;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface MetricsRepository {
    Mono<Metrics> getCpuUsage();
    Mono<Metrics> getJvmMemoryUsage();
}
