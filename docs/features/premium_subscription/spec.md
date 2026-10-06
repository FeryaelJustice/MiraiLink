# [APROBADO] Especificación Funcional: Sistema de Suscripción Premium y Google Play Billing

- **Fecha**: 2026-09-30
- **Estado**: [APROBADO]
- **Autor / Responsable**: Antigravity & ArisGuimera SDMD Protocol
- **Módulo Afectado**: `:app` (`com.feryaeljustice.mirailink`) y backend `MiraiLink-Backend`

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**: 
  MiraiLink carece de un sistema de monetización y suscripciones en producción. Existen funcionalidades clave preparadas en el código (como ver a quién le gustas en `ReceivedLikesScreen`, búsqueda ampliada en `SearchPreferences` y reducción de publicidad AdMob) que deben estar condicionadas a un plan de pago recurrente, transparente y seguro mediante Google Play Billing.
- **Objetivo**: 
  Implementar la arquitectura completa de facturación y suscripción para admitir un "Plan Premium" de periodicidad mensual con renovación automática:
  1. Integrar Google Play Billing Library actualizada (v9.1.0).
  2. Conectar la verificación y sincronización con el backend de MiraiLink (PostgreSQL y Express).
  3. Proporcionar una pantalla atractiva de Paywall con selector de plan y CTA prominente de Google Pay.
  4. Mostrar en Ajustes (`SettingsScreen`) una tarjeta dinámica con el plan actual del usuario.
  5. Permitir la gestión del plan (si es Gratuito redirige a contratar Premium; si es Premium abre la pantalla de gestión con opción de cancelar la auto-renovacion mediante confirmación y deep link oficial de Google Play).
  6. Desbloquear las características exclusivas en toda la aplicación según el estado de suscripción.

- - -

## 2. Situación Actual

- En el cliente Android:
  - `ReceivedLikesScreen` contiene un componente estático `PremiumLockedState` desconectado del estado real.
  - `SearchSettingsSection` contiene avisos de "Premium futuro" sin validación real.
  - `AdMobManager` muestra anuncios intersticiales a todos los usuarios sin distinguir si son premium.
  - `SettingsScreen` no incluye tarjeta ni sección informativa sobre la suscripción del usuario.
  - No existe dependencia de `billing-ktx` en `libs.versions.toml` ni cliente de compras en `data/` o `domain/`.
- En el backend (`MiraiLink-Backend`):
  - No existe tabla de suscripciones (`user_subscriptions`) en la base de datos PostgreSQL.
  - No existen rutas para validar tokens de compra (`purchaseToken`) de Google Play ni para consultar el estado de membresía de un usuario.

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)
- **Google Play Billing Library**: Integración de la librería oficial de Google Play Billing `billing-ktx:9.1.0` compatible con `compileSdk 37` y target SDK 37.
- **Suscripción Mensual Única ("Plan Premium")**:
  - Producto de suscripción con ID `mirailink_premium` y plan base `monthly-autorenew`.
  - Precio de referencia configurado en Play Console de 0,99 €/mes (mínimo permitido en Europa).
  - Precio dinámico obtenido de Google Play Console mediante `ProductDetails` (mostrando divisa y formato local según el país del usuario).
  - Plan base mensual con auto-renovacion.
- **Backend MiraiLink**:
  - Migración SQL `009_user_subscriptions.sql` con tabla `user_subscriptions` (guardando `user_id`, `product_id`, `base_plan_id`, `purchase_token`, `status`, `expires_at`, `auto_renewing`).
  - Endpoint `POST /api/v1/subscription/verify` para asociar la compra de Google Play al usuario autenticado.
  - Endpoint `GET /api/v1/subscription/status` para obtener el estado actual de la suscripción (`is_premium`, fecha de expiración, estado).
  - Endpoint `POST /api/v1/subscription/cancel-intent` para registrar la intención de cancelación antes de redirigir a Google Play.
- **Capa Data y Dominio en Android**:
  - `BillingClientManager` / `BillingDataSource` para gestionar la conexión con Google Play Services, consultar `ProductDetails`, procesar compras y reconectar automáticamente.
  - `SubscriptionRepository` y use cases correspondientes: `GetSubscriptionStatusUseCase`, `PurchaseSubscriptionUseCase`, `RestorePurchasesUseCase`, `CancelSubscriptionUseCase`.
  - Integración con `GlobalMiraiLinkSession` para emitir `isPremium: StateFlow<Boolean>` reactivo en toda la app.
- **Capa de Presentación (UI)**:
  - Tarjeta en `SettingsScreen` que visualiza el plan actual ("Plan Gratuito" o "Plan Premium").
  - Pantalla Paywall modal o completa (`SubscriptionPaywallScreen`):
    - Diseño visual premium con gradientes sutiles y tarjetas informativas.
    - Lista de ventajas destacadas (Ver a quién le gustas sin límites, 0 anuncios de interrupción, radio de búsqueda ilimitado, soporte prioritario).
    - Precio mensual localizado obtenido de Google Play.
    - Botón CTA grande en la parte inferior ("Suscribirse con Google Play") que lanza la pasarela nativa.
    - Botón secundario discreto de "Restaurar compras".
  - Pantalla de Gestión de Suscripción (`SubscriptionManageScreen`):
    - Se abre si el usuario ya es Premium al pulsar la tarjeta de Ajustes.
    - Muestra estado "Activa", fecha de proxima renovación y resumen de ventajas activas.
    - Botón "Cancelar suscripción": abre diálogo de confirmación ("Deseas cancelar la renovación automática mensual?") y, tras confirmar, abre el centro de suscripciones de Google Play Store (`play.google.com/store/account/subscriptions`).
- **Desbloqueo de Funcionalidades**:
  - `ReceivedLikesScreen`: Si el usuario es Free, muestra `PremiumLockedState` con botón "Ver planes Premium" que abre el Paywall. Si es Premium, desbloquea la lista y permite interactuar y hacer match.
  - `MainActivity` / `AdMobManager`: Si el usuario es Premium, se inhibe la carga y despliegue de anuncios intersticiales.
  - Búsqueda y filtros: Permite radios superiores a 250 km cuando el usuario es Premium.
- **Internacionalización (i18n)**:
  - Textos 100% localizados en español (`values-es`), inglés (`values-en`) y japones (`values-ja`).

### 3.2. Fuera del Alcance (Out of Scope)
- Múltiples planes o niveles (ej. Gold, Platinum, VIP): solo existirá Gratuito y Premium Mensual en esta etapa.
- Planes anuales o trimestrales: se diferiran para una actualización posterior.
- Pasarelas web externas o cobro con tarjeta sin Google Play (prohibido por políticas de Google Play Store para contenido digital dentro de la app).
- Bienes consumibles (comprar monedas, boosts o superlikes individuales).

- - -

## 4. Casuísticas y Comportamiento Mobile

- **Comportamiento en Modo Online vs Modo Offline / Sandbox Demo**:
  - En **Modo Online**: Las compras se inician con Google Play Billing y el token devuelto se verifica de inmediato contra el backend de MiraiLink mediante HTTPS.
  - En **Modo Offline / Demo**: La app simula el estado Premium localmente en `SessionManager` o permite alternar el estado en la pantalla de depuración sin llamar a Google Play Services.
- **Ciclo de Vida y Recuperación de Estado**:
  - Si el proceso es interrumpido por una llamada o pasa a segundo plano durante el proceso de pago, el listener `PurchasesUpdatedListener` del singleton `BillingClientManager` captura la transacción al reanudar la app y completa el reconocimiento (`acknowledgePurchase`).
  - La pantalla de Paywall sobrevive a rotaciones de pantalla sin reiniciar la consulta de productos de Play Store.
- **Manejo de Errores y Casos de Borde**:
  - **Cancelación de compra por el usuario** (`BillingResponseCode.USER_CANCELED`): No muestra mensaje de error ruidoso; la pantalla vuelve a su estado normal sin cambios.
  - **Compra pendiente de aprobación o pago en efectivo** (`PurchaseState.PENDING`): Se informa al usuario mediante banner o snackbar de que su suscripción se activará tan pronto como se complete el pago.
  - **Fallo de conexión con Google Play** (`BillingResponseCode.SERVICE_UNAVAILABLE`): Muestra mensaje amigable localizado y habilita botón de reintento.
  - **Fallo de sincronización con el backend tras el pago**: Si el pago se efectua en Google Play pero el servidor falla momentaneamente, la compra queda pendiente de sincronización local y se reintenta en el siguiente inicio de la app mediante `RestorePurchasesUseCase`.
- **Ergonomía, Teclado y Accesibilidad**:
  - Botón CTA con altura mínima de 56 dp y área táctil generosa.
  - Soporte para Tema Claro y Tema Oscuro Material 3.
  - Soporte Edge-to-Edge (`safeDrawingPadding`).

- - -

## 5. Criterios de Aceptación (Formato Given - When - Then)

### Criterio 1: Contratación exitosa de suscripción Premium
- **Dado que**: Un usuario en plan Gratuito pulsa en la tarjeta de plan en Ajustes o en el botón del banner de Likes recibidos.
- **Cuando**: Se visualiza la pantalla `SubscriptionPaywallScreen`, pulsa el botón "Suscribirse" y completa exitosamente el pago en el diálogo nativo de Google Play.
- **Entonces**: La compra se reconoce con `acknowledgePurchase`, se envía el token al backend, el estado de la app cambia a `isPremium = true`, se muestra mensaje de bienvenida y se desbloquean los likes recibidos y la exclusión de publicidad.

### Criterio 2: Visualización y apertura de gestión de suscripción activa
- **Dado que**: Un usuario con plan Premium activo accede a Ajustes.
- **Cuando**: Observa la tarjeta de plan, la cual indica "Plan Premium" con distintivo visual, y pulsa sobre ella.
- **Entonces**: Se navega a la pantalla `SubscriptionManageScreen` mostrando los detalles del plan activo y la fecha de renovación, sin volver a mostrar el formulario de compra.

### Criterio 3: Flujo de cancelación de renovación automática
- **Dado que**: Un usuario se encuentra en `SubscriptionManageScreen`.
- **Cuando**: Pulsa el botón "Cancelar suscripción".
- **Entonces**: Se despliega un diálogo de confirmación explicando que mantendrá las ventajas hasta el final del periodo de facturación actual; al confirmar, se registra la intención en backend y se abre la página de administración de suscripciones de Google Play Store de la app.

### Criterio 4: Tolerancia y reintentos ante cancelación o error en pasarela
- **Dado que**: Un usuario abre el selector de pago de Google Play.
- **Cuando**: Cierra el diálogo de pago o pulsa fuera sin completar la transacción.
- **Entonces**: No se altera la sesión del usuario, permanece en la pantalla de Paywall y puede reintentar en cualquier momento sin errores bloqueantes.

- - -

## 6. Decisiones Resueltas

- [x] **Versión de Play Billing Library**: Se aprueba `com.android.billingclient:billing-ktx:9.1.0` (versión oficial más moderna y recomendada para target SDK 37).
- [x] **Product ID y Base Plan ID**: Se aprueba `mirailink_premium` como ID de la suscripción en Play Console y `monthly-autorenew` como Base Plan ID.
- [x] **Precio en Play Store**: Se establece 0,99 €/mes como precio de referencia mínimo en Play Console. En la app Android, el precio se formatea dinámicamente leyendo `ProductDetails`.
- [x] **Verificación en Backend**: El backend implementará la migración `009_user_subscriptions.sql` y persistira en PostgreSQL el `purchaseToken`, estado y fecha de caducidad.
