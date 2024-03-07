package com.shuzijun.lc;

import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.HttpInterceptor;
import com.shuzijun.lc.http.HttpRequest;
import com.shuzijun.lc.http.HttpResponse;

public class HttpInterceptorTest implements HttpInterceptor {
    private String name;

    public HttpInterceptorTest(String name) {
        this.name = name;
    }


    @Override
    public InterceptorResult preHandle(HttpRequest request) throws LcException {
        System.out.println("test http interceptor preHandle :"+ name);
        return InterceptorResult.abort(new HttpResponse(200));
    }

    @Override
    public void postHandle(HttpRequest request, HttpResponse response) throws LcException {
        System.out.println("test http interceptor postHandle :"+ name);
    }
}
