# [APROBADO] Plan Tecnico de Arquitectura: Correccion Integral de Busqueda Geografica

- **Especificacion funcional asociada**: `docs/features/location_search_correctness/spec.md`
- **Estado**: [APROBADO]
- **Fecha**: 2026-09-21
- **Modulos**: `:app` (`com.feryaeljustice.mirailink`) y `MiraiLink-Backend` (`Express 5 + PostgreSQL`)

- - -

## 1. Hechos Verificados en los Proyectos

### 1.1. Android

- Kotlin `2.4.10`, Java 17, AGP `9.4.1`, KSP `2.3.8`.
- Compile SDK `37`, target SDK `37`, min SDK `26`.
- Compose BOM `2026.08.00`, Navigation 3 `1.1.7`, Koin BOM `4.2.2`.
- Room `2.8.5`, DataStore `1.2.1`, Retrofit `3.0.0`, OkHttp `5.5.0`.
- El proyecto ya usa `LocationManager`, permisos fine y coarse y solicitudes de ubicacion en contexto.
- `SendLocationPingUseCase` existe y esta registrado en Koin, pero no tiene consumidores.
- `SearchPreferencesRepositoryImpl` persiste primero en DataStore y despues llama al backend. Un fallo remoto puede dejar una preferencia local que no coincide con el servidor.
- `HomeViewModel` no observa cambios de preferencias.
- La edicion de residencia no propaga `residence_latitude` ni `residence_longitude` en la peticion multipart.

### 1.2. Backend

- Node.js `>=22`, Express `5.2.1`, PostgreSQL mediante `pg 8.23.0`, Zod `4.6.5`, Vitest `5.0.1`.
- La tabla `users` ya contiene residencia, ubicacion activa, `last_location_updated_at` y preferencias de busqueda.
- El contrato backend usa `radius`, `country`, `world` y `specific_country`.
- El cliente Android genera actualmente `radius`, `my_country`, `world` y `specific_country` mediante `enum.name.lowercase()`.
- El controlador del feed interpola coordenadas del buscador dentro del SQL. La correccion debe usar parametros PostgreSQL y expresiones SQL fijas.
- El endpoint de perfil propio no proyecta coordenadas ni `last_location_updated_at`.
- No hay tests especificos para el filtrado geografico del feed.

- - -

## 2. Contrato Geografico Unico

### 2.1. Valores de alcance en red

| Dominio Android | Valor de red y base de datos | Regla |
| --- | --- | --- |
| `RADIUS` | `radius` | Filtro duro por kilometros |
| `MY_COUNTRY` | `country` | Pais de residencia del buscador |
| `WORLD` | `world` | Sin exclusion geografica |
| `SPECIFIC_COUNTRY` | `specific_country` | Pais de residencia seleccionado |

- `SearchScope` tendra conversion explicita `toWireValue()` y `fromWireValue()`.
- Se eliminara la dependencia de `name.lowercase()` y `valueOf()` para el contrato remoto.
- La lectura local aceptara temporalmente `my_country` para migrar preferencias ya guardadas, pero toda escritura nueva usara `country`.

### 2.2. Seleccion de coordenadas

La misma funcion conceptual se aplicara al buscador y a cada candidato:

```text
si matchLiveLocation esta activo
  y currentLatitude/currentLongitude existen
  y lastLocationUpdatedAt >= ahora menos 24 horas
    usar ubicacion activa
si no
    usar coordenadas de residencia
```

- Con `matchLiveLocation` desactivado solo se usan coordenadas de residencia.
- No se mezclara latitud de una fuente con longitud de otra.
- No se usaran coordenadas por defecto de Palma para usuarios reales.
- La distancia se calcula con Haversine y radio terrestre de 6371 km.
- Los argumentos del buscador y el radio se pasan como parametros PostgreSQL.
- Las expresiones que eligen columnas del candidato son constantes internas, nunca entrada del cliente.

### 2.3. Regla por alcance

- `radius`: requiere origen util. Incluye candidatos con distancia menor o igual al radio y candidatos cuya geografia no puede verificarse. Estos ultimos llevan `distance_km = null` y menor prioridad.
- `country`: requiere `residence_country_code` del buscador. Incluye el mismo pais y candidatos sin pais de residencia como excepcion de menor prioridad. Excluye paises conocidos diferentes.
- `specific_country`: requiere un pais objetivo ISO de dos letras. Incluye ese pais y candidatos sin pais de residencia como excepcion de menor prioridad. Excluye paises conocidos diferentes.
- `world`: incluye todos los candidatos. Calcula distancia cuando existe origen y fuente util para el candidato.
- El radio no participa en `country`, `specific_country` ni `world`.

### 2.4. Ordenacion

El CTE del feed producira `geography_known`, `distance_km`, `common_interests` e `is_traveler`.

1. Candidatos que cumplen de forma verificable el alcance.
2. Puntuacion existente por intereses y proximidad.
3. Factor aleatorio y fecha de creacion como desempates.
4. Candidatos con geografia desconocida al final, aunque tengan muchos intereses comunes.

`is_traveler` se calculara cuando residencia y ubicacion activa valida disten mas de 10 km. No dependera de que el buscador haya activado el switch, porque describe al candidato y no el filtro elegido.

- - -

## 3. Impacto en Backend

### 3.1. Validacion y errores de dominio

- Mantener el esquema de guardado con los cuatro valores canonicos.
- Incorporar un esquema de query del feed que combine paginacion con overrides admitidos, o retirar overrides si no tienen consumidor legitimo. La implementacion no aceptara valores arbitrarios sin Zod.
- Responder con un error operativo `422 LOCATION_REQUIRED` cuando `radius` no tenga origen util.
- Responder con `422 RESIDENCE_COUNTRY_REQUIRED` cuando `country` no tenga pais de residencia.
- `specific_country` sin pais objetivo seguira siendo error de validacion.

### 3.2. Perfil propio y privacidad

- `GET /user` añadira exclusivamente para el usuario autenticado:
  - `residence_latitude`
  - `residence_longitude`
  - `current_latitude`
  - `current_longitude`
  - `last_location_updated_at`
- Las respuestas publicas de feed y perfil ajeno no expondran coordenadas ni marcas temporales.
- `distance_km` e `is_traveler` seguiran pasando por la lista blanca publica.

### 3.3. Controlador de feed

- Extraer helpers puros para resolver alcance, origen, fuente SQL fija y parametros.
- Reescribir la consulta para que todos los valores variables sean parametros.
- Comprobar la vigencia de la ubicacion activa con `last_location_updated_at >= NOW() - INTERVAL '24 hours'`.
- Aplicar la excepcion de geografia desconocida sin permitir que un pais conocido incorrecto atraviese filtros de pais.
- Mantener exclusiones de cuenta propia, likes y dislikes.
- Conservar la carga localizada de intereses y fotografias despues de resolver los candidatos.

### 3.4. Ubicacion y residencia

- `POST /user/location/ping` seguira actualizando coordenadas activas y `last_location_updated_at`.
- La regla historica de 5 km o 24 horas y el maximo de 50 puntos se mantiene.
- `PUT /user` recibira y validara las coordenadas de residencia que ya soporta el controlador.
- No se requiere una migracion de base de datos nueva porque las columnas necesarias ya existen.

### 3.5. Documentacion backend

- Actualizar `docs/openapi.yaml` con respuestas 422 y campos privados del perfil propio.
- Actualizar `docs/api-reference.md`, `docs/code-reference.md` y `docs/database.md` donde describan el feed, las fuentes geograficas o la vigencia.
- Mantener la especificacion funcional canonica en el repositorio Android y enlazarla desde la documentacion backend para evitar dos contratos divergentes.

- - -

## 4. Impacto en Android

### 4.1. Dominio y modelos

- Añadir `lastLocationUpdatedAt` al modelo propio solo si la UI o la seleccion local de fuente lo necesita.
- Implementar el mapeo explicito de `SearchScope`.
- Mantener `SearchPreferences` como flujo reactivo desde DataStore.
- Añadir errores de presentacion localizados para `LOCATION_REQUIRED` y `RESIDENCE_COUNTRY_REQUIRED`.

### 4.2. Guardado consistente

- En modo online, guardar primero en backend y actualizar DataStore solo tras respuesta satisfactoria.
- En modo demo, guardar directamente en DataStore.
- Si backend falla, conservar tanto el valor guardado anterior como el borrador visible para permitir reintento.
- Limpiar `targetCountryCode` al guardar un alcance diferente de `SPECIFIC_COUNTRY`, evitando que un valor obsoleto influya en futuras peticiones.

### 4.3. Recarga reactiva de Home

- `HomeViewModel` observara `GetSearchPreferencesUseCase` con `distinctUntilChanged()`.
- La primera emision inicializa la observacion sin provocar una segunda carga redundante.
- Cada cambio guardado posterior invalida `_userQueue`, reinicia el estado pertinente y ejecuta `loadUsers()`.
- Como el ViewModel permanece en la pila de Navigation 3, la recarga puede comenzar al guardar y Home mostrara el mazo nuevo al volver.
- Se añadira proteccion frente a cargas concurrentes para que refresh manual, retorno de preferencias y arranque no dupliquen peticiones ni permitan que una respuesta antigua sobrescriba una nueva.

### 4.4. Captura y envio de ubicacion activa

- Crear una abstraccion Android testeable para obtener una sola ubicacion de primer plano usando los proveedores disponibles y la ubicacion conocida mas reciente como respaldo.
- Solicitar permisos solo desde una accion visible y contextual. No abrir dialogos de permiso automaticamente al iniciar la app.
- Cuando el permiso ya este concedido y la sesion este autenticada y verificada, intentar un ping al entrar en Home y al volver la app a primer plano.
- Limitar los intentos dentro de una misma sesion para evitar llamadas repetidas por recomposicion o navegacion.
- El boton existente de actualizar ubicacion en preferencias actualizara el minimapa y enviara el ping remoto cuando proceda.
- La geocodificacion inversa de ciudad y pais es auxiliar. El filtro depende de coordenadas, no del texto devuelto por `Geocoder`.
- No se solicitaran actualizaciones continuas ni permiso de ubicacion en segundo plano.

### 4.5. Residencia con coordenadas

- Extender el estado de edicion de perfil para conservar latitud y longitud de la sugerencia o geocodificacion elegida.
- Propagar esas coordenadas por ViewModel, caso de uso, repositorio, datasource y multipart de `UserApiService`.
- Una ciudad escrita pero no resuelta no inventara coordenadas. La UI indicara que debe elegirse o confirmarse una sugerencia util.
- Conservar la jerarquia pais, region y ciudad ya implementada.

### 4.6. Estado sin origen y UX

- Extender `HomeUiState` con un estado especifico de requisito geografico o mapear de forma tipada los errores 422.
- Para `LOCATION_REQUIRED`, mostrar acciones para conceder permiso o abrir la edicion de residencia.
- Para `RESIDENCE_COUNTRY_REQUIRED`, mostrar una accion para configurar residencia.
- No sustituir estos estados por una lista vacia generica.
- Mantener objetivos tactiles de al menos 48 dp, contenido localizado y soporte claro y oscuro.

### 4.7. Modo demo

- Aplicar la misma seleccion simetrica de fuente, vigencia de 24 horas, excepciones de datos ausentes y prioridad geografica.
- Introducir un reloj inyectable o timestamps deterministas para probar ubicaciones activas vigentes y caducadas.
- El modo demo no solicita ni envia ubicacion real del dispositivo.

- - -

## 5. Contratos Reactivos y Concurrencia

```text
SearchPreferencesScreen
  -> SaveSearchPreferencesUseCase
  -> SearchPreferencesRepository
      online: backend correcto -> DataStore actualizado
      demo: DataStore actualizado
  -> Flow<SearchPreferences>
  -> HomeViewModel observa cambio
  -> cancela o invalida carga anterior
  -> GetFeedUseCase
  -> feed nuevo
```

- El flujo de preferencias es la unica fuente de invalidacion y evita callbacks globales o flags de navegacion consumibles una sola vez.
- Las operaciones de red y ubicacion se ejecutan fuera del hilo principal.
- Los trabajos viven en `viewModelScope` o en un coordinador ligado al lifecycle de primer plano.
- La respuesta de una peticion antigua no puede reemplazar un feed solicitado con preferencias mas nuevas.

- - -

## 6. Estrategia de Testing

### 6.1. Backend con Vitest

- Helper de distancia con Toulouse y Paris para demostrar que supera 100 km.
- `radius` incluye el borde exacto y excluye distancias superiores.
- `radius` usa residencia cuando el modo activo esta apagado.
- Modo activo usa coordenadas actuales vigentes de ambos lados.
- Modo activo recurre a residencia si la marca temporal supera 24 horas.
- Candidato sin coordenadas aparece al final con distancia nula.
- `country` incluye Francia, excluye España e ignora el radio.
- `country` devuelve `RESIDENCE_COUNTRY_REQUIRED` si falta el pais del buscador.
- `specific_country` incluye Toulouse y Paris al elegir Francia, sin aplicar radio.
- `world` no excluye y calcula distancia cuando es posible.
- Perfil propio expone coordenadas privadas, perfil ajeno y feed no.
- SQL usa parametros y conserva exclusiones de swipes previos.

### 6.2. Android JVM

- Conversion bidireccional `MY_COUNTRY <-> country` y migracion de `my_country` local.
- Repositorio online no actualiza DataStore si falla backend.
- Guardado correcto actualiza una sola vez y emite preferencias nuevas.
- `HomeViewModel` recarga al cambiar preferencias sin refresh manual.
- Dos invalidaciones rapidas no permiten que una respuesta antigua gane.
- Errores `LOCATION_REQUIRED` y `RESIDENCE_COUNTRY_REQUIRED` producen estados accionables.
- Propagacion completa de coordenadas de residencia al multipart.
- Reglas demo para Toulouse, Paris, Mallorca, ubicacion caducada y candidato desconocido.

### 6.3. Android instrumentado y recorrido manual

- Denegar permiso y verificar que residencia sigue funcionando.
- Sin permiso ni residencia, verificar el estado accionable de Radio local.
- Con permiso concedido, actualizar ubicacion y comprobar el ping.
- Guardar 50 km desde Toulouse y confirmar que Paris desaparece automaticamente al volver.
- Cambiar a Pasaporte Francia y confirmar que Toulouse y Paris pueden aparecer.
- Verificar rotacion, segundo plano, retorno a primer plano, tema claro y oscuro.
- Verificar que no hace falta swipe-to-refresh despues del guardado.

### 6.4. Comandos de verificacion previstos

Android:

```powershell
.\gradlew.bat testDebugUnitTest
.\gradlew.bat assembleDebug
.\gradlew.bat lintDebug
```

Backend:

```powershell
npm test
npm run lint
npm run check:routes
```

- Los tests de base de datos solo se ejecutaran contra una base desechable confirmada.
- No se ejecutara `db:reset` durante esta correccion.

- - -

## 7. Riesgos y Mitigaciones

- **Ubicacion aproximada**: coarse puede desplazar el punto varios kilometros. Se acepta la precision del sistema y no se promete exactitud inferior a la reportada.
- **Reloj de servidor**: la vigencia de 24 horas se evalua en PostgreSQL para evitar depender del reloj del telefono.
- **Carreras de recarga**: cancelar o versionar solicitudes de feed y aceptar solo la generacion mas reciente.
- **Privacidad**: coordenadas solo en perfil propio autenticado y nunca en DTO publico.
- **Persistencia dividida**: backend primero y DataStore despues evita confirmar localmente algo que el servidor rechazo.
- **Datos legados**: aceptar `my_country` unicamente al leer almacenamiento local, migrandolo a `country` en la siguiente escritura.
- **Perfiles sin coordenadas**: se incluyen al final, pero nunca reciben una distancia ficticia ni se presentan como coincidencia geografica verificada.
- **Cambios previos del backend**: la rama correctiva parte del `HEAD` de `codex/proxy-credential-manager`; antes de cualquier futura integracion se revisara su base y dependencias sin reescribir esos cambios.

- - -

## 8. Orden de Implementacion Propuesto

1. Tests y helpers puros del contrato geografico backend.
2. Reescritura parametrizada del feed y errores tipados.
3. Contrato privado de perfil propio y documentacion OpenAPI.
4. Mapeo canonico de alcances y guardado consistente Android.
5. Coordenadas de residencia extremo a extremo.
6. Proveedor y sincronizacion de ubicacion activa en primer plano.
7. Invalidacion reactiva y control de concurrencia en Home.
8. Paridad del modo demo.
9. Estados UI accionables y recursos localizados.
10. Suites completas y validacion en emulador o dispositivo.

- - -

## 9. Bloqueo SDMD

No se creara `tasks.md` ni se modificara codigo de produccion hasta recibir aprobacion explicita de este plan tecnico.
