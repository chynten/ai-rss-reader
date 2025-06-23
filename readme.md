# AI Web Feed Reader

A Spring Boot application that fetches RSS feeds, enhances them with full-text content, and uses AI to generate relevant tags for each article. Built for smarter, automated content discovery and categorization.

## Features
- Fetch RSS feeds from any URL
- Optionally extract and enhance feed items with the full readable content of linked articles
- Use AI (OpenAI or compatible models) to automatically generate tags for each article
- Built with Spring Boot, Thymeleaf, and Spring Security

## Tech Stack
- **Java 24**
- **Spring Boot 3.5** (Web, Security, Thymeleaf)
- **Spring AI** (Ollama model integration)
- **Jsoup** (HTML parsing)
- **Readability4J** (Extract readable content)
- **Apptastic RSS Reader** (RSS parsing)
- **Lombok** (Boilerplate reduction)

## Getting Started

### Prerequisites
- Java 24+
- [Ollama](https://ollama.com/) or compatible AI model running locally (default: `http://localhost:11434`)

### Setup
1. Clone the repository:
   ```bash
   git clone <repo-url>
   cd ai-rss-reader
   ```
2. Configure the AI model endpoint in `src/main/resources/application.yaml` if needed:
   ```yaml
   spring:
     ai:
       ollama:
         base-url: http://localhost:11434
         chat:
           options:
             model: gemma3:4b
   ```
3. Build and run the application:
   ```bash
   ./gradlew bootRun
   ```

## Usage
- Access the application in your browser (default: `http://localhost:8080`)
- Enter an RSS feed URL to fetch and analyze articles
- View enhanced content and AI-generated tags for each feed item

## Testing
- Run tests with:
  ```bash
  ./gradlew test
  ```
- Code coverage reports are generated with Jacoco

## License
MIT
