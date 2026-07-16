# Contexto técnico AI de MiraiLink

Esta carpeta es el contexto técnico canónico del cliente Android para Codex, Claude, Gemini y otros asistentes. Se creó a partir del código y la configuración versionados en `47a073e`, con validación local el 2026-07-15. El `README.md` original y los archivos de otros asistentes se conservaron sin reemplazarlos.

## Qué es el proyecto

MiraiLink es un cliente Android social y de citas orientado a fans de anime y videojuegos. Incluye registro y acceso, recuperación y verificación de cuenta, 2FA, feed de perfiles, like y dislike, matches, mensajería, edición de perfil y fotos, notificaciones FCM, analítica, anuncios, configuración remota y un chat con Gemini.

El repositorio contiene solo el cliente Android. No contiene el backend, su base de datos, la configuración del servidor ni su despliegue. Los detalles del backend que aparecen en estos documentos se deducen exclusivamente de los contratos Retrofit y Socket.IO.

## Ficha rápida verificada

| Dato | Valor actual |
| --- | --- |
| Módulos Gradle | `:app` |
| Package y application ID | `com.feryaeljustice.mirailink` |
| UI | Jetpack Compose y Material 3 |
| Navegación | Navigation 3 con claves serializables y pilas por subgrafo |
| DI | Koin 4.2.2 con KSP annotations |
| Capas | `data`, `domain`, `ui`, más `core`, `state` y `di/koin` |
| Persistencia local | DataStore JSON cifrado con AES-GCM y Android Keystore |
| Red | Retrofit 3, OkHttp 5 y Kotlinx Serialization |
| Mensajería | REST por sondeo en la UI actual; Socket.IO cableado pero no consumido |
| Servicios Google | Firebase Analytics, Crashlytics, Messaging, Remote Config, AI y App Check |
| Monetización | AdMob y UMP |
| SDK | min 26, compile 37, target 37 |
| Toolchain | Java 17, Kotlin 2.4.10, AGP 9.3.0, Gradle 9.6.1 |
| Versión de app | 2.3.0, version code 33 |

## Documentos

- [`architecture.md`](architecture.md): arquitectura real, arranque, navegación, estado, DI y límites de capas.
- [`codebase-map.md`](codebase-map.md): inventario del repositorio, paquetes, pantallas, endpoints, recursos y pruebas.
- [`runtime-and-integrations.md`](runtime-and-integrations.md): backend, Socket.IO, Firebase, Gemini, FCM, anuncios, credenciales y deep links.
- [`development-and-testing.md`](development-and-testing.md): configuración local, comandos, estrategia de pruebas y estado de validación.
- [testing/README.md](testing/README.md): estrategia completa, infraestructura compartida, Compose aislado, exclusiones y fuentes oficiales.
- [`error-handling.md`](error-handling.md): contrato tipado, taxonomía, clasificación, métodos, recursos, reintentos y guía de extensión.
- [`security-and-risks.md`](security-and-risks.md): riesgos priorizados, deuda técnica y hallazgos de lint.
- [`ai-context-audit.md`](ai-context-audit.md): evaluación del README y de los archivos de contexto AI existentes.
- [`/CODEX.md`](../../CODEX.md): instrucciones de entrada específicas para Codex.

## Lectura recomendada por tarea

| Tarea | Leer primero |
| --- | --- |
| Orientación general | Este archivo y `codebase-map.md` |
| Nueva funcionalidad | `architecture.md` y el paquete análogo más cercano |
| API, autenticación o errores | `error-handling.md`, `runtime-and-integrations.md` y `security-and-risks.md` |
| UI o navegación | `architecture.md`, sección de navegación, y `ui/navigation` |
| Chat | `runtime-and-integrations.md`, sección de mensajería |
| Firebase, Ads o consentimiento | `runtime-and-integrations.md` y `security-and-risks.md` |
| Build o pruebas | `development-and-testing.md` y `testing/README.md` |
| Actualizar documentación AI | `ai-context-audit.md` |

## Estado de salud resumido

- El APK debug se ensambla correctamente.
- Los 283 tests unitarios pasan tras migrar Splash y el tratamiento tipado de errores.
- La rama de testing amplía el árbol a 325 tests JVM y 58 instrumentados, pero estas adiciones no se han compilado ni ejecutado por instrucción expresa del propietario.
- Lint falla con un error preexistente de Credential Manager y reporta 103 advertencias; el sistema de errores nuevo no añade incidencias.
- Hay riesgos de seguridad que deben tratarse antes de una publicación: materiales sensibles versionados, App Check de debug instalado sin condición, tokens en logs y tráfico claro permitido globalmente.
- La documentación anterior contiene versiones obsoletas y una instrucción incorrecta para usar Hilt.

Este conjunto documenta el estado observado. No certifica que la app esté lista para producción.
