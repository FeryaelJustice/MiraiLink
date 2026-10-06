# [APROBADO] Especificación Funcional: Modos de Radio y Reparación de Chats

- **Fecha**: 2026-09-21
- **Estado**: [APROBADO]
- **Autor / Responsable**: Codex con validación pendiente del propietario de MiraiLink
- **Módulos afectados**: `:app` y `MiraiLink-Backend`
- **Especificación relacionada**: `docs/features/location_search_correctness/spec.md`

- - -

## 1. Problema y objetivo

La preferencia de ubicación activa se presenta como un switch secundario del modo Radio local, aunque en realidad cambia el origen geográfico de la búsqueda. Además, el minimapa no representa el radio en una escala geográfica real: usa zoom fijo y transforma linealmente 10 a 300 km a un circulo visual arbitrario. Por ello, la vista previa no coincide con el filtro geodésico aplicado por el backend.

La lista de chats obtiene correctamente el último mensaje, pero la respuesta de `GET /chats` no incluye el objeto `destinatary` que Android usa para construir el id, el nombre y la foto. La fila queda sin identidad y no puede abrir el chat.

El objetivo es ofrecer dos modos de radio explícitos y mutuamente excluyentes, hacer que la vista previa represente el mismo radio geodésico que el feed y reparar la identidad y navegación de las conversaciones privadas.

- - -

## 2. Situación actual verificada

1. `SearchScope` solo contiene un modo `RADIUS` y `matchByLiveLocation` modifica su comportamiento mediante un switch.
2. El backend usa distancia geodésica con radio terrestre de 6371 km y aplica el radio como filtro duro.
3. Con las coordenadas del seed, Palma a Inca son aproximadamente 28,0 km y Palma a Valencia son aproximadamente 259,8 km.
4. Por tanto, Inca debe aparecer con un radio de 250 km y Valencia puede empezar a aparecer con 260 km. La discrepancia observada procede de la representación del minimapa, no de esos dos límites concretos del backend.
5. El minimapa usa zoom 10 fijo, tiles mostrados a 128 dp y un circulo que interpola linealmente entre el 12 % y el 44 % del lado menor. No convierte kilometros a píxeles según latitud y zoom.
6. La implementación aprobada anteriormente permite que la ubicación activa caduque a las 24 horas y recurre a residencia cuando falta o caduca.
7. `GET /chats` devuelve metadatos y último mensaje, pero no devuelve `destinatary`.
8. Android deriva `userId`, `username`, `nickname` y `avatarUrl` de `destinatary`; si falta, el id queda nulo y el toque se ignora.
9. La sección Matches obtiene esos mismos datos desde el modelo de usuario y por eso muestra identidad y navega correctamente.

- - -

## 3. Contrato funcional propuesto

### 3.1. Alcances de búsqueda

Las preferencias muestran cinco chips mutuamente excluyentes:

1. `Radio desde mi residencia`.
2. `Radio desde mi ubicacion actual`.
3. `Todo mi pais`.
4. `Todo el mundo`.
5. `Pasaporte`.

El switch beta de viajeros desaparece. El radio y el minimapa se habilitan en los dos modos de radio. Los modos por país y mundo conservan el contrato aprobado y no aplican el radio.

### 3.2. Radio desde mi residencia

- El origen del usuario que busca son exclusivamente sus coordenadas de residencia.
- La coordenada comparable de cada candidato es su residencia.
- Si al usuario le falta residencia útil, no se degrada silenciosamente a otro modo y se ofrece configurar el perfil.
- Los candidatos sin residencia siguen la política de excepción ya aprobada: pueden aparecer sin distancia y con menor prioridad.

### 3.3. Radio desde mi ubicación actual

- El origen es la ubicación activa del usuario que busca, válida durante 24 horas.
- La coordenada comparable de cada candidato es su ubicación activa válida durante 24 horas.
- Si no existe permiso, posición activa válida o la última posición ha caducado, el modo queda bloqueado de forma explícita y ofrece acciones para conceder permiso o actualizar la ubicación. No recurre silenciosamente a residencia.
- No se realiza seguimiento continuo en segundo plano.
- Los candidatos sin ubicación activa válida pueden aparecer como excepción al final, sin distancia y con menor prioridad.

### 3.4. Vista previa geográfica

- El circulo representa distancia geodésica en línea recta, igual que el backend, no distancia por carretera ni tiempo de viaje.
- La conversión de kilometros a píxeles tiene en cuenta latitud, zoom y escala real del tile.
- El mapa ajusta el zoom para que el circulo seleccionado quepa y sea legible entre 10 y 300 km, conservando escala metrico-geografica correcta.
- El centro usado por la vista previa coincide con el origen del chip seleccionado.
- Inca debe quedar dentro del circulo de 250 km desde Palma.
- Valencia queda aproximadamente sobre el límite de 260 km desde las coordenadas del seed. Con esas coordenadas no entra a 250 km y debe entrar visualmente a partir de 260 km. El ejemplo visual se valida con las coordenadas reales del perfil activo, no con una silueta aproximada.

### 3.5. Lista de chats

- Cada conversación privada devuelve la identidad del otro miembro: id, username, nickname y foto principal.
- Android muestra la misma identidad visual que Matches y usa el id del otro miembro para abrir `ChatScreen`.
- La ausencia inesperada de identidad no produce una fila aparentemente interactiva ni usa una persona ficticia como identidad real.
- El último mensaje, orden, conteo de no leidos y comportamiento de Matches se conservan.
- El backend ya permite crear grupos, pero la lista y `ChatScreen` solo saben navegar mediante el id de otro usuario, por lo que los grupos no están funcionalmente terminados. Esta corrección debe auditar y completar su representación si aparecen en `GET /chats`: nombre del grupo, imagen o fallback de grupo, `chatId` y apertura por `chatId`. Si no hay grupos existentes, no se altera el flujo privado.

- - -

## 4. Criterios de aceptación

1. Al seleccionar cada chip de radio, el minimapa cambia al origen correspondiente y el feed usa esa misma clase de coordenada en ambos lados.
2. Desde Palma, Inca aparece con 250 km y queda dibujada dentro del circulo.
3. Desde Palma y con las coordenadas del seed, Valencia no aparece por debajo de su distancia geodésica y puede aparecer a partir de aproximadamente 260 km.
4. La frontera del backend y la del minimapa admiten solo la diferencia derivada del redondeo de presentación.
5. Guardar cualquiera de los dos modos recarga Home automáticamente, conforme al contrato aprobado anterior.
6. Una conversación privada muestra nombre y foto reales y abre el chat con el id correcto al tocarla.
7. Si `GET /chats` devuelve un grupo, la fila muestra su identidad de grupo y abre ese chat por `chatId`, sin intentar usar un miembro arbitrario como destinatario.
8. Los modos Todo mi país, Todo el mundo y Pasaporte conservan su comportamiento aprobado.
9. Existen pruebas de frontera geográfica, serialización de ambos alcances, escala del minimapa, contrato de chats, mapper y navegación de la fila.

- - -

## 5. Decisiones cerradas

- [x] Los dos chips se llaman `Radio desde mi residencia` y `Radio desde mi ubicacion actual`.
- [x] El modo de ubicación actual se bloquea con acciones de recuperación cuando no hay permiso, posición activa o vigencia de 24 horas. No recurre silenciosamente a residencia.
- [x] Los candidatos sin ubicación activa válida pueden aparecer al final, sin distancia y con menor prioridad.
- [x] La reparación cubre chats privados y audita los grupos ya soportados por el backend. Los grupos que aparezcan se corrigen de extremo a extremo.
- [x] El minimapa representa escala geográfica real y debe reflejar de manera visual la cobertura geodésica aplicada por el feed.

- - -

## 6. Siguiente paso SDMD

La especificación queda aprobada. El siguiente paso es revisar y aprobar el plan técnico antes de modificar código de producción.
