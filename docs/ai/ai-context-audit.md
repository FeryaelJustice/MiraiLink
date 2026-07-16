# Auditoría del contexto AI

## Archivos revisados

| Archivo | Líneas | Estado |
| --- | ---: | --- |
| `README.md` | 85 | Útil, pero parcialmente obsoleto |
| `AGENTS.md` | 197 | Amplio, con una contradicción crítica sobre DI |
| `CLAUDE.md` | 64 | Resumen genérico y duplicado |
| `GEMINI.md` | 74 | Resumen genérico y parcialmente obsoleto |
| `WARP.md` | 170 | Contexto extenso, duplicado y con versiones antiguas |
| `.cursor/worktrees.json` | JSON | Configuración de worktrees, no contexto técnico |

No existían `CODEX.md`, `docs/` ni `.codex/context-health/check.json` al comenzar.

## Hallazgos

### Contradicciones factuales

| Tema | Documento antiguo | Código actual |
| --- | --- | --- |
| DI | AGENTS indica Hilt y anotaciones Hilt | Koin en 15 módulos, KSP annotations y Koin Test |
| SDK | Target 36 | Compile y target 37 |
| Kotlin | 2.2 o 2.2.20 | 2.4.10 |
| AGP | 8.13.0 | 9.3.0 |
| Gradle | No reflejado con precisión | 9.6.1 |
| Chat | Tiempo real Socket.IO | UI actual por polling REST, socket sin call sites |
| Tests | Testing al completo | 283 unit tests pasan; lint mantiene un error DAL preexistente |
| Arquitectura | Clean Architecture estricta | Dependencias cruzadas entre domain, data y UI |

### Duplicación

AGENTS, CLAUDE, GEMINI y WARP repiten descripción, flujo de capas, comandos, seguridad y tareas comunes. Al divergir, una corrección debe aplicarse en varios sitios y ya se observa drift.

### Información que faltaba

- Inventario verificable y conteos.
- Flujo de arranque real.
- Grafo de Koin y qualifiers.
- Estado central de navegación y sesión.
- Lista completa de endpoints.
- Diferencia entre Socket.IO y polling.
- Remote Config sin inicialización.
- Estado real de build, tests y lint.
- Riesgos de App Check, tokens, cleartext y material de firma.
- Limitación de deep links y notificaciones.

## Decisión de documentación

Para respetar la petición de conservar los archivos existentes:

- No se reemplazó `README.md`.
- No se reescribieron AGENTS, CLAUDE, GEMINI ni WARP.
- `docs/ai/README.md` pasa a ser la fuente técnica común y verificable.
- `CODEX.md` es un índice corto específico para Codex.
- `.codex/context-health/check.json` permite detectar cambios en los archivos AI revisados.

Esto mejora el contexto sin resolver por completo la duplicación histórica. La siguiente limpieza debería convertir los archivos específicos de cada agente en stubs cortos que apunten a `docs/ai`, pero ese cambio debe hacerse de forma coordinada para no eliminar instrucciones que otros entornos todavía carguen.

## Evaluación del README

El README sigue siendo una buena introducción de producto y contiene comandos útiles. Debe actualizarse en una tarea separada para:

- Sincronizar SDK, AGP, Kotlin, Compose BOM y toolchain.
- Aclarar que el backend no está en este repositorio.
- Sustituir afirmaciones de testing completo por el estado verificable.
- Explicar que la URL del backend está fijada en `NetworkModule`.
- Describir el chat actual como polling y Socket.IO como integración pendiente de uso.
- Enlazar a `docs/ai` para arquitectura, integraciones y riesgos.

## Mantenimiento

Actualiza los documentos y el cache cuando cambie cualquiera de estos puntos:

- Plugins, SDK, Kotlin, Gradle o dependencias principales.
- Módulos o estructura de paquetes.
- Rutas Retrofit, base URL o eventos Socket.IO.
- Flujo de sesión, navegación o deep links.
- Persistencia y cifrado.
- Firebase, Ads, permisos o firma.
- Resultado base de build, tests o lint.

Una futura auditoría puede reutilizar `.codex/context-health/check.json` si los hashes de los archivos revisados siguen coincidiendo.
