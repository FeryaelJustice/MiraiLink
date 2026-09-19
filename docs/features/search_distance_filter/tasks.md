# Desglose de Tareas: Busqueda Geografica, Doble Ubicacion, Historico de 50 Puntos, Minimapa y FAQ

- **Plan tecnico asociado**: `docs/features/search_distance_filter/plan.md`
- **Estado**: En Progreso
- **Modulos**: `:app` (`com.feryaeljustice.mirailink`) y `MiraiLink-Backend` (`Express 5 + PostgreSQL`)

- - -

## Fase 1: Backend Database & Endpoints (`MiraiLink-Backend`)

- [x] **Tarea 1.1**: Crear migracion SQL para anadir campos de residencia (`residence_city`, `residence_region`, `residence_country_code`, `residence_latitude`, `residence_longitude`), ubicacion activa (`current_latitude`, `current_longitude`, `last_location_updated_at`), preferencias de busqueda en `users`, y crear la tabla `user_location_history` con regla de poda a 50 puntos.
- [x] **Tarea 1.2**: Actualizar DTOs en `src/dto/user.dto.js` con los nuevos campos de residencia, distancia relativa y ubicacion.
- [x] **Tarea 1.3**: Actualizar `src/controllers/user.controller.js` con el endpoint de ping de ubicacion (poda a 50 registros) y actualizacion de residencia y busqueda.
- [x] **Tarea 1.4**: Actualizar `src/controllers/swipe.controller.js` para soportar filtro por residencia vs ubicacion activa (modo viajeros), alcances (km, pais, mundo, pasaporte) y ordenacion ponderada.

- - -

## Fase 2: Capa de Dominio y Utilidades Geograficas (Android)

- [x] **Tarea 2.1**: Extender entidad `User.kt` con datos de residencia habitual y distancia calculada.
- [x] **Tarea 2.2**: Crear modelo `SearchPreferences.kt` con enum `SearchScope` (RADIUS, MY_COUNTRY, WORLD, SPECIFIC_COUNTRY), radio en km, switch de viajeros y flag de preparacion Premium.
- [x] **Tarea 2.3**: Crear modelo `FaqItem.kt` para la seccion de Preguntas Frecuentes.
- [x] **Tarea 2.4**: Implementar `GeoUtils.kt` con formula Haversine y anadir tests unitarios en `GeoUtilsTest.kt`.
- [x] **Tarea 2.5**: Definir contratos de repositorio `SearchPreferencesRepository.kt` y `FaqRepository.kt` con sus respectivos Casos de Uso.

- - -

## Fase 3: Capa de Datos y Persistencia (Android)

- [ ] **Tarea 3.1**: Actualizar `AppPrefs.kt` en `EncryptedDataStore` para almacenar `SearchPreferences`.
- [ ] **Tarea 3.2**: Implementar `SearchPreferencesRepositoryImpl.kt` y `FaqRepositoryImpl.kt`.
- [ ] **Tarea 3.3**: Actualizar Room `MiraiLinkDemoDatabase.kt` a version 2 (`fallbackToDestructiveMigration`) anadiendo campos de residencia y ubicacion en `DemoEntities.kt`.
- [ ] **Tarea 3.4**: Actualizar `DemoDataSeeder.kt` ubicando a Hikari en Palma de Mallorca y sembrando perfiles locales de Mallorca y viajeros en la zona.
- [ ] **Tarea 3.5**: Actualizar `DemoSwipeRepositoryImpl.kt` para aplicar el algoritmo de filtrado por residencia o viajeros segun las preferencias activas.

- - -

## Fase 4: Capa de Presentacion, Minimapa y FAQ (Android)

- [ ] **Tarea 4.1**: Crear componente Compose `SearchRadiusMinimap.kt` con teselas abiertas OpenStreetMap y circulo dinamico interactivo.
- [ ] **Tarea 4.2**: Crear componente `SearchSettingsSection.kt` en Ajustes con chips de alcance, Slider, Switch de viajeros, minimapa y boton explicito "Guardar Ajustes".
- [ ] **Tarea 4.3**: Crear pantalla `FaqScreen.kt` con acordeones Material 3 respondiendo sobre MiraiLink, la privacidad del historico de 50 puntos y las busquedas.
- [ ] **Tarea 4.4**: Actualizar `SettingsViewModel.kt` y `SettingsScreen.kt` para coordinar el guardado explicito y la navegacion a FAQ.
- [ ] **Tarea 4.5**: Actualizar `UserCard.kt` para mostrar el lugar de residencia ("Palma de Mallorca • A 8 km") y badge sutil para viajeros.
- [ ] **Tarea 4.6**: Actualizar `HomeScreen.kt` con estado vacio elegante si no hay ubicacion ni permisos concedidos.
- [ ] **Tarea 4.7**: Registrar todos los nuevos componentes en los modulos de Koin (`di/koin/`).

- - -

## Fase 5: Verificacion, Tests y Build

- [ ] **Tarea 5.1**: Ejecutar suite de pruebas unitarias (`./gradlew.bat testDebugUnitTest`).
- [ ] **Tarea 5.2**: Compilar APK debug (`./gradlew.bat assembleDebug`).
- [ ] **Tarea 5.3**: Generar walkthrough detallado de la funcionalidad.
