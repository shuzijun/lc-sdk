package com.shuzijun.lc.command;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.Graphql;
import com.shuzijun.lc.http.HttpClient;
import com.shuzijun.lc.http.HttpResponse;
import com.shuzijun.lc.model.Question;
import com.shuzijun.lc.model.Submission;
import com.shuzijun.lc.model.SubmissionDetail;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class SubmissionCommand {

    /**
     * 获取提交列表
     *
     * @param titleSlug {@link Question#getTitleSlug()}题目标识
     * @param offset    偏移量
     * @param limit     限制
     * @return {@link List<Submission>} 提交列表
     */
    public static SubmissionList buildSubmissionList(String titleSlug, int offset, int limit) {
        return new SubmissionList(titleSlug, offset, limit);
    }

    /**
     * 获取提交详情
     *
     * @param submissionId {@link Submission#getId()} 提交id
     * @return {@link SubmissionDetail} 提交详情
     */
    public static GetSubmissionDetail buildSubmissionDetail(String submissionId) {
        return new GetSubmissionDetail(submissionId);
    }

    public static class SubmissionList implements Command<List<Submission>> {

        private final String titleSlug;

        private final int offset;

        private final int limit;

        public SubmissionList(String titleSlug, int offset, int limit) {
            this.titleSlug = titleSlug;
            this.offset = offset;
            this.limit = limit;
        }

        @Override
        public List<Submission> execute(HttpClient client) throws LcException {
            HttpResponse response = Graphql.builder(client.getGraphql()).header(client.getHeader())
                    .operationName("submissions")
                    .variables("offset", offset)
                    .variables("limit", limit)
                    .variables("questionSlug", titleSlug)
                    .request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                JSONArray jsonArray = JSONObject.parseObject(response.getBody()).getJSONObject("data")
                        .getJSONObject("submissionList").getJSONArray("submissions");
                return jsonArray.toJavaList(Submission.class);
            } else {
                throw new LcException("SubmissionList fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }

    public static class GetSubmissionDetail implements Command<SubmissionDetail> {

        private final String submissionId;

        public GetSubmissionDetail(String submissionId) {
            this.submissionId = submissionId;
        }

        @Override
        public SubmissionDetail execute(HttpClient client) throws LcException {
            if (client.isCn()) {
                HttpResponse response = Graphql.builder(client.getGraphql()).cn(client.isCn()).header(client.getHeader())
                        .operationName("submissionDetail").variables("id", submissionId)
                        .request(client.getExecutorHttp());
                if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                    String body = response.getBody();
                    JSONObject cnObject = JSONObject.parseObject(body).getJSONObject("data").getJSONObject("submissionDetail");

                    SubmissionDetail submissionDetail = new SubmissionDetail();
                    submissionDetail.setSubmissionCode(cnObject.getString("code"));
                    submissionDetail.setRuntime(cnObject.getString("runtime"));
                    submissionDetail.setMemory(cnObject.getString("memory"));
                    submissionDetail.setTotalTestcases(cnObject.getString("totalTestCaseCnt"));
                    submissionDetail.setTotalCorrect(cnObject.getString("passedTestCaseCnt"));
                    submissionDetail.setInputFormatted(cnObject.getJSONObject("outputDetail").getString("input"));
                    submissionDetail.setExpectedOutput(cnObject.getJSONObject("outputDetail").getString("expectedOutput"));
                    submissionDetail.setCodeOutput(cnObject.getJSONObject("outputDetail").getString("codeOutput"));
                    submissionDetail.setRuntimeError(cnObject.getJSONObject("outputDetail").getString("runtimeError"));
                    submissionDetail.setLastTestcase(cnObject.getJSONObject("outputDetail").getString("lastTestcase"));
                    submissionDetail.setCompileError(cnObject.getJSONObject("outputDetail").getString("compileError"));

                    return submissionDetail;

                } else {
                    throw new LcException("GetSubmissionDetail fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
                }
            } else {
                HttpResponse response = Graphql.builder(client.getGraphql()).header(client.getHeader())
                        .operationName("submissionDetail", "submissionDetails").variables("id", submissionId)
                        .request(client.getExecutorHttp());
                if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                    String body = response.getBody();
                    JSONObject enObject = JSONObject.parseObject(body).getJSONObject("data").getJSONObject("submissionDetails");

                    SubmissionDetail submissionDetail = new SubmissionDetail();
                    submissionDetail.setSubmissionCode(enObject.getString("code"));
                    submissionDetail.setRuntime(enObject.getString("runtimeDisplay"));
                    submissionDetail.setMemory(enObject.getString("memoryDisplay"));
                    submissionDetail.setTotalTestcases("");
                    submissionDetail.setTotalCorrect("");
                    submissionDetail.setInputFormatted("");
                    submissionDetail.setExpectedOutput("");
                    submissionDetail.setCodeOutput("");
                    submissionDetail.setRuntimeError(enObject.getString("runtimeError"));
                    submissionDetail.setLastTestcase(enObject.getString("lastTestcase"));
                    submissionDetail.setCompileError(enObject.getString("compileError"));

                    return submissionDetail;
                } else {
                    throw new LcException("GetSubmissionDetail fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
                }
            }
        }
    }
}
