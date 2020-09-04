package com.study.reactive.sample.handler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest
class MainHandlerTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    void hello() {
        webClient.get()
                .uri(uriBuilder ->
                    uriBuilder.path("/").queryParam("code", "fast").build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }
}