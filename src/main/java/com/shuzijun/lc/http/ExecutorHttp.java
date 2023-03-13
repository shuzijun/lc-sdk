package com.shuzijun.lc.http;

import com.shuzijun.lc.errors.LcException;

public interface ExecutorHttp {

    /**
     * 获取cookieStore
     *
     * @return {@link CookieStore} 存储cookie实现
     */
    public CookieStore cookieStore();

    /**
     * 执行get请求
     *
     * @param httpRequest 请求参数
     * @return {@link HttpResponse} 返回结果
     * @throws LcException
     */
    public HttpResponse executeGet(HttpRequest httpRequest) throws LcException;

    /**
     * 执行Post请求
     *
     * @param httpRequest 请求参数
     * @return {@link HttpResponse} 返回结果
     * @throws LcException
     */
    public HttpResponse executePost(HttpRequest httpRequest) throws LcException;

    /**
     * 执行PUT请求
     *
     * @param httpRequest 请求参数
     * @return {@link HttpResponse} 返回结果
     * @throws LcException
     */
    public HttpResponse executePut(HttpRequest httpRequest) throws LcException;


}
