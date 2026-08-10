package com.shuzijun.lc.model;

public final class CommonNoteResult {

    private boolean success;
    private CommonNote note;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public CommonNote getNote() {
        return note;
    }

    public void setNote(CommonNote note) {
        this.note = note;
    }
}
