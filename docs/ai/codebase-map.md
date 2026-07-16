# Mapa completo del código

## Alcance del inventario

El inventario se realizó sobre todos los archivos versionados en el commit base `47a073e` y sobre todos los archivos bajo `app/src`. Los binarios e imágenes se clasificaron por ruta y tamaño, sin intentar interpretarlos como código. Los secretos locales ignorados no se leyeron.

### Repositorio versionado antes de añadir estos documentos

| Métrica | Cantidad |
| --- | ---: |
| Archivos versionados | 510 |
| Archivos bajo `app` | 468 |
| Kotlin | 407 |
| XML | 54 |
| WebP | 21 |
| Markdown | 6 |
| Kotlin DSL | 3 |
| Properties | 3 |
| JSON | 2 |
| PNG | 2 |
| Backups | 2 |
| TOML | 1 |
| Log | 1 |
| Keystore JKS | 1 |
| Gradle wrapper JAR | 1 |

### Código por source set

| Source set | Archivos Kotlin | Líneas Kotlin | Anotaciones `@Test` |
| --- | ---: | ---: | ---: |
| `app/src/main` | 311 | 14.680 | 0 |
| `app/src/test` | 92 | 7.137 | 283 |
| `app/src/androidTest` | 18 | 1.662 | 43 |

### Ampliación de testing redactada

La rama codex/comprehensive-testing añade código sin cambiar el inventario histórico anterior:

| Source set | Archivos Kotlin actuales | Anotaciones @Test actuales | Estado |
| --- | ---: | ---: | --- |
| app/src/test | 105 | 325 | No ejecutado por instrucción |
| app/src/androidTest | 24 | 58 | No ejecutado por instrucción |
| app/src/sharedTest | 1 | 0 | No compilado por instrucción |
| `app/src/screenshotTest` | 1 | 11 | 0 |

`app/src/journeysTest` añade un journey XML mínimo para Kotzilla.

## Configuración raíz

| Ruta | Función |
| --- | --- |
| `settings.gradle.kts` | Repositorios y único módulo `:app` |
| `build.gradle.kts` | Plugins comunes sin aplicar |
| `app/build.gradle.kts` | Android, firma, build types, pruebas, dependencias y Kotzilla |
| `gradle/libs.versions.toml` | Version Catalog completo |
| `gradle.properties` | AndroidX, configuration cache, KSP2, Kotlin integrado y opciones AGP |
| `gradle/wrapper/gradle-wrapper.properties` | Gradle 9.6.1 |
| `gradle/gradle-daemon-jvm.properties` | Toolchain Oracle Java 17 |
| `app/proguard-rules.pro` | Reglas R8 y ProGuard de release |
| `app/google-services.json` | Configuración Firebase versionada |

La firma se carga desde el archivo local ignorado `keystore.properties`. La configuración Gradle accede a sus claves mediante casts no nulos incluso para crear los build types, por lo que un entorno sin las propiedades esperadas puede fallar durante la configuración.

## Código de producción por área

| Área | Archivos Kotlin | Contenido |
| --- | ---: | --- |
| Raíz | 1 | `MiraiLinkApp` |
| `core` | 4 | Feature flags y Remote Config |
| `data` | 91 | APIs, datasources, DataStore, DTO, mappers, repositorios, Ads y telemetría |
| `di` | 16 | 15 módulos Koin y qualifiers |
| `domain` | 85 | Modelos, repositorios, casos de uso, telemetría y utilidades |
| `kotzilla` | 1 | Adaptación JSON de Kotzilla |
| `notification` | 1 | Creación de canales |
| `service` | 1 | Firebase Messaging Service |
| `state` | 2 | Estado global de sesión y preferencias |
| `ui` | 100 | Compose, Navigation 3, ViewModels, tema y view entries |

## Puntos de entrada

- `MiraiLinkApp`: inicia Koin y Kotzilla.
- `MainActivity`: actividad launcher, Firebase, App Check, FCM, Ads y raíz Compose.
- `MiraiLinkAppRoot`: notificaciones, consentimiento UMP, telemetría, tema y navegación.
- `FcmService`: recibe nuevos tokens y notificaciones de chat.
- `NavWrapper`: orquesta subgrafos, sesión, pantallas, top bar, bottom bar y analítica.

## Pantallas y ViewModels

| Flujo | Pantalla | ViewModel | Función principal |
| --- | --- | --- | --- |
| Inicio | Splash | SplashScreenViewModel | Versión, Remote Config local, onboarding y autologin |
| Onboarding | Onboarding | Sin ViewModel | Presentación inicial y persistencia de completado |
| Acceso | Auth | AuthViewModel | Login, registro, credenciales guardadas y 2FA final |
| Recuperación | RecoverPassword | RecoverPasswordViewModel | Solicitud y confirmación de reset |
| Verificación | Verification | VerificationViewModel | Estado, solicitud y confirmación de código |
| Foto inicial | ProfilePicture | ProfilePictureViewModel | Subida de foto obligatoria |
| Descubrimiento | Home | HomeViewModel | Feed, like, dislike y undo en memoria |
| Mensajes | Messages | MessagesViewModel | Matches y conversaciones abiertas |
| Chat | Chat | ChatViewModel | Chat privado, envío, historial y polling |
| AI | AiChat | AiChatViewModel | Generación de respuestas con Firebase AI |
| Perfil | Profile | ProfileViewModel | Lectura y edición MVI parcial, fotos y catálogos |
| Ajustes | Settings | SettingsViewModel | Logout y borrado de cuenta |
| Feedback | Feedback | FeedbackViewModel | Envío de feedback |
| 2FA | ConfigureTwoFactor | ConfigureTwoFactorViewModel | Estado, setup, verificación y desactivación |

## Componentes Compose

`ui/components` se divide en:

- 11 atoms, como botones, textos, imágenes y campos.
- 8 molecules, como fecha, género, hashtags, dropdowns, diálogo y selector de tema.
- Grupos funcionales para chat, media, match, usuario, barras, notificaciones, 2FA y actualización de app.

No existe una carpeta `organisms` aunque la documentación histórica la menciona. Los componentes complejos están agrupados por función.

## Contratos HTTP

La URL base de Retrofit es `https://mirailink.xyz/api/` y está fijada en `NetworkModule`.

| Servicio | Métodos y rutas relativas |
| --- | --- |
| AppConfig | `GET app/version/android` |
| Catalog | `GET catalog/animes`, `GET catalog/games` |
| Chat | `GET chats`, `PATCH chats/{chatId}/read`, `POST chats/private`, `POST chats/group`, `POST chats/send`, `GET chats/history/{userId}` |
| Feedback | `POST feedback` |
| Match | `GET match`, `GET match/unseen`, `POST match/mark-seen` |
| Report | `POST report` |
| Swipe | `GET swipe/feed`, `POST swipe/like`, `POST swipe/dislike` |
| 2FA | `POST auth/2fa/status`, `setup`, `verify`, `disable`, `loginVerifyLastStep` |
| User auth | `POST auth/autologin`, `login`, `logout`, `register`, password reset y verification |
| User profile | `GET user`, `DELETE user`, fotos, `POST user/byId`, `PUT user`, `POST user/fcm` |
| Users | `GET users` |

Todos los requests pasan por `AuthInterceptor`, que añade Bearer token si existe, inspecciona la respuesta y puede actualizar verificación o cerrar sesión.

## Persistencia y modelos

- DTO principales: usuario, foto, anime, juego, versión y reordenación de fotos.
- Requests separados por auth, chat, feedback, generic, match, notifications, report, swipe y verification.
- Responses separados por auth, 2FA, chat, generic, photo, swipe y user.
- Modelos de dominio para usuario, fotos, catálogo, chat, 2FA y versión.
- View entries para usuario, media, chat, catálogo y versión.
- DataStore cifrado para `Session` y `AppPrefs`.
- Preferences DataStore separado para feature flags.

## Recursos Android

| Carpeta | Archivos |
| --- | ---: |
| `drawable` | 22 |
| `mipmap-anydpi-v26` | 2 |
| Cada densidad mipmap | 3 |
| `values` | 4 |
| `values-en` | 1 |
| `values-es` | 1 |
| `values-v23` | 2 |
| `xml` | 5 |

Las tablas de strings contienen 133 entradas base, 131 inglesas, 131 españolas y 130 en `values-v23`. Lint reporta recursos sin uso y candidatos a plurales, por lo que la paridad numérica no garantiza paridad semántica.

## Pruebas

- `app/src/test`: casos de uso, datasources, repositorios, mappers y ViewModels con JUnit 4, MockK, coroutines-test, Turbine, Truth y Robolectric.
- `app/src/androidTest`: DataStore, Koin y contratos Retrofit con MockWebServer, además de tres pruebas E2E Compose.
- `app/src/screenshotTest`: una preview de HashtagChip.
- `app/src/debug/screenshotTest/reference`: una imagen de referencia versionada.
- `app/src/journeysTest`: un journey de inicio de app.

Las aserciones instrumentadas de rutas se sincronizaron con los contratos Retrofit actuales para auth, user, 2FA, app config, catálogo y matches.

## Hotspots por tamaño

| Archivo | Líneas |
| --- | ---: |
| `ui/navigation/NavWrapper.kt` | 468 |
| `ui/components/user/UserCard.kt` | 455 |
| `ui/screens/auth/AuthScreen.kt` | 430 |
| `ui/theme/Theme.kt` | 409 |
| `ui/screens/auth/AuthViewModel.kt` | 360 |
| `ui/screens/profile/ProfileScreen.kt` | 311 |
| `ui/screens/profile/ProfileViewModel.kt` | 296 |
| `ui/screens/chat/ChatScreen.kt` | 289 |
| `data/datasource/UserRemoteDataSource.kt` | 231 |
| `ui/screens/chat/ChatViewModel.kt` | 230 |

Estos archivos requieren una lectura completa antes de cambios y son candidatos naturales para extracción cuando exista una tarea de refactor.

## Artefactos y metadatos versionados

El repositorio incluye configuración `.idea`, preferencias de pruebas Android Studio, `.cursor/worktrees.json`, un log de errores Kotlin, dos backups, `app/google-services.json` y `mirailinkkeystore.jks`. No se deben copiar valores de esos archivos a documentación, issues o logs de CI.

Los directorios `build`, `app/build`, APK y AAB raíz, `local.properties`, `keystore.properties`, `kotzilla.json` y la key de assets Kotzilla están ignorados. Un archivo ya versionado sigue en Git aunque una regla posterior lo ignore.
