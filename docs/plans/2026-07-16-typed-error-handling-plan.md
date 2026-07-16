# Typed Error Handling Implementation Plan

> **For agentic workers:** Use this plan task by task. Follow TDD, keep the application compiling between migration batches, and do not expose raw server or exception text as a compatibility shortcut.

**Goal:** Replace MiraiLink's free-form error propagation with typed domain failures, data-owned classification, localized actionable UI errors and ViewModel-owned recovery across every existing `MiraiLinkResult` flow.

**Architecture:** Retrofit and local I/O failures are classified inside `data` into a pure Kotlin `AppError`. Repositories and use cases propagate or refine that type. ViewModels map it to resource-backed `UiError` state and own the last safe recovery action. Compose renders state and forwards the action without inspecting technical failures.

**Tech Stack:** Kotlin 2.4, Android Gradle Plugin 9.3, Jetpack Compose Material 3, Retrofit, OkHttp, Kotlinx Serialization, coroutines and StateFlow, Koin, JUnit 4, MockK, Truth, Turbine and Robolectric.

**Platform:** Android

---

## Guardrails

- Work only on `codex/typed-error-handling`.
- Keep `domain` free of Retrofit, Android logging, Android resources and JSON parsing for error handling.
- Never consume `CancellationException`.
- Never add a backend body, backend `message`, `Throwable` or `Throwable.message` to UI state.
- Keep suspend APIs main-safe. Retrofit suspend calls are already asynchronous; blocking content resolver work stays behind the injected I/O dispatcher.
- Use state for visible errors. Do not add snackbar or toast error events.
- Retry only explicit user-triggered operations. Session expiration routes to sign-in and never repeats the unauthorized call.
- Preserve unrelated behavior and existing success notifications.

## Task 1: Establish the typed domain contract

**Files:**

- Create `app/src/main/java/com/feryaeljustice/mirailink/domain/error/AppError.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/domain/util/MiraiLinkResult.kt`.
- Create `app/src/test/java/com/feryaeljustice/mirailink/domain/util/MiraiLinkResultTest.kt`.
- Create `app/src/test/java/com/feryaeljustice/mirailink/domain/error/AppErrorTest.kt`.

**Steps:**

1. Write tests that reference the approved taxonomy and assert that `MiraiLinkResult.Error` carries only `AppError`.
2. Run `./gradlew testDebugUnitTest --tests "*MiraiLinkResultTest" --tests "*AppErrorTest"` and observe the expected compilation failure.
3. Add sealed `AppError`, `DataError.Network`, `DataError.Local`, `AuthError`, `ValidationError` and `UnknownError` types.
4. Add typed `Success` and `Error` branches plus `map`, `mapError`, `onSuccess`, `onError` and `asEmptyResult` helpers.
5. Keep a temporary deprecated free-form compatibility factory only if required to migrate in compiling batches. It must map to `UnknownError`, must not expose its text, and must be removed in Task 8.
6. Run the targeted tests until green.

## Task 2: Build and test data-owned network classification

**Files:**

- Create `app/src/main/java/com/feryaeljustice/mirailink/data/model/response/generic/ApiErrorResponse.kt`.
- Create `app/src/main/java/com/feryaeljustice/mirailink/data/util/NetworkOperation.kt`.
- Create `app/src/main/java/com/feryaeljustice/mirailink/data/util/NetworkErrorMapper.kt`.
- Create `app/src/main/java/com/feryaeljustice/mirailink/data/util/SafeApiCall.kt`.
- Create `app/src/test/java/com/feryaeljustice/mirailink/data/util/NetworkErrorMapperTest.kt`.
- Create `app/src/test/java/com/feryaeljustice/mirailink/data/util/SafeApiCallTest.kt`.

**Steps:**

1. Write failing tests for no connection, timeout, serialization, HTTP 400, 403, 404, 409, 413, 429, 500, 503 and unknown failures.
2. Write failing context tests for login 401 as `InvalidCredentials`, authenticated 401 as `SessionExpired`, legacy invalid verification or two-factor code recognition, malformed bodies and unknown backend text.
3. Write a cancellation test proving `CancellationException` is rethrown.
4. Run `./gradlew testDebugUnitTest --tests "*NetworkErrorMapperTest" --tests "*SafeApiCallTest"` and observe failure.
5. Implement a private serializable payload decoder. Prefer stable server codes, then status plus `NetworkOperation`, then a closed legacy allowlist.
6. Implement `safeApiCall` for Retrofit suspend calls and ensure no raw body or cause is placed in the returned result.
7. Run targeted tests until green.

## Task 3: Migrate every remote datasource

**Files:**

- Modify `app/src/main/java/com/feryaeljustice/mirailink/data/datasource/AppConfigRemoteDataSource.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/data/datasource/CatalogRemoteDataSource.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/data/datasource/FeedbackRemoteDatasource.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/data/datasource/MatchRemoteDataSource.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ReportRemoteDataSource.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/data/datasource/SwipeRemoteDataSource.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/data/datasource/TwoFactorRemoteDataSource.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UsersRemoteDataSource.kt`.
- Remove `app/src/main/java/com/feryaeljustice/mirailink/domain/util/HttpUtils.kt` after its last caller is gone.
- Modify the corresponding ten datasource tests under `app/src/test/java/com/feryaeljustice/mirailink/data/datasource/`.

**Steps:**

1. Add datasource failure tests before each migration batch, asserting typed errors rather than strings.
2. Replace broad `Throwable` catches and `parseMiraiLinkHttpError` calls with `safeApiCall` and the correct `NetworkOperation`.
3. Keep existing chat creation idempotency: recover a valid `chatId` from a conflict response without exposing its message. Add tests for valid id, missing id and malformed conflict payload.
4. Map content resolver failures in profile photo operations to `ValidationError.InvalidMedia` or `DataError.Local.AccessDenied` as appropriate.
5. Convert password reset, verification and two-factor success prose to `Unit` through datasource, repository and use-case contracts where the string is only feedback.
6. Remove logging that prints profile content, backend error wording or technical messages.
7. Run each datasource test class after its file is migrated.
8. Run `rg -n "parseMiraiLinkHttpError|errorBody\(\).*message|result\.message" app/src/main/java/com/feryaeljustice/mirailink/data` and resolve every result.

## Task 4: Simplify repositories and use cases

**Files:**

- Modify repository contracts in `app/src/main/java/com/feryaeljustice/mirailink/domain/repository/`.
- Modify implementations in `app/src/main/java/com/feryaeljustice/mirailink/data/repository/`.
- Modify all result-returning use cases in `app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt`.
- Modify repository and use-case tests under `app/src/test/java/com/feryaeljustice/mirailink/data/repository/` and `app/src/test/java/com/feryaeljustice/mirailink/domain/usecase/`.

**Steps:**

1. Update tests first so repository and use-case failures use concrete `AppError` values.
2. Change reset, verification and two-factor feedback-only success contracts from `String` to `Unit`.
3. Remove redundant generic catches from use cases that only delegate to a repository.
4. Preserve business validation as typed `ValidationError` results.
5. For dependencies that can still throw outside Retrofit, catch at the owning boundary, rethrow cancellation and map unexpected failure to `UnknownError` without retaining the message.
6. Use result helpers in `CheckAppVersionUseCase` and other mapping use cases instead of reconstructing an error.
7. Run repository and use-case test packages after each feature group: auth, users, chat, catalog, feed and match, feedback and report, media, app config and onboarding.

## Task 5: Add presentation error and recovery primitives

**Files:**

- Create `app/src/main/java/com/feryaeljustice/mirailink/ui/error/UiText.kt`.
- Create `app/src/main/java/com/feryaeljustice/mirailink/ui/error/UiError.kt`.
- Create `app/src/main/java/com/feryaeljustice/mirailink/ui/error/AppErrorUiMapper.kt`.
- Create `app/src/main/java/com/feryaeljustice/mirailink/ui/error/RetryableViewModel.kt`.
- Create `app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkErrorContent.kt`.
- Modify `app/src/main/res/values/strings.xml`.
- Modify `app/src/main/res/values-en/strings.xml`.
- Modify `app/src/main/res/values-es/strings.xml`.
- Modify `app/src/main/res/values-v23/strings.xml`.
- Create `app/src/test/java/com/feryaeljustice/mirailink/ui/error/AppErrorUiMapperTest.kt`.
- Create `app/src/test/java/com/feryaeljustice/mirailink/ui/error/RetryableViewModelTest.kt`.
- Create `app/src/androidTest/java/com/feryaeljustice/mirailink/ui/components/MiraiLinkErrorContentTest.kt`.

**Steps:**

1. Write mapper tests covering every leaf error and asserting a resource-backed message, resource-backed action label and correct recovery kind.
2. Write retry tests proving that the last safe action runs once per tap, can be replaced, clears after success, and session expiration invokes sign-in recovery instead of retry.
3. Run the targeted tests and observe failure.
4. Implement `UiText`, `UiError`, `ErrorRecovery` and exhaustive `AppError.toUiError()`.
5. Implement a ViewModel base or composed controller that keeps mutable flow private, exposes immutable state, and stores recovery callbacks outside Compose state.
6. Ensure callbacks captured for retry contain only operation inputs and ViewModel methods, never Activity, Context, Composable state holders or navigation controllers.
7. Add neutral Spanish and English strings with explicit actions. Keep base, Spanish and API-specific resources consistent.
8. Implement an accessible error component with message text and a Material 3 action button. The component receives only `UiError` and `onAction`.
9. Run mapper, recovery and component compilation tests.

## Task 6: Migrate authentication and settings presentation

**Files:**

- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordViewModel.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordScreen.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationViewModel.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationScreen.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsViewModel.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackViewModel.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackScreen.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorViewModel.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt`.
- Modify the six corresponding ViewModel tests under `app/src/test/java/com/feryaeljustice/mirailink/ui/screens/`.

**Steps:**

1. Add failing ViewModel tests for invalid credentials, network retry, verification retry, password reset retry, settings operation retry and two-factor retry.
2. Replace error strings and exception-bearing state with `UiError`.
3. Keep form validation distinct from asynchronous errors and preserve entered data for `ReviewInput` recovery.
4. Render `MiraiLinkErrorContent` at existing error text locations.
5. Add `retryLastAction()` or `performErrorAction()` calls from each action button.
6. Represent session expiration as state that triggers the existing sign-in route. Do not call the failed authenticated use case again.
7. Remove error toast and snackbar paths in these screens while preserving unrelated success feedback.
8. Run the migrated ViewModel tests and compile the affected UI.

## Task 7: Migrate the remaining presentation flows

**Files:**

- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatUiState.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatViewModel.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatScreen.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeScreen.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesViewModel.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesScreen.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureViewModel.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureScreen.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt` only where typed result access changes.
- Modify the corresponding ViewModel tests under `app/src/test/java/com/feryaeljustice/mirailink/ui/screens/`.

**Steps:**

1. Write failing tests for load, send, upload, edit and refresh retry behavior before each feature migration.
2. Stop exposing `MiraiLinkResult` from `ProfilePictureViewModel` and expose presentation state instead.
3. Convert error-only logs and ignored user-triggered failures into `UiError` state where the user needs recovery.
4. Move profile edit errors out of transient events and into `EditProfileUiState`; keep unrelated success navigation behavior unchanged.
5. Render the reusable error component in each existing error location and wire its action to the owning ViewModel.
6. Keep splash app-config failure non-blocking because it is intentionally invisible and does not represent a user-triggered recoverable operation.
7. Run every migrated ViewModel test class.

## Task 8: Remove compatibility paths and close test debt

**Files:**

- Modify all remaining tests returned by `rg -l "MiraiLinkResult|\.message|\.exception" app/src/test app/src/androidTest`.
- Modify `app/src/test/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModelTest.kt` to provide `store` and `isInChristmasMode`.
- Modify `app/src/main/java/com/feryaeljustice/mirailink/domain/util/MiraiLinkResult.kt` to remove temporary free-form compatibility APIs.
- Modify `docs/ai/architecture.md` and `docs/ai/development-and-testing.md` with the final error flow and verified commands.

**Steps:**

1. Migrate remaining fixtures and assertions to concrete errors.
2. Fix the verified pre-existing Splash test constructor mismatch without changing production splash behavior.
3. Remove deprecated message and exception constructors and factories.
4. Run searches for forbidden propagation:

   ```powershell
   rg -n "MiraiLinkResult\.Error\([^A]|result\.message|result\.exception|parseMiraiLinkHttpError" app/src/main app/src/test app/src/androidTest
   rg -n "errorBody\(\).*string|Throwable\.message|exception\.message" app/src/main/java/com/feryaeljustice/mirailink/ui app/src/main/java/com/feryaeljustice/mirailink/domain
   ```

5. Resolve every relevant result. Diagnostic-only technical logging must remain confined to the owning data boundary and must not contain server bodies or personal data.
6. Update AI documentation so future agents preserve the contract.

## Task 9: Full verification and branch handoff

**Files:**

- Review all changed files.
- Do not modify unrelated baseline issues unless required for compilation or requested scope.

**Steps:**

1. Run `git diff --check`.
2. Run `./gradlew testDebugUnitTest` and record the exact result.
3. Run `./gradlew assembleDebug` and record the exact result.
4. Run `./gradlew lintDebug` and record any remaining baseline or introduced findings.
5. Run `./gradlew compileDebugAndroidTestKotlin` to verify instrumented test compilation without contacting the real backend.
6. Re-run the forbidden propagation searches from Task 8.
7. Review `git diff --stat`, `git status --short` and the final diff for secrets, unrelated edits and raw backend wording.
8. Use `mobiai-mobile-verification` before making completion claims.
9. Use `mobiai-mobile-finishing-branch` and keep the branch available unless the user explicitly chooses another integration option.
