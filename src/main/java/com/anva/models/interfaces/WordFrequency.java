package com.anva.models.interfaces;

/**
 * Interface representing a word and its frequency.
 */
public interface WordFrequency {
    String getWord();

    int getFrequency();

    WordFrequency setFrequency(int frequency);
}
