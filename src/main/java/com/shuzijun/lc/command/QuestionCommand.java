package com.shuzijun.lc.command;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.Graphql;
import com.shuzijun.lc.http.HttpClient;
import com.shuzijun.lc.http.HttpResponse;
import com.shuzijun.lc.model.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionCommand {

   public static ProblemSetQuestionList buildProblemSetQuestionList(ProblemSetParam problemSetParam) {
        return new ProblemSetQuestionList(problemSetParam);
    }

    public static AllQuestions buildAllQuestions() {
        return new AllQuestions();
    }

    public static GetQuestion buildGetQuestion(String titleSlug) {
        return new GetQuestion(titleSlug);
    }

    public static QuestionOfToday buildQuestionOfToday() {
        return new QuestionOfToday();
    }

    /**
     * 分页获取题目列表
     */
    public static class  ProblemSetQuestionList implements Command<PageInfo<QuestionView>> {

        private final ProblemSetParam problemSetParam;

        public ProblemSetQuestionList(ProblemSetParam problemSetParam) {
            this.problemSetParam = problemSetParam;
        }

        @Override
        public PageInfo<QuestionView> execute(HttpClient client) throws LcException {
            Map<String,String> header = client.getHeader();
            HttpResponse response = Graphql.builder(client.getGraphql()).cn(client.isCn()).header(header)
                    .operationName("problemsetQuestionList")
                    .variables("categorySlug", problemSetParam.getCategorySlug())
                    .variables("skip", problemSetParam.getSkip())
                    .variables("limit", problemSetParam.getPageSize())
                    .variables("filters", problemSetParam.getFilters())
                    .request(client.getExecutorHttp());

            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                PageInfo<QuestionView> pageInfo = new PageInfo<>(problemSetParam.getPageIndex(), problemSetParam.getPageSize());
                List<QuestionView> questionList = new ArrayList<>();

                JSONObject dataObject = JSONObject.parseObject(response.getBody()).getJSONObject("data");
                JSONArray questionArray = dataObject.getJSONObject("problemsetQuestionList").getJSONArray("questions");
                for (int i = 0; i < questionArray.size(); i++) {
                    JSONObject object = questionArray.getJSONObject(i);
                    QuestionView question = object.toJavaObject(QuestionView.class);
                    questionList.add(question);
                }
                pageInfo.setRows(questionList);
                Integer total = dataObject.getJSONObject("problemsetQuestionList").getInteger("total");
                pageInfo.setRowTotal(total);
                return pageInfo;
            } else {
                throw new LcException("ProblemSetQuestionList fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }


    /**
     * 获取所有题目
     */
    public static class AllQuestions implements Command<List<QuestionView>> {

        @Override
        public List<QuestionView> execute(HttpClient client) throws LcException {
            Map<String,String> header = client.getHeader();
            HttpResponse response = Graphql.builder(client.getGraphql()).cn(client.isCn()).header(header)
                    .operationName("allQuestions")
                    .request(client.getExecutorHttp());

            if (response.isCodeSuccess()) {
                List<QuestionView> questionViews = new ArrayList<>();

                JSONArray allQuestions = JSONObject.parseObject(response.getBody()).getJSONObject("data").getJSONArray("allQuestions");
                for (int i = 0; i < allQuestions.size(); i++) {
                    JSONObject jsonObject = allQuestions.getJSONObject(i);
                    QuestionView questionView = jsonObject.toJavaObject(QuestionView.class);
                    questionViews.add(questionView);
                }
                return questionViews;
            } else {
                throw new LcException("AllQuestions fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }

    /**
     * 获取题目详情
     */
    public static class GetQuestion implements Command<Question> {

        private final String titleSlug;

        public GetQuestion(String titleSlug) {
            this.titleSlug = titleSlug;
        }

        @Override
        public Question execute(HttpClient client) throws LcException {
            Map<String,String> header = client.getHeader();
            HttpResponse response = Graphql.builder(client.getGraphql()).header(header)
                    .operationName("questionData")
                    .variables("titleSlug", titleSlug).request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                JSONObject jsonObject = JSONObject.parseObject(response.getBody()).getJSONObject("data").getJSONObject("question");
                Question question = jsonObject.toJavaObject(Question.class);
                return question;
            } else {
                throw new LcException("GetQuestion fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }


    public static class QuestionOfToday implements Command<Question>{
        @Override
        public Question execute(HttpClient client) throws LcException {
            Map<String,String> header = client.getHeader();
            HttpResponse response = Graphql.builder(client.getGraphql()).cn(client.isCn()).header(header)
                    .operationName("questionOfToday")
                    .request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                JSONObject jsonObject = JSONObject.parseObject(response.getBody()).getJSONObject("data");
                JSONObject todayRecordObject;
                if (client.isCn()) {
                    todayRecordObject = jsonObject.getJSONArray("activeDailyCodingChallengeQuestion").getJSONObject(0);
                } else {
                    todayRecordObject = jsonObject.getJSONObject("activeDailyCodingChallengeQuestion");
                }
                if (todayRecordObject == null) {
                    return null;
                }

                return buildGetQuestion(todayRecordObject.getJSONObject("question").getString("titleSlug")).execute(client);
            } else {
                throw new LcException("QuestionOfToday fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }
}
