# Anva Document Word Counter

A Spring Boot REST API for analyzing word frequencies in text documents.

## How to Run the Application

```bash
mvn spring-boot:run
```

The application starts on port `8080` by default.

## API Endpoints

All endpoints are prefixed with `/documents` and accept plain text as request body.

### 1. Calculate Highest Frequency
**Endpoint:** `POST /documents/highest-frequency`
- **Input:** Plain text string
- **Output:** Integer (highest word frequency)

### 2. Calculate Word Frequency  
**Endpoint:** `POST /documents/word-frequency?word={word}`
- **Input:** Plain text string + query parameter `word`
- **Output:** Integer (frequency of specified word)

### 3. Calculate Most Frequent N Words
**Endpoint:** `POST /documents/most-frequent-words?n={number}`  
- **Input:** Plain text string + query parameter `n`
- **Output:** List of WordFrequency objects with word and frequency

## System Architecture

The application follows a Model View Controller (MVC) architecture:

- **Controller Layer** (`DocumentController`): REST endpoints handling HTTP requests/responses
- **Service Layer** (`WordFrequencyAnalyzer`): Business logic for word frequency analysis
- **Model Layer** (`WordFrequency`): Data models representing word-frequency pairs

## Algorithmic Choices in Word Frequency Processing

### Word Tokenization
- Uses regex pattern `\b[a-zA-Z]+\b` to extract alphabetic words with word boundaries
- Normalizes words to lowercase for case-insensitive counting
- Excludes punctuation and non-alphabetic characters

### Parallel Processing
- Employs Java parallel streams for potential performance gains on large text inputs
- Uses `ConcurrentHashMap` and atomic operations for thread-safe frequency counting
- Trade-off: Synchronization overhead may outweigh benefits for smaller inputs

### Data Structures
- **ConcurrentHashMap**: Thread-safe word-to-frequency mapping
- **AtomicReference/AtomicInteger**: Thread-safe counters and references
- **Stream sorting**: For top-N frequency ranking with frequency desc, word asc ordering

### Performance Considerations
- Single-pass processing for efficiency, focusing on large incoming texts. 
- However, the parallel approach may introduce overhead for smaller texts, it is optimized for larger documents.
- Although, this could be probably improved by adding a conditional processing by first checking the size of the document/text and choose a sequential processing to avoid synchronization and context switching overhead due to parallelism.
- In-memory frequency counting suitable for moderate text sizes
- Parallel processing designed for CPU-bound workloads with available cores
