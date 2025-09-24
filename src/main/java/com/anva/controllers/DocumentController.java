package com.anva.controllers;

import com.anva.models.WordFrequency;
import com.anva.services.interfaces.WordFrequencyAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling document-related operations.
 */
@RestController
@RequestMapping("documents")
class DocumentController {

    @Autowired
    private WordFrequencyAnalyzer wordFrequencyAnalyzer;

    /**
     * API to calculate the highest frequency of any word in the given text.
     *
     * @param text The input text to analyze
     * @return The highest frequency of any word in the text
     */
    @PostMapping("/highest-frequency")
    public ResponseEntity<Integer> calculateHighestFrequency(@RequestBody String text) {
        int highestFrequency = wordFrequencyAnalyzer.calculateHighestFrequency(text);
        return ResponseEntity.ok(highestFrequency);
    }

    /**
     * API to calculate the frequency of a specific word in the given text.
     *
     * @param text The input text to analyze
     * @param word The word whose frequency is to be calculated
     * @return The frequency of the specified word in the text
     */
    @PostMapping("/word-frequency")
    public ResponseEntity<Integer> calculateFrequencyForWord(
            @RequestBody String text,
            @RequestParam String word) {
        int frequency = wordFrequencyAnalyzer.calculateFrequencyForWord(text, word);
        return ResponseEntity.ok(frequency);
    }

    /**
     * API to calculate the most frequent N words in the given text.
     *
     * @param text The input text to analyze
     * @param n The number of most frequent words to return
     * @return A list of WordFrequency objects representing the most frequent N words
     */
    @PostMapping("/most-frequent-words")
    public ResponseEntity<List<WordFrequency>> calculateMostFrequentNWords(
            @RequestBody String text,
            @RequestParam int n) {
        List<WordFrequency> mostFrequentWords = wordFrequencyAnalyzer.calculateMostFrequentNWords(text, n);
        return ResponseEntity.ok(mostFrequentWords);
    }
}