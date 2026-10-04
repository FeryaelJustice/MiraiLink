# Checklist de Tareas: Sistema de Deshacer en Discovery e Integridad Temporal

- [x] **Fase 1: Backend - Base de Datos PostgreSQL y Migraciones**
  - [x] Crear migracion `src/database/migrations/010_timezone_and_swipe_undo_quota.sql` para convertir todas las columnas de tiempo a `TIMESTAMPTZ` y crear la tabla `user_swipe_undos`.
  - [x] Actualizar el esquema maestro `src/database/db.sql` con `TIMESTAMPTZ` y la definicion de `user_swipe_undos`.
  - [x] Verificar migracion limpia de base de datos ejecutando migraciones locales.

- [x] **Fase 2: Backend - Logica de Negocio, Endpoints y Tests**
  - [x] Definir constantes de cuota `UNDO_DAILY_LIMITS` (Free: 1, Plus: 3, Premium: 6) en `src/consts/subscriptionConsts.js`.
  - [x] Implementar `getUndoQuota` en `src/controllers/swipe.controller.js` calculando cuota consumida y verificando si existen swipes reversibles.
  - [x] Implementar `undoSwipe` en `src/controllers/swipe.controller.js` con soporte para sesiones anteriores, reversion atomica de likes/matches/dislikes, auditoria en `user_swipe_undos` y devolucion del DTO del perfil deshecho.
  - [x] Registrar rutas `POST /undo` y `GET /undo-quota` en `src/routes/swipe.routes.js`.
  - [x] Escribir y pasar suite de tests con Vitest y validacion de OpenAPI contract.

- [x] **Fase 3: Android - Dependencias e Integridad Temporal (TrustedTime & Interceptor)**
  - [x] Agregar `play-services-time:16.0.1` en `gradle/libs.versions.toml` y `app/build.gradle.kts`.
  - [x] Implementar `TrustedTimeProvider` con `TrustedTimeClient` de Google Play Services y fallback de reloj monotonico de hardware.
  - [x] Implementar `ServerTimeInterceptor` en OkHttp para sincronizar la cabecera HTTP `Date` de las respuestas del backend con `TrustedTimeProvider`.
  - [x] Registrar `TrustedTimeProvider` y `ServerTimeInterceptor` en los modulos Koin de la aplicacion.
  - [x] Escribir tests unitarios para `TrustedTimeProvider`.

- [x] **Fase 4: Android - Capa de Datos, Dominio y Modo Demo**
  - [x] Crear modelos de dominio y DTOs (`UndoQuota`, `UndoSwipeResult`, DTOs de peticion y respuesta).
  - [x] Actualizar `SwipeApiService` con las llamadas Retrofit a `/api/swipe/undo` y `/api/swipe/undo-quota`.
  - [x] Actualizar `MiraiLinkDemoDatabase`, DAOs y `DemoSwipeHistoryEntity` para persistir el historial de swipes en Room entre reinicios en Modo Demo.
  - [x] Implementar metodos `undoSwipe` y `getUndoQuota` en `SwipeRepositoryImpl`, `DelegatingSwipeRepository` y `DemoSwipeRepositoryImpl`.
  - [x] Crear casos de uso `UndoSwipeUseCase` y `GetUndoQuotaUseCase` en la capa de dominio.
  - [x] Escribir tests unitarios para los casos de uso de rebobinado.

- [x] **Fase 5: Android - Capa de Presentacion (UI) y Flujo de Paywall**
  - [x] Actualizar `HomeViewModel`: exponer `undoQuota: StateFlow<UndoQuota?>`, cargar cuota al inicio, implementar `undoSwipe()` reactivo y emitir navegacion al Paywall si la cuota es 0 o se recibe rechazo.
  - [x] Actualizar `HomeScreen.kt` y `UserSwipeCardStack.kt` para mantener el boton `returnSwipeBtn` operativo ante swipes de la sesion o de sesiones anteriores.
  - [x] Asegurar que el rebobinado consecutivo funcione hasta agotar la cuota disponible.
  - [x] Escribir tests unitarios para `HomeViewModel` verificando el flujo de deshacer y la redireccion al Paywall.

- [x] **Fase 6: Verificacion Final y Documentacion de Despliegue**
  - [x] Ejecutar tests de backend: `npm test` y comprobacion de rutas `npm run check:routes`.
  - [x] Ejecutar suite de pruebas unitarias de Android: `.\gradlew.bat testDebugUnitTest`.
  - [x] Compilar APK de debug de Android: `.\gradlew.bat assembleDebug`.
  - [x] Detallar los comandos exactos para el servidor de produccion (migracion de base de datos y despliegue).
