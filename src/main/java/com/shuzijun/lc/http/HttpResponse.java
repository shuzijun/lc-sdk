package com.shuzijun.lc.http;

public class HttpResponse {

    private final int statusCode;
    private HttpRequest httpRequest;
    private String body;

    public HttpResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public HttpResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public boolean isCodeSuccess() {
        return statusCode == 200;
    }
}
