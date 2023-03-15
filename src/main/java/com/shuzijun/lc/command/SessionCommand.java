package com.shuzijun.lc.command;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.Graphql;
import com.shuzijun.lc.http.HttpClient;
import com.shuzijun.lc.http.HttpClient.SiteEnum;
import com.shuzijun.lc.http.HttpRequest;
import com.shuzijun.lc.http.HttpResponse;
import com.shuzijun.lc.model.Session;
import com.shuzijun.lc.model.User;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class SessionCommand {

    /**
     * 获取session列表
     *
     * @param userSlug {@link SiteEnum#CN}需要此参数，来自{@link User#getUserSlug()}
     * @return {@link List<Session>} session列表
     */
    public static SessionListCommand buildSessionListCommand(String userSlug) {
        return new SessionListCommand(userSlug);
    }

    /**
     * 切换session
     *
     * @param sessionId 来自{@link Session#getId()}
     * @return {@link Boolean} 是否切换成功
     */
    public static SwitchSession buildSwitchSession(Integer sessionId) {
        return new SwitchSession(sessionId);
    }

    public static class SessionListCommand implements Command<List<Session>> {

        private final String userSlug;

        public SessionListCommand(String userSlug) {
            this.userSlug = userSlug;
        }


        @Override
        public List<Session> execute(HttpClient client) throws LcException {
            HttpResponse httpResponse = HttpRequest.builderGet(client.getProgress()).addHeader(client.getHeader()).request(client.getExecutorHttp());
            if (httpResponse.isCodeSuccess() && StringUtils.isNotBlank(httpResponse.getBody())) {

                JSONObject jsonObject = JSON.parseObject(httpResponse.getBody());

                Session defSession = jsonObject.toJavaObject(Session.class);
                defSession.setEasy(jsonObject.getJSONObject("solvedPerDifficulty").getInteger("Easy"));
                defSession.setMedium(jsonObject.getJSONObject("solvedPerDifficulty").getInteger("Medium"));
                defSession.setHard(jsonObject.getJSONObject("solvedPerDifficulty").getInteger("Hard"));

                if (client.isCn()) {
                    HttpResponse sessionHttpResponse = Graphql.builder(client.getGraphql()).cn(client.isCn())
                            .header(client.getHeader())
                            .operationName("userSessionProgress")
                            .variables("userSlug", userSlug)
                            .request(client.getExecutorHttp());
                    if (sessionHttpResponse.isCodeSuccess() && StringUtils.isNotBlank(sessionHttpResponse.getBody())) {
                        JSONObject questionProgressObject = JSON.parseObject(sessionHttpResponse.getBody()).getJSONObject("data").getJSONObject("userProfileUserQuestionProgress");
                        JSONArray numAcceptedQuestions = questionProgressObject.getJSONArray("numAcceptedQuestions");
                        for (int i = 0; i < numAcceptedQuestions.size(); i++) {
                            JSONObject acceptedQuestions = numAcceptedQuestions.getJSONObject(i);
                            if ("EASY".equals(acceptedQuestions.getString("difficulty"))) {
                                defSession.setEasy(acceptedQuestions.getInteger("count"));
                            } else if ("MEDIUM".equals(acceptedQuestions.getString("difficulty"))) {
                                defSession.setMedium(acceptedQuestions.getInteger("count"));
                            } else if ("HARD".equals(acceptedQuestions.getString("difficulty"))) {
                                defSession.setHard(acceptedQuestions.getInteger("count"));
                            }
                        }
                        int attempted = 0;
                        JSONArray numFailedQuestions = questionProgressObject.getJSONArray("numFailedQuestions");
                        for (int i = 0; i < numFailedQuestions.size(); i++) {
                            attempted = attempted + numFailedQuestions.getJSONObject(i).getInteger("count");
                        }
                        defSession.setAttempted(attempted);
                        defSession.setSolvedTotal(defSession.getEasy() + defSession.getMedium() + defSession.getHard());
                        defSession.setUnsolved(defSession.getQuestionTotal() - defSession.getSolvedTotal());

                    }
                }

                List<Session> sessionList = jsonObject.getJSONArray("sessionList").toJavaList(Session.class);
                sessionList.add(0, defSession);

                return sessionList;
            } else {
                throw new LcException("SessionList fail", HttpClient.buildHttpTrace(httpResponse.getHttpRequest(), httpResponse));
            }
        }
    }

    public static class SwitchSession implements Command<Boolean> {

        private final Integer sessionId;

        public SwitchSession(Integer sessionId) {
            this.sessionId = sessionId;
        }

        @Override
        public Boolean execute(HttpClient client) throws LcException {
            HttpResponse httpResponse = HttpRequest.builderPut(client.getSession(), "application/json")
                    .addHeader(client.getHeader())
                    .addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .body("{\"func\":\"activate\",\"target\":" + sessionId + "}")
                    .request(client.getExecutorHttp());

            return httpResponse.isCodeSuccess();
        }
    }
}
