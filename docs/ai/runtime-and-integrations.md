# Runtime e integraciones

## Backend REST

`NetworkModule` fija dos qualifiers:

- Base web: `https://mirailink.xyz`
- Base API: `https://mirailink.xyz/api/`

OkHttp usa timeouts de 10 segundos para conexión, lectura y escritura. En debug añade un `HttpLoggingInterceptor` a nivel BODY. Después añade `AuthInterceptor`.

`AuthInterceptor`:

1. Lee un token cacheado desde `SessionManager`.
2. Añade `Authorization: Bearer <token>`.
3. Consume el body completo de la respuesta y lo reconstruye.
4. Busca los booleanos JSON `verified` y `shouldLogout`.
5. Marca la sesión como no verificada si procede.
6. Limpia sesión para respuestas 401, 404 o 502 cuando `shouldLogout` sea true.

El backend no forma parte del repositorio. No hay selección de entorno, flavors de endpoint ni inyección desde BuildConfig. Para desarrollo local habría que introducir una fuente de URL explícita.

## Chat y mensajería

Hay dos mecanismos:

### REST activo

`ChatViewModel` crea o recupera un chat privado, marca lectura, carga participantes y consulta el historial cada 3 segundos. El envío también usa REST. Los mensajes enviados se añaden optimistamente con un UUID local.

### Socket.IO cableado pero inactivo

`SocketService` se crea con la base web, puede conectar, desconectar, escuchar, emitir y retirar listeners. `ChatRepositoryImpl` escucha el evento `receive_message`, y existen casos de uso de conexión, desconexión y escucha. No se encontró ninguna llamada de UI a esos tres casos de uso.

El flujo de chat de grupo crea el chat, pero luego llama a un método TODO con un ID vacío. No está terminado.

## Firebase

### Analytics y Crashlytics

Koin expone contratos `AnalyticsTracker` y `CrashReporter`. Navigation registra `screen_view`, Home registra un supuesto deep link base y Auth registra éxito o error de login.

La raíz Compose desactiva telemetría al inicio y la vuelve a activar según el resultado de UMP. Esa política llama directamente a Firebase desde una utilidad colocada actualmente en `domain`.

### Messaging

`MainActivity` obtiene un token al arrancar. `FcmService` recibe rotaciones de token. Ambos esperan hasta 1,5 segundos a que aparezca una sesión autenticada y envían el token al endpoint `user/fcm`. Si no hay sesión, no existe persistencia de token pendiente.

Las notificaciones con `type=new_message` usan nombre, preview y conversation ID. El PendingIntent abre `MainActivity`, pero no incluye el identificador de conversación, por lo que no abre el chat concreto.

### Remote Config y feature flags

`RemoteConfigManager` define:

- Default XML para `gemini_model_name`, con valor `gemini-2.5-flash`.
- Fetch y activate con intervalo mínimo de una hora.
- Lectura del modo navideño.

No existe ninguna llamada a `RemoteConfigManager.initialize()`. El ViewModel de Splash y el módulo de AI leen valores antes de que los defaults XML y el fetch hayan sido aplicados por este wrapper. El modelo de Gemini puede quedar vacío y el modo navideño en false.

### Firebase AI

`aiModule` crea `GenerativeModel` con el nombre de Remote Config, `GeminiDataSource`, `AiRepositoryImpl` y `GenerateContentUseCase`. `AiChatViewModel` envía prompts y expone Idle, Loading, Success o Error.

`AiRepository` también está registrado en `repositoryModule`, duplicando el binding.

### App Check

`MainActivity` instala `DebugAppCheckProviderFactory` sin condición por build type. La dependencia debug de App Check está declarada como `implementation`. Esto debe separarse por variante y usar un provider de producción para release.

## AdMob y consentimiento UMP

- El manifest contiene el application ID de Ads.
- `AdMobManager` contiene un ad unit ID de interstitial.
- `MainActivity` inicializa el manager y programa anuncios.
- `MiraiLinkAppRoot` resuelve UMP y también llama a `MobileAds.initialize`.
- `AdMobManager.initialize` vuelve a llamar a `MobileAds.initialize`.
- En debug se registran dos IDs de dispositivo de prueba.

La inicialización está duplicada y una parte ocurre antes de resolver consentimiento. La política de mostrar un interstitial a los 10 segundos y cada 5 minutos debe revisarse como decisión de producto y cumplimiento.

## Credential Manager

`CredentialHelper` usa PasswordCredential para guardar y recuperar usuario y contraseña. Auth llama a este helper para autofill y persistencia tras acceso.

Lint detecta dos problemas:

- Falta la meta-data de Digital Asset Links en el manifest.
- `getCredential` no maneja explícitamente `NoCredentialException`.

El helper está dentro de `domain/util` aunque depende de Android Context y AndroidX Credentials.

## Almacenamiento cifrado

Session y preferencias de onboarding se serializan a JSON y se cifran con AES-256-GCM. Android Keystore genera la clave. El payload guarda IV y ciphertext. El manifest permite backups, pero las reglas excluyen el directorio DataStore de cloud backup y device transfer.

La Preferences DataStore de feature flags usa el almacenamiento estándar. Al ubicarse en el directorio DataStore también queda cubierta por la exclusión de backup basada en ruta.

## Deep links

El manifest declara HTTPS autoVerify para:

- `mirailink.xyz`
- `www.mirailink.xyz`
- `/verification`
- `/recover_password`
- `/login`

La Activity no procesa la URI entrante y Navigation 3 no contiene un parser de deep links. Además, cada elemento `<data>` se combina dentro del mismo intent filter según las reglas Android, por lo que conviene validar el filtro generado y sus combinaciones.

La app sí abre la política de privacidad externa con `deepLinkPrivacyPolicyUrl`.

## Permisos y componentes Android

| Elemento | Uso observado |
| --- | --- |
| `INTERNET` | Retrofit, Socket.IO, Firebase y Ads |
| `POST_NOTIFICATIONS` | Solicitado en Compose con rationale |
| `CAMERA` | Solicitado al editar foto de perfil |
| `AD_ID` | AdMob |
| `SYSTEM_ALERT_WINDOW` | Declarado, sin uso localizado |
| Cámara como feature requerida | Excluye dispositivos sin cámara |
| FileProvider | Comparte imágenes desde cache con URI temporal |
| FcmService | Servicio no exportado para `MESSAGING_EVENT` |

## Configuración de red

`network_security_config.xml` permite tráfico claro en la configuración base, fuerza HTTPS para `mirailink.xyz` y permite HTTP para localhost y direcciones comunes de emulador. Lint lo reporta como `InsecureBaseConfiguration` porque cualquier dominio no cubierto puede usar HTTP.

No se encontró certificate pinning ni una configuración separada entre debug y release.
