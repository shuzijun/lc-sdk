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
     * @param articleId 题解请求标识 <br>
     *                  leetcode.com 使用{@link Solution#getTopicId()}<br>
     *                  leetcode.cn 使用{@link Solution#getSlug()}<br>
     * @return {@link String} 题解详情
     */
    public static SolutionArticle buildSolutionArticle(String articleId,Option<?> ...option) {
        return new SolutionArticle(articleId,option);
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
            String operationName = client.isCn()
                    ? "questionSolutionArticles"
                    : "ugcArticleSolutionArticles";
            HttpResponse response = Graphql.builder(client.getGraphql()).cn(client.isCn()).header(client.getHeader())
                    .operationName("questionSolutionArticles", operationName)
                    .variables("questionSlug", titleSlug)
                    .variables("first", first)
                    .variables("skip", skip)
                    .variables("orderBy", client.isCn() ? "DEFAULT" : "HOT")
                    .variables("userInput", "")
                    .variables("tagSlugs", new ArrayList<>())
                    .addOption(getOptions())
                    .request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                List<Solution> solutionList = new ArrayList<>();
                JSONObject data = JSONObject.parseObject(response.getBody()).getJSONObject("data");
                JSONObject articles = data == null ? null : data.getJSONObject(operationName);
                JSONArray edges = articles == null ? null : articles.getJSONArray("edges");
                if (edges == null) {
                    throw new LcException("SolutionList response is empty",
                            HttpClient.buildHttpTrace(response.getHttpRequest(), response));
                }
                for (int i = 0; i < edges.size(); i++) {
                    JSONObject node = edges.getJSONObject(i).getJSONObject("node");
                    if (node == null) {
                        continue;
                    }
                    Solution solution = node.toJavaObject(Solution.class);
                    JSONArray tagArray = node.getJSONArray("tags");
                    String tags = tagArray == null
                            ? ""
                            : tagArray.stream()
                            .map(tag -> JSONObject.from(tag).getString("name"))
                            .collect(Collectors.joining(","));
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

        private final String articleId;

        public SolutionArticle(String articleId,Option<?> ...option) {
            super(option);
            this.articleId = articleId;
        }

        @Override
        public String execute(HttpClient client) throws LcException {
            String operationName = client.isCn()
                    ? "solutionDetailArticle"
                    : "ugcArticleSolutionArticle";
            Graphql.GraphqlBuilder request = Graphql.builder(client.getGraphql())
                    .cn(client.isCn())
                    .header(client.getHeader())
                    .operationName("solutionDetailArticle", operationName);
            if (client.isCn()) {
                request.variables("slug", articleId)
                        .variables("orderBy", "DEFAULT");
            } else {
                request.variables("topicId", articleId);
            }
            HttpResponse response = request.addOption(getOptions())
                    .request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                JSONObject data = JSONObject.parseObject(response.getBody()).getJSONObject("data");
                JSONObject solutionArticleObject = data == null
                        ? null
                        : data.getJSONObject(client.isCn()
                        ? "solutionArticle"
                        : "ugcArticleSolutionArticle");
                if (solutionArticleObject == null) {
                    throw new LcException("SolutionArticle response is empty",
                            HttpClient.buildHttpTrace(response.getHttpRequest(), response));
                }
                String content;
                if (client.isCn()) {
                    content = solutionArticleObject.getString("content");
                    if (StringUtils.isBlank(content)) {
                        content = SlateMarkdownConverter.convert(
                                solutionArticleObject.getString("slateValue")
                        );
                    }
                } else {
                    content = solutionArticleObject.getString("content");
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
