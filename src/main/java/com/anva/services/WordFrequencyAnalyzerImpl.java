package com.anva.services;

import com.anva.models.WordFrequency;
import com.anva.services.interfaces.WordFrequencyAnalyzer;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the WordFrequencyAnalyzer interface.
 */
@Service
class WordFrequencyAnalyzerImpl implements WordFrequencyAnalyzer {

    /**
     * @inheritDoc
     */
    @Override
    public int calculateHighestFrequency(String text) {
        return 0;
    }

    /**
     * @inheritDoc
     */
    @Override
    public int calculateFrequencyForWord(String text, String word) {
        return 0;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<WordFrequency> calculateMostFrequentNWords(String text, int n) {
        return List.of();
    }
}