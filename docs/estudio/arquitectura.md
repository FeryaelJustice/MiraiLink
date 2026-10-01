# Arquitectura real del cliente

[Guía maestra](../guia-maestra.md) | [Cobertura](cobertura.md)

Revisión de fuentes: 2026-10-01. Las observaciones estáticas no certifican el servidor desplegado ni el comportamiento en dispositivo.


El proyecto contiene un único módulo Gradle, `:app`. `data`, `domain` y `ui` son paquetes, no módulos aislados. Hay una separación por capas pragmática: el dominio conserva tipos Android y algunos mapeadores de datos producen modelos de presentación. Koin resuelve los bindings; las referencias históricas a Hilt no describen este código.

## Arranque y composición

Android instancia [MiraiLinkApp.kt](../../app/src/main/java/com/feryaeljustice/mirailink/MiraiLinkApp.kt), que inicia Koin y configura Coil con un OkHttp específico para imágenes. [MainActivity.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/MainActivity.kt) configura la Activity, Firebase/App Check según build type, Ads, notificaciones y procesamiento de `intent.data` tanto al entrar como en `onNewIntent`. No describir deep links como ausentes usando auditorías antiguas: su enrutamiento actual pasa por la sesión y [NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

Los módulos se declaran en [RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt) y [NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt). `Remote` y `Demo` distinguen implementaciones; los delegados eligen según el modo. No todos los contratos disponen de una implementación demo: comprobar el binding antes de afirmar paridad offline.

## Camino de una operación

Compose emite una acción; el ViewModel actualiza estado y llama a un caso de uso; un repositorio obtiene datos de Retrofit o Room; los DTO se transforman y el resultado se presenta. Los errores se representan mediante `MiraiLinkResult` y se traducen a `UiError`. La dirección concreta de importaciones se puede estudiar en [referencia de fuentes](referencia-codigo.md).

[AppScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/AppScreen.kt) define rutas serializables y subgrafos. [NavigationState.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavigationState.kt) mantiene pilas con decoración guardable. [GlobalMiraiLinkSession.kt](../../app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt) expone autenticación, verificación, usuario y barras; no equivale a persistir cualquier borrador de formulario.

## Límites

Un `StateFlow` es una fuente observable, no una cola de sincronización ni almacenamiento tras muerte de proceso. `viewModelScope` cancela al eliminar el ViewModel; comprobar además los efectos Compose y observadores globales. Los bindings AI se repiten en módulos: registrar la duplicidad sin prometer independencia entre capas ni corregirla en esta entrega.

Diagramas: [componentes y paquetes](diagramas/componentes.md), [navegación y sesión](diagramas/estados.md). [Backend](https://github.com/FeryaelJustice/MiraiLink-Backend/blob/codex/documentacion-integral/docs/guia-maestra.md) describe el pipeline servidor.
