package com.study.reactive.sample;

import com.study.reactive.sample.handler.MainHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;

import java.time.Duration;

import static org.springframework.http.MediaType.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@EnableWebFlux
@Configuration
public class WebConfig extends DelegatingWebFluxConfiguration {

    @Autowired
    private MainHandler mainHandler;

    @Bean
    public RouterFunction<?> mainRouter() {
        return route(GET("/slow").and(accept(APPLICATION_JSON)), mainHandler::slow)
                .andRoute(GET("/fast").and(accept(APPLICATION_JSON)), mainHandler::fast)
                .andRoute(GET("/requestFast").and(accept(APPLICATION_JSON)), mainHandler::requestFast)
                .andRoute(GET("/requestSlow").and(accept(APPLICATION_JSON)), mainHandler::requestSlow);
    }


    @Bean
    public WebClientCustomizer webClientCustomizer() {
        return webClientBuilder -> webClientBuilder.build();
    }

}