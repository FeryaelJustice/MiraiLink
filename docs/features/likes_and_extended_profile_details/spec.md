# [APROBADO] Especificacion Funcional: Likes Recibidos, Detalle de Usuario por Username y Perfil Extendido

- **Fecha**: 2026-09-24
- **Estado**: [APROBADO]
- **Autor / Responsable**: Antigravity & FeryaelJustice
- **Modulos Afectados**: `:app` (`com.feryaeljustice.mirailink`), `MiraiLink-Backend` (Express 5 + PostgreSQL)

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**:
  - Los usuarios no pueden ver quien ha mostrado interes en ellos (likes recibidos) antes de hacer match.
  - No existe una pantalla dedicada de detalle de perfil accesible de forma universal mediante deep links (`mirailink.com/user/<username>`), ni se puede consultar el perfil completo al chatear tocando el avatar del destinatario.
  - El registro no exige de forma estricta la edad (fecha de nacimiento con edad minima de 16 años) ni el genero. Ademas, estos campos estaban expuestos a modificacion posterior en la edicion de perfil.
  - El perfil de usuario carece de informacion enriquecida sobre estilo de vida, datos personales, idiomas hablados, familia y curiosidades / hechos ("facts" estilo gamer) organizados por categorias con internacionalizacion (`es`, `en`, `ja`).
- **Objetivo**:
  - Incorporar la seccion "Gente a la que le gustas" en el tercer slot de la barra inferior de navegacion, con arquitectura preparada para bloqueo Premium en el futuro.
  - Construir la pantalla completa `UserProfileDetailScreen` navegable por `username`, con carrusel de imagenes en altura fija, vista completa al mantener pulsado, secciones categorizadas con scroll, boton de compartir enlace nativo, e integracion al pulsar el avatar en el chat.
  - Asegurar la captura obligatoria de fecha de nacimiento (edad minima de 16 años requerida) y genero en el registro, y eliminarlos por completo de los formularios y endpoints de edicion de perfil (solo lectura una vez registrados).
  - Implementar el esquema de base de datos normalizado para perfiles extendidos (estilo de vida, familia, religion, zodiaco, politica, idiomas, educacion, profesion y 8 curiosidades/facts estilo gamer con maximo 3 respuestas por usuario) con soporte i18n (`es`, `en`, `ja`) y tablas intermedias.
  - Proveer paridad y datos de prueba en el modo Demo Offline con Room.

- - -

## 2. Situacion Actual

- La barra de navegacion inferior (`MiraiLinkBottomBar`) solo cuenta con tres tabs: Inicio (`HomeScreen`), Mensajes (`MessagesScreen`) y Perfil (`ProfileScreen`).
- En el chat (`ChatScreen` y `ChatTopBar`), mantener pulsado el avatar abre la foto en pantalla completa, pero un toque simple (tap) no abre el perfil del interlocutor.
- La pantalla de detalle de un usuario externo no existe como pantalla dedicada desacoplada con ruta por `username`; la visualizacion de perfiles se limita a las cartas de descubrimiento (`UserCard` / `PublicUserCard`) y no soporta deep links directos.
- En registro (`AuthScreen` y `auth.controller.js`), solo se solicitan `username`, `email` y `password`. `birthdate` y `gender` se editaban en el perfil; ahora deben retirarse por completo de la edicion.
- El perfil solo almacena intereses de anime y videojuegos (`user_anime_interests`, `user_game_interests`). No existen tablas ni modelos para habitos (fumar, beber), orientacion sexual, religion, zodiaco, politica, familia, idiomas hablados, profesion ni curiosidades estilo gamer ("facts").

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)

1. **Pantalla y Tab "Gente a la que le gustas" (Who Liked You)**:
   - Nuevo tab en `MiraiLinkBottomBar` colocado como tercer elemento (despues de Mensajes y antes de Perfil).
   - Icono de corazon (`ic_heart`).
   - Lista lazy de usuarios que dieron like al usuario autenticado y con los que **no hay match todavia**, ordenada por fecha descendente (`created_at DESC`).
   - Si se acepta (like) o se rechaza (dislike), el usuario desaparece de la lista de likes recibidos.
   - Cada elemento de la lista muestra foto circular (estilo avatar del chat), nombre / nickname, edad y un boton de accion rapida para hacer match inmediato.
   - Al pulsar sobre la foto, nombre o tarjeta, navega a la pantalla de detalle de usuario (`UserProfileDetailScreen`).
   - Componente desacoplado para estado bloqueado Premium (`PremiumLockedState`) preparado para activarse en el futuro.
2. **Pantalla de Detalle de Usuario (`UserProfileDetailScreen`)**:
   - Navegacion mediante clave/identificador unico inmutable: `username`.
   - Soporte para deep link nativo Android: esquema web `https://mirailink.com/user/{username}` (o scheme `mirailink://user/{username}`).
   - Boton de retroceso desacoplado que gestione tanto la pila interna como la salida hacia la app principal si se proviene de un deep link externo.
   - Encabezado con carrusel de fotos a altura fija, indicador de fotos multiples, gesto de pulsacion prolongada para ver a pantalla completa (`FullscreenImagePreview`).
   - Secciones con scroll continuo sobre el fondo de la aplicacion, divididas por categorias:
     - Informacion Basica (edad calculada, genero, orientacion sexual, residencia/distancia si aplica).
     - Curiosidades / Facts estilo gamer (hasta 3 preguntas elegidas por el usuario y sus respuestas).
     - Estilo de Vida (habitos de tabaco, alcohol).
     - Familia (opciones combinadas: tengo hijos, quiero hijos, etc.).
     - Creencias y Personalidad (signo del zodiaco, religion, postura politica).
     - Educacion y Profesion (nivel maximo de estudios, profesion en texto libre).
     - Idiomas que habla (etiquetas localizadas segun idioma activo).
     - Intereses (animes y juegos existentes).
   - Boton de accion "Compartir perfil" que invoca el dialogo nativo de Android (`Intent.ACTION_SEND`) con la URL del perfil.
   - Botones de accion Like / Rechazar en la parte inferior si el usuario visualizado procede de likes recibidos o aun no hay match con el.
3. **Punto de Entrada desde el Chat**:
   - En `ChatTopBar`, un tap simple en la foto de perfil navega a `UserProfileDetailScreen` con el `username` del destinatario.
   - Si ya hay un match activo (como en el chat), los botones de Like/Rechazar no se muestran en el detalle del perfil.
   - La pulsacion larga (`onLongPress`) se preserva para abrir la foto a pantalla completa.
4. **Registro Obligatorio de Edad (Minimo 16 Años) y Genero Inmutables**:
   - Validacion en backend (`registerSchema`): `birthdate` (ISO Date YYYY-MM-DD, verificando `>= 16` años) y `gender` (enum valido) son campos obligatorios.
   - En el frontend (`AuthScreen`), adicion de selectores de fecha de nacimiento (`BirthdateField`) y genero (`GenderSelector`) en el paso de registro.
   - Verificacion con snackbar si la edad es menor de 16 años: "Debes tener al menos 16 años para registrarte" (localizado en `es`, `en`, `ja`).
   - Retiro total de `birthdate` y `gender` del formulario de edicion de perfil (`UserCard`, `EditProfileUiState`, `updateProfile` de backend). Solo se muestran en modo vista.
5. **Esquema de Base de Datos y Backend para Perfil Extendido**:
   - Soporte para idiomas `es`, `en`, `ja` en `supported_languages`.
   - Tablas maestras e intermedias para catalogos localizados:
     - `relationship_goals` y `relationship_goal_translations` + tabla intermedia `user_relationship_goals`.
     - `family_options` y `family_option_translations` + tabla intermedia `user_family_options` (seleccion multiple: "no tengo hijos", "ya tengo hijos", "quiero hijos", "no se si quiero hijos").
     - `religions` y `religion_translations` + asignacion al usuario.
     - `zodiac_signs` y `zodiac_sign_translations` + asignacion al usuario.
     - `political_stances` y `political_stance_translations` + asignacion al usuario.
     - `smoking_habits` y `smoking_habit_translations` + asignacion al usuario.
     - `drinking_habits` y `drinking_habit_translations` + asignacion al usuario.
     - `sexual_orientations` y `sexual_orientation_translations` + asignacion al usuario.
     - `spoken_languages` y `spoken_language_translations` + tabla intermedia `user_spoken_languages`.
     - `education_levels` y `education_level_translations` + asignacion al usuario.
     - Profesion como texto libre `profession VARCHAR(100)` en tabla de usuario.
     - `profile_prompts` (8 preguntas gamer precargadas) y `profile_prompt_translations` + tabla `user_profile_prompts` (`user_id`, `prompt_id`, `answer` max 300 caracteres, limite maximo de 3 por usuario).
   - Endpoint backend `GET /swipe/likes-received`: lista paginada de usuarios que dieron like a `req.user.id` sin match previo, con orden `likes.created_at DESC`.
   - Endpoint backend `GET /user/by-username/:username`: detalle publico extendido de un usuario por su username.
   - Endpoint `PUT /user`: guardado de datos extendidos (sin permitir alterar genero ni fecha de nacimiento).
6. **Soporte en Modo Demo Offline (Android)**:
   - Seeder simulado con datos de likes recibidos, detalle de usuario por username y atributos extendidos en `DemoDataSeeder.kt` y `DemoUserRepositoryImpl.kt`.

### 3.2. Fuera del Alcance (Out of Scope)

- Cobro real o pasarela de pagos para el modo Premium.
- Deshacer match (unmatch) - se abordara en una funcionalidad futura.
- Subida de mas de 4 fotos de perfil.

- - -

## 4. Casuisticas y Comportamiento Mobile

- **Comportamiento en Modo Online vs Modo Offline Demo**:
  - En Modo Online: Consultas y swipes/likes a la API REST.
  - En Modo Offline Demo: `DemoUserRepositoryImpl` proveera datos locales simulados completos para likes recibidos y detalle de perfil.
- **Ciclo de Vida y Recuperacion de Estado**:
  - `UserProfileDetailViewModel` y `ReceivedLikesViewModel` usan `SavedStateHandle`.
  - Navegacion resiliente ante rotacion o apertura directa via deep link.
- **Estados Vacios (Empty States) y Manejo de Errores**:
  - Empty state en Likes: "Aun no tienes me gusta nuevos. Sigue descubriendo perfiles en Inicio".
  - Manejo de reintentos con `MiraiLinkButton`.
- **Ergonomia, Teclado y Accesibilidad**:
  - Elementos tactiles de al menos 48 x 48 dp.
  - Soporte completo para temas claro y oscuro Material 3.
  - `imePadding()` en campos de entrada de profesion y respuestas a preguntas gamer.

- - -

## 5. Criterios de Aceptacion (Formato Given - When - Then)

### Criterio 1: Visualizacion de Likes Recibidos
- **Dado que**: Un usuario autenticado accede a la aplicacion.
- **Cuando**: Pulsa el tercer icono en la barra inferior (Gente a la que le gustas).
- **Entonces**: Se carga la lista de perfiles que han dado like (sin match previo) ordenados de mas reciente a mas antiguo, con foto circular, nombre, edad y boton de match rapido.

### Criterio 2: Accion Rapida de Match o Rechazo
- **Dado que**: El usuario esta en la lista de likes recibidos o en la pantalla de detalle de un like recibido.
- **Cuando**: Pulsa el boton de dar like (match) o el boton de rechazar.
- **Entonces**: El perfil desaparece de la lista de likes recibidos y, en caso de like, se crea el match correspondiente.

### Criterio 3: Acceso a Detalle de Usuario por Username
- **Dado que**: El usuario visualiza la lista de likes recibidos o chatea con un contacto.
- **Cuando**: Toca la tarjeta/foto en la lista de likes o el avatar en la barra superior del chat.
- **Entonces**: La aplicacion abre `UserProfileDetailScreen` cargando el perfil completo correspondiente mediante `username`.

### Criterio 4: Compartir Perfil mediante Deep Link
- **Dado que**: El usuario se encuentra en la pantalla de detalle de un perfil.
- **Cuando**: Pulsa el boton de compartir.
- **Entonces**: Se despliega el selector de compartir de Android con un texto que incluye el enlace universal `https://mirailink.com/user/<username>`.

### Criterio 5: Registro con Validacion Estricta de 16 Años y Genero
- **Dado que**: Un nuevo usuario intenta registrarse sin fecha de nacimiento o genero, o con una edad menor a 16 años.
- **Cuando**: Pulsa el boton de registrarse.
- **Entonces**: Se bloquea el registro mostrando snackbar/error informativo, y una vez registrado el usuario, fecha y genero no aparecen en el formulario de edicion de perfil.

### Criterio 6: Perfil Extendido con Curiosidades Gamer
- **Dado que**: Un usuario accede a editar sus atributos extendidos (estilo de vida, familia, creencias, idiomas, profesion y hasta 3 curiosidades gamer).
- **Cuando**: Guarda sus elecciones.
- **Entonces**: La informacion se persiste en base de datos normalizada con traducciones (`es`, `en`, `ja`) y se muestra categorizada en la pantalla de detalle.

- - -

## 6. Decisiones Resueltas

- [x] Idiomas a sembrar: Español (`es`), Ingles (`en`) y Japones (`ja`).
- [x] Facts gamer: 8 preguntas predefinidas en base de datos con localizacion, permitiendo responder hasta 3 en el perfil.
- [x] Edad minima: 16 años cumplidos con validacion estricta en frontend y backend.
- [x] Genero y fecha de nacimiento retirados por completo del formulario y endpoint de edicion de perfil (solo lectura en vista).
- [x] Lista de likes recibidos: filtra usuarios que dieron like y con los que aun no hay match. Al dar like o rechazar, desaparecen de la seccion. Boton de accion rapida en cada fila.
- [x] Pantalla de detalle: carrusel fijo, mantener pulsado para pantalla completa, scroll categorizado y botones de like/rechazar si proviene de likes recibidos.
- [x] Modo Demo: datos simulados en `DemoDataSeeder` para funcionamiento offline.
