package com.shuzijun.lc.model;

public class CodeSnippet {

    private String code;

    private String lang;

    private String langSlug;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if (code != null) {
            code = code.replaceAll("\\n", "\n");
        }
        this.code = code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLangSlug() {
        return langSlug;
    }

    public void setLangSlug(String langSlug) {
        this.langSlug = langSlug;
    }
}
