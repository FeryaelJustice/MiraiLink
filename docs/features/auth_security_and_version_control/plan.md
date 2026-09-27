# [APROBADO] Plan Tecnico de Arquitectura: Sistema de Verificacion de Correo, 2FA, Recuperacion de Contrasena y Control de Versiones

- **Especificacion funcional asociada**: `docs/features/auth_security_and_version_control/spec.md`
- **Estado**: [APROBADO]
- **Fecha**: 2026-09-27
- **Modulos Afectados**: `:app` (`com.feryaeljustice.mirailink`), `MiraiLink-Backend` (Node.js Express 5 + PostgreSQL)

- - -

## 1. Hechos Verificados en el Proyecto (Sin Alucinaciones)

Informacion verificada rigurosamente en `gradle/libs.versions.toml`, `app/build.gradle.kts` y `MiraiLink-Backend/package.json`:

- **Android Client**:
  - **Lenguaje & JVM**: Kotlin `2.4.10` / Java 17
  - **Compilador & Build Tool**: AGP `9.4.1`, Gradle `9.6.1`, KSP `2.3.8`
  - **SDK Targets**: Min SDK `26`, Compile SDK `37`, Target SDK `37`
  - **UI**: Jetpack Compose BOM `2026.09.00` con Material 3
  - **Navegacion**: Navigation 3 (`androidx.navigation3:navigation3-core:1.1.7`)
  - **Inyeccion de Dependencias**: Koin BOM `4.2.2` con Koin Annotations y ViewModel integration
  - **Red & Serializacion**: Retrofit `3.0.0`, OkHttp `5.5.0`, Kotlinx Serialization `1.11.0`
  - **Generacion de QR**: ZXing Core `3.5.4` (`com.google.zxing:core:3.5.4`) ya implementado en `QrCodeImage.kt`
- **Backend Service**:
  - **Runtime**: Node.js >= 22 (ES Modules)
  - **Framework Web**: Express `5.2.1`
  - **Base de Datos**: PostgreSQL (`pg: ^8.23.0`)
  - **Autenticacion & Criptografia**: `bcrypt: ^6.0.0`, `jsonwebtoken: ^9.0.3`, `speakeasy: ^2.0.0`
  - **Correo**: `nodemailer: ^10.0.10`

- - -

## 2. Impacto Arquitectonico y Contratos por Capas

### 2.1. Backend (`MiraiLink-Backend`)

#### A. Servicio de Correo Generico y Plantillas Escalables (`src/utils/mailer.js`)
- Arquitectura desacoplada y escalable:
  - Funcion base generica `sendGenericEmail({ to, subject, html, text, logData })`:
    - Si faltan credenciales SMTP (`EMAIL_USER`, `EMAIL_PASSWORD`), emite en terminal una tarjeta visual estructurada `[DEV EMAIL SIMULATOR]`:
      ```text
      ======================================================
      📧 [DEV EMAIL SIMULATOR] - Plantilla: <templateName>
      Para: <email>
      Asunto: <subject>
      Codigo: >>> <code> <<<
      Deep Link: >>> <link> <<<
      Detalles adicionales: <metadata>
      ======================================================
      ```
    - Si existen credenciales SMTP, envia el correo formateado en HTML responsivo y texto plano con `nodemailer`. Si el transporte SMTP falla (error de red/autenticacion), captura el error con `try-catch`, emite advertencia por log y no rompe la peticion (retorna `{ success: false, simulated: true }`).
  - Generador de plantillas escalables (`getVerificationTemplate`, `getPasswordResetTemplate`, `getBaseEmailTemplate`):
    - Plantilla base con cabecera de MiraiLink, tarjeta contenedora, boton de accion estilizado y pie con aviso legal/expiracion.
    - Especializacion por caso de uso:
      - `sendVerificationEmail(to, code, token)`
      - `sendPasswordResetEmail(to, code, token)`
      - Escalable a futuras notificaciones o avisos de seguridad.

#### B. Controlador de Autenticacion (`src/controllers/auth.controller.js`)
- `register`:
  - Inserta nuevo usuario con `is_verified = FALSE`.
  - Genera codigo de 6 digitos y hash de expiracion de 15 minutos en `verification_tokens`.
  - Invoca `sendVerificationEmail` de forma segura.
  - Retorna `{ message: 'User created', userId, isVerified: false, token }`.
- `login`:
  - Si `two_fa_enabled = true`: retorna `{ requires2FA: true, challengeToken, expiresIn: 300 }`.
  - Si `two_fa_enabled = false`: retorna `{ token, userId, isVerified: user.is_verified }`.
- `setup2FA`:
  - Genera secreto TOTP y 8 codigos de recuperacion aleatorios (16 caracteres hexadecimales).
  - Retorna DTO con claves alineadas:
    ```javascript
    return res.json({
        otpauth_url: secret.otpauth_url,
        base32: secret.base32,
        recovery_codes: recoveryCodes
    });
    ```
- `disable2FA`:
  - Como el usuario ya posee sesion autenticada valida, desactiva 2FA eliminando el registro en `user_2fa` y purgando todos sus registros en `recovery_codes`:
    ```sql
    DELETE FROM user_2fa WHERE user_id = $1;
    DELETE FROM recovery_codes WHERE user_id = $1;
    ```
  - Retorna `{ message: '2FA disabled' }`.
- `loginVerify2FALastStep`:
  - Recibe `{ challengeToken, code }`.
  - Verifica si `code` es TOTP de 6 digitos o codigo de recuperacion de 16 caracteres hexadecimales (usando `useRecoveryCode`).
  - Si es valido, retorna `{ token: createAccessToken(user), userId: user.id, isVerified: user.is_verified }`.
- `requestPasswordReset`:
  - Genera codigo numerico de 6 digitos, guarda en `password_reset_tokens` (expiracion 5 minutos) y emite correo/log seguro con deep link `mirailink://reset-password?token=...`.
- `confirmPasswordReset`:
  - Valida coincidencia de token y correo. Actualiza `password_hash` y purga tokens.

#### C. Arranque y Migraciones (`src/server.js` y `scripts/migrate-db.js`)
- Refactorizar `migrate-db.js` para exportar una funcion reusable `runMigrations(pool)`.
- En `src/server.js`, invocar `runMigrations(db.pool)` de forma asincrona antes de `app.listen()`.

---

### 2.2. Capa de Datos Android (`data/`)

#### A. Modelos de Red (`data/model/`)
- Modificar `data/model/response/auth/LoginResponse.kt`:
  ```kotlin
  @Serializable
  data class LoginResponse(
      @SerialName("token") val token: String? = null,
      @SerialName("userId") val userId: String? = null,
      @SerialName("requires2FA") val requires2FA: Boolean = false,
      @SerialName("challengeToken") val challengeToken: String? = null,
      @SerialName("expiresIn") val expiresIn: Int? = null,
      @SerialName("isVerified") val isVerified: Boolean = false,
  )
  ```
- Modificar `data/model/response/auth/two_factor/TwoFactorSetupResponse.kt`:
  ```kotlin
  @Serializable
  data class TwoFactorSetupResponse(
      @SerialName("otpauth_url") val otpAuthUrl: String,
      @SerialName("base32") val baseCode: String,
      @SerialName("recovery_codes") val recoveryCodes: List<String> = emptyList(),
  )
  ```

#### B. Servicios API y DataSources
- En `TwoFactorApiService.kt`:
  - `loginVerifyTwoFactorLastStep` debe recibir `@Body body: Map<String, String>` con `challengeToken` y `code`, retornando `LoginResponse`.
  - `disableTwoFactor` recibe cuerpo vacio o de confirmacion opcional y retorna `Response<Unit>`.
- En `TwoFactorRemoteDataSource.kt`:
  - Actualizar `loginVerifyTwoFactorLastStep(challengeToken: String, code: String): MiraiLinkResult<LoginResponse>`.

---

### 2.3. Capa de Dominio Android (`domain/`)

#### A. Casos de Uso
- Actualizar `LoginVerifyTwoFactorLastStepUseCase`:
  - Recibe `challengeToken` y `code`, retornando `MiraiLinkResult<LoginResponse>`.
- Actualizar `CheckAppVersionUseCase`:
  - Retorna `VersionCheckResult(mustUpdate: Boolean, shouldUpdate: Boolean, message: String, playStoreUrl: String)`.
  - Logica de 3 estados:
    - Estado 1: `!mustUpdate && !shouldUpdate` (al dia).
    - Estado 2: `!mustUpdate && shouldUpdate` (flexible/sugerida).
    - Estado 3: `mustUpdate` (critica/obligatoria).

---

### 2.4. Capa de Presentacion Android (`ui/`)

#### A. Control de Versiones (`SplashScreenViewModel` y `UpdateGate`)
- En `SplashScreenViewModel.kt`:
  - Si `versionResult` retorna `mustUpdate == true`:
    - Asignar `_updateDiagInfo.value = info.toVersionCheckResultViewEntry()`.
    - Asignar `uiState.value = SplashUiState.Idle`.
    - **`return@launch`**: No iniciar el bloque paralelo de onboarding / autologin.
  - Si `shouldUpdate == true`:
    - Asignar `_updateDiagInfo.value` y permitir que el usuario pulse "Continuar", momento en el cual se procede con la navegacion.
- En `UpdateGate.kt` y `MiraiLinkDialog.kt`:
  - Incorporar parametro `properties: DialogProperties = DialogProperties(dismissOnBackPress = !force, dismissOnClickOutside = !force)`.

#### B. Pantalla Intermedia de Verificacion (`VerificationScreen`)
- Registrar `entry<AppScreen.VerificationScreen>` en `NavWrapper.kt`.
- `VerificationScreen.kt`:
  - Campos de entrada: Codigo de 6 digitos.
  - Boton de reenviar codigo con cooldown visual de 60 segundos.
  - Al recibir deep link `mirailink://verify?token=...`, autollenar el campo o ejecutar verificacion directa.
  - Al completar la verificacion: invocar callback de guardado de sesion e ingresar a `HomeScreen`.
  - Boton "Cerrar sesion" que llama a `miraiLinkSession.clearSession()` y redirige a `AuthScreen`.

#### C. Pantalla de Recuperacion de Contrasena (`RecoverPasswordScreen`)
- Agregar campo `confirmPassword` en el formulario del paso 2.
- Validar coincidencia:
  - Si `newPassword != confirmPassword`: emitir error `R.string.error_passwords_do_not_match`.
- Al confirmar exito:
  - Mostrar feedback amigable ("Su contrasena se ha restablecido exitosamente").
  - Redirigir a `AuthScreen` (modo login) sin auto-login.
- Soportar deep link `mirailink://reset-password?token=...` pre-llenando el token y situando al usuario directamente en el paso 2.

#### D. Pantalla de Configuracion 2FA en Ajustes (`ConfigureTwoFactorScreen`)
- `TwoFactorSetupDialog.kt`:
  - Renderiza `QrCodeImage` a partir de `otpAuthUrl`.
  - Muestra la clave secreta `base32` con soporte de copia rapida con pulsacion prolongada (`combinedClickable(onLongClick = ...)` o icono de portapapeles) acompanado de Toast informativo.
  - Campo para ingresar el codigo TOTP generado por la app autenticadora.
- `TwoFactorSetupCompletedDialog.kt`:
  - Despliega los 8 codigos de recuperacion formateados en cuadricula de dos columnas.
  - Alerta critica de seguridad: *"Conserva estos codigos en un lugar seguro. Si pierdes el autenticador, cada uno te permitira acceder una unica vez"*.
  - Boton "Copiar todos los codigos" y boton "Entendido / Finalizar".
- Desactivacion de 2FA:
  - Dialogo de confirmacion simple: *"¿Estas seguro de que deseas desactivar el doble factor? Tus codigos de respaldo se invalidaran de forma definitiva"*.
  - Al confirmar, invoca `disableTwoFactor` y actualiza la tarjeta a estado deshabilitado.

#### E. Desafio 2FA en Inicio de Sesion (`AuthScreen`)
- Cuando `login` responde con `requires2FA = true`:
  - Presentar dialogo/pantalla bloqueante de desafio 2FA.
  - El usuario puede ingresar el codigo TOTP de 6 digitos o cualquiera de sus codigos de recuperacion de 16 caracteres.
  - Opcion visible de "Cancelar / Volver" que limpia el challenge token y regresa al login sin inconsistencias de estado.

---

## 3. Estrategia de Testing

- **Backend**:
  - `npm run test:unit`: Validar `mailer.js` en modo fallback y en modo simulador sin credenciales.
  - `npm run test:integration`:
    - Flujo completo de registro (verificando que `is_verified = false`).
    - Flujo de login con 2FA (verificando generacion de challenge token y acceso con TOTP o recovery code de un solo uso).
    - Desactivacion de 2FA y purga de codigos en base de datos.
- **Android**:
  - `.\gradlew.bat testDebugUnitTest`:
    - `CheckAppVersionUseCaseTest`: Cobertura completa de los 3 estados (isUpToDate, shouldUpdate, mustUpdate).
    - `VerificationViewModelTest`: Comprobacion de validacion de codigo y transicion de estados.
    - `RecoverPasswordViewModelTest`: Validacion de contrasenas no coincidentes y confirmacion valida.
    - `ConfigureTwoFactorViewModelTest`: Activacion, obtencion de clave secreta y desactivacion.

---

## 4. Riesgos Tecnicos y Mitigaciones

- **Riesgo 1 (Muerte de proceso durante el Desafio 2FA)**:
  - *Mitigacion*: El `challengeToken` posee 300 segundos (5 minutos) de validez en el servidor JWT; se preserva en el ViewModel ante recreacion de configuracion.
- **Riesgo 2 (Inconsistencia de red al enviar correos)**:
  - *Mitigacion*: El sistema `sendMailSafe` garantiza que si SMTP no responde, la peticion HTTP no falle con 500, asegurando que las pruebas locales y en staging nunca se detengan.
- **Riesgo 3 (Cierre accidental del modal de version critica)**:
  - *Mitigacion*: Configuracion estricta de `DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)` y suspension de navegacion en `SplashScreenViewModel`.
