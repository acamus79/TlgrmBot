# Arquitectura del Sistema - Telegrm API

Este documento describe la estructura arquitectónica de la aplicación, basada en los principios de la **Arquitectura Hexagonal (Puertos y Adaptadores)**.

## 1. Estructura de Capas

La aplicación se divide en tres capas principales, representadas por la estructura de paquetes en `src/main/java/com/acamus/telegrm`:

-   `core`: El núcleo de la aplicación.
-   `application`: La capa de casos de uso.
-   `infrastructure`: La capa de infraestructura.

### 1.1. `core` (El Hexágono)

Esta es la capa más interna y pura. No tiene dependencias de ningún framework o librería externa (excepto las anotaciones de validación estándar de Jakarta).

-   **`core/domain/model`**: Contiene las entidades del dominio (`User`, `Conversation`, `Message`). Encapsulan el estado y las reglas de negocio.
-   **`core/domain/valueobjects`**: Contiene los Value Objects (`Email`, `Password`, `TelegramChatId`) que garantizan la validez y el significado de los datos.
-   **`core/domain/exception`**: Excepciones de negocio personalizadas (`InvalidCredentialsException`, `EmailAlreadyExistsException`).
-   **`core/ports/in`**: Los **Puertos de Entrada**. Definen cómo el mundo exterior interactúa con la aplicación. Son interfaces implementadas por los casos de uso.
-   **`core/ports/out`**: Los **Puertos de Salida**. Definen qué necesita la aplicación del mundo exterior (ej. persistencia, APIs externas). Son interfaces implementadas por los adaptadores de infraestructura.

### 1.2. `application`

Esta capa orquesta la lógica de negocio.

-   **`application/usecases`**: Implementaciones concretas de los Puertos de Entrada. Cada caso de uso tiene una única responsabilidad (ej. `RegisterUserUseCase`, `ProcessTelegramUpdateUseCase`).

### 1.3. `infrastructure`

Esta capa contiene todo lo que interactúa con el mundo exterior. Es el "pegamento" que conecta el núcleo con la tecnología.

-   **`infrastructure/adapters/in`**: **Adaptadores de Entrada (Driving Adapters)**. Invocan los casos de uso.
    -   `web`: Controladores REST que exponen la API.
    -   `scheduler`: Tareas programadas que inician un flujo de negocio (ej. el polling de Telegram).
-   **`infrastructure/adapters/out`**: **Adaptadores de Salida (Driven Adapters)**. Implementan los Puertos de Salida.
    -   `persistence`: Implementaciones de los repositorios usando Spring Data JPA.
    -   `security`: Implementaciones para la generación de tokens (JWT) y hashing de contraseñas.
    -   `ai`: Cliente para la API de OpenRouter.
    -   `telegram`: Cliente para la API de Telegram.
-   **`infrastructure/decorators`**: Decoradores que añaden comportamiento transversal (como `@Transactional`) a los casos de uso, permitiendo que la capa de aplicación permanezca agnóstica al framework.
-   **`infrastructure/config`**: Clases de configuración de Spring (`@Configuration`) donde se ensambla la aplicación (creación de beans, configuración de seguridad, etc.).
-   **`infrastructure/exceptions`**: Manejador de excepciones global (`@RestControllerAdvice`).

## 2. Flujo de una Petición (Ejemplo: Mensaje de Telegram)

1.  **Adaptador de Entrada**: `TelegramPollingService` (Scheduler) llama a la API de Telegram.
2.  **Conversión**: El adaptador convierte el DTO de Telegram en un `ProcessUpdateCommand` (objeto del `core`).
3.  **Puerto de Entrada**: Invoca el método `processUpdate` del puerto `ProcessTelegramUpdatePort`.
4.  **Caso de Uso**: `ProcessTelegramUpdateUseCase` (o su decorador transaccional) recibe el comando.
5.  **Lógica de Negocio**:
    -   Usa el `ConversationRepositoryPort` (Puerto de Salida) para buscar o crear una `Conversation`.
    -   Usa el `MessageRepositoryPort` (Puerto de Salida) para guardar el mensaje.
    -   Usa el `AiGeneratorPort` (Puerto de Salida) para obtener una respuesta.
    -   Usa el `TelegramPort` (Puerto de Salida) para enviar la respuesta.
6.  **Adaptadores de Salida**: Las implementaciones concretas (`ConversationRepositoryAdapter`, `OpenRouterAdapter`, etc.) realizan el trabajo "sucio" (hablar con la BD, llamar a la API de IA, etc.).

Este flujo garantiza que el caso de uso no sabe nada sobre PostgreSQL, Spring, Telegram o OpenRouter. Solo conoce las abstracciones definidas por los puertos.

## 3. Flujo de Envío Proactivo (API REST)

1.  **Adaptador de Entrada**: `ConversationController` recibe `POST /api/conversations/{id}/messages`.
2.  **Puerto de Entrada**: Invoca `sendMessage` del puerto `SendMessagePort`.
3.  **Caso de Uso**: `SendMessageUseCase` ejecuta la lógica:
    -   Valida la existencia de la conversación.
    -   Guarda el mensaje saliente.
    -   Usa `TelegramPort` para enviar el mensaje a Telegram.
4.  **Adaptadores de Salida**: `TelegramAdapter` realiza la llamada HTTP a la API de Telegram.
