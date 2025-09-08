package com.greennext.solarestimater.model.response;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse implements WebClientResponseBody{

    @JsonAlias("err")
    private int errorCode;
    @JsonAlias("desc")
    private String description;
    @JsonAlias("dat")
    private AuthDataResponse data;
}
