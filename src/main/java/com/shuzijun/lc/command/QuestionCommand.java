package com.shuzijun.lc.command;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.Graphql;
import com.shuzijun.lc.http.HttpClient;
import com.shuzijun.lc.http.HttpResponse;
import com.shuzijun.lc.model.PageInfo;
import com.shuzijun.lc.model.ProblemSetParam;
import com.shuzijun.lc.model.Question;
import com.shuzijun.lc.model.QuestionView;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class QuestionCommand {

    /**
     * 构建获取题目列表
     *
     * @param problemSetParam 题目列表参数
     * @return {@link PageInfo<QuestionView>} 题目列表
     */
    public static ProblemSetQuestionList buildProblemSetQuestionList(ProblemSetParam problemSetParam,Option<?> ...option) {
        return new ProblemSetQuestionList(problemSetParam,option);
    }

    /**
     * 构建获取所有题目
     *
     * @return {@link List<Question>} 题目列表
     */
    public static AllQuestions buildAllQuestions(Option<?> ...option) {
        return new AllQuestions(option);
    }

    /**
     * 构建获取题目详情
     *
     * @param titleSlug {@link QuestionView#getTitleSlug()} 题目slug
     * @return {@link Question} 题目详情
     */
    public static GetQuestion buildGetQuestion(String titleSlug,Option<?> ...option) {
        return new GetQuestion(titleSlug,option);
    }

    /**
     * 构建获取每日一题
     *
     * @return {@link Question} 每日一题信息
     */
    public static QuestionOfToday buildQuestionOfToday(Option<?> ...option) {
        return new QuestionOfToday(option);
    }

    /**
     * 构建获取随机题目
     * @param problemSetParam 题目列表参数
     * @return {@link String} 题目slug
     */
    public static RandomQuestion buildRandomQuestion(ProblemSetParam problemSetParam,Option<?> ...option) {
        return new RandomQuestion(problemSetParam,option);
    }

    public static class ProblemSetQuestionList extends OptionCommand implements Command<PageInfo<QuestionView>> {

        private final ProblemSetParam problemSetParam;

        public ProblemSetQuestionList(ProblemSetParam problemSetParam,Option<?> ...option) {
            super(option);
            this.problemSetParam = problemSetParam;
        }

        @Override
        public PageInfo<QuestionView> execute(HttpClient client) throws LcException {
            HttpResponse response = Graphql.builder(client.getGraphql()).cn(client.isCn()).header(client.getHeader())
                    .operationName("problemsetQuestionList")
                    .variables("categorySlug", problemSetParam.getCategorySlug())
                    .variables("skip", problemSetParam.getSkip())
                    .variables("limit", problemSetParam.getPageSize())
                    .variables("filters", problemSetParam.getFilters())
                    .addOption(getOptions())
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


    public static class AllQuestions extends OptionCommand implements Command<List<QuestionView>> {

        public AllQuestions(Option<?> ...option) {
            super(option);
        }

        @Override
        public List<QuestionView> execute(HttpClient client) throws LcException {
            HttpResponse response = Graphql.builder(client.getGraphql()).cn(client.isCn()).header(client.getHeader())
                    .operationName("allQuestions")
                    .addOption(getOptions())
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

    public static class GetQuestion  extends OptionCommand implements Command<Question> {

        private final String titleSlug;

        public GetQuestion(String titleSlug,Option<?> ...option) {
            super(option);
            this.titleSlug = titleSlug;
        }

        @Override
        public Question execute(HttpClient client) throws LcException {
            HttpResponse response = Graphql.builder(client.getGraphql()).header(client.getHeader())
                    .operationName("questionData")
                    .variables("titleSlug", titleSlug)
                    .addOption(getOptions())
                    .request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                JSONObject jsonObject = JSONObject.parseObject(response.getBody()).getJSONObject("data").getJSONObject("question");
                return jsonObject.toJavaObject(Question.class);
            } else {
                throw new LcException("GetQuestion fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }


    public static class QuestionOfToday extends OptionCommand implements Command<Question> {

        public QuestionOfToday(Option<?>...option) {
            super(option);
        }
        @Override
        public Question execute(HttpClient client) throws LcException {
            HttpResponse response = Graphql.builder(client.getGraphql()).cn(client.isCn()).header(client.getHeader())
                    .operationName("questionOfToday")
                    .addOption(getOptions())
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

    public static class RandomQuestion extends OptionCommand implements Command<String> {

        private final ProblemSetParam problemSetParam;

        public RandomQuestion(ProblemSetParam problemSetParam,Option<?>...option) {
            super(option);
            this.problemSetParam = problemSetParam;
        }
        @Override
        public String execute(HttpClient client) throws LcException {
            HttpResponse response = Graphql.builder(client.getGraphql()).cn(client.isCn()).header(client.getHeader())
                    .operationName("randomQuestion")
                    .variables("categorySlug", problemSetParam.getCategorySlug())
                    .variables("filters", problemSetParam.getFilters())
                    .addOption(getOptions())
                    .request(client.getExecutorHttp());

            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                String body = response.getBody();
                String titleSlug;
                if (client.isCn()) {
                    titleSlug = JSONObject.parseObject(body).getJSONObject("data").getString("randomQuestion");
                } else {
                    titleSlug = JSONObject.parseObject(body).getJSONObject("data").getJSONObject("randomQuestion").getString("titleSlug");
                }
                return titleSlug;
            } else {
                throw new LcException("QuestionOfToday fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }
}
