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

        // Parse tokens (words) using regular expression, and count in parallel for potential speedup on large inputs.
        // However, the overhead of synchronization may outweigh the benefits on smaller inputs.
        // In a heavily loaded system, keeping this to sequential processing might be better for overall thoroughput
        // because the CPU might not be available to do parallel processing, and also the context switching is additional overhead.

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
        // Map: normalized word -> mutable frequency holder (WordFrequencyImpl is used as value object here)
        ConcurrentHashMap<String, WordFrequency> wordFrequencyMap = new ConcurrentHashMap<>();

        // Parse token (words) using regular expression, and count in parallel for potential speedup on large inputs.
        // However, the overhead of synchronization may outweigh the benefits on smaller inputs.
        // In a heavily loaded system, keeping this to sequential processing might be better for overall thoroughput
        // because the CPU might not be available to do parallel processing, and also the context switching is additional overhead.
        ALL_WORDS_REGEX_PATTERN_MATCHER.matcher(text).results().parallel().forEach(it -> {
            wordFrequencyMap.compute(it.group().toLowerCase(), (k, v) -> {
                if (v == null) {
                    return new WordFrequencyImpl(k, 1); // first occurrence
                } else {
                    return v.setFrequency(v.getFrequency() + 1); // increment existing
                }
            });
        });

        // Sort by frequency desc, then word asc; truncate to n results.
        return wordFrequencyMap.values().stream()
                .sorted((a, b) -> {
                    int freqCompare = Integer.compare(b.getFrequency(), a.getFrequency());
                    return freqCompare != 0 ? freqCompare : a.getWord().compareTo(b.getWord());
                })
                .limit(n)
                .toList();
    }
}