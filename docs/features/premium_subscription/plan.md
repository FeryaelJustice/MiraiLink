# [APROBADO] Plan Técnico de Arquitectura: Sistema de Suscripción Premium y Google Play Billing

- **Especificación funcional asociada**: `docs/features/premium_subscription/spec.md`
- **Estado**: [APROBADO]
- **Fecha**: 2026-09-30
- **Módulo**: `:app` (`com.feryaeljustice.mirailink`) y backend `MiraiLink-Backend`

- - -

## 1. Hechos Verificados en el Proyecto (Sin Alucinaciones)

Información verificada rigurosamente en `gradle/libs.versions.toml`, `app/build.gradle.kts` y `MiraiLink-Backend/package.json`:

- **Lenguaje & JVM**: Kotlin `2.4.10` / Java 17
- **Compilador & Build Tool**: AGP `9.4.1`, KSP `2.3.8`
- **SDK Targets**: Min SDK `30`, Compile SDK `37`, Target SDK `37`
- **Librerías Verificadas en el Classpath de Android**:
  - UI: Jetpack Compose BOM `2026.09.00` con Material 3
  - Navegación: Navigation 3 (`androidx.navigation3:navigation3-runtime:1.1.7`, `androidx.navigation3:navigation3-ui:1.1.7`, `lifecycle-viewmodel-navigation3:2.11.0`)
  - Inyección de Dependencias: Koin BOM `4.2.2` (`koin-android`, `koin-androidx-compose`)
  - Persistencia Local: Room `2.8.5` (KSP) y Encrypted DataStore `1.2.1`
  - Red & Serialización: Retrofit `3.0.0`, OkHttp `5.5.0`, Kotlinx Serialization `1.11.0`
  - Anuncios: Google Play Services Ads `25.5.0` (`AdMobManager`)
- **Backend Verificado**: Express `5.2.1`, PostgreSQL (`pg 8.23.0`), Zod `4.6.5`
- **Librería Seleccionada para Facturación**:
  - `com.android.billingclient:billing-ktx:9.1.0`

- - -

## 2. Impacto Arquitectónico y Contratos por Capas

### 2.1. Backend (`MiraiLink-Backend`)
- **Migración SQL (`src/database/migrations/009_user_subscriptions.sql`)**:
  - Tabla `user_subscriptions` con clave ajena a `users(id)`, campos `product_id`, `base_plan_id`, `purchase_token`, `status`, `expires_at`, `auto_renewing`, `created_at`, `updated_at`.
- **Rutas y Controladores**:
  - `src/controllers/subscription.controller.js`:
    - `getSubscriptionStatus`: Recupera la membresía del usuario o responde con estado Gratuito por defecto.
    - `verifySubscription`: Valida y almacena el `purchaseToken`, marcando al usuario como Premium activo.
    - `cancelSubscriptionIntent`: Registra la fecha de intención de cancelación y responde con la URL para cancelar en Play Store.
  - `src/routes/subscription.routes.js`: Montado en `/api/v1/subscription` protegido con middleware `authRequired`.

### 2.2. Capa de Datos en Android (`data/`)
- **Modelos DTO y Requests (`data/model/`)**:
  - `SubscriptionStatusDto.kt`: DTO de respuesta con `isPremium: Boolean`, `plan: String`, `status: String`, `expiresAt: String?`, `autoRenewing: Boolean`.
  - `VerifySubscriptionRequest.kt`: DTO con `purchaseToken: String`, `productId: String`, `basePlanId: String`, `orderId: String?`.
  - `CancelSubscriptionIntentRequest.kt`: DTO para registrar intención de baja.
- **Servicio Remoto (`data/remote/SubscriptionApiService.kt`)**:
  - Contrato Retrofit para interactuar con los tres endpoints de suscripción.
- **DataSource de Google Play Billing (`data/billing/BillingDataSource.kt` & `BillingClientManager.kt`)**:
  - Encapsula `BillingClient` configurado con `enableAutoServiceReconnection()` y `enablePendingPurchases(PendingPurchasesParams)`.
  - Emite el flujo reactivo de `ProductDetails` para `mirailink_premium`.
  - Emite eventos de compras recibidas mediante `PurchasesUpdatedListener`.
  - Ejecuta el reconocimiento `acknowledgePurchase` obligatorio dentro de los 3 días.
- **Repositorio (`data/repository/SubscriptionRepositoryImpl.kt`)**:
  - Implementa `SubscriptionRepository` combinando el `BillingClientManager` local y `SubscriptionApiService` remoto.

### 2.3. Capa de Dominio en Android (`domain/`)
- **Modelos de Negocio (`domain/model/subscription/`)**:
  - `SubscriptionPlanType`: Enum `FREE`, `PREMIUM`.
  - `SubscriptionPlanInfo`: Modelo inmutable con `planType`, `title`, `formattedPrice`, `billingPeriod`, `isActive`, `renewalDate`, `autoRenewing`.
- **Contrato de Repositorio (`domain/repository/SubscriptionRepository.kt`)**:
  - `getSubscriptionStatus(): Flow<MiraiLinkResult<SubscriptionPlanInfo>>`
  - `getProductDetails(): Flow<SubscriptionProductDetails?>`
  - `launchPurchaseFlow(activity: Activity): MiraiLinkResult<Unit>`
  - `verifyPurchase(purchaseToken: String, productId: String): MiraiLinkResult<SubscriptionPlanInfo>`
  - `restorePurchases(): MiraiLinkResult<SubscriptionPlanInfo>`
- **Casos de Uso (`domain/usecase/subscription/`)**:
  - `GetSubscriptionStatusUseCase`
  - `LaunchBillingFlowUseCase`
  - `RestorePurchasesUseCase`
  - `CancelSubscriptionIntentUseCase`

### 2.4. Capa de Sesión Global (`state/`)
- **`GlobalMiraiLinkSession.kt`**:
  - Expone `val isPremium: StateFlow<Boolean>`.
  - Sincroniza el valor al iniciar sesión y ante compras o restauraciones exitosas.
- **`MainActivity.kt` & `AdMobManager.kt`**:
  - Desactiva los anuncios intersticiales cuando `isPremium` es `true`.
- **`ReceivedLikesViewModel.kt`**:
  - Conecta `uiState.isPremiumLocked` reactivamente con el estado de `isPremium`. Si es `true`, carga likes; si es `false`, muestra el bloqueo con CTA.

### 2.5. Capa de Presentación (UI) y Navegación
- **Navegación 3 (`ui/navigation/`)**:
  - Nuevas claves en `AppScreen.kt`:
    - `data object SubscriptionPaywallScreen : AppScreen()`
    - `data object SubscriptionManageScreen : AppScreen()`
  - Manejo en `NavWrapper.kt`.
- **Pantalla de Ajustes (`ui/screens/settings/SettingsScreen.kt`)**:
  - Tarjeta de plan actual con icono, estado y flecha:
    - Estado Free -> navega a `SubscriptionPaywallScreen`.
    - Estado Premium -> navega a `SubscriptionManageScreen`.
- **Pantalla Paywall (`ui/screens/subscription/paywall/SubscriptionPaywallScreen.kt`)**:
  - Header estilizado, tarjeta con listado de ventajas, caja de precio dinámica de Google Play, botón CTA de 56 dp ("Suscribirse con Google Play") y botón de restaurar compras.
- **Pantalla de Gestión (`ui/screens/subscription/manage/SubscriptionManageScreen.kt`)**:
  - Resumen de membresía, fecha de renovación, botón de cancelar suscripción que activa diálogo de confirmación y redirección a Google Play.
- **Internacionalización**:
  - Recursos localizados en `res/values/strings.xml`, `values-es/strings.xml` y `values-en/strings.xml`.

### 2.6. Inyección de Dependencias (`di/koin/`)
- Módulos Koin en `AppModule.kt` y `DataModule.kt` para registrar `BillingClientManager`, `SubscriptionApiService`, `SubscriptionRepository` y ViewModels (`SubscriptionPaywallViewModel`, `SubscriptionManageViewModel`).

- - -

## 3. Estrategia de Testing

- **Pruebas Unitarias Android (`app/src/test/`)**:
  - `SubscriptionRepositoryTest.kt`: Validación con MockK de la consulta de estado, verificación de token y manejo de errores.
  - `SubscriptionPaywallViewModelTest.kt`: Validación con Turbine del estado de UI al cargar producto y al disparar compra.
  - `SubscriptionManageViewModelTest.kt`: Validación del diálogo de confirmación y de cancelación.
  - `ReceivedLikesViewModelTest.kt`: Validación de la alternancia de bloqueo según `isPremium`.
- **Pruebas en Backend (`MiraiLink-Backend/tests/`)**:
  - Tests para `subscription.routes.js` con Supertest comprobando respuestas 200 y validaciones con Zod.
- **Verificación de Compilación**:
  - `./gradlew assembleDebug` y `./gradlew testDebugUnitTest`.

- - -

## 4. Riesgos Técnicos y Mitigaciones

- **Riesgo 1: Desconexión con Google Play Services**:
  - *Mitigación*: El `BillingClient` utilizará `enableAutoServiceReconnection()` y reintentos con backoff exponencial.
- **Riesgo 2: Token de compra no reconocido dentro de los 3 días (reembolso automático de Google)**:
  - *Mitigación*: El reconocimiento (`acknowledgePurchase`) se ejecuta inmediatamente después de recibir la compra y se reintenta automáticamente en segundo plano si la red se corta.
- **Riesgo 3: Desfase entre compra en Google Play y confirmación en backend**:
  - *Mitigación*: La app almacena temporalmente la compra como pendiente en DataStore si el backend no responde, reintentando la sincronización de forma automática en el siguiente inicio.
