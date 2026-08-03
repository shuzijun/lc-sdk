package com.shuzijun.lc;

import com.shuzijun.lc.command.CommonCommand;
import com.shuzijun.lc.command.CodeCommand;
import com.shuzijun.lc.command.Option;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.CookieStore;
import com.shuzijun.lc.http.ExecutorHttp;
import com.shuzijun.lc.http.HttpClient;
import com.shuzijun.lc.http.HttpRequest;
import com.shuzijun.lc.http.HttpResponse;
import com.shuzijun.lc.model.RunCodeCheckResult;
import com.shuzijun.lc.model.SubmitResult;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class LegacyApiCompatibilityTest {

    @Test
    public void testLegacyBuilderAndInvokerRemainUsable() throws LcException {
        RecordingExecutor executor = new RecordingExecutor();
        LcClient client = LcClient.builder(HttpClient.SiteEnum.CN)
                .endpoint("legacy.example")
                .executorHttp(executor)
                .build();

        boolean result = client.invoker(CommonCommand.buildVerify());

        Assert.assertTrue(result);
        Assert.assertEquals("https://legacy.example/problemset/", executor.request.getUrl());
    }

    @Test
    public void testLegacyCodeApiSignaturesAndSingleValueAccessorsRemainUsable() throws Exception {
        Method submitResult = LcApi.Code.class.getMethod(
                "submitResult",
                Integer.class,
                RequestContext.class
        );
        Method buildSubmitCheck = CodeCommand.class.getMethod(
                "buildSubmitCheck",
                Integer.class,
                Option[].class
        );
        Constructor<CodeCommand.SubmitCheck> submitCheck = CodeCommand.SubmitCheck.class
                .getConstructor(Integer.class, Option[].class);

        Assert.assertNotNull(submitResult);
        Assert.assertNotNull(buildSubmitCheck);
        Assert.assertNotNull(submitCheck);

        SubmitResult numericResult = new SubmitResult();
        numericResult.setSubmissionId(123);
        Assert.assertEquals(Integer.valueOf(123), numericResult.getSubmissionId());
        Assert.assertEquals("123", numericResult.getSubmissionIdValue());

        RunCodeCheckResult legacyResult = new RunCodeCheckResult();
        legacyResult.setCodeAnswer("[0,1]");
        legacyResult.setCodeOutput("stdout");
        legacyResult.setExpectedCodeAnswer("[0,1]");
        Assert.assertEquals("[0,1]", legacyResult.getCodeAnswer());
        Assert.assertEquals("stdout", legacyResult.getCodeOutput());
        Assert.assertEquals("[0,1]", legacyResult.getExpectedCodeAnswer());
        Assert.assertEquals("[0,1]", legacyResult.getCodeAnswers().get(0));
    }

    private static final class RecordingExecutor implements ExecutorHttp {
        private HttpRequest request;

        @Override
        public CookieStore cookieStore() {
            return new TestCookieStore();
        }

        @Override
        public HttpResponse executeGet(HttpRequest httpRequest) {
            request = httpRequest;
            return new HttpResponse(200, "", httpRequest);
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
