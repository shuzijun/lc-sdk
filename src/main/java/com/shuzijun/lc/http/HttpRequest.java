package com.shuzijun.lc.http;

import com.shuzijun.lc.errors.LcException;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private final Type type;

    private final String url;

    private final String body;
    /**
     * POST
     */
    private final String contentType;

    private final Map<String, String> header;

    private HttpRequest(Type type, String url, String body, String contentType, Map<String, String> header) {
        this.type = type;
        this.url = url;
        this.body = body;
        this.contentType = contentType;
        this.header = header;
    }

    public static HttpRequestBuilder builderGet(String url) {
        return new HttpRequestBuilder().get(url);
    }

    public static HttpRequestBuilder builderPost(String url, String contentType) {
        return new HttpRequestBuilder().post(url, contentType);
    }

    public static HttpRequestBuilder builderPut(String url, String contentType) {
        return new HttpRequestBuilder().put(url, contentType);
    }

    public String getUrl() {
        return url;
    }

    public String getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        GET, POST, PUT;
    }

    public static class HttpRequestBuilder {
        private String url;

        private String body;
        /**
         * POST
         */
        private String contentType;

        private Type type;

        private Map<String, String> header = new HashMap<>();

        private HttpRequestBuilder() {

        }

        private HttpRequestBuilder get(String url) {
            this.url = url;
            this.type = Type.GET;
            return this;
        }

        private HttpRequestBuilder post(String url, String contentType) {
            this.url = url;
            this.contentType = contentType;
            this.type = Type.POST;
            return this;
        }

        private HttpRequestBuilder put(String url, String contentType) {
            this.url = url;
            this.contentType = contentType;
            this.type = Type.PUT;
            return this;
        }

        public HttpRequestBuilder body(String body) {
            this.body = body;
            return this;
        }

        public HttpRequestBuilder addHeader(Map<String, String> header) {
            this.header.putAll(header);
            return this;
        }

        public HttpRequestBuilder addHeader(String name, String value) {
            this.header.put(name, value);
            return this;
        }

        public HttpRequest build() {
            header.putIfAbsent("Referer", url);
            return new HttpRequest(type, url, body, contentType, header);
        }

        public HttpResponse request(ExecutorHttp executorHttp) throws LcException {
            HttpRequest httpRequest = build();
            HttpResponse httpResponse;
            switch (type) {
                case GET:
                    httpResponse = executorHttp.executeGet(httpRequest);
                    break;
                case POST:
                    httpResponse = executorHttp.executePost(httpRequest);
                    break;
                case PUT:
                    httpResponse = executorHttp.executePut(httpRequest);
                    break;
                default:
                    throw new LcException("Type not supported", HttpClient.buildHttpTrace(httpRequest, null));
            }
            httpResponse.setHttpRequest(httpRequest);
            return httpResponse;
        }

    }
}