package com.shuzijun.lc.command;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.Graphql;
import com.shuzijun.lc.http.HttpClient;
import com.shuzijun.lc.http.HttpResponse;
import com.shuzijun.lc.model.CommonNote;
import com.shuzijun.lc.model.CommonNotePage;
import com.shuzijun.lc.model.CommonNoteResult;
import com.shuzijun.lc.model.NoteUpdateResult;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;

public final class NoteCommand {

    private static final String COMMON_QUESTION = "COMMON_QUESTION";

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

    public static CommonNoteList buildCommonNoteList(
            String targetId,
            int limit,
            int skip,
            Option<?>... options
    ) {
        return new CommonNoteList(targetId, limit, skip, options);
    }

    public static CreateCommonNote buildCreateCommonNote(
            String targetId,
            String content,
            String summary,
            Option<?>... options
    ) {
        return new CreateCommonNote(targetId, content, summary, options);
    }

    public static UpdateCommonNote buildUpdateCommonNote(
            String noteId,
            String content,
            String summary,
            Option<?>... options
    ) {
        return new UpdateCommonNote(noteId, content, summary, options);
    }

    public static DeleteCommonNote buildDeleteCommonNote(String noteId, Option<?>... options) {
        return new DeleteCommonNote(noteId, options);
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

    public static final class CommonNoteList extends OptionCommand
            implements Command<CommonNotePage> {
        private final String targetId;
        private final int limit;
        private final int skip;

        private CommonNoteList(String targetId, int limit, int skip, Option<?>... options) {
            super(options);
            this.targetId = requireId(targetId, "Target ID");
            if (limit <= 0 || skip < 0) {
                throw new IllegalArgumentException("Limit must be positive and skip must not be negative");
            }
            this.limit = limit;
            this.skip = skip;
        }

        @Override
        public CommonNotePage execute(HttpClient client) throws LcException {
            HttpResponse response = Graphql.builder(client.getGraphql())
                    .header(client.getHeader())
                    .operationName("noteOneTargetCommonNote")
                    .variables("noteType", COMMON_QUESTION)
                    .variables("targetId", targetId)
                    .variables("limit", limit)
                    .variables("skip", skip)
                    .addOption(getOptions())
                    .request(client.getExecutorHttp());
            JSONObject payload = requirePayload(response, "noteOneTargetCommonNote");
            CommonNotePage result = new CommonNotePage();
            if (payload == null) {
                return result;
            }
            result.setCount(payload.getIntValue("count"));
            JSONArray notes = payload.getJSONArray("userNotes");
            result.setNotes(notes == null
                    ? Collections.emptyList()
                    : notes.toJavaList(CommonNote.class));
            return result;
        }
    }

    public static final class CreateCommonNote extends OptionCommand
            implements Command<CommonNoteResult> {
        private final String targetId;
        private final String content;
        private final String summary;

        private CreateCommonNote(
                String targetId,
                String content,
                String summary,
                Option<?>... options
        ) {
            super(options);
            this.targetId = requireId(targetId, "Target ID");
            this.content = content == null ? "" : content;
            this.summary = summary == null ? "" : summary;
        }

        @Override
        public CommonNoteResult execute(HttpClient client) throws LcException {
            HttpResponse response = Graphql.builder(client.getGraphql())
                    .header(client.getHeader())
                    .operationName("noteCreateCommonNote")
                    .variables("content", content)
                    .variables("noteType", COMMON_QUESTION)
                    .variables("targetId", targetId)
                    .variables("summary", summary)
                    .addOption(getOptions())
                    .request(client.getExecutorHttp());
            return commonNoteResult(requirePayload(response, "noteCreateCommonNote"));
        }
    }

    public static final class UpdateCommonNote extends OptionCommand
            implements Command<CommonNoteResult> {
        private final String noteId;
        private final String content;
        private final String summary;

        private UpdateCommonNote(
                String noteId,
                String content,
                String summary,
                Option<?>... options
        ) {
            super(options);
            this.noteId = requireId(noteId, "Note ID");
            this.content = content == null ? "" : content;
            this.summary = summary == null ? "" : summary;
        }

        @Override
        public CommonNoteResult execute(HttpClient client) throws LcException {
            HttpResponse response = Graphql.builder(client.getGraphql())
                    .header(client.getHeader())
                    .operationName("noteUpdateUserNote")
                    .variables("content", content)
                    .variables("noteId", noteId)
                    .variables("summary", summary)
                    .addOption(getOptions())
                    .request(client.getExecutorHttp());
            return commonNoteResult(requirePayload(response, "noteUpdateUserNote"));
        }
    }

    public static final class DeleteCommonNote extends OptionCommand
            implements Command<Boolean> {
        private final String noteId;

        private DeleteCommonNote(String noteId, Option<?>... options) {
            super(options);
            this.noteId = requireId(noteId, "Note ID");
        }

        @Override
        public Boolean execute(HttpClient client) throws LcException {
            HttpResponse response = Graphql.builder(client.getGraphql())
                    .header(client.getHeader())
                    .operationName("noteDeleteUserNote")
                    .variables("noteId", noteId)
                    .addOption(getOptions())
                    .request(client.getExecutorHttp());
            JSONObject payload = requirePayload(response, "noteDeleteUserNote");
            return payload != null && Boolean.TRUE.equals(payload.getBoolean("ok"));
        }
    }

    private static JSONObject requirePayload(HttpResponse response, String field) throws LcException {
        if (!response.isCodeSuccess() || StringUtils.isBlank(response.getBody())) {
            throw new LcException(
                    field + " fail",
                    HttpClient.buildHttpTrace(response.getHttpRequest(), response)
            );
        }
        JSONObject data = JSONObject.parseObject(response.getBody()).getJSONObject("data");
        return data == null ? null : data.getJSONObject(field);
    }

    private static CommonNoteResult commonNoteResult(JSONObject payload) {
        CommonNoteResult result = new CommonNoteResult();
        if (payload == null) {
            return result;
        }
        result.setSuccess(Boolean.TRUE.equals(payload.getBoolean("ok")));
        JSONObject note = payload.getJSONObject("note");
        result.setNote(note == null ? null : note.toJavaObject(CommonNote.class));
        return result;
    }

    private static String requireTitleSlug(String titleSlug) {
        if (StringUtils.isBlank(titleSlug)) {
            throw new IllegalArgumentException("Title slug must not be blank");
        }
        return titleSlug;
    }

    private static String requireId(String value, String name) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
