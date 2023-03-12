package com.shuzijun.lc.command;

import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.HttpClient;

public interface Command<T> {

    T execute(HttpClient client) throws LcException;
}
