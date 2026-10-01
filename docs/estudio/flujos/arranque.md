# Arranque, onboarding, versión y banderas

[Guía maestra](../../guia-maestra.md) | [Mapa funcional](../funcionalidades.md)

Revisión: 2026-10-01, basada en código.

## Recorrido y decisiones

[SplashScreenViewModel.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt) consulta CheckAppVersionUseCase con VERSION_CODE antes de seleccionar destino. Una versión obligatoria guarda el bloqueo en sesión y detiene el flujo. Una actualización opcional calcula el destino, lo retiene y lo libera al descartar el aviso. Un fallo de consulta permite continuar y limpia el bloqueo.

Si onboardingCompleted es false, va al onboarding sin ejecutar autologin. Si está completado o falla su lectura, ejecuta autologin: éxito lleva a Home y error a Auth. [NavWrapper.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt) marca el onboarding local al terminarlo y gestiona el back stack con Navigation 3. Las ilustraciones del onboarding no demuestran disponibilidad de todas las funcionalidades que representan.

## Persistencia y configuración

[MiraiLinkPrefs.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/datastore/MiraiLinkPrefs.kt) actualiza AppPrefs.onboardingCompleted, separado de la cuenta remota. [FeatureFlagStoreImpl.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/core/featureflags/FeatureFlagStoreImpl.kt) guarda enable_christmas_theme con false por defecto; Splash lo sobrescribe con is_christmas_mode. [RemoteConfigManager.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/core/remoteconfig/RemoteConfigManager.kt) usa intervalo de fetch de 3600 s; el XML declara gemini_model_name=gemini-2.5-flash, pero no declara is_christmas_mode. Su lectura booleana tiene false como valor por defecto del SDK.

## Límites y responsabilidad cruzada

La política numérica de versión y su almacenamiento pertenecen al [backend](https://github.com/FeryaelJustice/MiraiLink-Backend/blob/codex/documentacion-integral/docs/estudio/flujos/soporte.md). Onboarding y tema son estado del cliente. Comprobar arranque con sesión, sin sesión, versión obligatoria/opcional, red fallida y corrupción local; los tests unitarios no verifican restauración visual ni integración de Remote Config.
