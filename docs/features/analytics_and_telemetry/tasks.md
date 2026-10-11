# Checklist de Tareas: Sistema Integral de Analíticas, Telemetría y Gestión de Crashes

- [x] **Fase 1: Capa de Dominio (Catálogo de Eventos & Contratos)**
  - [x] Crear `MiraiAnalyticsEvent.kt` con la jerarquía sellada de eventos (`SubscriptionEvent`, `DiscoveryEvent`, `ProfileEvent`, `ExploreEvent`, `LikesEvent`, `ChatEvent`, `AuthEvent`, `SettingsEvent`, `SystemEvent.AppException`).
  - [x] Actualizar la interfaz `AnalyticsTracker.kt` para admitir `fun logEvent(event: MiraiAnalyticsEvent)`.
  - [x] Actualizar la interfaz `CrashReporter.kt` para soportar `recordException(...)`, `log(message: String)`, `setCustomKey(...)` y `setUserId(...)`.

- [x] **Fase 2: Capa de Datos & Telemetría (Composite Trackers & Correlación)**
  - [x] Implementar `LogcatAnalyticsTracker.kt` y `LogcatCrashReporter.kt` para visualización limpia en Logcat en compilaciones Debug.
  - [x] Implementar `CompositeAnalyticsTracker.kt` con aislamiento de excepciones.
  - [x] Implementar `CompositeCrashReporter.kt` con delegación múltiple y correlación automática hacia `AnalyticsTracker` (emitiendo `SystemEvent.AppException`).
  - [x] Actualizar `FirebaseAnalyticsTracker.kt` para mapear `MiraiAnalyticsEvent` a `Bundle`.
  - [x] Actualizar `FirebaseCrashlyticsReporter.kt` para registrar excepciones con claves personalizadas y breadcrumbs en `FirebaseCrashlytics`.
  - [x] Implementar `MiraiUncaughtExceptionHandler.kt` para capturar crashes fatales no controlados antes del cierre del proceso.

- [x] **Fase 3: Inyección de Dependencias (Koin TelemetryModule)**
  - [x] Configurar `TelemetryModule.kt` para registrar los delegados (`FirebaseAnalyticsTracker`, `FirebaseCrashlyticsReporter`, loggers de depuración) y proveer los singletons `CompositeAnalyticsTracker` y `CompositeCrashReporter`.
  - [x] Inicializar `MiraiUncaughtExceptionHandler` en la configuración inicial de la aplicación.
  - [x] Asegurar que `CrashReporter` y `AnalyticsTracker` estén disponibles para inyección en todos los ViewModels.

- [x] **Fase 4: Instrumentación en Capa de Presentación (Navegación & ViewModels)**
  - [x] Instrumentar `NavWrapper.kt` para registrar eventos `screen_view` y breadcrumbs en `CrashReporter.log("Navigated to: ...")`.
  - [x] Instrumentar `SubscriptionPaywallViewModel.kt` y `SubscriptionManageViewModel.kt` (embudo de compras, paywall y reporte de excepciones de facturación).
  - [x] Instrumentar `HomeViewModel.kt` (swipes con `is_demo_mode`, vistas de tarjeta, giros, matches, límites diarios y reporte de fallos).
  - [x] Instrumentar `ProfileViewModel.kt` (edición, modificación de campos, reordenación de fotos, guardado y errores de actualización).
  - [x] Instrumentar `ExploreViewModel.kt` y `CategoryFeedViewModel.kt` (categorías, filtros aplicados, scroll).
  - [x] Instrumentar `ReceivedLikesViewModel.kt` (visita a pestaña, clic en fotos borrosas, matches de likes).
  - [x] Instrumentar `MessagesViewModel.kt` y complementar `ChatViewModel.kt` (apertura de conversaciones, mensajes y fallos en WebSockets/Socket.IO).
  - [x] Instrumentar `SettingsViewModel.kt` (tema, preferencias de notificación).
  - [x] Instrumentar `AuthViewModel.kt` (UserProperties: `subscription_tier`, `is_verified`, `account_gender` sincronizadas en Analytics y Crashlytics).

- [x] **Fase 5: Pruebas Unitarias y Automatizadas**
  - [x] Crear test unitario `CompositeAnalyticsTrackerTest.kt`.
  - [x] Crear test unitario `CompositeCrashReporterTest.kt` (verificando la correlación automática con Analytics).
  - [x] Crear test unitario `FirebaseAnalyticsTrackerTest.kt`.
  - [x] Actualizar tests de ViewModels para validar la emisión de eventos de analíticas y reporte de errores.
  - [x] Ejecutar suite completa con `.\gradlew.bat testDebugUnitTest`.

- [x] **Fase 6: Verificación Final y Documentación**
  - [x] Compilar el proyecto completo con `.\gradlew.bat assembleDebug`.
  - [x] Actualizar `walkthrough.md` documentando la solución y su extensibilidad para Kotzilla u otras herramientas.
