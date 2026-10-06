# Checklist de Tareas: Sistema de Verificación de Correo, 2FA, Recuperación de Contraseña y Control de Versiones

- **Especificación funcional**: `docs/features/auth_security_and_version_control/spec.md`
- **Plan técnico**: `docs/features/auth_security_and_version_control/plan.md`

- - -

## Fase 1: Ramas Git y Migraciones en Backend
- [x] Crear y posicionar la rama `feature/auth-security-and-version-control` en `MiraiLink` (Android).
- [x] Crear y posicionar la rama `feature/auth-security-and-version-control` en `MiraiLink-Backend` (Backend).
- [x] Refactorizar `MiraiLink-Backend/scripts/migrate-db.js` para exportar `runMigrations(pool)` reusable.
- [x] Modificar `MiraiLink-Backend/src/server.js` para ejecutar `runMigrations()` de forma automática en el arranque antes de `app.listen()`.

## Fase 2: Backend - Servicio de Correos Desacoplado y Flujos de Auth / 2FA
- [x] Modificar `MiraiLink-Backend/src/utils/mailer.js`:
  - Implementar wrapper seguro que capture fallos SMTP sin arrojar 500 y registre `[DEV EMAIL SIMULATOR]` en consola con código y deep link.
  - Implementar y exportar `sendVerificationEmail(to, code, token)` y `sendPasswordResetEmail(to, code, token)`.
- [x] Modificar `MiraiLink-Backend/src/controllers/auth.controller.js`:
  - En `register`: insertar con `is_verified = FALSE`, generar codigo/token de verificación y enviar correo/log seguro.
  - En `login`: responder con `requires2FA = true`, `challengeToken` y `expiresIn = 300` cuando 2FA este activo; responder con `token`, `userId` y `isVerified` cuando no.
  - En `setup2FA`: devolver `{ otpauth_url, base32, recovery_codes }` alineado con Android.
  - En `disable2FA`: permitir desactivación autenticada por sesión, eliminando fila en `user_2fa` y purgando registros en `recovery_codes`.
  - En `loginVerify2FALastStep`: validar `challengeToken` y `code` (soportando TOTP o código de recuperación de uso único), retornando `token`, `userId` e `isVerified`.
  - En `requestPasswordReset`: generar código de 6 dígitos, registrar en `password_reset_tokens` y emitir correo/log seguro.
  - En `confirmPasswordReset`: validar token/codigo y actualizar `password_hash`.
- [x] Modificar esquemas de validación en `MiraiLink-Backend/src/validation/auth.schemas.js` para adaptarse a los contratos ajustados.
- [x] Ejecutar tests de Backend (`npm run test:unit`, `npm run test:integration`) y verificar que todo pase en verde.

## Fase 3: Android - Capa de Datos y Dominio
- [x] Modificar `LoginResponse.kt` en `data/model/response/auth/` para soportar campos `requires2FA`, `challengeToken`, `expiresIn`, `isVerified` y token nullable.
- [x] Modificar `TwoFactorSetupResponse.kt` en `data/model/response/auth/two_factor/` asegurando nombres `@SerialName("otpauth_url")`, `@SerialName("base32")` y `@SerialName("recovery_codes")`.
- [x] Actualizar `TwoFactorApiService.kt` y `TwoFactorRemoteDataSource.kt`:
  - Enviar `{ challengeToken, code }` en `loginVerifyTwoFactorLastStep` y recibir `LoginResponse`.
  - Ajustar llamada a `disableTwoFactor` con cuerpo opcional.
- [x] Actualizar caso de uso `LoginVerifyTwoFactorLastStepUseCase` en `domain/usecase/auth/two_factor/`.
- [x] Actualizar `CheckAppVersionUseCase.kt` en `domain/usecase/` para computar estrictamente los 3 estados: `mustUpdate`, `shouldUpdate` e `isUpToDate`.

## Fase 4: Android - Control de Versiones Estricto
- [x] Actualizar `UpdateGate.kt` y `MiraiLinkDialog.kt`:
  - Configurar `DialogProperties(dismissOnBackPress = !force, dismissOnClickOutside = !force)`.
  - Deshabilitar cualquier forma de cierre en actualización forzada (`force == true`).
- [x] Modificar `SplashScreenViewModel.kt`:
  - Si `mustUpdate == true`: emitir estado y detener la ejecución de la corrutina (`return@launch`), impidiendo que se navegue a Home u Onboarding.
  - Si `shouldUpdate == true`: desplegar diálogo descartable y permitir continuar tras pulsar cancelar/descartar.
- [x] Probar compilación limpia en Android con `./gradlew assembleDebug`.

## Fase 5: Android - Verificación de Cuenta y Recuperación de Contraseña
- [x] Modificar `VerificationScreen.kt` y `VerificationViewModel.kt`:
  - Estructurar como pantalla intermedia completa con campo para código de 6 dígitos, temporizador de reenvío y botón de cerrar sesión.
  - Al completar verificación exitosa: invocar guardado de credenciales e ingresar a `HomeScreen`.
- [x] Registrar `entry<AppScreen.VerificationScreen>` en `NavWrapper.kt`.
- [x] Configurar deep links en `AndroidManifest.xml` para `mirailink://verify` y `mirailink://reset-password`.
- [x] Modificar `RecoverPasswordScreen.kt` y `RecoverPasswordViewModel.kt`:
  - Agregar campo de confirmación de contraseña en paso 2.
  - Validar coincidencia de contraseñas en tiempo real.
  - Mostrar diálogo o mensaje de éxito confirmando el cambio y regresar a `AuthScreen` sin iniciar sesión.
  - Autollenar token si se ingresa vía deep link.

## Fase 6: Android - Flujo Completo de Doble Factor (2FA)
- [x] Actualizar `ConfigureTwoFactorScreen.kt` y sus diálogos:
  - En `TwoFactorSetupDialog.kt`: renderizar QR con `QrCodeImage`, clave `base32` con soporte de copia rápida con pulsación prolongada (con Toast informativo), y campo de código TOTP.
  - En `TwoFactorSetupCompletedDialog.kt`: desplegar los 8 códigos de recuperación con advertencia enfatizada y botón "Copiar todos los códigos".
  - En desactivación: mostrar diálogo simple de confirmación ("¿Estas seguro de desactivar el doble factor?") y procesar desactivación con purga.
- [x] Actualizar `AuthScreen.kt` y `AuthViewModel.kt`:
  - Interceptar respuesta de login con `requires2FA = true`.
  - Desplegar pantalla/modal bloqueante de desafío 2FA pidiendo código TOTP o de recuperación.
  - Incluir botón "Cancelar / Volver" que regrese al login limpio.
  - Completar autenticación e inicio de sesión tras validar el desafío.

## Fase 7: Verificación y Suite de Pruebas
- [x] Ejecutar suite de pruebas de Backend (`npm run test` en `MiraiLink-Backend` con 80 tests pasados).
- [x] Ejecutar suite de pruebas unitarias en Android (`.\gradlew.bat testDebugUnitTest` con 401 tests pasados).
- [x] Ejecutar compilación completa de Android (`.\gradlew.bat assembleDebug` completado con éxito).
- [ ] Realizar pruebas manuales de extremo a extremo en dispositivo/emulador:
  - Registro -> Verificación bloqueante -> Login automático.
  - Recuperación con contraseñas no coincidentes -> Coincidentes -> Mensaje de éxito -> Retorno a Login.
  - Activación 2FA con QR y copia manual -> Guardado de códigos de respaldo -> Login con 2FA -> Desactivación con confirmación.
  - Simulación de actualización obligatoria (bloqueo total sin cierre) y actualización sugerida (descartable).

