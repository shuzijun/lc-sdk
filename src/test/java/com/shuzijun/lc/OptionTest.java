package com.shuzijun.lc;

import com.shuzijun.lc.command.Option;
import com.shuzijun.lc.command.OptionType;
import com.shuzijun.lc.http.HttpRequest;

public class OptionTest implements Option<HttpRequest.HttpRequestBuilder> {

    private String name;

    public OptionTest(String name) {
        this.name = name;
    }

    @Override
    public void Parse(HttpRequest.HttpRequestBuilder value) {
         value.addInterceptor(new HttpInterceptorTest(name));
    }

    @Override
    public OptionType<HttpRequest.HttpRequestBuilder> Type() {
        return OptionType.httpRequest;
    }
}
