package com.greennext.solarestimater.model.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ErrorResponseBody {
    @JsonAlias("err")
    private int errorCode;
    @JsonAlias("desc")
    private String description;
}
