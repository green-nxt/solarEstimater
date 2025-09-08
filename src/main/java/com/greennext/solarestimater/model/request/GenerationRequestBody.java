package com.greennext.solarestimater.model.request;

import java.time.LocalDate;

public class GenerationRequestBody {

    String secret;
    String token;
    String pn;
    String sn;
    int devcode = 632;
    int devaddr = 1;
    LocalDate date;
    String salt;
}
