# AI Web Feed Reader

A Spring Boot application that fetches RSS feeds, enhances them with full-text content, and uses AI to generate relevant tags for each article. Built for smarter, automated content discovery and categorization.

## RabbitMQ Integration

This application includes RabbitMQ integration for asynchronous message processing of RSS feeds.

### Configuration

The RabbitMQ configuration is defined in `application.yaml`:

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    virtual-host: /
    listener:
      simple:
        concurrency: 3
        max-concurrency: 10
        prefetch: 1
        default-requeue-rejected: false
```

### Components

- **RabbitMQConfig**: Configuration class that sets up queues, exchanges, and bindings
- **RabbitMQComponent**: Service component that handles message listening and sending

### Usage

The RabbitMQ integration automatically:
1. Listens for messages on the `rss.feed.queue`
2. Processes RSS feed messages asynchronously
3. Sends feed processing notifications when feeds are processed

### Running with RabbitMQ

1. Start RabbitMQ server (using Docker):
   ```bash
   docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
   ```

2. Start the application:
   ```bash
   ./gradlew bootRun
   ```

3. The application will automatically connect to RabbitMQ and start listening for messages.

### Message Flow

1. When RSS feeds are processed via `WebFeedService.getFeeds()`, a message is sent to RabbitMQ
2. The `RabbitMQComponent.handleRssFeedMessage()` method processes these messages
3. You can extend the message processing logic in the `processRssFeedMessage()` method

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
