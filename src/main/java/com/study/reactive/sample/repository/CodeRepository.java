package com.study.reactive.sample.repository;

import com.study.reactive.sample.Code;
import io.r2dbc.spi.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

import java.time.Duration;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Repository
public interface CodeRepository extends ReactiveCrudRepository<Code, String> {

}
