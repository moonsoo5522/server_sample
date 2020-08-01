package com.study.reactive.sample.service;

import com.study.reactive.sample.model.Metrics;
import com.study.reactive.sample.repository.MetricsRepository;
import com.study.reactive.sample.repository.MonitorRepository;
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

    @PostConstruct
    public void postConstruct() {
        Flux.interval(Duration.ofSeconds(2))
                .map(d -> {
                    process();
                    return d + 1;
                }).log().subscribe();
    }

    // @Scheduled(fixedDelay = 1000)
    private void process() {
        Mono<Metrics> cpuUsage = metricsRepository.getCpuUsage();
        Mono<Metrics> jvmMemoryUsage = metricsRepository.getJvmMemoryUsage();

        Flux.merge(cpuUsage, jvmMemoryUsage).subscribe(res -> {
            monitorRepository.sendLog(res);
        });
    }
}
