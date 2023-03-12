package com.shuzijun.lc.model;


import com.alibaba.fastjson2.annotation.JSONField;

import java.util.List;

public class CodeMetaData {

    private String name;

    private List<CodeParam> params;

    @JSONField(name = "return")
    private CodeReturn codeReturn;

    private Boolean manual;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CodeParam> getParams() {
        return params;
    }

    public void setParams(List<CodeParam> params) {
        this.params = params;
    }

    public CodeReturn getCodeReturn() {
        return codeReturn;
    }

    public void setCodeReturn(CodeReturn codeReturn) {
        this.codeReturn = codeReturn;
    }

    public Boolean getManual() {
        return manual;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }
}

class CodeParam {

    private String name;

    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

class CodeReturn {
    private String type;

    private String size;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}