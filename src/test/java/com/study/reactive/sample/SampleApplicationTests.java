package com.study.reactive.sample;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootTest
class SampleApplicationTests {

    @Test
    void contextLoads() throws InterruptedException {
        for(int i=0; i<100; i++) {
            Mono<ClientResponse> response = WebClient.create("http://localhost:15000")
                    .get()
                    .uri("/requestFast")
                    .exchange();
            response.log("fast").subscribe();
            Thread.sleep(10);
        }

        for(int i=0; i<30; i++) {
            Mono<ClientResponse> response = WebClient.create("http://localhost:15000")
                    .get()
                    .uri("/requestSlow")
                    .exchange();
            response.log("slow").subscribe();
        }

        Thread.sleep(100000);
    }

}
