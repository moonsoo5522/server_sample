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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MetricsRepositoryImplTest {

    @Autowired
    private MetricsRepository metricsRepository;

    @Test
    public void getCpuUsage() {
        Mono<Metrics> cpuUsage = metricsRepository.getCpuUsage();
        Metrics block = cpuUsage.block();


        assertThat(block.getName()).isEqualTo("process.cpu.usage");
        assertThat(block.getValue()).isPositive();
        assertThat(block.getRegDt()).isCloseTo(new Date(), 1000L);
    }

    @Test
    public void getJvmMemoryUsage() {
        Mono<Metrics> cpuUsage = metricsRepository.getJvmMemoryUsage();
        Metrics block = cpuUsage.block();

        assertThat(block.getName()).isEqualTo("jvm.memory.used");
        assertThat(block.getValue()).isPositive();
        assertThat(block.getRegDt()).isCloseTo(new Date(), 1000L);
    }
}