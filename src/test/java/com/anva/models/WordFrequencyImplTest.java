package com.anva.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WordFrequencyImplTest {
    @Test
    void testConstructorAndGetters() {
        WordFrequencyImpl wf = new WordFrequencyImpl("hello", 3);
        assertEquals("hello", wf.getWord());
        assertEquals(3, wf.getFrequency());
    }

    @Test
    void testSetFrequency() {
        WordFrequencyImpl wf = new WordFrequencyImpl("test", 1);
        wf.setFrequency(5);
        assertEquals(5, wf.getFrequency());
    }

    @Test
    void testNegativeFrequency() {
        WordFrequencyImpl wf = new WordFrequencyImpl("neg", -2);
        assertEquals("neg", wf.getWord());
        assertEquals(-2, wf.getFrequency());
        wf.setFrequency(-10);
        assertEquals(-10, wf.getFrequency());
    }
}
