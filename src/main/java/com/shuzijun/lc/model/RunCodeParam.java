package com.shuzijun.lc.model;

import com.alibaba.fastjson2.annotation.JSONField;

public class RunCodeParam {

    @JSONField(name = "question_id")
    private String questionId;
    @JSONField(serialize = false)
    private String titleSlug;
    @JSONField(name = "data_input")
    private String dataInput;
    @JSONField(name = "lang")
    private String lang;
    @JSONField(name = "judge_type")
    private String judgeType = "large";
    @JSONField(name = "typed_code")
    private String typedCode;

    /**
     * @param questionId {@link Question#getQuestionId()}
     * @param titleSlug  {@link Question#getTitleSlug()}
     * @param dataInput  {@link Question#getTestCase()} 或者 自定义测试用例
     * @param lang       语言类型
     * @param typedCode  代码
     */
    public RunCodeParam(String questionId, String titleSlug, String dataInput, String lang, String typedCode) {
        this.questionId = questionId;
        this.titleSlug = titleSlug;
        this.dataInput = dataInput;
        this.lang = lang;
        this.typedCode = typedCode;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getTitleSlug() {
        return titleSlug;
    }

    public String getDataInput() {
        return dataInput;
    }

    public String getLang() {
        return lang;
    }

    public String getJudgeType() {
        return judgeType;
    }

    public String getTypedCode() {
        return typedCode;
    }
}
