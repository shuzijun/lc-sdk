package com.shuzijun.lc;

import com.shuzijun.lc.http.HttpClient;
import org.junit.Assert;
import org.junit.Test;

public class LcEndpointTest {

    @Test
    public void testCustom_normalizesAbsoluteBaseUrl() {
        LcEndpoint endpoint = LcEndpoint.custom(HttpClient.SiteEnum.CN, "http://127.0.0.1:8080/");

        Assert.assertEquals(HttpClient.SiteEnum.CN, endpoint.getSite());
        Assert.assertEquals("http://127.0.0.1:8080", endpoint.getBaseUrl());
    }

    @Test
    public void testCustom_rejectsHostWithoutScheme() {
        try {
            LcEndpoint.custom(HttpClient.SiteEnum.EN, "leetcode.com");
            Assert.fail("Expected an invalid endpoint error");
        } catch (IllegalArgumentException expected) {
            Assert.assertTrue(expected.getMessage().contains("absolute HTTP(S) URL"));
        }
    }

    @Test
    public void testCustom_rejectsUnsupportedScheme() {
        try {
            LcEndpoint.custom(HttpClient.SiteEnum.EN, "file:///tmp/leetcode");
            Assert.fail("Expected an invalid endpoint error");
        } catch (IllegalArgumentException expected) {
            Assert.assertTrue(expected.getMessage().contains("absolute HTTP(S) URL"));
        }
    }
}
