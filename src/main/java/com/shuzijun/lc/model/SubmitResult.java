package com.shuzijun.lc.model;

public class SubmitResult {

    private Integer httpStatueCode;

    private Integer submissionId;

    private String submissionIdValue;

    public Integer getHttpStatueCode() {
        return httpStatueCode;
    }

    public void setHttpStatueCode(Integer httpStatueCode) {
        this.httpStatueCode = httpStatueCode;
    }

    public Integer getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Integer submissionId) {
        this.submissionId = submissionId;
        this.submissionIdValue = submissionId == null ? null : submissionId.toString();
    }

    public String getSubmissionIdValue() {
        return submissionIdValue;
    }

    public void setSubmissionIdValue(String submissionIdValue) {
        this.submissionIdValue = submissionIdValue;
        try {
            this.submissionId = submissionIdValue == null ? null : Integer.valueOf(submissionIdValue);
        } catch (NumberFormatException ignored) {
            this.submissionId = null;
        }
    }
}
