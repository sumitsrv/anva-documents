package com.anva.services.interfaces;

import com.anva.models.interfaces.WordFrequency;

import java.util.List;

/**
 * Interface for analyzing word frequencies in a given text.
 */
public interface WordFrequencyAnalyzer {

    /**
     * Calculates the highest frequency of any word in the given text.
     *
     * @param text The input text to analyze.
     * @return The highest frequency of any word in the text.
     */
    int calculateHighestFrequency(String text);

    /**
     * Calculates the frequency of a specific word in the given text.
     *
     * @param text The input text to analyze.
     * @param word The word whose frequency is to be calculated.
     * @return The frequency of the specified word in the text.
     */
    int calculateFrequencyForWord(String text, String word);

    /**
     * Calculates the most frequent N words in the given text.
     *
     * @param text The input text to analyze.
     * @param n    The number of most frequent words to return.
     * @return A list of WordFrequency objects representing the most frequent N words.
     */
    List<WordFrequency> calculateMostFrequentNWords(String text, int n);
}
