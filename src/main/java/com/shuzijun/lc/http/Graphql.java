package com.shuzijun.lc.http;


import com.alibaba.fastjson2.JSONObject;
import com.shuzijun.lc.command.Option;
import com.shuzijun.lc.errors.LcException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Graphql {

    private String operationName;

    private Map variables;

    private String query;

    private Graphql(String operationName, Map variables, String query) {
        this.operationName = operationName;
        this.variables = variables;
        this.query = query;
    }

    public static GraphqlBuilder builder(String url) {
        return new GraphqlBuilder(url);
    }

    public String getOperationName() {
        return operationName;
    }

    public Map getVariables() {
        return variables;
    }

    public String getQuery() {
        return query;
    }

    public String generate() {
        return JSONObject.toJSONString(this);
    }

    public static class GraphqlBuilder {

        private static final String PATH = "/graphql/";

        private static final String CNSUFFIX = "_cn.graphql";

        private final String url;

        private String operationName;

        private Map<String, Object> variables = new HashMap<>();

        private String query;

        private String suffix = ".graphql";

        private Map<String, String> header = new HashMap<>();

        private List<Option<?>> options = new ArrayList<>();

        private GraphqlBuilder(String url) {
            this.url = url;
        }

        public GraphqlBuilder cn(boolean isCn) {
            if (isCn) {
                this.suffix = CNSUFFIX;
            }
            return this;
        }

        public GraphqlBuilder operationName(String operationName) throws LcException {
            this.operationName = operationName;
            this.query(operationName);
            return this;
        }

        public GraphqlBuilder operationName(String operationName, String operationNameAlias) throws LcException {
            this.operationName = operationNameAlias;
            this.query(operationName);
            return this;
        }

        private GraphqlBuilder query(String operationName) throws LcException {
            try (InputStream inputStream = GraphqlBuilder.class.getResourceAsStream(PATH + operationName + suffix)) {
                if (inputStream == null) {
                    throw new LcException(PATH + operationName + suffix + " Path is empty");
                } else {
                    this.query = new String(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
                }
            } catch (IOException e) {
                throw new LcException(PATH + operationName + suffix + " Loading exception");
            }
            return this;
        }

        public GraphqlBuilder variables(String key, Object value) {
            variables.put(key, value);
            return this;
        }

        public GraphqlBuilder header(Map<String, String> header) {
            this.header.putAll(header);
            return this;
        }

        public GraphqlBuilder addOption(Option<?> ...option){
            this.options.addAll(Arrays.asList(option));
            return this;
        }

        public Graphql build() {
            return new Graphql(operationName, variables, query);
        }

        public HttpResponse request(ExecutorHttp executorHttp) throws LcException {
            header.putIfAbsent("referer", url);
            header.putIfAbsent("Accept", "application/json");
            return HttpRequest.builderPost(url, "application/json")
                    .body(build().generate())
                    .addHeader(header)
                    .addOption(options.toArray(new Option<?>[0]))
                    .request(executorHttp);
        }
    }

}
