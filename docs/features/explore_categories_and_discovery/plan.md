# [APROBADO] Plan Tecnico de Arquitectura: Seccion Explorar (Explore Hub), Feeds Tematicos y Ajustes por Categoria

- **Especificacion funcional asociada**: `docs/features/explore_categories_and_discovery/spec.md`
- **Estado**: [APROBADO]
- **Fecha**: 2026-09-24
- **Modulos Afectados**: `:app` (`com.feryaeljustice.mirailink`), `MiraiLink-Backend` (Express 5 + PostgreSQL)

- - -

## 1. Hechos Verificados en el Proyecto (Sin Alucinaciones)

Informacion verificada rigurosamente en `gradle/libs.versions.toml`, `app/build.gradle.kts` y `package.json`:

- **Android Stack**:
  - Lenguaje & JVM: Kotlin `2.4.10` / Java 17
  - Build Tool: AGP `9.4.1`, KSP `2.3.8`
  - SDK Targets: Min SDK `26`, Compile SDK `37`, Target SDK `37`
  - UI: Jetpack Compose BOM `2026.09.00` con Material 3 y adaptive `1.3.0`
  - Navegacion: Navigation 3 (`androidx.navigation3:navigation3-core:1.1.7`)
  - Inyeccion de Dependencias: Koin BOM `4.2.2` con `koin-androidx-compose`
  - Persistencia Local: Room `2.8.5` (con KSP) y Encrypted DataStore `1.2.1`
  - Red & Serializacion: Retrofit `3.0.0`, OkHttp `5.5.0`, Kotlinx Serialization `1.11.0`
  - Testing: JUnit `4.13.2`, MockK `1.14.11`, Turbine `1.2.1`, Coroutines Test `1.10.2`

- **Backend Stack**:
  - Runtime: Node.js con ES Modules (`"type": "module"`)
  - Framework Web: Express `5.2.1`
  - Base de Datos: PostgreSQL driver `pg` `8.23.0`
  - Validacion: Zod `4.6.5`
  - Testing: Vitest `5.0.1`, Supertest `7.3.0`

> **Regla estricta**: Prohibido inventar metodos inexistentes o sugerir librerias que no existan en el catalogo de versiones sin aprobacion previa.

- - -

## 2. Impacto Arquitectonico y Contratos en Backend (`MiraiLink-Backend`)

### 2.1. Base de Datos & Migraciones (`src/database/migrations/`)

- **Migracion `008_explore_categories_and_preferences.sql`**:
  - Tabla de categorias maestras `explore_categories`:
    ```sql
    CREATE TABLE IF NOT EXISTS explore_categories (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
        code VARCHAR(50) UNIQUE NOT NULL,
        section_group VARCHAR(50) NOT NULL, -- 'otaku', 'gaming', 'connections'
        icon_key VARCHAR(50) NOT NULL,      -- 'tv', 'theater', 'book', 'controller', 'trophy', 'sword', 'coffee', 'rose', 'cocktail', 'puzzle'
        filter_type VARCHAR(50) NOT NULL,   -- 'anime', 'game', 'relationship_goal'
        filter_value VARCHAR(100),
        sort_order INT NOT NULL DEFAULT 0,
        created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );
    ```
  - Tabla de traducciones i18n `explore_category_translations`:
    ```sql
    CREATE TABLE IF NOT EXISTS explore_category_translations (
        category_id UUID NOT NULL REFERENCES explore_categories(id) ON DELETE CASCADE,
        language_id UUID NOT NULL REFERENCES supported_languages(id) ON DELETE CASCADE,
        title VARCHAR(100) NOT NULL,
        description VARCHAR(255),
        PRIMARY KEY (category_id, language_id)
    );
    ```
  - Tabla de preferencias por usuario y categoria `user_category_preferences`:
    ```sql
    CREATE TABLE IF NOT EXISTS user_category_preferences (
        user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
        category_id UUID NOT NULL REFERENCES explore_categories(id) ON DELETE CASCADE,
        radius_km INT NOT NULL DEFAULT 40 CHECK (radius_km >= 10 AND radius_km <= 500),
        created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
        updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
        PRIMARY KEY (user_id, category_id)
    );
    CREATE INDEX IF NOT EXISTS idx_user_cat_prefs ON user_category_preferences(user_id, category_id);
    ```
  - Precarga de categorias y traducciones en `es`, `en`, `ja`:
    - **Otaku**: `anime_marathon` ("Maraton de Anime"), `cosplay_events` ("Cosplay & Eventos"), `manga_lovers` ("Manga & Lectura").
    - **Gaming**: `gaming_coop` ("Co-op & Duos"), `esports_competitive` ("Competitivo & E-Sports"), `rpg_fantasy` ("RPG & Fantasia"), `casual_gaming` ("Casual & Chill").
    - **Conexiones**: `long_term_relationship` ("Relacion estable"), `casual_dating` ("Citas informales"), `new_friends` ("Nuevas amistades").

### 2.2. Validaciones (`src/validation/explore.schemas.js`)

- `categoryParamsSchema`:
  - `categoryId`: UUID valido.
- `updateCategorySettingsSchema`:
  - `radius_km`: entero entre 10 y 500.

### 2.3. Servicios y Cache de Contadores (`src/services/explore.service.js`)

- Cache en memoria con TTL de 5 minutos:
  - Estructura: `Map<string, { count: number, expiresAt: number }>`.
  - Clave: `${userId}:${categoryId}` o calculo geo-agregado por coordenadas de residencia/activas.
- Consulta SQL agregada:
  - Cruza candidatos cercanos que cumplan la condicion de la categoria (`filter_type` en `user_anime_interests`, `user_game_interests` o `user_relationship_goals`).
  - Excluye perfiles ya interactuados en `likes` o `dislikes` por el usuario autenticado.

### 2.4. Controladores y Rutas (`src/controllers/explore.controller.js` y `src/routes/explore.routes.js`)

- `GET /api/explore/categories`:
  - Recupera las categorias agrupadas por `section_group`, con traduccion localizada segun cabecera `Accept-Language`, e inyecta el contador `active_count` desde la cache/servicio.
- `GET /api/explore/categories/:categoryId/feed`:
  - Consulta los perfiles candidatos de esa categoria.
  - Recupera el radio especifico de `user_category_preferences` (fallback a 40 km si no ha sido configurado todavia).
  - Devuelve candidatos en formato `toPublicUsers` con fotos, tags de anime/juegos coincidentes y calculo de distancia.
- `GET /api/explore/categories/:categoryId/settings`:
  - Devuelve `{ radius_km: number }` para el usuario y categoria.
- `PUT /api/explore/categories/:categoryId/settings`:
  - Ejecuta `UPSERT` idempotente:
    ```sql
    INSERT INTO user_category_preferences (user_id, category_id, radius_km, updated_at)
    VALUES ($1, $2, $3, NOW())
    ON CONFLICT (user_id, category_id)
    DO UPDATE SET radius_km = EXCLUDED.radius_km, updated_at = NOW();
    ```

- - -

## 3. Impacto Arquitectonico y Contratos en Android Client (`:app`)

### 3.1. Capa de Dominio (`domain/`)

- **Modelos de Negocio (`domain/model/explore/`)**:
  - `ExploreCategory`:
    ```kotlin
    data class ExploreCategory(
        val id: String,
        val code: String,
        val sectionGroup: ExploreSectionGroup,
        val iconKey: String,
        val title: String,
        val description: String,
        val activeCount: Int,
    )
    ```
  - `ExploreSectionGroup`: enum `OTAKU`, `GAMING`, `CONNECTIONS`.
  - `ExploreSection`:
    ```kotlin
    data class ExploreSection(
        val group: ExploreSectionGroup,
        val title: String,
        val categories: List<ExploreCategory>,
    )
    ```
  - `CategoryPreference`:
    ```kotlin
    data class CategoryPreference(
        val categoryId: String,
        val radiusKm: Int,
    )
    ```
- **Contrato de Repositorio (`domain/repository/ExploreRepository.kt`)**:
  ```kotlin
  interface ExploreRepository {
      suspend fun getExploreSections(): MiraiLinkResult<List<ExploreSection>>
      suspend fun getCategoryFeed(categoryId: String): MiraiLinkResult<List<User>>
      suspend fun getCategoryPreferences(categoryId: String): MiraiLinkResult<CategoryPreference>
      suspend fun updateCategoryPreferences(categoryId: String, radiusKm: Int): MiraiLinkResult<Unit>
  }
  ```
- **Casos de Uso (`domain/usecase/explore/`)**:
  - `GetExploreSectionsUseCase(repository: ExploreRepository)`
  - `GetCategoryFeedUseCase(repository: ExploreRepository)`
  - `GetCategoryPreferencesUseCase(repository: ExploreRepository)`
  - `UpdateCategoryPreferencesUseCase(repository: ExploreRepository)`

### 3.2. Capa de Datos (`data/`)

- **API Service & DTOs (`data/remote/ExploreApiService.kt`)**:
  - Modelos serializables Kotlinx: `ExploreCategoryDto`, `CategorySettingsRequest`, `CategorySettingsResponse`.
  - Endpoints Retrofit correspondientes a `/api/explore/*`.
- **Implementacion Remota (`data/repository/ExploreRepositoryImpl.kt`)**:
  - Llama a `ExploreApiService`, transforma DTOs a modelos de dominio y mapea URLs de fotos con `resolvePhotoUrls`.
- **Persistencia Local y Modo Demo (`data/local/demo/`)**:
  - Entidad Room `DemoCategoryPreferenceEntity`:
    ```kotlin
    @Entity(
        tableName = "demo_category_preferences",
        primaryKeys = ["userId", "categoryId"]
    )
    data class DemoCategoryPreferenceEntity(
        val userId: String,
        val categoryId: String,
        val radiusKm: Int,
        val updatedAt: Long = System.currentTimeMillis(),
    )
    ```
  - DAO `DemoCategoryDao`:
    - `getPreference(userId: String, categoryId: String): DemoCategoryPreferenceEntity?`
    - `insertOrUpdate(entity: DemoCategoryPreferenceEntity)`
  - Actualizacion de `MiraiLinkDemoDatabase`: incrementar `version = 3`, registrar entidad y DAO.
- **Implementacion Demo (`data/repository/demo/DemoExploreRepositoryImpl.kt`)**:
  - Seeder de categorias locales precargadas en memoria / Room.
  - Filtro dinamico de candidatos locales en memoria segun `filter_type` y distancia calculada con `GeoUtils.calculateDistanceKm`.
- **Repositorio Delegado (`data/repository/delegating/DelegatingExploreRepository.kt`)**:
  - Conmuta transparentemente entre `ExploreRepositoryImpl` y `DemoExploreRepositoryImpl` mediante `DemoModeManager.isDemoActive()`.

### 3.3. Capa de Presentacion (`ui/`)

- **Navegacion (`ui/navigation/`)**:
  - En `AppScreen.kt`:
    ```kotlin
    @Serializable
    @SerialName("explore")
    data object ExploreScreen : AppScreen()

    @Serializable
    @SerialName("category_feed")
    data class CategoryFeedScreen(
        val categoryId: String,
        val categoryName: String,
    ) : AppScreen()
    ```
  - En `MiraiLinkBottomBar.kt`:
    - Incorporar `BottomNavItem(AppScreen.ExploreScreen, R.drawable.ic_explore, R.string.nav_explore)` como 2º tab.
  - En `NavWrapper.kt`:
    - Registrar entradas de navegacion para `AppScreen.ExploreScreen` y `AppScreen.CategoryFeedScreen`.
- **Pantalla de Explorar (`ui/screens/explore/ExploreScreen.kt`)**:
  - Encabezado con carrusel horizontal superior de recomendaciones destacadas (Bumble style).
  - Bloques tematicos con titulos ("Otaku & Anime", "Videojuegos & Gaming", "Conexiones & Metas") y cuadriculas de 2 columnas con tarjetas de categoria (Tinder style: icono glossy, titulo y contador).
  - Al pulsar una categoria: `navigator.navigate(AppScreen.CategoryFeedScreen(categoryId, categoryTitle))`.
- **Pantalla de Feed por Categoria (`ui/screens/explore/feed/CategoryFeedScreen.kt`)**:
  - Top bar con boton atras (X), titulo de categoria y boton de ajustes (icono spark/filtro).
  - Pila de tarjetas reutilizando `UserSwipeCardStack` para swiping interactivo (like/dislike/undo).
- **Hoja de Ajustes de Categoria (`ui/screens/explore/feed/CategoryDiscoverySettingsSheet.kt`)**:
  - Modal Bottom Sheet con slider de 10 a 500 km.
  - Leyenda informativa: "Estos ajustes solo se aplican en [Nombre de Categoria]".
  - Boton "Actualizar ajustes" que ejecuta `updateCategoryPreferences` y refresca el feed.
- **ViewModels**:
  - `ExploreViewModel`: expone `ExploreUiState (Loading, Success, Error)` y carga de categorias.
  - `CategoryFeedViewModel`: expone `CategoryFeedUiState` y maneja swipe stack, undo y apertura/guardado de ajustes independientes.

### 3.4. Modulos Koin (`di/koin/`)

- `NetworkModule.kt`: agregar servicio Retrofit `ExploreApiService`.
- `DemoModule.kt`: registrar `DemoCategoryDao` y `DemoExploreRepositoryImpl` con calificador `Demo`.
- `RepositoryModule.kt`: registrar `ExploreRepositoryImpl` con `Remote` y `DelegatingExploreRepository` como principal.
- `UseCaseModule.kt`: registrar los 4 casos de uso de `explore`.
- `ViewModelModule.kt`: registrar `ExploreViewModel` y `CategoryFeedViewModel`.

- - -

## 4. Estrategia de Testing

- **Pruebas Unitarias en Android (`app/src/test/`)**:
  - `GetExploreSectionsUseCaseTest`: verificar ordenamiento y mapeo correcto.
  - `ExploreViewModelTest`: validar estados con Turbine (`Loading` -> `Success`).
  - `CategoryFeedViewModelTest`: validar swipe left/right, recarga tras cambio de radio y persistencia de ajustes.
- **Pruebas de Base de Datos en Room (`app/src/androidTest/` o Robolectric)**:
  - `DemoCategoryDaoTest`: verificar `insertOrUpdate` idempotente ante multiples guardados de radio.
- **Pruebas en Backend (`tests/`)**:
  - `explore.test.js` con Vitest y Supertest:
    - `GET /api/explore/categories`: formato, internacionalizacion de titulos y contadores.
    - `GET /api/explore/categories/:id/feed`: retorno de perfiles segun radio y categoria.
    - `PUT /api/explore/categories/:id/settings`: idempotencia de actualizacion y validaciones Zod.
- **Verificacion de Compilacion**:
  - `./gradlew assembleDebug` y `./gradlew testDebugUnitTest`.

- - -

## 5. Riesgos Tecnicos y Mitigaciones

- **Riesgo 1**: Sobrecarga en la base de datos de PostgreSQL al calcular en tiempo real los contadores de personas para multiples categorias.
  - **Mitigacion**: Cache en memoria en `explore.service.js` con TTL de 5 minutos e indices optimizados en PostgreSQL (`idx_user_anime_user`, `idx_user_game_user`, `idx_users_residence_coords`).
- **Riesgo 2**: Inconsistencia al cambiar de tab en la barra inferior (Bottom Bar) o recreacion de la actividad por rotacion.
  - **Mitigacion**: Uso de `SavedStateHandle` en `CategoryFeedViewModel` para guardar `categoryId` y `categoryName`, preservando la pila de navegacion nativa de Navigation 3.
- **Riesgo 3**: Desalineacion de ajustes entre modo online y modo demo offline.
  - **Mitigacion**: `DelegatingExploreRepository` implementa exactamente la misma interfaz para ambos modos, garantizando paridad completa de comportamiento.
