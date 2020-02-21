package com.study.reactive.sample;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Code {
    private String code;
    private String name;
}
