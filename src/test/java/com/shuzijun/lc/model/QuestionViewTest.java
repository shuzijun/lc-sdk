package com.shuzijun.lc.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class QuestionViewTest {

    @Test
    public void normalizesPercentageAndRatioSettersConsistently() {
        QuestionView question = new QuestionView();

        question.setFrequency(25.0d);
        assertEquals(0.25d, question.getFrequency(), 0.0001d);
        question.setFreqBar(0.5d);
        assertEquals(0.5d, question.getFrequency(), 0.0001d);

        question.setAcceptance(75.0d);
        assertEquals(0.75d, question.getAcceptance(), 0.0001d);
        question.setAcRate(1.0d);
        assertEquals(1.0d, question.getAcceptance(), 0.0001d);
    }

    @Test
    public void preservesNullPercentageValuesAndCopiedState() {
        QuestionView question = new QuestionView();
        question.setFrequency(null);
        question.setAcceptance(null);

        assertNull(question.getFrequency());
        assertNull(question.getAcceptance());
        assertNull(question.copy().getFrequency());
        assertNull(question.copy().getAcceptance());
    }
}
