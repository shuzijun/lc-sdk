package com.shuzijun.lc.http;

import com.shuzijun.lc.errors.LcException;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.HttpCookie;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultExecutoHttp implements ExecutorHttp {

    private final DefaultCookieStore cookieStore = new DefaultCookieStore();

    private final OkHttpClient okHttpClient;

    public DefaultExecutoHttp() {
        this.okHttpClient = newDefaultHttpClient(600, 1800, 1800);
    }

    public OkHttpClient newDefaultHttpClient(
            long connectTimeout, long writeTimeout, long readTimeout) {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient httpClient = new OkHttpClient()
                    .newBuilder()
                    .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                    .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                    .readTimeout(readTimeout, TimeUnit.SECONDS)
                    .cookieJar(new CookieJar() {
                        @Override
                        public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
                            cookieStore.addMyCookie(httpUrl.host(), list);
                        }

                        @NotNull
                        @Override
                        public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                            return cookieStore.getMyCookie(httpUrl.host());
                        }
                    })
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .build();
            return httpClient;
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public CookieStore cookieStore() {
        return cookieStore;
    }

    @Override
    public HttpResponse executeGet(HttpRequest httpRequest) throws LcException {
        return new InterceptorChain(httpRequest.getInterceptors(), request -> {
            try {
                Response response = getRequestClient().newCall(buildRequest(request)).execute();
                if (response.body() == null) {
                    return new HttpResponse(response.code(), "", request);
                } else {
                    return new HttpResponse(response.code(), response.body().string(), request);
                }
            } catch (IOException e) {
                throw new LcException("executeGet error", e, HttpClient.buildHttpTrace(request, null));
            }
        }).proceed(httpRequest);
    }

    @Override
    public HttpResponse executePost(HttpRequest httpRequest) throws LcException {
        return new InterceptorChain(httpRequest.getInterceptors(), request ->{
            try {
                Response response = getRequestClient().newCall(buildRequest(request)).execute();
                if (response.body() == null) {
                    return new HttpResponse(response.code(), "", request);
                } else {
                    return new HttpResponse(response.code(), response.body().string(), request);
                }
            } catch (IOException e) {
                throw new LcException("executePost error", e, HttpClient.buildHttpTrace(request, null));
            }
        }).proceed(httpRequest);
    }

    @Override
    public HttpResponse executePut(HttpRequest httpRequest) throws LcException {
        return new InterceptorChain(httpRequest.getInterceptors(), request ->{
            try {
                Response response = getRequestClient().newCall(buildRequest(request)).execute();
                if (response.body() == null) {
                    return new HttpResponse(response.code(), "", request);
                } else {
                    return new HttpResponse(response.code(), response.body().string(), request);
                }
            } catch (IOException e) {
                throw new LcException("executePut error", e, HttpClient.buildHttpTrace(request, null));
            }
        }).proceed(httpRequest);

    }

    private Request buildRequest(HttpRequest httpRequest) {
        RequestBody body = null;
        if (StringUtils.isNotBlank(httpRequest.getContentType()) && StringUtils.isNotBlank(httpRequest.getBody())) {
            body = RequestBody.create(httpRequest.getBody(), MediaType.parse(httpRequest.getContentType()));
        }
        return new Request.Builder().url(httpRequest.getUrl()).headers(Headers.of(httpRequest.getHeader())).method(httpRequest.getType().toString(), body).build();
    }

    public OkHttpClient getRequestClient(){
        return okHttpClient;
    }

    private static class DefaultCookieStore implements CookieStore {

        private final ReentrantLock lock = new ReentrantLock(false);
        ;
        private final ConcurrentHashMap<String, ConcurrentHashMap<String, Cookie>> cookieStore = new ConcurrentHashMap<>();

        public void addMyCookie(String domain, List<Cookie> cookieList) {
            if (cookieList == null || cookieList.isEmpty()) {
                return;
            }

            ConcurrentHashMap<String, Cookie> addCookie = cookieList.stream().collect(Collectors.toMap(Cookie::name, Function.identity(), (oldValue, newValue) -> newValue, ConcurrentHashMap::new));

            lock.lock();
            try {
                if (cookieStore.containsKey(domain)) {
                    cookieStore.get(domain).putAll(addCookie);
                } else {
                    cookieStore.put(domain, addCookie);
                }
            } finally {
                lock.unlock();
            }
        }

        public List<Cookie> getMyCookie(String domain) {
            if (cookieStore.containsKey(domain)) {
                return new ArrayList<>(cookieStore.get(domain).values());
            } else {
                return new ArrayList<>();
            }
        }

        @Override
        public void addCookie(String domain, List<HttpCookie> cookieList) {
            if (cookieList == null || cookieList.isEmpty()) {
                return;
            }


            List<Cookie> cookies = cookieList.stream().map(httpCookie -> {
                String cookieDomain = httpCookie.getDomain();
                if (cookieDomain.startsWith(".")){
                    cookieDomain = cookieDomain.substring(1);
                }
                return new Cookie.Builder().domain(cookieDomain).path(httpCookie.getPath()).name(httpCookie.getName()).value(httpCookie.getValue()).build();
            }).collect(Collectors.toList());

            addMyCookie(domain, cookies);
        }

        @Override
        public void clearCookie(String domain) {
            lock.lock();
            try {
                cookieStore.remove(domain);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public List<HttpCookie> getCookies(String domain) {
            if (cookieStore.containsKey(domain)) {
                return cookieStore.get(domain).values().stream().map(cookie -> {
                    HttpCookie httpCookie = new HttpCookie(cookie.name(), cookie.value());
                    httpCookie.setDomain(cookie.domain());
                    httpCookie.setPath(cookie.path());
                    return httpCookie;
                }).collect(Collectors.toList());
            } else {
                return new ArrayList<>();
            }
        }

        @Override
        public HttpCookie getCookie(String domain, String name) {
            if (cookieStore.containsKey(domain)) {
                Cookie cookie = cookieStore.get(domain).get(name);
                if (cookie != null) {
                    HttpCookie httpCookie = new HttpCookie(cookie.name(), cookie.value());
                    httpCookie.setDomain(cookie.domain());
                    httpCookie.setPath(cookie.path());
                    return httpCookie;
                }
            }
            return null;
        }
    }
}
