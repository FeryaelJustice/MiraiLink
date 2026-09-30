# Checklist de Tareas: Sistema de Suscripcion Premium y Google Play Billing

- [x] **Fase 1: Backend & Base de Datos (`MiraiLink-Backend`)**
  - [x] 1.1 Crear archivo de migracion SQL `src/database/migrations/009_user_subscriptions.sql` con tabla `user_subscriptions` e indices
  - [x] 1.2 Implementar controlador `src/controllers/subscription.controller.js` con logica para `getSubscriptionStatus`, `verifySubscription` y `cancelSubscriptionIntent`
  - [x] 1.3 Crear esquema de validacion Zod en `src/validation/subscription.schema.js`
  - [x] 1.4 Crear rutas en `src/routes/subscription.routes.js` y registrar el router en `src/app.js`
  - [x] 1.5 Crear tests de integracion en `tests/integration/subscription.test.js` y verificar ejecucion limpia con `npm run test:unit`

- [x] **Fase 2: Dependencias y Capa Data en Android (`:app`)**
  - [x] 2.1 Anadir `play-billing-ktx = "9.1.0"` en `gradle/libs.versions.toml` y dependencia en `app/build.gradle.kts`
  - [x] 2.2 Crear DTOs de suscripcion: `SubscriptionStatusDto.kt`, `VerifySubscriptionRequest.kt`, `CancelSubscriptionIntentRequest.kt`
  - [x] 2.3 Crear interfaz de red `SubscriptionApiService.kt` y registrar en el modulo de red de Koin
  - [x] 2.4 Implementar `BillingClientManager.kt` para conectar con Google Play Billing, consultar `ProductDetails`, procesar compras y reconocer transacciones
  - [x] 2.5 Implementar `SubscriptionRepositoryImpl.kt` integrando `BillingClientManager` y `SubscriptionApiService`

- [x] **Fase 3: Capa de Dominio (`domain/`)**
  - [x] 3.1 Crear modelos de dominio inmutables en `domain/model/subscription/`: `SubscriptionPlanType.kt` y `SubscriptionPlanInfo.kt`
  - [x] 3.2 Definir contrato de interfaz `domain/repository/SubscriptionRepository.kt`
  - [x] 3.3 Implementar Casos de Uso: `GetSubscriptionStatusUseCase.kt`, `LaunchBillingFlowUseCase.kt`, `RestorePurchasesUseCase.kt`, `CancelSubscriptionIntentUseCase.kt`
  - [x] 3.4 Escribir tests unitarios con MockK para los Casos de Uso en `app/src/test/java/com/feryaeljustice/mirailink/domain/usecase/subscription/`

- [x] **Fase 4: Sesion Global, Ajustes y Desbloqueo de Ventajas**
  - [x] 4.1 Exponer `isPremium: StateFlow<Boolean>` en `GlobalMiraiLinkSession.kt` y sincronizarlo al iniciar sesion y ante cambios de membresia
  - [x] 4.2 Actualizar `MainActivity.kt` para desactivar anuncios intersticiales de `AdMobManager` cuando `isPremium == true`
  - [x] 4.3 Actualizar `ReceivedLikesViewModel.kt` y `ReceivedLikesScreen.kt` para desbloquear la lista de likes cuando `isPremium == true` y mostrar boton a Paywall cuando es `false`
  - [x] 4.4 Actualizar `SearchSettingsSection.kt` para permitir radios superiores a 250 km sin bloqueo si `isPremium == true`

- [x] **Fase 5: Capa de Presentacion (UI) y Navegacion 3**
  - [x] 5.1 Anadir cadenas localizadas en `res/values/strings.xml`, `values-es/strings.xml` y `values-en/strings.xml` (titulos, beneficios, precios, dialogos)
  - [x] 5.2 Registrar claves de navegacion `SubscriptionPaywallScreen` y `SubscriptionManageScreen` en `AppScreen.kt` y `NavWrapper.kt`
  - [x] 5.3 Crear componente de tarjeta de plan actual `CurrentPlanCard` e integrarlo en `SettingsScreen.kt`
  - [x] 5.4 Implementar `SubscriptionPaywallViewModel.kt` y `SubscriptionPaywallScreen.kt` con selector visual, beneficios destacados y boton CTA prominente
  - [x] 5.5 Implementar `SubscriptionManageViewModel.kt` y `SubscriptionManageScreen.kt` con fecha de renovacion, beneficios activos y dialogo de cancelacion con redireccion a Google Play
  - [x] 5.6 Registrar ViewModels y dependencias en modulos de Koin (`AppModule.kt`, `ViewModelModule.kt`)

- [x] **Fase 6: Verificacion Final y Tests**
  - [x] 6.1 Escribir tests unitarios de ViewModels (`SubscriptionPaywallViewModelTest.kt`, `SubscriptionManageViewModelTest.kt`) con Turbine y MockK
  - [x] 6.2 Ejecutar suite completa de tests unitarios: `.\gradlew.bat testDebugUnitTest`
  - [x] 6.3 Ejecutar compilacion limpia de depuracion: `.\gradlew.bat assembleDebug`
