package com.study.reactive.sample.handler;

import com.study.reactive.sample.Code;
import com.study.reactive.sample.repository.CodeRepository;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class MainHandler {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private CodeRepository codeRepository;

    @Value("${remote.api.fast}")
    private String fastHost;

    @Value("${remote.api.slow}")
    private String slowHost;

    @Value("${server.port}")
    private Integer port;


    // front
    public Mono<ServerResponse> hello(ServerRequest request) {
        String scode = request.queryParam("code").orElse(null);
        Mono<Code> code = codeRepository.findById(scode);
        return ServerResponse.ok().body(request(code), String.class);
    }

    public Mono<String> request(Mono<Code> code) {
        return code.flatMap(data -> {
            WebClient webClient = webClientBuilder.build();
            return webClient.get()
                    .uri(URI.create(Optional.of(data.getName())
                                .filter(name -> name.equals("fast"))
                                .map(name2 -> fastHost + ":" + port + "/fast")
                                .orElse(slowHost + ":" + port + "/slow")))
                    .exchange()
                    .flatMap(clientResponse -> clientResponse.bodyToMono(String.class));
        });

    }

    public Mono<ServerResponse> slow(ServerRequest request) {
        return Mono.defer(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return ServerResponse.ok().body(Mono.just("slow"), String.class);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<ServerResponse> fast(ServerRequest request) {
        return ServerResponse.ok().body(Mono.just("fast"), String.class);
    }
}
