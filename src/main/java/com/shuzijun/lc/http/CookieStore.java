package com.shuzijun.lc.http;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

public interface CookieStore {

    default List<HttpCookie> parse(String domain, String cookie) {
        if (cookie == null || cookie.isEmpty()) {
            return new ArrayList<>();
        }
        final List<HttpCookie> cookieList = new ArrayList<>();
        String[] cookies = cookie.split(";");
        for (String cookieString : cookies) {
            String cookieNameValue = cookieString.trim();
            int separator = cookieNameValue.indexOf('=');
            if (separator > 0) {
                try {
                    HttpCookie basicClientCookie = new HttpCookie(
                            cookieNameValue.substring(0, separator),
                            cookieNameValue.substring(separator + 1)
                    );
                    basicClientCookie.setDomain(domain);
                    basicClientCookie.setPath("/");
                    cookieList.add(basicClientCookie);
                } catch (IllegalArgumentException ignore) {

                }
            }
        }
        return cookieList;
    }

    default void setCookie(String domain, String cookie) {
        final List<HttpCookie> cookieList = parse(domain, cookie);
        clearCookie(domain);
        addCookie(domain, cookieList);
    }

    default void setCookie(String domain, List<HttpCookie> cookieList) {
        clearCookie(domain);
        addCookie(domain, cookieList);
    }

    default void addCookie(String domain, String cookie) {
        final List<HttpCookie> cookieList = parse(domain, cookie);
        addCookie(domain, cookieList);
    }

    void addCookie(String domain, List<HttpCookie> cookieList);

    void clearCookie(String domain);

    List<HttpCookie> getCookies(String domain);

    HttpCookie getCookie(String domain, String name);
}
