package com.shuzijun.lc;

import com.shuzijun.lc.http.ExecutorHttp;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class LcClientConfig {

    private final LcEndpoint endpoint;
    private final ExecutorHttp executorHttp;
    private final Map<String, String> defaultHeaders;

    private LcClientConfig(Builder builder) {
        this.endpoint = builder.endpoint;
        this.executorHttp = builder.executorHttp;
        this.defaultHeaders = Collections.unmodifiableMap(new LinkedHashMap<>(builder.defaultHeaders));
    }

    public static Builder builder(LcEndpoint endpoint) {
        return new Builder(endpoint);
    }

    public LcEndpoint getEndpoint() {
        return endpoint;
    }

    public ExecutorHttp getExecutorHttp() {
        return executorHttp;
    }

    public Map<String, String> getDefaultHeaders() {
        return defaultHeaders;
    }

    public static final class Builder {
        private final LcEndpoint endpoint;
        private ExecutorHttp executorHttp;
        private final Map<String, String> defaultHeaders = new LinkedHashMap<>();

        private Builder(LcEndpoint endpoint) {
            if (endpoint == null) {
                throw new IllegalArgumentException("Endpoint must not be null");
            }
            this.endpoint = endpoint;
        }

        public Builder executorHttp(ExecutorHttp executorHttp) {
            this.executorHttp = executorHttp;
            return this;
        }

        public Builder header(String name, String value) {
            if (name == null || value == null) {
                throw new IllegalArgumentException("Header name and value must not be null");
            }
            defaultHeaders.put(name, value);
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    header(entry.getKey(), entry.getValue());
                }
            }
            return this;
        }

        public LcClientConfig build() {
            return new LcClientConfig(this);
        }
    }
}
