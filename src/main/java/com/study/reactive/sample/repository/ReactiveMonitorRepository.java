package com.study.reactive.sample.repository;

import com.study.reactive.sample.model.Metrics;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactiveMonitorRepository extends ReactiveElasticsearchRepository<Metrics, String> {
}
