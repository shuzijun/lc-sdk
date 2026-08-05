package com.shuzijun.lc.model;


import com.alibaba.fastjson2.annotation.JSONField;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    private String solutionNum;

    public QuestionView() {
    }

    public QuestionView(String title) {
        this.title = title;
    }

    public QuestionView(QuestionView source) {
        source.copyTo(this);
    }

    public QuestionView copy() {
        return new QuestionView(this);
    }

    protected void copyTo(QuestionView target) {
        target.title = title;
        target.titleCn = titleCn;
        target.questionId = questionId;
        target.level = level;
        target.status = status;
        target.titleSlug = titleSlug;
        target.frontendQuestionId = frontendQuestionId;
        target.acceptance = acceptance;
        target.frequency = frequency;
        target.category = category;
        target.paidOnly = paidOnly;
        target.solutionNum = solutionNum;
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
        return level == null ? 0 : level;
    }

    @JSONField(name = "level")
    public void setLevel(Integer level) {
        this.level = level;
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

    @JSONField(name = "acceptance")
    public void setAcceptance(Double acceptance) {
        this.acceptance = normalizePercentage(acceptance);
    }

    @JSONField(name = "acRate")
    public void setAcRate(Double acceptance) {
        this.acceptance = normalizePercentage(acceptance);
    }

    public Double getFrequency() {
        return frequency;
    }

    @JSONField(name = "frequency")
    public void setFrequency(Double frequency) {
        this.frequency = normalizePercentage(frequency);
    }

    @JSONField(name = "freqBar")
    public void setFreqBar(Double frequency) {
        this.frequency = normalizePercentage(frequency);
    }

    private static Double normalizePercentage(Double value) {
        if (value != null && value > 1) {
            return value / 100;
        }
        return value;
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

    @JSONField(name = "isPaidOnly")
    public void setIsPaidOnly(boolean paidOnly) {
        this.paidOnly = paidOnly;
    }

    public String getSolutionNum() {
        return solutionNum;
    }

    public void setSolutionNum(String solutionNum) {
        this.solutionNum = solutionNum;
    }

    @Override
    public String toString() {
        return getStatusSign() + getFormTitle();
    }

    private static final Map<Character, Integer> SORT;

    static {
        Map<Character, Integer> sortOrder = new HashMap<>();
        String sortStr = "剑面";
        for (int i = 0; i < sortStr.length(); i++) {
            sortOrder.put(sortStr.charAt(i), i);
        }
        SORT = Collections.unmodifiableMap(sortOrder);
    }

    public int frontendQuestionIdCompareTo(QuestionView questionView) {
        Integer i1 = SORT.get(frontendQuestionId.charAt(0));
        Integer i2 = SORT.get(questionView.frontendQuestionId.charAt(0));
        if (i1 != null && i2 != null) {
            if (i1 != i2) {
                return i1.compareTo(i2);
            }else {
                return frontendQuestionId.compareTo(questionView.frontendQuestionId);
            }
        } else if (i1 != null) {
            return 1;
        } else if (i2 != null) {
            return -1;
        }
        if (frontendQuestionId.length() != questionView.frontendQuestionId.length()) {
            return frontendQuestionId.length() - questionView.frontendQuestionId.length();
        }

        return frontendQuestionId.compareTo(questionView.frontendQuestionId);

    }

}
