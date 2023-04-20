package com.skyband.ecr.sdk.api.net;

import lombok.Getter;
@Getter
public class HostException extends Exception {
    private int statusCode;
    private String statusMessage;

    public HostException(int statusCode, String statusMessage) {
        super();
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}
