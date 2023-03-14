package com.shuzijun.lc.command;

import com.alibaba.fastjson2.JSONObject;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.Graphql;
import com.shuzijun.lc.http.HttpClient;
import com.shuzijun.lc.http.HttpResponse;
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
    public static AddQuestionToFavorite buildAddQuestionToFavorite(String favoriteIdHash, String questionId) {
        return new AddQuestionToFavorite(favoriteIdHash, questionId);
    }

    /**
     * 从收藏夹移除题目
     *
     * @param favoriteIdHash {@link FindCommand.Lists#buildLists()}返回列表中{@link Tag#getSlug()}
     * @param questionId     {@link Question#getQuestionId()} 题目id
     * @return {@link Boolean} 是否成功
     */
    public static RemoveQuestionFromFavorite buildRemoveQuestionFromFavorite(String favoriteIdHash, String questionId) {
        return new RemoveQuestionFromFavorite(favoriteIdHash, questionId);
    }


    public static class AddQuestionToFavorite implements Command<Boolean> {

        private final String favoriteIdHash;

        private final String questionId;

        public AddQuestionToFavorite(String favoriteIdHash, String questionId) {
            this.favoriteIdHash = favoriteIdHash;
            this.questionId = questionId;
        }

        @Override
        public Boolean execute(HttpClient client) throws LcException {
            HttpResponse response = Graphql.builder(client.getGraphql()).header(client.getHeader())
                    .operationName("addQuestionToFavorite")
                    .variables("favoriteIdHash", favoriteIdHash).variables("questionId", questionId)
                    .request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                String body = response.getBody();
                JSONObject object = JSONObject.parseObject(body).getJSONObject("data").getJSONObject("addQuestionToFavorite");
                return object.getBoolean("ok");
            } else {
                throw new LcException("AddQuestionToFavorite fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }


    public static class RemoveQuestionFromFavorite implements Command<Boolean> {

        private final String favoriteIdHash;

        private final String questionId;

        public RemoveQuestionFromFavorite(String favoriteIdHash, String questionId) {
            this.favoriteIdHash = favoriteIdHash;
            this.questionId = questionId;
        }

        @Override
        public Boolean execute(HttpClient client) throws LcException {
            HttpResponse response = Graphql.builder(client.getGraphql()).header(client.getHeader())
                    .operationName("removeQuestionFromFavorite")
                    .variables("favoriteIdHash", favoriteIdHash).variables("questionId", questionId)
                    .request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                String body = response.getBody();
                JSONObject object = JSONObject.parseObject(body).getJSONObject("data").getJSONObject("removeQuestionFromFavorite");
                return object.getBoolean("ok");
            } else {
                throw new LcException("RemoveQuestionFromFavorite fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }
}
