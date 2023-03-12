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

    public static Builder builder(HttpClient.SiteEnum siteEnum) {
        return new Builder(siteEnum);
    }

    /**
     * 执行器
     * @param command 命令
     * @return 返回结果
     * @param <T> 返回类型
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

        public Builder endpoint(String endpoint) {
            this.builder.endpoint(endpoint);
            return this;
        }

        public Builder executorHttp(ExecutorHttp executorHttp) {
            this.builder.executorHttp(executorHttp);
            return this;
        }


        public LcClient build() {
            return new LcClient(builder.build());
        }

    }
}
