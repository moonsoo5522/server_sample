package com.study.reactive.sample.connector;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class MysqlDBConnectorImpl implements DBConnector {

    private ConnectionFactory connectionFactory;
    private Scheduler scheduler;
    private Queue<Connection> queue;

    public MysqlDBConnectorImpl(ConnectionFactory connectionFactory, int poolSize) {
        this.connectionFactory = connectionFactory;
        this.scheduler = Schedulers.newBoundedElastic(poolSize, poolSize, "mysqlconn");
        this.queue = new ArrayBlockingQueue<>(poolSize);

        initConnections(poolSize);
    }

    private void initConnections(int poolSize) {
        for(int i=0; i<poolSize; i++) {
            Connection connection = Mono.from(connectionFactory.create()).block();
            queue.add(connection);
        }
    }

    @Override
    public Mono<Connection> pool() {
        // return Mono.create()
        return Mono.just(queue).map((q) -> q.peek()).subscribeOn(scheduler).doOnSuccess(conn -> queue.add(conn));

    }
}
