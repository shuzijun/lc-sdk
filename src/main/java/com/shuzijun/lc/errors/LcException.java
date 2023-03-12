package com.shuzijun.lc.errors;

public class LcException extends Exception {
    private static final long serialVersionUID = -7241010318779326306L;

    String httpTrace = null;

    public LcException() {
        super();
    }

    public LcException(String message) {
        super(message);
    }

    public LcException(String message, Exception e) {
        super(message, e);
    }

    public LcException(String message, String httpTrace) {
        super(message);
        this.httpTrace = httpTrace;
    }

    public LcException(String message, Exception e, String httpTrace) {
        super(message);
        this.httpTrace = httpTrace;
    }

    public String httpTrace() {
        return this.httpTrace;
    }
}