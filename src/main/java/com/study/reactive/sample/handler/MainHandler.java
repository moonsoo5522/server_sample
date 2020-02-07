package com.study.reactive.sample.handler;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Component
public class MainHandler {

    @Autowired
    private WebClient.Builder webClientBuilder;


    public Mono<ServerResponse> requestFast(ServerRequest request) {
        WebClient webClient = webClientBuilder.build();
        return ServerResponse.ok().body(
                webClient.get()
                        .uri(URI.create("http://fast:15001/fast"))
                        .exchange()
                        .flatMap(clientResponse -> clientResponse.bodyToMono(String.class))
                , String.class);
    }

    public Mono<ServerResponse> requestSlow(ServerRequest request) {
        WebClient webClient = webClientBuilder.build();
        return ServerResponse.ok().body(
                webClient.get()
                        .uri(URI.create("http://slow:15002/slow"))
                        .exchange()
                        .flatMap(clientResponse -> clientResponse.bodyToMono(String.class))
                , String.class);
    }

    public Mono<ServerResponse> slow(ServerRequest request) {
        return Mono.defer(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return ServerResponse.ok().build();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<ServerResponse> fast(ServerRequest request) {
        return ServerResponse.ok().build();
    }
}
