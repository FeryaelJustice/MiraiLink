# [APROBADO] Especificacion Funcional: Sincronizacion de Catalogo de Intereses y Visualizacion de Tarjetas con Miniaturas

- **Fecha**: 2026-09-23
- **Estado**: [APROBADO]
- **Autor / Responsable**: Antigravity y equipo MiraiLink
- **Modulos Afectados**: `:app` (`com.feryaeljustice.mirailink`) y `MiraiLink-Backend`

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**: Actualmente, la seleccion y visualizacion de intereses (animes y videojuegos favoritos) se realiza mediante listas planas de texto o chips (`TagsSection` y `PublicInterestChips`). Esto no refleja la identidad visual rica del mundo del anime y el gaming. Ademas, el catalogo de animes y juegos en el backend depende de seeds estaticos o inserciones manuales sin un mecanismo de ingestion periódica desde fuentes externas de referencia que enriquezca el catalogo con imagenes, portadas y titulos actualizados.
- **Objetivo**: 
  1. Implementar un proceso de sincronizacion periodico y resiliente en el backend de Node.js que consulte APIs externas (RAWG para videojuegos y catalogo de anime como Jikan/MyAnimeList) para enriquecer y mantener actualizado el catalogo en la base de datos PostgreSQL, sin duplicados ni sobreescritura destructiva de los datos existentes.
  2. Transformar la experiencia en Android:
     - En **Editar Perfil**: selector visual con lista categorizada/buscable, con miniatura representativa a la izquierda y nombre localizado. Al seleccionar intereses, se muestran en una cuadricula (grid) moderna en lugar de chips de texto.
     - En **Visualizacion de Perfil** (tanto `UserCard` en modo lectura como `PublicUserCard` en descubrimiento): mostrar los animes y juegos seleccionados en formato cuadricula de tarjetas visuales, con esquinas redondeadas, degradado inferior de abajo a arriba y el nombre localizado sobreimpreso legiblemente.
     - Sistema de fallbacks en cascada de imagen: si un anime/juego carece de imagen remota o falla su carga, comprobar si existe un recurso de imagen de Goku (`R.drawable.goku` o similar); si no existe o falla, recurrir al logotipo oficial de MiraiLink (`R.drawable.logomirailink`).

- - -

## 2. Situacion Actual Comprobada

- **Backend**:
  - Tablas `animes` y `games` cuentan con columnas `id` (UUID), `name`, `description`, `image_url`, `catalog_key` (VARCHAR 160 UNIQUE) e `image_path` (TEXT).
  - Tablas de traduccion `anime_name_translations`, `anime_biography_translations`, `game_name_translations` y `game_biography_translations` vinculadas por clave compuesta con `supported_languages` (`es`, `en`).
  - El endpoint `GET /api/v1/catalog/animes` y `GET /api/v1/catalog/games` devuelve una lista de items localizados (`id`, `catalog_key`, `name`, `biography`, `image_url`).
  - Las relaciones de usuario `user_anime_interests` y `user_game_interests` asocian `user_id` con el UUID del catalogo.
  - `.env.example` contiene la variable `RAWG_API_KEY`, pero actualmente no existe ninguna rutina o servicio de cron/polling que sincronice con la API de RAWG o con una API de animes.
- **Android (`:app`)**:
  - Los modelos `AnimeDto`, `Anime`, `AnimeViewEntry`, `GameDto`, `Game` y `GameViewEntry` ya poseen la propiedad `imageUrl: String?`.
  - En `UserCard.kt` (edicion), los intereses se seleccionan mediante `MultiSelectDropdown` sin fotos ni miniaturas.
  - En `UserCard.kt` (lectura) y `PublicUserCard.kt`, los animes y juegos se representan mediante `TagsSection` y `PublicInterestChips` con texto plano en chips.
  - El proyecto utiliza Coil (`coil-compose`) para la carga asincrona de imagenes y Compose Material 3.

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)

1. **Backend**:
   - Servicio de ingestion y sincronizacion periódica para videojuegos con RAWG API (`https://api.rawg.io/api/games?key=...`).
   - Servicio de ingestion y sincronizacion para anime (Jikan API v4 `https://api.jikan.moe/v4/top/anime` y endpoints asociados para títulos y portadas canónicas sin necesidad de API key).
   - Normalizacion e insercion idempotente en PostgreSQL: verificacion por `slug`/`catalog_key` y nombre para evitar duplicados, insercion de traducciones base (`es`, `en`) y asignacion de URL de imagen (`image_path`).
   - Prioridad absoluta del catalogo canonico existente en la base de datos: si un item ya existe, no se sobrescriben nombres ni biografias personalizadas; solo se completa `image_path` si estaba ausente.
2. **Android**:
   - Componente Atom/Molecule de tarjeta de interes visual (`InterestCard`): imagen con `AsyncImage` de Coil, `ContentScale.Crop`, borde redondeado (`RoundedCornerShape(12.dp)` o `16.dp`), `Box` con degradado vertical de abajo hacia arriba (`Brush.verticalGradient`) y texto del nombre localizado en la parte inferior con tipografia legible y contraste garantizado.
   - Fallback de imagen en cascada: imagen remota -> comprobacion de existencia de logo de Goku -> fallback final a `R.drawable.logomirailink`.
   - Grid de intereses (`InterestsGrid`): disposicion en cuadricula de 2 columnas para la seccion de gustos tanto en `UserCard` (lectura y seleccion en edicion) como en `PublicUserCard`.
   - Selector visual enriquecido en `EditProfile`: `ModalBottomSheet` que muestra la miniatura a la izquierda, nombre localizado, indicador de seleccion y filtro de busqueda por texto.
   - Soporte en modo Offline Sandbox (Room Database): persistencia y carga de `imageUrl` en el seeder local de demo (`DemoDataSeeder.kt`) y entities de Room.

### 3.2. Fuera del Alcance (Out of Scope)

- Pantallas completas de detalle enciclopedico del anime o juego (ej. lista de episodios, actores de voz o guias de trofeos).
- Edicion de catalogos por parte de usuarios finales comunes (solo sincronizacion automatica o administracion).
- Reemplazo de claves primarias UUID existentes o reestructuracion destructiva de tablas de usuarios.

- - -

## 4. Casuisticas y Comportamiento Mobile

- **Modo Online vs Modo Offline Demo**:
  - En Modo Online, la lista de seleccion y las tarjetas cargan las imagenes y nombres entregados por la API de MiraiLink. Coil cachea las imagenes en disco y memoria.
  - En Modo Offline Demo, Room Database proporciona la lista precargada con URLs simuladas o recursos locales, garantizando que la experiencia visual sea identica sin conexion.
- **Ciclo de Vida y Recuperacion de Estado**:
  - En la edicion de perfil, las listas seleccionadas (`selectedAnimes`, `selectedGames`) y el estado del selector visual se mantienen en `ProfileViewModel` / `EditProfileUiState` y sobreviven a cambios de orientacion (rotacion) y transiciones a segundo plano.
- **Estados Vacios y Manejo de Errores**:
  - Si el usuario no tiene ningun anime o juego seleccionado, se mantiene el mensaje localizado existente (`user_card_fav_animes_empty` / `user_card_fav_games_empty`) con diseno armonioso.
  - Si una imagen remota falla al cargar (error 404, sin conexion o timeout de red), Coil conmuta automaticamente al placeholder/fallback local (Goku o logotipo MiraiLink) sin romper el layout ni dejar espacios en blanco.
- **Ergonomia, Accesibilidad y Teclado**:
  - En la cuadricula de tarjetas, cada elemento interactivo o de eliminacion en edicion tendra un area tactil de al menos 48 x 48 dp.
  - En el selector con buscador, el campo de busqueda incorporara `imePadding()` para no ser tapado por el teclado virtual.
  - Se definira `contentDescription` accesible para TalkBack en cada tarjeta ("Anime: {nombre}" o "Juego: {nombre}").
  - Pleno soporte en Tema Claro y Tema Oscuro.

- - -

## 5. Criterios de Aceptacion (Formato Given - When - Then)

### Criterio 1: Visualizacion en Cuadricula con Degradado y Fallback
- **Dado que**: un usuario tiene animes y juegos favoritos asignados en su perfil.
- **Cuando**: se visualiza su tarjeta en la pantalla principal (`PublicUserCard`) o en la seccion de perfil (`UserCard`).
- **Entonces**: los intereses se muestran en una cuadricula visual de 2 columnas de tarjetas con portada, degradado vertical inferior sombreado y el nombre del anime o juego sobre el degradado. Si alguna imagen no esta disponible o falla la conexion, se muestra el fallback configurado (logo de Goku si existe en el proyecto, o logotipo de MiraiLink).

### Criterio 2: Selector Visual en Edicion de Perfil
- **Dado que**: el usuario esta editando su perfil en `UserCard` y desea anadir animes o juegos.
- **Cuando**: abre la interfaz de seleccion de animes o juegos (ModalBottomSheet).
- **Entonces**: se presenta una lista visual donde cada elemento muestra una miniatura a la izquierda y el nombre localizado, con buscador en tiempo real. Al marcar o desmarcar elementos, la lista de seleccion se actualiza inmediatamente en el estado de edicion en formato cuadrícula.

### Criterio 3: Sincronizacion Idempotente en Backend sin Duplicados
- **Dado que**: el backend ejecuta la tarea de sincronizacion periódica contra las APIs externas.
- **Cuando**: se reciben registros de juegos (RAWG) o animes que ya existen en la base de datos (por nombre o slug/catalog_key).
- **Entonces**: el backend no duplica registros, no modifica identificadores UUID asignados a los usuarios y preserva las traducciones existentes, actualizando unicamente el campo de imagen si este se encontraba nulo o vacio.

### Criterio 4: Resiliencia ante Fallos de APIs Externas
- **Dado que**: la API de RAWG o la de anime estan caidas, sin cuota o la red externa falla durante el ciclo de sincronizacion.
- **Cuando**: el proceso en segundo plano intenta obtener nuevos registros.
- **Entonces**: el backend captura y registra el error de forma segura en los logs sin interrumpir la operacion del servidor HTTP ni de Socket.IO, y los clientes Android continuan operando con el catalogo localmente persistido.

- - -

## 6. Decisiones Resueltas

- [x] **API de Animes**: Integracion de catalogo de anime mediante Jikan API v4 (`https://api.jikan.moe/v4/top/anime`), garantizando titulos oficiales, sinopsis y portadas de alta calidad sin necesidad de claves de pago, manteniendo total simetria con RAWG para videojuegos.
- [x] **Frecuencia y Disparador de Sincronizacion en Backend**: Servicio periodico configurable via variable de entorno `CATALOG_SYNC_INTERVAL_HOURS=24` al arrancar el servidor Node.js + script CLI ejecutable bajo demanda `npm run sync:catalog` (`scripts/sync-catalog.js`).
- [x] **UX del Selector en Editar Perfil**: `ModalBottomSheet` con campo de busqueda en tiempo real, lista scrolleable con miniatura a la izquierda, nombre localizado y checkbox de seleccion multiple.
- [x] **Disposicion de la Cuadricula Grid**: Cuadricula de 2 columnas estilo Tinder/Bumble para destacar las portadas visuales con degradado inferior y maxima legibilidad.
- [x] **Estrategia de Fallback en Cascada**: Verificacion dinamica de identificador de recurso para `goku` (`context.resources.getIdentifier("goku", "drawable", context.packageName)`); si existe se utiliza, y de no existir se recurre de forma segura al logotipo oficial `R.drawable.logomirailink`.
