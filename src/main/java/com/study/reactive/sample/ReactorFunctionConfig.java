package com.study.reactive.sample;

import com.study.reactive.sample.handler.MainHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@EnableWebFlux
@Configuration
public class ReactorFunctionConfig {


    @Bean
    public MainHandler mainHandler() {
        return new MainHandler();
    }

    @Bean
    public RouterFunction<?> mainRouter() {
        return route(GET("/slow").and(accept(TEXT_PLAIN)), mainHandler()::slow)
                .andRoute(GET("/fast").and(accept(TEXT_PLAIN)), mainHandler()::fast)
                .andRoute(GET("/").and(accept(APPLICATION_JSON)), mainHandler()::hello);
    }

}
