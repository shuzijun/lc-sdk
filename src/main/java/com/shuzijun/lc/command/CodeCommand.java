package com.shuzijun.lc.command;

import com.alibaba.fastjson2.JSONObject;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.HttpClient;
import com.shuzijun.lc.http.HttpRequest;
import com.shuzijun.lc.http.HttpResponse;
import com.shuzijun.lc.model.*;
import org.apache.commons.lang3.StringUtils;

public class CodeCommand {

    /**
     * 提交代码
     *
     * @param submitParam 提交参数
     * @return {@link Integer}  提交id
     */
    public static SubmitCode buildSubmitCode(SubmitParam submitParam) {
        return new SubmitCode(submitParam);
    }

    /**
     * 提交检查
     *
     * @param submissionId 提交id,提交代码返回的结果
     * @return {@link SubmitCheckResult} 提交代码结果
     */

    public static SubmitCheck buildSubmitCheck(Integer submissionId) {
        return new SubmitCheck(submissionId);
    }

    /**
     * 运行代码
     *
     * @param runCodeParam 运行参数
     * @return {@link RunCodeResult} 运行结果，包含运行id
     */
    public static RunCode buildRunCode(RunCodeParam runCodeParam) {
        return new RunCode(runCodeParam);
    }

    /**
     * 运行检查
     *
     * @param interpretId 运行id，运行代码返回的结果
     * @return {@link RunCodeResult} 运行结果
     */
    public static RunCodeCheck buildRunCodeCheck(String interpretId) {
        return new RunCodeCheck(interpretId);
    }

    public static class SubmitCode implements Command<Integer> {
        private final SubmitParam submitParam;

        public SubmitCode(SubmitParam submitParam) {
            this.submitParam = submitParam;
        }

        @Override
        public Integer execute(HttpClient client) throws LcException {
            HttpResponse response = HttpRequest.builderPost(client.getProblems() + submitParam.getTitleSlug() + "/submit/", "application/json")
                    .addHeader(client.getHeader()).addHeader("Accept", "application/json")
                    .body(JSONObject.toJSONString(submitParam))
                    .request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                String body = response.getBody();
                JSONObject returnObj = JSONObject.parseObject(body);
                return returnObj.getInteger("submission_id");
            } else if (response.getStatusCode() == 429) {
                throw new LcException("Code submitted. Please wait...");
            } else {
                throw new LcException("SubmitCode fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }

    public static class SubmitCheck implements Command<SubmitCheckResult> {

        private final Integer submissionId;

        public SubmitCheck(Integer submissionId) {
            this.submissionId = submissionId;
        }


        @Override
        public SubmitCheckResult execute(HttpClient client) throws LcException {

            HttpResponse response = HttpRequest.builderGet(client.getSubmissions() + submissionId + "/check/")
                    .addHeader(client.getHeader())
                    .request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                return JSONObject.parseObject(response.getBody(), SubmitCheckResult.class);
            } else {
                throw new LcException("SubmitCheck fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }

    public static class RunCode implements Command<RunCodeResult> {

        private final RunCodeParam runCodeParam;

        public RunCode(RunCodeParam runCodeParam) {
            this.runCodeParam = runCodeParam;
        }

        @Override
        public RunCodeResult execute(HttpClient client) throws LcException {

            HttpResponse response = HttpRequest.builderPost(client.getProblems() + runCodeParam.getTitleSlug() + "/interpret_solution/", "application/json")
                    .addHeader(client.getHeader())
                    .addHeader("Accept", "application/json")
                    .body(JSONObject.toJSONString(runCodeParam))
                    .request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                String body = response.getBody();
                return JSONObject.parseObject(body, RunCodeResult.class);
            } else if (response.getStatusCode() == 429) {
                throw new LcException("Code submitted. Please wait...");
            } else {
                throw new LcException("RunCode fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }

    public static class RunCodeCheck implements Command<RunCodeCheckResult> {

        private final String interpretId;

        public RunCodeCheck(String interpretId) {
            this.interpretId = interpretId;
        }

        @Override
        public RunCodeCheckResult execute(HttpClient client) throws LcException {
            HttpResponse response = HttpRequest.builderGet(client.getSubmissions() + interpretId + "/check/")
                    .addHeader(client.getHeader())
                    .request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                return JSONObject.parseObject(response.getBody(), RunCodeCheckResult.class);
            } else {
                throw new LcException("RunCodeCheck fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }
}
