package com.shuzijun.lc.command;

import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.HttpClient;
import com.shuzijun.lc.http.HttpRequest;

public class CookieCommand {

    /**
     * 设置cookie，一般用于设置登录信息
     *
     * @param cookie 设置的cookie,格式为: key=value;key=value
     * @return {@link Void}
     */
    public static SetCookie buildSetCookie(String cookie,Option<?> ...option) {
        return new SetCookie(cookie,option);
    }

    /**
     * 退出
     */
    public static Logout buildLogout(Option<?>...option) {
        return new Logout(option);
    }

    /**
     * 设置cookie
     */
    public static class SetCookie extends OptionCommand implements Command<Void> {
        private String cookie;

        public SetCookie(String cookie,Option<?>...option) {
            super(option);
            this.cookie = cookie;
        }

        @Override
        public Void execute(HttpClient client) throws LcException {
            client.cookieStore().addCookie(client.getEndpoint(), cookie);
            return null;
        }
    }

    public static class Logout extends OptionCommand implements Command<Void> {
        public Logout(Option<?>...option) {
            super(option);
        }

        @Override
        public Void execute(HttpClient client) throws LcException {
            HttpRequest.builderGet(client.getLogout())
                    .addHeader(client.getHeader())
                    .addOption(getOptions())
                    .request(client.getExecutorHttp());
            client.cookieStore().clearCookie(client.getEndpoint());
            return null;
        }
    }
}
