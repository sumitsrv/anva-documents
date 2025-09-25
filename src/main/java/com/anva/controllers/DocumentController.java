package com.anva.controllers;

import com.anva.models.WordFrequency;
import com.anva.services.interfaces.WordFrequencyAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> calculateHighestFrequency(@RequestBody String text) {
        try {
            // Input validation
            if (text == null || text.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Error: Input text cannot be null or empty");
            }

            int highestFrequency = wordFrequencyAnalyzer.calculateHighestFrequency(text);
            return ResponseEntity.ok(highestFrequency);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Error: Invalid input - " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: An unexpected error occurred while processing the request - " + e.getMessage());
        }
    }

    /**
     * API to calculate the frequency of a specific word in the given text.
     *
     * @param text The input text to analyze
     * @param word The word whose frequency is to be calculated
     * @return The frequency of the specified word in the text
     */
    @PostMapping("/word-frequency")
    public ResponseEntity<?> calculateFrequencyForWord(
            @RequestBody String text,
            @RequestParam String word) {
        try {
            // Input validation
            if (text == null || text.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Error: Input text cannot be null or empty");
            }

            if (word == null || word.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Error: Word parameter cannot be null or empty");
            }

            int frequency = wordFrequencyAnalyzer.calculateFrequencyForWord(text, word);
            return ResponseEntity.ok(frequency);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Error: Invalid input - " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: An unexpected error occurred while processing the request - " + e.getMessage());
        }
    }

    /**
     * API to calculate the most frequent N words in the given text.
     *
     * @param text The input text to analyze
     * @param n The number of most frequent words to return
     * @return A list of WordFrequency objects representing the most frequent N words
     */
    @PostMapping("/most-frequent-words")
    public ResponseEntity<?> calculateMostFrequentNWords(
            @RequestBody String text,
            @RequestParam int n) {
        try {
            // Input validation
            if (text == null || text.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Error: Input text cannot be null or empty");
            }

            if (n <= 0) {
                return ResponseEntity.badRequest()
                        .body("Error: Number of words (n) must be greater than 0");
            }

            List<WordFrequency> mostFrequentWords = wordFrequencyAnalyzer.calculateMostFrequentNWords(text, n);
            return ResponseEntity.ok(mostFrequentWords);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Error: Invalid input - " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: An unexpected error occurred while processing the request - " + e.getMessage());
        }
    }
}