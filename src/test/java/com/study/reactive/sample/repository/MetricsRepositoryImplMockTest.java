package com.study.reactive.sample.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.reactive.sample.model.Metrics;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetricsRepositoryImplMockTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    private Metrics metrics;

    @BeforeEach
    public void before() {
        metrics = new Metrics("name", 1.0, new Date());

    }

    @InjectMocks
    private MetricsRepositoryImpl metricsRepository;

    @Test
    public void getCpuUsage() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://localhost:5000/actuator/metrics/process.cpu.usage")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.exchange()).thenReturn(
                Mono.just(
                        ClientResponse.create(HttpStatus.OK)
                                .header("Content-Type", "application/json")
                                .body("{\"name\":\"process.cpu.usage\",\"description\":\"The \\\"recent cpu usage\\\" for the Java Virtual Machine process\",\"baseUnit\":null,\"measurements\":[{\"statistic\":\"VALUE\",\"value\":1.0}],\"availableTags\":[]}")
                                .build()
                )
        );

        Metrics actual = metricsRepository.getCpuUsage().block();

        assertThat(actual.getName()).isEqualTo("process.cpu.usage");
        assertThat(actual.getValue()).isEqualTo(1.0);
        assertThat(actual.getRegDt()).isCloseTo(new Date(), 1000L);
    }

    @Test
    public void getJvmMemoryUsage() {

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://localhost:5000/actuator/metrics/jvm.memory.used")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.exchange()).thenReturn(
                Mono.just(
                        ClientResponse.create(HttpStatus.OK)
                                .header("Content-Type", "application/json")
                                .body("{\"name\":\"jvm.memory.used\",\"description\":\"The amount of used memory\",\"baseUnit\":\"bytes\",\"measurements\":[{\"statistic\":\"VALUE\",\"value\":2.23820384E8}],\"availableTags\":[{\"tag\":\"area\",\"values\":[\"heap\",\"nonheap\"]},{\"tag\":\"id\",\"values\":[\"G1 Old Gen\",\"CodeHeap 'non-profiled nmethods'\",\"G1 Survivor Space\",\"Compressed Class Space\",\"Metaspace\",\"G1 Eden Space\",\"CodeHeap 'non-nmethods'\"]}]}")
                                .build()
                )
        );

        Metrics actual = metricsRepository.getJvmMemoryUsage().block();

        assertThat(actual.getName()).isEqualTo("jvm.memory.used");
        assertThat(actual.getValue()).isEqualTo(2.23820384E8);
        assertThat(actual.getRegDt()).isCloseTo(new Date(), 1000L);
    }

}