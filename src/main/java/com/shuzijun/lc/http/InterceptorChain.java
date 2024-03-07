package com.shuzijun.lc.http;

import com.shuzijun.lc.errors.LcException;

import java.util.ArrayList;
import java.util.List;

public class InterceptorChain {

    private final List<HttpInterceptor> interceptors;
    private int currentIndex = 0;
    private final Function<HttpRequest, HttpResponse> executorHttp;

    public InterceptorChain(List<HttpInterceptor> interceptors, Function<HttpRequest, HttpResponse> executorHttp) {
        this.interceptors = new ArrayList<>(interceptors);
        this.executorHttp = executorHttp;
    }


    public HttpResponse proceed(HttpRequest request) throws LcException {
        if (currentIndex < interceptors.size()) {
            HttpInterceptor currentInterceptor = interceptors.get(currentIndex);
            currentIndex++;

            HttpInterceptor.InterceptorResult result = currentInterceptor.preHandle(request);
            if (result.isAbort()) {
                return result.respond();
            }
            HttpResponse response = proceed(request);
            currentInterceptor.postHandle(request, null);

            currentIndex--;
            return response;

        } else {
            return executorHttp.apply(request);
        }
    }

    @FunctionalInterface
    public interface Function<T, R> {
        R apply(T t) throws LcException;
    }
}