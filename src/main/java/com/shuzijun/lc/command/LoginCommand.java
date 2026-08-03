package com.shuzijun.lc.command;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.HttpClient;
import com.shuzijun.lc.http.HttpRequest;
import com.shuzijun.lc.http.HttpResponse;
import org.apache.commons.lang3.StringUtils;

public final class LoginCommand {

    private static final String BOUNDARY = "----lc-sdk-login-boundary";

    private LoginCommand() {
    }

    public static Login buildLogin(String username, String password, String csrfToken, Option<?>... options) {
        return new Login(username, password, csrfToken, options);
    }

    public static final class Login extends OptionCommand implements Command<LoginResult> {
        private final String username;
        private final String password;
        private final String csrfToken;

        private Login(String username, String password, String csrfToken, Option<?>... options) {
            super(options);
            if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
                throw new IllegalArgumentException("Username and password must not be blank");
            }
            this.username = username;
            this.password = password;
            this.csrfToken = csrfToken == null ? "" : csrfToken;
        }

        @Override
        public LoginResult execute(HttpClient client) throws LcException {
            if (!client.isCn()) {
                return new LoginResult(false, 0, "HTTP login is only supported for leetcode.cn");
            }
            String body = part("csrfmiddlewaretoken", csrfToken)
                    + part("login", username)
                    + part("password", password)
                    + part("next", "/problems")
                    + "--" + BOUNDARY + "--\r\n";
            HttpResponse response = HttpRequest.builderPost(
                            client.getLogin(), "multipart/form-data; boundary=" + BOUNDARY)
                    .addHeader(client.getHeader())
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addOption(getOptions())
                    .body(body)
                    .request(client.getExecutorHttp());
            boolean success = response.getStatusCode() == 200 || response.getStatusCode() == 302;
            if (success && StringUtils.isNotBlank(response.getBody())
                    && response.getBody().trim().startsWith("{")) {
                JSONObject form = JSONObject.parseObject(response.getBody()).getJSONObject("form");
                JSONArray errors = form == null ? null : form.getJSONArray("errors");
                success = errors == null || errors.isEmpty();
            }
            return new LoginResult(success, response.getStatusCode(), response.getBody());
        }

        private static String part(String name, String value) {
            return "--" + BOUNDARY + "\r\n"
                    + "Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n"
                    + value + "\r\n";
        }
    }

    public static final class LoginResult {
        private final boolean success;
        private final int statusCode;
        private final String responseBody;

        private LoginResult(boolean success, int statusCode, String responseBody) {
            this.success = success;
            this.statusCode = statusCode;
            this.responseBody = responseBody;
        }

        public boolean isSuccess() {
            return success;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getResponseBody() {
            return responseBody;
        }
    }
}
