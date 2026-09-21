# [APROBADO] Especificacion Funcional: Modos de Radio y Reparacion de Chats

- **Fecha**: 2026-09-21
- **Estado**: [APROBADO]
- **Autor / Responsable**: Codex con validacion pendiente del propietario de MiraiLink
- **Modulos afectados**: `:app` y `MiraiLink-Backend`
- **Especificacion relacionada**: `docs/features/location_search_correctness/spec.md`

- - -

## 1. Problema y objetivo

La preferencia de ubicacion activa se presenta como un switch secundario del modo Radio local, aunque en realidad cambia el origen geografico de la busqueda. Ademas, el minimapa no representa el radio en una escala geografica real: usa zoom fijo y transforma linealmente 10 a 300 km a un circulo visual arbitrario. Por ello, la vista previa no coincide con el filtro geodesico aplicado por el backend.

La lista de chats obtiene correctamente el ultimo mensaje, pero la respuesta de `GET /chats` no incluye el objeto `destinatary` que Android usa para construir el id, el nombre y la foto. La fila queda sin identidad y no puede abrir el chat.

El objetivo es ofrecer dos modos de radio explicitos y mutuamente excluyentes, hacer que la vista previa represente el mismo radio geodesico que el feed y reparar la identidad y navegacion de las conversaciones privadas.

- - -

## 2. Situacion actual verificada

1. `SearchScope` solo contiene un modo `RADIUS` y `matchByLiveLocation` modifica su comportamiento mediante un switch.
2. El backend usa distancia geodesica con radio terrestre de 6371 km y aplica el radio como filtro duro.
3. Con las coordenadas del seed, Palma a Inca son aproximadamente 28,0 km y Palma a Valencia son aproximadamente 259,8 km.
4. Por tanto, Inca debe aparecer con un radio de 250 km y Valencia puede empezar a aparecer con 260 km. La discrepancia observada procede de la representacion del minimapa, no de esos dos limites concretos del backend.
5. El minimapa usa zoom 10 fijo, tiles mostrados a 128 dp y un circulo que interpola linealmente entre el 12 % y el 44 % del lado menor. No convierte kilometros a pixeles segun latitud y zoom.
6. La implementacion aprobada anteriormente permite que la ubicacion activa caduque a las 24 horas y recurre a residencia cuando falta o caduca.
7. `GET /chats` devuelve metadatos y ultimo mensaje, pero no devuelve `destinatary`.
8. Android deriva `userId`, `username`, `nickname` y `avatarUrl` de `destinatary`; si falta, el id queda nulo y el toque se ignora.
9. La seccion Matches obtiene esos mismos datos desde el modelo de usuario y por eso muestra identidad y navega correctamente.

- - -

## 3. Contrato funcional propuesto

### 3.1. Alcances de busqueda

Las preferencias muestran cinco chips mutuamente excluyentes:

1. `Radio desde mi residencia`.
2. `Radio desde mi ubicacion actual`.
3. `Todo mi pais`.
4. `Todo el mundo`.
5. `Pasaporte`.

El switch beta de viajeros desaparece. El radio y el minimapa se habilitan en los dos modos de radio. Los modos por pais y mundo conservan el contrato aprobado y no aplican el radio.

### 3.2. Radio desde mi residencia

- El origen del usuario que busca son exclusivamente sus coordenadas de residencia.
- La coordenada comparable de cada candidato es su residencia.
- Si al usuario le falta residencia util, no se degrada silenciosamente a otro modo y se ofrece configurar el perfil.
- Los candidatos sin residencia siguen la politica de excepcion ya aprobada: pueden aparecer sin distancia y con menor prioridad.

### 3.3. Radio desde mi ubicacion actual

- El origen es la ubicacion activa del usuario que busca, valida durante 24 horas.
- La coordenada comparable de cada candidato es su ubicacion activa valida durante 24 horas.
- Si no existe permiso, posicion activa valida o la ultima posicion ha caducado, el modo queda bloqueado de forma explicita y ofrece acciones para conceder permiso o actualizar la ubicacion. No recurre silenciosamente a residencia.
- No se realiza seguimiento continuo en segundo plano.
- Los candidatos sin ubicacion activa valida pueden aparecer como excepcion al final, sin distancia y con menor prioridad.

### 3.4. Vista previa geografica

- El circulo representa distancia geodesica en linea recta, igual que el backend, no distancia por carretera ni tiempo de viaje.
- La conversion de kilometros a pixeles tiene en cuenta latitud, zoom y escala real del tile.
- El mapa ajusta el zoom para que el circulo seleccionado quepa y sea legible entre 10 y 300 km, conservando escala metrico-geografica correcta.
- El centro usado por la vista previa coincide con el origen del chip seleccionado.
- Inca debe quedar dentro del circulo de 250 km desde Palma.
- Valencia queda aproximadamente sobre el limite de 260 km desde las coordenadas del seed. Con esas coordenadas no entra a 250 km y debe entrar visualmente a partir de 260 km. El ejemplo visual se valida con las coordenadas reales del perfil activo, no con una silueta aproximada.

### 3.5. Lista de chats

- Cada conversacion privada devuelve la identidad del otro miembro: id, username, nickname y foto principal.
- Android muestra la misma identidad visual que Matches y usa el id del otro miembro para abrir `ChatScreen`.
- La ausencia inesperada de identidad no produce una fila aparentemente interactiva ni usa una persona ficticia como identidad real.
- El ultimo mensaje, orden, conteo de no leidos y comportamiento de Matches se conservan.
- El backend ya permite crear grupos, pero la lista y `ChatScreen` solo saben navegar mediante el id de otro usuario, por lo que los grupos no estan funcionalmente terminados. Esta correccion debe auditar y completar su representacion si aparecen en `GET /chats`: nombre del grupo, imagen o fallback de grupo, `chatId` y apertura por `chatId`. Si no hay grupos existentes, no se altera el flujo privado.

- - -

## 4. Criterios de aceptacion

1. Al seleccionar cada chip de radio, el minimapa cambia al origen correspondiente y el feed usa esa misma clase de coordenada en ambos lados.
2. Desde Palma, Inca aparece con 250 km y queda dibujada dentro del circulo.
3. Desde Palma y con las coordenadas del seed, Valencia no aparece por debajo de su distancia geodesica y puede aparecer a partir de aproximadamente 260 km.
4. La frontera del backend y la del minimapa admiten solo la diferencia derivada del redondeo de presentacion.
5. Guardar cualquiera de los dos modos recarga Home automaticamente, conforme al contrato aprobado anterior.
6. Una conversacion privada muestra nombre y foto reales y abre el chat con el id correcto al tocarla.
7. Si `GET /chats` devuelve un grupo, la fila muestra su identidad de grupo y abre ese chat por `chatId`, sin intentar usar un miembro arbitrario como destinatario.
8. Los modos Todo mi pais, Todo el mundo y Pasaporte conservan su comportamiento aprobado.
9. Existen pruebas de frontera geografica, serializacion de ambos alcances, escala del minimapa, contrato de chats, mapper y navegacion de la fila.

- - -

## 5. Decisiones cerradas

- [x] Los dos chips se llaman `Radio desde mi residencia` y `Radio desde mi ubicacion actual`.
- [x] El modo de ubicacion actual se bloquea con acciones de recuperacion cuando no hay permiso, posicion activa o vigencia de 24 horas. No recurre silenciosamente a residencia.
- [x] Los candidatos sin ubicacion activa valida pueden aparecer al final, sin distancia y con menor prioridad.
- [x] La reparacion cubre chats privados y audita los grupos ya soportados por el backend. Los grupos que aparezcan se corrigen de extremo a extremo.
- [x] El minimapa representa escala geografica real y debe reflejar de manera visual la cobertura geodesica aplicada por el feed.

- - -

## 6. Siguiente paso SDMD

La especificacion queda aprobada. El siguiente paso es revisar y aprobar el plan tecnico antes de modificar codigo de produccion.
