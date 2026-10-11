# Walkthrough: Sistema Integral de Analíticas, Telemetría y Gestión de Crashes

## Resumen de Cambios

Se ha completado e implementado el sistema completo de analíticas de negocio, telemetría y captura de errores/crashes para MiraiLink, siguiendo estrictamente la metodología **SDMD** (*Spec-Driven Mobile Development*) y las directrices de código limpio.

---

## 1. Capa de Dominio (Domain Layer)

- [`MiraiAnalyticsEvent.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/domain/telemetry/MiraiAnalyticsEvent.kt): Catálogo unificado y fuertemente tipado de eventos:
  - `SubscriptionEvent`: `PaywallViewed`, `PlanSelected`, `PurchaseInitiated`, `PurchaseSuccess`, `PurchaseCancelled`, `PurchaseFailed`, `SubscriptionRestored`.
  - `DiscoveryEvent`: `CardViewed`, `CardFlipped`, `CardSwiped` (con atributo `is_demo_mode`), `MatchCreated`, `DailyLimitReached`.
  - `ProfileEvent`: `ProfileEditOpened`, `FieldEdited`, `PhotosReordered`, `ProfileSaveSuccess`, `ProfileSaveFailed`.
  - `ExploreEvent`: `CategoryClicked`, `FeedScrolled`, `FilterApplied`.
  - `LikesEvent`: `ReceivedLikesViewed`, `BlurredLikeClicked`, `ReceivedLikeMatched`.
  - `ChatEvent`: `ChatListOpened`, `ConversationOpened`, `MessageSent`, `CapsuleUnlocked`.
  - `AuthEvent`: `OnboardingStepCompleted`, `LoginSuccess`, `LoginFailed`, `RegisterSuccess`, `RegisterFailed`, `Logout`.
  - `SettingsEvent`: `ThemeChanged`, `NotificationToggled`.
  - `NavigationEvent`: `ScreenView`, `DeepLinkOpened`.
  - `SystemEvent`: `AppException` (correlación directa con errores y crashes).
- [`AnalyticsTracker.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/domain/telemetry/AnalyticsTracker.kt): Contrato abstracto para emisión desacoplada de eventos y propiedades de usuario (`setUserId`, `setUserProperty`).
- [`CrashReporter.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/domain/telemetry/CrashReporter.kt): Contrato abstracto para registro de excepciones no fatales, breadcrumbs de navegación, claves contextuales y usuario.

---

## 2. Capa de Datos & Telemetría (Data Layer)

- [`CompositeAnalyticsTracker.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/data/telemetry/CompositeAnalyticsTracker.kt):
  - Patrón Composite con aislamiento individual de excepciones mediante `runCatching` por delegado.
- [`CompositeCrashReporter.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/data/telemetry/CompositeCrashReporter.kt):
  - Multidelegación para reporteros de crash.
  - **Correlación automática con analíticas**: cada llamada a `recordException` emite automáticamente un evento `SystemEvent.AppException` hacia `AnalyticsTracker`.
- [`FirebaseAnalyticsTracker.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/data/telemetry/FirebaseAnalyticsTracker.kt):
  - Adaptador a Google Analytics 4 (`FirebaseAnalytics`). Inicialización segura para entornos sin Google Services / tests.
- [`FirebaseCrashlyticsReporter.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/data/telemetry/FirebaseCrashlyticsReporter.kt):
  - Adaptador a Firebase Crashlytics con metadata tipada, breadcrumbs y claves dinámicas.
- [`LogcatAnalyticsTracker.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/data/telemetry/LogcatAnalyticsTracker.kt) & [`LogcatCrashReporter.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/data/telemetry/LogcatCrashReporter.kt):
  - Registro formateado en Logcat activo en builds `DEBUG` sin impacto en producción.
- [`MiraiUncaughtExceptionHandler.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/data/telemetry/MiraiUncaughtExceptionHandler.kt):
  - Handler global instalado en [`MiraiLinkApp.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/MiraiLinkApp.kt) para registrar crashes fatales no controlados antes del cierre del proceso.

---

## 3. Inyección de Dependencias (Koin)

- [`TelemetryModule.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/di/koin/TelemetryModule.kt):
  - Configura singletons resilientes de `AnalyticsTracker` y `CrashReporter`.
  - Fácilmente extensible: añadir un nuevo proveedor (como Kotzilla, Datadog o Sentry) solo requiere incorporar el delegado a la lista en este módulo.
- [`ViewModelModule.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt):
  - Inyecta `AnalyticsTracker` y `CrashReporter` en todos los ViewModels del sistema.

---

## 4. Instrumentación de ViewModels

- **Navegación**: [`NavAnalyticsViewModel.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavAnalyticsViewModel.kt) registra `screen_view` y breadcrumbs en cada cambio de ruta y deeplink.
- **Suscripción y Monetización**: [`SubscriptionPaywallViewModel.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallViewModel.kt) y [`SubscriptionManageViewModel.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageViewModel.kt) registran visitas, selección de planes, inicios de compra, éxitos, cancelaciones, errores y restauraciones.
- **Descubrimiento y Swipes**: [`HomeViewModel.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt) registra swipes (con flag `is_demo_mode`), giros de tarjeta, creación de matches y alcance de límites diarios.
- **Perfil**: [`ProfileViewModel.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt) registra aperturas de edición, campos modificados, reordenación de fotos y guardados/fallos.
- **Explorar**: [`ExploreViewModel.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreViewModel.kt) y [`CategoryFeedViewModel.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt) registran categorías visitadas, filtros y scroll.
- **Likes Recibidos**: [`ReceivedLikesViewModel.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesViewModel.kt) registra visitas, clics en fotos difuminadas y matches.
- **Mensajería & Chat**: [`MessagesViewModel.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesViewModel.kt) y [`ChatViewModel.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt) registran aperturas, envíos y excepciones de conexión de red/sockets.
- **Ajustes**: [`SettingsViewModel.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsViewModel.kt) registra cambios de tema y canales de notificación.
- **Autenticación**: [`AuthViewModel.kt`](file:///c:/Users/nano9/AndroidStudioProjects/MiraiLink/app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt) sincroniza propiedades del usuario en Analytics y Crashlytics (`subscription_tier`, `is_verified`, `account_gender`).

---

## 5. Verificación y Resultados de Pruebas

1. **Tests Unitarios**:
   - Ejecución completa con `.\gradlew.bat testDebugUnitTest`.
   - **Resultado**: 520 tests ejecutados y pasando exitosamente (`BUILD SUCCESSFUL`).
2. **Compilación Debug APK**:
   - Ejecución completa con `.\gradlew.bat assembleDebug`.
   - **Resultado**: APK debug compilado exitosamente (`BUILD SUCCESSFUL`).
3. **Limpieza de Código**:
   - No quedan variables sin usar, funciones huérfanas ni implementaciones duplicadas de trackers.
