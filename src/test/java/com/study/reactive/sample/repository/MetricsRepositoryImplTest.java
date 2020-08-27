package com.study.reactive.sample.repository;

import com.study.reactive.sample.SampleApplication;
import com.study.reactive.sample.WebConfig;
import com.study.reactive.sample.model.Metrics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@WebFluxTest
public class MetricsRepositoryImplTest {

    @Autowired
    private MetricsRepository metricsRepository;

    @Autowired
    private ReactiveMonitorRepository reactiveMonitorRepository;

    @Test
    public void getCpuUsage() {
        Mono<Metrics> cpuUsage = metricsRepository.getCpuUsage();
        Metrics block = cpuUsage.block();
        System.out.println(block);
    }
}