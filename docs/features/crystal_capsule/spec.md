# [APROBADO] Especificación Funcional: Cápsula de Cristal v2 (Rediseño y Dinámica de Conexión)

- **Fecha**: 2026-10-07
- **Estado**: [APROBADO]
- **Autor / Responsable**: ArisGuimera SDMD / Antigravity Pair Programming
- **Módulo Afectado**: `:app` (`com.feryaeljustice.mirailink`)

---

## 1. Problema y Objetivo

- **Problema que resuelve**: La interfaz anterior de la Cápsula de Cristal incrustaba un panel gris plano (`CapsulePanel`) fijo en la parte superior del chat que sobrecargaba la pantalla, resultaba antiestético y restaba protagonismo a la conversación. Además, el sistema de puntuación permitía avanzar por mensajes arbitrarios en lugar de fomentar respuestas compartidas reflexivas, y las fotografías veladas presentaban artefactos lineales ("rayitas") que deterioraban la calidad visual de las fotos.
- **Objetivo**: Rediseñar la experiencia de la Cápsula de Cristal centralizando toda su gestión en un modal BottomSheet accesible desde un icono interactivo en el header (ubicado a la izquierda del botón de reportar) con indicador dinámico de progreso. Establecer una meta compartida de 4 puntos (1 punto por pregunta respondida bilateralmente), dinámica estricta por turnos con 1 sola pregunta activa a la vez, bloqueo total del chat convencional durante la cápsula activa, mensaje estático de felicitación persistente tras desbloquearse y eliminación de las líneas de fractura en las fotos manteniendo un desenfoque progresivo limpio.

---

## 2. Situación Actual

- **Descubrimiento y Match**: Los participantes coinciden mediante swipe voluntario con fotos veladas basándose en bio y aficiones. Este like mutuo inicial se conserva como requisito indispensable de consentimiento previo.
- **Visualización en Chat**: Un panel gris superior ocupaba espacio vertical permanente en `ChatScreen`.
- **Efecto en Fotos**: `CrystalPhoto` dibuja líneas vectoriales diagonales de fractura en un Canvas que afean las fotos en el perfil y en la vista previa.
- **Progreso y Chat**: La escala previa era de 8 puntos e intercalaba mensajes de chat normales y misiones sin bloquear la escritura libre.

---

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)
- **Icono con Progreso en TopBar**:
  - Ubicación: En `ChatTopBar`, situado a la izquierda del botón de reportar (`ic_report`).
  - Apariencia: Icono especial con estado visual evolutivo que inicia completamente vacío (0/4), se va rellenando gradualmente según el avance (1/4, 2/4, 3/4) y permanece lleno/iluminado al finalizar el modo (4/4).
  - Comportamiento: Al pulsar, siempre despliega el modal centralizado (incluso tras finalizar el modo para consultar el histórico).
- **Modal Centralizado (BottomSheet)**:
  - Cabecera: Título "Cápsula de Cristal", barra de progreso lineal temática y estado actual.
  - Centro (Modo Activo):
    - Botón 1: "Preguntas e Historial" (con badge/indicador si hay una pregunta pendiente por contestar).
    - Botón 2: "Proponer Nueva Pregunta" (bloqueado/deshabilitado si ya hay una pregunta en curso o pendiente de respuesta).
  - Centro (Modo Revelado / 4 puntos):
    - Se oculta "Proponer Nueva Pregunta".
    - Se muestra únicamente "Ver Historial de Preguntas".
    - Mensaje destacado de éxito y compatibilidad alcanzada.
  - Subflujo Nueva Pregunta:
    - Selector de categorías temáticas (anime, gaming, hobbies, vida cotidiana, cita ideal, proyectos, relaciones, familia).
    - Al seleccionar categoría: 2-3 preguntas sugeridas del catálogo oficial + 4ª opción "Crear mi propia pregunta personalizada" (límite de 120 caracteres para la pregunta).
    - Campo de respuesta obligatoria del usuario proponente (hasta 300 caracteres). Botón "Enviar" deshabilitado si algún campo está en blanco.
    - Al enviar, se registra la pregunta con la respuesta del proponente, se cierra el modal y pasa a ser la única pregunta activa del turno.
  - Subflujo Historial y Preguntas:
    - *Pendiente de responder*: Pregunta recibida que espera mi respuesta + campo de texto + botón "Enviar respuesta". Al responder, desaparece de pendientes y suma 1 punto compartido.
    - *Esperando respuesta*: Pregunta formulada por mí que aún no ha sido respondida por la otra persona. Muestra aviso de turno en curso.
    - *Historial completado*: Preguntas contestadas por ambos mostrando la pregunta, la respuesta del compañero y la respuesta propia.
  - Footer (Modo Activo):
    - Botones en formato tarjeta con iconos y descripciones contextuales explicativas para pausar dinámica, abandonar cápsula o solicitar revelación anticipada.
    - En modo completado, se reemplaza por un banner de felicitación.
- **Regla de Puntuación (4 Puntos Compartidos)**:
  - 1 punto = Ambos usuarios han contestado a la misma pregunta.
  - Dinámica estricta por turnos: Solo 1 pregunta activa simultáneamente. Bloqueo de proponer nuevas preguntas hasta resolver la activa.
  - Al alcanzar 4 puntos compartidos, el estado pasa a `revealed` y las fotos se desvelan completamente.
- **Bloqueo del Chat Convencional**:
  - Mientras el modo esté activo (`status != "revealed"`), el campo de texto inferior de envío de mensajes y el botón de la ruleta de gestos quedan bloqueados con un mensaje informativo: "Chat bloqueado hasta completar la Cápsula. Abre la Cápsula para avanzar respondiendo preguntas."
- **Banner Estático Post-Completado**:
  - Al llegar a 4 puntos, aparece un banner estático en el chat: *"Felicidades, has completado el modo. Ahora puedes empezar a hablar con la persona."*
  - Persiste de manera fija (incluso saliendo y volviendo a entrar a la pantalla) hasta que cualquiera de los dos participantes envíe el primer mensaje normal de chat.
- **Limpieza Visual de Fotos**:
  - Eliminación de las líneas de dibujo vectorial en `CrystalPhoto`, conservando el desenfoque progresivo (`blur`).

### 3.2. Fuera del Alcance (Out of Scope)
- Modificaciones en algoritmos de afinidad fuera del chat.
- Cambios en el sistema de reportes de usuario.
- Notificaciones push en segundo plano para clientes sin soporte Firebase.

---

## 4. Casuísticas y Comportamiento Mobile

- **Comportamiento en Modo Online vs Modo Offline Demo**:
  - En modo Demo: La base de datos local Room simula las respuestas del interlocutor de forma consistente, permitiendo probar el flujo completo de 4 preguntas y desbloqueo.
  - En modo Online: Sincronización con el backend mediante contratos REST/Socket con tolerancia a cortes de red e idempotencia.
- **Ciclo de Vida y Recuperación de Estado**:
  - La navegación interna del modal (pasos, categoría seleccionada, texto en redacción) sobrevive a la rotación de pantalla mediante `rememberSaveable`.
- **Ergonomía, Teclado y Accesibilidad**:
  - Campos de entrada dentro del modal y del chat con `imePadding()` para no quedar ocultos tras el teclado en pantalla.
  - Targets táctiles mínimos de 48 x 48 dp para todos los botones del modal y del top bar.
  - Soporte completo de tema claro y tema oscuro Material 3.

---

## 5. Criterios de Aceptación (Given - When - Then)

### Criterio 1: Icono con Progreso en TopBar
- **Dado que** un usuario entra en un chat con cápsula activa,
- **Cuando** observa la barra superior (`ChatTopBar`),
- **Entonces** ve un icono interactivo a la izquierda del botón de reportar, cuyo llenado refleja el progreso actual (vacío en 0/4, llenándose en 1/4, 2/4, 3/4 y lleno en 4/4), y al pulsarlo se abre el modal centralizado sin que aparezca ningún panel gris estático en el cuerpo del chat.

### Criterio 2: Bloqueo del Chat Convencional durante la Cápsula
- **Dado que** la cápsula está en estado `active` con menos de 4 puntos,
- **Cuando** el usuario mira la barra de entrada de mensajes inferior,
- **Entonces** el campo de texto y la ruleta de gestos están bloqueados con un mensaje que indica que deben avanzar completando las preguntas de la cápsula.

### Criterio 3: Dinámica por Turnos (1 Sola Pregunta Activa)
- **Dado que** ya existe una pregunta activa esperando respuesta (sea propia o del compañero),
- **Cuando** el usuario accede al modal,
- **Entonces** la opción de proponer una nueva pregunta está bloqueada, indicando que debe responder la pendiente o esperar la respuesta del compañero.

### Criterio 4: Proponer Pregunta (Catálogo o Personalizada)
- **Dado que** no hay ninguna pregunta activa en curso,
- **Cuando** el usuario elige una categoría, selecciona una pregunta predefinida o escribe una personalizada (hasta 120 caracteres), responde a la pregunta (hasta 300 caracteres) y pulsa "Enviar",
- **Entonces** la pregunta y su respuesta quedan registradas, el modal se cierra y la pregunta pasa a estar pendiente de respuesta para el otro usuario.

### Criterio 5: Respuesta Bilateral y Otorgamiento de Puntos
- **Dado que** existe una pregunta propuesta por un usuario,
- **Cuando** el otro usuario redacta y envía su respuesta desde la sección de pendientes del modal,
- **Entonces** la cápsula suma exactamente 1 punto compartido, actualizando el nivel visual tanto en el modal como en el icono del header.

### Criterio 6: Finalización a los 4 Puntos, Banner Persistente y Desbloqueo del Chat
- **Dado que** ambos usuarios completan la cuarta pregunta compartida (4 puntos),
- **Cuando** se actualiza el estado de la cápsula,
- **Entonces** pasa a estado `revealed`, las fotos pierden todo el desenfoque, el chat muestra el banner de felicitación estático de forma persistente hasta que se envíe el primer mensaje, y se habilitan los controles para enviar mensajes convencionales.

### Criterio 7: Eliminación de Rayas en Fotos
- **Dado que** se muestra una foto velada en cualquier pantalla (perfil, chat, preview),
- **Cuando** se renderiza la imagen,
- **Entonces** presenta desenfoque gradual sin líneas diagonales de corte superpuestas.

---

## 6. Decisiones Acordadas

- **Ubicación del icono**: A la izquierda del botón de reportar en `ChatTopBar`. Inicia vacío y se rellena hasta quedar lleno.
- **Turnos de preguntas**: Estrictamente 1 pregunta activa a la vez. No se puede crear otra hasta contestar la pendiente.
- **Límites de texto**: Máximo 120 caracteres en pregunta personalizada y 300 caracteres en respuestas; botón de envío bloqueado si está vacío.
- **Banner de felicitación**: Persistente en pantalla de chat hasta que efectivamente se envíe el primer mensaje normal.
