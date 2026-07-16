# Codex context for MiraiLink

Este archivo es el punto de entrada específico para Codex. El contexto técnico común a cualquier asistente está en [`docs/ai/README.md`](docs/ai/README.md).

## Orden de lectura

1. Lee `AGENTS.md` para las reglas generales del repositorio.
2. Lee `docs/ai/README.md` y el documento especializado que corresponda a la tarea.
3. Comprueba el código afectado antes de confiar en documentos históricos.
4. Consulta `docs/ai/security-and-risks.md` antes de cambiar autenticación, red, almacenamiento, Firebase, permisos o firma.

## Correcciones importantes al contexto antiguo

- La inyección de dependencias real es Koin, no Hilt. No introduzcas Hilt salvo que se solicite una migración completa.
- El proyecto tiene un único módulo Gradle, `:app`. Las carpetas `data`, `domain` y `ui` son paquetes dentro de ese módulo, no módulos aislados.
- La configuración actual es compile SDK 37, target SDK 37, min SDK 26, AGP 9.3.0, Kotlin 2.4.10, Gradle 9.6.1 y Java 17.
- La arquitectura es una separación por capas pragmática, no Clean Architecture estricta. Existen dependencias de `domain` hacia Android, Retrofit, Firebase y tipos de UI.
- Socket.IO está implementado y registrado, pero el chat visible obtiene mensajes por REST cada 3 segundos. No describas el chat actual como tiempo real sin resolver esa diferencia.
- Los deep links están declarados en el manifest, pero no existe procesamiento de `Intent.data` que los convierta en destinos de Navigation 3.

## Guardas de trabajo

- No leas, copies ni publiques valores de `keystore.properties`, `app/google-services.json`, keystores, tokens o credenciales de prueba.
- No añadas nuevos secretos al repositorio. Usa configuración local o un almacén seguro.
- Mantén los contratos en `domain/repository`, las implementaciones en `data/repository` y el cableado en `di/koin` cuando trabajes dentro del diseño existente.
- Para UI, conserva Compose Material 3, Navigation 3 y los componentes existentes en `ui/components`.
- Para estado asíncrono, usa `StateFlow`, `viewModelScope`, dispatchers inyectados y el contrato de [`docs/ai/error-handling.md`](docs/ai/error-handling.md). `MiraiLinkResult.Error` solo acepta `AppError`.
- Revisa rutas API, DTO, datasource, repositorio, caso de uso, ViewModel y prueba relacionada cuando cambie un contrato de backend.
- No ejecutes `connectedDebugAndroidTest` sin revisar primero si las pruebas usarán el backend real. `AppE2ETest` inicia sesión y realiza acciones con datos remotos.

## Validación mínima

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat testDebugUnitTest
.\gradlew.bat lintDebug
```

Estado verificado el 2026-07-16:

- `assembleDebug`: correcto.
- `testDebugUnitTest`: correcto, 283 tests.
- `lintDebug`: 1 error y 103 advertencias. El error preexistente es `CredManMissingDal`.


La rama codex/comprehensive-testing redacta 325 tests JVM y 58 instrumentados. No se han compilado ni ejecutado por instrucción expresa; consulta docs/ai/testing/README.md antes de modificarlos.
Los detalles y los comandos adicionales están en [`docs/ai/development-and-testing.md`](docs/ai/development-and-testing.md).
