# [APROBADO] Especificacion Funcional: Seccion Explorar (Explore Hub), Feeds Tematicos y Ajustes por Categoria

- **Fecha**: 2026-09-24
- **Estado**: [APROBADO]
- **Autor / Responsable**: Antigravity & FeryaelJustice
- **Modulos Afectados**: `:app` (`com.feryaeljustice.mirailink`), `MiraiLink-Backend` (Express 5 + PostgreSQL)

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**:
  - Actualmente los usuarios de MiraiLink solo disponen de un feed de swipes general en la pantalla de inicio (`HomeScreen`), donde todos los perfiles se mezclan sin clasificacion tematica.
  - No existe una forma de explorar perfiles segun afinidades especificas (otakus, maratones de anime, jugadores de videojuegos cooperativos o competitivos, relaciones estables, nuevas amistades, etc.).
  - Los usuarios que buscan jugar juntos o comentar series no pueden filtrar directamente sin alterar sus preferencias de busqueda globales.
  - La barra inferior (`MiraiLinkBottomBar`) carece de un acceso directo a la exploracion por afinidades y categorias.
- **Objetivo**:
  - Incorporar la nueva seccion "Explorar" como segundo tab en la barra de navegacion inferior (`MiraiLinkBottomBar`), identificada con icono de brujula (`ic_compass` / `ic_explore`), manteniendo `HomeScreen` intacto como feed general de swipes.
  - Disenar una experiencia visual hibrida: encabezado superior con carrusel horizontal de recomendaciones destacadas (estilo Bumble) y debajo secciones tematicas (Otaku & Anime, Gaming, Conexiones) organizadas en cuadriculas de 2 columnas con tarjetas de iconos llamativos y contadores de personas (estilo Tinder).
  - Permitir que al seleccionar una categoria se abra un feed especializado de swipes (`CategoryFeedScreen`) con candidatos que cumplen dinamicamente los criterios de dicha tematica segun sus gustos registrados.
  - Dotar a cada categoria de una hoja de ajustes independientes (`CategoryDiscoverySettingsSheet`) donde el usuario puede ajustar su radio de distancia local (10 a 500 km) sin afectar la busqueda global, persistiendo de forma idempotente tanto en backend como en el modo demo local (Room).
  - Ofrecer contadores reales de personas por categoria con almacenamiento en cache en memoria (TTL de 5 minutos) para evitar sobrecarga de consultas en base de datos.

- - -

## 2. Situacion Actual

- La barra de navegacion inferior cuenta actualmente con cuatro tabs: Inicio/Swipes (`HomeScreen`), Mensajes (`MessagesScreen`), Likes recibidos (`ReceivedLikesScreen`) y Perfil (`ProfileScreen`).
- El swipe feed se gestiona exclusivamente desde `SwipeRepository` y `SwipeApiService` (`GET /swipe/feed`), aplicando las preferencias globales de `user_search_preferences` (radio, ambito de busqueda y ubicacion activa/residencia).
- No existen tablas ni endpoints para categorias de exploracion ni preferencias de descubrimiento aisladas por categoria.
- En el modo Offline Demo (`DemoSwipeRepositoryImpl`), los usuarios se filtran unicamente por distancia general contra `search_preferences`.

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)

1. **Nuevo Tab y Pantalla "Explorar" (`ExploreScreen`)**:
   - Acceso desde `MiraiLinkBottomBar` situado como segundo elemento (Inicio, Explorar, Mensajes, Likes, Perfil).
   - Icono representativo de brujula (`ic_compass` / `ic_explore`) con etiqueta localizada en `strings.xml`.
   - Encabezado con carrusel horizontal de recomendaciones destacadas (estilo Bumble, ej. "Recomendadas para ti", "Intereses similares").
   - Secciones tematicas en cuadricula vertical de 2 columnas (estilo Tinder):
     - **Otaku & Anime**: "Maraton de Anime", "Cosplay & Eventos", "Manga & Lectura".
     - **Videojuegos & Gaming**: "Co-op & Duos", "Competitivo & E-Sports", "RPG & Fantasia", "Casual & Chill".
     - **Conexiones & Metas**: "Relacion Estable", "Citas Informales", "Nuevas Amistades".
   - Cada tarjeta de categoria muestra icono representativo, titulo y contador real de personas con cache en memoria.
2. **Pantalla de Feed por Categoria (`CategoryFeedScreen`)**:
   - Navegacion con paso de parametros (`categoryId`, `categoryName`).
   - Top bar personalizada: boton de retroceso/cierre (X), titulo de la categoria, icono de ajustes/filtros (`ic_sparkles` / `ic_tune`).
   - Pila de tarjetas de candidatos (`UserSwipeCardStack`) con diseno adaptado: insignia "Online", tags de intereses/aficiones coincidentes y botones de swipe (Deshacer, Dislike, Like).
   - Filtrado dinamico en backend y Room: cruza candidatos que compartan animes (para categorias Otaku), videojuegos (para Gaming) o metas de relacion (para Citas).
3. **Hoja de Ajustes de Descubrimiento por Categoria (`CategoryDiscoverySettingsSheet`)**:
   - Modal inferior (Bottom Sheet) accesible desde el icono de filtros en la pantalla de la categoria.
   - Titulo dinamico: "Ajustes de [Nombre de Categoria]".
   - Slider de distancia de discovery (en km, rango 10 km a 500 km).
   - Exclusivamente enfocado a radio local/residencia (se omiten filtros globales de "Todo el mundo" o selector de paises especificos).
   - Texto informativo: "Estos ajustes solo se aplican en [Nombre de Categoria]".
   - Boton de accion principal: "Actualizar ajustes".
4. **Backend (Express 5 + PostgreSQL)**:
   - Nueva migracion (`008_explore_categories_and_preferences.sql`):
     - Tabla `explore_categories` (id, code, icon_key, section_group, sort_order).
     - Tabla `explore_category_translations` (category_id, language_id, title, description).
     - Tabla `user_category_preferences` (user_id, category_id, radius_km, updated_at) con clave primaria compuesta `(user_id, category_id)` para garantizar idempotencia en `UPSERT`.
   - Endpoints en `src/routes/explore.routes.js`:
     - `GET /explore/categories`: listado de categorias ordenadas por secciones con traduccion y conteos reales en cache.
     - `GET /explore/categories/:categoryId/feed`: candidatos que cumplen los criterios de la categoria y el radio de esa categoria.
     - `GET /explore/categories/:categoryId/settings`: consulta de ajustes guardados para esa categoria (o valor por defecto de 40 km si no existen).
     - `PUT /explore/categories/:categoryId/settings`: guardado idempotente de preferencias de la categoria.
5. **Modo Offline Demo (Room Database)**:
   - Paridad completa: catalogo de categorias pre-sembrado en Room / Seeder.
   - Entidad `DemoCategoryPreferenceEntity` y DAO para almacenar el radio por categoria.
   - Filtrado dinamico en memoria/Room segun la categoria seleccionada.

### 3.2. Fuera del Alcance (Out of Scope)

- Filtros globales de seleccion de paises ("Todo el mundo" o "Pais especifico") dentro del modal de ajustes de categorias (descartados explicitamente por peticion del usuario).
- Superlikes o Boosts de pago (botones decorativos o reservados para futuras versiones).
- Creacion dinamica de categorias por parte de usuarios finales (las categorias son administradas y catalogadas por el sistema).

- - -

## 4. Casuisticas y Comportamiento Mobile

- **Comportamiento en Modo Online vs Modo Offline Demo**:
  - En Modo Online, las categorias y candidatos se obtienen de los endpoints `/explore/*` con token JWT.
  - En Modo Offline Demo, el repositorio delegado recurre a `DemoExploreRepositoryImpl`, consultando entidades locales de Room pre-cargadas por el seeder.
- **Ciclo de Vida y Recuperacion de Estado**:
  - `CategoryFeedViewModel` y `ExploreViewModel` deben conservar la categoria activa, la lista de perfiles y los ajustes en `SavedStateHandle` para soportar giros de pantalla y recreacion de actividad por el sistema.
- **Estados Vacios (Empty States) y Manejo de Errores**:
  - Si una categoria no tiene candidatos cercanos en el radio seleccionado, se muestra un estado vacio amigable ("No hay perfiles cercanos en [Categoria] por ahora") con sugerencia de ampliar el radio en ajustes.
  - Manejo de fallos con `MiraiLinkResult` y pantalla de reintento (`MiraiLinkErrorContent`).
- **Ergonomia, Teclado y Accesibilidad**:
  - Objetivos tactiles de al menos 48 x 48 dp en todas las tarjetas de categorias, botones de swipe y controles de slider.
  - Soporte completo para Tema Claro y Tema Oscuro.
  - `imePadding()` y `navigationBarsPadding()` en la hoja de ajustes y barras de navegacion.

- - -

## 5. Criterios de Aceptacion (Formato Given - When - Then)

### Criterio 1: Navegacion al Hub de Explorar desde la Bottom Bar
- **Dado que**: El usuario se encuentra en la pantalla principal de la app con la barra de navegacion visible.
- **Cuando**: Pulsa en el tab "Explorar" (icono de brujula, segundo tab).
- **Entonces**: Se navega a `ExploreScreen`, visualizando el carrusel superior y las secciones tematicas ("Otaku & Anime", "Videojuegos & Gaming", "Conexiones & Metas") con sus tarjetas y contadores correspondientes.

### Criterio 2: Apertura del Feed de una Categoria
- **Dado que**: El usuario se encuentra en `ExploreScreen`.
- **Cuando**: Pulsa sobre una categoria (ej. "Gamers & Co-op").
- **Entonces**: Se abre `CategoryFeedScreen` con el titulo de la categoria en la barra superior, cargando perfiles que comparten intereses de videojuegos.

### Criterio 3: Actualizacion de Ajustes de Distancia por Categoria
- **Dado que**: El usuario esta en el feed de "Maraton de Anime" y abre el modal de ajustes.
- **Cuando**: Modifica el slider de distancia a 80 km y pulsa "Actualizar ajustes".
- **Entonces**: La preferencia se guarda de manera idempotente solo para esa categoria, el feed se recarga con el nuevo radio y las preferencias globales de la app en la pantalla de inicio no sufren modificaciones.

- - -

## 6. Resolucion de Decisiones Previas

- [x] **Organizacion Visual**: Opcion A (Hibrido Bumble + Tinder: carrusel superior destacado estilo Bumble y secciones tematicas en cuadricula vertical de 2 columnas estilo Tinder con iconos y contadores).
- [x] **Criterio de Filtrado**: Opcion A (Filtrado dinamico e inferido en backend y Room a partir de animes, videojuegos y metas de relacion existentes del perfil, sin friccion para el usuario).
- [x] **Contador de Personas**: Opcion A (Calculo real agregado con cache en memoria de 5 minutos para evitar sobrecarga en base de datos).
- [x] **Nomenclatura Interna**: `ExploreScreen` y `ExploreViewModel` para el hub de exploracion, `CategoryFeedScreen` y `CategoryFeedViewModel` para el swipe feed tematico, manteniendo `HomeScreen` como el feed principal general.
