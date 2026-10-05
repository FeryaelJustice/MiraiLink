# Ajustes, FAQ, denuncias y feedback

[Guía maestra](../../guia-maestra.md) | [Mapa de funcionalidades](../funcionalidades.md)

## Responsabilidad y recorrido

Settings reúne preferencias de la app, sesión y accesos de gestión; SearchPreferences tiene pantalla propia. FAQ usa repositorio local. FeedbackApiService y ReportApiService remiten texto/identidad objetivo al servidor y validan resultado Unit mediante los datasources.

## Alternativas y efectos

Un mensaje de soporte inválido o error remoto no es envío correcto. Reportar a otra persona requiere contrato y autorización; el cliente no modera ni borra datos por sí solo. Preferencias de notificación locales deben comprobarse en el receptor FCM y no se deben atribuir a una configuración backend inexistente.

## Fuentes para estudiar

**Perfil Holo-3D** aparece en Apariencia tanto en demo como en sesión online. `HoloPreferencesRepository` conserva un valor local cifrado, reactivo y compatible con preferencias anteriores (`true` por defecto); el ViewModel usa el contrato de errores y reintento existente. FAQ incorpora cinco entradas en `CARDS_AND_MATCHING`. Preguntas, respuestas y categorías son referencias `@StringRes`, resueltas mediante `stringResource` en Compose; los tres juegos de recursos incluyen español e inglés. Ver [Holo Mirai](../../features/holo_mirai_parallax/implementation.md).

- [FeedbackApiService.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/FeedbackApiService.kt)
- [ReportApiService.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/ReportApiService.kt)
- [FaqRepositoryImpl.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/FaqRepositoryImpl.kt)

Revisión: 2026-10-01, por inspección del código. Consultar [verificación](../desarrollo-y-verificacion.md) para resultados ejecutados.
