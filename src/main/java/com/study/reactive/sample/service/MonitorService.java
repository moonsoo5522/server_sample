package com.study.reactive.sample.service;

import com.study.reactive.sample.model.Metrics;
import com.study.reactive.sample.repository.MetricsRepository;
import com.study.reactive.sample.repository.MonitorRepository;
import com.study.reactive.sample.repository.ReactiveMonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class MonitorService {

    @Autowired
    @Qualifier("ESMonitorRepository")
    private MonitorRepository monitorRepository;

    @Autowired
    private MetricsRepository metricsRepository;

    @Autowired
    private ReactiveMonitorRepository reactiveMonitorRepository;

    @PostConstruct
    public void postConstruct() {
        Flux.interval(Duration.ofSeconds(2))
                .log()
                .flatMap(d -> process())
                .subscribe(r -> System.out.println("sub: " + Thread.currentThread().getName()));
    }

    Flux<Metrics> process() {
        Mono<Metrics> cpuUsage = metricsRepository.getCpuUsage();
        Mono<Metrics> jvmMemoryUsage = metricsRepository.getJvmMemoryUsage();

        return Flux.merge(cpuUsage, jvmMemoryUsage)
                .flatMap(metrics -> reactiveMonitorRepository.save(metrics));
    }

    public Flux<Metrics> getMetricsList(int limit) {
        return null;
    }
}

