package com.anva.controllers;

import com.anva.application.ApplicationBoot;
import com.anva.controllers.DocumentController;
import com.anva.models.interfaces.WordFrequency;
import com.anva.models.WordFrequencyImpl;
import com.anva.services.interfaces.WordFrequencyAnalyzer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DocumentController.class)
@ContextConfiguration(classes = ApplicationBoot.class)
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WordFrequencyAnalyzer wordFrequencyAnalyzer;

    private static final String BASE_URL = "/documents";

    @Test
    void calculateHighestFrequency_ShouldReturnHighestFrequency_WhenValidTextProvided() throws Exception {
        // Given
        String inputText = "The quick brown fox jumps over the lazy dog";
        int expectedFrequency = 2;
        when(wordFrequencyAnalyzer.calculateHighestFrequency(inputText)).thenReturn(expectedFrequency);

        // When & Then
        mockMvc.perform(post(BASE_URL + "/highest-frequency")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(inputText))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("2"));
    }

    @Test
    void calculateHighestFrequency_ShouldReturnZero_WhenEmptyTextProvided() throws Exception {
        // Given
        String inputText = "";
        int expectedFrequency = 0;
        when(wordFrequencyAnalyzer.calculateHighestFrequency(inputText)).thenReturn(expectedFrequency);

        // When & Then
        mockMvc.perform(post(BASE_URL + "/highest-frequency")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(inputText))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    @Test
    void calculateFrequencyForWord_ShouldReturnWordFrequency_WhenValidTextAndWordProvided() throws Exception {
        // Given
        String inputText = "The quick brown fox jumps over the lazy dog";
        String word = "the";
        int expectedFrequency = 2;
        when(wordFrequencyAnalyzer.calculateFrequencyForWord(inputText, word)).thenReturn(expectedFrequency);

        // When & Then
        mockMvc.perform(post(BASE_URL + "/word-frequency")
                        .param("word", word)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(inputText))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("2"));
    }

    @Test
    void calculateFrequencyForWord_ShouldReturnZero_WhenWordNotFound() throws Exception {
        // Given
        String inputText = "The quick brown fox jumps over the lazy dog";
        String word = "elephant";
        int expectedFrequency = 0;
        when(wordFrequencyAnalyzer.calculateFrequencyForWord(inputText, word)).thenReturn(expectedFrequency);

        // When & Then
        mockMvc.perform(post(BASE_URL + "/word-frequency")
                        .param("word", word)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(inputText))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    @Test
    void calculateFrequencyForWord_ShouldReturnBadRequest_WhenWordParameterMissing() throws Exception {
        // Given
        String inputText = "The quick brown fox jumps over the lazy dog";

        // When & Then
        mockMvc.perform(post(BASE_URL + "/word-frequency")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(inputText))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateMostFrequentNWords_ShouldReturnTopWords_WhenValidTextAndNProvided() throws Exception {
        // Given
        String inputText = "The quick brown fox jumps over the lazy dog. The fox is quick.";
        int n = 3;
        List<WordFrequency> expectedWords = Arrays.asList(
                new WordFrequencyImpl("the", 2),
                new WordFrequencyImpl("quick", 2),
                new WordFrequencyImpl("fox", 2)
        );
        when(wordFrequencyAnalyzer.calculateMostFrequentNWords(inputText, n)).thenReturn(expectedWords);

        // When & Then
        mockMvc.perform(post(BASE_URL + "/most-frequent-words")
                        .param("n", String.valueOf(n))
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(inputText))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].word").value("the"))
                .andExpect(jsonPath("$[0].frequency").value(2))
                .andExpect(jsonPath("$[1].word").value("quick"))
                .andExpect(jsonPath("$[1].frequency").value(2))
                .andExpect(jsonPath("$[2].word").value("fox"))
                .andExpect(jsonPath("$[2].frequency").value(2));
    }

    @Test
    void calculateMostFrequentNWords_ShouldReturnEmptyList_WhenEmptyText() throws Exception {
        // Given
        String inputText = "";
        int n = 5;
        List<WordFrequency> expectedWords = List.of();
        when(wordFrequencyAnalyzer.calculateMostFrequentNWords(inputText, n)).thenReturn(expectedWords);

        // When & Then
        mockMvc.perform(post(BASE_URL + "/most-frequent-words")
                        .param("n", String.valueOf(n))
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(inputText))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void calculateMostFrequentNWords_ShouldReturnBadRequest_WhenNParameterMissing() throws Exception {
        // Given
        String inputText = "The quick brown fox jumps over the lazy dog";

        // When & Then
        mockMvc.perform(post(BASE_URL + "/most-frequent-words")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(inputText))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateMostFrequentNWords_ShouldHandleZeroN() throws Exception {
        // Given
        String inputText = "The quick brown fox";
        int n = 0;
        List<WordFrequency> expectedWords = List.of();
        when(wordFrequencyAnalyzer.calculateMostFrequentNWords(inputText, n)).thenReturn(expectedWords);

        // When & Then
        mockMvc.perform(post(BASE_URL + "/most-frequent-words")
                        .param("n", String.valueOf(n))
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(inputText))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void calculateHighestFrequency_ShouldHandleSpecialCharacters() throws Exception {
        // Given
        String inputText = "Hello, world! How are you? I'm fine, thanks.";
        int expectedFrequency = 1;
        when(wordFrequencyAnalyzer.calculateHighestFrequency(inputText)).thenReturn(expectedFrequency);

        // When & Then
        mockMvc.perform(post(BASE_URL + "/highest-frequency")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(inputText))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }
}
