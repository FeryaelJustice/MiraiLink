# [APROBADO] Especificacion Funcional: Sistema de Suscripcion Premium y Google Play Billing

- **Fecha**: 2026-09-30
- **Estado**: [APROBADO]
- **Autor / Responsable**: Antigravity & ArisGuimera SDMD Protocol
- **Modulo Afectado**: `:app` (`com.feryaeljustice.mirailink`) y backend `MiraiLink-Backend`

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**: 
  MiraiLink carece de un sistema de monetizacion y suscripciones en produccion. Existen funcionalidades clave preparadas en el codigo (como ver a quien le gustas en `ReceivedLikesScreen`, busqueda ampliada en `SearchPreferences` y reduccion de publicidad AdMob) que deben estar condicionadas a un plan de pago recurrente, transparente y seguro mediante Google Play Billing.
- **Objetivo**: 
  Implementar la arquitectura completa de facturacion y suscripcion para admitir un "Plan Premium" de periodicidad mensual con renovacion automatica:
  1. Integrar Google Play Billing Library actualizada (v9.1.0).
  2. Conectar la verificacion y sincronizacion con el backend de MiraiLink (PostgreSQL y Express).
  3. Proporcionar una pantalla atractiva de Paywall con selector de plan y CTA prominente de Google Pay.
  4. Mostrar en Ajustes (`SettingsScreen`) una tarjeta dinamica con el plan actual del usuario.
  5. Permitir la gestion del plan (si es Gratuito redirige a contratar Premium; si es Premium abre la pantalla de gestion con opcion de cancelar la auto-renovacion mediante confirmacion y deep link oficial de Google Play).
  6. Desbloquear las caracteristicas exclusivas en toda la aplicacion segun el estado de suscripcion.

- - -

## 2. Situacion Actual

- En el cliente Android:
  - `ReceivedLikesScreen` contiene un componente estatico `PremiumLockedState` desconectado del estado real.
  - `SearchSettingsSection` contiene avisos de "Premium futuro" sin validacion real.
  - `AdMobManager` muestra anuncios intersticiales a todos los usuarios sin distinguir si son premium.
  - `SettingsScreen` no incluye tarjeta ni seccion informativa sobre la suscripcion del usuario.
  - No existe dependencia de `billing-ktx` en `libs.versions.toml` ni cliente de compras en `data/` o `domain/`.
- En el backend (`MiraiLink-Backend`):
  - No existe tabla de suscripciones (`user_subscriptions`) en la base de datos PostgreSQL.
  - No existen rutas para validar tokens de compra (`purchaseToken`) de Google Play ni para consultar el estado de membresia de un usuario.

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)
- **Google Play Billing Library**: Integracion de la libreria oficial de Google Play Billing `billing-ktx:9.1.0` compatible con `compileSdk 37` y target SDK 37.
- **Suscripcion Mensual Unica ("Plan Premium")**:
  - Producto de suscripcion con ID `mirailink_premium` y plan base `monthly-autorenew`.
  - Precio de referencia configurado en Play Console de 0,99 €/mes (minimo permitido en Europa).
  - Precio dinamico obtenido de Google Play Console mediante `ProductDetails` (mostrando divisa y formato local segun el pais del usuario).
  - Plan base mensual con auto-renovacion.
- **Backend MiraiLink**:
  - Migracion SQL `009_user_subscriptions.sql` con tabla `user_subscriptions` (guardando `user_id`, `product_id`, `base_plan_id`, `purchase_token`, `status`, `expires_at`, `auto_renewing`).
  - Endpoint `POST /api/v1/subscription/verify` para asociar la compra de Google Play al usuario autenticado.
  - Endpoint `GET /api/v1/subscription/status` para obtener el estado actual de la suscripcion (`is_premium`, fecha de expiracion, estado).
  - Endpoint `POST /api/v1/subscription/cancel-intent` para registrar la intencion de cancelacion antes de redirigir a Google Play.
- **Capa Data y Dominio en Android**:
  - `BillingClientManager` / `BillingDataSource` para gestionar la conexion con Google Play Services, consultar `ProductDetails`, procesar compras y reconectar automaticamente.
  - `SubscriptionRepository` y use cases correspondientes: `GetSubscriptionStatusUseCase`, `PurchaseSubscriptionUseCase`, `RestorePurchasesUseCase`, `CancelSubscriptionUseCase`.
  - Integracion con `GlobalMiraiLinkSession` para emitir `isPremium: StateFlow<Boolean>` reactivo en toda la app.
- **Capa de Presentacion (UI)**:
  - Tarjeta en `SettingsScreen` que visualiza el plan actual ("Plan Gratuito" o "Plan Premium").
  - Pantalla Paywall modal o completa (`SubscriptionPaywallScreen`):
    - Diseno visual premium con gradientes sutiles y tarjetas informativas.
    - Lista de ventajas destacadas (Ver a quien le gustas sin limites, 0 anuncios de interrupcion, radio de busqueda ilimitado, soporte prioritario).
    - Precio mensual localizado obtenido de Google Play.
    - Boton CTA grande en la parte inferior ("Suscribirse con Google Play") que lanza la pasarela nativa.
    - Boton secundario discreto de "Restaurar compras".
  - Pantalla de Gestion de Suscripcion (`SubscriptionManageScreen`):
    - Se abre si el usuario ya es Premium al pulsar la tarjeta de Ajustes.
    - Muestra estado "Activa", fecha de proxima renovacion y resumen de ventajas activas.
    - Boton "Cancelar suscripcion": abre dialogo de confirmacion ("Deseas cancelar la renovacion automatica mensual?") y, tras confirmar, abre el centro de suscripciones de Google Play Store (`play.google.com/store/account/subscriptions`).
- **Desbloqueo de Funcionalidades**:
  - `ReceivedLikesScreen`: Si el usuario es Free, muestra `PremiumLockedState` con boton "Ver planes Premium" que abre el Paywall. Si es Premium, desbloquea la lista y permite interactuar y hacer match.
  - `MainActivity` / `AdMobManager`: Si el usuario es Premium, se inhibe la carga y despliegue de anuncios intersticiales.
  - Busqueda y filtros: Permite radios superiores a 250 km cuando el usuario es Premium.
- **Internacionalizacion (i18n)**:
  - Textos 100% localizados en espanol (`values-es`), ingles (`values-en`) y japones (`values-ja`).

### 3.2. Fuera del Alcance (Out of Scope)
- Multiples planes o niveles (ej. Gold, Platinum, VIP): solo existira Gratuito y Premium Mensual en esta etapa.
- Planes anuales o trimestrales: se diferiran para una actualizacion posterior.
- Pasarelas web externas o cobro con tarjeta sin Google Play (prohibido por politicas de Google Play Store para contenido digital dentro de la app).
- Bienes consumibles (comprar monedas, boosts o superlikes individuales).

- - -

## 4. Casuisticas y Comportamiento Mobile

- **Comportamiento en Modo Online vs Modo Offline / Sandbox Demo**:
  - En **Modo Online**: Las compras se inician con Google Play Billing y el token devuelto se verifica de inmediato contra el backend de MiraiLink mediante HTTPS.
  - En **Modo Offline / Demo**: La app simula el estado Premium localmente en `SessionManager` o permite alternar el estado en la pantalla de depuracion sin llamar a Google Play Services.
- **Ciclo de Vida y Recuperacion de Estado**:
  - Si el proceso es interrumpido por una llamada o pasa a segundo plano durante el proceso de pago, el listener `PurchasesUpdatedListener` del singleton `BillingClientManager` captura la transaccion al reanudar la app y completa el reconocimiento (`acknowledgePurchase`).
  - La pantalla de Paywall sobrevive a rotaciones de pantalla sin reiniciar la consulta de productos de Play Store.
- **Manejo de Errores y Casos de Borde**:
  - **Cancelacion de compra por el usuario** (`BillingResponseCode.USER_CANCELED`): No muestra mensaje de error ruidoso; la pantalla vuelve a su estado normal sin cambios.
  - **Compra pendiente de aprobacion o pago en efectivo** (`PurchaseState.PENDING`): Se informa al usuario mediante banner o snackbar de que su suscripcion se activara tan pronto como se complete el pago.
  - **Fallo de conexion con Google Play** (`BillingResponseCode.SERVICE_UNAVAILABLE`): Muestra mensaje amigable localizado y habilita boton de reintento.
  - **Fallo de sincronizacion con el backend tras el pago**: Si el pago se efectua en Google Play pero el servidor falla momentaneamente, la compra queda pendiente de sincronizacion local y se reintenta en el siguiente inicio de la app mediante `RestorePurchasesUseCase`.
- **Ergonomia, Teclado y Accesibilidad**:
  - Boton CTA con altura minima de 56 dp y area tactil generosa.
  - Soporte para Tema Claro y Tema Oscuro Material 3.
  - Soporte Edge-to-Edge (`safeDrawingPadding`).

- - -

## 5. Criterios de Aceptacion (Formato Given - When - Then)

### Criterio 1: Contratacion exitosa de suscripcion Premium
- **Dado que**: Un usuario en plan Gratuito pulsa en la tarjeta de plan en Ajustes o en el boton del banner de Likes recibidos.
- **Cuando**: Se visualiza la pantalla `SubscriptionPaywallScreen`, pulsa el boton "Suscribirse" y completa exitosamente el pago en el dialogo nativo de Google Play.
- **Entonces**: La compra se reconoce con `acknowledgePurchase`, se envia el token al backend, el estado de la app cambia a `isPremium = true`, se muestra mensaje de bienvenida y se desbloquean los likes recibidos y la exclusion de publicidad.

### Criterio 2: Visualizacion y apertura de gestion de suscripcion activa
- **Dado que**: Un usuario con plan Premium activo accede a Ajustes.
- **Cuando**: Observa la tarjeta de plan, la cual indica "Plan Premium" con distintivo visual, y pulsa sobre ella.
- **Entonces**: Se navega a la pantalla `SubscriptionManageScreen` mostrando los detalles del plan activo y la fecha de renovacion, sin volver a mostrar el formulario de compra.

### Criterio 3: Flujo de cancelacion de renovacion automatica
- **Dado que**: Un usuario se encuentra en `SubscriptionManageScreen`.
- **Cuando**: Pulsa el boton "Cancelar suscripcion".
- **Entonces**: Se despliega un dialogo de confirmacion explicando que mantendra las ventajas hasta el final del periodo de facturacion actual; al confirmar, se registra la intencion en backend y se abre la pagina de administracion de suscripciones de Google Play Store de la app.

### Criterio 4: Tolerancia y reintentos ante cancelacion o error en pasarela
- **Dado que**: Un usuario abre el selector de pago de Google Play.
- **Cuando**: Cierra el dialogo de pago o pulsa fuera sin completar la transaccion.
- **Entonces**: No se altera la sesion del usuario, permanece en la pantalla de Paywall y puede reintentar en cualquier momento sin errores bloqueantes.

- - -

## 6. Decisiones Resueltas

- [x] **Version de Play Billing Library**: Se aprueba `com.android.billingclient:billing-ktx:9.1.0` (version oficial mas moderna y recomendada para target SDK 37).
- [x] **Product ID y Base Plan ID**: Se aprueba `mirailink_premium` como ID de la suscripcion en Play Console y `monthly-autorenew` como Base Plan ID.
- [x] **Precio en Play Store**: Se establece 0,99 €/mes como precio de referencia minimo en Play Console. En la app Android, el precio se formatea dinamicamente leyendo `ProductDetails`.
- [x] **Verificacion en Backend**: El backend implementara la migracion `009_user_subscriptions.sql` y persistira en PostgreSQL el `purchaseToken`, estado y fecha de caducidad.
