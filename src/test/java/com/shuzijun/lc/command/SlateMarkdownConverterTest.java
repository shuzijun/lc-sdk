package com.shuzijun.lc.command;

import org.junit.Assert;
import org.junit.Test;

public class SlateMarkdownConverterTest {

    @Test
    public void convertsCommonSlateNodesToMarkdown() {
        String slateValue = "["
                + "{\"type\":\"Heading1\",\"children\":[{\"text\":\"Approach\"}]},"
                + "{\"type\":\"Paragraph\",\"children\":["
                + "{\"text\":\"Use \",\"bold\":true},"
                + "{\"type\":\"Link\",\"href\":\"https://example.test\",\"children\":[{\"text\":\"a map\"}]},"
                + "{\"text\":\" and \"},{\"text\":\"carry\",\"code\":true}]},"
                + "{\"type\":\"BlockQuote\",\"children\":[{\"text\":\"Keep the carry.\"}]},"
                + "{\"type\":\"Image\",\"src\":\"https://example.test/diagram.png\",\"children\":[{\"text\":\"\"}]},"
                + "{\"type\":\"CodeBlock\",\"children\":["
                + "{\"type\":\"CodeTab\",\"language\":\"java\",\"children\":[{\"text\":\"return sum;\"}]}]}"
                + "]";

        String markdown = SlateMarkdownConverter.convert(slateValue);

        Assert.assertEquals(
                "# Approach\n\n"
                        + "**Use **[a map](https://example.test) and `carry`\n\n"
                        + "> Keep the carry.\n\n"
                        + "![](https://example.test/diagram.png)\n\n"
                        + "```java\nreturn sum;\n```",
                markdown
        );
    }

    @Test
    public void returnsNullForMissingOrMalformedSlateValue() {
        Assert.assertNull(SlateMarkdownConverter.convert(null));
        Assert.assertNull(SlateMarkdownConverter.convert(""));
        Assert.assertNull(SlateMarkdownConverter.convert("{not-json}"));
    }
}
