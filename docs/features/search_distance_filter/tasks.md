# Desglose de Tareas: Búsqueda Geográfica, Doble Ubicación, Histórico de 50 Puntos, Minimapa y FAQ

- **Plan técnico asociado**: `docs/features/search_distance_filter/plan.md`
- **Estado**: En Progreso
- **Módulos**: `:app` (`com.feryaeljustice.mirailink`) y `MiraiLink-Backend` (`Express 5 + PostgreSQL`)

- - -

## Fase 1: Backend Database & Endpoints (`MiraiLink-Backend`)

- [x] **Tarea 1.1**: Crear migración SQL para añadir campos de residencia (`residence_city`, `residence_region`, `residence_country_code`, `residence_latitude`, `residence_longitude`), ubicación activa (`current_latitude`, `current_longitude`, `last_location_updated_at`), preferencias de búsqueda en `users`, y crear la tabla `user_location_history` con regla de poda a 50 puntos.
- [x] **Tarea 1.2**: Actualizar DTOs en `src/dto/user.dto.js` con los nuevos campos de residencia, distancia relativa y ubicación.
- [x] **Tarea 1.3**: Actualizar `src/controllers/user.controller.js` con el endpoint de ping de ubicación (poda a 50 registros) y actualización de residencia y búsqueda.
- [x] **Tarea 1.4**: Actualizar `src/controllers/swipe.controller.js` para soportar filtro por residencia vs ubicación activa (modo viajeros), alcances (km, país, mundo, pasaporte) y ordenación ponderada.

- - -

## Fase 2: Capa de Dominio y Utilidades Geograficas (Android)

- [x] **Tarea 2.1**: Extender entidad `User.kt` con datos de residencia habitual y distancia calculada.
- [x] **Tarea 2.2**: Crear modelo `SearchPreferences.kt` con enum `SearchScope` (RADIUS, MY_COUNTRY, WORLD, SPECIFIC_COUNTRY), radio en km, switch de viajeros y flag de preparación Premium.
- [x] **Tarea 2.3**: Crear modelo `FaqItem.kt` para la sección de Preguntas Frecuentes.
- [x] **Tarea 2.4**: Implementar `GeoUtils.kt` con fórmula Haversine y añadir tests unitarios en `GeoUtilsTest.kt`.
- [x] **Tarea 2.5**: Definir contratos de repositorio `SearchPreferencesRepository.kt` y `FaqRepository.kt` con sus respectivos Casos de Uso.

- - -

## Fase 3: Capa de Datos y Persistencia (Android)

- [ ] **Tarea 3.1**: Actualizar `AppPrefs.kt` en `EncryptedDataStore` para almacenar `SearchPreferences`.
- [ ] **Tarea 3.2**: Implementar `SearchPreferencesRepositoryImpl.kt` y `FaqRepositoryImpl.kt`.
- [ ] **Tarea 3.3**: Actualizar Room `MiraiLinkDemoDatabase.kt` a versión 2 (`fallbackToDestructiveMigration`) añadiendo campos de residencia y ubicación en `DemoEntities.kt`.
- [ ] **Tarea 3.4**: Actualizar `DemoDataSeeder.kt` ubicando a Hikari en Palma de Mallorca y sembrando perfiles locales de Mallorca y viajeros en la zona.
- [ ] **Tarea 3.5**: Actualizar `DemoSwipeRepositoryImpl.kt` para aplicar el algoritmo de filtrado por residencia o viajeros según las preferencias activas.

- - -

## Fase 4: Capa de Presentación, Minimapa y FAQ (Android)

- [ ] **Tarea 4.1**: Crear componente Compose `SearchRadiusMinimap.kt` con teselas abiertas OpenStreetMap y circulo dinámico interactivo.
- [ ] **Tarea 4.2**: Crear componente `SearchSettingsSection.kt` en Ajustes con chips de alcance, Slider, Switch de viajeros, minimapa y botón explícito "Guardar Ajustes".
- [ ] **Tarea 4.3**: Crear pantalla `FaqScreen.kt` con acordeones Material 3 respondiendo sobre MiraiLink, la privacidad del histórico de 50 puntos y las búsquedas.
- [ ] **Tarea 4.4**: Actualizar `SettingsViewModel.kt` y `SettingsScreen.kt` para coordinar el guardado explícito y la navegación a FAQ.
- [ ] **Tarea 4.5**: Actualizar `UserCard.kt` para mostrar el lugar de residencia ("Palma de Mallorca • A 8 km") y badge sutil para viajeros.
- [ ] **Tarea 4.6**: Actualizar `HomeScreen.kt` con estado vacío elegante si no hay ubicación ni permisos concedidos.
- [ ] **Tarea 4.7**: Registrar todos los nuevos componentes en los módulos de Koin (`di/koin/`).

- - -

## Fase 5: Verificación, Tests y Build

- [ ] **Tarea 5.1**: Ejecutar suite de pruebas unitarias (`./gradlew.bat testDebugUnitTest`).
- [ ] **Tarea 5.2**: Compilar APK debug (`./gradlew.bat assembleDebug`).
- [ ] **Tarea 5.3**: Generar walkthrough detallado de la funcionalidad.
