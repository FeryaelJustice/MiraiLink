# [APROBADO] Plan Técnico de Arquitectura: Corrección Integral de Búsqueda Geográfica

- **Especificación funcional asociada**: `docs/features/location_search_correctness/spec.md`
- **Estado**: [APROBADO]
- **Fecha**: 2026-09-21
- **Módulos**: `:app` (`com.feryaeljustice.mirailink`) y `MiraiLink-Backend` (`Express 5 + PostgreSQL`)

- - -

## 1. Hechos Verificados en los Proyectos

### 1.1. Android

- Kotlin `2.4.10`, Java 17, AGP `9.4.1`, KSP `2.3.8`.
- Compile SDK `37`, target SDK `37`, min SDK `26`.
- Compose BOM `2026.08.00`, Navigation 3 `1.1.7`, Koin BOM `4.2.2`.
- Room `2.8.5`, DataStore `1.2.1`, Retrofit `3.0.0`, OkHttp `5.5.0`.
- El proyecto ya usa `LocationManager`, permisos fine y coarse y solicitudes de ubicación en contexto.
- `SendLocationPingUseCase` existe y está registrado en Koin, pero no tiene consumidores.
- `SearchPreferencesRepositoryImpl` persiste primero en DataStore y después llama al backend. Un fallo remoto puede dejar una preferencia local que no coincide con el servidor.
- `HomeViewModel` no observa cambios de preferencias.
- La edición de residencia no propaga `residence_latitude` ni `residence_longitude` en la petición multipart.

### 1.2. Backend

- Node.js `>=22`, Express `5.2.1`, PostgreSQL mediante `pg 8.23.0`, Zod `4.6.5`, Vitest `5.0.1`.
- La tabla `users` ya contiene residencia, ubicación activa, `last_location_updated_at` y preferencias de búsqueda.
- El contrato backend usa `radius`, `country`, `world` y `specific_country`.
- El cliente Android genera actualmente `radius`, `my_country`, `world` y `specific_country` mediante `enum.name.lowercase()`.
- El controlador del feed interpola coordenadas del buscador dentro del SQL. La corrección debe usar parámetros PostgreSQL y expresiones SQL fijas.
- El endpoint de perfil propio no proyecta coordenadas ni `last_location_updated_at`.
- No hay tests específicos para el filtrado geográfico del feed.

- - -

## 2. Contrato Geográfico Único

### 2.1. Valores de alcance en red

| Dominio Android | Valor de red y base de datos | Regla |
| --- | --- | --- |
| `RADIUS` | `radius` | Filtro duro por kilometros |
| `MY_COUNTRY` | `country` | País de residencia del buscador |
| `WORLD` | `world` | Sin exclusión geográfica |
| `SPECIFIC_COUNTRY` | `specific_country` | País de residencia seleccionado |

- `SearchScope` tendrá conversión explícita `toWireValue()` y `fromWireValue()`.
- Se eliminará la dependencia de `name.lowercase()` y `valueOf()` para el contrato remoto.
- La lectura local aceptará temporalmente `my_country` para migrar preferencias ya guardadas, pero toda escritura nueva usará `country`.

### 2.2. Selección de coordenadas

La misma función conceptual se aplicará al buscador y a cada candidato:

```text
si matchLiveLocation está activo
  y currentLatitude/currentLongitude existen
  y lastLocationUpdatedAt >= ahora menos 24 horas
    usar ubicación activa
si no
    usar coordenadas de residencia
```

- Con `matchLiveLocation` desactivado solo se usan coordenadas de residencia.
- No se mezclará latitud de una fuente con longitud de otra.
- No se usarán coordenadas por defecto de Palma para usuarios reales.
- La distancia se calcula con Haversine y radio terrestre de 6371 km.
- Los argumentos del buscador y el radio se pasan como parámetros PostgreSQL.
- Las expresiones que eligen columnas del candidato son constantes internas, nunca entrada del cliente.

### 2.3. Regla por alcance

- `radius`: requiere origen útil. Incluye candidatos con distancia menor o igual al radio y candidatos cuya geografía no puede verificarse. Estos últimos llevan `distance_km = null` y menor prioridad.
- `country`: requiere `residence_country_code` del buscador. Incluye el mismo país y candidatos sin país de residencia como excepción de menor prioridad. Excluye países conocidos diferentes.
- `specific_country`: requiere un país objetivo ISO de dos letras. Incluye ese país y candidatos sin país de residencia como excepción de menor prioridad. Excluye países conocidos diferentes.
- `world`: incluye todos los candidatos. Calcula distancia cuando existe origen y fuente útil para el candidato.
- El radio no participa en `country`, `specific_country` ni `world`.

### 2.4. Ordenación

El CTE del feed producira `geography_known`, `distance_km`, `common_interests` e `is_traveler`.

1. Candidatos que cumplen de forma verificable el alcance.
2. Puntuación existente por intereses y proximidad.
3. Factor aleatorio y fecha de creación como desempates.
4. Candidatos con geografía desconocida al final, aunque tengan muchos intereses comunes.

`is_traveler` se calculará cuando residencia y ubicación activa válida disten más de 10 km. No dependera de que el buscador haya activado el switch, porque describe al candidato y no el filtro elegido.

- - -

## 3. Impacto en Backend

### 3.1. Validación y errores de dominio

- Mantener el esquema de guardado con los cuatro valores canónicos.
- Incorporar un esquema de query del feed que combine paginación con overrides admitidos, o retirar overrides si no tienen consumidor legitimo. La implementación no aceptará valores arbitrarios sin Zod.
- Responder con un error operativo `422 LOCATION_REQUIRED` cuando `radius` no tenga origen útil.
- Responder con `422 RESIDENCE_COUNTRY_REQUIRED` cuando `country` no tenga país de residencia.
- `specific_country` sin país objetivo seguirá siendo error de validación.

### 3.2. Perfil propio y privacidad

- `GET /user` añadira exclusivamente para el usuario autenticado:
  - `residence_latitude`
  - `residence_longitude`
  - `current_latitude`
  - `current_longitude`
  - `last_location_updated_at`
- Las respuestas públicas de feed y perfil ajeno no expondrán coordenadas ni marcas temporales.
- `distance_km` e `is_traveler` seguirán pasando por la lista blanca publica.

### 3.3. Controlador de feed

- Extraer helpers puros para resolver alcance, origen, fuente SQL fija y parámetros.
- Reescribir la consulta para que todos los valores variables sean parámetros.
- Comprobar la vigencia de la ubicación activa con `last_location_updated_at >= NOW() - INTERVAL '24 hours'`.
- Aplicar la excepción de geografía desconocida sin permitir que un país conocido incorrecto atraviese filtros de país.
- Mantener exclusiones de cuenta propia, likes y dislikes.
- Conservar la carga localizada de intereses y fotografías después de resolver los candidatos.

### 3.4. Ubicación y residencia

- `POST /user/location/ping` seguirá actualizando coordenadas activas y `last_location_updated_at`.
- La regla histórica de 5 km o 24 horas y el máximo de 50 puntos se mantiene.
- `PUT /user` recibirá y validará las coordenadas de residencia que ya soporta el controlador.
- No se requiere una migración de base de datos nueva porque las columnas necesarias ya existen.

### 3.5. Documentación backend

- Actualizar `docs/openapi.yaml` con respuestas 422 y campos privados del perfil propio.
- Actualizar `docs/api-reference.md`, `docs/code-reference.md` y `docs/database.md` donde describan el feed, las fuentes geograficas o la vigencia.
- Mantener la especificación funcional canónica en el repositorio Android y enlazarla desde la documentación backend para evitar dos contratos divergentes.

- - -

## 4. Impacto en Android

### 4.1. Dominio y modelos

- Añadir `lastLocationUpdatedAt` al modelo propio solo si la UI o la selección local de fuente lo necesita.
- Implementar el mapeo explícito de `SearchScope`.
- Mantener `SearchPreferences` como flujo reactivo desde DataStore.
- Añadir errores de presentación localizados para `LOCATION_REQUIRED` y `RESIDENCE_COUNTRY_REQUIRED`.

### 4.2. Guardado consistente

- En modo online, guardar primero en backend y actualizar DataStore solo tras respuesta satisfactoria.
- En modo demo, guardar directamente en DataStore.
- Si backend falla, conservar tanto el valor guardado anterior como el borrador visible para permitir reintento.
- Limpiar `targetCountryCode` al guardar un alcance diferente de `SPECIFIC_COUNTRY`, evitando que un valor obsoleto influya en futuras peticiones.

### 4.3. Recarga reactiva de Home

- `HomeViewModel` observará `GetSearchPreferencesUseCase` con `distinctUntilChanged()`.
- La primera emisión inicializa la observación sin provocar una segunda carga redundante.
- Cada cambio guardado posterior invalida `_userQueue`, reinicia el estado pertinente y ejecuta `loadUsers()`.
- Como el ViewModel permanece en la pila de Navigation 3, la recarga puede comenzar al guardar y Home mostrará el mazo nuevo al volver.
- Se añadira protección frente a cargas concurrentes para que refresh manual, retorno de preferencias y arranque no dupliquen peticiones ni permitan que una respuesta antigua sobrescriba una nueva.

### 4.4. Captura y envío de ubicación activa

- Crear una abstracción Android testeable para obtener una sola ubicación de primer plano usando los proveedores disponibles y la ubicación conocida más reciente como respaldo.
- Solicitar permisos solo desde una acción visible y contextual. No abrir diálogos de permiso automáticamente al iniciar la app.
- Cuando el permiso ya este concedido y la sesión este autenticada y verificada, intentar un ping al entrar en Home y al volver la app a primer plano.
- Limitar los intentos dentro de una misma sesión para evitar llamadas repetidas por recomposición o navegación.
- El botón existente de actualizar ubicación en preferencias actualizará el minimapa y enviará el ping remoto cuando proceda.
- La geocodificación inversa de ciudad y país es auxiliar. El filtro depende de coordenadas, no del texto devuelto por `Geocoder`.
- No se solicitarán actualizaciones continuas ni permiso de ubicación en segundo plano.

### 4.5. Residencia con coordenadas

- Extender el estado de edición de perfil para conservar latitud y longitud de la sugerencia o geocodificación elegida.
- Propagar esas coordenadas por ViewModel, caso de uso, repositorio, datasource y multipart de `UserApiService`.
- Una ciudad escrita pero no resuelta no inventará coordenadas. La UI indicará que debe elegirse o confirmarse una sugerencia útil.
- Conservar la jerarquía país, región y ciudad ya implementada.

### 4.6. Estado sin origen y UX

- Extender `HomeUiState` con un estado específico de requisito geográfico o mapear de forma tipada los errores 422.
- Para `LOCATION_REQUIRED`, mostrar acciones para conceder permiso o abrir la edición de residencia.
- Para `RESIDENCE_COUNTRY_REQUIRED`, mostrar una acción para configurar residencia.
- No sustituir estos estados por una lista vacía genérica.
- Mantener objetivos táctiles de al menos 48 dp, contenido localizado y soporte claro y oscuro.

### 4.7. Modo demo

- Aplicar la misma selección simétrica de fuente, vigencia de 24 horas, excepciones de datos ausentes y prioridad geográfica.
- Introducir un reloj inyectable o timestamps deterministas para probar ubicaciones activas vigentes y caducadas.
- El modo demo no solicita ni envía ubicación real del dispositivo.

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

- El flujo de preferencias es la única fuente de invalidación y evita callbacks globales o flags de navegación consumibles una sola vez.
- Las operaciones de red y ubicación se ejecutan fuera del hilo principal.
- Los trabajos viven en `viewModelScope` o en un coordinador ligado al lifecycle de primer plano.
- La respuesta de una petición antigua no puede reemplazar un feed solicitado con preferencias más nuevas.

- - -

## 6. Estrategia de Testing

### 6.1. Backend con Vitest

- Helper de distancia con Toulouse y París para demostrar que supera 100 km.
- `radius` incluye el borde exacto y excluye distancias superiores.
- `radius` usa residencia cuando el modo activo está apagado.
- Modo activo usa coordenadas actuales vigentes de ambos lados.
- Modo activo recurre a residencia si la marca temporal supera 24 horas.
- Candidato sin coordenadas aparece al final con distancia nula.
- `country` incluye Francia, excluye España e ignora el radio.
- `country` devuelve `RESIDENCE_COUNTRY_REQUIRED` si falta el país del buscador.
- `specific_country` incluye Toulouse y París al elegir Francia, sin aplicar radio.
- `world` no excluye y calcula distancia cuando es posible.
- Perfil propio expone coordenadas privadas, perfil ajeno y feed no.
- SQL usa parámetros y conserva exclusiones de swipes previos.

### 6.2. Android JVM

- Conversión bidireccional `MY_COUNTRY <-> country` y migración de `my_country` local.
- Repositorio online no actualiza DataStore si falla backend.
- Guardado correcto actualiza una sola vez y emite preferencias nuevas.
- `HomeViewModel` recarga al cambiar preferencias sin refresh manual.
- Dos invalidaciones rápidas no permiten que una respuesta antigua gane.
- Errores `LOCATION_REQUIRED` y `RESIDENCE_COUNTRY_REQUIRED` producen estados accionables.
- Propagación completa de coordenadas de residencia al multipart.
- Reglas demo para Toulouse, París, Mallorca, ubicación caducada y candidato desconocido.

### 6.3. Android instrumentado y recorrido manual

- Denegar permiso y verificar que residencia sigue funcionando.
- Sin permiso ni residencia, verificar el estado accionable de Radio local.
- Con permiso concedido, actualizar ubicación y comprobar el ping.
- Guardar 50 km desde Toulouse y confirmar que París desaparece automáticamente al volver.
- Cambiar a Pasaporte Francia y confirmar que Toulouse y París pueden aparecer.
- Verificar rotación, segundo plano, retorno a primer plano, tema claro y oscuro.
- Verificar que no hace falta swipe-to-refresh después del guardado.

### 6.4. Comandos de verificación previstos

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

- Los tests de base de datos solo se ejecutarán contra una base desechable confirmada.
- No se ejecutará `db:reset` durante esta corrección.

- - -

## 7. Riesgos y Mitigaciones

- **Ubicación aproximada**: coarse puede desplazar el punto varios kilometros. Se acepta la precisión del sistema y no se promete exactitud inferior a la reportada.
- **Reloj de servidor**: la vigencia de 24 horas se evalúa en PostgreSQL para evitar depender del reloj del teléfono.
- **Carreras de recarga**: cancelar o versionar solicitudes de feed y aceptar solo la generación más reciente.
- **Privacidad**: coordenadas solo en perfil propio autenticado y nunca en DTO publico.
- **Persistencia dividida**: backend primero y DataStore después evita confirmar localmente algo que el servidor rechazo.
- **Datos legados**: aceptar `my_country` únicamente al leer almacenamiento local, migrandolo a `country` en la siguiente escritura.
- **Perfiles sin coordenadas**: se incluyen al final, pero nunca reciben una distancia ficticia ni se presentan como coincidencia geográfica verificada.
- **Cambios previos del backend**: la rama correctiva parte del `HEAD` de `codex/proxy-credential-manager`; antes de cualquier futura integración se revisará su base y dependencias sin reescribir esos cambios.

- - -

## 8. Orden de Implementación Propuesto

1. Tests y helpers puros del contrato geográfico backend.
2. Reescritura parametrizada del feed y errores tipados.
3. Contrato privado de perfil propio y documentación OpenAPI.
4. Mapeo canónico de alcances y guardado consistente Android.
5. Coordenadas de residencia extremo a extremo.
6. Proveedor y sincronización de ubicación activa en primer plano.
7. Invalidación reactiva y control de concurrencia en Home.
8. Paridad del modo demo.
9. Estados UI accionables y recursos localizados.
10. Suites completas y validación en emulador o dispositivo.

- - -

## 9. Bloqueo SDMD

No se creará `tasks.md` ni se modificará código de producción hasta recibir aprobación explícita de este plan técnico.
