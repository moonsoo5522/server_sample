package com.study.reactive.sample.service;

import com.study.reactive.sample.repository.MonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private MonitorRepository monitorRepository;

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
        Mono<Map> r1 = WebClient.create("http://localhost:5000/actuator/metrics/process.cpu.usage")
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(d -> d.bodyToMono(Map.class))
                .subscribeOn(Schedulers.boundedElastic());

        Mono<Map> r2 = WebClient.create("http://localhost:5000/actuator/metrics/jvm.memory.used")
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(d -> d.bodyToMono(Map.class))
                .subscribeOn(Schedulers.boundedElastic());

        Flux.merge(r1, r2).subscribe(res -> {
            List<Map<String, Object>> measureList = (List<Map<String, Object>>)res.get("measurements");
            String name = (String) res.get("name");
            double cpuRate = (Double)measureList.get(0).get("value");

            monitorRepository.sendLog(name + " : " + cpuRate);
        });
    }
}
