package com.shuzijun.lc.command;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.HttpClient;
import com.shuzijun.lc.http.HttpRequest;
import com.shuzijun.lc.http.HttpResponse;
import com.shuzijun.lc.model.Tag;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class FindCommand {

    /**
     * 构建获取题目列表
     *
     * @return {@link List<Tag>} Tag列表
     */
    public static Tags buildTags() {
        return new Tags();
    }

    /**
     * 构建获取题目列表
     *
     * @return {@link List<Tag>} List列表
     */
    public static Lists buildLists() {
        return new Lists();
    }

    /**
     * 构建获取题目列表
     *
     * @return {@link List<Tag>} Category列表
     */
    public static Category buildCategory() {
        return new Category();
    }


    public static class Tags implements Command<List<Tag>> {

        @Override
        public List<Tag> execute(HttpClient client) throws LcException {
            HttpResponse response = HttpRequest.builderGet(client.getTags()).addHeader(client.getHeader()).request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                return JSONObject.parseObject(response.getBody()).getJSONArray("topics").toJavaList(Tag.class);
            } else {
                throw new LcException("Tags fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }

    public static class Lists implements Command<List<Tag>> {
        @Override
        public List<Tag> execute(HttpClient client) throws LcException {

            HttpResponse response = HttpRequest.builderGet(client.getFavorites()).addHeader(client.getHeader())
                    .request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                return JSONArray.parseArray(response.getBody()).toJavaList(Tag.class);
            } else {
                throw new LcException("Lists fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }

    public static class Category implements Command<List<Tag>> {
        @Override
        public List<Tag> execute(HttpClient client) throws LcException {

            HttpResponse response = HttpRequest.builderGet(client.getCardInfo()).addHeader(client.getHeader()).request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                return JSONObject.parseObject(response.getBody()).getJSONObject("categories").getJSONArray("0").toJavaList(Tag.class);
            } else {
                throw new LcException("Category fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }
}
