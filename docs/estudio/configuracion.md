# Configuración Android: propósito, fallback y fallos

[Guía maestra](../guia-maestra.md) | [Cobertura](cobertura.md)

Revisión de fuentes: 2026-10-01. Las observaciones estáticas no certifican el servidor desplegado ni el comportamiento en dispositivo.


La tabla se obtiene del código versionado, no de archivos privados. [build.gradle.kts](../../app/build.gradle.kts), [NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt), manifest y XML son las fuentes. [Índice de lectores](lectores-configuracion.md).

| Opción | Para qué y cuándo se usa | Ausencia y valor inválido |
| --- | --- | --- |
| `sdk.dir` | Localización SDK por Gradle/Android tooling | Si no existe una resolución alternativa, no puede configurarse/buildar el proyecto; no es una variable del servidor |
| `mirailink.baseUrl` | BuildConfig de origen; NetworkModule añade `/api/` | Default `http://10.0.2.2:3000`, útil para emulador. En móvil físico no identifica el PC. Un origen inválido puede fallar al crear Retrofit; incluir ya `/api` duplica el prefijo |
| `mirailink.imageAllowedDomains` | Allowlist del interceptor de imágenes | Lista pública predeterminada del build; un host nuevo puede bloquear imágenes aunque REST funcione. Revisar matching de subdominios en el interceptor |
| `admob.interstitial.testAdUnitId` | Unidad debug de interstitial | Default de prueba en build; no acredita anuncios entregados |
| `admob.interstitial.productionAdUnitId` | Unidad release de interstitial | Default vacío; el gestor no dispone de unidad de producción válida si falta |
| Propiedades de firma `storeFile/storePassword/keyAlias/keyPassword` | Configuración release | Casts directos en build: la ausencia puede romper incluso la fase de configuración. No mostrar valores ni rutas privadas |
| `TEST_USER/TEST_PASS` | BuildConfig debug usado por pruebas | Casts directos; ausencia puede romper configuración. Son credenciales y nunca ejemplos públicos |
| Configuración Firebase del proyecto | Plugin Google Services y SDK | Sin archivo/configuración compatible falla procesamiento del plugin o integración; no copiar su contenido |
| `gemini_model_name` | Remote Config leído por módulo AI | XML default `gemini-2.5-flash`; fallo de fetch mantiene defaults/cache disponible, no prueba de acceso al modelo |
| `is_christmas_mode` | Flag remoto de tema | No declarado en el XML observado; revisar default del getter boolean y overrides locales |
| AdMob application ID | Meta-data del manifest | Independiente de ad unit; ID incompatible afecta inicialización/monetización |
| App Check | Debug provider en debug, Play Integrity en release | Proveedor/registro mal configurado puede denegar llamadas a servicios protegidos |
| Productos y base plans Billing | Constantes y catálogo de Google Play | Un producto no disponible, oferta sin token o cuenta no elegible impide compra aunque se muestre un precio fallback |

## Permisos y archivos de plataforma

[AndroidManifest.xml](../../app/src/main/AndroidManifest.xml) declara Internet, cámara, notificaciones, ubicación, vibración, AD_ID y overlay. Cámara es feature requerida, lo que restringe disponibilidad en dispositivos sin ella. Permisos declarados no equivalen a permisos concedidos: ver solicitud y rama de rechazo en cada pantalla.

`network_security_config.xml` gobierna transporte; `backup_rules.xml` y `data_extraction_rules.xml` gobiernan exclusiones de copias. El FileProvider usa `file_paths.xml` para URIs de archivos de cámara. Configurarlos cambia exposición y disponibilidad; no asumir que `allowBackup=true` excluye todos los datos locales.

El servidor configura sus propias URLs públicas, SMTP, JWT y DB. Leer [configuración backend](https://github.com/FeryaelJustice/MiraiLink-Backend/blob/codex/documentacion-integral/docs/estudio/configuracion.md) para efectos cruzados de URL de media, correo, proxies y compatibilidad.
