package com.shuzijun.lc.command;

import com.alibaba.fastjson2.JSONObject;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.Graphql;
import com.shuzijun.lc.http.HttpClient;
import com.shuzijun.lc.http.HttpResponse;
import com.shuzijun.lc.model.FavoriteResult;
import com.shuzijun.lc.model.Question;
import com.shuzijun.lc.model.Tag;
import org.apache.commons.lang3.StringUtils;

public class FavoriteCommand {

    /**
     * 添加题目到收藏夹
     *
     * @param favoriteIdHash {@link FindCommand.Lists#buildLists()}返回列表中{@link Tag#getSlug()}
     * @param questionId     {@link Question#getQuestionId()} 题目id
     * @return {@link Boolean} 是否成功
     */
    public static AddQuestionToFavorite buildAddQuestionToFavorite(String favoriteIdHash, String questionId,Option<?> ...option) {
        return new AddQuestionToFavorite(favoriteIdHash, questionId,option);
    }

    /**
     * 从收藏夹移除题目
     *
     * @param favoriteIdHash {@link FindCommand.Lists#buildLists()}返回列表中{@link Tag#getSlug()}
     * @param questionId     {@link Question#getQuestionId()} 题目id
     * @return {@link Boolean} 是否成功
     */
    public static RemoveQuestionFromFavorite buildRemoveQuestionFromFavorite(String favoriteIdHash, String questionId,Option<?> ...option) {
        return new RemoveQuestionFromFavorite(favoriteIdHash, questionId,option);
    }


    public static class AddQuestionToFavorite  extends OptionCommand implements Command<FavoriteResult> {

        private final String favoriteIdHash;

        private final String questionId;

        public AddQuestionToFavorite(String favoriteIdHash, String questionId,Option<?> ...option) {
            super(option);
            this.favoriteIdHash = favoriteIdHash;
            this.questionId = questionId;
        }

        @Override
        public FavoriteResult execute(HttpClient client) throws LcException {
            HttpResponse response = Graphql.builder(client.getGraphql()).header(client.getHeader())
                    .operationName("addQuestionToFavorite")
                    .variables("favoriteIdHash", favoriteIdHash).variables("questionId", questionId)
                    .addOption(getOptions())
                    .request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                String body = response.getBody();
                JSONObject object = JSONObject.parseObject(body).getJSONObject("data").getJSONObject("addQuestionToFavorite");
                return object.to(FavoriteResult.class);
            } else {
                throw new LcException("AddQuestionToFavorite fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }


    public static class RemoveQuestionFromFavorite extends OptionCommand implements Command<FavoriteResult> {

        private final String favoriteIdHash;

        private final String questionId;

        public RemoveQuestionFromFavorite(String favoriteIdHash, String questionId,Option<?> ...option) {
            super(option);
            this.favoriteIdHash = favoriteIdHash;
            this.questionId = questionId;
        }

        @Override
        public FavoriteResult execute(HttpClient client) throws LcException {
            HttpResponse response = Graphql.builder(client.getGraphql()).header(client.getHeader())
                    .operationName("removeQuestionFromFavorite")
                    .variables("favoriteIdHash", favoriteIdHash).variables("questionId", questionId)
                    .addOption(getOptions())
                    .request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                String body = response.getBody();
                JSONObject object = JSONObject.parseObject(body).getJSONObject("data").getJSONObject("removeQuestionFromFavorite");
                return object.to(FavoriteResult.class);
            } else {
                throw new LcException("RemoveQuestionFromFavorite fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }
}
