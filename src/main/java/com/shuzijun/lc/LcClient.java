package com.shuzijun.lc;

import com.shuzijun.lc.command.Command;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.ExecutorHttp;
import com.shuzijun.lc.http.HttpClient;

public class LcClient {
    private final HttpClient client;

    private LcClient(HttpClient client) {
        this.client = client;
    }

    /**
     * 构建LcClient
     * @param siteEnum 站点
     * @return {@link Builder}
     */
    public static Builder builder(HttpClient.SiteEnum siteEnum) {
        return new Builder(siteEnum);
    }

    public HttpClient getClient() {
        return client;
    }

    /**
     * 执行器
     *
     * @param command 命令
     * @param <T>     返回类型
     * @return 返回结果
     * @throws LcException 异常
     */

    public <T> T invoker(Command<T> command) throws LcException {
        return command.execute(client);
    }

    public static final class Builder {

        private final HttpClient.Builder builder;

        public Builder(HttpClient.SiteEnum siteEnum) {
            builder = HttpClient.builder(siteEnum);
        }

        /**
         * 设置站点,默认使用构建{@link HttpClient.SiteEnum}对应的站点，一般不需要设置
         *
         * @param endpoint {@link HttpClient.SiteEnum}站点
         * @return {@link Builder}
         */
        public Builder endpoint(String endpoint) {
            this.builder.endpoint(endpoint);
            return this;
        }

        /**
         * 设置ExecutorHttp
         *
         * @param executorHttp {@link ExecutorHttp}http请求执行器,默认使用{@link com.shuzijun.lc.http.DefaultExecutoHttp}
         * @return {@link Builder}
         */
        public Builder executorHttp(ExecutorHttp executorHttp) {
            this.builder.executorHttp(executorHttp);
            return this;
        }


        public LcClient build() {
            return new LcClient(builder.build());
        }

    }
}
