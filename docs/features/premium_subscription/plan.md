# [APROBADO] Plan Tecnico de Arquitectura: Sistema de Suscripcion Premium y Google Play Billing

- **Especificacion funcional asociada**: `docs/features/premium_subscription/spec.md`
- **Estado**: [APROBADO]
- **Fecha**: 2026-09-30
- **Modulo**: `:app` (`com.feryaeljustice.mirailink`) y backend `MiraiLink-Backend`

- - -

## 1. Hechos Verificados en el Proyecto (Sin Alucinaciones)

Informacion verificada rigurosamente en `gradle/libs.versions.toml`, `app/build.gradle.kts` y `MiraiLink-Backend/package.json`:

- **Lenguaje & JVM**: Kotlin `2.4.10` / Java 17
- **Compilador & Build Tool**: AGP `9.4.1`, KSP `2.3.8`
- **SDK Targets**: Min SDK `30`, Compile SDK `37`, Target SDK `37`
- **Librerias Verificadas en el Classpath de Android**:
  - UI: Jetpack Compose BOM `2026.09.00` con Material 3
  - Navegacion: Navigation 3 (`androidx.navigation3:navigation3-runtime:1.1.7`, `androidx.navigation3:navigation3-ui:1.1.7`, `lifecycle-viewmodel-navigation3:2.11.0`)
  - Inyeccion de Dependencias: Koin BOM `4.2.2` (`koin-android`, `koin-androidx-compose`)
  - Persistencia Local: Room `2.8.5` (KSP) y Encrypted DataStore `1.2.1`
  - Red & Serializacion: Retrofit `3.0.0`, OkHttp `5.5.0`, Kotlinx Serialization `1.11.0`
  - Anuncios: Google Play Services Ads `25.5.0` (`AdMobManager`)
- **Backend Verificado**: Express `5.2.1`, PostgreSQL (`pg 8.23.0`), Zod `4.6.5`
- **Libreria Seleccionada para Facturacion**:
  - `com.android.billingclient:billing-ktx:9.1.0`

- - -

## 2. Impacto Arquitectonico y Contratos por Capas

### 2.1. Backend (`MiraiLink-Backend`)
- **Migracion SQL (`src/database/migrations/009_user_subscriptions.sql`)**:
  - Tabla `user_subscriptions` con clave ajena a `users(id)`, campos `product_id`, `base_plan_id`, `purchase_token`, `status`, `expires_at`, `auto_renewing`, `created_at`, `updated_at`.
- **Rutas y Controladores**:
  - `src/controllers/subscription.controller.js`:
    - `getSubscriptionStatus`: Recupera la membresia del usuario o responde con estado Gratuito por defecto.
    - `verifySubscription`: Valida y almacena el `purchaseToken`, marcando al usuario como Premium activo.
    - `cancelSubscriptionIntent`: Registra la fecha de intencion de cancelacion y responde con la URL para cancelar en Play Store.
  - `src/routes/subscription.routes.js`: Montado en `/api/v1/subscription` protegido con middleware `authRequired`.

### 2.2. Capa de Datos en Android (`data/`)
- **Modelos DTO y Requests (`data/model/`)**:
  - `SubscriptionStatusDto.kt`: DTO de respuesta con `isPremium: Boolean`, `plan: String`, `status: String`, `expiresAt: String?`, `autoRenewing: Boolean`.
  - `VerifySubscriptionRequest.kt`: DTO con `purchaseToken: String`, `productId: String`, `basePlanId: String`, `orderId: String?`.
  - `CancelSubscriptionIntentRequest.kt`: DTO para registrar intencion de baja.
- **Servicio Remoto (`data/remote/SubscriptionApiService.kt`)**:
  - Contrato Retrofit para interactuar con los tres endpoints de suscripcion.
- **DataSource de Google Play Billing (`data/billing/BillingDataSource.kt` & `BillingClientManager.kt`)**:
  - Encapsula `BillingClient` configurado con `enableAutoServiceReconnection()` y `enablePendingPurchases(PendingPurchasesParams)`.
  - Emite el flujo reactivo de `ProductDetails` para `mirailink_premium`.
  - Emite eventos de compras recibidas mediante `PurchasesUpdatedListener`.
  - Ejecuta el reconocimiento `acknowledgePurchase` obligatorio dentro de los 3 dias.
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

### 2.4. Capa de Sesion Global (`state/`)
- **`GlobalMiraiLinkSession.kt`**:
  - Expone `val isPremium: StateFlow<Boolean>`.
  - Sincroniza el valor al iniciar sesion y ante compras o restauraciones exitosas.
- **`MainActivity.kt` & `AdMobManager.kt`**:
  - Desactiva los anuncios intersticiales cuando `isPremium` es `true`.
- **`ReceivedLikesViewModel.kt`**:
  - Conecta `uiState.isPremiumLocked` reactivamente con el estado de `isPremium`. Si es `true`, carga likes; si es `false`, muestra el bloqueo con CTA.

### 2.5. Capa de Presentacion (UI) y Navegacion
- **Navegacion 3 (`ui/navigation/`)**:
  - Nuevas claves en `AppScreen.kt`:
    - `data object SubscriptionPaywallScreen : AppScreen()`
    - `data object SubscriptionManageScreen : AppScreen()`
  - Manejo en `NavWrapper.kt`.
- **Pantalla de Ajustes (`ui/screens/settings/SettingsScreen.kt`)**:
  - Tarjeta de plan actual con icono, estado y flecha:
    - Estado Free -> navega a `SubscriptionPaywallScreen`.
    - Estado Premium -> navega a `SubscriptionManageScreen`.
- **Pantalla Paywall (`ui/screens/subscription/paywall/SubscriptionPaywallScreen.kt`)**:
  - Header estilizado, tarjeta con listado de ventajas, caja de precio dinamica de Google Play, boton CTA de 56 dp ("Suscribirse con Google Play") y boton de restaurar compras.
- **Pantalla de Gestion (`ui/screens/subscription/manage/SubscriptionManageScreen.kt`)**:
  - Resumen de membresia, fecha de renovacion, boton de cancelar suscripcion que activa dialogo de confirmacion y redireccion a Google Play.
- **Internacionalizacion**:
  - Recursos localizados en `res/values/strings.xml`, `values-es/strings.xml` y `values-en/strings.xml`.

### 2.6. Inyeccion de Dependencias (`di/koin/`)
- Modulos Koin en `AppModule.kt` y `DataModule.kt` para registrar `BillingClientManager`, `SubscriptionApiService`, `SubscriptionRepository` y ViewModels (`SubscriptionPaywallViewModel`, `SubscriptionManageViewModel`).

- - -

## 3. Estrategia de Testing

- **Pruebas Unitarias Android (`app/src/test/`)**:
  - `SubscriptionRepositoryTest.kt`: Validacion con MockK de la consulta de estado, verificacion de token y manejo de errores.
  - `SubscriptionPaywallViewModelTest.kt`: Validacion con Turbine del estado de UI al cargar producto y al disparar compra.
  - `SubscriptionManageViewModelTest.kt`: Validacion del dialogo de confirmacion y de cancelacion.
  - `ReceivedLikesViewModelTest.kt`: Validacion de la alternancia de bloqueo segun `isPremium`.
- **Pruebas en Backend (`MiraiLink-Backend/tests/`)**:
  - Tests para `subscription.routes.js` con Supertest comprobando respuestas 200 y validaciones con Zod.
- **Verificacion de Compilacion**:
  - `./gradlew assembleDebug` y `./gradlew testDebugUnitTest`.

- - -

## 4. Riesgos Tecnicos y Mitigaciones

- **Riesgo 1: Desconexion con Google Play Services**:
  - *Mitigacion*: El `BillingClient` utilizara `enableAutoServiceReconnection()` y reintentos con backoff exponencial.
- **Riesgo 2: Token de compra no reconocido dentro de los 3 dias (reembolso automatico de Google)**:
  - *Mitigacion*: El reconocimiento (`acknowledgePurchase`) se ejecuta inmediatamente despues de recibir la compra y se reintenta automaticamente en segundo plano si la red se corta.
- **Riesgo 3: Desfase entre compra en Google Play y confirmacion en backend**:
  - *Mitigacion*: La app almacena temporalmente la compra como pendiente en DataStore si el backend no responde, reintentando la sincronizacion de forma automatica en el siguiente inicio.
