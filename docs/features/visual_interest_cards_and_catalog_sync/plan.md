# [APROBADO] Plan Tecnico de Arquitectura: Sincronizacion de Catalogo y Tarjetas Visuales de Intereses

- **Especificacion funcional asociada**: `docs/features/visual_interest_cards_and_catalog_sync/spec.md`
- **Estado**: [APROBADO]
- **Fecha**: 2026-09-23
- **Modulos**: `:app` (`com.feryaeljustice.mirailink`) y `MiraiLink-Backend`

- - -

## 1. Hechos Verificados en el Proyecto (Sin Alucinaciones)

Informacion verificada rigurosamente en `gradle/libs.versions.toml`, `app/build.gradle.kts` y `MiraiLink-Backend/package.json`:

- **Android**:
  - **Lenguaje & JVM**: Kotlin `2.4.10` / Java 17
  - **Compilador & Build Tool**: AGP `9.4.1`, Gradle `9.6.1`
  - **SDK Targets**: Min SDK `26`, Compile SDK `37`, Target SDK `37`
  - **UI**: Compose BOM `2026.09.00` con Material 3
  - **Carga de Imagenes**: Coil `2.7.0` (`io.coil-kt:coil-compose`)
  - **Inyeccion de Dependencias**: Koin BOM `4.2.2` con `koin-androidx-compose`
  - **Persistencia Local**: Room Database `2.8.5` (KSP) y Encrypted DataStore `1.2.1`
  - **Red & API**: Retrofit `3.0.0`, OkHttp `5.5.0`
  - **Serializacion**: Kotlinx Serialization `1.11.0`
- **Backend (`MiraiLink-Backend`)**:
  - **Runtime**: Node.js (ES Modules `type: "module"`)
  - **Base de Datos**: PostgreSQL (`pg` pool)
  - **Seguridad**: `bcrypt`, `jsonwebtoken`
  - **APIs Externas**: RAWG API (`https://api.rawg.io/api/games`), Jikan API v4 (`https://api.jikan.moe/v4/top/anime`)

- - -

## 2. Impacto Arquitectonico y Contratos por Capas

### 2.1. Backend (`MiraiLink-Backend`)

#### Servicios Externos
- `src/services/rawgService.js`:
  - `fetchTopGames(limit, page)`: realiza petición a `https://api.rawg.io/api/games?key=${RAWG_API_KEY}&page_size=${limit}&page=${page}&ordering=-added`.
  - Transforma cada juego a: `{ name, slug, image_url, description, catalog_key: 'game:' + slug }`.
  - Manejo de rate limit y errores con fallback seguro.
- `src/services/jikanService.js`:
  - `fetchTopAnimes(limit, page)`: realiza petición a `https://api.jikan.moe/v4/top/anime?page=${page}&limit=${limit}&filter=bypopularity`.
  - Transforma cada anime a: `{ name, title_english, image_url: images.webp.large_image_url || images.jpg.large_image_url, synopsis, catalog_key: 'anime:' + mal_id }`.
  - Respeto del rate limit de Jikan (3 peticiones/segundo).
- `src/services/catalogSyncService.js`:
  - `syncCatalog()`:
    - Sincroniza juegos (RAWG) y animes (Jikan).
    - Para cada elemento: comprueba si existe por `catalog_key` o por `name` (insensible a mayúsculas/minúsculas).
    - Si existe: ejecuta `UPDATE SET image_path = COALESCE(image_path, $new_image)` preservando `id` (UUID), `catalog_key` y traducciones ya existentes.
    - Si no existe: inserta en `animes` / `games` generando un nuevo UUID, crea las entradas en `anime_name_translations` y `anime_biography_translations` (o `game_*`) para `es` y `en`.
    - No bloquea el event loop ni el pool de base de datos.
- `scripts/sync-catalog.js`:
  - Script independiente ejecutable con `node scripts/sync-catalog.js` o `npm run sync:catalog`.
- `src/server.js` / `src/app.js`:
  - Tarea periódica con temporizador (`setInterval`) basada en `CATALOG_SYNC_INTERVAL_HOURS` (por defecto 24 horas), con primera ejecución diferida para no retrasar el arranque del servidor.

### 2.2. Android (`:app`)

#### Capa de Dominio (`domain/`)
- Utilidad `InterestImageFallback.kt`:
  - Función `resolveInterestFallback(context: Context): Int` que comprueba dinámicamente si existe `R.drawable.goku` mediante `context.resources.getIdentifier("goku", "drawable", context.packageName)`.
  - Si el identificador es mayor que 0, retorna ese recurso. De lo contrario, retorna `R.drawable.logomirailink`.

#### Capa de Presentación (`ui/`)
- Componente Molecule `ui/components/catalog/InterestCard.kt`:
  - Parámetros: `title: String`, `imageUrl: String?`, `modifier: Modifier = Modifier`, `isSelected: Boolean = false`, `onRemoveClick: (() -> Unit)? = null`, `onClick: (() -> Unit)? = null`.
  - Renderizado: `Card` o `Box` con `RoundedCornerShape(12.dp)`, `AsyncImage` con `ContentScale.Crop`, degradado vertical inferior (`Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)))`), texto del título en blanco con `FontWeight.Bold` y `TextOverflow.Ellipsis`.
  - Si `onRemoveClick != null`, muestra un botón accesible de eliminar (área táctil mínima 48x48 dp) en la esquina superior derecha.
- Componente Molecule `ui/components/catalog/InterestsGrid.kt`:
  - Disposición en 2 columnas: muestra la lista de `AnimeViewEntry` o `GameViewEntry` con `InterestCard`.
- Componente Organism `ui/components/catalog/VisualInterestPickerModal.kt`:
  - `ModalBottomSheet` con campo de texto de búsqueda (`MiraiLinkSearchField` con `imePadding()`), contador de seleccionados, botón de cerrar/confirmar y lista con miniaturas cuadradas a la izquierda, título en negrita y `Checkbox`.
- Modificación en `ui/components/user/UserCard.kt`:
  - En modo lectura: reemplaza `TagsSection` plano por `InterestsGrid` con tarjetas visuales.
  - En modo edición: reemplaza `MultiSelectDropdown` por botones de acción que abren `VisualInterestPickerModal` y muestra la cuadrícula editable de intereses seleccionados.
- Modificación en `ui/components/user/PublicUserCard.kt`:
  - Reemplaza `PublicInterestChips` por la cuadrícula visual de intereses.

#### Capa de Datos (`data/`)
- Actualización de `DemoDataSeeder.kt`:
  - Asegurar URLs representativas y fallbacks en los animes y juegos precargados para el modo offline sandbox.

- - -

## 3. Estrategia de Testing

- **Backend**:
  - Tests unitarios de parsing y normalización en `rawgService.test.js` y `jikanService.test.js`.
  - Tests de sincronización idempotente en `catalogSyncService.test.js` con base de datos de prueba o mocks de `pg`.
  - Validación con `npm test`.
- **Android**:
  - Tests unitarios en `app/src/test/`:
    - `InterestImageFallbackTest`: verificar retorno del logo de MiraiLink cuando no existe el drawable de Goku.
    - `ProfileViewModelTest`: verificar que la adición y eliminación de intereses mantiene la coherencia de `selectedAnimes` y `selectedGames`.
  - Compilación y pruebas:
    - `./gradlew.bat testDebugUnitTest`
    - `./gradlew.bat assembleDebug`

- - -

## 4. Riesgos Tecnicos y Mitigaciones

- **Riesgo 1: Rate Limiting o Caída de APIs Externas (RAWG / Jikan)**:
  - *Mitigación*: Bloques `try/catch` con reintentos exponenciales en los clientes de sincronización; si la API externa falla, el backend registra el warning y conserva intacto el catálogo existente.
- **Riesgo 2: Tiempos de Carga de Imágenes en Android**:
  - *Mitigación*: Coil realiza caching automático en disco y memoria; el fallback local en cascada se muestra instantáneamente mientras la imagen remota se descarga.
- **Riesgo 3: Teclado ocultando el buscador en el ModalBottomSheet**:
  - *Mitigación*: Inclusión de `Modifier.imePadding()` y gestión de foco en el buscador dentro del modal.
