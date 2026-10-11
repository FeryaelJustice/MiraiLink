# [APROBADO] Plan Técnico de Arquitectura: Sistema Integral de Analíticas, Telemetría y Gestión de Crashes

- **Especificación funcional asociada**: `docs/features/analytics_and_telemetry/spec.md`
- **Estado**: [APROBADO]
- **Fecha**: 2026-10-10
- **Módulo**: `:app` (`com.feryaeljustice.mirailink`)

---

## 1. Hechos Verificados en el Proyecto (Sin Alucinaciones)

Información verificada rigurosamente en `gradle/libs.versions.toml` y `app/build.gradle.kts`:

- **Lenguaje & JVM**: Kotlin `2.4.20` / Java 17 toolchain
- **Compilador & Build Tool**: AGP `9.4.1`, KSP `2.3.12`
- **SDK Targets**: Min SDK `30`, Compile SDK `37`, Target SDK `37`
- **Librerías de Telemetría y Monitorización Verificadas**:
  - Firebase BoM: `34.19.0` (`com.google.firebase:firebase-bom`)
  - Firebase Analytics: `com.google.firebase:firebase-analytics`
  - Firebase Crashlytics: `com.google.firebase:firebase-crashlytics` con plugin Gradle `3.0.8`
  - Kotzilla: Plugin `3.0.0` ya incorporado en el proyecto
  - Inyección de Dependencias: Koin BOM `4.2.2` con `koin-android` y `koin-core`
  - UI: Jetpack Compose BOM `2026.09.00` con Material 3
  - Navegación: Navigation 3 (`androidx.navigation3:navigation3-core:1.2.0`)

---

## 2. Impacto Arquitectónico y Contratos por Capas

```mermaid
flowchart TD
    subgraph UI_Layer["Capa UI (ViewModels / Navigation)"]
        VM["ViewModels (Paywall, Home, Chat, etc.)"]
        NAV["NavWrapper (LaunchedEffect currentKey)"]
    end

    subgraph Domain_Layer["Capa Domain (Contratos Puros)"]
        AT_INTF["interface AnalyticsTracker"]
        CR_INTF["interface CrashReporter"]
        EVENTS["sealed interface MiraiAnalyticsEvent"]
    end

    subgraph Data_Layer["Capa Data (Implementaciones Compuestas)"]
        COMP_AT["CompositeAnalyticsTracker"]
        COMP_CR["CompositeCrashReporter"]
        
        FA["FirebaseAnalyticsTracker"]
        LOG_AT["LogcatAnalyticsTracker (Debug)"]
        
        FC["FirebaseCrashlyticsReporter"]
        LOG_CR["LogcatCrashReporter (Debug)"]
        KOTZ["Kotzilla / Futuros Proveedores"]
    end

    VM --> AT_INTF
    VM --> CR_INTF
    NAV --> AT_INTF
    NAV --> CR_INTF

    AT_INTF <|.. COMP_AT
    CR_INTF <|.. COMP_CR

    COMP_AT --> FA
    COMP_AT --> LOG_AT

    COMP_CR --> FC
    COMP_CR --> LOG_CR
    COMP_CR -. "Extensible" .-> KOTZ
    COMP_CR -. "Correlacion de Crashes" .-> AT_INTF
```

### 2.1. Capa de Dominio (`domain/telemetry/`)

- **Contrato `AnalyticsTracker.kt`**:
  ```kotlin
  package com.feryaeljustice.mirailink.domain.telemetry

  interface AnalyticsTracker {
      fun logEvent(name: String, params: Map<String, Any?> = emptyMap())
      fun logEvent(event: MiraiAnalyticsEvent)
      fun setUserId(id: String?)
      fun setUserProperty(name: String, value: String?)
  }
  ```

- **Contrato `CrashReporter.kt`**:
  ```kotlin
  package com.feryaeljustice.mirailink.domain.telemetry

  interface CrashReporter {
      fun recordException(
          throwable: Throwable,
          reason: String? = null,
          screen: String? = null,
          metadata: Map<String, Any?> = emptyMap(),
      )
      fun recordNonFatal(throwable: Throwable) = recordException(throwable)
      fun log(message: String)
      fun setUserId(id: String?)
      fun setCustomKey(key: String, value: String)
  }
  ```

- **Catálogo de Eventos (`MiraiAnalyticsEvent.kt`)**:
  - `SubscriptionEvent`: `PaywallViewed`, `PlanSelected`, `CheckoutInitiated`, `PurchaseSuccess`, `PurchaseCancelled`, `PurchaseFailed`, `RestorePurchasesClicked`, `RestorePurchasesResult`.
  - `DiscoveryEvent`: `CardViewed`, `CardSwiped`, `CardFlipped`, `MatchCreated`, `DailyLimitReached`.
  - `ProfileEvent`: `ProfileViewed`, `ProfileEditStarted`, `ProfileFieldUpdated`, `ProfilePhotosReordered`, `ProfilePhotoAdded`, `ProfileEditSaved`, `VerificationCompleted`.
  - `ExploreEvent`: `CategorySelected`, `FilterApplied`, `FeedScrolled`.
  - `LikesEvent`: `ReceivedLikesViewed`, `BlurredLikeClicked`, `ReceivedLikeMatched`.
  - `ChatEvent`: `ChatListOpened`, `ConversationOpened`, `MessageSent`, `CapsuleUnlocked`.
  - `AuthEvent`: `LoginSuccess`, `LoginFailed`, `RegisterSuccess`, `Logout`.
  - `SettingsEvent`: `ThemeChanged`, `NotificationToggled`.
  - `SystemEvent`:
    - `AppException(exceptionType: String, reason: String, screen: String?, isFatal: Boolean, metadata: Map<String, Any?>)`

### 2.2. Capa de Datos (`data/telemetry/`)

1. **`FirebaseAnalyticsTracker.kt`**:
   - Mapea `MiraiAnalyticsEvent` y diccionarios a `Bundle` de Android y los remite a `FirebaseAnalytics.logEvent`.
2. **`LogcatAnalyticsTracker.kt`**:
   - Imprime en `Log.d("MiraiAnalytics", ...)` en modo debug con formato legible `{evento}: {parámetros}`.
3. **`CompositeAnalyticsTracker.kt`**:
   - Despacha cada evento a la lista de trackers configurados aislando excepciones con `try-catch`.
4. **`FirebaseCrashlyticsReporter.kt`**:
   - Remite excepciones a `FirebaseCrashlytics.recordException`.
   - Asigna claves personalizadas con `setCustomKey`.
   - Deja breadcrumbs en `FirebaseCrashlytics.log`.
5. **`LogcatCrashReporter.kt`**:
   - Imprime en `Log.e("MiraiCrash", ...)` las excepciones reportadas y en `Log.v("MiraiBreadcrumb", ...)` los mensajes de navegación.
6. **`CompositeCrashReporter.kt`**:
   - Despacha a los reporters registrados (Firebase, Logcat, y ranura futura para Kotzilla).
   - **Correlación con Analytics**: Recibe opcionalmente una referencia o lazy provider a `AnalyticsTracker`. Cada vez que se registra una excepción con `recordException()`, emite automáticamente el evento `SystemEvent.AppException` a la analítica, vinculando la causa del error con la sesión del usuario.
7. **`MiraiUncaughtExceptionHandler.kt`**:
   - Instalado en la inicialización de la app como manejador de excepciones no controladas a nivel de proceso (`Thread.setDefaultUncaughtExceptionHandler`). Registra el último estado y breadcrumb en Crashlytics antes de delegar en el handler original del sistema.

### 2.3. Inyección de Dependencias (`di/koin/TelemetryModule.kt`)

```kotlin
val telemetryModule = module {
    single { FirebaseAnalytics.getInstance(androidContext()) }
    single { FirebaseCrashlytics.getInstance() }

    single { FirebaseAnalyticsTracker(get()) }
    single { LogcatAnalyticsTracker() }

    single<AnalyticsTracker> {
        val delegates = mutableListOf<AnalyticsTracker>()
        delegates.add(get<FirebaseAnalyticsTracker>())
        if (BuildConfig.DEBUG) {
            delegates.add(get<LogcatAnalyticsTracker>())
        }
        CompositeAnalyticsTracker(delegates)
    }

    single { FirebaseCrashlyticsReporter(get()) }
    single { LogcatCrashReporter() }

    single<CrashReporter> {
        val delegates = mutableListOf<CrashReporter>()
        delegates.add(get<FirebaseCrashlyticsReporter>())
        if (BuildConfig.DEBUG) {
            delegates.add(get<LogcatCrashReporter>())
        }
        // Pasamos lazy provider o referencia de AnalyticsTracker para correlacion automatica
        CompositeCrashReporter(
            delegates = delegates,
            analyticsTracker = get()
        )
    }
}
```

### 2.4. Capa de Presentación (Instrumentación en ViewModels y Navegación)

1. **`NavWrapper.kt`**:
   - Al cambiar de ruta (`LaunchedEffect(currentKey)`):
     - `navAnalyticsVm.logScreen(currentKey.debugRouteName())`
     - Registrar breadcrumb en `CrashReporter.log("Navigated to: ${currentKey.debugRouteName()}")`
2. **`SubscriptionPaywallViewModel.kt` & `SubscriptionManageViewModel.kt`**:
   - Inyectar `AnalyticsTracker` y `CrashReporter`.
   - Eventos de embudo: `PaywallViewed`, `PlanSelected`, `CheckoutInitiated`, `PurchaseSuccess`, `PurchaseCancelled`.
   - Errores de billing: `PurchaseFailed` y `CrashReporter.recordException(throwable, reason = "Billing failed", screen = "paywall")`.
3. **`HomeViewModel.kt`**:
   - Swipes (`CardSwiped` con `isDemoMode`), giros de tarjeta (`CardFlipped`), visualización de perfiles (`CardViewed`), matches (`MatchCreated`), límite diario (`DailyLimitReached`).
   - Errores de carga de feed reportados con `CrashReporter.recordException`.
4. **`ProfileViewModel.kt`**:
   - Edición iniciada (`ProfileEditStarted`), campos modificados (`ProfileFieldUpdated`), fotos reordenadas (`ProfilePhotosReordered`), fotos agregadas (`ProfilePhotoAdded`), guardado exitoso/fallido (`ProfileEditSaved`).
5. **`ExploreViewModel.kt` & `CategoryFeedViewModel.kt`**:
   - Selección de categorías (`CategorySelected`), filtros aplicados (`FilterApplied`), scroll en feed.
6. **`ReceivedLikesViewModel.kt`**:
   - Apertura de pestaña (`ReceivedLikesViewed`), clic en fotos borrosas para detonar el paywall (`BlurredLikeClicked`), matches aceptados (`ReceivedLikeMatched`).
7. **`MessagesViewModel.kt` & `ChatViewModel.kt`**:
   - Apertura de conversaciones (`ConversationOpened`), mensajes enviados (`MessageSent`), afinidad desbloqueada (`CapsuleUnlocked`).
   - Errores de conexión Socket.IO reportados con `CrashReporter.recordException`.
8. **`AuthViewModel.kt`**:
   - Registro de `UserProperties` (`subscription_tier`, `is_verified`, `account_gender`) y sincronización de UID anónimo con `setUserId` en Analytics y Crashlytics.
9. **`SettingsViewModel.kt`**:
   - Cambios de tema (`ThemeChanged`) y configuración de notificaciones (`NotificationToggled`).

---

## 3. Integración Futura con Kotzilla u Otras Herramientas

El diseño desacoplado garantiza que integrar Kotzilla o cualquier otra herramienta de monitorización sea inmediato:
1. Crear una clase `KotzillaCrashReporter(private val kotzillaClient: ...): CrashReporter` que implemente el contrato.
2. Añadirla a la lista de delegados en `TelemetryModule.kt`:
   `delegates.add(get<KotzillaCrashReporter>())`
3. Ningún ViewModel, UseCase ni pantalla de la UI sufrirá alteración alguna.

---

## 4. Estrategia de Testing

- **Pruebas Unitarias (`app/src/test/`)**:
  - `CompositeAnalyticsTrackerTest.kt`: Verificar delegación a múltiples trackers y captura de excepciones.
  - `CompositeCrashReporterTest.kt`: Verificar delegación de crashes, registro de breadcrumbs y correlación automática emitiendo `SystemEvent.AppException` hacia `AnalyticsTracker`.
  - `FirebaseAnalyticsTrackerTest.kt`: Verificar mapeo de eventos tipados a Bundle de GA4.
  - `SubscriptionPaywallViewModelTest.kt`: Verificar emisión de eventos del embudo de compra y reporte de errores en caso de fallo de facturación.
  - Ejecutar `./gradlew testDebugUnitTest` para asegurar estabilidad completa.

---

## 5. Riesgos Técnicos y Mitigaciones

- **Riesgo 1**: Recursión infinita si `CompositeCrashReporter` falla al reportar el evento analítico correlacionado.
  - **Mitigación**: El despacho correlacionado a `AnalyticsTracker` se encuentra envuelto en un bloque `try-catch` aislado que no reintenta ni re-despacha a `CrashReporter`.
- **Riesgo 2**: Rendimiento del hilo principal ante serialización de excepciones.
  - **Mitigación**: Normalización de trazas liviana y despacho no bloqueante.
