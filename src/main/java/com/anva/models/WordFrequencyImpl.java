package com.anva.models;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation of the WordFrequency interface.
 */
public class WordFrequencyImpl implements WordFrequency {
    private final String word;
    private AtomicInteger frequency;

    /**
     * Default constructor.
     */
    public WordFrequencyImpl(String word, int frequency) {
        this.word = word;
        this.frequency = new AtomicInteger(frequency);
    }

    @Override
    public String getWord() {
        return this.word;
    }

    @Override
    public int getFrequency() {
        return 0;
    }

    public WordFrequency setFrequency(int frequency) {
        this.frequency.set(frequency);
        return this;
    }
}
