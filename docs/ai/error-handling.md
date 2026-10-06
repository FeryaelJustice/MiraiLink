# Tratamiento tipado de errores

## Objetivo

MiraiLink transforma cualquier fallo técnico en un `AppError` estable antes de salir de `data`. La UI nunca recibe el texto libre de una respuesta HTTP, un cuerpo de error, una excepción ni un mensaje de base de datos. `ui` convierte el tipo estable en un `UiError` localizado, neutral y accionable.

El flujo canónico es:

```text
Retrofit, archivo o DataStore
  -> safeApiCall o safeLocalCall
  -> MiraiLinkResult<T> con AppError
  -> repositorio y caso de uso sin cambiar el error
  -> ViewModel llama toUiError y registra la recuperación
  -> MiraiLinkErrorContent muestra mensaje y acción
```

## Contrato de dominio

### `AppError`

`AppError` es la raíz sellada. Solo contiene categorías de negocio o infraestructura estables. No contiene `String`, `Throwable`, códigos HTTP ni cuerpos del servidor.

| Tipo | Valores | Cuándo usarlo |
| --- | --- | --- |
| `DataError.Network` | `NO_CONNECTION`, `TIMEOUT`, `BAD_REQUEST`, `FORBIDDEN`, `NOT_FOUND`, `CONFLICT`, `RATE_LIMITED`, `PAYLOAD_TOO_LARGE`, `SERVER`, `SERVICE_UNAVAILABLE`, `SERIALIZATION`, `UNKNOWN` | Fallos producidos al acceder a un servicio remoto. |
| `DataError.Local` | `NOT_FOUND`, `STORAGE_FULL`, `CORRUPTED`, `ACCESS_DENIED`, `UNKNOWN` | Fallos de archivo, DataStore, caché o almacenamiento local. |
| `AuthError` | `INVALID_CREDENTIALS`, `SESSION_EXPIRED`, `VERIFICATION_REQUIRED`, `INVALID_VERIFICATION_CODE`, `INVALID_TWO_FACTOR_CODE` | Fallos de autenticación que necesitan una recuperación específica. Credenciales inválidas y sesión expirada son casos diferentes. |
| `ValidationError` | `INVALID_INPUT`, `INVALID_MEDIA`, `MISSING_REQUIRED_VALUE` | Datos que no se pueden procesar antes de completar la operación. |
| `UnknownError` | valor único | Último recurso cuando no existe una clasificación segura. No se debe usar si hay información suficiente para elegir otro tipo. |

### `MiraiLinkResult<T>`

| Miembro | Qué hace | Cuándo usarlo |
| --- | --- | --- |
| `Success(data)` | Transporta un valor válido. | Cuando la operación terminó correctamente. |
| `Error(error)` | Transporta exclusivamente un `AppError`. | Cuando la operación falló de forma controlada. |
| `success(data)` | Factoría equivalente a `Success`. | Cuando mejora la inferencia del tipo genérico. |
| `EmptyResult` | Alias de `MiraiLinkResult<Unit>`. | Operaciones sin dato útil de éxito. |
| `error(error)` | Factoría equivalente a `Error`. | Cuando mejora la legibilidad de una rama de error. |
| `map(transform)` | Transforma solo el dato de éxito y conserva el error exacto. | Adaptación DTO, modelo o retorno `Unit`. |
| `mapError(transform)` | Sustituye una categoría de error sin tocar el éxito. | Solo cuando una capa conoce semántica adicional real. |
| `onSuccess(action)` | Ejecuta un efecto para un éxito y devuelve el mismo resultado. | Telemetría o efectos pequeños que no cambian el contrato. |
| `onError(action)` | Ejecuta un efecto para un error y devuelve el mismo resultado. | Telemetría basada en categorías, nunca en mensajes del servidor. |
| `asEmptyResult()` | Convierte un éxito de cualquier tipo en `Success(Unit)` y conserva el error. | Operaciones cuyo dato de éxito no tiene valor para el consumidor. |

No existe constructor de compatibilidad con `String` o `Throwable`. Si aparece un uso nuevo de `MiraiLinkResult.Error("texto")`, el código debe corregirse, no adaptarse.

## Clasificación de red

### `NetworkOperation`

Da contexto al mismo estado HTTP. `LOGIN` convierte un 401 en `INVALID_CREDENTIALS`; una operación autenticada convierte un 401 en `SESSION_EXPIRED`. Usa `REGISTER`, `VERIFICATION` y `TWO_FACTOR` para que los códigos estables del backend se interpreten en el contexto correcto. Usa `PUBLIC` solo cuando no hay sesión ni semántica especial.

| Valor | Cuándo usarlo |
| --- | --- |
| `PUBLIC` | Endpoint sin sesión ni interpretación especial. |
| `LOGIN` | Inicio de sesión; un 401 significa credenciales no aceptadas. |
| `REGISTER` | Registro de cuenta. |
| `AUTHENTICATED` | Operación con sesión; un 401 significa sesión finalizada. |
| `VERIFICATION` | Solicitud o confirmación de verificación. |
| `TWO_FACTOR` | Alta, baja o validación de segundo factor. |

Cada datasource debe elegir el valor más específico. No se decide el contexto desde la UI ni desde el texto de una respuesta.

### `NetworkErrorMapper`

| Método | Responsabilidad |
| --- | --- |
| `map(throwable, operation)` | Convierte `HttpException`, timeout, DNS, conexión, IO y serialización en `AppError`. Siempre vuelve a una categoría estable. |
| `existingChatId(exception)` | Recupera un `chatId` válido del cuerpo de un conflicto de creación de chat. Es el único dato de recuperación permitido y nunca expone el cuerpo completo. |
| `mapHttpException` | Aplica primero códigos estables del payload y después el estado HTTP. |
| `parsePayload` | Deserializa de forma tolerante `ApiErrorResponse`. Devuelve `null` si el cuerpo no es válido. |
| `mapKnownPayload` | Interpreta `code` y `error`; usa `message` solo contra listas cerradas de compatibilidad. |
| `normalizeCode` | Normaliza mayúsculas, espacios y guiones de un código estable. |
| `mapStableCode` | Traduce el vocabulario estable del backend a `AuthError`. |

La prioridad es código estable, contexto de endpoint y estado HTTP. El mensaje libre solo se compara con listas cerradas para compatibilidad antigua. Nunca se devuelve ni se registra.

### Llamadas seguras

`ApiErrorResponse` es un DTO interno y serializable que permite leer únicamente `code`, `error`, `message` y `chatId`. Solo se usa dentro de `NetworkErrorMapper`: no debe devolverse desde un datasource ni convertirse en modelo de dominio.

| Función | Qué hace | Cuándo usarla |
| --- | --- | --- |
| `safeApiCall(operation, call)` | Ejecuta una llamada que devuelve directamente el cuerpo esperado y clasifica cualquier fallo. | La mayoría de endpoints Retrofit. |
| `safeApiUnitResponse(operation, call)` | Comprueba `Response<Unit>.isSuccessful` antes de producir éxito. | Endpoints cuyo servicio Retrofit devuelve `Response<Unit>`. |
| `safeApiCallRecoveringHttp(operation, recover, call)` | Permite recuperar un valor conocido de un `HttpException` y clasifica el resto. | Conflicto de creación de chat que ya contiene un `chatId`. No debe convertirse en un capturador genérico. |

Las tres funciones vuelven a lanzar `CancellationException`. No se debe convertir una cancelación de corrutina en un error visible.

## Fallos locales

`safeLocalCall(dispatcher, call)` mueve trabajo bloqueante al dispatcher inyectado y clasifica archivo inexistente, acceso denegado, serialización corrupta, IO y fallo local desconocido. También convierte `InvalidMediaException` en `ValidationError.INVALID_MEDIA` y vuelve a lanzar cancelación.

`InvalidMediaException` es interna a `data`. Se usa cuando un `ContentResolver` no puede abrir un medio elegido. No debe cruzar al dominio.

## Presentación

### Tipos

| Tipo o método | Responsabilidad |
| --- | --- |
| `UiText.Resource(id, args)` | Referencia un recurso localizado sin guardar `Context` en el ViewModel. |
| `ErrorRecovery.RETRY` | La misma operación se puede repetir. |
| `ErrorRecovery.SIGN_IN_AGAIN` | La sesión finalizó y la recuperación debe volver al acceso. |
| `ErrorRecovery.REVIEW_INPUT` | La UI debe permitir revisar datos o códigos. |
| `UiError(message, actionLabel, recovery)` | Estado visible completo y sin callbacks. |
| `AppError.toUiError()` | Mapea exhaustivamente cada tipo a mensaje, acción y estrategia. |
| `UiText.asString()` | Resuelve el recurso dentro de composición. |
| `MiraiLinkErrorContent(error, onAction, modifier)` | Muestra el mensaje y un botón accesible con llamada a la acción. |
| `messageResource()` | Selecciona de forma exhaustiva el recurso de mensaje para cada `AppError`; es privada al mapper. |

Los textos están en `values`, `values-es` y `values-en`. Son neutrales, no culpan a la persona y siempre indican qué se puede hacer a continuación.

### `RetryableViewModel`

| Método | Responsabilidad |
| --- | --- |
| `setRecoveryAction(action)` | Registra desde el ViewModel la operación exacta o la transición de recuperación. Solo está disponible para subclases. |
| `performErrorAction()` | Ejecuta la recuperación cuando la UI pulsa el botón. La UI no conoce repositorios ni casos de uso. |
| `onCleared()` | Elimina la referencia al callback para evitar conservar capturas después de destruir el ViewModel. |

La acción no se almacena dentro de `UiError`, por lo que el estado sigue siendo inmutable y comprobable. Una pantalla debe renderizar el `UiError` y enlazar el botón a `viewModel::performErrorAction`.

En autenticación, credenciales inválidas, códigos inválidos y sesión expirada vuelven al estado editable. No se hace un reintento ciego con una sesión expirada. En operaciones de red, carga, envío, foto o catálogo se registra la operación exacta con sus parámetros actuales.

## Integración por ViewModel

| ViewModel | Operaciones con recuperación |
| --- | --- |
| `AuthViewModel` | Login, registro, consulta 2FA y confirmación 2FA. Los errores de autenticación vuelven al estado editable. |
| `RecoverPasswordViewModel` | Solicitud y confirmación de restablecimiento. |
| `VerificationViewModel` | Consulta de estado, solicitud de código y confirmación. |
| `HomeViewModel` | Usuario actual, feed, like y dislike. Una tarjeta solo se elimina tras éxito. |
| `MessagesViewModel` | Matches y chats, cada carga conserva su reintento exacto. |
| `ChatViewModel` | Creación, lectura, participantes, mensajes, envío y reporte. El sondeo puede recuperarse sin exponer el fallo técnico. |
| `ProfileViewModel` | Perfil, guardado, eliminación de foto y catálogos. La validación local usa `ValidationError`. |
| `ProfilePictureViewModel` | Subida de imagen con la URI seleccionada. |
| `SettingsViewModel` | Cierre de sesión y eliminación de cuenta. |
| `FeedbackViewModel` | Envío del texto actual. |
| `ConfigureTwoFactorViewModel` | Estado, alta, verificación y baja de 2FA. |
| `AiChatViewModel` | Reenvío del prompt que falló. |

`SplashScreenViewModel` es la excepción deliberada: la comprobación de versión falla en abierto y los fallos de autologin u onboarding seleccionan una ruta segura. No presenta un error bloqueante y por tanto no crea un `UiError`.

## Estado de uso actual

| API o tipo | Estado | Uso actual o condición de uso |
| --- | --- | --- |
| `AppError`, `MiraiLinkResult`, `Success`, `Error` y `map` | Producción | Contrato común de datasources, repositorios, casos de uso y ViewModels. |
| `success` y `error` | Disponibles | Factorías de conveniencia; se usan cuando ayudan a inferir el genérico, no son obligatorias. |
| `mapError`, `onSuccess` y `onError` | Sin call sites de producción | Extensiones probadas para adaptación o telemetría futura. No deben usarse para recuperar texto del servidor. |
| `EmptyResult` y `asEmptyResult` | Producción | `LogoutUseCase` elimina el dato de éxito y conserva el error. |
| `NetworkOperation`, `NetworkErrorMapper` y `ApiErrorResponse` | Producción interna de data | Todos los datasources remotos clasifican con contexto. El DTO nunca cruza la capa. |
| `safeApiCall` | Producción | Llamadas Retrofit que devuelven cuerpo. |
| `safeApiUnitResponse` | Producción | Chat, feedback, reportes, usuario y 2FA con `Response<Unit>`. |
| `safeApiCallRecoveringHttp` y `existingChatId` | Producción limitada | Solo creación de chat cuando un conflicto contiene un `chatId` reutilizable. |
| `safeLocalCall` e `InvalidMediaException` | Producción | Preparación local de fotos y medios en `UserRemoteDataSource`. |
| `DataError.Local.STORAGE_FULL` | Preparado, sin productor actual | Se usará al añadir una detección fiable de almacenamiento lleno; ya tiene texto y mapeo de UI. |
| `UiText`, `UiError`, `ErrorRecovery`, `toUiError` y `messageResource` | Producción | Los ViewModels producen estado localizado y las pantallas muestran mensaje y acción. |
| `RetryableViewModel`, `performErrorAction`, `asString` y `MiraiLinkErrorContent` | Producción | Recuperación exacta desde todas las pantallas migradas de la tabla anterior. |

No hay clases deprecated ni adaptadores de compatibilidad dentro de este sistema. Si una API de esta tabla deja de tener call sites, debe conservarse solo si existe un caso de uso documentado y pruebas; en caso contrario se elimina.

## Cómo añadir un error nuevo

1. Añade un valor a la categoría adecuada de `AppError` solo si cambia la recuperación o el significado para la aplicación.
2. Añade la clasificación en `NetworkErrorMapper` o `safeLocalCall`.
3. Añade el mapeo exhaustivo y recursos español e inglés en `AppErrorUiMapper`.
4. Haz que el ViewModel guarde el `UiError` en estado visible y registre una recuperación.
5. Renderiza `MiraiLinkErrorContent` en la pantalla.
6. Añade pruebas del clasificador, mapper de UI, ViewModel y acción de recuperación.
7. Ejecuta `testDebugUnitTest`, `assembleDebug` y `lintDebug`.

## Prácticas prohibidas

- Pasar `Throwable`, `HttpException`, `Response`, código HTTP o cuerpo de error a domain o ui.
- Mostrar `exception.message`, `response.message`, `errorBody` o texto libre del backend.
- Decidir errores con búsquedas abiertas como `contains` sobre el mensaje del servidor.
- Capturar `CancellationException` como fallo normal.
- Guardar `Context`, `Activity`, `NavController` o callbacks de UI dentro de `UiError`.
- Crear un toast o snackbar como sustituto del estado visible y accionable.
- Culpar a la persona en el mensaje o dejar un error sin acción.
