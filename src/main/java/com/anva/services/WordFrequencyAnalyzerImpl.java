package com.anva.services;

import com.anva.models.WordFrequency;
import com.anva.models.WordFrequencyImpl;
import com.anva.services.interfaces.WordFrequencyAnalyzer;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

/**
 * Implementation of the WordFrequencyAnalyzer interface.
 */
@Service
class WordFrequencyAnalyzerImpl implements WordFrequencyAnalyzer {

    final static Pattern ALL_WORDS_REGEX_PATTERN_MATCHER = Pattern.compile("\\b[a-zA-Z]+\\b");

    /**
     * @inheritDoc
     */
    @Override
    public int calculateHighestFrequency(String text) {
        AtomicReference<String> wordWithHighestFrequency = new AtomicReference<>("");
        AtomicInteger highestFrequency = new AtomicInteger(0);
        ConcurrentHashMap<String, WordFrequency> wordFrequencyMap = new ConcurrentHashMap<>();

        ALL_WORDS_REGEX_PATTERN_MATCHER.matcher(text).results().parallel().forEach(it -> {
            wordFrequencyMap.compute(it.group().toLowerCase(), (k, v) -> {
                if (v == null) {
                    return new WordFrequencyImpl(k, 1);
                } else {
                    if (v.getFrequency() >= highestFrequency.get()) {
                        highestFrequency.set(v.getFrequency());
                        wordWithHighestFrequency.set(v.getWord());
                    }
                    return v.setFrequency(v.getFrequency() + 1);
                }
            });
        });

        return wordFrequencyMap.get(wordWithHighestFrequency.get()).getFrequency();
    }

    /**
     * @inheritDoc
     */
    @Override
    public int calculateFrequencyForWord(String text, String word) {
        Pattern wordPatternRegex = Pattern.compile("\\b" + word + "\\b");
        AtomicInteger frequency = new AtomicInteger(0);
        wordPatternRegex.matcher(text).results().parallel().forEach(it -> frequency.getAndIncrement());
        return frequency.get();
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<WordFrequency> calculateMostFrequentNWords(String text, int n) {
        LinkedList<WordFrequency> mostFrequentWords = new LinkedList<>();
        ConcurrentHashMap<String, WordFrequency> wordFrequencyMap = new ConcurrentHashMap<>();

        ALL_WORDS_REGEX_PATTERN_MATCHER.matcher(text).results().parallel().forEach(it -> {
            wordFrequencyMap.compute(it.group().toLowerCase(), (k, v) -> {
                if (v == null) {
                    return new WordFrequencyImpl(k, 1);
                } else {
                    return v.setFrequency(v.getFrequency() + 1);
                }
            });
        });
        return mostFrequentWords;
    }
}