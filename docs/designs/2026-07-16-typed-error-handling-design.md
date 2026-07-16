# Typed Error Handling Design

## Context

MiraiLink currently represents expected failures with `MiraiLinkResult.Error(message: String, exception: Throwable?)`. Remote datasources parse the `message` field from HTTP error bodies in `domain/util/HttpUtils.kt`, use cases often catch exceptions again, and ViewModels forward those strings and exceptions to Compose.

The current flow has these problems:

- Backend wording and technical exception messages can reach the UI.
- `domain` depends on Android logging, JSON parsing and Retrofit.
- HTTP status, connectivity, authentication and local storage failures are not distinguishable.
- Most use cases replace an already classified failure with a new free-form message.
- Several ViewModels duplicate error conversion, ignore failures or expose `MiraiLinkResult` directly.
- Recovery behavior is inconsistent and visible errors do not always offer an action.
- Broad `Throwable` catches can consume coroutine cancellation.

The refactor affects the complete asynchronous result path: remote and local data access, repositories, use cases, application state, ViewModels, visible error states and their tests.

## Goals

- Prevent raw backend, HTTP and exception messages from reaching presentation.
- Represent expected failures with stable, exhaustive types.
- Keep transport and parsing knowledge inside `data`.
- Preserve domain purity and existing repository boundaries.
- Produce localized, neutral and actionable messages only in presentation.
- Let the ViewModel repeat the exact failed operation when retrying is safe.
- Recover from an expired session by asking the user to sign in again instead of looping on HTTP 401.
- Preserve the existing UI delivery mechanism where practical and avoid adding snackbars or toasts.
- Cover the classification, mapping and recovery behavior with unit tests.

## Non-goals

- Redesigning screens or migrating the complete application to a new MVI framework.
- Adding automatic network retries, exponential backoff or background queues.
- Changing backend endpoints or introducing a token refresh endpoint.
- Exposing diagnostic details to users.
- Replacing field-level validation that already provides immediate form guidance.

## Considered approaches

### 1. Replace the error string only

Change `MiraiLinkResult.Error` to contain an enum but keep custom mapping and retry logic in every ViewModel.

This has a smaller initial diff, but preserves duplicated presentation behavior and makes full coverage difficult.

### 2. Typed result with layer-specific mapping

Use one stable result contract, a domain error taxonomy, centralized data classification, presentation-only localization and a reusable recovery model owned by ViewModels.

This is the selected approach because it addresses the complete flow without forcing an unrelated architecture rewrite.

### 3. Full presentation rewrite

Convert every feature to a new State, Action and Event contract while introducing typed errors.

This could improve long-term consistency, but it combines two project-wide migrations and substantially increases regression risk.

## Architecture

### Domain result contract

`MiraiLinkResult` remains the project result type to limit churn, but its failure branch changes from free-form data to a typed error:

```kotlin
sealed interface MiraiLinkResult<out T> {
    data class Success<out T>(val data: T) : MiraiLinkResult<T>
    data class Error(val error: AppError) : MiraiLinkResult<Nothing>
}
```

The contract will include result helpers such as `map`, `mapError`, `onSuccess`, `onError` and `asEmptyResult` where they reduce branching. It will not contain a user-facing string or a `Throwable`.

Expected failures return `MiraiLinkResult.Error`. Programming errors remain exceptions. Coroutine cancellation is always rethrown.

### Domain error taxonomy

`AppError` is a sealed domain contract with these categories:

```text
AppError
|-- DataError
|   |-- Network
|   |   |-- NoConnection
|   |   |-- Timeout
|   |   |-- BadRequest
|   |   |-- Forbidden
|   |   |-- NotFound
|   |   |-- Conflict
|   |   |-- RateLimited
|   |   |-- PayloadTooLarge
|   |   |-- Server
|   |   |-- ServiceUnavailable
|   |   |-- Serialization
|   |   `-- Unknown
|   `-- Local
|       |-- NotFound
|       |-- StorageFull
|       |-- Corrupted
|       |-- AccessDenied
|       `-- Unknown
|-- AuthError
|   |-- InvalidCredentials
|   |-- SessionExpired
|   |-- VerificationRequired
|   |-- InvalidVerificationCode
|   `-- InvalidTwoFactorCode
|-- ValidationError
|   |-- InvalidInput
|   |-- InvalidMedia
|   `-- MissingRequiredValue
`-- UnknownError
```

Invalid credentials are an authentication outcome, not a network transport failure. HTTP 401 is interpreted using the operation context:

- Login endpoint: `AuthError.InvalidCredentials`.
- Authenticated endpoint: `AuthError.SessionExpired`.
- Two-factor verification endpoint: `AuthError.InvalidTwoFactorCode` when the server contract or legacy allowlist identifies that condition.

Existing form field errors remain presentation validation because they are synchronous guidance, not backend failures.

## Data layer

### Network call wrapper

A reusable Retrofit wrapper in `data/util` will own exception and HTTP classification. Each remote datasource will call it with an operation context when status alone is ambiguous.

Classification rules include:

- `UnknownHostException` and connection failures: `NoConnection`.
- `SocketTimeoutException`: `Timeout`.
- Serialization failures: `Serialization`.
- HTTP 400: `BadRequest` unless an endpoint-specific stable code maps to a more precise domain error.
- HTTP 401: authentication mapping based on operation context.
- HTTP 403, 404, 409, 413 and 429: their matching typed errors.
- HTTP 500 through 599: `Server` or `ServiceUnavailable` for 503.
- Unrecognized I/O or HTTP failures: the narrowest safe generic category.
- Unexpected exceptions: `UnknownError` after diagnostic reporting.
- `CancellationException`: rethrow unchanged.

The existing `domain/util/HttpUtils.kt` will be removed or replaced by data-owned utilities. Domain will no longer import Retrofit, `JSONObject` or Android logging for error mapping.

### Server error payloads

The backend error body will be decoded into a private data DTO with optional stable fields such as `code`, `error` and `message`.

Mapping priority:

1. Stable machine-readable error code.
2. HTTP status plus endpoint context.
3. A closed, tested allowlist of normalized legacy messages for known authentication and verification cases.
4. Generic classification.

The raw message and body remain inside `data`. They are never added to `AppError`, domain models, analytics parameters or UI state.

The special chat creation behavior remains supported: when a conflict response contains a valid existing `chatId`, it is treated as a successful idempotent result. Parsing this field will use the same structured decoder rather than an independent `JSONObject` block.

### Local failures

Operations that read media, DataStore or encrypted local state will map expected storage and security failures to `DataError.Local`. Opening or reading an invalid image URI maps to `ValidationError.InvalidMedia` or a local access error, never to a handcrafted string.

### Logging and diagnostics

Data code may record the mapped category, HTTP status and operation name. It must not log tokens, credentials, complete backend bodies, image contents or user profile data. A cause may be reported directly to diagnostics at the boundary where it is caught, but it will not travel inside `MiraiLinkResult`.

## Repositories and use cases

Repositories keep returning `MiraiLinkResult` and map successful DTOs to domain models. They may translate a data error into a more specific business error when the repository has the necessary context.

Use cases will no longer wrap every repository invocation in a generic `try/catch`. They will:

- Forward already typed expected errors.
- Perform business validation and return a typed domain error when necessary.
- Preserve successful values or map them with result helpers.
- Avoid reconstructing errors with operation names.

Success messages returned by the backend for reset, verification and two-factor actions will be converted to `Unit` when presentation only needs completion. User-facing success wording will come from resources. Identifiers, tokens, URLs and generated AI content remain successful domain data because they are not server prose used as UI feedback.

## Presentation

### Localized UI model

Presentation defines:

```kotlin
sealed interface UiText {
    data class StringResource(val id: Int, val args: List<Any> = emptyList()) : UiText
    data class Dynamic(val value: String) : UiText
}

data class UiError(
    val message: UiText,
    val actionLabel: UiText,
    val recovery: ErrorRecovery,
)
```

`AppError.toUiError()` is the only generic mapping from domain error to localized presentation content. No data-layer type is imported by UI.

The default messages will be neutral, concise and action-oriented. Examples:

- No connection: the service could not be reached and the user can try again.
- Timeout: the operation took longer than expected and can be retried.
- Server: the service is temporarily unavailable and can be retried later.
- Invalid credentials: sign-in could not be completed and the information can be reviewed before trying again.
- Local storage: saved information could not be accessed and the operation can be retried.
- Unknown: the operation could not be completed and can be tried again.

Messages must not blame the user, expose HTTP codes or reproduce backend text.

### Recovery model

`ErrorRecovery` distinguishes:

- `Retry`: rerun the exact failed ViewModel operation with its captured inputs.
- `SignInAgain`: clear the invalid authenticated state and return to authentication.
- `ReviewInput`: keep the form content available so it can be reviewed and submitted again.

A reusable ViewModel-owned retry controller stores only the latest safe recovery callback. It exposes the current `UiError`, supports `retryLastAction()` and clears stale errors on success or explicit dismissal.

Retry callbacks are not stored in immutable screen state. They remain private to the ViewModel so Compose state stays stable and testable.

For `AuthError.SessionExpired`, the action is `Iniciar sesión de nuevo`. The same authenticated network call is not repeated because there is no token refresh mechanism and a blind retry would loop on HTTP 401.

### Screens

Existing text-based error locations will render localized `UiError` content and an action button. A reusable Compose component will avoid duplicating message and action layout.

The migration covers error states in authentication, verification, password recovery, home, messages, chat, profile, profile picture, feedback, settings, two-factor configuration and AI chat. Errors currently ignored or only logged will be represented in their owning ViewModel when they affect a user-triggered operation.

No new snackbar or toast delivery mechanism will be introduced. Existing transient error paths will move to the owning screen state when an action is required, while unrelated success notifications remain unchanged.

## End-to-end flow

```text
Retrofit or local operation
    -> data wrapper classifies technical failure
    -> repository preserves or refines AppError
    -> use case applies business rules
    -> ViewModel maps AppError to UiError and registers recovery
    -> Compose renders localized message and action
    -> user action calls ViewModel retry or sign-in recovery
```

At no point after the data boundary does raw HTTP body text or a technical exception participate in UI rendering.

## Testing strategy

Implementation follows test-first development:

1. Result contract and helper tests.
2. Network classifier tests for exception types, relevant HTTP statuses, endpoint contexts and cancellation.
3. Server payload tests for stable codes, legacy allowlist behavior, malformed bodies and chat conflict recovery.
4. Local error classifier tests where local operations are migrated.
5. Presentation mapper tests ensuring every `AppError` produces a resource-backed message and recovery action.
6. Retry controller tests for retry, replacement of the latest action, clearing after success and session-expired recovery.
7. ViewModel tests for each migrated visible error path.
8. Existing datasource, repository and use case tests migrated from string assertions to typed error assertions.
9. Compose tests for the reusable error component and action callback.

The existing `SplashScreenViewModelTest` constructor mismatch will be corrected separately within the branch so the full unit suite can compile. It is a verified baseline failure and not caused by this design.

## Validation

The completed implementation must provide fresh evidence from:

```powershell
.\gradlew.bat testDebugUnitTest
.\gradlew.bat assembleDebug
.\gradlew.bat lintDebug
```

Targeted tests will be run during each test-first step. Instrumented tests will only run if a safe emulator is available and the selected tests do not invoke the real backend.

## Acceptance criteria

- `MiraiLinkResult.Error` exposes no message or exception.
- No UI or ViewModel reads an HTTP body, exception message or data DTO error message.
- No data source calls a domain-owned HTTP parser.
- All expected remote failures map to a typed category and preserve coroutine cancellation.
- Invalid credentials and expired sessions are distinct.
- Every visible asynchronous error has a neutral localized message and a recovery action.
- Recoverable errors retry the exact failed ViewModel operation.
- Expired sessions route to sign-in instead of retrying the unauthorized operation.
- Existing server success prose used only as feedback is replaced with app-owned resources.
- Tests cover the taxonomy, classification, mapping and recovery paths.
- Debug unit tests and debug build complete successfully, excluding only independently documented external tooling blockers if any appear.
