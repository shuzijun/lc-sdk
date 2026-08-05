package com.shuzijun.lc.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stable code execution view shared by run and submit checks.
 */
public final class CodeExecutionResult {

    private final String state;
    private final boolean runSuccess;
    private final Integer statusCode;
    private final String statusRuntime;
    private final BigDecimal runtimePercentile;
    private final String statusMemory;
    private final BigDecimal memoryPercentile;
    private final String input;
    private final String codeOutput;
    private final String expectedOutput;
    private final String standardOutput;
    private final String lastTestCase;
    private final List<String> codeAnswers;
    private final List<String> codeOutputs;
    private final List<String> expectedCodeAnswers;
    private final String statusMessage;
    private final String fullCompileError;
    private final String fullRuntimeError;

    private CodeExecutionResult(Builder builder) {
        this.state = builder.state;
        this.runSuccess = builder.runSuccess;
        this.statusCode = builder.statusCode;
        this.statusRuntime = builder.statusRuntime;
        this.runtimePercentile = builder.runtimePercentile;
        this.statusMemory = builder.statusMemory;
        this.memoryPercentile = builder.memoryPercentile;
        this.input = builder.input;
        this.codeOutput = builder.codeOutput;
        this.expectedOutput = builder.expectedOutput;
        this.standardOutput = builder.standardOutput;
        this.lastTestCase = builder.lastTestCase;
        this.codeAnswers = immutableCopy(builder.codeAnswers);
        this.codeOutputs = immutableCopy(builder.codeOutputs);
        this.expectedCodeAnswers = immutableCopy(builder.expectedCodeAnswers);
        this.statusMessage = builder.statusMessage;
        this.fullCompileError = builder.fullCompileError;
        this.fullRuntimeError = builder.fullRuntimeError;
    }

    public static CodeExecutionResult fromRun(RunCodeCheckResult result) {
        if (result == null) {
            throw new IllegalArgumentException("Run result must not be null");
        }
        return new Builder()
                .state(result.getState())
                .runSuccess(result.getRunSuccess())
                .codeAnswers(result.getCodeAnswers())
                .codeOutputs(result.getCodeOutputs())
                .expectedCodeAnswers(result.getExpectedCodeAnswers())
                .statusMessage(result.getStatusMsg())
                .fullRuntimeError(result.getFullRuntimeError())
                .build();
    }

    public static CodeExecutionResult fromSubmit(SubmitCheckResult result) {
        if (result == null) {
            throw new IllegalArgumentException("Submit result must not be null");
        }
        return new Builder()
                .state(result.getState())
                .runSuccess(result.getRunSuccess())
                .statusCode(result.getStatusCode())
                .statusRuntime(result.getStatusRuntime())
                .runtimePercentile(result.getRuntimePercentile())
                .statusMemory(result.getStatusMemory())
                .memoryPercentile(result.getMemoryPercentile())
                .input(result.getInput())
                .codeOutput(result.getCodeOutput())
                .expectedOutput(result.getExpectedOutput())
                .standardOutput(result.getStdOutput())
                .lastTestCase(result.getLastTestcase())
                .statusMessage(result.getStatusMsg())
                .fullCompileError(result.getFullCompileError())
                .fullRuntimeError(result.getFullRuntimeError())
                .build();
    }

    public boolean isComplete() {
        return "SUCCESS".equals(state);
    }

    public String getState() {
        return state;
    }

    public boolean isRunSuccess() {
        return runSuccess;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getStatusRuntime() {
        return statusRuntime;
    }

    public BigDecimal getRuntimePercentile() {
        return runtimePercentile;
    }

    public String getStatusMemory() {
        return statusMemory;
    }

    public BigDecimal getMemoryPercentile() {
        return memoryPercentile;
    }

    public String getInput() {
        return input;
    }

    public String getCodeOutput() {
        return codeOutput;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public String getStandardOutput() {
        return standardOutput;
    }

    public String getLastTestCase() {
        return lastTestCase;
    }

    public List<String> getCodeAnswers() {
        return codeAnswers;
    }

    public List<String> getCodeOutputs() {
        return codeOutputs;
    }

    public List<String> getExpectedCodeAnswers() {
        return expectedCodeAnswers;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getFullCompileError() {
        return fullCompileError;
    }

    public String getFullRuntimeError() {
        return fullRuntimeError;
    }

    private static List<String> immutableCopy(List<String> values) {
        return values == null
                ? Collections.<String>emptyList()
                : Collections.unmodifiableList(new ArrayList<>(values));
    }

    private static final class Builder {
        private String state;
        private boolean runSuccess;
        private Integer statusCode;
        private String statusRuntime;
        private BigDecimal runtimePercentile;
        private String statusMemory;
        private BigDecimal memoryPercentile;
        private String input;
        private String codeOutput;
        private String expectedOutput;
        private String standardOutput;
        private String lastTestCase;
        private List<String> codeAnswers;
        private List<String> codeOutputs;
        private List<String> expectedCodeAnswers;
        private String statusMessage;
        private String fullCompileError;
        private String fullRuntimeError;

        private Builder state(String value) {
            state = value;
            return this;
        }

        private Builder runSuccess(boolean value) {
            runSuccess = value;
            return this;
        }

        private Builder statusCode(Integer value) {
            statusCode = value;
            return this;
        }

        private Builder statusRuntime(String value) {
            statusRuntime = value;
            return this;
        }

        private Builder runtimePercentile(BigDecimal value) {
            runtimePercentile = value;
            return this;
        }

        private Builder statusMemory(String value) {
            statusMemory = value;
            return this;
        }

        private Builder memoryPercentile(BigDecimal value) {
            memoryPercentile = value;
            return this;
        }

        private Builder input(String value) {
            input = value;
            return this;
        }

        private Builder codeOutput(String value) {
            codeOutput = value;
            return this;
        }

        private Builder expectedOutput(String value) {
            expectedOutput = value;
            return this;
        }

        private Builder standardOutput(String value) {
            standardOutput = value;
            return this;
        }

        private Builder lastTestCase(String value) {
            lastTestCase = value;
            return this;
        }

        private Builder codeAnswers(List<String> value) {
            codeAnswers = value;
            return this;
        }

        private Builder codeOutputs(List<String> value) {
            codeOutputs = value;
            return this;
        }

        private Builder expectedCodeAnswers(List<String> value) {
            expectedCodeAnswers = value;
            return this;
        }

        private Builder statusMessage(String value) {
            statusMessage = value;
            return this;
        }

        private Builder fullCompileError(String value) {
            fullCompileError = value;
            return this;
        }

        private Builder fullRuntimeError(String value) {
            fullRuntimeError = value;
            return this;
        }

        private CodeExecutionResult build() {
            return new CodeExecutionResult(this);
        }
    }
}
