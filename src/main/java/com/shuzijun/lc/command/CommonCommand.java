package com.shuzijun.lc.command;

import com.alibaba.fastjson2.JSONObject;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.Graphql;
import com.shuzijun.lc.http.HttpClient;
import com.shuzijun.lc.http.HttpRequest;
import com.shuzijun.lc.http.HttpResponse;
import com.shuzijun.lc.model.Checkin;
import com.shuzijun.lc.model.User;
import org.apache.commons.lang3.StringUtils;

public class CommonCommand {


    /**
     * 构建验证客户端与网站链接
     *
     * @return {@link Boolean} true 验证成功
     */
    public static Verify buildVerify() {
        return new Verify();
    }

    /**
     * 构建检查登录状态
     *
     * @return {@link Boolean} true 已登录
     */
    public static CheckLogin buildCheckLogin() {
        return new CheckLogin();
    }

    /**
     * 构建获取用户信息, 需要提前设置cookie
     *
     * @return {@link User} 用户信息
     */
    public static GetUser buildGetUser() {
        return new GetUser();
    }

    /**
     * 检查登录状态
     *
     * @return {@link Checkin} 登录信息
     */
    public static GetCheckin buildCheckin() {
        return new GetCheckin();
    }


    public static class Verify implements Command<Boolean> {
        @Override
        public Boolean execute(HttpClient client) throws LcException {
            return HttpRequest.builderGet(client.getVerify())
                    .addHeader(client.getHeader())
                    .request(client.getExecutorHttp())
                    .isCodeSuccess();
        }
    }

    public static class CheckLogin implements Command<Boolean> {
        @Override
        public Boolean execute(HttpClient client) throws LcException {
            return HttpRequest.builderGet(client.getPoints()).addHeader(client.getHeader()).request(client.getExecutorHttp()).isCodeSuccess();
        }
    }

    public static class GetUser implements Command<User> {
        @Override
        public User execute(HttpClient client) throws LcException {
            HttpResponse response;
            if (client.isCn()) {
                response = Graphql.builder(client.getGraphql() + "/noj-go").cn(client.isCn()).header(client.getHeader())
                        .operationName("userStatus", "userStatusGlobal")
                        .request(client.getExecutorHttp());
            } else {
                response = Graphql.builder(client.getGraphql()).cn(client.isCn()).header(client.getHeader())
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

    public static class  GetCheckin implements Command<Checkin> {

        @Override
        public Checkin execute(HttpClient client) throws LcException {
            HttpResponse response = Graphql.builder(client.getGraphql()).operationName("checkin").header(client.getHeader()).request(client.getExecutorHttp());
            if (response.isCodeSuccess() && StringUtils.isNotBlank(response.getBody())) {
               return  JSONObject.parseObject(response.getBody()).getJSONObject("data").getJSONObject("checkin").to(Checkin.class);
            } else {
                throw new LcException("checkin fail", HttpClient.buildHttpTrace(response.getHttpRequest(), response));
            }
        }
    }
}
