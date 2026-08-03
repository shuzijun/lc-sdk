package com.shuzijun.lc.command;

import com.alibaba.fastjson2.JSONObject;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.Graphql;
import com.shuzijun.lc.http.HttpClient;
import com.shuzijun.lc.http.HttpResponse;
import com.shuzijun.lc.model.NoteUpdateResult;
import org.apache.commons.lang3.StringUtils;

public final class NoteCommand {

    private NoteCommand() {
    }

    public static GetNote buildGetNote(String titleSlug, Option<?>... options) {
        return new GetNote(titleSlug, options);
    }

    public static UpdateNote buildUpdateNote(String titleSlug, String content, Option<?>... options) {
        return new UpdateNote(titleSlug, content, options);
    }

    public static UpdateNoteResult buildUpdateNoteResult(
            String titleSlug,
            String content,
            Option<?>... options
    ) {
        return new UpdateNoteResult(titleSlug, content, options);
    }

    public static final class GetNote extends OptionCommand implements Command<String> {
        private final String titleSlug;

        private GetNote(String titleSlug, Option<?>... options) {
            super(options);
            this.titleSlug = requireTitleSlug(titleSlug);
        }

        @Override
        public String execute(HttpClient client) throws LcException {
            HttpResponse response = Graphql.builder(client.getGraphql())
                    .header(client.getHeader())
                    .operationName("getNote")
                    .variables("titleSlug", titleSlug)
                    .addOption(getOptions())
                    .request(client.getExecutorHttp());
            if (!response.isCodeSuccess() || StringUtils.isBlank(response.getBody())) {
                throw new LcException("GetNote fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
            JSONObject question = JSONObject.parseObject(response.getBody())
                    .getJSONObject("data")
                    .getJSONObject("question");
            return question == null ? null : question.getString("note");
        }
    }

    public static final class UpdateNote extends OptionCommand implements Command<Boolean> {
        private final String titleSlug;
        private final String content;

        private UpdateNote(String titleSlug, String content, Option<?>... options) {
            super(options);
            this.titleSlug = requireTitleSlug(titleSlug);
            this.content = content == null ? "" : content;
        }

        @Override
        public Boolean execute(HttpClient client) throws LcException {
            HttpResponse response = Graphql.builder(client.getGraphql())
                    .header(client.getHeader())
                    .operationName("updateNote")
                    .variables("titleSlug", titleSlug)
                    .variables("content", content)
                    .addOption(getOptions())
                    .request(client.getExecutorHttp());
            if (!response.isCodeSuccess() || StringUtils.isBlank(response.getBody())) {
                throw new LcException("UpdateNote fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
            JSONObject update = JSONObject.parseObject(response.getBody())
                    .getJSONObject("data")
                    .getJSONObject("updateNote");
            return update != null && Boolean.TRUE.equals(update.getBoolean("ok"));
        }
    }

    public static final class UpdateNoteResult extends OptionCommand
            implements Command<NoteUpdateResult> {
        private final String titleSlug;
        private final String content;

        private UpdateNoteResult(String titleSlug, String content, Option<?>... options) {
            super(options);
            this.titleSlug = requireTitleSlug(titleSlug);
            this.content = content == null ? "" : content;
        }

        @Override
        public NoteUpdateResult execute(HttpClient client) throws LcException {
            HttpResponse response = Graphql.builder(client.getGraphql())
                    .header(client.getHeader())
                    .operationName("updateNote")
                    .variables("titleSlug", titleSlug)
                    .variables("content", content)
                    .addOption(getOptions())
                    .request(client.getExecutorHttp());
            if (!response.isCodeSuccess() || StringUtils.isBlank(response.getBody())) {
                throw new LcException(
                        "UpdateNoteResult fail",
                        HttpClient.buildHttpTrace(response.getHttpRequest(), response)
                );
            }
            JSONObject update = JSONObject.parseObject(response.getBody())
                    .getJSONObject("data")
                    .getJSONObject("updateNote");
            NoteUpdateResult result = new NoteUpdateResult();
            if (update == null) {
                return result;
            }
            result.setSuccess(Boolean.TRUE.equals(update.getBoolean("ok")));
            result.setError(update.getString("error"));
            JSONObject question = update.getJSONObject("question");
            result.setNote(question == null ? null : question.getString("note"));
            return result;
        }
    }

    private static String requireTitleSlug(String titleSlug) {
        if (StringUtils.isBlank(titleSlug)) {
            throw new IllegalArgumentException("Title slug must not be blank");
        }
        return titleSlug;
    }
}
