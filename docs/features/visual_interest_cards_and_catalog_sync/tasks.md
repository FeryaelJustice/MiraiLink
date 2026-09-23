# Checklist de Tareas: Sincronizacion de Catalogo y Tarjetas Visuales de Intereses

- [x] **Fase 1: Backend - Clientes de APIs Externas y Servicio de Sincronizacion Idempotente**
  - [x] **Tarea 1.1**: Implementar cliente de RAWG (`src/services/rawgService.js`) con gestion de clave API, peticion de juegos populares y mapeo a estructura interna.
  - [x] **Tarea 1.2**: Implementar cliente de Jikan (`src/services/jikanService.js`) con gestion de rate limit, peticion de animes populares y mapeo a estructura interna.
  - [x] **Tarea 1.3**: Implementar servicio de sincronizacion (`src/services/catalogSyncService.js`) con logica idempotente, preservando IDs de usuarios y catalogos canonicos existentes.
  - [x] **Tarea 1.4**: Crear script CLI `scripts/sync-catalog.js` y registrar comando `npm run sync:catalog` en `package.json`.
  - [x] **Tarea 1.5**: Configurar temporizador periodico en `src/server.js` / `src/app.js` controlado por `CATALOG_SYNC_INTERVAL_HOURS`.
  - [x] **Tarea 1.6**: Escribir tests unitarios en backend (`tests/catalogSyncService.test.js`) y verificar con `npm test`.

- [x] **Fase 2: Android - Utilidades de Fallback y Componentes Visuales de Tarjeta**
  - [x] **Tarea 2.1**: Implementar utilidad `InterestImageFallback.kt` con resolucion en cascada (recurso `goku` -> `logomirailink`).
  - [x] **Tarea 2.2**: Implementar componente `InterestCard.kt` con Coil `AsyncImage`, esquinas redondeadas, degradado vertical inferior sombreado y nombre localizado legible.
  - [x] **Tarea 2.3**: Implementar componente `InterestsGrid.kt` con cuadricula de 3 columnas compactas adaptable y soporte para estados vacios.
  - [x] **Tarea 2.4**: Escribir tests unitarios en `app/src/test/` para `InterestImageFallback`.

- [x] **Fase 3: Android - Selector Visual y Renovacion de Tarjetas de Perfil**
  - [x] **Tarea 3.1**: Implementar componente `VisualInterestPickerModal.kt` (`ModalBottomSheet`) con buscador en tiempo real, `imePadding()`, miniaturas cuadradas a la izquierda y checkboxes.
  - [x] **Tarea 3.2**: Actualizar `UserCard.kt` para integrar la apertura del modal y la cuadricula de tarjetas visuales en edicion y lectura.
  - [x] **Tarea 3.3**: Actualizar `PublicUserCard.kt` para mostrar la cuadricula de tarjetas visuales en la tarjeta publica de descubrimiento.
  - [x] **Tarea 3.4**: Actualizar `DemoDataSeeder.kt` para que el modo offline sandbox disponga de imagenes validas en animes y videojuegos.

- [x] **Fase 4: Verificacion Final y Calidad**
  - [x] **Tarea 4.1**: Ejecutar suite completa de tests de Android (`./gradlew.bat testDebugUnitTest`).
  - [x] **Tarea 4.2**: Ejecutar suite completa de tests de Backend (`npm test`).
  - [x] **Tarea 4.3**: Compilar aplicacion Android limpia (`./gradlew.bat assembleDebug`).
  - [x] **Tarea 4.4**: Verificar ausencia de commits no autorizados en ambas ramas `feature/visual-interest-catalog-sync`.
