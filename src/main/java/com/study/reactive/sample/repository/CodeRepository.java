package com.study.reactive.sample.repository;

import com.study.reactive.sample.Code;
import io.r2dbc.spi.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

import java.time.Duration;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Repository
public class CodeRepository {

    @Value("${r2dbc.mysql.url}")
    private String dbHost;

    @Value("${r2dbc.mysql.user}")
    private String userName;

    @Value("${r2dbc.mysql.password}")
    private String password;

    @Value("${r2dbc.mysql.port}")
    private Integer port;


    @PostConstruct
    public void postConstruct() {

    }

    public Mono<Code> getCode(String code) {
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

// Creating a Mono using Project Reactor
        Mono<Connection> connectionMono = Mono.from(connectionFactory.create());
        return connectionMono.flatMapMany(conn ->
                    Flux.from(conn.createStatement("SELECT * FROM code WHERE code = ?")
                            .bind(0, code)
                            .execute())
                            .flatMap(it ->
                                    it.map((row, rowMetadata) ->
                                                    Code.builder()
                                                    .code(row.get(0, String.class))
                                                    .name(row.get(1, String.class))
                                                    .build()
                                    )
                            )
        ).next();
    }
}
