package com.shuzijun.lc.command;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;

final class SlateMarkdownConverter {

    private SlateMarkdownConverter() {
    }

    static String convert(String slateValue) {
        if (StringUtils.isBlank(slateValue)) {
            return null;
        }
        JSONArray nodes;
        try {
            nodes = JSONArray.parseArray(slateValue);
        } catch (RuntimeException exception) {
            return null;
        }
        StringBuilder markdown = new StringBuilder();
        for (int i = 0; i < nodes.size(); i++) {
            appendBlock(nodes.getJSONObject(i), markdown, 0);
        }
        return StringUtils.trimToNull(markdown.toString());
    }

    private static void appendBlock(JSONObject node, StringBuilder markdown, int depth) {
        if (node == null) {
            return;
        }
        String type = node.getString("type");
        if ("Heading1".equals(type) || "Heading2".equals(type) || "Heading3".equals(type)
                || "Heading4".equals(type) || "Heading5".equals(type) || "Heading6".equals(type)) {
            int level = Character.digit(type.charAt(type.length() - 1), 10);
            markdown.append(repeat("#", level)).append(' ');
            appendInlineChildren(node, markdown);
            appendBlankLine(markdown);
        } else if ("BlockQuote".equals(type)) {
            StringBuilder quote = new StringBuilder();
            appendInlineChildren(node, quote);
            for (String line : quote.toString().split("\\R", -1)) {
                markdown.append("> ").append(line).append('\n');
            }
            markdown.append('\n');
        } else if ("CodeBlock".equals(type)) {
            appendCodeBlock(node, markdown);
        } else if ("Image".equals(type)) {
            String source = node.getString("src");
            if (StringUtils.isNotBlank(source)) {
                markdown.append("![](").append(source).append(')');
                appendBlankLine(markdown);
            }
        } else if (isList(type)) {
            appendList(node, markdown, depth);
            markdown.append('\n');
        } else if ("ListItem".equals(type)) {
            appendListItem(node, markdown, depth, "- ");
        } else {
            appendInlineChildren(node, markdown);
            appendBlankLine(markdown);
        }
    }

    private static void appendCodeBlock(JSONObject node, StringBuilder markdown) {
        JSONArray children = node.getJSONArray("children");
        if (children == null) {
            return;
        }
        for (int i = 0; i < children.size(); i++) {
            JSONObject tab = children.getJSONObject(i);
            String language = StringUtils.defaultString(tab.getString("language"));
            String code = collectText(tab);
            if (StringUtils.isBlank(code)) {
                continue;
            }
            markdown.append("```").append(language).append('\n')
                    .append(code);
            if (!code.endsWith("\n")) {
                markdown.append('\n');
            }
            markdown.append("```");
            appendBlankLine(markdown);
        }
    }

    private static void appendList(JSONObject node, StringBuilder markdown, int depth) {
        JSONArray children = node.getJSONArray("children");
        if (children == null) {
            return;
        }
        boolean ordered = "NumberedList".equals(node.getString("type"))
                || "OrderedList".equals(node.getString("type"));
        for (int i = 0; i < children.size(); i++) {
            JSONObject child = children.getJSONObject(i);
            appendListItem(child, markdown, depth, ordered ? (i + 1) + ". " : "- ");
        }
    }

    private static void appendListItem(
            JSONObject node,
            StringBuilder markdown,
            int depth,
            String marker
    ) {
        markdown.append(repeat("  ", depth)).append(marker);
        appendInlineChildren(node, markdown);
        trimTrailingNewlines(markdown);
        markdown.append('\n');
    }

    private static boolean isList(String type) {
        return "BulletedList".equals(type)
                || "UnorderedList".equals(type)
                || "NumberedList".equals(type)
                || "OrderedList".equals(type);
    }

    private static void appendInlineChildren(JSONObject node, StringBuilder markdown) {
        JSONArray children = node.getJSONArray("children");
        if (children == null) {
            appendText(node, markdown);
            return;
        }
        for (int i = 0; i < children.size(); i++) {
            JSONObject child = children.getJSONObject(i);
            String type = child.getString("type");
            if ("Link".equals(type)) {
                StringBuilder label = new StringBuilder();
                appendInlineChildren(child, label);
                String href = child.getString("href");
                if (StringUtils.isNotBlank(href)) {
                    markdown.append('[').append(label).append("](").append(href).append(')');
                } else {
                    markdown.append(label);
                }
            } else if ("Image".equals(type)) {
                String source = child.getString("src");
                if (StringUtils.isNotBlank(source)) {
                    markdown.append("![](").append(source).append(')');
                }
            } else if ("CodeTab".equals(type)) {
                markdown.append(collectText(child));
            } else if (child.containsKey("text")) {
                appendText(child, markdown);
            } else {
                appendInlineChildren(child, markdown);
            }
        }
    }

    private static void appendText(JSONObject node, StringBuilder markdown) {
        String text = node.getString("text");
        if (text == null) {
            return;
        }
        if (node.getBooleanValue("code")) {
            text = "`" + text.replace("`", "\\`") + "`";
        }
        if (node.getBooleanValue("bold")) {
            text = "**" + text + "**";
        }
        if (node.getBooleanValue("italic")) {
            text = "*" + text + "*";
        }
        if (node.getBooleanValue("strikethrough")) {
            text = "~~" + text + "~~";
        }
        markdown.append(text);
    }

    private static String collectText(JSONObject node) {
        StringBuilder text = new StringBuilder();
        appendInlineChildren(node, text);
        return text.toString();
    }

    private static String repeat(String value, int count) {
        StringBuilder repeated = new StringBuilder(value.length() * count);
        for (int i = 0; i < count; i++) {
            repeated.append(value);
        }
        return repeated.toString();
    }

    private static void appendBlankLine(StringBuilder markdown) {
        trimTrailingNewlines(markdown);
        if (markdown.length() > 0) {
            markdown.append("\n\n");
        }
    }

    private static void trimTrailingNewlines(StringBuilder value) {
        while (value.length() > 0 && value.charAt(value.length() - 1) == '\n') {
            value.setLength(value.length() - 1);
        }
    }
}
