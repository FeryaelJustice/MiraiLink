# Suscripciones, compras y gestión

[Guía maestra](../../guia-maestra.md) | [Mapa de funcionalidades](../funcionalidades.md)

## Responsabilidad y recorrido

BillingClientManager obtiene ProductDetails y ofertas; SubscriptionRepositoryImpl observa eventos y llama verifySubscription con purchaseToken, producto y base plan. Los estados observables determinan Plus/Premium en UI. Los precios fallback no garantizan una oferta disponible para comprar.

## Alternativas y efectos

Oferta sin token, producto inexistente, pago pendiente o cancelación de diálogo no son compra confirmada. Una cancel-intent devuelve enlace de Google Play; no equivale a cancelar facturación desde la app. El servidor guarda compras sin verificación externa, limitación registrada en [hallazgos](../hallazgos.md).

## Fuentes para estudiar

- [BillingClientManager.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/billing/BillingClientManager.kt)
- [SubscriptionRepositoryImpl.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt)
- [SubscriptionApiService.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/SubscriptionApiService.kt)

Revisión: 2026-10-01, por inspección del código. Consultar [verificación](../desarrollo-y-verificacion.md) para resultados ejecutados.

## Selección y restauración de ofertas

launchBillingFlow conserva dos grafías del base plan trimestral y, si no encuentra coincidencia, usa la primera oferta. El fallback de ProductDetails también puede elegir Premium si falta el producto solicitado. El BillingResult inmediato solo refleja el lanzamiento del flujo. handlePurchase procesa PURCHASED y acknowledgment; PENDING no emite evento de éxito. queryActivePurchases devuelve lista vacía tanto sin conexión o ante error como cuando no hay compras.
