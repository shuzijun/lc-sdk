package com.shuzijun.lc.http;

import org.junit.Assert;
import org.junit.Test;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

public class CookieStoreTest {

    @Test
    public void testParse_preservesEqualsCharactersInCookieValue() {
        CookieStore store = new InMemoryCookieStore();

        List<HttpCookie> cookies = store.parse(
                "leetcode.cn",
                "LEETCODE_SESSION=header.payload=signature; csrftoken=csrf-value"
        );

        Assert.assertEquals(2, cookies.size());
        Assert.assertEquals("LEETCODE_SESSION", cookies.get(0).getName());
        Assert.assertEquals("header.payload=signature", cookies.get(0).getValue());
        Assert.assertEquals("leetcode.cn", cookies.get(0).getDomain());
        Assert.assertEquals("/", cookies.get(0).getPath());
    }

    private static final class InMemoryCookieStore implements CookieStore {
        @Override
        public void addCookie(String domain, List<HttpCookie> cookieList) {
        }

        @Override
        public void clearCookie(String domain) {
        }

        @Override
        public List<HttpCookie> getCookies(String domain) {
            return new ArrayList<>();
        }

        @Override
        public HttpCookie getCookie(String domain, String name) {
            return null;
        }
    }
}
