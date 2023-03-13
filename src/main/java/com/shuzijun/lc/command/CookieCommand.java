package com.shuzijun.lc.command;

import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.HttpClient;

public class CookieCommand {

    /**
     * 设置cookie，一般用于设置登录信息
     *
     * @param cookie cookie
     * @return {@link Void}
     */
    public static SetCookie buildSetCookie(String cookie) {
        return new SetCookie(cookie);
    }

    /**
     * 设置cookie
     */
    public static class SetCookie implements Command<Void> {
        private String cookie;

        public SetCookie(String cookie) {
            this.cookie = cookie;
        }

        @Override
        public Void execute(HttpClient client) throws LcException {
            client.cookieStore().addCookie(client.getEndpoint(), cookie);
            return null;
        }
    }
}
