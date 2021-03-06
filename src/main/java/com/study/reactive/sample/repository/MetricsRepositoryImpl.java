package com.study.reactive.sample.repository;

import com.study.reactive.sample.model.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class MetricsRepositoryImpl implements MetricsRepository {

    @Autowired
    private WebClient webClient;

    @Override
    public Mono<Metrics> getCpuUsage() {
        return webClient
                .get()
                .uri("http://localhost:5000/actuator/metrics/process.cpu.usage")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(d -> {
                    return d.bodyToMono(Map.class);
                })
                .map(res -> {
                    List<Map<String, Object>> measureList = (List<Map<String, Object>>)res.get("measurements");
                    String name = (String) res.get("name");
                    double cpuRate = (Double)measureList.get(0).get("value");
                    return new Metrics(name, cpuRate, new Date());
                });
    }

    @Override
    public Mono<Metrics> getJvmMemoryUsage() {
        return webClient
                .get()
                .uri("http://localhost:5000/actuator/metrics/jvm.memory.used")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(d -> d.bodyToMono(Map.class))
                .map(res -> {
                    List<Map<String, Object>> measureList = (List<Map<String, Object>>)res.get("measurements");
                    String name = (String) res.get("name");
                    double cpuRate = (Double)measureList.get(0).get("value");
                    return new Metrics(name, cpuRate, new Date());
                });
    }
}
