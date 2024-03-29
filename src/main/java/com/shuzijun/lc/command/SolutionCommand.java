package com.shuzijun.lc.command;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.Graphql;
import com.shuzijun.lc.http.HttpClient;
import com.shuzijun.lc.http.HttpResponse;
import com.shuzijun.lc.model.QuestionView;
import com.shuzijun.lc.model.Solution;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SolutionCommand {

    /**
     * 构建获取题解列表
     *
     * @param titleSlug {@link QuestionView#getTitleSlug()} 题目slug
     * @return {@link List<Solution>} 题解列表
     */
    public static SolutionList buildSolutionList(String titleSlug,Option<?> ...option) {
        return new SolutionList(200, 0, titleSlug,option);
    }

    /**
     * 构建获取题解列表
     *
     * @param first     获取数量
     * @param skip      跳过数量
     * @param titleSlug {@link QuestionView#getTitleSlug()} 题目slug
     * @return {@link List<Solution>} 题解列表
     */
    public static SolutionList buildSolutionList(int first, int skip, String titleSlug,Option<?> ...option) {
        return new SolutionList(first, skip, titleSlug,option);
    }

    /**
     * 构建获取题解详情
     *
     * @param articleSlug 题解slug <br>
     *                    leetcode.com articleSlug为{@link  QuestionView#getTitleSlug()}<br>
     *                    leetcode.cn articleSlug为{@link  Solution#getSlug()}<br>
     * @return {@link String} 题解详情
     */
    public static SolutionArticle buildSolutionArticle(String articleSlug,Option<?> ...option) {
        return new SolutionArticle(articleSlug,option);
    }


    public static class SolutionList extends OptionCommand implements Command<List<Solution>> {

        private final int first;
        private final int skip;
        private final String titleSlug;

        public SolutionList(int first, int skip, String titleSlug,Option<?> ...option) {
            super(option);
            this.first = first;
            this.skip = skip;
            this.titleSlug = titleSlug;
        }

        @Override
        public List<Solution> execute(HttpClient client) throws LcException {
            if (!client.isCn()) {
                return null;
            }
            HttpResponse response = Graphql.builder(client.getGraphql()).cn(client.isCn()).header(client.getHeader())
                    .operationName("questionSolutionArticles")
                    .variables("questionSlug", titleSlug)
                    .variables("first", first).variables("skip", skip).variables("orderBy", "DEFAULT")
                    .addOption(getOptions())
                    .request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                List<Solution> solutionList = new ArrayList<>();

                JSONArray edges = JSONObject.parseObject(response.getBody()).getJSONObject("data").getJSONObject("questionSolutionArticles").getJSONArray("edges");
                for (int i = 0; i < edges.size(); i++) {
                    JSONObject node = edges.getJSONObject(i).getJSONObject("node");
                    Solution solution = node.toJavaObject(Solution.class);
                    JSONArray tagArray = node.getJSONArray("tags");
                    String tags = tagArray.stream().map(tag -> JSONObject.from(tag).getString("name")).collect(Collectors.joining(","));
                    solution.setTags(tags);
                    solutionList.add(solution);
                }

                return solutionList;
            } else {
                throw new LcException("SolutionList fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }

    public static class SolutionArticle extends OptionCommand implements Command<String> {

        private final String articleSlug;

        public SolutionArticle(String articleSlug,Option<?> ...option) {
            super(option);
            this.articleSlug = articleSlug;
        }

        @Override
        public String execute(HttpClient client) throws LcException {
            HttpResponse response = Graphql.builder(client.getGraphql()).cn(client.isCn()).header(client.getHeader())
                    .operationName("solutionDetailArticle")
                    .variables("slug", articleSlug)
                    .variables("titleSlug", articleSlug)
                    .variables("orderBy", "DEFAULT")
                    .addOption(getOptions())
                    .request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                JSONObject solutionArticleObject = JSONObject.parseObject(response.getBody()).getJSONObject("data").getJSONObject("solutionArticle");
                String content;
                if (client.isCn()) {
                    content = solutionArticleObject.getString("content");
                } else {
                    content = solutionArticleObject.getJSONObject("solution").getString("content");
                }
                if (StringUtils.isBlank(content)) {
                    return null;
                } else {
                    return content;
                }
            } else {
                throw new LcException("SolutionArticle fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }
}
