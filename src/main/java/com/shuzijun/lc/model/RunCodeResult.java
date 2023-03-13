package com.shuzijun.lc.model;

import com.alibaba.fastjson2.annotation.JSONField;

public class RunCodeResult {

    @JSONField(name = "interpret_id")
    private String interpretId;

    @JSONField(name = "test_case")
    private String testCase;

    public String getInterpretId() {
        return interpretId;
    }

    public void setInterpretId(String interpretId) {
        this.interpretId = interpretId;
    }

    public String getTestCase() {
        return testCase;
    }

    public void setTestCase(String testCase) {
        this.testCase = testCase;
    }
}
