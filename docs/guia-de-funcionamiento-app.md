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

### 2.6 Edición del Perfil de Demostración (`Hikari`)

En la pestaña **Perfil (Profile)** puedes gestionar tu identidad local:

- **Perfil asignado**: Por defecto juegas con el perfil de *Hikari Takahashi* (23 años, aficionada a JRPGs, Frieren, Steins;Gate y Zelda).
- **Modo Edición**:
  1. Presiona el botón **"Editar"** (*Edit*) al pie de la tarjeta de perfil.
  2. Modifica el apodo (Nickname), la biografía (Biography), el género o las preferencias.
  3. Presiona **"Guardar"** (*Save*).
  4. Los cambios se actualizarán al instante en la base de datos Room y se reflejarán en toda la aplicación.

---

### 2.7 Restauración de Datos y Salida del Modo Demo

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
