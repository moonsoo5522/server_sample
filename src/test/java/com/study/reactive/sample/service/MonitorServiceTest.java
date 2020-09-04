package com.study.reactive.sample.service;

import com.study.reactive.sample.model.Metrics;
import com.study.reactive.sample.repository.MetricsRepository;
import com.study.reactive.sample.repository.ReactiveMonitorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonitorServiceTest {


    @InjectMocks
    private MonitorService monitorService;

    @Mock
    private MetricsRepository metricsRepository;

    @Mock
    private ReactiveMonitorRepository reactiveMonitorRepository;

    private Metrics metrics;
    private Metrics resultMetrics;


    @BeforeEach
    public void before() {
        metrics = new Metrics("name", 0.0, new Date());

        resultMetrics = new Metrics("result", 0.0, new Date());

    }

    @Test
    public void process() {
        when(metricsRepository.getCpuUsage()).thenReturn(Mono.just(metrics));
        when(metricsRepository.getJvmMemoryUsage()).thenReturn(Mono.just(metrics));
        when(reactiveMonitorRepository.save(metrics)).thenReturn(Mono.just(resultMetrics));

        Flux<Metrics> actual = monitorService.process();
        // collectList, iterator

        StepVerifier.create(actual)
                .expectNext(resultMetrics)
                .expectNext(resultMetrics)
                .expectComplete()
                .verify();
    }
}