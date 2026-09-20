# [APROBADO] Plan Tecnico: Catalogo localizado de animes y juegos

- **Especificacion funcional asociada**: `docs/features/localized-interest-catalog/spec.md`
- **Estado**: [APROBADO]
- **Fecha**: 2026-09-20
- **Modulos**: Android `:app` y MiraiLink Backend

## 1. Hechos verificados

- Android usa Kotlin `2.4.10`, Java `17`, AGP `9.4.1`, min SDK `26`, compile y target SDK `37`.
- Android usa Compose BOM `2026.09.00`, Room `2.8.5`, Koin `4.2.2`, Retrofit `3.0.0`, OkHttp `5.5.0`, Kotlin serialization `1.11.0`, JUnit `4.13.2`, MockK `1.14.11` y Turbine `1.2.1`.
- La base demo Room está en versión `2`, no exporta esquema y actualmente no declara migración registrada.
- Backend usa Node `>=22`, Express `5.2.1`, PostgreSQL mediante `pg` `8.23.0`, Zod `4.6.5` y Vitest `5.0.1`.
- El reset local recrea el esquema desde `db.sql`, aplica los SQL de `src/database/migrations` en orden y después carga `db_inserts.sql`. El seed Node existente es idempotente. No existe aún un ejecutor incremental de migraciones para una base desplegada.
- Los endpoints públicos de catálogo no reciben locale. `CatalogApiService` no manda cabecera de idioma. La selección de Android compara nombres en `ProfileViewModel`, y `UserCard` entrega listas de nombres a `MultiSelectDropdown`.
- El perfil autenticado expone más información que una tarjeta pública, incluido `username`, email, teléfono, coordenadas y preferencias de búsqueda. Perfiles públicos y otros flujos todavía pueden exponer `username`.

## 2. Diseño de datos y migración PostgreSQL

### 2.1 Esquema final

Se conservarán `animes` y `games` como raíces independientes. No habrá una tabla polimórfica ni un identificador común de catálogo.

| Tabla | Clave y campos principales | Finalidad |
|---|---|---|
| `supported_languages` | `id` UUID, `code` único, `english_name` | Idiomas disponibles. Seed inicial `es` y `en`. |
| `animes` | `id` UUID, `catalog_key` único e inmutable, `image_path` nullable | Identidad estable y metadato no localizado de anime. |
| `anime_name_translations` | PK `(anime_id, language_id)`, `name` | Nombre localizado de anime. |
| `anime_biography_translations` | PK `(anime_id, language_id)`, `biography` no nula por defecto vacía | Biografía localizada preparada para edición administrativa futura. |
| `games` | `id` UUID, `catalog_key` único e inmutable, `image_path` nullable | Identidad estable y metadato no localizado de juego. |
| `game_name_translations` | PK `(game_id, language_id)`, `name` | Nombre localizado de juego. |
| `game_biography_translations` | PK `(game_id, language_id)`, `biography` no nula por defecto vacía | Biografía localizada preparada para edición administrativa futura. |

Las tablas de traducción tendrán FKs con borrado en cascada hacia su entidad y hacia `supported_languages`. Los intereses existentes seguirán referenciando `animes.id` y `games.id`, por lo que no requieren reescritura de relaciones.

`image_path` admitirá una URL absoluta `http` o `https`, o una ruta relativa completa. El backend aplicará `resolvePublicMediaUrl`: retornará las URL absolutas intactas y prefijará una ruta relativa con el origen público configurado, igual que los recursos de foto. PostgreSQL no guardará blobs.

### 2.2 Migración de una base desplegada

1. Crear `schema_migrations` si no existe y registrar cada archivo SQL aplicado de forma transaccional.
2. Añadir las nuevas tablas e índices sin tocar `user_anime_interests` ni `user_game_interests`.
3. Insertar idiomas `es` y `en` de forma idempotente.
4. Para cada fila heredada de `animes` y `games`, conservar su UUID, generar un `catalog_key` determinista y único, copiar el antiguo `image_url` a `image_path`, crear ambos nombres traducidos a partir de `name` y crear biografías vacías para ambos idiomas. La columna `description` no se convierte en biografía porque el requisito establece biografías iniciales vacías.
5. Validar conteos, claves y relaciones. Las columnas heredadas `name`, `description` e `image_url` se conservarán temporalmente sin ser usadas por el contrato, para evitar una eliminación irreversible de datos editoriales. Su retirada requerirá backup verificado y una aprobación específica posterior.
6. Actualizar `db.sql` y `db_inserts.sql` para que una base nueva nazca directamente con el modelo final.

Se añadirá `npm run db:migrate` mediante un ejecutor explícito y seguro. El servidor no ejecutará migraciones al arrancar. Producción deberá ejecutar ese comando una vez antes de desplegar el backend nuevo.

### 2.3 Seeds

- Extraer los datos de catálogo de los inserts SQL a una fuente estructurada reutilizable para `db_inserts.sql` y `scripts/seed.js`, o mantener una única fuente si el análisis de implementación confirma que permite ambos consumidores sin divergencia.
- `npm run db:seed` insertará o actualizará idiomas, claves de catálogo, traducciones de nombre y filas de biografía vacía sin borrar intereses ni contenido administrativo futuro.
- `npm run db:reset` seguirá siendo destructivo y separado. Nunca se invocará desde `db:migrate` ni `db:seed`.

## 3. Contrato HTTP y privacidad

### 3.1 Localización

- `GET /api/catalog/animes` y `GET /api/catalog/games` aceptarán `Accept-Language`.
- Las rutas de perfil y descubrimiento que incluyan intereses usarán la misma cabecera.
- El resolvedor aceptará el primer tag BCP 47 soportado, reducirá variantes como `es-ES` a `es` y usará `es` cuando no haya coincidencia o falte una traducción.
- La respuesta de cada ítem será `{ id, catalog_key, name, biography, image_url }`. `biography` podrá ser cadena vacía y `image_url` podrá ser nulo. `image_url` será ya absoluta y cargable para Android.
- La actualización de perfil seguirá recibiendo arrays de objetos por ID. La validación Zod sustituirá el parseo genérico por una lista de UUIDs sin duplicados, máximo 100, y comprobará en transacción que todos existen antes de sustituir intereses.

### 3.2 Matriz de proyecciones de usuario

| Campo de `users` o relación | Perfil autenticado | Terceros, discovery, matches y chat | Edición |
|---|---|---|---|
| `id` | Sí | Sí | No |
| `username` | Sí, solo lógica interna | No | Nunca tras registro |
| `nickname`, `bio`, `gender`, `birthdate`, fotos, intereses | Sí | Sí, con la proyección correspondiente | Sí, salvo fotos por sus endpoints |
| `email`, `phone_number` | Sí, solo cuenta | No | Fuera de este cambio |
| residencia textual | Sí | Sí | Sí |
| coordenadas de residencia y localización actual, historial | No en respuesta ordinaria | No | Se envían solo a los endpoints de ubicación ya existentes |
| preferencias de búsqueda | Sí | No | Por su endpoint propio |
| `password_hash`, proveedor, verificación, 2FA, secretos, tokens, borrado, marcas temporales | No | No | No |

Se consolidarán DTOs explícitos de propietario y tercero. Controladores de perfiles, swipe, match y chat deberán usar estas proyecciones, evitando construir respuestas desde `SELECT *` o campos ad hoc.

## 4. Arquitectura Android

### 4.1 Data y dominio

- Ampliar `AnimeDto`, `GameDto`, `Anime`, `Game` y sus mappers con `catalogKey`, `biography` e `imageUrl` normalizada, preservando `id` como identidad de selección.
- `CatalogApiService` recibirá el locale actual en `Accept-Language`. Un proveedor inyectable de locale permitirá pruebas y refresco al recrearse la pantalla con otro idioma.
- `UserDto`, modelos de dominio y mappers diferenciarán perfil propietario de proyección pública para que `username` no sea requerido en datos de terceros. Los modelos de chat y match dejarán de depender de él para renderizado.
- Room demo aumentará de versión y tendrá una migración probada. Las listas JSON de intereses conservarán objetos por `id`; los datos demo incluirán los campos de catálogo nuevos, aunque no se representen aún.

### 4.2 Estado, edición y UI

- `EditProfileUiState` conservará catálogo e intereses como entradas con ID. La intención de tags transportará IDs, y `ProfileViewModel` eliminará las búsquedas por `name`.
- El selector recibirá pares ID-etiqueta localizada y comparará la selección por ID, manteniendo la etiqueta solo para representación.
- `UserCard` tendrá componentes de sección reutilizables. Vista: Información básica, Residencia y Gustos. Edición: mismas secciones y operaciones existentes. Los gustos permanecerán separados de la información básica.
- Biografía e imagen de catálogo se transportarán hasta los view entries pero no generarán controles, miniaturas, detalles ni texto visible.
- La UI seguirá usando `StateFlow`, `viewModelScope`, manejo tipado de errores, targets de 48 dp, `imePadding()`, colores Material 3 y contenido accesible.

## 5. Estrategia de pruebas

### Backend

- Pruebas de migración PostgreSQL sobre una base desechable: filas, UUIDs de intereses y conteos antes y después; repetición segura del ejecutor; rollback ante error.
- Pruebas del seed: no duplicados, mantiene traducciones administrativas no vacías y no altera intereses.
- Integración HTTP: `Accept-Language` en `es`, `en`, `es-ES`, idioma no soportado y traducción ausente; la respuesta tiene ID estable, URL resuelta y biografía vacía.
- Validación de perfil: IDs válidos, duplicados, catálogo desconocido y sustitución atómica.
- Pruebas de DTO y rutas para probar que `username` solo está en el perfil autenticado y que no salen campos sensibles en cada proyección pública.
- Actualizar OpenAPI, `docs/api-reference.md`, `docs/code-reference.md` y README con el contrato y los comandos operativos.

### Android

- Unit tests de DTO a dominio, normalización de selección por ID y fallback recibido desde API.
- Tests de `ProfileViewModel` con catálogos cuyos nombres cambian entre cargas, verificando que los intereses siguen seleccionados.
- Tests Room de migración de la base demo y deserialización de intereses antiguos.
- Pruebas Compose o screenshot para la jerarquía de vista y edición, sin sección visible para metadatos de catálogo.
- Verificación manual en dispositivo o emulador de selección, guardado, cambio de idioma, tema claro y oscuro, rotación y fallo de red.

## 6. Riesgos y mitigaciones

- **Migración irreversible de columnas heredadas**: validar en transacción y conservar un backup lógico antes de ejecutar producción. El SQL migratorio tendrá comprobaciones de conteo y no borrará datos de usuario.
- **Divergencia entre reset, seed y producción**: un único orden de migraciones y tests de esquema para ambos recorridos.
- **Fuga de `username` o ubicación**: DTOs allowlist por contexto y pruebas de respuesta negativas.
- **Cambio de idioma con selección incorrecta**: estado basado exclusivamente en IDs y tests con etiquetas distintas para el mismo ID.
- **URL de imagen no cargable**: un único normalizador en backend, pruebas para ruta relativa y URL absoluta, sin lógica duplicada en Android.
