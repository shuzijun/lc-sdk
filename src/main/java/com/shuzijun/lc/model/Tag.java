package com.shuzijun.lc.model;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.HashSet;
import java.util.Set;

public class Tag {

    @JSONField(alternateNames = {"slug", "id"})
    private String slug;

    @JSONField(alternateNames = {"name", "title"})
    private String name;

    @JSONField(alternateNames = {"translatedName"})
    private String translatedName;
    @JSONField(alternateNames = {"type", "url"})
    private String type;
    @JSONField(alternateNames = {"questions"})
    private Set<String> questions = new HashSet<>();

    private boolean isSelect = false;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTranslatedName() {
        return translatedName;
    }

    public void setTranslatedName(String translatedName) {
        this.translatedName = translatedName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<String> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<String> questions) {
        this.questions = questions;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
