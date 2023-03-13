package com.shuzijun.lc.command;

import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.HttpClient;

public interface Command<T> {

    /**
     * 执行命令
     *
     * @param client httpclient
     * @return Command实现类返回的结果
     * @throws LcException 异常结果,返回结果异常时包含请求返回信息
     */
    T execute(HttpClient client) throws LcException;
}
