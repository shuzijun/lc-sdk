package com.shuzijun.lc;

import com.shuzijun.lc.command.LoginCommand;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.CookieStore;
import com.shuzijun.lc.http.ExecutorHttp;
import com.shuzijun.lc.http.HttpClient;
import com.shuzijun.lc.http.HttpRequest;
import com.shuzijun.lc.http.HttpResponse;
import com.shuzijun.lc.model.PageInfo;
import com.shuzijun.lc.model.FavoriteResult;
import com.shuzijun.lc.model.NoteUpdateResult;
import com.shuzijun.lc.model.ProblemSetParam;
import com.shuzijun.lc.model.Question;
import com.shuzijun.lc.model.QuestionView;
import com.shuzijun.lc.model.RunCodeCheckResult;
import com.shuzijun.lc.model.RunCodeParam;
import com.shuzijun.lc.model.RunCodeResult;
import com.shuzijun.lc.model.Session;
import com.shuzijun.lc.model.Submission;
import com.shuzijun.lc.model.SubmissionDetail;
import com.shuzijun.lc.model.SubmitCheckResult;
import com.shuzijun.lc.model.SubmitParam;
import com.shuzijun.lc.model.SubmitResult;
import com.shuzijun.lc.model.Tag;
import com.shuzijun.lc.model.User;
import com.alibaba.fastjson2.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class LcApiTest {

    @Test
    public void testNotes_getAndUpdateReturnTypedResults() throws LcException {
        QueueExecutor executor = new QueueExecutor();
        executor.add(new HttpResponse(200,
                "{\"data\":{\"question\":{\"questionId\":\"1\",\"note\":\"stored note\"}}}"));
        executor.add(new HttpResponse(200,
                "{\"data\":{\"updateNote\":{\"ok\":true,\"error\":null}}}"));
        executor.add(new HttpResponse(200,
                "{\"data\":{\"updateNote\":{\"ok\":false,\"error\":\"note rejected\","
                        + "\"question\":{\"questionId\":\"1\",\"note\":\"server note\"}}}}"));
        LcClient client = client(HttpClient.SiteEnum.CN, executor);

        Assert.assertEquals("stored note", client.api().notes().get("two-sum", null));
        Assert.assertTrue(client.api().notes().update("two-sum", "changed", null));
        Assert.assertTrue(executor.lastRequest.getBody().contains("\"content\":\"changed\""));
        NoteUpdateResult failure = client.api().notes()
                .updateResult("two-sum", "rejected", RequestContext.DEFAULT);
        Assert.assertFalse(failure.isSuccess());
        Assert.assertEquals("note rejected", failure.getError());
        Assert.assertEquals("server note", failure.getNote());
        Assert.assertTrue(executor.lastRequest.getBody().contains("\"content\":\"rejected\""));
    }

    @Test
    public void testAccount_loginUsesMultipartAndRejectsFormErrors() throws LcException {
        QueueExecutor executor = new QueueExecutor();
        executor.add(new HttpResponse(200, "{\"form\":{\"errors\":[]}}"));
        executor.add(new HttpResponse(200, "{\"form\":{\"errors\":[\"bad password\"]}}"));
        LcClient client = client(HttpClient.SiteEnum.CN, executor);

        LoginCommand.LoginResult success = client.api().account()
                .login("user@example.com", "secret", "csrf", RequestContext.DEFAULT);
        Assert.assertTrue(success.isSuccess());
        Assert.assertTrue(executor.lastRequest.getContentType().startsWith("multipart/form-data; boundary="));
        Assert.assertTrue(executor.lastRequest.getBody().contains("name=\"login\""));
        Assert.assertTrue(executor.lastRequest.getBody().contains("user@example.com"));

        LoginCommand.LoginResult failure = client.api().account()
                .login("user@example.com", "wrong", "csrf", RequestContext.DEFAULT);
        Assert.assertFalse(failure.isSuccess());
    }

    @Test
    public void testAccount_loginIsUnavailableForEnglishSite() throws LcException {
        QueueExecutor executor = new QueueExecutor();
        LcClient client = client(HttpClient.SiteEnum.EN, executor);

        LoginCommand.LoginResult result = client.api().account()
                .login("user@example.com", "secret", "csrf", null);

        Assert.assertFalse(result.isSuccess());
        Assert.assertNull(executor.lastRequest);
    }

    @Test
    public void testAccount_cnUserRequestsAndMapsPremiumProfile() throws LcException {
        QueueExecutor executor = new QueueExecutor();
        executor.add(new HttpResponse(200,
                "{\"data\":{\"userStatus\":{\"isPremium\":true,\"username\":\"premium-user\","
                        + "\"userSlug\":\"premium-profile\",\"realName\":\"Premium User\","
                        + "\"isSignedIn\":true,\"isVerified\":true,\"isPhoneVerified\":true}}}"));
        LcClient client = client(HttpClient.SiteEnum.CN, executor);

        User user = client.api().account().user(RequestContext.DEFAULT);

        JSONObject requestBody = JSONObject.parseObject(executor.lastRequest.getBody());
        Assert.assertEquals("http://127.0.0.1:8080/graphql/noj-go", executor.lastRequest.getUrl());
        Assert.assertEquals("userStatusGlobal", requestBody.getString("operationName"));
        Assert.assertTrue(requestBody.getString("query").contains("isPremium"));
        Assert.assertTrue(user.isPremium());
        Assert.assertEquals("premium-profile", user.getUserSlug());
        Assert.assertEquals("Premium User", user.getRealName());
    }

    @Test
    public void testAccount_cnGlobalDataModePreservesCommunityRequestContract() throws LcException {
        QueueExecutor executor = new QueueExecutor();
        executor.add(new HttpResponse(200,
                "{\"data\":{\"userStatus\":{\"isPremium\":false,\"username\":\"community-user\","
                        + "\"userSlug\":\"community-profile\",\"realName\":\"Community User\","
                        + "\"isSignedIn\":true,\"isVerified\":true,\"isPhoneVerified\":true}}}"));
        LcClient client = client(HttpClient.SiteEnum.CN, executor);

        User user = client.api().account().user(UserQueryMode.GLOBAL_DATA, RequestContext.DEFAULT);

        JSONObject requestBody = JSONObject.parseObject(executor.lastRequest.getBody());
        Assert.assertEquals("http://127.0.0.1:8080/graphql", executor.lastRequest.getUrl());
        Assert.assertEquals("globalData", requestBody.getString("operationName"));
        Assert.assertTrue(requestBody.getString("query").contains("query globalData"));
        Assert.assertEquals("community-profile", user.getUserSlug());
        Assert.assertEquals("Community User", user.getRealName());
    }

    @Test
    public void testQuestions_cnListMapsTypedFieldsAndRequestContract() throws LcException {
        QueueExecutor executor = new QueueExecutor();
        executor.add(new HttpResponse(200,
                "{\"data\":{\"problemsetQuestionList\":{\"total\":1,\"questions\":[{"
                        + "\"acRate\":50.0,\"difficulty\":\"Medium\",\"freqBar\":25.0,"
                        + "\"frontendQuestionId\":\"1\",\"paidOnly\":true,"
                        + "\"solutionNum\":3,\"status\":\"TRIED\",\"title\":\"Two Sum\","
                        + "\"titleCn\":\"两数之和\",\"titleSlug\":\"two-sum\"}]}}}"));
        LcClient client = client(HttpClient.SiteEnum.CN, executor);
        ProblemSetParam param = new ProblemSetParam(2, 50);
        param.setCategorySlug("algorithms");
        param.getFilters().setSearchKeywords("two");

        PageInfo<QuestionView> page = client.api().questions().list(param, RequestContext.DEFAULT);

        JSONObject requestBody = JSONObject.parseObject(executor.lastRequest.getBody());
        Assert.assertEquals("problemsetQuestionList", requestBody.getString("operationName"));
        Assert.assertTrue(requestBody.getJSONObject("variables").toJSONString()
                .contains("\"skip\":50"));
        Assert.assertEquals(1, page.getRowTotal());
        Assert.assertEquals(1, page.getRows().size());
        QuestionView question = page.getRows().get(0);
        Assert.assertEquals("两数之和", question.getTitleCn());
        Assert.assertEquals(Integer.valueOf(2), question.getLevel());
        Assert.assertEquals(0.5d, question.getAcceptance(), 0.0001d);
        Assert.assertEquals(0.25d, question.getFrequency(), 0.0001d);
        Assert.assertTrue(question.isPaidOnly());
        Assert.assertEquals("tried", question.getStatus());
    }

    @Test
    public void testQuestions_detailParsesMetadataStringAndRichFields() throws LcException {
        QueueExecutor executor = new QueueExecutor();
        executor.add(new HttpResponse(200,
                "{\"data\":{\"question\":{"
                        + "\"questionId\":\"1\",\"frontendQuestionId\":\"1\","
                        + "\"title\":\"Two Sum\",\"titleCn\":\"两数之和\","
                        + "\"titleSlug\":\"two-sum\",\"content\":\"<p>content</p>\","
                        + "\"translatedContent\":\"<p>内容</p>\",\"difficulty\":\"Easy\","
                        + "\"exampleTestcases\":\"[2,7]\\n9\",\"testCase\":\"[2,7]\\n9\","
                        + "\"likes\":100,\"dislikes\":5,"
                        + "\"topicTags\":[{\"name\":\"Array\",\"slug\":\"array\","
                        + "\"translatedName\":\"数组\"}],"
                        + "\"similarQuestions\":\"[]\",\"hints\":[\"Use a map\"],"
                        + "\"codeSnippets\":[{\"lang\":\"Java\",\"langSlug\":\"java\","
                        + "\"code\":\"class Solution {\\n}\"}],"
                        + "\"metaData\":\"{\\\"name\\\":\\\"twoSum\\\","
                        + "\\\"params\\\":[{\\\"name\\\":\\\"nums\\\",\\\"type\\\":\\\"integer[]\\\"}],"
                        + "\\\"return\\\":{\\\"type\\\":\\\"integer[]\\\"}}\"}}}"));
        LcClient client = client(HttpClient.SiteEnum.EN, executor);

        Question question = client.api().questions().get("two-sum", RequestContext.DEFAULT);

        Assert.assertEquals("two-sum", question.getTitleSlug());
        Assert.assertEquals("两数之和", question.getTitleCn());
        Assert.assertEquals("<p>内容</p>", question.getTranslatedContent());
        Assert.assertEquals(Integer.valueOf(100), question.getLikes());
        Assert.assertEquals(Integer.valueOf(5), question.getDislikes());
        Assert.assertEquals("Array", question.getTopicTags().get(0).getName());
        Assert.assertEquals("数组", question.getTopicTags().get(0).getTranslatedName());
        Assert.assertEquals("Use a map", question.getHints().get(0));
        Assert.assertEquals("class Solution {\n}", question.getCodeSnippets().get(0).getCode());
        Assert.assertNotNull(question.getCodeMetaData());
        Assert.assertEquals("twoSum", question.getCodeMetaData().getName());
        Assert.assertEquals(1, question.getCodeMetaData().getParams().size());
        Assert.assertEquals(
                "questionData",
                JSONObject.parseObject(executor.lastRequest.getBody()).getString("operationName")
        );
    }

    @Test
    public void testCode_runAndChecksPreserveTypedIdsAndArrayResults() throws LcException {
        QueueExecutor executor = new QueueExecutor();
        executor.add(new HttpResponse(200,
                "{\"interpret_id\":\"run-1\",\"interpret_expected_id\":\"expected-1\","
                        + "\"test_case\":\"[2,7]\\n9\"}"));
        executor.add(new HttpResponse(200,
                "{\"state\":\"SUCCESS\",\"run_success\":true,"
                        + "\"code_answer\":[\"[0,1]\"],\"code_output\":[\"stdout\"],"
                        + "\"expected_code_answer\":[\"[0,1]\"]}"));
        executor.add(new HttpResponse(200,
                "{\"state\":\"SUCCESS\",\"run_success\":true,"
                        + "\"code_answer\":[\"[0,1]\"],\"code_output\":[]}"));
        LcClient client = client(HttpClient.SiteEnum.CN, executor);

        RunCodeResult started = client.api().code().run(
                new RunCodeParam("1", "two-sum", "[2,7]\n9", "java", "class Solution {}"),
                RequestContext.DEFAULT
        );

        Assert.assertEquals("http://127.0.0.1:8080/problems/two-sum/interpret_solution/",
                executor.lastRequest.getUrl());
        JSONObject requestBody = JSONObject.parseObject(executor.lastRequest.getBody());
        Assert.assertEquals("1", requestBody.getString("question_id"));
        Assert.assertEquals("[2,7]\n9", requestBody.getString("data_input"));
        Assert.assertEquals("java", requestBody.getString("lang"));
        Assert.assertEquals("large", requestBody.getString("judge_type"));
        Assert.assertEquals("class Solution {}", requestBody.getString("typed_code"));
        Assert.assertFalse(requestBody.containsKey("titleSlug"));
        Assert.assertEquals("run-1", started.getInterpretId());
        Assert.assertEquals("expected-1", started.getExpectedInterpretId());
        Assert.assertEquals("[2,7]\n9", started.getTestCase());
        Assert.assertEquals(Integer.valueOf(200), started.getHttpStatueCode());

        RunCodeCheckResult expected = client.api().code()
                .runResult(started.getExpectedInterpretId(), RequestContext.DEFAULT);
        Assert.assertEquals("expected-1",
                executor.lastRequest.getUrl().substring(
                        executor.lastRequest.getUrl().indexOf("/detail/") + 8,
                        executor.lastRequest.getUrl().indexOf("/check/")
                ));
        Assert.assertEquals("SUCCESS", expected.getState());
        Assert.assertEquals("[0,1]", expected.getCodeAnswers().get(0));
        Assert.assertEquals("stdout", expected.getCodeOutputs().get(0));
        Assert.assertEquals("[0,1]", expected.getExpectedCodeAnswers().get(0));

        RunCodeCheckResult actual = client.api().code()
                .runResult(started.getInterpretId(), RequestContext.DEFAULT);
        Assert.assertEquals("[0,1]", actual.getCodeAnswers().get(0));
        Assert.assertTrue(actual.getCodeOutputs().isEmpty());
    }

    @Test
    public void testCode_submitSupportsOpaqueIdsAndTypedCheckResult() throws LcException {
        QueueExecutor executor = new QueueExecutor();
        executor.add(new HttpResponse(200, "{\"submission_id\":\"submit-1\"}"));
        executor.add(new HttpResponse(200,
                "{\"state\":\"SUCCESS\",\"run_success\":true,\"status_code\":10,"
                        + "\"status_runtime\":\"1 ms\",\"runtime_percentile\":50.25,"
                        + "\"status_memory\":\"10 MB\",\"memory_percentile\":60.75}"));
        LcClient client = client(HttpClient.SiteEnum.EN, executor);

        SubmitResult started = client.api().code().submit(
                new SubmitParam("class Solution {}", "java", "two-sum", "1"),
                RequestContext.DEFAULT
        );

        Assert.assertEquals("http://127.0.0.1:8080/problems/two-sum/submit/",
                executor.lastRequest.getUrl());
        JSONObject requestBody = JSONObject.parseObject(executor.lastRequest.getBody());
        Assert.assertEquals("1", requestBody.getString("question_id"));
        Assert.assertEquals("java", requestBody.getString("lang"));
        Assert.assertEquals("class Solution {}", requestBody.getString("typed_code"));
        Assert.assertFalse(requestBody.containsKey("titleSlug"));
        Assert.assertEquals("submit-1", started.getSubmissionIdValue());
        Assert.assertNull(started.getSubmissionId());
        Assert.assertEquals(Integer.valueOf(200), started.getHttpStatueCode());

        SubmitCheckResult result = client.api().code()
                .submitResultById(started.getSubmissionIdValue(), RequestContext.DEFAULT);

        Assert.assertEquals("http://127.0.0.1:8080/submissions/detail/submit-1/check/",
                executor.lastRequest.getUrl());
        Assert.assertEquals("SUCCESS", result.getState());
        Assert.assertTrue(result.getRunSuccess());
        Assert.assertEquals(Integer.valueOf(10), result.getStatusCode());
        Assert.assertEquals("1 ms", result.getStatusRuntime());
        Assert.assertEquals("50.25", result.getRuntimePercentile().toPlainString());
        Assert.assertEquals("60.75", result.getMemoryPercentile().toPlainString());
    }

    @Test
    public void testCode_rateLimitReturnsTypedStartStatus() throws LcException {
        QueueExecutor executor = new QueueExecutor();
        executor.add(new HttpResponse(429, ""));
        executor.add(new HttpResponse(429, ""));
        LcClient client = client(HttpClient.SiteEnum.EN, executor);

        RunCodeResult run = client.api().code().run(
                new RunCodeParam("1", "two-sum", "[]", "java", "code"),
                RequestContext.DEFAULT
        );
        SubmitResult submit = client.api().code().submit(
                new SubmitParam("code", "java", "two-sum", "1"),
                RequestContext.DEFAULT
        );

        Assert.assertEquals(Integer.valueOf(429), run.getHttpStatueCode());
        Assert.assertNull(run.getInterpretId());
        Assert.assertEquals(Integer.valueOf(429), submit.getHttpStatueCode());
        Assert.assertNull(submit.getSubmissionIdValue());
    }

    @Test
    public void testSubmissions_listMapsHistoryAndRequestContract() throws LcException {
        QueueExecutor executor = new QueueExecutor();
        executor.add(new HttpResponse(200,
                "{\"data\":{\"submissionList\":{\"submissions\":[{"
                        + "\"id\":\"100\",\"statusDisplay\":\"Accepted\",\"lang\":\"java\","
                        + "\"runtime\":\"4 ms\",\"timestamp\":\"1720000000\",\"memory\":\"42 MB\""
                        + "}]}}}"));
        LcClient client = client(HttpClient.SiteEnum.EN, executor);

        List<Submission> submissions = client.api().submissions()
                .list("two-sum", 20, 10, RequestContext.DEFAULT);

        JSONObject requestBody = JSONObject.parseObject(executor.lastRequest.getBody());
        Assert.assertEquals("submissions", requestBody.getString("operationName"));
        Assert.assertEquals(20, requestBody.getJSONObject("variables").getIntValue("offset"));
        Assert.assertEquals(10, requestBody.getJSONObject("variables").getIntValue("limit"));
        Assert.assertEquals("two-sum",
                requestBody.getJSONObject("variables").getString("questionSlug"));
        Assert.assertEquals(1, submissions.size());
        Submission submission = submissions.get(0);
        Assert.assertEquals("100", submission.getId());
        Assert.assertEquals("Accepted", submission.getStatus());
        Assert.assertEquals("java", submission.getLang());
        Assert.assertEquals("4 ms", submission.getRuntime());
        Assert.assertEquals("1720000000", submission.getTime());
        Assert.assertEquals("42 MB", submission.getMemory());
    }

    @Test
    public void testSubmissions_enDetailMapsDiagnosticsAndRequestContext() throws LcException {
        QueueExecutor executor = new QueueExecutor();
        executor.add(new HttpResponse(200,
                "{\"data\":{\"submissionDetails\":{"
                        + "\"code\":\"class Solution {}\",\"runtimeDisplay\":\"4 ms\","
                        + "\"memoryDisplay\":\"42 MB\",\"runtimeError\":\"runtime failure\","
                        + "\"lastTestcase\":\"[2,7]\\n9\",\"compileError\":\"compile failure\""
                        + "}}}"));
        LcClient client = client(HttpClient.SiteEnum.EN, executor);

        SubmissionDetail detail = client.api().submissions().detail(
                "100",
                RequestContext.builder().header("X-Submission-Test", "en").build()
        );

        JSONObject requestBody = JSONObject.parseObject(executor.lastRequest.getBody());
        Assert.assertEquals("submissionDetails", requestBody.getString("operationName"));
        Assert.assertEquals("100", requestBody.getJSONObject("variables").getString("id"));
        Assert.assertEquals("en", executor.lastRequest.getHeader().get("X-Submission-Test"));
        Assert.assertEquals("class Solution {}", detail.getSubmissionCode());
        Assert.assertEquals("4 ms", detail.getRuntime());
        Assert.assertEquals("42 MB", detail.getMemory());
        Assert.assertEquals("", detail.getTotalTestcases());
        Assert.assertEquals("", detail.getTotalCorrect());
        Assert.assertEquals("", detail.getInputFormatted());
        Assert.assertEquals("", detail.getExpectedOutput());
        Assert.assertEquals("", detail.getCodeOutput());
        Assert.assertEquals("runtime failure", detail.getRuntimeError());
        Assert.assertEquals("[2,7]\n9", detail.getLastTestcase());
        Assert.assertEquals("compile failure", detail.getCompileError());
    }

    @Test
    public void testSubmissions_cnDetailMapsOutputDiagnostics() throws LcException {
        QueueExecutor executor = new QueueExecutor();
        executor.add(new HttpResponse(200,
                "{\"data\":{\"submissionDetail\":{"
                        + "\"code\":\"class Solution {}\",\"runtime\":\"4 ms\",\"memory\":\"42 MB\","
                        + "\"totalTestCaseCnt\":\"3\",\"passedTestCaseCnt\":\"1\","
                        + "\"outputDetail\":{\"input\":\"[2,7]\\n9\","
                        + "\"expectedOutput\":\"[0,1]\",\"codeOutput\":\"[1,0]\","
                        + "\"runtimeError\":\"runtime failure\",\"lastTestcase\":\"[3,2,4]\\n9\","
                        + "\"compileError\":\"compile failure\"}}}}"));
        LcClient client = client(HttpClient.SiteEnum.CN, executor);

        SubmissionDetail detail = client.api().submissions()
                .detail("cn-100", RequestContext.DEFAULT);

        JSONObject requestBody = JSONObject.parseObject(executor.lastRequest.getBody());
        Assert.assertEquals("submissionDetail", requestBody.getString("operationName"));
        Assert.assertEquals("cn-100", requestBody.getJSONObject("variables").getString("id"));
        Assert.assertEquals("class Solution {}", detail.getSubmissionCode());
        Assert.assertEquals("4 ms", detail.getRuntime());
        Assert.assertEquals("42 MB", detail.getMemory());
        Assert.assertEquals("3", detail.getTotalTestcases());
        Assert.assertEquals("1", detail.getTotalCorrect());
        Assert.assertEquals("[2,7]\n9", detail.getInputFormatted());
        Assert.assertEquals("[0,1]", detail.getExpectedOutput());
        Assert.assertEquals("[1,0]", detail.getCodeOutput());
        Assert.assertEquals("runtime failure", detail.getRuntimeError());
        Assert.assertEquals("[3,2,4]\n9", detail.getLastTestcase());
        Assert.assertEquals("compile failure", detail.getCompileError());
    }

    @Test
    public void testSessions_enListMapsProgressAndAvailableSessions() throws LcException {
        QueueExecutor executor = new QueueExecutor();
        executor.add(new HttpResponse(200,
                "{\"sessionName\":\"Primary\",\"XP\":100,\"solvedTotal\":4,"
                        + "\"questionTotal\":10,\"attempted\":2,\"leetCoins\":30,\"unsolved\":6,"
                        + "\"solvedPerDifficulty\":{\"Easy\":2,\"Medium\":1,\"Hard\":1},"
                        + "\"sessionList\":[{\"id\":7,\"name\":\"Interview\"}]}"));
        LcClient client = client(HttpClient.SiteEnum.EN, executor);

        List<Session> sessions = client.api().sessions().list(
                null,
                RequestContext.builder().header("X-Session-Test", "en").build()
        );

        Assert.assertEquals("http://127.0.0.1:8080/api/progress/all/",
                executor.lastRequest.getUrl());
        Assert.assertEquals("en", executor.lastRequest.getHeader().get("X-Session-Test"));
        Assert.assertEquals(2, sessions.size());
        Session current = sessions.get(0);
        Assert.assertNull(current.getId());
        Assert.assertEquals("Primary", current.getName());
        Assert.assertEquals(Integer.valueOf(100), current.getXP());
        Assert.assertEquals(Integer.valueOf(4), current.getSolvedTotal());
        Assert.assertEquals(Integer.valueOf(10), current.getQuestionTotal());
        Assert.assertEquals(Integer.valueOf(2), current.getAttempted());
        Assert.assertEquals(Integer.valueOf(30), current.getPoint());
        Assert.assertEquals(Integer.valueOf(6), current.getUnsolved());
        Assert.assertEquals(Integer.valueOf(2), current.getEasy());
        Assert.assertEquals(Integer.valueOf(1), current.getMedium());
        Assert.assertEquals(Integer.valueOf(1), current.getHard());
        Assert.assertEquals(Integer.valueOf(7), sessions.get(1).getId());
        Assert.assertEquals("Interview", sessions.get(1).getName());
    }

    @Test
    public void testSessions_cnListUsesProfileProgressAndRequestContext() throws LcException {
        QueueExecutor executor = new QueueExecutor();
        executor.add(new HttpResponse(200,
                "{\"sessionName\":\"Primary\",\"XP\":100,\"solvedTotal\":1,"
                        + "\"questionTotal\":20,\"attempted\":1,\"leetCoins\":30,\"unsolved\":19,"
                        + "\"solvedPerDifficulty\":{\"Easy\":1,\"Medium\":0,\"Hard\":0},"
                        + "\"sessionList\":[]}"));
        executor.add(new HttpResponse(200,
                "{\"data\":{\"userProfileUserQuestionProgress\":{"
                        + "\"numAcceptedQuestions\":["
                        + "{\"difficulty\":\"EASY\",\"count\":3},"
                        + "{\"difficulty\":\"MEDIUM\",\"count\":2},"
                        + "{\"difficulty\":\"HARD\",\"count\":1}],"
                        + "\"numFailedQuestions\":["
                        + "{\"difficulty\":\"EASY\",\"count\":2},"
                        + "{\"difficulty\":\"MEDIUM\",\"count\":1}]}}}"));
        LcClient client = client(HttpClient.SiteEnum.CN, executor);

        List<Session> sessions = client.api().sessions().list(
                "profile-user",
                RequestContext.builder().header("X-Session-Test", "cn").build()
        );

        JSONObject requestBody = JSONObject.parseObject(executor.lastRequest.getBody());
        Assert.assertEquals("userSessionProgress", requestBody.getString("operationName"));
        Assert.assertEquals("profile-user",
                requestBody.getJSONObject("variables").getString("userSlug"));
        Assert.assertEquals("cn", executor.lastRequest.getHeader().get("X-Session-Test"));
        Assert.assertEquals(1, sessions.size());
        Session current = sessions.get(0);
        Assert.assertEquals(Integer.valueOf(3), current.getEasy());
        Assert.assertEquals(Integer.valueOf(2), current.getMedium());
        Assert.assertEquals(Integer.valueOf(1), current.getHard());
        Assert.assertEquals(Integer.valueOf(3), current.getAttempted());
        Assert.assertEquals(Integer.valueOf(6), current.getSolvedTotal());
        Assert.assertEquals(Integer.valueOf(14), current.getUnsolved());
    }

    @Test
    public void testSessions_switchUsesTypedPutContractAndStatus() throws LcException {
        QueueExecutor executor = new QueueExecutor();
        executor.add(new HttpResponse(200, ""));
        executor.add(new HttpResponse(500, ""));
        LcClient client = client(HttpClient.SiteEnum.EN, executor);
        RequestContext context = RequestContext.builder()
                .header("X-Session-Test", "switch")
                .build();

        Assert.assertTrue(client.api().sessions().switchTo(7, context));
        Assert.assertEquals("http://127.0.0.1:8080/session/", executor.lastRequest.getUrl());
        Assert.assertEquals(HttpRequest.Type.PUT, executor.lastRequest.getType());
        Assert.assertEquals("{\"func\":\"activate\",\"target\":7}", executor.lastRequest.getBody());
        Assert.assertEquals("XMLHttpRequest",
                executor.lastRequest.getHeader().get("x-requested-with"));
        Assert.assertEquals("switch", executor.lastRequest.getHeader().get("X-Session-Test"));

        Assert.assertFalse(client.api().sessions().switchTo(8, context));
        Assert.assertEquals("{\"func\":\"activate\",\"target\":8}", executor.lastRequest.getBody());
    }

    @Test
    public void testFavorites_addAndRemoveMapTypedResultsAndRequestContext() throws LcException {
        QueueExecutor executor = new QueueExecutor();
        executor.add(new HttpResponse(200,
                "{\"data\":{\"addQuestionToFavorite\":{\"ok\":true,\"error\":null,"
                        + "\"favoriteIdHash\":\"favorite-list\",\"questionId\":\"101\"}}}"));
        executor.add(new HttpResponse(200,
                "{\"data\":{\"removeQuestionFromFavorite\":{\"ok\":false,\"error\":\"denied\","
                        + "\"favoriteIdHash\":\"favorite-list\",\"questionId\":\"101\"}}}"));
        LcClient client = client(HttpClient.SiteEnum.EN, executor);
        RequestContext context = RequestContext.builder()
                .header("X-Favorite-Test", "favorite")
                .build();

        FavoriteResult added = client.api().favorites()
                .add("favorite-list", "101", context);

        JSONObject addRequest = JSONObject.parseObject(executor.lastRequest.getBody());
        Assert.assertEquals("addQuestionToFavorite", addRequest.getString("operationName"));
        Assert.assertEquals("favorite-list",
                addRequest.getJSONObject("variables").getString("favoriteIdHash"));
        Assert.assertEquals("101",
                addRequest.getJSONObject("variables").getString("questionId"));
        Assert.assertEquals("favorite",
                executor.lastRequest.getHeader().get("X-Favorite-Test"));
        Assert.assertTrue(added.isOk());
        Assert.assertNull(added.getError());

        FavoriteResult removed = client.api().favorites()
                .remove("favorite-list", "101", context);

        JSONObject removeRequest = JSONObject.parseObject(executor.lastRequest.getBody());
        Assert.assertEquals("removeQuestionFromFavorite",
                removeRequest.getString("operationName"));
        Assert.assertEquals("favorite-list",
                removeRequest.getJSONObject("variables").getString("favoriteIdHash"));
        Assert.assertEquals("101",
                removeRequest.getJSONObject("variables").getString("questionId"));
        Assert.assertFalse(removed.isOk());
        Assert.assertEquals("denied", removed.getError());
    }

    @Test
    public void testFind_tagsListsAndCategoriesMapTypedContracts() throws LcException {
        QueueExecutor executor = new QueueExecutor();
        executor.add(new HttpResponse(200,
                "{\"topics\":[{\"slug\":\"array\",\"name\":\"Array\","
                        + "\"translatedName\":\"数组\",\"questions\":[1,2]}]}"));
        executor.add(new HttpResponse(200,
                "[{\"id\":\"favorite-list\",\"name\":\"Favorites\","
                        + "\"type\":\"leetcode_favorites\",\"questions\":[53]}]"));
        executor.add(new HttpResponse(200,
                "{\"categories\":{\"0\":[{\"slug\":\"algorithms\","
                        + "\"title\":\"Algorithms\",\"url\":\"/problemset/algorithms/\"}]}}"));
        LcClient client = client(HttpClient.SiteEnum.CN, executor);
        RequestContext context = RequestContext.builder()
                .header("X-Find-Test", "find")
                .build();

        List<Tag> tags = client.api().questions().tags(context);

        Assert.assertEquals("http://127.0.0.1:8080/problems/api/tags/",
                executor.lastRequest.getUrl());
        Assert.assertEquals("find", executor.lastRequest.getHeader().get("X-Find-Test"));
        Assert.assertEquals(1, tags.size());
        Assert.assertEquals("array", tags.get(0).getSlug());
        Assert.assertEquals("Array", tags.get(0).getName());
        Assert.assertEquals("数组", tags.get(0).getTranslatedName());
        Assert.assertTrue(tags.get(0).getQuestions().contains("1"));
        Assert.assertTrue(tags.get(0).getQuestions().contains("2"));

        List<Tag> lists = client.api().questions().lists(context);

        Assert.assertEquals("http://127.0.0.1:8080/problems/api/favorites/",
                executor.lastRequest.getUrl());
        Assert.assertEquals(1, lists.size());
        Assert.assertEquals("favorite-list", lists.get(0).getSlug());
        Assert.assertEquals("Favorites", lists.get(0).getName());
        Assert.assertEquals("leetcode_favorites", lists.get(0).getType());
        Assert.assertTrue(lists.get(0).getQuestions().contains("53"));

        List<Tag> categories = client.api().questions().categories(context);

        Assert.assertEquals("http://127.0.0.1:8080/problems/api/card-info/",
                executor.lastRequest.getUrl());
        Assert.assertEquals(1, categories.size());
        Assert.assertEquals("algorithms", categories.get(0).getSlug());
        Assert.assertEquals("Algorithms", categories.get(0).getName());
        Assert.assertEquals("/problemset/algorithms/", categories.get(0).getType());
    }

    @Test
    public void testLegacyBuilderRetainsHostSemantics() {
        LcClient client = LcClient.builder(HttpClient.SiteEnum.EN)
                .endpoint("example.test")
                .build();

        Assert.assertEquals("https://example.test", client.getClient().getUrl());
    }

    private static LcClient client(HttpClient.SiteEnum site, QueueExecutor executor) {
        return LcClient.create(LcClientConfig.builder(
                        LcEndpoint.custom(site, "http://127.0.0.1:8080"))
                .executorHttp(executor)
                .build());
    }

    private static final class QueueExecutor implements ExecutorHttp {
        private final Queue<HttpResponse> responses = new ArrayDeque<>();
        private HttpRequest lastRequest;

        void add(HttpResponse response) {
            responses.add(response);
        }

        @Override
        public CookieStore cookieStore() {
            return new TestCookieStore();
        }

        @Override
        public HttpResponse executeGet(HttpRequest httpRequest) {
            return respond(httpRequest);
        }

        @Override
        public HttpResponse executePost(HttpRequest httpRequest) {
            return respond(httpRequest);
        }

        @Override
        public HttpResponse executePut(HttpRequest httpRequest) {
            return respond(httpRequest);
        }

        private HttpResponse respond(HttpRequest request) {
            lastRequest = request;
            HttpResponse response = responses.remove();
            response.setHttpRequest(request);
            return response;
        }
    }
}
