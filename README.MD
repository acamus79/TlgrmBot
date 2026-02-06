# TelegrmBOT API - Backend Hexagonal
<div style="display: inline_block"><br><br>
 <p align="center">
  <a href="#" target="_blank"><img align="center" alt="JAVA" height="84" width="84" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg"></a>
  <a href="#" target="_blank"><img align="center" alt="SPRING" height="84" width="84" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg"></a>
  <a href="#" target="_blank"><img align="center" alt="MAVEN" height="84" width="84" src="https://www.svgrepo.com/show/373829/maven.svg"></a>
  <a href="#" target="_blank"><img align="center" alt="POSTGRESQL" height="84" width="84" src="https://www.svgrepo.com/show/303301/postgresql-logo.svg" /></a>
  <a href="#" target="_blank"><img align="center" alt="SWAGGER" height="84" width="84" src="https://www.svgrepo.com/show/374111/swagger.svg" /></a>
  <a href="#" target="_blank"><img align="center" alt="Docker" height="84" width="84" src="https://www.svgrepo.com/show/452192/docker.svg" /></a>
  <a href="#" target="_blank"><img align="center" alt="Intellijidea" height="84" width="84" src="https://www.svgrepo.com/show/306240/intellijidea.svg" /></a>
  </p>
 </div>

Esta es una API REST desarrollada como parte de un ejercicio Backend. El proyecto integra el servicio de mensajería de Telegram, responde a los mensajes utilizando IA y expone una API para gestionar las conversaciones, todo construido sobre una estricta **Arquitectura Hexagonal**.

## ✨ Features

-   **Autenticación de Usuarios:** Sistema de registro y login con JWT.
-   **Integración con Telegram:** Escucha mensajes de Telegram mediante polling.
-   **Respuestas con IA:** Genera respuestas a los mensajes utilizando un modelo de lenguaje configurable a través de OpenRouter.
-   **API de Gestión:** Endpoints REST para listar conversaciones y enviar mensajes.
-   **Arquitectura Robusta:** Implementación estricta de Arquitectura Hexagonal (Puertos y Adaptadores) y principios de Domain-Driven Design (DDD).
-   **Base de Datos Versionada:** Migraciones gestionadas con Flyway.
-   **Contenerizado:** Totalmente dockerizado para un despliegue fácil y consistente.
-   **Documentación de API:** Documentación interactiva con Swagger (OpenAPI).

## 🏛️ Arquitectura y Diseño

El proyecto sigue los siguientes principios de diseño:

-   **Arquitectura Hexagonal (Puertos y Adaptadores):** El núcleo de la aplicación (dominio y casos de uso) es completamente independiente de la infraestructura (frameworks, bases de datos, APIs externas).
-   **Domain-Driven Design (DDD):** Uso de Entidades, Value Objects y Agregados para modelar el dominio del negocio.
-   **Configuración Explícita:** Las dependencias se inyectan a través de clases de configuración (`@Configuration`) para mantener el control sobre el ensamblaje de la aplicación.

*Para un análisis más profundo de las decisiones de diseño, consultar el archivo `ARCHITECTURE.MD`.*

## 🛠️ Tech Stack

-   **Lenguaje:** Java 21
-   **Framework:** Spring Boot 3
-   **Seguridad:** Spring Security (JWT)
-   **Base de Datos:** PostgreSQL
-   **Persistencia:** Spring Data JPA / Hibernate
-   **Migraciones:** Flyway
-   **Contenerización:** Docker & Docker Compose
-   **Testing:** JUnit 5, Mockito, Testcontainers
-   **IA:** OpenRouter API
-   **Build Tool:** Maven
-   **IDE:** IntelliJ Idea (Community Edition)

## 🚀 Getting Started

### Prerrequisitos

-   JDK 21 o superior
-   Maven 3.9 o superior
-   Docker y Docker Compose

### ⚙️ Configuración

Para ejecutar la aplicación, necesitas configurar las siguientes variables de entorno.

1.  Crea un archivo llamado `.env` en la raíz del proyecto.
2.  Copia el contenido de `.env.example` en tu nuevo archivo `.env`.
3.  Rellena los valores de las siguientes variables:

| Variable           | Descripción                                                                                             | Cómo Obtenerla                                                                                             |
| ------------------ | ------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------- |
| `DB_USER`          | Usuario para la base de datos PostgreSQL.                                                               | (El valor por defecto `postgres` suele funcionar).                                                         |
| `DB_PASSWORD`      | Contraseña para el usuario de la base de datos.                                                         | (Define una contraseña segura).                                                                            |
| `JWT_SECRET`       | Clave secreta larga y segura para firmar los tokens JWT.                                                | Genera una cadena aleatoria larga. Puedes usar `openssl rand -base64 32`.                                  |
| `TELEGRAM_BOT_TOKEN` | Token de tu bot de Telegram.                                                                            | Habla con **@BotFather** en Telegram, crea un nuevo bot con `/newbot` y copia el token que te proporciona. |
| `OPENROUTE_KEY`    | API Key para acceder a los modelos de IA a través de OpenRouter.                                        | Crea una cuenta en OpenRouter.ai y genera una nueva API Key en la sección 'Keys'. |
| `AI_SYSTEM_PROMPT` | El "prompt de sistema" que define la personalidad del bot.                                              | (Opcional, tiene un valor por defecto "loco").                                                             |
| `AI_TEMPERATURE`   | Controla la creatividad de la IA (0.1 = predecible, 1.5 = caótico).                                     | (Opcional, tiene un valor por defecto).                                                                    |
| `AI_MAX_TOKENS`    | Longitud máxima de la respuesta de la IA.                                                               | (Opcional, tiene un valor por defecto).                                                                    |

### 🐳 Ejecución con Docker (Recomendado)

Esta es la forma más fácil y recomendada de levantar todo el entorno.

1.  **Asegúrate de tener Docker corriendo.**
2.  **Configura tu archivo `.env`** con las claves necesarias.
3.  **Ejecuta el siguiente comando** en la raíz del proyecto:
    ```sh
    docker-compose up --build
    ```

La aplicación estará disponible en `http://localhost:8080`.

### 💻 Ejecución Local (Para Desarrollo)

1.  **Asegúrate de tener una instancia de PostgreSQL** corriendo localmente.
2.  **Configura las variables de entorno** en tu sistema o en tu IDE. Necesitarás definir `DB_URL` además de las listadas arriba (ej: `DB_URL=jdbc:postgresql://localhost:5432/telegrmdb`).
3.  **Ejecuta la aplicación** con Maven:
    ```sh
    mvn spring-boot:run
    ```

## 📖 Documentación de la API

Una vez que la aplicación está corriendo, puedes acceder a la documentación interactiva de la API (Swagger UI) en la siguiente URL:

**http://localhost:8080/swagger-ui.html**

Para probar los endpoints protegidos:
1.  Usa el endpoint `POST /auth/login` para obtener un token JWT.
2.  Haz clic en el botón "Authorize" en la parte superior derecha de Swagger UI.
3.  Pega tu token en el campo `Value` (ej: `Bearer tu-token-aqui`).

## ✅ Running Tests

Para ejecutar todos los tests unitarios y de integración (requiere Docker), usa el siguiente comando:
```sh
mvn clean verify
```

## ✨ Colecciones y documentación

* Colección Postman: `Telgrm BOT API.json`
* Documentación generada: `Telgrm BOT API-documentation.html`
 

Se incluye una coleccion para probar la API desarrollada en Bruno, que es una alternativa GNU a Postman pero con GIT lo que la hace ideal para equipos [<img src="https://fetch.usebruno.com/button.svg" alt="Fetch in Bruno" style="width: 130px; height: 30px;" width="128" height="32">](https://fetch.usebruno.com?url=https%3A%2F%2Fgithub.com%2Facamus79%2FTlgrmBot.git "target=_blank rel=noopener noreferrer")

<p align="center">
 <a href="https://github.com/usebruno/bruno" target="_blank"><img align="center" alt="Bruno" height="84" width="84" src="https://github.com/usebruno/bruno/raw/main/assets/images/logo-transparent.png"></a>
</p>


_____
<p align="center">
 <a href="https://acamus.is-a.dev" target="_blank"><img align="center" alt="Portfolio"  src="https://forthebadge.com/images/badges/built-with-love.svg"></a>
 <a href="#" target="_blank"><img align="center" alt="Daugther"  src="https://github.com/acamus79/StoreChallenge/assets/85143329/f5dda3bd-81cb-4ece-9a60-4c7ae215fa88"></a>
 <a href="#" target="_blank"><img align="center" alt="myMachine"  src="https://github.com/acamus79/StoreChallenge/assets/85143329/5f6c41aa-4209-44b4-8eb7-62f6163c23a7"></a>
</p>
