package com.shuzijun.lc.model;

import com.alibaba.fastjson2.annotation.JSONField;

public class RunCodeResult {

    @JSONField(name = "interpret_id")
    private String interpretId;

    @JSONField(name = "interpret_expected_id")
    private String expectedInterpretId;

    @JSONField(name = "test_case")
    private String testCase;

    private Integer httpStatueCode;

    public String getInterpretId() {
        return interpretId;
    }

    public void setInterpretId(String interpretId) {
        this.interpretId = interpretId;
    }

    public String getExpectedInterpretId() {
        return expectedInterpretId;
    }

    public void setExpectedInterpretId(String expectedInterpretId) {
        this.expectedInterpretId = expectedInterpretId;
    }

    public String getTestCase() {
        return testCase;
    }

    public void setTestCase(String testCase) {
        this.testCase = testCase;
    }

    public Integer getHttpStatueCode() {
        return httpStatueCode;
    }

    public void setHttpStatueCode(Integer httpStatueCode) {
        this.httpStatueCode = httpStatueCode;
    }
}
