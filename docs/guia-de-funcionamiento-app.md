# Guía de Funcionamiento de MiraiLink

Bienvenido a la **Guía Oficial de Funcionamiento de MiraiLink**, la aplicación social y de citas diseñada para conectar entusiastas del anime, videojuegos, manga y cultura otaku mediante interfaces modernas en Jetpack Compose, Clean Architecture y sistemas reactivos.

---

## 1. Introducción y Arquitectura General

MiraiLink combina un diseño visual atractivo con una arquitectura limpia dividida en tres capas principales:

- **Capa UI (Presentación)**: Pantallas declarativas construidas en Jetpack Compose con componentes siguiendo el patrón Atomic Design (Atoms, Molecules, Organisms) y Navigation 3.
- **Capa de Dominio (Domain)**: Modelos de negocio inmutables, contratos de repositorios y casos de uso (`UseCases`) que encapsulan la lógica central de la aplicación.
- **Capa de Datos (Data)**: Fuentes de datos remotas (Ktor/Retrofit y Socket.IO) y locales (Room Database y Encrypted DataStore) gestionadas mediante inyección de dependencias con Koin.

MiraiLink soporta dos modos operativos intercambiables:
1. **Modo Online (Conectado)**: Conexión completa con el backend de MiraiLink, autenticación JWT, coincidencias remotas y chat en tiempo real mediante WebSockets.
2. **Modo Demostración Offline (Local)**: Experiencia interactiva autónoma sin servidores ni registro, respaldada por Room 2.7.2 para pruebas, evaluación y aprendizaje.

---

## 2. Manual del Modo Offline (Demostración)

### 2.1 ¿Qué es el Modo Demostración Offline?

El Modo Offline permite experimentar todas las características interactivas de MiraiLink directamente en el dispositivo, sin necesidad de conexión a Internet ni creación de una cuenta real.

Está especialmente diseñado para:
- Evaluar la interfaz de usuario, fluidez y navegación de la aplicación sin ingresar datos personales.
- Realizar pruebas de rendimiento y usabilidad en entornos aislados.
- Demostrar las capacidades de persistencia local y desacoplamiento arquitectónico de la aplicación.

> **Aviso de Privacidad**: Mientras este modo está activo, un banner superior permanente indica:
> *"Offline Demo Mode: Profiles and chats are simulated and do not connect to real users."*

---

### 2.2 Cómo Acceder al Modo Offline

1. Abre la aplicación MiraiLink.
2. Tras completar o saltar la introducción inicial, llegarás a la pantalla de bienvenida / inicio de sesión (`AuthScreen`).
3. En la parte inferior, debajo del botón de inicio de sesión principal, presiona el botón **"Probar Modo Demostración (Offline)"** (*Try Offline Demo Mode*).
4. La aplicación inicializará instantáneamente la base de datos Room local y te llevará directamente a la pantalla principal sin solicitar contraseñas ni correos.

---

### 2.3 Exploración del Feed y Swipe de Perfiles

En la pestaña **Inicio (Home)** podrás interactuar con tarjetas de candidatos ficticios preconfigurados (como *Aoi*, *Ren* o *Mia*):

- **Ver detalles del perfil**: Desliza verticalmente en la tarjeta para leer la biografía, edad, animes favoritos y videojuegos preferidos.
- **Galería de fotos**: Toca los indicadores de imagen en la tarjeta para alternar entre las diferentes fotos del usuario.
- **Deslizar a la izquierda (Descartar)**: Arrastra la tarjeta hacia la izquierda o presiona el botón circular azul con una **X**. El perfil se descartará localmente.
- **Deslizar a la derecha (Like)**: Arrastra la tarjeta hacia la derecha o presiona el botón circular rojo con un **Corazón**.
- **Coincidencia Instantánea (Match)**: Si el perfil tiene afinidad contigo, la app creará un nuevo Match en la base de datos local y desbloqueará una nueva conversación de chat.

---

### 2.3.1 Perfil Holo-3D

La foto visible de la tarjeta superior en Inicio y los feeds de Explorar puede reaccionar suavemente a la inclinación del móvil. En retratos compatibles, el sujeto y el fondo natural se desplazan en sentidos opuestos. Un reflejo discreto en el borde acompaña la estética de MiraiLink sin mover los textos ni los controles. Si el recorte no ofrece cobertura suficiente, se mueve la foto completa; sin sensores compatibles, permanece estática.

Se activa por defecto. Para desactivarlo, abre **Ajustes > Apariencia > Perfil Holo-3D**. La preferencia se guarda cifrada en este dispositivo. El movimiento se neutraliza al tocar, deslizar, cambiar la foto o abrirla en grande; la imagen ampliada conserva la foto original. La inclinación no produce votos ni modifica la afinidad. El efecto se detiene en segundo plano, sin foco, con ahorro de batería o animaciones del sistema deshabilitadas.

El análisis usa las fotos ya cargadas y funciona sin conexión si están disponibles en la caché existente. Fotos y recortes se procesan localmente; los derivados solo viven en memoria. Esto no excluye las métricas de uso y diagnóstico del SDK de Google. Las cinco preguntas de **FAQ > Búsqueda y tarjetas** explican el ajuste, alternativas, gestos y privacidad en español e inglés.

Consulta [implementación y privacidad](features/holo_mirai_parallax/implementation.md) y [evidencias y límites](features/holo_mirai_parallax/verification.md). La compilación no acredita calidad de recorte, fluidez ni experiencia de inclinación en un móvil físico.

### 2.4 Gestión de Matches y Conversaciones Simuladas

En la pestaña **Mensajes (Messages)** encontrarás:

- **Carrusel de Matches**: Avatares circulares de las personas con las que has conectado (por defecto *Sakura* y *Kenji*, más los nuevos perfiles a los que des Like).
- **Lista de Conversaciones**: Resumen de chats activos con foto de perfil, nombre del contacto y vista previa del último mensaje enviado o recibido.

---

### 2.5 Sala de Chat Interactiva con Respuestas Automáticas

Al tocar cualquier conversación en la lista de mensajes:

1. **Historial Completo**: Podrás leer todos los mensajes previos guardados en Room.
2. **Envío de Mensajes**: Escribe cualquier texto en el campo inferior y presiona el botón de envío (icono de avión de papel).
3. **Persistencia Inmediata**: El mensaje se guardará localmente en Room y aparecerá con tu burbuja de chat y la hora actual.
4. **Respuesta Automática Inteligente**: El contacto simulado responderá de forma natural tras 1 segundo de retardo, simulando una interacción real en tiempo real.

---

### 2.6 Ruleta de Gestos en Vivo (Minijuego Visual para Romper el Hielo)

Dentro de cualquier conversación de chat:

1. **Lanzar o Enviar Reto**: En la barra de entrada de texto, presiona el icono de mando/consola (`ic_gamepad`). Aparecerá un diálogo con dos opciones:
   - **🎯 Jugar Desafío (15s)**: Inicia de inmediato tu propia sesión de minijuego frente a la cámara.
   - **📨 Enviar reto al chat**: Envía una tarjeta interactiva a la conversación para retar a tu match.
2. **Cómo se Juega**:
   - Se abre un modal interactivo con la vista previa de la cámara frontal y una cuenta atrás de 15 segundos.
   - La app solicita imitar 4 gestos rápidos secuenciales: *Guiña el ojo izquierdo*, *Sonrisa sorpresa*, *Guiña el ojo derecho* y *Ladea la cabeza*.
   - Los gestos se evalúan en tiempo real mediante **IA local con ML Kit Face Detection** (probabilidad ocular, de sonrisa y rotación Euler).
   - Al detectar cada gesto con éxito, el móvil vibra (feedback háptico), emite un destello visual verde y avanza al siguiente reto.
3. **Privacidad Total y Coste Cero**:
   - Todo el análisis se ejecuta localmente en el procesador del dispositivo. **No se transmite vídeo ni imágenes por la red**.
4. **Celebración y Tarjeta de Chispa**:
   - Al finalizar, explota una animación de confeti en Compose y se genera una tarjeta de compatibilidad dinámica (porcentaje de chispa del 73% al 99% y título divertido).
   - Presiona **"Compartir en chat"** para publicar la tarjeta en la conversación y permitir que tu match intente superar tu puntuación.

---

### 2.7 Edición del Perfil de Demostración (`Hikari`)

En la pestaña **Perfil (Profile)** puedes gestionar tu identidad local:

- **Perfil asignado**: Por defecto juegas con el perfil de *Hikari Takahashi* (23 años, aficionada a JRPGs, Frieren, Steins;Gate y Zelda).
- **Modo Edición**:
  1. Presiona el botón **"Editar"** (*Edit*) al pie de la tarjeta de perfil.
  2. Modifica el apodo (Nickname), la biografía (Biography), el género o las preferencias.
  3. Presiona **"Guardar"** (*Save*).
  4. Los cambios se actualizarán al instante en la base de datos Room y se reflejarán en toda la aplicación.

---

### 2.8 Restauración de Datos y Salida del Modo Demo

En la pantalla de **Ajustes (Settings)** (accesible tocando el icono de engranaje en la esquina superior derecha):

- **Restablecer datos de demostración (Reset demo data)**:
  - Si has descartado todos los perfiles, quieres borrar los mensajes nuevos o restaurar el perfil de Hikari a sus valores originales, presiona este botón.
  - La base de datos Room reiniciará todas las tablas con los datos de fábrica.
- **Salir del modo demostración (Exit demo mode)**:
  - Cierra la sesión local del modo demo y te redirige de inmediato a la pantalla de inicio de sesión (`AuthScreen`) para entrar en modo online si lo deseas.

---

## 3. Guía de Uso del Modo Online (Producción)

Para conectar con otros usuarios reales:

1. **Registro e Inicio de Sesión**:
   - Pulsa "Registrarse" (*Sign Up*) en `AuthScreen` para crear una cuenta con tu nombre de usuario y contraseña.
   - Inicia sesión con tus credenciales. El token JWT se almacena de forma segura en `EncryptedDataStore`.
2. **Descubrimiento Real**:
   - Las tarjetas de candidatos se obtienen de la API REST de MiraiLink según tu ubicación y preferencias de matching.
3. **Chat en Tiempo Real**:
   - La mensajería se gestiona mediante Socket.IO con cifrado en tránsito, confirmaciones de entrega y estado de presencia en línea.

---

## 4. Estructura de Persistencia y Seguridad

| Componente | Tecnología | Propósito |
| :--- | :--- | :--- |
| **Base de Datos Demo** | Room 2.7.2 (SQLite) | Tablas `demo_user_profile`, `demo_feed_users`, `demo_matches`, `demo_chats`, `demo_messages`. |
| **Sesión Global** | `GlobalMiraiLinkSession` | Singleton reactivo que expone `isDemoMode` y `currentUser`. |
| **Credenciales Online** | `EncryptedDataStore` | Almacenamiento cifrado con Android Keystore para tokens de acceso y refresh. |
| **Repositorios** | Inversión de Dependencias (DIP) | Repositorios delegados que conmutan dinámicamente entre API remota y Room local. |

---

## 5. Galería Visual del Modo Offline

Las capturas de alta definición se encuentran almacenadas en el proyecto en `docs/screenshots/`:

- `docs/screenshots/01-auth-screen-demo-button.webp` - Acceso directo al modo offline.
- `docs/screenshots/02-home-screen-demo-feed.webp` - Feed con banner superior y tarjetas.
- `docs/screenshots/03-messages-screen-demo-matches.webp` - Matches y lista de conversaciones.
- `docs/screenshots/04-chat-screen-demo-conversation.webp` - Chat interactivo y respuestas Room.
- `docs/screenshots/05-profile-screen-demo-edit.webp` - Perfil de demostración de Hikari.
- `docs/screenshots/05b-profile-screen-editing.webp` - Formulario de edición local.
- `docs/screenshots/06-settings-screen-demo-restore.webp` - Panel de restablecimiento y salida.
- `docs/screenshots/07-auth-screen-after-exit.webp` - Retorno limpio a la pantalla de acceso.

---

## 6. Preferencias de Busqueda, Filtros de Genero y Suscripciones

MiraiLink incluye un sistema integral de configuracion de busqueda para calibrar los candidatos tanto en el Feed Principal como en el Descubrimiento por Categorias (Explore):

### 6.1 Genero del Usuario y Filtro de Genero de Busqueda

- **Generos de perfil de usuario**: El sistema cuenta estrictamente con dos generos: **Hombre** (`male`) y **Mujer** (`female`).
- **Selector de genero objetivo ("Busco")**: Permite calibrar los perfiles candidatos entre:
  - **Todos** (`all`): Opcion por defecto activa y gratuita para cualquier usuario, mezclando perfiles de todos los generos.
  - **Mujeres** (`female`): Requiere suscripcion Plus o Premium.
  - **Hombres** (`male`): Requiere suscripcion Plus o Premium.

### 6.2 Reglas segun el Plan de Suscripcion

1. **Plan Gratuito (Free)**:
   - Por defecto, el descubrimiento busca a **Todos** (`all`) los perfiles mezclados independientemente del genero del usuario.
   - La opcion "Todos" esta completamente disponible y activable en cuentas gratuitas.
   - Las opciones especificas ("Mujeres" y "Hombres") se muestran con el distintivo visual `(Plus)`. Al pulsar sobre ellas se abre el Paywall de suscripciones informando de la ventaja.
   - El radio de distancia maximo en el plan gratuito es de 250 km.

2. **Planes de Pago (MiraiLink Plus y MiraiLink Premium)**:
   - El filtrado selectivo por genero ("Mujeres" u "Hombres") queda completamente desbloqueado desde **MiraiLink Plus** en adelante.
   - El usuario suscrito puede alternar a placer entre Mujeres, Hombres o Todos sin ninguna restriccion.
   - El radio de distancia se extiende hasta 800 km (en busqueda general) o 500 km (en categorias).

### 6.3 Filtros en el Descubrimiento por Categorias

Dentro de las secciones tematicas de Explorar (por ejemplo *Anime Fans*, *Gamer*, etc.):
- Cada categoria cuenta con su propia hoja de ajustes (`CategoryDiscoverySettingsSheet`).
- El usuario puede personalizar de forma independiente tanto la distancia como el genero de busqueda para esa categoria concreta.
- En cuentas gratuitas la opcion por defecto es "Todos" (`all`). Filtrar especificamente por "Mujeres" u "Hombres" dentro de la categoria tambien requiere MiraiLink Plus o Premium.

