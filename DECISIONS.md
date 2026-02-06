# Registro de Decisiones de Arquitectura (ADR)

Este documento registra las decisiones de diseño y arquitectura tomadas durante el desarrollo del proyecto, justificando los trade-offs considerados.

## 1. Uso de Objetos Command en Puertos de Entrada
**Fecha:** 2023-10-27
**Contexto:**
Los puertos de entrada (Input Ports) definen la interfaz que los adaptadores externos (como controladores REST) utilizan para interactuar con la lógica de negocio. Inicialmente, se pasaban múltiples parámetros primitivos (`String name`, `String email`, etc.) directamente en los métodos de la interfaz.

**Decisión:**
Se decidió encapsular los parámetros de entrada en objetos `Command` (ej. `RegisterUserCommand`, `AuthenticateUserCommand`) implementados como Java Records.

**Justificación:**
*   **Encapsulamiento:** Agrupa datos relacionados lógicamente, reduciendo la cantidad de argumentos en los métodos.
*   **Evolución:** Permite agregar nuevos campos sin romper la firma del método en la interfaz, facilitando la refactorización y manteniendo la compatibilidad hacia atrás.
*   **Validación Temprana:** El constructor del `record` permite validar la integridad básica de los datos (nulos, vacíos) antes de que lleguen al caso de uso.
*   **Inmutabilidad:** Los Records son inmutables por defecto, lo que es ideal para DTOs de entrada, previniendo modificaciones accidentales.

## 2. Eliminación de Dependencias de Framework en la Capa de Aplicación
**Fecha:** 2023-10-27
**Contexto:**
Inicialmente, las clases de casos de uso (ej. `RegisterUserUseCase`) estaban anotadas con `@Service` y `@Transactional` de Spring Framework.

**Decisión:**
Se eliminaron todas las anotaciones de Spring de las clases dentro del paquete `application` y `core`.

**Justificación:**
*   **Independencia del Framework:** Cumple estrictamente con la Arquitectura Hexagonal, donde el núcleo (Dominio y Aplicación) no debe depender de detalles de infraestructura o frameworks externos. Esto protege la lógica de negocio de cambios tecnológicos.
*   **Testeabilidad:** Facilita las pruebas unitarias puras sin necesidad de levantar un contexto de Spring, lo que acelera la ejecución de los tests.
*   **Configuración Explicita:** La inyección de dependencias y la gestión de transacciones se delegan a clases de configuración (`@Configuration`) en la capa de infraestructura (`BeanConfiguration`), centralizando el ensamblaje de la aplicación.

## 3. Eliminación de Lombok en el Core (Dominio y Aplicación)
**Fecha:** 2023-10-27
**Contexto:**
Se utilizaba la librería Lombok para reducir el código repetitivo (boilerplate) en clases del dominio y casos de uso.

**Decisión:**
Se decidió eliminar el uso de Lombok en los paquetes `core` y `application`, optando por código Java estándar (constructores manuales, getters).

**Justificación:**
*   **Pureza Arquitectónica:** Se elimina una dependencia externa del núcleo de la aplicación.
*   **Explicitud:** Los constructores y métodos son visibles y explícitos, lo que facilita la comprensión del flujo de datos y la inyección de dependencias sin "magia" de compilación.
*   **Estabilidad:** Se evita el acoplamiento a una herramienta de construcción específica.

## 4. Delegación de Generación de Tokens a Infraestructura
**Fecha:** 2023-10-27
**Contexto:**
El caso de uso de autenticación necesita devolver un token de acceso (JWT). Existía la opción de generar el token dentro del caso de uso o delegarlo.

**Decisión:**
Se definió un puerto de salida `TokenGeneratorPort` en el `core`, cuya implementación (`JwtTokenProvider`) reside en la capa de `infrastructure`.

**Justificación:**
*   **Desacoplamiento:** El dominio no conoce qué tipo de token se genera (JWT, Opaque, etc.) ni depende de librerías de terceros como `jjwt`.
*   **Flexibilidad:** Permite cambiar la estrategia de autenticación (ej. cambiar de JWT a PASETO o Session IDs) cambiando solo el adaptador de infraestructura, sin tocar la lógica de negocio.

## 5. Autenticación Stateless con Filtro JWT
**Fecha:** 2023-10-27
**Contexto:**
Se requiera un mecanismo para asegurar los endpoints de la API.

**Decisión:**
Se implementó una arquitectura de seguridad Stateless utilizando un filtro personalizado (`JwtAuthenticationFilter`) que valida un token JWT en cada petición, en lugar de usar sesiones de servidor.

**Justificación:**
*   **Escalabilidad:** Al no guardar estado de sesión en el servidor, la aplicación puede escalar horizontalmente sin necesidad de replicación de sesiones o sticky sessions.
*   **Estándar REST:** Se alinea con los principios REST de no mantener estado entre peticiones.

## 6. Mapeo Manual en Adaptadores de Persistencia
**Fecha:** 2023-10-27
**Contexto:**
Es necesario convertir entre las Entidades de Dominio (`User`) y las Entidades JPA (`UserEntity`).

**Decisión:**
Se optó por implementar métodos de mapeo manual (`toDomain`, `fromDomain`) en lugar de utilizar librerías de mapeo automático como MapStruct o ModelMapper.

**Justificación:**
*   **Simplicidad:** Para el volumen actual de entidades, el mapeo manual es directo, fácil de leer y depurar.
*   **Control:** Permite un control total sobre cómo se construyen los objetos de dominio, asegurando que siempre se respeten las reglas de validación de los constructores y Value Objects.
*   **Menos Dependencias:** Evita añadir complejidad de configuración y dependencias adicionales al proyecto.

## 7. Gestión de Transacciones con el Patrón Decorator
**Fecha:** 2023-10-27
**Contexto:**
Los casos de uso que modifican la base de datos deben ejecutarse dentro de una transacción atómica. La opción más simple era anotar el caso de uso con `@Transactional` de Spring.

**Decisión:**
Se optó por una solución más pura arquitectónicamente:
1.  Los casos de uso (`ProcessTelegramUpdateUseCase`, `SendMessageUseCase`) se mantienen libres de anotaciones de framework.
2.  Se crearon decoradores (`Transactional...`) en la capa de **`infrastructure`** que envuelven al caso de uso y añaden `@Transactional`.
3.  Mediante configuración de beans (`@Primary`, `@Qualifier`), se inyecta el decorador transaccional en los adaptadores de entrada.

**Justificación:**
*   **Máxima Pureza Arquitectónica:** El caso de uso principal permanece 100% agnóstico al framework, cumpliendo de forma estricta el principio de independencia del núcleo.
*   **Principio de Responsabilidad Única (SRP):** El caso de uso se enfoca exclusivamente en la lógica de negocio, mientras que el decorador se enfoca exclusivamente en la gestión de la transacción.
*   **Flexibilidad y Testeabilidad:** Permite testear la lógica del caso de uso sin necesidad de un contexto transaccional, y testear la transaccionalidad de forma separada si fuera necesario.

## 8. Uso de OpenRouter como Gateway de IA
**Fecha:** 2023-10-27
**Contexto:**
Se requería integrar un modelo de lenguaje (LLM) para generar respuestas. Las opciones directas (OpenAI, Gemini) presentaban barreras de entrada (costo, disponibilidad de capa gratuita, complejidad de API).

**Decisión:**
Se optó por utilizar **OpenRouter** como intermediario.

**Justificación:**
*   **Flexibilidad:** Permite cambiar entre múltiples modelos (Deepseek, Llama, Gemini, GPT) cambiando solo la configuración, sin tocar el código.
*   **Compatibilidad:** Ofrece una API compatible con OpenAI, lo que estandariza la integración.
*   **Acceso:** Facilita el acceso a modelos gratuitos y de código abierto para desarrollo y pruebas.

## 9. Gestión de Configuración y Secretos con Docker y .env
**Fecha:** 2023-10-27
**Contexto:**
La aplicación requiere múltiples credenciales (BD, Telegram, IA, JWT) que no deben estar en el código fuente.

**Decisión:**
Se implementó una estrategia basada en variables de entorno inyectadas mediante un archivo `.env` no versionado y orquestadas por Docker Compose.

**Justificación:**
*   **Seguridad:** Los secretos nunca se commitean al repositorio.
*   **Portabilidad:** El mismo contenedor Docker funciona en cualquier entorno simplemente cambiando el archivo `.env`.
*   **Simplicidad:** `docker-compose` centraliza la inyección de variables tanto para la aplicación como para la base de datos.
