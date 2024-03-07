package com.shuzijun.lc.model;

import com.alibaba.fastjson2.annotation.JSONField;

public class RunCodeCheckResult {

    private String state;
    @JSONField(name = "code_answer")
    private String codeAnswer;
    @JSONField(name = "code_output")
    private String codeOutput;
    @JSONField(name = "elapsed_time")
    private String elapsedTime;
    @JSONField(name = "fast_submit")
    private String fastSubmit;
    @JSONField(name = "full_runtime_error")
    private String fullRuntimeError;
    private String lang;
    @JSONField(name = "memory")
    private String memory;
    @JSONField(name = "memory_percentile")
    private String memoryPercentile;
    @JSONField(name = "pretty_lang")
    private String prettyLang;
    @JSONField(name = "run_success")
    private boolean runSuccess;
    @JSONField(name = "runtime_error")
    private String runtimeError;
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
    @JSONField(name = "std_output_list")
    private String stdOutputList;
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
    @JSONField(name = "compare_result")
    private String compareResult;
    @JSONField(name = "correct_answer")
    private String correctAnswer;
    @JSONField(name = "display_runtime")
    private String displayRuntime;
    @JSONField(name = "expected_code_answer")
    private String expectedCodeAnswer;
    @JSONField(name = "expected_code_output")
    private String expectedCodeOutput;
    @JSONField(name = "expected_elapsed_time")
    private String expectedElapsedTime;
    @JSONField(name = "expected_lang")
    private String expectedLang;
    @JSONField(name = "expected_memory")
    private String expectedMemory;
    @JSONField(name = "expected_run_success")
    private String expectedRunSuccess;
    @JSONField(name = "expected_status_code")
    private String expectedStatusCode;
    @JSONField(name = "expected_status_runtime")
    private String expectedStatusRuntime;
    @JSONField(name = "expected_std_output_list")
    private String expectedStdOutputList;
    @JSONField(name = "expected_task_finish_time")
    private String expectedTaskFinishTime;
    @JSONField(name = "expected_task_name")
    private String expectedTaskName;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCodeAnswer() {
        return codeAnswer;
    }

    public void setCodeAnswer(String codeAnswer) {
        this.codeAnswer = codeAnswer;
    }

    public String getCodeOutput() {
        return codeOutput;
    }

    public void setCodeOutput(String codeOutput) {
        this.codeOutput = codeOutput;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getFastSubmit() {
        return fastSubmit;
    }

    public void setFastSubmit(String fastSubmit) {
        this.fastSubmit = fastSubmit;
    }

    public String getFullRuntimeError() {
        return fullRuntimeError;
    }

    public void setFullRuntimeError(String fullRuntimeError) {
        this.fullRuntimeError = fullRuntimeError;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
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

    public boolean getRunSuccess() {
        return runSuccess;
    }

    public void setRunSuccess(boolean runSuccess) {
        this.runSuccess = runSuccess;
    }

    public String getRuntimeError() {
        return runtimeError;
    }

    public void setRuntimeError(String runtimeError) {
        this.runtimeError = runtimeError;
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

    public String getStdOutputList() {
        return stdOutputList;
    }

    public void setStdOutputList(String stdOutputList) {
        this.stdOutputList = stdOutputList;
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

    public String getCompareResult() {
        return compareResult;
    }

    public void setCompareResult(String compareResult) {
        this.compareResult = compareResult;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getDisplayRuntime() {
        return displayRuntime;
    }

    public void setDisplayRuntime(String displayRuntime) {
        this.displayRuntime = displayRuntime;
    }

    public String getExpectedCodeAnswer() {
        return expectedCodeAnswer;
    }

    public void setExpectedCodeAnswer(String expectedCodeAnswer) {
        this.expectedCodeAnswer = expectedCodeAnswer;
    }

    public String getExpectedCodeOutput() {
        return expectedCodeOutput;
    }

    public void setExpectedCodeOutput(String expectedCodeOutput) {
        this.expectedCodeOutput = expectedCodeOutput;
    }

    public String getExpectedElapsedTime() {
        return expectedElapsedTime;
    }

    public void setExpectedElapsedTime(String expectedElapsedTime) {
        this.expectedElapsedTime = expectedElapsedTime;
    }

    public String getExpectedLang() {
        return expectedLang;
    }

    public void setExpectedLang(String expectedLang) {
        this.expectedLang = expectedLang;
    }

    public String getExpectedMemory() {
        return expectedMemory;
    }

    public void setExpectedMemory(String expectedMemory) {
        this.expectedMemory = expectedMemory;
    }

    public String getExpectedRunSuccess() {
        return expectedRunSuccess;
    }

    public void setExpectedRunSuccess(String expectedRunSuccess) {
        this.expectedRunSuccess = expectedRunSuccess;
    }

    public String getExpectedStatusCode() {
        return expectedStatusCode;
    }

    public void setExpectedStatusCode(String expectedStatusCode) {
        this.expectedStatusCode = expectedStatusCode;
    }

    public String getExpectedStatusRuntime() {
        return expectedStatusRuntime;
    }

    public void setExpectedStatusRuntime(String expectedStatusRuntime) {
        this.expectedStatusRuntime = expectedStatusRuntime;
    }

    public String getExpectedStdOutputList() {
        return expectedStdOutputList;
    }

    public void setExpectedStdOutputList(String expectedStdOutputList) {
        this.expectedStdOutputList = expectedStdOutputList;
    }

    public String getExpectedTaskFinishTime() {
        return expectedTaskFinishTime;
    }

    public void setExpectedTaskFinishTime(String expectedTaskFinishTime) {
        this.expectedTaskFinishTime = expectedTaskFinishTime;
    }

    public String getExpectedTaskName() {
        return expectedTaskName;
    }

    public void setExpectedTaskName(String expectedTaskName) {
        this.expectedTaskName = expectedTaskName;
    }
}
