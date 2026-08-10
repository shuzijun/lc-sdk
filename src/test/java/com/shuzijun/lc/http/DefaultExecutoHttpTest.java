package com.shuzijun.lc.http;

import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Test;

public class DefaultExecutoHttpTest {

    @Test
    public void testNewDefaultHttpClientUsesPlatformTlsVerification() {
        OkHttpClient client = new DefaultExecutoHttp().newDefaultHttpClient(1, 1, 1);
        OkHttpClient defaultClient = new OkHttpClient();

        Assert.assertSame(defaultClient.hostnameVerifier(), client.hostnameVerifier());
        Assert.assertEquals(defaultClient.connectionSpecs(), client.connectionSpecs());
    }
}
