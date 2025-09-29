package com.anva.services;

import com.anva.models.interfaces.WordFrequency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordFrequencyAnalyzerImplTest {
    private WordFrequencyAnalyzerImpl analyzer;

    @BeforeEach
    void setUp() {
        analyzer = new WordFrequencyAnalyzerImpl();
    }

    @Test
    void testCalculateHighestFrequency_basic() {
        String text = "The sun shines over the lake";
        int freq = analyzer.calculateHighestFrequency(text);
        assertEquals(2, freq); // "the" appears twice
    }

    @Test
    void testCalculateHighestFrequency_empty() {
        assertEquals(0, analyzer.calculateHighestFrequency(""));
    }

    @Test
    void testCalculateHighestFrequency_caseInsensitive() {
        String text = "Hello hello HELLO world";
        int freq = analyzer.calculateHighestFrequency(text);
        assertEquals(3, freq); // "hello" appears 3 times (case insensitive)
    }

    @Test
    void testCalculateFrequencyForWord_basic() {
        String text = "apple banana apple orange apple";
        int freq = analyzer.calculateFrequencyForWord(text, "apple");
        assertEquals(3, freq);
    }

    @Test
    void testCalculateMostFrequentNWords_basic() {
        String text = "cat bat cat dog bat cat";
        List<WordFrequency> result = analyzer.calculateMostFrequentNWords(text, 2);
        assertEquals(2, result.size());

        // Check that the most frequent words are "cat" (3 times) and "bat" (2 times)
        assertEquals("cat", result.get(0).getWord());
        assertEquals(3, result.get(0).getFrequency());
        assertEquals("bat", result.get(1).getWord());
        assertEquals(2, result.get(1).getFrequency());
    }
}
