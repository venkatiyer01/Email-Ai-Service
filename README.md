# 🚀 Email AI Service

AI-powered email reply generator built using **Spring Boot 4 (Java 21)** and **Google Gemini API**.

This backend service generates intelligent email responses using a clean architecture (Port-Adapter pattern) and provider abstraction for extensibility.

---

## 🏗 Architecture

- Clean Architecture (Hexagonal / Port-Adapter)
- Service Layer abstraction
- Provider-based AI integration
- External API integration using WebClient
- Environment-based configuration

---

## 🛠 Tech Stack

- Java 21
- Spring Boot 4
- Maven
- WebClient
- Google Gemini 2.5 Flash
- REST APIs

---

## 📌 Features

- Generate AI-powered email replies
- Configurable tone & context
- Provider abstraction layer (extensible to OpenAI/Anthropic)
- REST API endpoint
- Clean separation of concerns

---

## 📂 Project Structure

src/main/java/io/emailassistant
├── controller
├── service
├── provider
├── config
└── EmailAiServiceApplication.java


---

## ▶️ Running Locally

Clone the repository:

git clone https://github.com/venkatiyer01/Email-Ai-Service.git


Navigate into the folder:
cd Email-Ai-Service


Run the application:
./mvnw spring-boot:run


---

## 🔐 Environment Variables

Set your Gemini API key inside:
application.properties
gemini.api.key=YOUR_API_KEY


⚠️ Never commit API keys to GitHub.

---

## 🧪 Example API Request

POST:
http://localhost:8080/api/email/generate

Body:

```json
{
  "emailContent": "Hi team, I will not be able to attend today's meeting due to a personal emergency.",
  "tone": "professional"
}

👩‍💻 Author

Built by Venkatraman R Iyer
Backend Engineer


