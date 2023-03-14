package com.shuzijun.lc.model;

import com.alibaba.fastjson2.annotation.JSONField;

public class SubmitParam {

    @JSONField(name = "typed_code")
    private String typedCode;
    @JSONField(name = "lang")
    private String lang;
    @JSONField(serialize = false)
    private String titleSlug;
    @JSONField(name = "question_id")
    private String questionId;

    /**
     * @param typedCode  代码
     * @param lang       语言类型
     * @param titleSlug  {@link Question#getTitleSlug()}
     * @param questionId {@link Question#getQuestionId()}
     */
    public SubmitParam(String typedCode, String lang, String titleSlug, String questionId) {
        this.typedCode = typedCode;
        this.lang = lang;
        this.titleSlug = titleSlug;
        this.questionId = questionId;
    }

    public String getTypedCode() {
        return typedCode;
    }

    public void setTypedCode(String typedCode) {
        this.typedCode = typedCode;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTitleSlug() {
        return titleSlug;
    }

    public void setTitleSlug(String titleSlug) {
        this.titleSlug = titleSlug;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
