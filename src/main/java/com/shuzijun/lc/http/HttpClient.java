package com.shuzijun.lc.http;


import java.net.URI;
import java.net.URISyntaxException;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HttpClient {

    private static final String prefix = "https://";
    private static final String login = "/accounts/login/";
    private static final String logout = "/accounts/logout/";
    private static final String all = "/api/problems/all/";
    private static final String graphql = "/graphql";
    private static final String points = "/points/api/";
    private static final String problems = "/problems/";
    private static final String submissions = "/submissions/detail/";
    private static final String tags = "/problems/api/tags/";
    private static final String favorites = "/problems/api/favorites/";
    private static final String verify = "/problemset/";
    private static final String progress = "/api/progress/all/";
    private static final String session = "/session/";
    private static final String cardInfo = "/problems/api/card-info/";
    private static final String randomOneQuestion = "/problems/random-one-question/all";
    /**
     * 网站地址
     */
    private final String endpoint;
    private final String baseUrl;
    /**
     * 站点类型
     */
    private final int site;
    private final Map<String, String> defaultHeaders;
    /**
     * 请求执行器
     */
    private final ExecutorHttp executorHttp;

    private HttpClient(
            String endpoint,
            String baseUrl,
            int site,
            ExecutorHttp executorHttp,
            Map<String, String> defaultHeaders) {
        this.endpoint = endpoint;
        this.baseUrl = baseUrl;
        this.site = site;
        this.executorHttp = executorHttp;
        this.defaultHeaders = new HashMap<>(defaultHeaders);
    }

    public static String buildHttpTrace(HttpRequest httpRequest, HttpResponse httpResponse) {
        StringBuilder traceBuilder = new StringBuilder();
        traceBuilder.append("---------START-HTTP---------\n");
        traceBuilder.append("-----------REQUEST----------\n");
        if (httpRequest == null) {
            traceBuilder.append("Request is null\n");
        } else {
            traceBuilder.append(httpRequest.getType()).append(" ").append(httpRequest.getUrl()).append(" HTTP/1.1\n");
            if (httpRequest.getContentType() != null) {
                traceBuilder.append("content-type").append(": ").append(httpRequest.getContentType()).append("\n");
            }
            httpRequest.getHeader().forEach((k, v) -> {
                        if (k.equalsIgnoreCase("cookie")) {
                            return;
                        }
                        traceBuilder.append(k).append(": ").append(v).append("\n");
                    }
            );
            if (httpRequest.getBody() != null) {
                traceBuilder.append(httpRequest.getBody()).append("\n");
            }
        }

        if (httpResponse != null) {
            traceBuilder.append("-----------RESPONSE---------\n");
            traceBuilder.append("statusCode").append(": ").append(httpResponse.getStatusCode()).append("\n");
            traceBuilder.append("body").append(": ").append(httpResponse.getBody()).append("\n");
        }
        traceBuilder.append("---------END-HTTP---------\n");
        return traceBuilder.toString();
    }

    public static Builder builder(SiteEnum siteEnum) {
        return new Builder(siteEnum);
    }

    public String getUrl() {
        return baseUrl == null ? prefix + getEndpoint() : baseUrl;
    }

    public String getLogout() {
        return getUrl() + logout;
    }

    public String getLogin() {
        return getUrl() + login;
    }

    public String getAll() {
        return getUrl() + all;
    }

    public String getGraphql() {
        return getUrl() + graphql;
    }

    public String getPoints() {
        return getUrl() + points;
    }

    public String getProblems() {
        return getUrl() + problems;
    }

    public String getSubmissions() {
        return getUrl() + submissions;
    }

    public String getTags() {
        return getUrl() + tags;
    }

    public String getFavorites() {
        return getUrl() + favorites;
    }

    public String getVerify() {
        return getUrl() + verify;
    }

    public String getProgress() {
        return getUrl() + progress;
    }

    public String getSession() {
        return getUrl() + session;
    }

    public String getCardInfo() {
        return getUrl() + cardInfo;
    }

    public String getRandomOneQuestion() {
        return getUrl() + randomOneQuestion;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public boolean isCn() {
        return site == SiteEnum.CN.code;
    }

    public ExecutorHttp getExecutorHttp() {
        return executorHttp;
    }

    public CookieStore cookieStore() {
        return getExecutorHttp().cookieStore();
    }

    public Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.putIfAbsent("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36");
        header.putIfAbsent("Accept", "*/*");
        header.putIfAbsent("Accept-Language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        header.putIfAbsent("origin", getUrl());
        header.putAll(defaultHeaders);

        HttpCookie csrfTokenCookie = cookieStore().getCookie(getEndpoint(), "csrftoken");
        if (csrfTokenCookie != null) {
            header.putIfAbsent("x-csrftoken", csrfTokenCookie.getValue());
        }
        return header;
    }

    public enum SiteEnum {
        EN(0, "leetcode", "leetcode.com"),
        CN(1, "leetcode-cn", "leetcode.cn");


        public final String defaultEndpoint;
        private final int code;
        private final String name;

        SiteEnum(int code, String name, String defaultEndpoint) {
            this.code = code;
            this.name = name;
            this.defaultEndpoint = defaultEndpoint;
        }
    }

    public static final class Builder {
        /**
         * 网站地址
         */
        private String endpoint;
        private String baseUrl;

        /**
         * 中国站
         */
        private int site;
        /**
         * 请求执行器
         */
        private ExecutorHttp executorHttp;
        private final Map<String, String> defaultHeaders = new HashMap<>();

        public Builder(SiteEnum siteEnum) {
            this.site = siteEnum.code;
            this.endpoint = siteEnum.defaultEndpoint;
        }

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            this.baseUrl = null;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = normalizeBaseUrl(baseUrl);
            try {
                this.endpoint = new URI(this.baseUrl).getHost();
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Invalid base URL: " + baseUrl, e);
            }
            return this;
        }

        public Builder executorHttp(ExecutorHttp executorHttp) {
            this.executorHttp = executorHttp;
            return this;
        }

        public Builder addHeader(String name, String value) {
            if (name == null || value == null) {
                throw new IllegalArgumentException("Header name and value must not be null");
            }
            defaultHeaders.put(name, value);
            return this;
        }

        public Builder addHeader(Map<String, String> headers) {
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    addHeader(entry.getKey(), entry.getValue());
                }
            }
            return this;
        }

        public HttpClient build() {
            if (executorHttp == null) {
                executorHttp = new DefaultExecutoHttp();
            }
            return new HttpClient(endpoint, baseUrl, site, executorHttp, defaultHeaders);
        }

        private static String normalizeBaseUrl(String baseUrl) {
            if (baseUrl == null) {
                throw new IllegalArgumentException("Base URL must not be null");
            }
            String normalized = baseUrl.trim();
            while (normalized.endsWith("/")) {
                normalized = normalized.substring(0, normalized.length() - 1);
            }
            try {
                URI uri = new URI(normalized);
                if (!("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme()))
                        || uri.getHost() == null) {
                    throw new IllegalArgumentException("Base URL must be an absolute HTTP(S) URL: " + baseUrl);
                }
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Invalid base URL: " + baseUrl, e);
            }
            return normalized;
        }
    }

}
