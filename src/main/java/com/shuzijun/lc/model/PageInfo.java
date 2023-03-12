package com.shuzijun.lc.model;

import java.util.List;

public class PageInfo<T> {

    private int pageIndex;
    private int pageSize;
    private int rowTotal;

    private List<T> rows;

    public PageInfo(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        if (rowTotal <= 0) {
            pageIndex = 1;
        } else if (pageIndex > getPageTotal()) {
            pageIndex = getPageTotal();
        }
    }

    public int getPageTotal() {
        return (rowTotal / pageSize) + ((rowTotal % pageSize) > 0 ? 1 : 0);
    }

    public int getRowTotal() {
        return rowTotal;
    }

    public void setRowTotal(int rowTotal) {
        this.rowTotal = rowTotal;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

}
