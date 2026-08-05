package com.shuzijun.lc.command;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.shuzijun.lc.errors.LcException;
import com.shuzijun.lc.http.HttpClient;
import com.shuzijun.lc.http.HttpRequest;
import com.shuzijun.lc.http.HttpResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
                return new LoginResult(
                        false,
                        0,
                        "HTTP login is only supported for leetcode.cn",
                        Collections.<String>emptyList()
                );
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
            boolean successStatus = response.getStatusCode() == 200
                    || response.getStatusCode() == 302;
            String responseBody = response.getBody();
            List<String> errors = new ArrayList<>();
            boolean success = successStatus && StringUtils.isBlank(responseBody);
            if (successStatus && StringUtils.isNotBlank(responseBody)
                    && responseBody.trim().startsWith("{")) {
                JSONObject form = JSONObject.parseObject(responseBody).getJSONObject("form");
                JSONArray errorValues = form == null ? null : form.getJSONArray("errors");
                if (errorValues != null) {
                    for (int i = 0; i < errorValues.size(); i++) {
                        errors.add(errorValues.getString(i));
                    }
                }
                success = errors.isEmpty();
            }
            return new LoginResult(success, response.getStatusCode(), responseBody, errors);
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
        private final List<String> errors;

        private LoginResult(
                boolean success,
                int statusCode,
                String responseBody,
                List<String> errors
        ) {
            this.success = success;
            this.statusCode = statusCode;
            this.responseBody = responseBody;
            this.errors = Collections.unmodifiableList(new ArrayList<>(errors));
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

        public List<String> getErrors() {
            return errors;
        }
    }
}
