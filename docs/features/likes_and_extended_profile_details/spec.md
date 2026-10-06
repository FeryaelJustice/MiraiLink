# [APROBADO] Especificación Funcional: Likes Recibidos, Detalle de Usuario por Username y Perfil Extendido

- **Fecha**: 2026-09-24
- **Estado**: [APROBADO]
- **Autor / Responsable**: Antigravity & FeryaelJustice
- **Módulos Afectados**: `:app` (`com.feryaeljustice.mirailink`), `MiraiLink-Backend` (Express 5 + PostgreSQL)

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**:
  - Los usuarios no pueden ver quien ha mostrado interés en ellos (likes recibidos) antes de hacer match.
  - No existe una pantalla dedicada de detalle de perfil accesible de forma universal mediante deep links (`mirailink.com/user/<username>`), ni se puede consultar el perfil completo al chatear tocando el avatar del destinatario.
  - El registro no exige de forma estricta la edad (fecha de nacimiento con edad mínima de 16 años) ni el género. Además, estos campos estaban expuestos a modificación posterior en la edición de perfil.
  - El perfil de usuario carece de información enriquecida sobre estilo de vida, datos personales, idiomas hablados, familia y curiosidades / hechos ("facts" estilo gamer) organizados por categorías con internacionalización (`es`, `en`, `ja`).
- **Objetivo**:
  - Incorporar la sección "Gente a la que le gustas" en el tercer slot de la barra inferior de navegación, con arquitectura preparada para bloqueo Premium en el futuro.
  - Construir la pantalla completa `UserProfileDetailScreen` navegable por `username`, con carrusel de imágenes en altura fija, vista completa al mantener pulsado, secciones categorizadas con scroll, botón de compartir enlace nativo, e integración al pulsar el avatar en el chat.
  - Asegurar la captura obligatoria de fecha de nacimiento (edad mínima de 16 años requerida) y género en el registro, y eliminarlos por completo de los formularios y endpoints de edición de perfil (solo lectura una vez registrados).
  - Implementar el esquema de base de datos normalizado para perfiles extendidos (estilo de vida, familia, religión, zodiaco, política, idiomas, educación, profesión y 8 curiosidades/facts estilo gamer con máximo 3 respuestas por usuario) con soporte i18n (`es`, `en`, `ja`) y tablas intermedias.
  - Proveer paridad y datos de prueba en el modo Demo Offline con Room.

- - -

## 2. Situación Actual

- La barra de navegación inferior (`MiraiLinkBottomBar`) solo cuenta con tres tabs: Inicio (`HomeScreen`), Mensajes (`MessagesScreen`) y Perfil (`ProfileScreen`).
- En el chat (`ChatScreen` y `ChatTopBar`), mantener pulsado el avatar abre la foto en pantalla completa, pero un toque simple (tap) no abre el perfil del interlocutor.
- La pantalla de detalle de un usuario externo no existe como pantalla dedicada desacoplada con ruta por `username`; la visualización de perfiles se limita a las cartas de descubrimiento (`UserCard` / `PublicUserCard`) y no soporta deep links directos.
- En registro (`AuthScreen` y `auth.controller.js`), solo se solicitan `username`, `email` y `password`. `birthdate` y `gender` se editaban en el perfil; ahora deben retirarse por completo de la edición.
- El perfil solo almacena intereses de anime y videojuegos (`user_anime_interests`, `user_game_interests`). No existen tablas ni modelos para habitos (fumar, beber), orientación sexual, religión, zodiaco, política, familia, idiomas hablados, profesión ni curiosidades estilo gamer ("facts").

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)

1. **Pantalla y Tab "Gente a la que le gustas" (Who Liked You)**:
   - Nuevo tab en `MiraiLinkBottomBar` colocado como tercer elemento (después de Mensajes y antes de Perfil).
   - Icono de corazón (`ic_heart`).
   - Lista lazy de usuarios que dieron like al usuario autenticado y con los que **no hay match todavía**, ordenada por fecha descendente (`created_at DESC`).
   - Si se acepta (like) o se rechaza (dislike), el usuario desaparece de la lista de likes recibidos.
   - Cada elemento de la lista muestra foto circular (estilo avatar del chat), nombre / nickname, edad y un botón de acción rápida para hacer match inmediato.
   - Al pulsar sobre la foto, nombre o tarjeta, navega a la pantalla de detalle de usuario (`UserProfileDetailScreen`).
   - Componente desacoplado para estado bloqueado Premium (`PremiumLockedState`) preparado para activarse en el futuro.
2. **Pantalla de Detalle de Usuario (`UserProfileDetailScreen`)**:
   - Navegación mediante clave/identificador único inmutable: `username`.
   - Soporte para deep link nativo Android: esquema web `https://mirailink.com/user/{username}` (o scheme `mirailink://user/{username}`).
   - Botón de retroceso desacoplado que gestione tanto la pila interna como la salida hacia la app principal si se proviene de un deep link externo.
   - Encabezado con carrusel de fotos a altura fija, indicador de fotos múltiples, gesto de pulsación prolongada para ver a pantalla completa (`FullscreenImagePreview`).
   - Secciones con scroll continuo sobre el fondo de la aplicación, divididas por categorías:
     - Información Básica (edad calculada, género, orientación sexual, residencia/distancia si aplica).
     - Curiosidades / Facts estilo gamer (hasta 3 preguntas elegidas por el usuario y sus respuestas).
     - Estilo de Vida (habitos de tabaco, alcohol).
     - Familia (opciones combinadas: tengo hijos, quiero hijos, etc.).
     - Creencias y Personalidad (signo del zodiaco, religión, postura política).
     - Educación y Profesión (nivel máximo de estudios, profesión en texto libre).
     - Idiomas que habla (etiquetas localizadas según idioma activo).
     - Intereses (animes y juegos existentes).
   - Botón de acción "Compartir perfil" que invoca el diálogo nativo de Android (`Intent.ACTION_SEND`) con la URL del perfil.
   - Botones de acción Like / Rechazar en la parte inferior si el usuario visualizado procede de likes recibidos o aun no hay match con el.
3. **Punto de Entrada desde el Chat**:
   - En `ChatTopBar`, un tap simple en la foto de perfil navega a `UserProfileDetailScreen` con el `username` del destinatario.
   - Si ya hay un match activo (como en el chat), los botones de Like/Rechazar no se muestran en el detalle del perfil.
   - La pulsación larga (`onLongPress`) se preserva para abrir la foto a pantalla completa.
4. **Registro Obligatorio de Edad (Mínimo 16 Años) y Género Inmutables**:
   - Validación en backend (`registerSchema`): `birthdate` (ISO Date YYYY-MM-DD, verificando `>= 16` años) y `gender` (enum válido) son campos obligatorios.
   - En el frontend (`AuthScreen`), adición de selectores de fecha de nacimiento (`BirthdateField`) y género (`GenderSelector`) en el paso de registro.
   - Verificación con snackbar si la edad es menor de 16 años: "Debes tener al menos 16 años para registrarte" (localizado en `es`, `en`, `ja`).
   - Retiro total de `birthdate` y `gender` del formulario de edición de perfil (`UserCard`, `EditProfileUiState`, `updateProfile` de backend). Solo se muestran en modo vista.
5. **Esquema de Base de Datos y Backend para Perfil Extendido**:
   - Soporte para idiomas `es`, `en`, `ja` en `supported_languages`.
   - Tablas maestras e intermedias para catálogos localizados:
     - `relationship_goals` y `relationship_goal_translations` + tabla intermedia `user_relationship_goals`.
     - `family_options` y `family_option_translations` + tabla intermedia `user_family_options` (selección multiple: "no tengo hijos", "ya tengo hijos", "quiero hijos", "no se si quiero hijos").
     - `religions` y `religion_translations` + asignación al usuario.
     - `zodiac_signs` y `zodiac_sign_translations` + asignación al usuario.
     - `political_stances` y `political_stance_translations` + asignación al usuario.
     - `smoking_habits` y `smoking_habit_translations` + asignación al usuario.
     - `drinking_habits` y `drinking_habit_translations` + asignación al usuario.
     - `sexual_orientations` y `sexual_orientation_translations` + asignación al usuario.
     - `spoken_languages` y `spoken_language_translations` + tabla intermedia `user_spoken_languages`.
     - `education_levels` y `education_level_translations` + asignación al usuario.
     - Profesión como texto libre `profession VARCHAR(100)` en tabla de usuario.
     - `profile_prompts` (8 preguntas gamer precargadas) y `profile_prompt_translations` + tabla `user_profile_prompts` (`user_id`, `prompt_id`, `answer` max 300 caracteres, límite máximo de 3 por usuario).
   - Endpoint backend `GET /swipe/likes-received`: lista paginada de usuarios que dieron like a `req.user.id` sin match previo, con orden `likes.created_at DESC`.
   - Endpoint backend `GET /user/by-username/:username`: detalle publico extendido de un usuario por su username.
   - Endpoint `PUT /user`: guardado de datos extendidos (sin permitir alterar género ni fecha de nacimiento).
6. **Soporte en Modo Demo Offline (Android)**:
   - Seeder simulado con datos de likes recibidos, detalle de usuario por username y atributos extendidos en `DemoDataSeeder.kt` y `DemoUserRepositoryImpl.kt`.

### 3.2. Fuera del Alcance (Out of Scope)

- Cobro real o pasarela de pagos para el modo Premium.
- Deshacer match (unmatch) - se abordará en una funcionalidad futura.
- Subida de más de 4 fotos de perfil.

- - -

## 4. Casuísticas y Comportamiento Mobile

- **Comportamiento en Modo Online vs Modo Offline Demo**:
  - En Modo Online: Consultas y swipes/likes a la API REST.
  - En Modo Offline Demo: `DemoUserRepositoryImpl` proveera datos locales simulados completos para likes recibidos y detalle de perfil.
- **Ciclo de Vida y Recuperación de Estado**:
  - `UserProfileDetailViewModel` y `ReceivedLikesViewModel` usan `SavedStateHandle`.
  - Navegación resiliente ante rotación o apertura directa vía deep link.
- **Estados Vacíos (Empty States) y Manejo de Errores**:
  - Empty state en Likes: "Aun no tienes me gusta nuevos. Sigue descubriendo perfiles en Inicio".
  - Manejo de reintentos con `MiraiLinkButton`.
- **Ergonomía, Teclado y Accesibilidad**:
  - Elementos táctiles de al menos 48 x 48 dp.
  - Soporte completo para temas claro y oscuro Material 3.
  - `imePadding()` en campos de entrada de profesión y respuestas a preguntas gamer.

- - -

## 5. Criterios de Aceptación (Formato Given - When - Then)

### Criterio 1: Visualización de Likes Recibidos
- **Dado que**: Un usuario autenticado accede a la aplicación.
- **Cuando**: Pulsa el tercer icono en la barra inferior (Gente a la que le gustas).
- **Entonces**: Se carga la lista de perfiles que han dado like (sin match previo) ordenados de más reciente a más antiguo, con foto circular, nombre, edad y botón de match rápido.

### Criterio 2: Acción Rápida de Match o Rechazo
- **Dado que**: El usuario está en la lista de likes recibidos o en la pantalla de detalle de un like recibido.
- **Cuando**: Pulsa el botón de dar like (match) o el botón de rechazar.
- **Entonces**: El perfil desaparece de la lista de likes recibidos y, en caso de like, se crea el match correspondiente.

### Criterio 3: Acceso a Detalle de Usuario por Username
- **Dado que**: El usuario visualiza la lista de likes recibidos o chatea con un contacto.
- **Cuando**: Toca la tarjeta/foto en la lista de likes o el avatar en la barra superior del chat.
- **Entonces**: La aplicación abre `UserProfileDetailScreen` cargando el perfil completo correspondiente mediante `username`.

### Criterio 4: Compartir Perfil mediante Deep Link
- **Dado que**: El usuario se encuentra en la pantalla de detalle de un perfil.
- **Cuando**: Pulsa el botón de compartir.
- **Entonces**: Se despliega el selector de compartir de Android con un texto que incluye el enlace universal `https://mirailink.com/user/<username>`.

### Criterio 5: Registro con Validación Estricta de 16 Años y Género
- **Dado que**: Un nuevo usuario intenta registrarse sin fecha de nacimiento o género, o con una edad menor a 16 años.
- **Cuando**: Pulsa el botón de registrarse.
- **Entonces**: Se bloquea el registro mostrando snackbar/error informativo, y una vez registrado el usuario, fecha y género no aparecen en el formulario de edición de perfil.

### Criterio 6: Perfil Extendido con Curiosidades Gamer
- **Dado que**: Un usuario accede a editar sus atributos extendidos (estilo de vida, familia, creencias, idiomas, profesión y hasta 3 curiosidades gamer).
- **Cuando**: Guarda sus elecciones.
- **Entonces**: La información se persiste en base de datos normalizada con traducciones (`es`, `en`, `ja`) y se muestra categorizada en la pantalla de detalle.

- - -

## 6. Decisiones Resueltas

- [x] Idiomas a sembrar: Español (`es`), Inglés (`en`) y Japones (`ja`).
- [x] Facts gamer: 8 preguntas predefinidas en base de datos con localización, permitiendo responder hasta 3 en el perfil.
- [x] Edad mínima: 16 años cumplidos con validación estricta en frontend y backend.
- [x] Género y fecha de nacimiento retirados por completo del formulario y endpoint de edición de perfil (solo lectura en vista).
- [x] Lista de likes recibidos: filtra usuarios que dieron like y con los que aun no hay match. Al dar like o rechazar, desaparecen de la sección. Botón de acción rápida en cada fila.
- [x] Pantalla de detalle: carrusel fijo, mantener pulsado para pantalla completa, scroll categorizado y botones de like/rechazar si proviene de likes recibidos.
- [x] Modo Demo: datos simulados en `DemoDataSeeder` para funcionamiento offline.
