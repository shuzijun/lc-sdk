package com.shuzijun.lc.model;


import com.alibaba.fastjson2.annotation.JSONField;
import org.apache.commons.lang3.StringUtils;

public class QuestionView {
    private String title;
    private String titleCn;
    private String questionId;
    private Integer level;
    private String status;
    private String titleSlug;
    /**
     * 页面的题目编号
     */
    private String frontendQuestionId;

    /**
     * 通过率 %
     */
    private Double acceptance = 0D;

    /**
     * 频率
     */
    private Double frequency = 0d;

    private String category;

    private boolean paidOnly;

    public QuestionView() {
    }

    public QuestionView(String title) {
        this.title = title;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFormTitle() {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotBlank(frontendQuestionId)) {
            sb.append("[").append(frontendQuestionId).append("]");
        }
        return sb.append(title).toString();
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public Integer getLevel() {
        return level;
    }

    @JSONField(name = "difficulty")
    public void setLevel(String difficulty) {
        if (difficulty == null) {
            this.level = 0;
        } else if ("easy".equalsIgnoreCase(difficulty)) {
            this.level = 1;
        } else if ("medium".equalsIgnoreCase(difficulty)) {
            this.level = 2;
        } else if ("hard".equalsIgnoreCase(difficulty)) {
            this.level = 3;
        } else if ("1".equals(difficulty) || "2".equals(difficulty) || "3".equals(difficulty)) {
            this.level = Integer.valueOf(difficulty);
        } else {
            this.level = 0;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status != null) {
            status = status.toLowerCase();
        }
        this.status = status;
    }


    public String getTitleSlug() {
        return titleSlug;
    }

    public void setTitleSlug(String titleSlug) {
        this.titleSlug = titleSlug;
    }

    public String getFrontendQuestionId() {
        return frontendQuestionId;
    }

    public void setFrontendQuestionId(String frontendQuestionId) {
        this.frontendQuestionId = frontendQuestionId;
    }

    public Double getAcceptance() {
        return acceptance;
    }

    @JSONField(name = "acRate")
    public void setAcceptance(Double acceptance) {
        if (acceptance != null && acceptance.doubleValue() > 1) {
            acceptance = acceptance / 100;
        }
        this.acceptance = acceptance;
    }

    public Double getFrequency() {
        return frequency;
    }

    @JSONField(name = "freqBar")
    public void setFrequency(Double frequency) {
        if (frequency != null && frequency.doubleValue() > 1) {
            frequency = frequency / 100;
        }
        this.frequency = frequency;
    }

    public String getStatusSign() {

        if ("notac".equalsIgnoreCase(status) || "TRIED".equalsIgnoreCase(status)) {
            return "?";
        } else if ("ac".equalsIgnoreCase(status)) {
            return "✔";
        } else if ("lock".equalsIgnoreCase(status)) {
            return "$";
        } else if ("day".equalsIgnoreCase(status)) {
            return "D";
        } else if (level != null) {
            return " ";
        }
        return " ";
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitleCn() {
        return titleCn;
    }

    public void setTitleCn(String titleCn) {
        this.titleCn = titleCn;
    }

    public boolean isPaidOnly() {
        return paidOnly;
    }

    public void setPaidOnly(boolean paidOnly) {
        this.paidOnly = paidOnly;
    }
}
