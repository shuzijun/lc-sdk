package com.shuzijun.lc.command;

import com.shuzijun.lc.http.HttpRequest;

public class OptionType<T> {

    public static OptionType<HttpRequest.HttpRequestBuilder> httpRequest = OptionType.create("httpRequest");

    private final String name;
    private  OptionType(String name){
        this.name = name;
    }
    public static <T> OptionType<T> create(String name) {
         return new OptionType<>(name);
    }

    public boolean IsType(OptionType<?> type) {
        return this.name.equals(type.name);
    }
}
