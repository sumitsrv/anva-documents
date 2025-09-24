# Anva Document Word Counter

A Spring Boot REST API application for analyzing word frequencies in text documents.

## Overview

This application provides REST endpoints to analyze text and calculate various word frequency metrics including:
- Highest frequency of any word in a text
- Frequency of a specific word in a text  
- Most frequent N words in a text

## Technologies Used

- **Java 24**
- **Spring Boot 3.5.3**
- **Spring Web** (REST API)
- **Maven** (Build tool)

## Project Structure

```
src/
├── main/
│   ├── java/com/anva/
│   │   ├── application/
│   │   │   └── ApplicationBoot.java          # Main Spring Boot application class
│   │   ├── controllers/
│   │   │   └── DocumentController.java       # REST controller for document operations
│   │   ├── models/
│   │   │   └── WordFrequency.java           # Interface for word frequency model
│   │   └── services/
│   │       ├── WordFrequencyAnalyzerImpl.java # Service implementation
│   │       └── interfaces/
│   │           └── WordFrequencyAnalyzer.java # Service interface
│   └── resources/
│       └── application.properties            # Application configuration
└── test/
    ├── java/                                # Test source directory
    └── resources/                           # Test resources
```

## Prerequisites

- Java 24 or higher
- Maven 3.6+ 
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd AnvaDocumentWordCounter
```

### 2. Build the Project

```bash
mvn clean compile
```

### 3. Run Tests

```bash
mvn test
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

Alternatively, you can run the main class directly from your IDE.

The application will start on port `8080` by default.

## API Endpoints

All endpoints are prefixed with `/documents`

### 1. Calculate Highest Frequency

Returns the highest frequency of any word in the given text.

**Endpoint:** `POST /documents/highest-frequency`

**Request Body:** Plain text string

**Response:** Integer representing the highest word frequency

**Example:**
```bash
curl -X POST http://localhost:8080/documents/highest-frequency \
  -H "Content-Type: text/plain" \
  -d "The quick brown fox jumps over the lazy dog. The fox is quick."
```

### 2. Calculate Word Frequency

Returns the frequency of a specific word in the given text.

**Endpoint:** `POST /documents/word-frequency?word={word}`

**Request Body:** Plain text string

**Query Parameter:** `word` - The word to count

**Response:** Integer representing the word frequency

**Example:**
```bash
curl -X POST "http://localhost:8080/documents/word-frequency?word=the" \
  -H "Content-Type: text/plain" \
  -d "The quick brown fox jumps over the lazy dog. The fox is quick."
```

### 3. Calculate Most Frequent N Words

Returns the N most frequent words in the given text.

**Endpoint:** `POST /documents/most-frequent-words?n={number}`

**Request Body:** Plain text string

**Query Parameter:** `n` - Number of top frequent words to return

**Response:** List of WordFrequency objects

**Example:**
```bash
curl -X POST "http://localhost:8080/documents/most-frequent-words?n=3" \
  -H "Content-Type: text/plain" \
  -d "The quick brown fox jumps over the lazy dog. The fox is quick."
```

## Configuration

The application uses the following default configuration in `application.properties`:

```properties
server.port=8080
```

You can override these settings by:
- Modifying `src/main/resources/application.properties`
- Using environment variables
- Passing JVM arguments
