package com.estockmarket.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class GatewayResponse {

    private final String body;
    private final Map<String, String> headers;
    private final int statusCode;

    public GatewayResponse(final String body, final Map<String, String> headers, final int statusCode) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = Collections.unmodifiableMap(new HashMap<>(headers));
    }

}
