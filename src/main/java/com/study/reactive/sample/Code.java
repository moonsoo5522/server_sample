package com.study.reactive.sample;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Getter
@Setter
@Table("code")
public class Code {

    @Id
    private String code;

    private String name;
}
