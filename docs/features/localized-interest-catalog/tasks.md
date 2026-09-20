# Checklist de tareas: Catalogo localizado de animes y juegos

- [ ] **Fase 1: Migración y semillas de backend**
  - [x] Crear ejecutor incremental `db:migrate` y tabla de control de migraciones.
  - [x] Crear migración PostgreSQL para idiomas, catálogos separados, traducciones y migración conservadora de datos existentes.
  - [x] Actualizar esquema de creación, inserts y seed idempotente sin reset implícito.
  - [x] Añadir pruebas de localización y seed, ejecutar lint y tests backend pertinentes.

- [ ] **Fase 2: API, contratos y privacidad**
  - [x] Añadir resolución `Accept-Language`, fallback `es` y URL pública de imagen.
  - [x] Reformar catálogo, intereses y validación por UUID.
  - [x] Separar DTO de propietario y proyecciones públicas sin `username` ni datos sensibles.
  - [x] Actualizar pruebas HTTP, OpenAPI y documentación backend.

- [ ] **Fase 3: Modelos Android y demo local**
  - [x] Actualizar DTOs, dominio, mappers y servicio Retrofit para catálogo localizado.
  - [x] Mantener Room demo y sus seeds serializados por ID, compatible con los nuevos campos opcionales.
  - [x] Actualizar repositorios, ViewModel y selector para selección por ID.
  - [ ] Ejecutar pruebas unitarias y de Room pertinentes. Bloqueado por errores de compilación preexistentes en `DemoModeManagerTest` y `SettingsViewModelTest`.

- [ ] **Fase 4: Jerarquía de perfil Android**
  - [x] Reorganizar UserCard y edición en Información básica, Residencia y Gustos.
  - [x] Mantener metadatos de catálogo fuera de la UI y conservar accesibilidad y estado.
  - [ ] Añadir o actualizar pruebas visuales y de ViewModel. La comprobación visual queda para dispositivo o emulador, por decisión del usuario.

- [ ] **Fase 5: Verificación y operación**
  - [ ] Ejecutar `npm run lint`, pruebas backend y `./gradlew.bat assembleDebug`, `testDebugUnitTest`, `lintDebug`. Lint y 37 pruebas backend, contrato OpenAPI y ensamblado debug pasan. Las pruebas JVM y lint Android están bloqueados por fallos preexistentes de tests y de resolución de dependencias.
  - [ ] Verificar manualmente en emulador o dispositivo si hay uno disponible.
  - [ ] Documentar el orden de despliegue exacto, sin commits ni PR.
