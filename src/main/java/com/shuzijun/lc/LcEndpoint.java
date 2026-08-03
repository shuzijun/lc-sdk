package com.shuzijun.lc;

import com.shuzijun.lc.http.HttpClient;

import java.net.URI;
import java.net.URISyntaxException;

public final class LcEndpoint {

    public static final LcEndpoint LEETCODE = new LcEndpoint(HttpClient.SiteEnum.EN, "https://leetcode.com");
    public static final LcEndpoint LEETCODE_CN = new LcEndpoint(HttpClient.SiteEnum.CN, "https://leetcode.cn");

    private final HttpClient.SiteEnum site;
    private final String baseUrl;

    private LcEndpoint(HttpClient.SiteEnum site, String baseUrl) {
        if (site == null) {
            throw new IllegalArgumentException("Site must not be null");
        }
        this.site = site;
        this.baseUrl = normalize(baseUrl);
    }

    public static LcEndpoint custom(HttpClient.SiteEnum protocol, String baseUrl) {
        return new LcEndpoint(protocol, baseUrl);
    }

    public HttpClient.SiteEnum getSite() {
        return site;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    private static String normalize(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Base URL must not be null");
        }
        String normalized = value.trim();
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        try {
            URI uri = new URI(normalized);
            if (!(uri.getScheme() != null
                    && ("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme()))
                    && uri.getHost() != null)) {
                throw new IllegalArgumentException("Base URL must be an absolute HTTP(S) URL: " + value);
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid base URL: " + value, e);
        }
        return normalized;
    }
}
