# [PENDIENTE DE APROBACION] Plan Tecnico: Modos de Radio y Reparacion de Chats

- **Fecha**: 2026-09-21
- **Especificacion**: `docs/features/search_radius_modes_and_chat_list_repair/spec.md`
- **Repositorios**: `MiraiLink` y `MiraiLink-Backend`
- **Estado**: [PENDIENTE DE APROBACION]

## 1. Hechos verificados

- Android es un modulo `:app`, con Kotlin 2.4.10, AGP 9.4.1, Compose BOM 2026.09.00, Navigation 3 version 1.1.7, Koin 4.2.2, Retrofit 3.0.0 y Coil 2.7.0.
- El modelo actual guarda `SearchScope.RADIUS` y el booleano `matchByLiveLocation` en DataStore y en el backend como `search_scope` y `search_match_live_location`.
- `SearchRadiusMinimap` usa tiles OSM a zoom 10 y pinta un circulo por fraccion visual, sin conversion de km a pixeles.
- `geoSearch.js` y `swipe.controller.js` ya calculan la distancia geodesica con radio terrestre 6371 km y filtran el radio de forma dura.
- El backend usa Express 5.2.1, PostgreSQL mediante `pg` 8.23.0, Zod 4.6.5 y Vitest 5.0.1.
- `GET /chats` no incluye la identidad del otro usuario. Android espera `destinatary` en `ChatSummaryResponse`, por lo que genera filas con id nulo.
- El backend permite crear chats de grupo, pero la ruta Android `ChatScreen` recibe `userId`, no `chatId`.

## 2. Estrategia de implementacion

### Fase A. Contrato de preferencias y ubicacion

1. Sustituir el booleano de viajeros por dos valores de alcance persistibles y compatibles: `radius_residence` y `radius_active`.
2. Migrar de forma segura los valores existentes: `radius` con `matchByLiveLocation=false` pasa a residencia, y con `true` pasa a ubicacion activa. El booleano deja de participar en decisiones nuevas.
3. Actualizar DTO, datasource, repositorio, demo Room y backend para enviar, guardar, validar y devolver el nuevo valor de alcance.
4. Mantener pais, mundo y pasaporte sin filtro de radio.
5. Para `radius_active`, devolver `LOCATION_REQUIRED` cuando el buscador no tenga ubicacion activa fresca. No se usa residencia como respaldo en este modo.
6. Aplicar coordenadas de residencia a ambos lados en `radius_residence`, y coordenadas activas frescas a ambos lados en `radius_active`. Los candidatos sin coordenada aplicable conservan la excepcion de prioridad baja y sin distancia aprobada.

### Fase B. Mapa con escala real

1. Extraer una funcion pura de proyeccion Web Mercator que convierta distancia geodesica a pixeles para latitud y zoom concretos.
2. Seleccionar dinamicamente un zoom entero que permita visualizar el circulo completo con margen en el area disponible, y cargar el mosaico OSM correspondiente a ese zoom.
3. Dibujar el radio con el resultado de esa conversion, manteniendo tiles cuadrados, coordenadas fraccionales, wrapping horizontal y limite vertical.
4. El centro del mapa procede del estado que el ViewModel resuelve para el chip activo. El composable no elige ni sustituye fuentes de ubicacion.
5. Cambiar el switch beta por dos `FilterChip` de radio con recursos localizados, mantener el slider habilitado para ambos y mostrar acciones recuperables cuando falta ubicacion activa.

### Fase C. Respuesta y navegacion de chats

1. Reescribir la consulta de `GET /chats` para que cada chat privado devuelva el otro miembro con id, username, nickname y foto principal. La consulta no expondrá correo, coordenadas ni otros datos privados.
2. Completar el contrato de grupo con `chat_id`, `type`, `name` e identidad visual de grupo. Para privados se conserva el destinatario, para grupos se muestra nombre y fallback de grupo.
3. Extender DTO, dominio, mapper y `ChatPreviewViewEntry` para representar explícitamente una fila privada o de grupo, sin valores nulos ambiguos.
4. Cambiar la navegación y el repositorio de conversación a una ruta que acepte `chatId`. Las conversaciones privadas conservan la creación o recuperación de chat desde Matches, mientras la lista usa directamente el `chatId` existente.
5. Hacer que `MessageListItem` solo sea interactivo si su destino de navegación es válido y proporcionar contenido accesible para avatar o fallback.

### Fase D. Recarga y paridad demo

1. Conservar la invalidación automática de Home ya creada y adaptarla a los dos nuevos valores de alcance.
2. Aplicar las mismas reglas de fuente geográfica y de falta de ubicación en `DemoSwipeRepositoryImpl`.
3. Ajustar la demo de chats para que privados y grupos, cuando existan, compongan datos coherentes con el mismo contrato de presentación.

## 3. Archivos previsiblemente afectados

- Android: `SearchPreferences.kt`, `SearchPreferencesRepositoryImpl.kt`, `SearchPreferencesViewModel.kt`, `SearchPreferencesScreen.kt`, `SearchSettingsSection.kt`, `SearchRadiusMinimap.kt`, recursos de cadenas, repositorios demo, modelos y mappers de chat, `MessagesViewModel.kt`, `ChatList.kt`, `MessageListItem.kt`, `AppScreen.kt`, `NavWrapper.kt`, `ChatScreen` y sus casos de uso.
- Backend: `src/routes/user.routes.js`, `src/controllers/user.controller.js`, `src/controllers/swipe.controller.js`, `src/utils/geoSearch.js`, `src/controllers/chat.controller.js`, OpenAPI y pruebas Vitest. Se añadirá migracion SQL solo si la base de datos impone valores de `search_scope` que no admitan los nuevos alcances.

## 4. Estrategia de pruebas

### Android unitarias

- Migracion desde preferencias antiguas a los dos alcances de radio.
- ViewModel de preferencias: estado de cada chip, guardado, error de ubicacion activa y señal de recarga a Home.
- Utilidad de escala del minimapa: Palma a Inca queda dentro a 250 km, Palma a Valencia queda fuera a 250 km y entra a 260 km, dentro de una tolerancia de redondeo visible.
- Demo: residencia y ubicacion activa producen conjuntos distintos y respetan el limite duro.
- Mappers de chat: privado con identidad, grupo con identidad de grupo y respuesta incompleta no navegable.
- ViewModel de Mensajes y navegación: cada fila válida emite el `chatId` correcto.

### Backend unitarias e integracion

- `radius_residence` y `radius_active` aplican la fuente correcta al buscador y candidato.
- La falta o caducidad de ubicacion activa del buscador devuelve el error tipado esperado.
- Fronteras geodésicas Palma, Inca y Valencia validan exactamente inclusión y exclusión.
- `GET /chats` devuelve destinatario privado y datos de grupo sin filtrar datos sensibles.
- OpenAPI y validacion Zod aceptan solo los alcances aprobados.

### Verificacion manual

- Cambiar entre los cinco chips, guardar y volver a Home sin swipe manual.
- Comparar la cobertura visible del circulo en Palma para 250 y 260 km frente a candidatos seed.
- Validar denegacion de permiso y ubicacion activa caducada.
- Abrir chats privados y grupos desde Mensajes, comprobar nombre, imagen, ultimo mensaje y retroceso.
- Repetir la vista de preferencias en retrato y paisaje.

## 5. Riesgos y mitigaciones

- Los tiles remotos pueden tardar o no estar disponibles. La geometria y el circulo se calculan localmente, por lo que la escala no depende de que cargue una imagen.
- Un chat de grupo requiere separar semánticamente `chatId` de `userId`. Se hará mediante rutas tipadas y se mantendrá el flujo existente de Matches para no romper los chats privados.
- Los perfiles con coordenadas de seed y ubicaciones reales pueden diferir unos metros. El backend conserva comparación numérica, y la UI solo redondea texto y representación.
- No se añaden dependencias Android ni se realizan commits.

## 6. Bloqueo SDMD

No se creará `tasks.md` ni se modificará código de producción hasta recibir aprobación explícita de este plan técnico.
