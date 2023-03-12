package com.shuzijun.lc;

import com.alibaba.fastjson2.JSON;
import com.shuzijun.lc.command.CommonCommand;
import com.shuzijun.lc.command.CookieCommand;
import com.shuzijun.lc.command.QuestionCommand;
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
        lcClient =LcClient.builder(HttpClient.SiteEnum.EN).build();
        boolean t = lcClient.invoker(CommonCommand.buildVerify());
        Assert.assertTrue(t);
        //cookie = System.getenv("LC_COOKIE");
        lcClient.invoker(CookieCommand.buildSetCookie(cookie));

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
        PageInfo<QuestionView> pageInfo = lcClient.invoker(QuestionCommand.buildProblemSetQuestionList(new ProblemSetParam(4,50)));
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
        QuestionView questionView = lcClient.invoker(QuestionCommand.buildGetQuestion("two-sum"));
        Assert.assertNotNull(questionView);
        System.out.println(JSON.toJSONString(questionView));
    }

    @Test
    public void testQuestionOfToday() throws LcException {
        Question question = lcClient.invoker(QuestionCommand.buildQuestionOfToday());
        Assert.assertNotNull(question);
        System.out.println(JSON.toJSONString(question));
    }

}
