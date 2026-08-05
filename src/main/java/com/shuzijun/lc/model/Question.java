package com.shuzijun.lc.model;


import java.util.List;

public class Question extends QuestionView {

    private String testCase;
    private String exampleTestcases;
    /**
     * 题目描述
     */
    private String content;

    private String translatedContent;


    /**
     * 所有的代码片段
     */
    private List<CodeSnippet> codeSnippets;

    private List<Tag> topicTags;

    private Integer likes;

    private Integer dislikes;

    private String solution;

    /**
     * 相似题目
     */
    private String similarQuestions;

    /**
     * 提示
     */
    private List<String> hints;

    /**
     * 代码元数据
     */
    private CodeMetaData metaData;

    public Question() {
        super();

    }

    public Question(String title) {
        super(title);

    }

    public Question(Question source) {
        super(source);
        testCase = source.testCase;
        exampleTestcases = source.exampleTestcases;
        content = source.content;
        translatedContent = source.translatedContent;
        codeSnippets = source.codeSnippets;
        topicTags = source.topicTags;
        likes = source.likes;
        dislikes = source.dislikes;
        solution = source.solution;
        similarQuestions = source.similarQuestions;
        hints = source.hints;
        metaData = source.metaData;
    }

    @Override
    public Question copy() {
        return new Question(this);
    }

    public String getTestCase() {
        return testCase;
    }

    public void setTestCase(String testCase) {
        this.testCase = testCase;
    }

    public String getExampleTestcases() {
        return exampleTestcases;
    }

    public void setExampleTestcases(String exampleTestcases) {
        this.exampleTestcases = exampleTestcases;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTranslatedContent() {
        return translatedContent;
    }

    public void setTranslatedContent(String translatedContent) {
        this.translatedContent = translatedContent;
    }

    public List<CodeSnippet> getCodeSnippets() {
        return codeSnippets;
    }

    public void setCodeSnippets(List<CodeSnippet> codeSnippets) {
        this.codeSnippets = codeSnippets;
    }

    public List<Tag> getTopicTags() {
        return topicTags;
    }

    public void setTopicTags(List<Tag> topicTags) {
        this.topicTags = topicTags;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getDislikes() {
        return dislikes;
    }

    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
    }

    public String getSimilarQuestions() {
        return similarQuestions;
    }

    public void setSimilarQuestions(String similarQuestions) {
        this.similarQuestions = similarQuestions;
    }

    public List<String> getHints() {
        return hints;
    }

    public void setHints(List<String> hints) {
        this.hints = hints;
    }

    public CodeMetaData getCodeMetaData() {
        return metaData;
    }

    public void setCodeMetaData(CodeMetaData metaData) {
        this.metaData = metaData;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
