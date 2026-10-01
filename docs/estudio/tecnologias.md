# Tecnologías Android y su función

[Guía maestra](../guia-maestra.md) | [Cobertura](cobertura.md)

Revisión de fuentes: 2026-10-01. Las observaciones estáticas no certifican el servidor desplegado ni el comportamiento en dispositivo.


Las versiones son declaraciones de [libs.versions.toml](../../gradle/libs.versions.toml), [build.gradle.kts](../../app/build.gradle.kts) y [gradle-wrapper.properties](../../gradle/wrapper/gradle-wrapper.properties), no una recomendación de actualización ni comprobación remota.

| Tecnología | Declaración | Para qué se utiliza y dónde estudiar |
| --- | --- | --- |
| Kotlin / Java | 2.4.10 / 17 | Tipos, sealed results, lambdas, interoperabilidad; fuentes propias y toolchain |
| AGP / Gradle / KSP | 9.4.1 / 9.7.1 / 2.3.8 | Compilación, recursos, generación Koin/Room; catálogo y plugins |
| Compose BOM / Material 3 | 2026.09.00 | UI declarativa, estado y tema; `ui/screens`, `ui/components`, `ui/theme` |
| Navigation 3 | 1.1.7 | Claves, pilas y destinos; `ui/navigation` |
| Koin BOM y annotations | 4.2.2 | Construcción y qualifiers; `di/koin` |
| Coroutines / Flow | Uso en fuentes; test 1.10.2 | Trabajo cancelable, state streams y dispatchers; ViewModels y sesión |
| Retrofit / OkHttp | 3.0.0 / 5.5.0 | HTTP, interceptores, timeouts; `data/remote` |
| Serialization | 1.11.0 | DTO JSON y datos persistidos; `data/model` y serializer |
| Room | 2.8.5 | Base demo v3, DAO y entidades; `data/local/demo` |
| DataStore | 1.2.1 | Sesión/preferencias y flags; `data/datastore`, `core/featureflags` |
| Android Keystore / AES-GCM | Plataforma | Clave local y payload cifrado; `SecretKeyProvider` |
| Coil | 2.7.0 | Imágenes, caché de carga y cliente con allowlist; Application e interceptor |
| Socket.IO | 2.1.2 | Servicio registrado; no usado por el polling visible del chat |
| Firebase BOM | 34.19.0 | Analytics, Crashlytics, Messaging, Remote Config, AI y App Check |
| AdMob / UMP | 25.5.0 / 4.0.0 | Anuncios y consentimiento; raíz UI y gestores |
| Credentials | 1.6.0 | Proveedor del sistema para contraseñas; `CredentialHelper` |
| Billing | 9.1.0 | Ofertas, flujo de compra y eventos; `data/billing` |
| CameraX / ML Kit | 1.6.2 / 16.1.7 | Mirai Studio, cámara y detección facial local |
| ZXing | 3.5.4 | Representación QR en el flujo 2FA |
| Kotzilla / stability analyzer | 2.3.6 / 0.14.0 | Observabilidad de DI y análisis de estabilidad, según plugins |
| JUnit / MockK / Turbine / Robolectric | 4.13.2 / 1.14.11 / 1.2.1 / 4.17 | Pruebas JVM, mocks, flows y entorno Android simulado |

El catálogo completo, incluidos componentes adaptativos, UI tooling, Play Services y screenshot tooling, está en [libs.versions.toml](../../gradle/libs.versions.toml) y [build de la app](../../app/build.gradle.kts). Un paquete declarado no garantiza que todas sus posibilidades estén integradas. Seguir importaciones y bindings para distinguir uso activo de infraestructura disponible.
