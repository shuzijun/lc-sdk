package com.shuzijun.lc;

import com.shuzijun.lc.command.Option;
import com.shuzijun.lc.command.OptionType;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.HttpInterceptor;
import com.shuzijun.lc.http.HttpRequest;
import com.shuzijun.lc.http.HttpResponse;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class RequestContext implements Option<HttpRequest.HttpRequestBuilder> {

    public static final RequestContext DEFAULT = builder().build();

    private final Map<String, String> headers;
    private final CancellationToken cancellationToken;

    private RequestContext(Builder builder) {
        this.headers = Collections.unmodifiableMap(new LinkedHashMap<>(builder.headers));
        this.cancellationToken = builder.cancellationToken;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public CancellationToken getCancellationToken() {
        return cancellationToken;
    }

    public void throwIfCancellationRequested() throws LcException {
        if (cancellationToken.isCancellationRequested()) {
            throw new LcException("Request cancelled");
        }
    }

    @Override
    public void Parse(HttpRequest.HttpRequestBuilder value) {
        value.addHeader(headers);
        value.addInterceptor(new HttpInterceptor() {
            @Override
            public InterceptorResult preHandle(HttpRequest request) throws LcException {
                throwIfCancellationRequested();
                return InterceptorResult.continueWith();
            }

            @Override
            public void postHandle(HttpRequest request, HttpResponse response) {
            }
        });
    }

    @Override
    public OptionType<HttpRequest.HttpRequestBuilder> Type() {
        return OptionType.httpRequest;
    }

    public static final class Builder {
        private final Map<String, String> headers = new LinkedHashMap<>();
        private CancellationToken cancellationToken = CancellationToken.NONE;

        private Builder() {
        }

        public Builder header(String name, String value) {
            if (name == null || value == null) {
                throw new IllegalArgumentException("Header name and value must not be null");
            }
            headers.put(name, value);
            return this;
        }

        public Builder headers(Map<String, String> values) {
            if (values != null) {
                for (Map.Entry<String, String> entry : values.entrySet()) {
                    header(entry.getKey(), entry.getValue());
                }
            }
            return this;
        }

        public Builder cancellationToken(CancellationToken cancellationToken) {
            this.cancellationToken = cancellationToken == null ? CancellationToken.NONE : cancellationToken;
            return this;
        }

        public RequestContext build() {
            return new RequestContext(this);
        }
    }
}
