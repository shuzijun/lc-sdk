package com.shuzijun.lc.model;

import com.alibaba.fastjson2.annotation.JSONField;

public class SubmissionDetail {

    private String runtime;
    private String memory;
    @JSONField(name = "totalTestCaseCnt")
    private String totalTestcases;
    @JSONField(name = "passedTestCaseCnt")
    private String totalCorrect;
    @JSONField(name = "input_formatted")
    private String inputFormatted;
    @JSONField(name = "expected_output")
    private String expectedOutput;
    @JSONField(name = "code_output")
    private String codeOutput;
    @JSONField(name = "runtime_error")
    private String runtimeError;
    @JSONField(name = "last_testcase")
    private String lastTestcase;
    @JSONField(name = "compile_error")
    private String compileError;

    private String submissionCode;

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getTotalTestcases() {
        return totalTestcases;
    }

    public void setTotalTestcases(String totalTestcases) {
        this.totalTestcases = totalTestcases;
    }

    public String getTotalCorrect() {
        return totalCorrect;
    }

    public void setTotalCorrect(String totalCorrect) {
        this.totalCorrect = totalCorrect;
    }

    public String getInputFormatted() {
        return inputFormatted;
    }

    public void setInputFormatted(String inputFormatted) {
        this.inputFormatted = inputFormatted;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

    public String getCodeOutput() {
        return codeOutput;
    }

    public void setCodeOutput(String codeOutput) {
        this.codeOutput = codeOutput;
    }

    public String getRuntimeError() {
        return runtimeError;
    }

    public void setRuntimeError(String runtimeError) {
        this.runtimeError = runtimeError;
    }

    public String getLastTestcase() {
        return lastTestcase;
    }

    public void setLastTestcase(String lastTestcase) {
        this.lastTestcase = lastTestcase;
    }

    public String getCompileError() {
        return compileError;
    }

    public void setCompileError(String compileError) {
        this.compileError = compileError;
    }

    public String getSubmissionCode() {
        return submissionCode;
    }

    public void setSubmissionCode(String submissionCode) {
        this.submissionCode = submissionCode;
    }
}
