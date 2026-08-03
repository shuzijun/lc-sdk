package com.shuzijun.lc.http;

import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Test;

public class DefaultExecutoHttpTest {

    @Test
    public void testNewDefaultHttpClientUsesPlatformTlsVerification() {
        OkHttpClient client = new DefaultExecutoHttp().newDefaultHttpClient(1, 1, 1);

        Assert.assertEquals(
                "okhttp3.internal.tls.OkHostnameVerifier",
                client.hostnameVerifier().getClass().getName());
    }
}
