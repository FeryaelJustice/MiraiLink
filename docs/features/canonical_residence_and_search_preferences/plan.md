# [APROBADO] Plan Tecnico: Residencia Canonica y Preferencias de Busqueda Separadas

- **Especificacion asociada**: `docs/features/canonical_residence_and_search_preferences/spec.md`
- **Estado**: [APROBADO]
- **Fecha**: 2026-09-21
- **Modulos**: `:app` y `MiraiLink-Backend`

## 1. Hechos verificados

- Android usa Kotlin `2.4.10`, Java 17, Room `2.8.4`, Retrofit `3.0.0`, Kotlinx Serialization `1.11.0` y Koin `4.2.2`.
- Backend usa Node.js 22 o superior, Express `5.2.1`, PostgreSQL 16, Zod `4.6.5` y UUID de PostgreSQL mediante `gen_random_uuid()`.
- El backend ya tiene `supported_languages` con UUID y código único, además de las tablas de traducciones de anime y juego. `catalogLocalization.js` resuelve `Accept-Language` con español como respaldo.
- `users` contiene ahora textos de residencia, coordenadas de residencia y activa, además de las cuatro preferencias de búsqueda.
- La actualización de perfil usa una transacción, `PUT /user/settings/search` escribe las preferencias y el feed las lee directamente de `users`.
- El modo demo guarda usuarios y preferencias con Room y filtra radios con `GeoUtils`.
- El selector de residencia actual usa `Locale` y `Geocoder`. Tras esta entrega no serán la fuente de catálogo ni de identidad de residencia.

## 2. Modelo de datos backend

### 2.1 Catálogo geográfico global

La migración crea estas tablas, todas con UUID de PostgreSQL:

| Tabla | Campos y restricciones principales |
| --- | --- |
| `countries` | `id`, `iso_code` único, `geonames_id` único opcional, coordenadas de referencia opcionales |
| `country_name_translations` | `country_id`, `language_id`, `name`, clave primaria compuesta |
| `regions` | `id`, `country_id`, `geonames_id`, `admin_code`, coordenadas de referencia, unicidad por país y código administrativo |
| `region_name_translations` | `region_id`, `language_id`, `name`, clave primaria compuesta |
| `cities` | `id`, `country_id`, `region_id`, `geonames_id` único, latitud, longitud, población opcional |
| `city_name_translations` | `city_id`, `language_id`, `name`, clave primaria compuesta |

Los textos de las tablas base no se usan como identidad visible. El nombre se resuelve por `language_id`, con español como respaldo, igual que el catálogo existente.

Se añadirán índices para `countries.iso_code`, `regions.country_id`, `cities.region_id`, `cities.country_id`, claves GeoNames y búsqueda normalizada por traducción. El importador debe preservar una clave GeoNames estable para repetirse sin crear duplicados.

### 2.2 Users y preferencias

La migración sustituye los textos de residencia por:

- `users.residence_country_id UUID REFERENCES countries(id)`.
- `users.residence_region_id UUID REFERENCES regions(id)`.
- `users.residence_city_id UUID REFERENCES cities(id)`.

`residence_latitude`, `residence_longitude`, `current_latitude`, `current_longitude` y `last_location_updated_at` permanecen en `users`.

Se crea `user_search_preferences` con:

- `user_id UUID PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE`.
- `search_radius_km INT NOT NULL DEFAULT 40`, validado entre 10 y 800.
- `search_scope VARCHAR(20) NOT NULL DEFAULT 'radius_residence'` con restricción de valores permitidos.
- `search_target_country_id UUID NULL REFERENCES countries(id)`.
- `search_match_live_location BOOLEAN NOT NULL DEFAULT FALSE`.
- `created_at` y `updated_at`.

Las cuatro columnas de preferencias dejan de existir en `users`. La referencia objetivo de Pasaporte será `search_target_country_id`, no un código ni un texto.

### 2.3 Migración y seed reproducibles

1. Añadir migración incremental que cree catálogo, referencias y preferencias.
2. Incorporar al backend los datos fuente GeoNames y un importador determinista, con versión y atribución documentadas.
3. Ejecutar el importador antes de resolver perfiles heredados.
4. Insertar una fila de preferencias idempotente para cada usuario existente.
5. Asociar datos heredados solo si el ISO de país y la cadena país, región, ciudad dan un único resultado normalizado. La comparación nunca se realiza por un nombre traducido sin país.
6. Para asociaciones ambiguas o no encontradas, dejar las tres referencias nulas y conservar las coordenadas existentes. El cliente pedirá reseleccionar residencia.
7. Eliminar los textos y preferencias antiguos de `users` al final de la misma migración coordinada.
8. Actualizar `db.sql`, `seed.js` y `reset-db.js` para que una base limpia alcance el mismo esquema y datos que una migrada.

El importador no se ejecutará al arrancar el servidor. Quedará como paso explícito de `db:seed` y como requisito operativo del despliegue.

## 3. Contrato API y backend

### 3.1 Catálogo

Se añade un recurso autenticado de catálogo geográfico con consultas dependientes:

- `GET /catalog/geography/countries`
- `GET /catalog/geography/countries/:countryId/regions`
- `GET /catalog/geography/regions/:regionId/cities?query=`

Las respuestas incluyen `id` y `name` localizado. País incluye `iso_code` para interoperabilidad visual, pero Android persiste y reenvía el UUID. Las consultas reciben `Accept-Language`, usan las traducciones solicitadas y recurren a español.

### 3.2 Perfil

`PUT /user` acepta `residence_country_id`, `residence_region_id`, `residence_city_id`, `residence_latitude` y `residence_longitude`.

El controlador valida en transacción que ciudad pertenece a región y país. Las coordenadas se actualizan como una pareja coherente: ambas nulas para borrar o ambas válidas para establecer. No se aceptan coordenadas de una ciudad distinta de la referencia recibida. La validación compara el punto enviado contra la ciudad canónica con un umbral documentado en código, evitando guardar un punto de otra localidad por un resultado erróneo.

El perfil y los perfiles públicos devuelven referencias `residence_country_id`, `residence_region_id` y `residence_city_id`, junto con sus etiquetas localizadas. Los DTO públicos mantienen la privacidad de coordenadas de terceros.

### 3.3 Preferencias y feed

`PUT /user/settings/search` conserva nombre de ruta y cuerpo funcional, pero acepta `search_target_country_id` y guarda mediante `INSERT ... ON CONFLICT (user_id) DO UPDATE` en `user_search_preferences`.

`GET /user` y el feed obtienen preferencias por `LEFT JOIN user_search_preferences`, usando valores por defecto solo para filas que aún no existan durante la migración transaccional. Los modos `country` y `specific_country` comparan IDs de `countries`. Los modos de radio conservan las coordenadas de `users` y la fórmula geodésica aprobada.

Todas las consultas de perfil y feed cambian a `JOIN` localizado para proyectar etiquetas, sin usar nombres como condición de negocio. Se actualizan Zod, DTOs, OpenAPI y documentación API.

## 4. Android y demo

### 4.1 Modelos y red

- Añadir modelos de dominio serializables `GeographicCountry`, `GeographicRegion` y `GeographicCity` con UUID, etiqueta y los atributos necesarios para la UI.
- Sustituir textos y código de residencia en `User`, `UserDto`, mapeadores, `UserViewEntry` y estado de edición por referencias más etiquetas separadas.
- Cambiar `UpdateUserProfileUseCase`, repositorios, datasource Retrofit y request multipart para enviar los tres UUID y las coordenadas.
- Cambiar `UpdateSearchSettingsRequest` para enviar el UUID del país objetivo.
- Añadir `GeographyApiService`, datasource y repositorio para recuperar catálogo remoto localizado.

### 4.2 Selector y persistencia de coordenadas

- Reemplazar `Locale` y `Geocoder` en `ResidenceSelector` por resultados del repositorio de catálogo.
- El selector pide países, luego regiones del país y ciudades de la región. Una selección descendente invalida selecciones dependientes y coordenadas anteriores.
- Una ciudad suministra las coordenadas canónicas del catálogo. El `ViewModel` las guarda en el borrador y el guardado de perfil las persiste junto con los IDs.
- Al cargar un perfil canónico sin coordenadas, se toma la coordenada de la ciudad devuelta por catálogo y se intenta una actualización parcial segura. Si falla, no se modifica el centro mostrado ni se inventa un origen: se presenta estado de residencia incompleta y acción de reintento o reselección.
- El minimapa usa las coordenadas del perfil persistido o de la ciudad seleccionada pendiente de guardar. No geocodifica etiquetas.

### 4.3 Demo Room

- Añadir entidades y DAOs Room para países, regiones, ciudades, traducciones y preferencias de búsqueda por usuario, con migración de base demo si la versión lo requiere.
- Sembrar datos demo con IDs iguales o equivalentes al catálogo remoto para Palma, Francia, París y los perfiles existentes.
- `DemoUserRepositoryImpl` y `DemoSwipeRepositoryImpl` trabajan con referencias de país y coordenadas de ciudad. No dependen de coincidencia por texto ni del `Geocoder`.

### 4.4 Estados y ciclo de vida

- `ProfileViewModel` mantiene identificadores, etiquetas y coordenadas en `StateFlow` y `SavedStateHandle` donde corresponda.
- El catálogo se recarga al cambiar dependencia y conserva la selección durante rotación. Si no hay red, se conserva el último estado local y se ofrece reintento en lugar de limpiar la residencia.
- El guardado no confirma hasta que perfil y coordenadas se hayan persistido en remoto. Un fallo conserva el borrador para volver a intentarlo.

## 5. Pruebas

### Backend

- Pruebas de migración PostgreSQL: tabla de preferencias uno a uno, datos heredados inequívocos, ambiguos y no encontrados.
- Pruebas del importador: repetibilidad, unicidad GeoNames, jerarquía país-región-ciudad y cobertura de traducción español e inglés.
- Pruebas de rutas: catálogo localizado, validación de jerarquía de residencia, pares de coordenadas y UUID inválidos.
- Pruebas de feed: `Todo mi país` y Pasaporte comparan IDs, no etiquetas. Radio usa coordenadas persistidas de residencia.
- Pruebas de regresión: un perfil cuyo mapa se corrige por ciudad recibe las coordenadas persistidas antes de calcular su feed.
- Ejecutar `npm run lint`, `npm test`, `npm run check:routes` y, con PostgreSQL desechable, `npm run test:database`.

### Android

- Tests de mapeador y serialización para IDs, etiquetas y requests de perfil y preferencias.
- Tests de `ProfileViewModel`: selección dependiente, invalidación de coordenadas, guardado atómico y restauración de borrador.
- Tests de repositorio demo: país por UUID, radio por coordenadas de ciudad y ausencia de coincidencia textual.
- Tests de `SearchPreferencesViewModel`: país objetivo UUID y carga de coordenadas de residencia canónica.
- Ejecutar `compileDebugKotlin`, tests unitarios focalizados y `testDebugUnitTest`. Si cambia la UI del selector, verificar en dispositivo o emulador la selección, rotación y estados de error.

## 6. Despliegue coordinado

1. Realizar copia de seguridad de producción.
2. Publicar una versión Android que pueda ser marcada como mínima obligatoria durante la ventana coordinada.
3. Detener o poner en mantenimiento el backend antiguo para evitar escrituras contra columnas eliminadas.
4. Desplegar código backend, ejecutar `npm run db:migrate`, ejecutar `npm run db:seed` para importar catálogo y perfiles demo, y arrancar backend nuevo.
5. Activar la versión mínima Android y publicar la aplicación nueva.
6. Verificar cuentas migradas, perfil, selección de residencia, modos país y los dos modos radio con perfiles conocidos.

Nunca se ejecutará `db:reset` sobre producción.

## 7. Riesgos y mitigaciones

| Riesgo | Mitigación |
| --- | --- |
| Dataset global grande | Versionar fuente comprimida y salida normalizada, usar importador por lotes e índices después de carga. |
| Nombres de ciudad ambiguos | Requerir cadena completa de IDs y migrar automático solo si hay un único resultado. |
| Coordenadas incorrectas | Usar coordenada canónica de ciudad y validar proximidad de cualquier par recibido. |
| Cliente antiguo | Despliegue coordinado con versión mínima obligatoria, sin aceptar escritura legacy. |
| Mapa y feed vuelven a divergir | Una única fuente persistida de coordenadas de residencia y pruebas extremo a extremo. |
| Falta de red al editar | Conservar borrador y catálogo en Room, no interpretar textos localmente ni confirmar cambios no sincronizados. |

## 8. Siguiente paso SDMD

El plan queda aprobado. La implementación se realizará mediante el checklist secuencial de `tasks.md`.
