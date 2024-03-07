package com.shuzijun.lc.command;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.HttpClient;
import com.shuzijun.lc.http.HttpRequest;
import com.shuzijun.lc.http.HttpResponse;
import com.shuzijun.lc.model.Tag;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FindCommand {

    public static final String DIFFICULTY_EASY = "Easy";
    public static final String DIFFICULTY_MEDIUM = "Medium";
    public static final String DIFFICULTY_HARD = "Hard";
    public static final String DIFFICULTY_UNKNOWN = "Unknown";

    public static final String STATUS_TODO = "Todo";
    public static final String STATUS_SOLVED = "Solved";
    public static final String STATUS_ATTEMPTED = "Attempted";

    /**
     * 构建获取tab
     *
     * @return {@link List<Tag>} Tag列表
     */
    public static Tags buildTags(Option<?>... option) {
        return new Tags(option);
    }

    /**
     * 构建获取列表
     *
     * @return {@link List<Tag>} List列表
     */
    public static Lists buildLists(Option<?>... option) {
        return new Lists(option);
    }

    /**
     * 构建获取题目列表
     *
     * @return {@link List<Tag>} Category列表
     */
    public static Category buildCategory(Option<?>... option) {
        return new Category(option);
    }

    /**
     * 构建获取难度
     *
     * @return {@link List<Tag>} Category列表
     */
    public static Difficulty buildDifficulty(Option<?>... option) {
        return new Difficulty(option);
    }

    /**
     * 构建获取状态
     *
     * @return {@link List<Tag>} Category列表
     */
    public static Status buildStatus(Option<?>... option) {
        return new Status(option);
    }


    public static class Tags extends OptionCommand implements Command<List<Tag>> {

        public Tags(Option<?>... option) {
            super(option);
        }

        @Override
        public List<Tag> execute(HttpClient client) throws LcException {
            HttpResponse response = HttpRequest.builderGet(client.getTags()).addHeader(client.getHeader()).addOption(getOptions()).request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                return JSONObject.parseObject(response.getBody()).getJSONArray("topics").toJavaList(Tag.class);
            } else {
                throw new LcException("Tags fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }

    public static class Lists extends OptionCommand implements Command<List<Tag>> {
        public Lists(Option<?>... option) {
            super(option);
        }

        @Override
        public List<Tag> execute(HttpClient client) throws LcException {

            HttpResponse response = HttpRequest.builderGet(client.getFavorites()).addHeader(client.getHeader()).addOption(getOptions()).request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                return JSONArray.parseArray(response.getBody()).toJavaList(Tag.class);
            } else {
                throw new LcException("Lists fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }

    public static class Category extends OptionCommand implements Command<List<Tag>> {
        public Category(Option<?>... option) {
            super(option);
        }

        @Override
        public List<Tag> execute(HttpClient client) throws LcException {

            HttpResponse response = HttpRequest.builderGet(client.getCardInfo()).addHeader(client.getHeader()).addOption(getOptions()).request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
                return JSONObject.parseObject(response.getBody()).getJSONObject("categories").getJSONArray("0").toJavaList(Tag.class);
            } else {
                throw new LcException("Category fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }

    public static class Difficulty extends OptionCommand implements Command<List<Tag>> {
        public Difficulty(Option<?>... option) {
            super(option);
        }

        @Override
        public List<Tag> execute(HttpClient client) throws LcException {
            List<String> keyList = new ArrayList<>();
            Collections.addAll(keyList, DIFFICULTY_EASY, DIFFICULTY_MEDIUM, DIFFICULTY_HARD);
            List<Tag> difficultyList = new ArrayList<>();
            for (String key : keyList) {
                Tag tag = new Tag();
                tag.setName(key);
                tag.setSlug(key.toUpperCase());
                difficultyList.add(tag);
            }
            return difficultyList;
        }
    }

    public static class Status extends OptionCommand implements Command<List<Tag>> {
        public Status(Option<?>... option) {
            super(option);
        }

        @Override
        public List<Tag> execute(HttpClient client) throws LcException {
            List<String> keyList = new ArrayList<>();
            Collections.addAll(keyList, STATUS_TODO, STATUS_SOLVED, STATUS_ATTEMPTED);
            List<Tag> statusList = new ArrayList<>();
            for (String key : keyList) {
                Tag tag = new Tag();
                tag.setName(key);
                if (STATUS_TODO.equals(key)) {
                    tag.setSlug("NOT_STARTED");
                } else if (STATUS_SOLVED.equals(key)) {
                    tag.setSlug("AC");
                } else if (STATUS_ATTEMPTED.equals(key)) {
                    tag.setSlug("TRIED");
                }
                statusList.add(tag);
            }
            return statusList;
        }
    }
}
