package com.study.reactive.sample;

import com.study.reactive.sample.connector.MysqlDBConnectorImpl;
import com.study.reactive.sample.handler.MainHandler;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@EnableWebFlux
@Configuration
public class WebConfig extends DelegatingWebFluxConfiguration {

    @Value("${r2dbc.mysql.url}")
    private String dbHost;

    @Value("${r2dbc.mysql.user}")
    private String userName;

    @Value("${r2dbc.mysql.password}")
    private String password;

    @Value("${r2dbc.mysql.port}")
    private Integer port;

    @Autowired
    private MainHandler mainHandler;

    @Bean
    public RouterFunction<?> mainRouter() {
        return route(GET("/slow").and(accept(TEXT_PLAIN)), mainHandler::slow)
                .andRoute(GET("/fast").and(accept(TEXT_PLAIN)), mainHandler::fast)
                .andRoute(GET("/").and(accept(APPLICATION_JSON)), mainHandler::hello);
    }


    @Bean
    public WebClientCustomizer webClientCustomizer() {
        return webClientBuilder -> webClientBuilder.build();
    }

    @Bean
    public MysqlDBConnectorImpl mysqlDBConnector() {
        ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
                .option(DRIVER, "mysql")
                .option(HOST, dbHost)
                .option(USER, userName)
                .option(PORT, port)  // optional, default 3306
                .option(PASSWORD, password) // optional, default null, null means has no password
                .option(DATABASE, "sample") // optional, default null, null means not specifying the database
                .option(CONNECT_TIMEOUT, Duration.ofSeconds(3)) // optional, default null, null means no timeout
                .build();
        ConnectionFactory connectionFactory = ConnectionFactories.get(options);
        return new MysqlDBConnectorImpl(connectionFactory, 10);
    }

}