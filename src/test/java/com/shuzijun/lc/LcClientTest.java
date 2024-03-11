package com.shuzijun.lc;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shuzijun.lc.command.*;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.HttpClient;
import com.shuzijun.lc.model.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class LcClientTest {

    private static LcClient lcClient;

    private static String cookie = "";

    @BeforeClass
    public static void before() throws LcException {
        lcClient = LcClient.builder(HttpClient.SiteEnum.CN).build();
        // 从环境变量中获取cookie
        cookie = System.getenv("LC_COOKIE");
        lcClient.invoker(CookieCommand.buildSetCookie(cookie));
        boolean t = lcClient.invoker(CommonCommand.buildVerify());
        Assert.assertTrue(t);

    }

    @Test
    public void testVerify() throws LcException {
    }

    @Test
    public void testCheckLogin() throws LcException {
        boolean t = lcClient.invoker(CommonCommand.buildCheckLogin());
        Assert.assertEquals(t, StringUtils.isNotBlank(cookie));
    }

    @Test
    public void testGetUser() throws LcException {
        User u = lcClient.invoker(CommonCommand.buildGetUser());
        Assert.assertNotNull(u);
        System.out.println(JSON.toJSONString(u));
    }

    @Test
    public void testProblemSetQuestionList() throws LcException {
        PageInfo<QuestionView> pageInfo = lcClient.invoker(QuestionCommand.buildProblemSetQuestionList(new ProblemSetParam(4, 50)));
        Assert.assertNotNull(pageInfo);
        System.out.println(JSON.toJSONString(pageInfo));
    }

    @Test
    public void testGetAllQuestion() throws LcException {
        List<QuestionView> questionViews = lcClient.invoker(QuestionCommand.buildAllQuestions());
        Assert.assertNotNull(questionViews);
        System.out.println(JSON.toJSONString(questionViews.get(0)));
    }

    @Test
    public void testGetQuestion() throws LcException {
        QuestionView questionView = lcClient.invoker(QuestionCommand.buildGetQuestion("two-sum",new OptionTest("two-sum")));
        Assert.assertNotNull(questionView);
        System.out.println(JSON.toJSONString(questionView));
    }

    @Test
    public void testQuestionOfToday() throws LcException {
        Question question = lcClient.invoker(QuestionCommand.buildQuestionOfToday());
        Assert.assertNotNull(question);
        System.out.println(JSON.toJSONString(question));
    }

    @Test
    public void testSolution() throws LcException {
        List<Solution> solutions = lcClient.invoker(SolutionCommand.buildSolutionList(1, 0, "two-sum"));
        if (lcClient.getClient().isCn()) {
            Assert.assertNotNull(solutions);
            System.out.println(JSON.toJSONString(solutions));
            String content = lcClient.invoker(SolutionCommand.buildSolutionArticle(solutions.get(0).getSlug()));
            Assert.assertNotNull(content);
            System.out.println(content);
        } else {
            String content = lcClient.invoker(SolutionCommand.buildSolutionArticle("two-sum"));
            Assert.assertNotNull(content);
            System.out.println(content);
        }
    }

    @Test
    public void testSubmitCode() throws LcException, InterruptedException {
        if (StringUtils.isBlank(cookie)) {
            System.out.println("cookie is null,skip testRunCode");
            return;
        }
        String code = "class Solution {\n" +
                "    public int[] twoSum(int[] nums, int target) {\n" +
                "        int n = nums.length;\n" +
                "        for (int i = 0; i < n; ++i) {\n" +
                "            for (int j = i + 1; j < n; ++j) {\n" +
                "                if (nums[i] + nums[j] == target) {\n" +
                "                    return new int[]{i, j};\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        return new int[0];\n" +
                "    }\n" +
                "}\n";
        SubmitResult result = lcClient.invoker(CodeCommand.buildSubmitCode(new SubmitParam(code, "java", "two-sum", "1")));
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getSubmissionId());
        System.out.println(result);

        for (int i = 0; i < 100; i++) {
            SubmitCheckResult submitCheckResult = lcClient.invoker(CodeCommand.buildSubmitCheck(result.getSubmissionId()));
            Assert.assertNotNull(submitCheckResult);
            if (!"PENDING".equalsIgnoreCase(submitCheckResult.getState()) && !"STARTED".equalsIgnoreCase(submitCheckResult.getState())) {
                System.out.println(JSON.toJSONString(submitCheckResult));
                break;
            }
            Thread.sleep(1000);
        }

    }

    @Test
    public void testRunCode() throws LcException, InterruptedException {
        if (StringUtils.isBlank(cookie)) {
            System.out.println("cookie is null,skip testRunCode");
            return;
        }
        String code = "class Solution {\n" +
                "    public int[] twoSum(int[] nums, int target) {\n" +
                "        int n = nums.length;\n" +
                "        for (int i = 0; i < n; ++i) {\n" +
                "            for (int j = i + 1; j < n; ++j) {\n" +
                "                if (nums[i] + nums[j] == target) {\n" +
                "                    return new int[]{i, j};\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        return new int[0];\n" +
                "    }\n" +
                "}\n";
        RunCodeResult result = lcClient.invoker(CodeCommand.buildRunCode(new RunCodeParam("1", "two-sum", "[2,7,11,15]\n9", "java", code)));
        Assert.assertNotNull(result);
        System.out.println(JSONObject.toJSONString(result));

        for (int i = 0; i < 100; i++) {
            RunCodeCheckResult runCodeCheckResult = lcClient.invoker(CodeCommand.buildRunCodeCheck(result.getInterpretId()));
            Assert.assertNotNull(runCodeCheckResult);
            if (!"PENDING".equalsIgnoreCase(runCodeCheckResult.getState()) && !"STARTED".equalsIgnoreCase(runCodeCheckResult.getState())) {
                System.out.println(JSON.toJSONString(runCodeCheckResult));
                break;
            }
            Thread.sleep(1000);
        }
    }

    @Test
    public void testTags() throws LcException {
        List<Tag> tags = lcClient.invoker(FindCommand.buildTags());
        Assert.assertNotNull(tags);
        System.out.println(JSONObject.toJSONString(tags));
    }

    @Test
    public void testLists() throws LcException {
        List<Tag> lists = lcClient.invoker(FindCommand.buildLists());
        Assert.assertNotNull(lists);
        System.out.println(JSONObject.toJSONString(lists));

        Tag favoriteTag = lists.stream().filter(tag -> !tag.getType().equals("leetcode_favorites")).findAny().orElse(null);
        if (favoriteTag != null) {
            Boolean add = lcClient.invoker(FavoriteCommand.buildAddQuestionToFavorite(favoriteTag.getSlug(), "3")).isOk();
            Assert.assertTrue(add);
            System.out.println("add success");
            Boolean rm = lcClient.invoker(FavoriteCommand.buildRemoveQuestionFromFavorite(favoriteTag.getSlug(), "3")).isOk();
            Assert.assertTrue(rm);
            System.out.println("rm success");
        }

    }

    @Test
    public void testCategory() throws LcException {
        List<Tag> categoryList = lcClient.invoker(FindCommand.buildCategory());
        Assert.assertNotNull(categoryList);
        System.out.println(JSONObject.toJSONString(categoryList));
    }

    @Test
    public void testSession() throws LcException {
        if (StringUtils.isBlank(cookie)) {
            System.out.println("cookie is null,skip testRunCode");
            return;
        }
        User u = lcClient.invoker(CommonCommand.buildGetUser());
        Assert.assertNotNull(u);
        List<Session> sessions = lcClient.invoker(SessionCommand.buildSessionListCommand(u.getUserSlug()));
        Assert.assertNotNull(sessions);
        System.out.println(JSONObject.toJSONString(sessions));

        if (sessions.size() > 1) {
            Boolean b = lcClient.invoker(SessionCommand.buildSwitchSession(sessions.get(1).getId()));
            Assert.assertTrue(b);
        }
    }

    @Test
    public void testSubmission() throws LcException {
        if (StringUtils.isBlank(cookie)) {
            System.out.println("cookie is null,skip testRunCode");
            return;
        }
        List<Submission> submissions = lcClient.invoker(SubmissionCommand.buildSubmissionList("two-sum", 0, 10));
        Assert.assertNotNull(submissions);
        System.out.println(JSONObject.toJSONString(submissions));
        SubmissionDetail submissionDetail = lcClient.invoker(SubmissionCommand.buildSubmissionDetail(submissions.get(0).getId()));
        Assert.assertNotNull(submissionDetail);
        System.out.println(JSONObject.toJSONString(submissionDetail));
    }

}
