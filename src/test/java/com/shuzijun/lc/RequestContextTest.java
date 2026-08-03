package com.shuzijun.lc;

import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.CookieStore;
import com.shuzijun.lc.http.ExecutorHttp;
import com.shuzijun.lc.http.HttpRequest;
import com.shuzijun.lc.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;

public class RequestContextTest {

    @Test
    public void testApi_addsContextAndClientHeaders() throws LcException {
        RecordingExecutor executor = new RecordingExecutor(new HttpResponse(200));
        LcClient client = LcClient.create(LcClientConfig.builder(
                        LcEndpoint.custom(com.shuzijun.lc.http.HttpClient.SiteEnum.EN, "http://127.0.0.1:8080"))
                .executorHttp(executor)
                .header("X-Client", "client")
                .build());

        boolean verified = client.api().account().verify(
                RequestContext.builder().header("X-Request", "request").build());

        Assert.assertTrue(verified);
        Assert.assertEquals("client", executor.request.getHeader().get("X-Client"));
        Assert.assertEquals("request", executor.request.getHeader().get("X-Request"));
        Assert.assertEquals("http://127.0.0.1:8080/problemset/", executor.request.getUrl());
    }

    @Test
    public void testApi_cancelledContextDoesNotInvokeTransport() {
        RecordingExecutor executor = new RecordingExecutor(new HttpResponse(200));
        LcClient client = LcClient.create(LcClientConfig.builder(LcEndpoint.LEETCODE)
                .executorHttp(executor)
                .build());
        RequestContext cancelled = RequestContext.builder()
                .cancellationToken(new CancellationToken() {
                    @Override
                    public boolean isCancellationRequested() {
                        return true;
                    }
                })
                .build();

        try {
            client.api().account().verify(cancelled);
            Assert.fail("Expected cancellation");
        } catch (LcException expected) {
            Assert.assertEquals("Request cancelled", expected.getMessage());
        }
        Assert.assertNull(executor.request);
    }

    private static final class RecordingExecutor implements ExecutorHttp {
        private final HttpResponse response;
        private HttpRequest request;

        private RecordingExecutor(HttpResponse response) {
            this.response = response;
        }

        @Override
        public CookieStore cookieStore() {
            return new TestCookieStore();
        }

        @Override
        public HttpResponse executeGet(HttpRequest httpRequest) {
            request = httpRequest;
            response.setHttpRequest(httpRequest);
            return response;
        }

        @Override
        public HttpResponse executePost(HttpRequest httpRequest) {
            return executeGet(httpRequest);
        }

        @Override
        public HttpResponse executePut(HttpRequest httpRequest) {
            return executeGet(httpRequest);
        }
    }
}
