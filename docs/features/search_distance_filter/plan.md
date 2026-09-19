# [APROBADO] Plan Tecnico de Arquitectura: Busqueda y Filtrado por Radio en Km con Minimapa

- **Especificacion funcional asociada**: `docs/features/search_distance_filter/spec.md`
- **Estado**: [APROBADO]
- **Fecha**: 2026-09-17
- **Modulo**: `:app` (`com.feryaeljustice.mirailink`) y `MiraiLink-Backend` (`Express 5 + PostgreSQL`)

- - -

## 1. Hechos Verificados en el Proyecto (Sin Alucinaciones)

Informacion verificada rigurosamente en `gradle/libs.versions.toml`, `app/build.gradle.kts` y `MiraiLink-Backend`:

- **Android App**:
  - Lenguaje & JVM: Kotlin `2.4.10` / Java 17
  - Build Tools: AGP `9.4.0`, KSP `2.3.8`, compileSdk `37`, targetSdk `37`, minSdk `26`
  - Compose: Compose BOM `2026.08.00`, Material 3, Material 3 Adaptive `1.3.0`
  - Navegacion: Jetpack Navigation 3 (`1.1.7`)
  - Dependency Injection: Koin BOM `4.2.2`, `koin-android`, `koin-androidx-compose`, `koin-annotations`
  - Persistencia Local: Room `2.8.4` (KSP), EncryptedDataStore `1.2.1` (`security-crypto:1.1.0`)
  - Imagenes & Red: Coil `2.7.0`, Retrofit `3.0.0`, OkHttp `5.5.0`
  - Testing: JUnit 4, MockK `1.14.11`, Turbine `1.2.1`, Coroutines Test `1.11.0`, Room Testing `2.8.4`

- **Backend (MiraiLink-Backend)**:
  - Runtime & Framework: Node.js, Express `5.x`, PostgreSQL (`pg` pool)
  - Testing: Vitest

- - -

## 2. Impacto Arquitectonico y Contratos por Capas

### 2.1. Backend (`MiraiLink-Backend`)
- **Migracion SQL**:
  - `src/database/migrations/migration_005_user_location.sql`
  - Agregar `latitude DOUBLE PRECISION`, `longitude DOUBLE PRECISION`, `location_name VARCHAR(100)`, `search_radius_km INT DEFAULT 40`, `search_global BOOLEAN DEFAULT FALSE` a la tabla `users`.
- **DTOs (`src/dto/user.dto.js`)**:
  - Anadir campos de ubicacion a `publicUserFields` y `PUBLIC_USER_SQL_COLUMNS`.
- **Controladores**:
  - `src/controllers/user.controller.js`: Exponer y persistir `latitude`, `longitude`, `location_name`, `search_radius_km`, `search_global`.
  - `src/controllers/swipe.controller.js`: Calcular distancia esferica en SQL sobre el feed cuando `search_global = FALSE` y ordenar por distancia ascendente.

### 2.2. Capa de Dominio Android (`domain/`)
- **Modelos**:
  - `User.kt`: Anadir `val latitude: Double? = null`, `val longitude: Double? = null`, `val locationName: String? = null`.
  - `SearchPreferences.kt`: Modelo inmutable con `radiusKm: Float = 40f`, `isGlobalSearch: Boolean = false`, `isPremium: Boolean = false`.
- **Utilidades Geograficas**:
  - `GeoUtils.kt`: Formula de Haversine pura en Kotlin:
    ```kotlin
    fun calculateDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double
    ```
- **Contratos de Repositorio**:
  - `SearchPreferencesRepository.kt`: Interfaz con `getSearchPreferences(): Flow<SearchPreferences>` y `saveSearchPreferences(prefs: SearchPreferences)`.
- **Casos de Uso**:
  - `GetSearchPreferencesUseCase.kt`
  - `SaveSearchPreferencesUseCase.kt`

### 2.3. Capa de Datos Android (`data/`)
- **EncryptedDataStore**:
  - `AppPrefs.kt`: Anadir `val searchRadiusKm: Float = 40f`, `val searchGlobalEnabled: Boolean = false`.
- **Room Database (Modo Demo)**:
  - `MiraiLinkDemoDatabase.kt`: Incrementar version a 2 con fallback o migracion automatica.
  - `DemoEntities.kt`: Anadir `latitude`, `longitude`, `locationName` a `DemoUserProfileEntity` y `DemoFeedUserEntity`.
  - `DemoDataSeeder.kt`: Configurar a Hikari en Palma de Mallorca y perfiles del feed con coordenadas insulares y exteriores.
  - `DemoSwipeRepositoryImpl.kt`: Filtrar usuarios del feed comparando contra las coordenadas de Hikari con `GeoUtils.calculateDistanceKm` y ordenando por distancia ascendente.
- **Repositorio Remoto**:
  - `SwipeRepositoryImpl.kt`: Adaptado a los campos de distancia del backend.
  - `SearchPreferencesRepositoryImpl.kt`: Implementacion backed por `DataStore<AppPrefs>`.

### 2.4. Capa de Presentacion Android (`ui/`)
- **Componentes**:
  - `SearchRadiusMinimap.kt`: Renderizado en Compose Canvas de teselas de mapa (OpenStreetMap) con circulo semitransparente escalado segun el radio en kilometros.
  - `SearchSettingsSection.kt`: Seccion en Ajustes con Slider (5 km - 150 km), Checkbox/Switch para "Buscar en todo el mundo", valor actual en texto y transicion animada para el minimapa.
- **ViewModel**:
  - `SettingsViewModel.kt`: Manejo reactivo de `searchPreferences` (`StateFlow`), actualizacion de radio y toggle de busqueda global con debounce y guardado en DataStore.
  - `HomeViewModel.kt`: Sincronizado para recargar usuarios al volver de Ajustes si cambio la configuracion.
- **Pantalla**:
  - `SettingsScreen.kt`: Integracion limpia de `SearchSettingsSection` con soporte para tema claro/oscuro y padding accesible.

### 2.5. Inyeccion de Dependencias (`di/koin/`)
- Registro de `SearchPreferencesRepository` y casos de uso en `repositoryModule` y `useCaseModule`.

- - -

## 3. Estrategia de Testing

1. **Pruebas Unitarias (`testDebugUnitTest`)**:
   - `GeoUtilsTest`: Validar exactitud de Haversine con coordenadas conocidas.
   - `DemoSwipeRepositoryTest`: Validar exclusion de candidatos fuera del radio y exclusion de likes/dislikes/cuenta propia.
   - `SettingsViewModelTest`: Validar flujo de estados con Turbine al modificar radio y toggle global.
2. **Pruebas de Backend (`npm test`)**:
   - Validar calculo de distancia y filtro de feed en `swipe.controller.test.js`.
3. **Compilacion de Verificacion**:
   - `./gradlew assembleDebug` y `./gradlew testDebugUnitTest`.

- - -

## 4. Riesgos Tecnicos y Mitigaciones

- **Riesgo 1**: Carga de teselas de mapa lenta o sin red.
  - **Mitigacion**: El minimapa cuenta con fondo esquematico vectorizado offline y solo consulta teselas publicas si hay conexion, dibujando siempre el circulo vectorial centrado.
- **Riesgo 2**: Incompatibilidad de esquema previo de Room en instalaciones locales existentes.
  - **Mitigacion**: Se utiliza `fallbackToDestructiveMigration()` en la base de datos demo para recrear los datos mock limpios sin crasheos.
