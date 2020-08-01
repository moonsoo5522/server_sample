package com.study.reactive.sample.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Metrics {
    private String name;
    private double value;

    public Metrics(String name, double value) {
        this.name = name;
        this.value = value;
    }
}
