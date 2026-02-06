# Notas sobre el Uso de IA en el Desarrollo

Este documento describe cómo se utilizó la Inteligencia Artificial (específicamente, un asistente de chat basado en un LLM) durante el desarrollo de este proyecto, cumpliendo con los criterios de evaluación del ejercicio.

## 1. Estrategia de Prompting y Colaboración

La estrategia principal fue tratar al asistente de IA como un **Arquitecto Consultor o un Desarrollador Junior Experto**. No se utilizó para generar código a ciegas, sino como una herramienta de "rubber ducking" y aceleración, siguiendo un flujo de trabajo de **análisis, propuesta y corrección**.

-   **Análisis:** Se le presentaba una descripcion del Ejercicio Backend, y se le pedía que propusiera un plan de implementación.
-   **Propuesta:** El asistente generaba una propuesta de código (DTOs, Controladores, Casos de Uso, etc.).
-   **Corrección y Refinamiento:** Como desarrollador senior, mi rol fue revisar críticamente cada propuesta, identificar fallos arquitectónicos, inconsistencias o malas prácticas, y guiar al asistente para corregirlos.

## 2. Ejemplos Concretos de Interacción

### Ejemplo 1: Violación de Arquitectura y Corrección

-   **Situación:** En la primera implementación del `ProcessTelegramUpdateUseCase`, el asistente propuso que el caso de uso recibiera directamente el DTO de la API de Telegram (`Update.java`), que residía en la capa de `infrastructure`.
-   **Mi Intervención:** Detecté que esto era una **violación crítica** de la Arquitectura Hexagonal (la capa de aplicación no debe depender de la infraestructura).
-   **Instrucción al Asistente:** Le pedí que corrigiera el error desacoplando el caso de uso. Le instruí para que creara un `Command` (`ProcessUpdateCommand`) en el `core` y que el adaptador de entrada (`TelegramPollingService`) fuera el responsable de convertir el DTO de infraestructura al `Command` del `core`.
-   **Resultado:** El asistente entendió la corrección y refactorizó el código, resultando en una arquitectura limpia y correcta.

### Ejemplo 2: Debate sobre Gestión de Transacciones

-   **Situación:** Se necesitaba que el `ProcessTelegramUpdateUseCase` fuera transaccional. La primera propuesta del asistente fue añadir la anotación `@Transactional` directamente en la clase del caso de uso.
-   **Mi Intervención:** Cuestioné esta decisión, argumentando que introducía un acoplamiento (aunque pragmático) con Spring en el núcleo de la aplicación.
-   **Instrucción al Asistente:** Le pedí que explorara una solución más pura. El asistente propuso usar el **Patrón Decorator**, creando una clase `TransactionalProcessTelegramUpdateUseCase` que envolviera al caso de uso real y le añadiera la transaccionalidad.
-   **Resultado:** Tras debatir los pros y contras, decidí adoptar la solución del Decorator por su pureza arquitectónica. Le pedí al asistente que implementara el decorador y que documentara esta decisión en `DECISIONS.MD`.

### Ejemplo 3: Integración de APIs Externas (IA)

-   **Situación:** Se necesitaba reemplazar la respuesta aleatoria por una llamada a una API de IA.
-   **Mi Intervención:** Le pedí al asistente que diseñara la integración, pero siguiendo la arquitectura hexagonal.
-   **Instrucción al Asistente:** Le instruí para que creara un nuevo puerto de salida (`AiGeneratorPort`) y un adaptador (`OpenRouterAdapter`). Durante la implementación, el asistente cometió errores (como olvidar el `@Qualifier` para `RestClient`, causando ambigüedad de beans), los cuales identifiqué y le pedí que corrigiera.
-   **Resultado:** El proceso iterativo de prueba y error, guiado por mi supervisión, resultó en una integración robusta, configurable y desacoplada.

## 3. Conclusión

La IA fue una herramienta extremadamente útil para generar "boilerplate" y proponer estructuras iniciales. Sin embargo, la supervisión y el conocimiento de un desarrollador senior fueron cruciales para:

-   Garantizar el cumplimiento de los principios arquitectónicos.
-   Identificar y corregir errores sutiles.
-   Tomar decisiones de diseño informadas.
-   Refinar el código para mejorar su calidad y robustez.

El proyecto demuestra una colaboración efectiva entre un desarrollador y una herramienta de IA, donde la IA acelera y el humano dirige y asegura la calidad.
