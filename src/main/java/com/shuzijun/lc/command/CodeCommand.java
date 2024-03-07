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
    public static SubmitCode buildSubmitCode(SubmitParam submitParam,Option<?> ...option) {
        return new SubmitCode(submitParam,option);
    }

    /**
     * 提交检查
     *
     * @param submissionId 提交{@link #buildSubmitCode}返回的id
     * @return {@link SubmitCheckResult} 提交代码结果
     */

    public static SubmitCheck buildSubmitCheck(Integer submissionId,Option<?> ...option) {
        return new SubmitCheck(submissionId,option);
    }

    /**
     * 运行代码
     *
     * @param runCodeParam {@link RunCodeParam} 运行参数
     * @return {@link RunCodeResult} 运行结果，包含运行id
     */
    public static RunCode buildRunCode(RunCodeParam runCodeParam,Option<?> ...option) {
        return new RunCode(runCodeParam,option);
    }

    /**
     * 运行检查
     *
     * @param interpretId 运行{@link #buildRunCode}返回的{@link RunCodeResult#getInterpretId()
     * @return {@link RunCodeResult } 运行结果
     */
    public static RunCodeCheck buildRunCodeCheck(String interpretId,Option<?> ...option) {
        return new RunCodeCheck(interpretId,option);
    }

    public static class SubmitCode extends OptionCommand implements Command<SubmitResult>  {
        private final SubmitParam submitParam;

        public SubmitCode(SubmitParam submitParam,Option<?> ...option) {
            super(option);
            this.submitParam = submitParam;
        }

        @Override
        public SubmitResult execute(HttpClient client) throws LcException {
            HttpResponse response = HttpRequest.builderPost(client.getProblems() + submitParam.getTitleSlug() + "/submit/", "application/json")
                    .addHeader(client.getHeader()).addHeader("Accept", "application/json")
                    .addOption(getOptions())
                    .body(JSONObject.toJSONString(submitParam))
                    .request(client.getExecutorHttp());
            SubmitResult submitResult = new SubmitResult();
            submitResult.setHttpStatueCode(response.getStatusCode());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                String body = response.getBody();
                JSONObject returnObj = JSONObject.parseObject(body);
                submitResult.setSubmissionId(returnObj.getInteger("submission_id"));
            } else if (response.getStatusCode() == 429) {
                //Code submitted. Please wait...
            } else {
                throw new LcException("SubmitCode fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
            return submitResult;
        }
    }

    public static class SubmitCheck extends OptionCommand implements Command<SubmitCheckResult> {

        private final Integer submissionId;

        public SubmitCheck(Integer submissionId,Option<?> ...option) {
            super(option);
            this.submissionId = submissionId;
        }


        @Override
        public SubmitCheckResult execute(HttpClient client) throws LcException {

            HttpResponse response = HttpRequest.builderGet(client.getSubmissions() + submissionId + "/check/")
                    .addHeader(client.getHeader())
                    .addOption(getOptions())
                    .request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                return JSONObject.parseObject(response.getBody(), SubmitCheckResult.class);
            } else {
                throw new LcException("SubmitCheck fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }

    public static class RunCode extends OptionCommand implements Command<RunCodeResult> {

        private final RunCodeParam runCodeParam;

        public RunCode(RunCodeParam runCodeParam,Option<?> ...option) {
            super(option);
            this.runCodeParam = runCodeParam;
        }

        @Override
        public RunCodeResult execute(HttpClient client) throws LcException {

            HttpResponse response = HttpRequest.builderPost(client.getProblems() + runCodeParam.getTitleSlug() + "/interpret_solution/", "application/json")
                    .addHeader(client.getHeader())
                    .addHeader("Accept", "application/json")
                    .addOption(getOptions())
                    .body(JSONObject.toJSONString(runCodeParam))
                    .request(client.getExecutorHttp());
            RunCodeResult runCodeResult;
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                String body = response.getBody();
                runCodeResult = JSONObject.parseObject(body, RunCodeResult.class);
                runCodeResult.setHttpStatueCode(response.getStatusCode());
                return runCodeResult;
            } else if (response.getStatusCode() == 429) {
                //Code submitted. Please wait...
                runCodeResult = new RunCodeResult();
                runCodeResult.setHttpStatueCode(response.getStatusCode());
                return runCodeResult;
            } else {
                throw new LcException("RunCode fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }

    public static class RunCodeCheck extends OptionCommand implements Command<RunCodeCheckResult> {

        private final String interpretId;

        public RunCodeCheck(String interpretId,Option<?> ...option) {
            super(option);
            this.interpretId = interpretId;
        }

        @Override
        public RunCodeCheckResult execute(HttpClient client) throws LcException {
            HttpResponse response = HttpRequest.builderGet(client.getSubmissions() + interpretId + "/check/")
                    .addHeader(client.getHeader())
                    .addOption(getOptions())
                    .request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                return JSONObject.parseObject(response.getBody(), RunCodeCheckResult.class);
            } else {
                throw new LcException("RunCodeCheck fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }
}
