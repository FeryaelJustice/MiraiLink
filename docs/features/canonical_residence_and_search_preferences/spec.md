# [APROBADO] Especificacion Funcional: Residencia Canonica y Preferencias de Busqueda Separadas

- **Fecha**: 2026-09-21
- **Estado**: [APROBADO]
- **Autor / Responsable**: Codex con validacion del propietario de MiraiLink
- **Modulos afectados**: `:app` (`com.feryaeljustice.mirailink`) y `MiraiLink-Backend` (`Express 5 + PostgreSQL`)
- **Especificaciones relacionadas**: `location_search_correctness` y `search_radius_modes_and_chat_list_repair`

## 1. Problema y objetivo

La residencia se guarda hoy como textos libres (`residence_country_code`, `residence_region`, `residence_city`) dentro de `users`. El selector Android los obtiene del `Geocoder`, por lo que una misma localidad puede tener nombres distintos segun idioma, dispositivo o proveedor. El mapa puede resolver localmente una ciudad sin coordenadas, pero ese resultado no se persiste, mientras que el backend calcula el feed solo con coordenadas almacenadas. Como consecuencia, mapa y feed pueden utilizar orígenes distintos.

El objetivo es establecer una residencia canonica por identificadores, con nombres traducibles, separar las preferencias de busqueda de la identidad del usuario y persistir de forma segura las coordenadas de residencia seleccionadas. El mapa, Android demo y backend deben usar el mismo origen de residencia para el radio.

## 2. Situacion actual verificada

1. La migracion `003_user_location_and_search_settings.sql` incorpora a `users` los textos de residencia, coordenadas de residencia y ubicacion activa, y las cuatro preferencias de busqueda.
2. `users` conserva ya `residence_latitude`, `residence_longitude`, `current_latitude`, `current_longitude` y `last_location_updated_at`.
3. `ResidenceSelector` obtiene pais, region y ciudad mediante `Locale` y `Geocoder`; al elegir una ciudad obtiene coordenadas.
4. Android envia actualmente pais, region, ciudad y coordenadas en la actualizacion de perfil. El contrato remoto devuelve esos textos junto con las preferencias de busqueda.
5. El minimapa puede geocodificar un texto de residencia que carece de coordenadas para representarlo, pero esa correccion local no actualiza el perfil remoto. El endpoint del feed usa coordenadas persistidas y no geocodifica textos.
6. La API de preferencias se guarda mediante `PUT /user/settings/search`; el backend lee esos campos directamente desde `users` para generar el feed.
7. El modo demo conserva un modelo de usuario equivalente en Room y debe mantener la misma semantica sin red.

## 3. Alcance

### 3.1 Dentro del alcance

- Persistir las coordenadas devueltas al seleccionar una ciudad de residencia, de modo que el perfil remoto y el minimapa compartan origen.
- Reparar perfiles con residencia textual y coordenadas ausentes mediante la estrategia de migracion aprobada.
- Crear un catalogo relacional de paises, regiones y ciudades con identificadores estables y tablas de traducciones.
- Sustituir los textos de residencia de `users` por referencias a ese catalogo, conservando en `users` las coordenadas de residencia.
- Crear una tabla uno a uno de preferencias de busqueda por usuario para `search_match_live_location`, `search_target_country`, `search_scope` y `search_radius_km`.
- Migrar los datos existentes, actualizar seed y reset de base de datos, modelos, consultas, validaciones, DTOs, Android online y demo.
- Mantener la pantalla de perfil localizada: cada persona ve nombres en el idioma de su aplicacion, mientras servidor y filtros trabajan con identificadores.
- Mantener los contratos geograficos ya aprobados: los modos radio usan coordenadas y los modos por pais comparan residencia canonica.
- Incluir pruebas de migracion, contratos API, seleccion de residencia y paridad del filtro demo y remoto.

### 3.2 Fuera del alcance

- Seguimiento GPS continuo o en segundo plano.
- Exponer coordenadas exactas de otros usuarios.
- Cambiar los cinco modos de busqueda, sus radios o la visualizacion ya aprobada, salvo lo imprescindible para consumir el nuevo modelo.
- Crear un editor administrativo de catalogos geograficos.
- Resolver una direccion concreta o barrio como una nueva entidad distinta de ciudad en esta entrega.

## 4. Contrato funcional propuesto

### 4.1 Residencia canonica

- Cada perfil guarda referencias de pais, region y ciudad, no etiquetas localizadas como fuente de verdad.
- Una ciudad pertenece a una region y una region pertenece a un pais. El backend valida que las tres referencias seleccionadas formen esa cadena.
- Cada entidad tiene un identificador interno estable y traducciones separadas por idioma.
- La API devuelve los identificadores de residencia y las etiquetas resueltas para el idioma solicitado, de modo que Android no tenga que inferir identidad geografica a partir de un texto mostrado.
- Los filtros `Todo mi pais` y `Pasaporte` usan el identificador del pais, nunca el nombre traducido ni la ubicacion activa.

### 4.2 Coordenadas de residencia

- Al elegir una ciudad valida, Android conserva las coordenadas obtenidas y las envía al guardar el perfil.
- Si un perfil heredado tiene ciudad valida y coordenadas ausentes, la aplicacion no debe usar una coordenada local solo para el minimapa sin sincronizarla. La estrategia definitiva de reparacion queda pendiente de aprobacion.
- El radio desde residencia usa exclusivamente `residence_latitude` y `residence_longitude` de la cuenta que busca y de los candidatos, conforme al contrato previo.
- La ubicacion actual y su fecha de actualizacion siguen en `users` y no forman parte del catalogo de residencia.

### 4.3 Preferencias de busqueda

- Existe exactamente una fila de preferencias por usuario, creada junto con la cuenta o garantizada de forma idempotente para cuentas existentes.
- Esa fila contiene radio en kilometros, alcance, pais objetivo cuando corresponda y la preferencia de coincidencia por ubicacion activa.
- El endpoint de actualizacion de preferencias conserva su comportamiento funcional, pero lee y escribe la tabla dedicada.
- El feed une las preferencias del usuario que busca y las referencias canonicas de residencia de los candidatos.

### 4.4 Experiencia Android y modo demo

- El selector sigue el flujo pais, region, ciudad, pero opera con opciones identificadas y etiquetas localizadas.
- El estado de edicion conserva identificadores y etiquetas separadas para sobrevivir a rotacion y a cambios de idioma.
- En modo demo, Room conserva los mismos identificadores y coordenadas y aplica los mismos filtros sin depender del `Geocoder` ni de red.
- Si faltan coordenadas necesarias para el radio desde residencia, se comunica como configuracion incompleta. No se muestra un mapa aparentemente valido con un feed vacio sin explicacion.

## 5. Casuisticas mobile

- **Sin red**: el modo demo funciona con el catalogo local. En modo online, guardar residencia o preferencias no se confirma hasta recibir respuesta correcta. Se muestra error recuperable y se conservan las selecciones del formulario.
- **Rotacion y proceso**: los identificadores, etiquetas y coordenadas del borrador viven en el `ViewModel` y se restauran sin volver a interpretar etiquetas por coincidencia textual.
- **Permisos**: el permiso de ubicacion actual no es necesario para editar residencia. La residencia procede de una seleccion explicita y sus coordenadas asociadas.
- **Idiomas**: cambiar idioma modifica la etiqueta mostrada, no la residencia guardada ni los resultados del filtro por pais.
- **Privacidad**: solo se persisten y usan coordenadas propias para filtros autorizados. Las respuestas de otros perfiles siguen exponiendo distancia derivada, no sus coordenadas.
- **Accesibilidad**: las sugerencias mantienen objetivo tactil minimo de 48 dp, contenido legible en tema claro y oscuro, y los campos respetan `imePadding()`.

## 6. Criterios de aceptacion

### Criterio 1: Mismo origen para mapa y feed

- **Dado que**: una persona selecciona una ciudad de residencia y Android obtiene sus coordenadas.
- **Cuando**: guarda el perfil y selecciona `Radio desde mi residencia`.
- **Entonces**: el perfil remoto guarda esas coordenadas y mapa y backend usan ese mismo punto para el radio.

### Criterio 2: Identidad independiente del idioma

- **Dado que**: dos dispositivos usan idiomas distintos.
- **Cuando**: ambos muestran la residencia de un mismo perfil.
- **Entonces**: cada uno ve la traduccion disponible en su idioma, mientras los identificadores persistidos y los resultados de pais son iguales.

### Criterio 3: Filtro de pais por referencia canonica

- **Dado que**: un usuario tiene residencia en Francia y hay candidatos residentes en Francia y Espana.
- **Cuando**: selecciona `Todo mi pais`.
- **Entonces**: el feed incluye solo candidatos con la misma referencia de pais, sin comparar nombres localizados ni radio.

### Criterio 4: Preferencias aisladas de users

- **Dado que**: existe una cuenta nueva o migrada.
- **Cuando**: consulta o modifica sus preferencias de busqueda.
- **Entonces**: existe una unica fila relacionada con ese usuario y `users` no contiene columnas de preferencias de busqueda activas.

### Criterio 5: Perfil heredado incompleto

- **Dado que**: un perfil existente conserva textos de residencia pero no coordenadas o no puede asociarse de manera inequívoca al catalogo.
- **Cuando**: intenta usar radio desde residencia.
- **Entonces**: recibe el comportamiento aprobado para datos incompletos y nunca se inventa ni sobrescribe una ubicacion silenciosamente.

### Criterio 6: Paridad demo y online

- **Dado que**: existen perfiles equivalentes en backend y en Room demo.
- **Cuando**: se editan residencia y preferencias y se solicita el feed.
- **Entonces**: ambos modos conservan la misma eleccion por identificador, coordenadas y reglas de inclusion geografica.

## 7. Decisiones cerradas

- [x] El catalogo geografico inicial sera global, versionado y auditable. No dependera del `Geocoder` del dispositivo como fuente de verdad.
- [x] Pais, region y ciudad exponen UUID opacos como referencias. El ISO 3166-1 alpha-2 queda como atributo unico del pais, no como clave primaria publica.
- [x] El seed incorpora traducciones completas en español e inglés, con idioma base como respaldo cuando no exista traduccion solicitada.
- [x] La migracion automatica solo asigna referencias cuando la cadena pais-region-ciudad sea inequívoca. Los demas perfiles deben reseleccionar residencia.
- [x] Android puede persistir automaticamente coordenadas solo para una ciudad canonica sin coordenadas y cuando el resultado confirme inequívocamente esa ciudad. Ante duda o error, no escribe y comunica configuracion incompleta.

## 8. Estrategia de datos y lanzamiento aprobada

- [x] GeoNames es la fuente de datos geograficos global. Sus ficheros se versionaran y se procesaran mediante un importador reproducible del backend.
- [x] El despliegue es coordinado: se aplica la migracion, se publica el backend y se exige una version Android minima que use el contrato nuevo. No se mantienen escrituras legacy ambiguas.

## 9. Siguiente paso SDMD

La especificacion queda aprobada. El siguiente paso es revisar y aprobar el plan tecnico antes de modificar codigo de produccion.
