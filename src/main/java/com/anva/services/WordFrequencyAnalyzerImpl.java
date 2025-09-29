package com.anva.services;

import com.anva.models.WordFrequencyImpl;
import com.anva.models.interfaces.WordFrequency;
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
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        AtomicReference<WordFrequency> wordWithHighestFrequency = new AtomicReference<>(null);
        ConcurrentHashMap<String, WordFrequency> wordFrequencyMap = new ConcurrentHashMap<>();

        ALL_WORDS_REGEX_PATTERN_MATCHER.matcher(text).results().parallel().forEach(token -> {
            wordFrequencyMap.compute(token.group().toLowerCase(), (key, value) -> {
                WordFrequency newFreq;
                if (value == null) {
                    newFreq = new WordFrequencyImpl(key, 1);
                } else {
                    newFreq = value.setFrequency(value.getFrequency() + 1);
                }

                wordWithHighestFrequency.updateAndGet(current -> {
                    if (current == null || newFreq.getFrequency() > current.getFrequency()) {
                        return newFreq;
                    }
                    return current;
                });

                return newFreq;
            });
        });


        if (wordWithHighestFrequency.get() == null) {
            return 0;
        }
        return wordWithHighestFrequency.get().getFrequency();
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
        return wordFrequencyMap.values().stream()
                .sorted((a, b) -> {
                    int freqCompare = Integer.compare(b.getFrequency(), a.getFrequency());
                    return freqCompare != 0 ? freqCompare : a.getWord().compareTo(b.getWord());
                })
                .limit(n)
                .toList();
    }
}