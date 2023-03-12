package com.shuzijun.lc.command;

import com.alibaba.fastjson2.JSONObject;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.Graphql;
import com.shuzijun.lc.http.HttpClient;
import com.shuzijun.lc.http.HttpRequest;
import com.shuzijun.lc.http.HttpResponse;
import com.shuzijun.lc.model.User;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class CommonCommand {

    public static Verify buildVerify() {
        return new Verify();
    }

    public static CheckLogin buildCheckLogin() {
        return new CheckLogin();
    }

    public static GetUser buildGetUser() {
        return new GetUser();
    }


    /**
     * 验证客户端与网站链接
     */
    public static class Verify implements Command<Boolean> {
        @Override
        public Boolean execute(HttpClient client) throws LcException {
            Map<String,String> header = client.getHeader();
            return HttpRequest.builderGet(client.getVerify())
                    .addHeader(header)
                    .request(client.getExecutorHttp())
                    .isCodeSuccess();
        }
    }

    /**
     * 检查登录状态
     */
    public static class CheckLogin implements  Command<Boolean>{
        @Override
        public Boolean execute(HttpClient client) throws LcException {
            Map<String,String> header = client.getHeader();
            return HttpRequest.builderGet(client.getPoints()).addHeader(header).request(client.getExecutorHttp()).isCodeSuccess();
        }
    }

    public static class GetUser implements Command<User> {
        @Override
        public User execute(HttpClient client) throws LcException {
            Map<String,String> header = client.getHeader();
            HttpResponse response;
            if (client.isCn()) {
                response = Graphql.builder(client.getGraphql() + "/noj-go").cn(client.isCn()).header(header)
                        .operationName("userStatus", "userStatusGlobal")
                        .request(client.getExecutorHttp());
            } else {
                response = Graphql.builder(client.getGraphql()).cn(client.isCn()).header(header)
                        .operationName("userStatus", "globalData")
                        .request(client.getExecutorHttp());
            }

            if (response.isCodeSuccess()) {
                JSONObject userObject = JSONObject.parseObject(response.getBody()).getJSONObject("data").getJSONObject("userStatus");
                User user = new User();
                user.setPremium(userObject.getBoolean("isPremium"));
                user.setUsername(userObject.getString("username"));
                if (userObject.containsKey("userSlug")) {
                    user.setUserSlug(userObject.getString("userSlug"));
                } else {
                    user.setUserSlug(user.getUsername());
                }
                if (userObject.containsKey("realName") && StringUtils.isNotBlank(userObject.getString("realName"))) {
                    user.setRealName(userObject.getString("realName"));
                } else {
                    user.setRealName(user.getUsername());
                }
                user.setSignedIn(userObject.getBoolean("isSignedIn"));
                user.setVerified(userObject.getBoolean("isVerified"));
                user.setPhoneVerified(userObject.getBoolean("isPhoneVerified"));
                return user;
            } else {
                return new User();
            }

        }
    }
}
