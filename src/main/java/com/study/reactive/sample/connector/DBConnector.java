package com.study.reactive.sample.connector;

import io.r2dbc.spi.Connection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DBConnector {

    Mono<Connection> pool();
}
