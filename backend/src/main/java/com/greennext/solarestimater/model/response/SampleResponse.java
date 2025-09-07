package com.greennext.solarestimater.model.response;

public class SampleResponse {
    private String message;
    private int code;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "SampleResponse{" +
                "message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}

