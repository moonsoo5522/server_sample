package com.study.reactive.sample.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Document(indexName = "testindex")
@Getter
public class Metrics {

    @Id
    private String id;

    private String name;

    private double value;

    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date regDt;

    public Metrics(String name, double value, Date regDt) {
        this.name = name;
        this.value = value;
        this.regDt = regDt;
    }
}
