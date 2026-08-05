package com.shuzijun.lc.model;

public final class CodeStartResult {

    private final int statusCode;
    private final String id;
    private final String expectedId;
    private final String testCase;

    private CodeStartResult(int statusCode, String id, String expectedId, String testCase) {
        this.statusCode = statusCode;
        this.id = id;
        this.expectedId = expectedId;
        this.testCase = testCase;
    }

    public static CodeStartResult fromRun(RunCodeResult result) {
        return new CodeStartResult(
                statusCode(result.getHttpStatueCode()),
                result.getInterpretId(),
                result.getExpectedInterpretId(),
                result.getTestCase()
        );
    }

    public static CodeStartResult fromSubmit(SubmitResult result) {
        return new CodeStartResult(
                statusCode(result.getHttpStatueCode()),
                result.getSubmissionIdValue(),
                null,
                null
        );
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getId() {
        return id;
    }

    public String getExpectedId() {
        return expectedId;
    }

    public String getTestCase() {
        return testCase;
    }

    private static int statusCode(Integer statusCode) {
        return statusCode == null ? -1 : statusCode;
    }
}
