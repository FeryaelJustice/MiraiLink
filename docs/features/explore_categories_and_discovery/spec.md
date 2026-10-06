# [APROBADO] Especificación Funcional: Sección Explorar (Explore Hub), Feeds Temáticos y Ajustes por Categoría

- **Fecha**: 2026-09-24
- **Estado**: [APROBADO]
- **Autor / Responsable**: Antigravity & FeryaelJustice
- **Módulos Afectados**: `:app` (`com.feryaeljustice.mirailink`), `MiraiLink-Backend` (Express 5 + PostgreSQL)

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**:
  - Actualmente los usuarios de MiraiLink solo disponen de un feed de swipes general en la pantalla de inicio (`HomeScreen`), donde todos los perfiles se mezclan sin clasificación temática.
  - No existe una forma de explorar perfiles según afinidades específicas (otakus, maratones de anime, jugadores de videojuegos cooperativos o competitivos, relaciones estables, nuevas amistades, etc.).
  - Los usuarios que buscan jugar juntos o comentar series no pueden filtrar directamente sin alterar sus preferencias de búsqueda globales.
  - La barra inferior (`MiraiLinkBottomBar`) carece de un acceso directo a la exploración por afinidades y categorías.
- **Objetivo**:
  - Incorporar la nueva sección "Explorar" como segundo tab en la barra de navegación inferior (`MiraiLinkBottomBar`), identificada con icono de brujula (`ic_compass` / `ic_explore`), manteniendo `HomeScreen` intacto como feed general de swipes.
  - Diseñar una experiencia visual hibrida: encabezado superior con carrusel horizontal de recomendaciones destacadas (estilo Bumble) y debajo secciones temáticas (Otaku & Anime, Gaming, Conexiones) organizadas en cuadrículas de 2 columnas con tarjetas de iconos llamativos y contadores de personas (estilo Tinder).
  - Permitir que al seleccionar una categoría se abra un feed especializado de swipes (`CategoryFeedScreen`) con candidatos que cumplen dinámicamente los criterios de dicha temática según sus gustos registrados.
  - Dotar a cada categoría de una hoja de ajustes independientes (`CategoryDiscoverySettingsSheet`) donde el usuario puede ajustar su radio de distancia local (10 a 500 km) sin afectar la búsqueda global, persistiendo de forma idempotente tanto en backend como en el modo demo local (Room).
  - Ofrecer contadores reales de personas por categoría con almacenamiento en caché en memoria (TTL de 5 minutos) para evitar sobrecarga de consultas en base de datos.

- - -

## 2. Situación Actual

- La barra de navegación inferior cuenta actualmente con cuatro tabs: Inicio/Swipes (`HomeScreen`), Mensajes (`MessagesScreen`), Likes recibidos (`ReceivedLikesScreen`) y Perfil (`ProfileScreen`).
- El swipe feed se gestiona exclusivamente desde `SwipeRepository` y `SwipeApiService` (`GET /swipe/feed`), aplicando las preferencias globales de `user_search_preferences` (radio, ámbito de búsqueda y ubicación activa/residencia).
- No existen tablas ni endpoints para categorías de exploración ni preferencias de descubrimiento aisladas por categoría.
- En el modo Offline Demo (`DemoSwipeRepositoryImpl`), los usuarios se filtran únicamente por distancia general contra `search_preferences`.

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)

1. **Nuevo Tab y Pantalla "Explorar" (`ExploreScreen`)**:
   - Acceso desde `MiraiLinkBottomBar` situado como segundo elemento (Inicio, Explorar, Mensajes, Likes, Perfil).
   - Icono representativo de brujula (`ic_compass` / `ic_explore`) con etiqueta localizada en `strings.xml`.
   - Encabezado con carrusel horizontal de recomendaciones destacadas (estilo Bumble, ej. "Recomendadas para ti", "Intereses similares").
   - Secciones temáticas en cuadrícula vertical de 2 columnas (estilo Tinder):
     - **Otaku & Anime**: "Maraton de Anime", "Cosplay & Eventos", "Manga & Lectura".
     - **Videojuegos & Gaming**: "Co-op & Dúos", "Competitivo & E-Sports", "RPG & Fantasia", "Casual & Chill".
     - **Conexiones & Metas**: "Relación Estable", "Citas Informales", "Nuevas Amistades".
   - Cada tarjeta de categoría muestra icono representativo, título y contador real de personas con caché en memoria.
2. **Pantalla de Feed por Categoría (`CategoryFeedScreen`)**:
   - Navegación con paso de parámetros (`categoryId`, `categoryName`).
   - Top bar personalizada: botón de retroceso/cierre (X), título de la categoría, icono de ajustes/filtros (`ic_sparkles` / `ic_tune`).
   - Pila de tarjetas de candidatos (`UserSwipeCardStack`) con diseño adaptado: insignia "Online", tags de intereses/aficiones coincidentes y botones de swipe (Deshacer, Dislike, Like).
   - Filtrado dinámico en backend y Room: cruza candidatos que compartan animes (para categorías Otaku), videojuegos (para Gaming) o metas de relación (para Citas).
3. **Hoja de Ajustes de Descubrimiento por Categoría (`CategoryDiscoverySettingsSheet`)**:
   - Modal inferior (Bottom Sheet) accesible desde el icono de filtros en la pantalla de la categoría.
   - Título dinámico: "Ajustes de [Nombre de Categoría]".
   - Slider de distancia de discovery (en km, rango 10 km a 500 km).
   - Exclusivamente enfocado a radio local/residencia (se omiten filtros globales de "Todo el mundo" o selector de países específicos).
   - Texto informativo: "Estos ajustes solo se aplican en [Nombre de Categoría]".
   - Botón de acción principal: "Actualizar ajustes".
4. **Backend (Express 5 + PostgreSQL)**:
   - Nueva migración (`008_explore_categories_and_preferences.sql`):
     - Tabla `explore_categories` (id, code, icon_key, section_group, sort_order).
     - Tabla `explore_category_translations` (category_id, language_id, title, description).
     - Tabla `user_category_preferences` (user_id, category_id, radius_km, updated_at) con clave primaria compuesta `(user_id, category_id)` para garantizar idempotencia en `UPSERT`.
   - Endpoints en `src/routes/explore.routes.js`:
     - `GET /explore/categories`: listado de categorías ordenadas por secciones con traducción y conteos reales en caché.
     - `GET /explore/categories/:categoryId/feed`: candidatos que cumplen los criterios de la categoría y el radio de esa categoría.
     - `GET /explore/categories/:categoryId/settings`: consulta de ajustes guardados para esa categoría (o valor por defecto de 40 km si no existen).
     - `PUT /explore/categories/:categoryId/settings`: guardado idempotente de preferencias de la categoría.
5. **Modo Offline Demo (Room Database)**:
   - Paridad completa: catálogo de categorías pre-sembrado en Room / Seeder.
   - Entidad `DemoCategoryPreferenceEntity` y DAO para almacenar el radio por categoría.
   - Filtrado dinámico en memoria/Room según la categoría seleccionada.

### 3.2. Fuera del Alcance (Out of Scope)

- Filtros globales de selección de países ("Todo el mundo" o "País específico") dentro del modal de ajustes de categorías (descartados explícitamente por petición del usuario).
- Superlikes o Boosts de pago (botones decorativos o reservados para futuras versiones).
- Creación dinámica de categorías por parte de usuarios finales (las categorías son administradas y catalogadas por el sistema).

- - -

## 4. Casuísticas y Comportamiento Mobile

- **Comportamiento en Modo Online vs Modo Offline Demo**:
  - En Modo Online, las categorías y candidatos se obtienen de los endpoints `/explore/*` con token JWT.
  - En Modo Offline Demo, el repositorio delegado recurre a `DemoExploreRepositoryImpl`, consultando entidades locales de Room pre-cargadas por el seeder.
- **Ciclo de Vida y Recuperación de Estado**:
  - `CategoryFeedViewModel` y `ExploreViewModel` deben conservar la categoría activa, la lista de perfiles y los ajustes en `SavedStateHandle` para soportar giros de pantalla y recreación de actividad por el sistema.
- **Estados Vacíos (Empty States) y Manejo de Errores**:
  - Si una categoría no tiene candidatos cercanos en el radio seleccionado, se muestra un estado vacío amigable ("No hay perfiles cercanos en [Categoría] por ahora") con sugerencia de ampliar el radio en ajustes.
  - Manejo de fallos con `MiraiLinkResult` y pantalla de reintento (`MiraiLinkErrorContent`).
- **Ergonomía, Teclado y Accesibilidad**:
  - Objetivos táctiles de al menos 48 x 48 dp en todas las tarjetas de categorías, botones de swipe y controles de slider.
  - Soporte completo para Tema Claro y Tema Oscuro.
  - `imePadding()` y `navigationBarsPadding()` en la hoja de ajustes y barras de navegación.

- - -

## 5. Criterios de Aceptación (Formato Given - When - Then)

### Criterio 1: Navegación al Hub de Explorar desde la Bottom Bar
- **Dado que**: El usuario se encuentra en la pantalla principal de la app con la barra de navegación visible.
- **Cuando**: Pulsa en el tab "Explorar" (icono de brujula, segundo tab).
- **Entonces**: Se navega a `ExploreScreen`, visualizando el carrusel superior y las secciones temáticas ("Otaku & Anime", "Videojuegos & Gaming", "Conexiones & Metas") con sus tarjetas y contadores correspondientes.

### Criterio 2: Apertura del Feed de una Categoría
- **Dado que**: El usuario se encuentra en `ExploreScreen`.
- **Cuando**: Pulsa sobre una categoría (ej. "Gamers & Co-op").
- **Entonces**: Se abre `CategoryFeedScreen` con el título de la categoría en la barra superior, cargando perfiles que comparten intereses de videojuegos.

### Criterio 3: Actualización de Ajustes de Distancia por Categoría
- **Dado que**: El usuario está en el feed de "Maraton de Anime" y abre el modal de ajustes.
- **Cuando**: Modifica el slider de distancia a 80 km y pulsa "Actualizar ajustes".
- **Entonces**: La preferencia se guarda de manera idempotente solo para esa categoría, el feed se recarga con el nuevo radio y las preferencias globales de la app en la pantalla de inicio no sufren modificaciones.

- - -

## 6. Resolución de Decisiones Previas

- [x] **Organización Visual**: Opción A (Hibrido Bumble + Tinder: carrusel superior destacado estilo Bumble y secciones temáticas en cuadrícula vertical de 2 columnas estilo Tinder con iconos y contadores).
- [x] **Criterio de Filtrado**: Opción A (Filtrado dinámico e inferido en backend y Room a partir de animes, videojuegos y metas de relación existentes del perfil, sin fricción para el usuario).
- [x] **Contador de Personas**: Opción A (Calculo real agregado con caché en memoria de 5 minutos para evitar sobrecarga en base de datos).
- [x] **Nomenclatura Interna**: `ExploreScreen` y `ExploreViewModel` para el hub de exploración, `CategoryFeedScreen` y `CategoryFeedViewModel` para el swipe feed temático, manteniendo `HomeScreen` como el feed principal general.
