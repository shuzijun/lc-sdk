package com.shuzijun.lc.model;

import java.util.Collections;
import java.util.List;

public final class CommonNotePage {

    private int count;
    private List<CommonNote> notes = Collections.emptyList();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<CommonNote> getNotes() {
        return notes;
    }

    public void setNotes(List<CommonNote> notes) {
        this.notes = notes == null ? Collections.emptyList() : notes;
    }
}
