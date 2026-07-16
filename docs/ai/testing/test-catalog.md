# Catálogo de tests añadidos

Este catálogo documenta cada clase y método incorporado por la iniciativa de testing completo. Los tests anteriores siguen documentados por paquete y responsabilidad en development-and-testing.md.

## Infraestructura

### MainCoroutineRule

- starting: instala el dispatcher Main determinista.
- finished: restaura el dispatcher Main real.
- testDispatcher: ejecuta trabajo encolado con tiempo virtual.
- testDispatcherUnconfined: permite entrada inmediata usando el mismo scheduler.
- scheduler: controla todo el tiempo virtual del test.

### ScreenTestSupport

- setMiraiLinkContent: renderiza Compose con el tema de producción sin colores dinámicos.
- testSession: crea una sesión controlada con flujos observables reales.

## Datos y dominio

### AiRepositoryImplTest

- generate content delegates prompt and returns data source response: comprueba delegación y respuesta exacta.
- generate content propagates data source failure: confirma que el repositorio fino no oculta excepciones.

### GenerateContentUseCaseTest

- invoke returns generated content: envuelve éxito.
- invoke maps unexpected exception to unknown error: clasifica fallo inesperado.
- invoke rethrows cancellation exception: preserva cancelación estructurada.

### StringUtilsTest

Cubre URLs seguras, placeholder, emails, contraseñas, capitalización, nulos y patrones de inyección.

### MediaUtilsTest

Cubre las cuatro combinaciones de barras, URLs absolutas, orden de fotos y preservación de campos.

### GenericUtilsTest

Cubre transformación indexada, filtrado nulo, orden y lista vacía.

### UserUtilsTest

Cubre preferencia de nickname y fallback a username en modelos completo y mínimo.

### DateUtilsTest

Cubre ISO UTC, epoch, backend date, DatePicker, edad, inválidos y serialización. Fija y restaura locale y zona horaria.

### DataMediaUtilsTest

Cubre creación de URI FileProvider, detección de cache y borrado tolerante mediante Robolectric.

### AndroidLoggerTest

- debug delegates to android log: verifica nivel, tag y mensaje mediante ShadowLog.

## Estado y presentación

### GlobalMiraiLinkSessionTest

- session exposes datastore state changes: replica autenticación, verificación e identidad.
- session commands delegate to session manager: verifica guardar, verificar y cerrar sesión.
- bar configuration commands update only requested properties: cubre toda la configuración de barras.
- refresh profile picture updates successful value and preserves it on error: cubre cache y fallo.
- user id starts profile picture observation: cubre arranque automático del observador.
- createSession: crea la sesión con backgroundScope para cancelar collectors de larga vida.

### MainViewModelTest

- feature flag flow mirrors store updates: verifica forwarding reactivo de feature flags.

### NavAnalyticsViewModelTest

- log screen sends screen view event: valida nombre y parámetro de pantalla.
- log deep link sends opened event: valida nombre y URI del deep link.

### RetryableViewModelTest

Cubre ausencia de acción, sustitución por la última acción y limpieza de lifecycle. TestRetryableViewModel expone APIs protegidas solo dentro del test.

### AiChatViewModelTest

- send message emits loading and success: prueba secuencia con Turbine.
- send message emits mapped error: prueba clasificación de UI.
- error action retries last prompt: prueba recuperación exacta.
- createViewModel: crea el sistema con dispatcher compartido.

## Compose instrumentado

### AuthScreensTest

Cubre login y cambio a registro, solicitud de recuperación de contraseña y solicitud de código de verificación. authViewModel configura todos los flujos recogidos por AuthScreen.

### AppScreensTest

Cubre las tres páginas de onboarding, envío de prompt de AI y splash en loading.

### SocialScreensTest

Cubre Home sin usuarios, navegación de Messages a AI, envío en Chat, loading de Profile y logout desde ProfilePicture. chatViewModel configura todos los flujos del chat.

### SettingsScreensTest

Cubre navegación a Feedback, formulario Feedback vacío y navegación atrás en 2FA. twoFactorViewModel configura todos sus flujos y diálogos.

### MiraiLinkErrorContentTest

Resuelve mensaje y CTA desde resources y verifica que el botón ejecuta una sola vez la recuperación.

### AppE2ETest

No se añadieron recorridos externos. Se sustituyó su import por androidx.compose.ui.test.junit4.v2.createAndroidComposeRule para retirar el uso anterior.


### Contratos Retrofit existentes

Se corrigieron las rutas esperadas en AppConfigApiServiceTest, CatalogApiServiceTest, MatchApiServiceTest, TwoFactorApiServiceTest y UserApiServiceTest. Las expectativas ahora coinciden con las anotaciones Retrofit actuales, incluyendo auth, user, app/version y matches/mark-seen.

No se cambió el servidor ni se realizó tráfico de red.
## Cambio mínimo de producción

SplashScreen ahora aplica el modifier recibido también en sus ramas de loading e idle. Esto conserva el comportamiento visual y permite semántica, accesibilidad y test tags desde el llamador.
