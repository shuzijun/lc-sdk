package com.shuzijun.lc.model;

import com.alibaba.fastjson2.JSON;

import java.util.List;

public class ProblemSetParam {

    private int pageIndex;
    private int pageSize;

    private String categorySlug = "";

    private Filters filters = new Filters();

    /**
     * @param pageIndex 页码
     * @param pageSize  每页数量
     */
    public ProblemSetParam(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getSkip() {
        return (pageIndex - 1) * pageSize;
    }

    public String getCategorySlug() {
        return categorySlug;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    public Filters getFilters() {
        return filters;
    }

    public void setFilters(Filters filters) {
        this.filters = filters;
    }

    //TODO 补充枚举值
    public static class Filters {
        /**
         * 搜索关键字
         */
        private String searchKeywords;
        /**
         * 排序字段
         */
        private String orderBy;
        /**
         * 排序方式
         */
        private String sortOrder;
        /**
         * 难度
         */
        private String difficulty;
        /**
         * 状态
         */
        private String status;
        /**
         * 列表id
         */
        private String listId;
        /**
         * 标签
         */
        private List<String> tags;

        public String getSearchKeywords() {
            return searchKeywords;
        }

        public void setSearchKeywords(String searchKeywords) {
            this.searchKeywords = searchKeywords;
        }

        public String getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(String orderBy) {
            this.orderBy = orderBy;
        }

        public String getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
        }

        public String getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(String difficulty) {
            this.difficulty = difficulty;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getListId() {
            return listId;
        }

        public void setListId(String listId) {
            this.listId = listId;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }


        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }
    }
}
