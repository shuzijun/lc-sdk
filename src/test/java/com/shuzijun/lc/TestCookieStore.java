package com.shuzijun.lc;

import com.shuzijun.lc.http.CookieStore;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

final class TestCookieStore implements CookieStore {

    private final List<HttpCookie> cookies = new ArrayList<>();

    @Override
    public void addCookie(String domain, List<HttpCookie> cookieList) {
        cookies.addAll(cookieList);
    }

    @Override
    public void clearCookie(String domain) {
        cookies.clear();
    }

    @Override
    public List<HttpCookie> getCookies(String domain) {
        return new ArrayList<>(cookies);
    }

    @Override
    public HttpCookie getCookie(String domain, String name) {
        for (HttpCookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }
}
