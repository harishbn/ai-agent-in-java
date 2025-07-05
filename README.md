# AI Agent with Spring Boot

This project implements a chat interface using Spring Boot and OpenAI's GPT API. It provides a RESTful endpoint that allows users to interact with OpenAI's language models, with support for both user messages and system messages.

## Features

- Spring Boot REST API
- Integration with OpenAI's Chat API
- Support for system messages to control AI behavior
- Simple request/response DTOs
- Configurable OpenAI settings

## Prerequisites

- Java 17 or higher
- Gradle
- OpenAI API Key

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/harishbn/ai-agent-in-java.git
   cd ai-agent-in-java
   ```

2. Configure OpenAI API Key:
   Create `src/main/resources/application.properties` and add your OpenAI API key:
   ```properties
   openai.api.key=your_api_key_here
   ```

3. Build the project:
   ```bash
   ./gradlew build
   ```

4. Run the application:
   ```bash
   ./gradlew bootRun
   ```

## Usage

Send POST requests to `/chat` endpoint with JSON body:

```json
{
  "message": "What is the capital of France?",
  "systemMessage": "You are a helpful assistant that specializes in geography."
}
```

The `systemMessage` is optional and can be used to set the behavior or role of the AI.

Example response:
```json
{
  "response": "The capital of France is Paris."
}
```

## API Endpoints

- POST `/chat` - Send a message to the AI agent

## Project Structure

```
src/main/java/com/example/demo/
├── config/
│   └── OpenAIConfig.java
├── controller/
│   └── ChatController.java
├── dto/
│   ├── ChatRequest.java
│   └── ChatResponse.java
├── service/
│   └── ChatService.java
└── DemoApplication.java
```

## Contributing

Feel free to open issues or submit pull requests for any improvements.

## License

This project is open source and available under the [MIT License](LICENSE). 