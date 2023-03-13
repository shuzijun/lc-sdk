package com.shuzijun.lc.model;

import com.alibaba.fastjson2.annotation.JSONField;

public class SubmitCheckResult {

    private String state;
    @JSONField(name = "code_output")
    private String codeOutput;
    @JSONField(name = "compare_result")
    private String compareResult;
    @JSONField(name = "display_runtime")
    private String displayRuntime;
    @JSONField(name = "elapsed_time")
    private String elapsedTime;
    @JSONField(name = "expected_output")
    private String expectedOutput;
    @JSONField(name = "fast_submit")
    private String fastSubmit;
    @JSONField(name = "finished")
    private String finished;
    private String lang;
    @JSONField(name = "last_testcase")
    private String lastTestcase;
    private String memory;
    @JSONField(name = "memory_percentile")
    private String memoryPercentile;
    @JSONField(name = "pretty_lang")
    private String prettyLang;
    @JSONField(name = "question_id")
    private String questionId;
    @JSONField(name = "run_success")
    private String runSuccess;
    @JSONField(name = "runtime_percentile")
    private String runtimePercentile;
    @JSONField(name = "status_code")
    private String statusCode;
    @JSONField(name = "status_memory")
    private String statusMemory;
    @JSONField(name = "status_msg")
    private String statusMsg;
    @JSONField(name = "status_runtime")
    private String statusRuntime;
    @JSONField(name = "std_output")
    private String stdOutput;
    @JSONField(name = "submission_id")
    private String submissionId;
    @JSONField(name = "task_finish_time")
    private String taskFinishTime;
    @JSONField(name = "task_name")
    private String taskName;
    @JSONField(name = "total_correct")
    private String totalCorrect;
    @JSONField(name = "total_testcases")
    private String totalTestcases;
    @JSONField(name = "compile_error")
    private String compileError;
    @JSONField(name = "full_compile_error")
    private String fullCompileError;
    @JSONField(name = "input")
    private String input;
    @JSONField(name = "input_formatted")
    private String inputFormatted;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCodeOutput() {
        return codeOutput;
    }

    public void setCodeOutput(String codeOutput) {
        this.codeOutput = codeOutput;
    }

    public String getCompareResult() {
        return compareResult;
    }

    public void setCompareResult(String compareResult) {
        this.compareResult = compareResult;
    }

    public String getDisplayRuntime() {
        return displayRuntime;
    }

    public void setDisplayRuntime(String displayRuntime) {
        this.displayRuntime = displayRuntime;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

    public String getFastSubmit() {
        return fastSubmit;
    }

    public void setFastSubmit(String fastSubmit) {
        this.fastSubmit = fastSubmit;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLastTestcase() {
        return lastTestcase;
    }

    public void setLastTestcase(String lastTestcase) {
        this.lastTestcase = lastTestcase;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getMemoryPercentile() {
        return memoryPercentile;
    }

    public void setMemoryPercentile(String memoryPercentile) {
        this.memoryPercentile = memoryPercentile;
    }

    public String getPrettyLang() {
        return prettyLang;
    }

    public void setPrettyLang(String prettyLang) {
        this.prettyLang = prettyLang;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getRunSuccess() {
        return runSuccess;
    }

    public void setRunSuccess(String runSuccess) {
        this.runSuccess = runSuccess;
    }

    public String getRuntimePercentile() {
        return runtimePercentile;
    }

    public void setRuntimePercentile(String runtimePercentile) {
        this.runtimePercentile = runtimePercentile;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMemory() {
        return statusMemory;
    }

    public void setStatusMemory(String statusMemory) {
        this.statusMemory = statusMemory;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public String getStatusRuntime() {
        return statusRuntime;
    }

    public void setStatusRuntime(String statusRuntime) {
        this.statusRuntime = statusRuntime;
    }

    public String getStdOutput() {
        return stdOutput;
    }

    public void setStdOutput(String stdOutput) {
        this.stdOutput = stdOutput;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public String getTaskFinishTime() {
        return taskFinishTime;
    }

    public void setTaskFinishTime(String taskFinishTime) {
        this.taskFinishTime = taskFinishTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTotalCorrect() {
        return totalCorrect;
    }

    public void setTotalCorrect(String totalCorrect) {
        this.totalCorrect = totalCorrect;
    }

    public String getTotalTestcases() {
        return totalTestcases;
    }

    public void setTotalTestcases(String totalTestcases) {
        this.totalTestcases = totalTestcases;
    }

    public String getCompileError() {
        return compileError;
    }

    public void setCompileError(String compileError) {
        this.compileError = compileError;
    }

    public String getFullCompileError() {
        return fullCompileError;
    }

    public void setFullCompileError(String fullCompileError) {
        this.fullCompileError = fullCompileError;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getInputFormatted() {
        return inputFormatted;
    }

    public void setInputFormatted(String inputFormatted) {
        this.inputFormatted = inputFormatted;
    }
}
