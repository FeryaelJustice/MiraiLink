# [APROBADO] Especificación Funcional: Sistema Integral de Analíticas, Telemetría y Gestión de Crashes

- **Fecha**: 2026-10-10
- **Estado**: [APROBADO]
- **Autor / Responsable**: MiraiLink Team / Antigravity Agent
- **Módulo Afectado**: `:app` (`com.feryaeljustice.mirailink`)

---

## 1. Problema y Objetivo

- **Problema que resuelve**: 
  MiraiLink carecía de visibilidad sobre el comportamiento de sus usuarios (conversiones a Premium, swipes, perfiles, chat) y sobre la estabilidad técnica de la app (crashes fatales, excepciones silenciadas, fallos de red o errores en el flujo de pagos). Cuando ocurría un fallo técnico o una caída inesperada, no se recopilaban motivos estructurados, contexto de pantalla ni trazas correlacionadas con la sesión de analíticas, imposibilitando diagnosticar por qué se perdió una venta o qué causó el cierre imprevisto.
- **Objetivo**: 
  Implementar un sistema integral, unificado y desacoplado de telemetría que cubra tanto **analíticas de comportamiento de producto** como **monitorización técnica de fallos y crashes**. El sistema registrará errores fatales y no fatales en Crashlytics con metadatos contextuales (pantalla, breadcrumbs, ID anónimo), y correlacionará simultáneamente cada excepción con un evento analítico en Firebase GA4 (`app_exception` / `handled_exception`) para entender su impacto directo en los embudos de usuario. La arquitectura será modular y extensible mediante contratos de Clean Architecture (`AnalyticsTracker`, `CrashReporter`), lista para incorporar fácilmente herramientas como Kotzilla u otras plataformas de observabilidad.

---

## 2. Situación Actual

- **Dependencias y configuración**: 
  Firebase BoM `34.19.0`, plugin `com.google.firebase.crashlytics` (`crashlytics-gradle = 3.0.8`) y `firebase-crashlytics` están declarados en `gradle/libs.versions.toml` y `app/build.gradle.kts`. El plugin Kotzilla (`kotzilla = 3.0.0`) también está configurado en el proyecto.
- **Implementación existente**: 
  Existen interfaces básicas `AnalyticsTracker` y `CrashReporter` en `domain/telemetry/`, con implementaciones `FirebaseAnalyticsTracker` y `FirebaseCrashlyticsReporter`. Sin embargo:
  1. No están tipadas y carecen de catálogo de eventos de error.
  2. No existe correlación: un error reportado a Crashlytics no se refleja en Analytics para análisis de negocio.
  3. No se recopilan breadcrumbs de navegación ni contexto del estado de la app previo al fallo.
  4. Los ViewModels capturan excepciones en bloques `catch` de forma aislada sin reportar a telemetría.
  5. En depuración (`debug`), las excepciones no se muestran con formato estructurado en Logcat.

---

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)

1. **Catálogo de Eventos de Analítica (`MiraiAnalyticsEvent`)**:
   - **Monetización (Premium Paywall)**: `paywall_viewed`, `paywall_plan_selected`, `subscription_checkout_initiated`, `subscription_purchase_success`, `subscription_purchase_cancelled`, `subscription_purchase_failed`, `subscription_restore_clicked`, `subscription_restore_result`.
   - **Discovery & Swipes (Home)**: `profile_card_viewed`, `card_swiped` (con flag `is_demo_mode`), `profile_card_flipped`, `match_dialog_shown`, `daily_limit_reached`.
   - **Perfil & Edición**: `profile_viewed`, `profile_edit_started`, `profile_field_updated`, `profile_photos_reordered`, `profile_photo_added`, `profile_edit_saved`, `profile_verification_completed`.
   - **Explore & Filtros**: `explore_category_selected`, `explore_filter_applied`, `explore_feed_scrolled`.
   - **Likes Recibidos**: `received_likes_viewed`, `blurred_like_clicked`, `received_like_matched`.
   - **Mensajería & Affinity Capsule**: `chat_list_opened`, `conversation_opened`, `message_sent`, `affinity_capsule_unlocked`.
   - **Autenticación & Onboarding**: `onboarding_step_completed`, `login_success`, `login_failed`, `register_success`, `logout`.
   - **Ajustes & Sistema**: `theme_changed`, `notification_preference_toggled`.
   - **Errores y Excepciones de Sistema (`app_exception`)**:
     - `exception_type`: clase del error (`NetworkError`, `BillingError`, `DatabaseException`, `SocketException`, etc.).
     - `reason`: mensaje o causa descriptiva normalizada.
     - `screen_name`: pantalla donde se produjo el fallo.
     - `is_fatal`: indicador booleano de si causó interrupción crítica o fue recuperado.

2. **Gestión Integral de Crashes y Excepciones (`CrashReporter`)**:
   - `recordException(throwable: Throwable, reason: String? = null, metadata: Map<String, Any?> = emptyMap())`: Envía a Crashlytics con claves personalizadas (`custom_keys`).
   - `log(message: String)`: Deja breadcrumbs en la sesión de Crashlytics (y consola Debug) para reconstruir los pasos previos al crash.
   - `setUserId(id: String?)`: Asigna el identificador anónimo de sesión.
   - `setCustomKey(key: String, value: String)`: Establece contexto persistente (versión, modo demo, tier de suscripción).
   - **Correlación Automática con Analytics**: La implementación de telemetría despacha simultáneamente el evento analítico `app_exception` cada vez que se reporta una excepción, permitiendo medir en Google Analytics qué porcentaje de usuarios sufrieron incidencias técnicas.

3. **Arquitectura Desacoplada y Extensible**:
   - `CompositeCrashReporter`: Despacha a `FirebaseCrashlyticsReporter` y, en `BuildConfig.DEBUG`, a un `LogcatCrashReporter` legible. Deja una ranura abierta para delegar en Kotzilla u otras herramientas sin alterar capas superiores.
   - `CompositeAnalyticsTracker`: Despacha a `FirebaseAnalyticsTracker` y `LogcatAnalyticsTracker`.
   - `MiraiUncaughtExceptionHandler`: Interceptor global de excepciones no capturadas a nivel de hilo/aplicación para garantizar que los crashes inesperados dejen rastro de contexto y breadcrumbs antes de la terminación del proceso.

4. **Propiedades de Usuario (User Properties)**:
   - Sincronización de `subscription_tier`, `is_verified` y `account_gender` tanto en Analytics como en las claves de Crashlytics.

5. **Tratamiento del Modo Offline Demo (Sandbox)**:
   - Parámetro contextual `is_demo_mode = true` en todos los eventos y clave `is_demo_mode = "true"` en Crashlytics para separar incidencias de prueba de incidentes en producción.

### 3.2. Fuera del Alcance (Out of Scope)

- Transmisión de datos sensibles personales (PII): contraseñas, tokens JWT, coordenadas GPS exactas, texto de chats privados.
- Subida de archivos binarios pesados (heap dumps o grabaciones de pantalla) a la nube.

---

## 4. Casuísticas y Comportamiento Mobile

- **Modo Online vs Modo Offline**:
  - Crashlytics almacena localmente los informes de crash no fatales y fatales en disco, sincronizándolos automáticamente en el siguiente arranque con red.
- **Ciclo de Vida y Breadcrumbs**:
  - Al cambiar de pantalla en `NavWrapper`, se registra un breadcrumb `Navigated to [screen_name]` en `CrashReporter`, de modo que cualquier caída posterior incluya el historial de pantallas recorridas.
- **Tolerancia a Fallos en la Propia Telemetría**:
  - Todas las operaciones en `CrashReporter` y `AnalyticsTracker` están aisladas con bloques `try-catch` para asegurar que un problema en los servicios de Google o en la librería nunca provoque un crash secundario.
- **Rendimiento UI**:
  - Registro asíncrono y no bloqueante que no genera congelamientos (*jank*) ni ralentiza transiciones de Jetpack Compose.

---

## 5. Criterios de Aceptación (Formato Given - When - Then)

### Criterio 1: Registro y Correlación de Excepción en Flujo de Pagos (Paywall)
- **Dado que**: El usuario intenta suscribirse a Premium pero Google Play Billing arroja una excepción de red o rechazo de tarjeta.
- **Cuando**: El `SubscriptionPaywallViewModel` detecta la excepción y la envía a `CrashReporter.recordException(throwable, reason = "Billing flow failed", metadata = mapOf("tier" to "premium"))`.
- **Entonces**: 
  1. Se registra la excepción no fatal en Crashlytics con sus metadatos.
  2. Se emite simultáneamente en Analytics el evento `app_exception(exception_type = "BillingError", reason = "Billing flow failed", screen = "subscription_paywall", is_fatal = false)`.
  3. En modo Debug, Logcat imprime con formato legible tanto el reporte de crash como el evento analítico.

### Criterio 2: Breadcrumbs de Navegación ante Crash Fatal Inesperado
- **Dado que**: El usuario navega desde Auth hasta Home, luego a Profile y finalmente al Chat.
- **Cuando**: Ocurre un crash no capturado (ej. NullPointerException o OutOfMemory).
- **Entonces**: `MiraiUncaughtExceptionHandler` asegura que los breadcrumbs de navegación previa (`Auth -> Home -> Profile -> Chat`) y las UserProperties queden guardados en el reporte de Crashlytics antes de la terminación del proceso.

### Criterio 3: Embudo Completo de Suscripción y Conversión
- **Dado que**: El usuario entra al paywall desde los likes borrosos (`trigger_source = "likes_blur"`).
- **Cuando**: Selecciona el plan anual y completa la compra.
- **Entonces**: Se emiten en orden `paywall_viewed`, `paywall_plan_selected`, `subscription_checkout_initiated` y `subscription_purchase_success`.

### Criterio 4: Swipes Diarios y Corte de Límite en Home
- **Dado que**: El usuario desliza tarjetas en Home evaluando candidatos.
- **Cuando**: Da like a una tarjeta y posteriormente alcanza el límite de swipes gratuitos diarios.
- **Entonces**: Se emite `card_swiped(action = "like", target_user_id = "...", is_demo_mode = ...)` en cada tarjeta y `daily_limit_reached(limit_type = "swipes")` al alcanzar el límite.

---

## 6. Resoluciones Acordadas

- [x] **Decisión 1**: Se utilizará Firebase GA4 + Crashlytics + Logger en consola para Debug, con arquitectura de adaptadores desacoplada y preparada para Kotzilla y otros proveedores.
- [x] **Decisión 2**: Cada error o crash reportado a Crashlytics emitirá automáticamente un evento correlacionado en Analytics para análisis de conversión y causas de caída.
- [x] **Decisión 3**: En Modo Offline Demo, los eventos y crashes se enviarán marcados con `is_demo_mode = true` para aislarlos en filtros.
- [x] **Decisión 4**: Se mantendrán sincronizadas las propiedades de usuario `subscription_tier`, `is_verified` y `account_gender` en Analytics y Crashlytics.
