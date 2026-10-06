# [APROBADO] Especificación Funcional: Sistema de Verificación de Correo, Recuperación de Contraseña, Autenticación 2FA y Control de Versiones

- **Fecha**: 2026-09-27
- **Estado**: [APROBADO]
- **Autor / Responsable**: Antigravity & FeryaelJustice
- **Módulos Afectados**: `:app` (`com.feryaeljustice.mirailink`), `MiraiLink-Backend` (Express 5 + PostgreSQL)

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**:
  - **Verificación de cuenta**: El registro crea cuentas sin exigir verificación real previa al uso normal, el servicio de correo falla si no hay SMTP configurado provocando bloqueos, y no existe un flujo robusto y bloqueante ni soporte para deep links / futura web.
  - **Recuperación de contraseña**: El flujo actual en UI no valida doble campo de contraseña ("nueva contraseña" y "repetir contraseña"), y no ofrece soporte cómodo para resolver mediante enlace directo o código.
  - **Doble Factor de Autenticación (2FA / TOTP)**: La implementación previa presenta discrepancias críticas de contrato entre backend y Android (deserialización de JSON con nombres de campos desiguales, falta del secret en texto plano para copia manual con pulsación larga, flujo de login que no intercepta adecuadamente con challenge token y códigos de respaldo descartables de un solo uso). Al desactivar 2FA, se deben purgar/invalidar los códigos antiguos.
  - **Control de Versiones de la Aplicación**: La comprobación de versión actual en `SplashScreenViewModel` continua la navegación normal en paralelo e ignora el bloqueo forzado (`mustUpdate`), permitiendo que el usuario cierre el diálogo o pase a la aplicación principal. Además, el diálogo no bloquea eventos de retroceso o pulsaciones fuera del contenedor en actualizaciones críticas.
  - **Resiliencia de Backend sin SMTP**: En entornos de desarrollo o pruebas sin servidor de correo activo, los envíos deben simularse limpiamente en logs de consola sin arrojar excepciones 500 no controladas.
- **Objetivo**:
  - Implementar un sistema unificado y desacoplado de correo en backend con simulador visual por logs para desarrollo y soporte SMTP configurable.
  - Crear un flujo integral de verificación de cuenta (código de 6 dígitos y hash para deep link / web) con pantalla intermedia bloqueante (`VerificationScreen`), que al completarse inicie sesión de forma directa.
  - Implementar el restablecimiento seguro de contraseña con doble confirmación de clave y retorno a login sin inicio automático.
  - Perfeccionar el flujo 2FA en Ajustes (generación de QR, frase secreta copiable con pulsación prolongada, activación con código TOTP, visualización clara de códigos de recuperación de uso único, invalidación al desactivar con confirmación y pantalla bloqueante en login que acepte TOTP o código de recuperación con opción de cerrar sesión).
  - Robustecer el control de versiones en 3 estados estrictos: sin actualización, actualización sugerida (descartable) y actualización obligatoria (estrictamente bloqueante, no descartable).
  - Coordinar la creación de ramas dedicadas en Git para Android y Backend, junto con la comprobación automática de migraciones SQL al inicio del backend.

- - -

## 2. Situación Actual

- **Backend**:
  - `src/utils/mailer.js`: Contiene un fallback básico si faltan variables, pero `sendVerificationEmail` solo cubre el asunto de verificación, no restablecimiento. Si ocurre un fallo en transporte SMTP, puede interrumpir la llamada.
  - `src/controllers/auth.controller.js`:
    - `setup2FA` devuelve `{ otpauthUrl, recoveryCodes }`, omitiendo la clave secreta en texto base32 (`base32`), lo que impide la copia manual.
    - `login` devuelve `{ requires2FA: true, challengeToken, expiresIn }` cuando 2FA está activo, pero `LoginResponse` en Android solo espera `{ token: String }`, provocando fallos de deserialización.
    - `loginVerify2FALastStep` valida `challengeToken` y `code`, pero Android enviaba `userId` y `code`.
  - Migraciones de base de datos: Se ejecutan manualmente vía `npm run db:migrate`. El servidor `src/server.js` no verifica migraciones al arrancar.
- **Android**:
  - `SplashScreenViewModel.kt`: Lanza la comprobación de versión pero no suspende la navegación; lanza `onboardingDeferred` y `autologinDeferred` en paralelo y sobreescribe el estado de la UI con `SplashUiState.Navigate`, descartando el diálogo de actualización.
  - `MiraiLinkDialog.kt`: No configura `DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)` para modales críticos.
  - `VerificationDialog.kt` y `VerificationScreen.kt`: Solo existe como diálogo flotante en `AuthScreen.kt`; no hay pantalla intermedia formal en el grafo de navegación ni gestión de deep links.
  - `RecoverPasswordScreen.kt`: Solo dispone de un campo de contraseña nueva, sin confirmación redundante para evitar errores tipográficos.
  - `ConfigureTwoFactorScreen.kt` y `TwoFactorSetupDialog.kt`: Despliega códigos de respaldo antes de verificar el código; falta la acción de copiar con pulsación prolongada la clave en texto y el aviso de respaldo tras la confirmación.

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)

1. **Ramas de Trabajo Git y Migraciones**:
   - Creación de rama `feature/auth-security-and-version-control` en `MiraiLink` y `MiraiLink-Backend`.
   - Comprobacion/ejecucion automática de migraciones pendientes en el arranque del servidor backend.
2. **Servicio de Correos Desacoplado y Resiliente (Backend)**:
   - Módulo unificado de envío de correos con modo simulador en consola cuando falte configuración SMTP o cuando falle la conexión.
   - Plantillas claras para: Verificación de Cuenta y Restablecimiento de Contraseña.
   - Logs estructurados con el código y el enlace directo generado para pruebas locales rápidas.
3. **Flujo de Verificación de Cuenta (Backend y Android)**:
   - Registro con `is_verified = FALSE` por defecto.
   - Pantalla intermedia dedicada bloqueante (`VerificationScreen`) en el grafo de navegación que intercepta a usuarios no verificados.
   - Soporte para código de 6 dígitos y para hash/token directo mediante deep link (`mirailink://verify?token=...` / enlace web futuro).
   - Acción de reenvío de código con control de tiempo/rate-limit.
   - Inicio de sesión automático tras verificación exitosa.
4. **Flujo de Recuperación de Contraseña (Backend y Android)**:
   - Solicitud con correo electrónico.
   - Pantalla de restablecimiento con codigo/token (o enlace directo con autofill) y doble campo obligatorio: "Nueva contraseña" y "Confirmar contraseña" con validación de coincidencia en tiempo real.
   - Al completar con éxito: mostrar mensaje amigable de éxito ("Su contraseña se ha restablecido correctamente") y redirigir al Login (sin inicio de sesión automático).
5. **Flujo Completo de Doble Factor (2FA / TOTP) (Backend y Android)**:
   - Pantalla de configuración en Ajustes:
     - Generación de QR en backend/Android y presentación de la clave secreta manual (`base32`) con acción de copia al mantener pulsado.
     - Introducción del código de 6 dígitos para activar 2FA.
     - Generación y despliegue modal de 8 códigos de recuperación con advertencia enfatizada de seguridad y botón de copiar todos los códigos.
     - Botón de desactivar 2FA cuando este activo, con diálogo de confirmación simple ("¿Estas seguro de que deseas desactivar el doble factor?") sin requerir código extra.
     - Al desactivar 2FA, purgar e invalidar permanentemente todos los códigos de recuperación asociados.
   - En Inicio de Sesión:
     - Detección de 2FA activo: respuesta con `challengeToken`.
     - Pantalla/modal bloqueante en Android pidiendo código TOTP de aplicación o código de respaldo.
     - Cada código de recuperación utilizado queda invalidado permanentemente (`used = TRUE`).
     - Botón accesible de "Cerrar sesión" o "Cancelar" para regresar a la pantalla de Login limpia sin inconsistencias de estado.
6. **Sistema de Control de Versiones (Backend y Android)**:
   - Evaluación estricta de 3 estados:
     - **Estado 1 (Actualizado)**: Versión actual >= `latestVersionCode`. Sin diálogos.
     - **Estado 2 (Actualización flexible)**: `minVersionCode` <= Versión actual < `latestVersionCode`. Diálogo informativo descartable; permite continuar usando la aplicación.
     - **Estado 3 (Actualización critica obligatoria)**: Versión actual < `minVersionCode`. Diálogo estrictamente bloqueante:
       - No descartable por toque exterior ni botón atrás de Android (`DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)`).
       - Detiene la navegación en Splash; no permite transicionar a Auth o Home.
       - Acción única: redirigir a Google Play Store / URL de actualización.

### 3.2. Fuera del Alcance (Out of Scope)

- Integración con proveedores externos de SMS para 2FA (se mantiene exclusivamente TOTP estándar RFC 6238 y códigos de respaldo).
- Desarrollo del frontend web completo (se garantiza la compatibilidad de rutas, endpoints y tokens para consumo web futuro).

- - -

## 4. Casuísticas y Comportamiento Mobile

- **Modo Online vs Modo Offline Demo**:
  - En Modo Demo, el usuario de prueba cuenta con estado verificado y 2FA simulable localmente sin conexión de red.
  - La comprobación de versiones omite el bloqueo si no hay red o si el backend no está disponible, permitiendo continuar en modo offline/contingencia.
- **Ciclo de Vida y Recuperación de Estado**:
  - `TwoFactorChallenge` y códigos ingresados se preservan ante rotaciones de pantalla mediante `rememberSaveable` o `SavedStateHandle`.
  - La pantalla bloqueante de actualización obligatoria persiste intacta ante cambios de configuración y recreación de la actividad.
- **Manejo del Teclado y Ergonomía**:
  - `imePadding()` aplicado en todas las pantallas y diálogos para evitar que el teclado oculte campos de código o contraseña.
  - Objetivos táctiles de al menos 48 x 48 dp en botones de copia, envío y cierre.

- - -

## 5. Criterios de Aceptación (Formato Given - When - Then)

### Criterio 1: Verificación de cuenta obligatoria tras registro
- **Dado que**: Un usuario se registra con un nuevo correo electrónico.
- **Cuando**: Se completa la llamada de registro.
- **Entonces**: La cuenta se crea con `is_verified = false`, el backend imprime en logs el codigo/enlace simulado sin fallar, y Android presenta la pantalla intermedia dedicada `VerificationScreen` impidiendo acceder a la app hasta validar el código o enlace. Al validar, inicia sesión directamente.

### Criterio 2: Restablecimiento de contraseña con doble comprobación
- **Dado que**: Un usuario solicita recuperar su contraseña en `RecoverPasswordScreen`.
- **Cuando**: Recibe el codigo/enlace e introduce la nueva contraseña y la confirmación.
- **Entonces**: Si no coinciden, se muestra error de validación local. Si coinciden y el código es válido, se actualiza la contraseña en base de datos, se muestra mensaje de confirmación y se redirige a la pantalla de inicio de sesión (sin iniciar sesión directamente).

### Criterio 3: Activación y uso de Doble Factor con códigos de recuperación
- **Dado que**: El usuario accede a Ajustes -> Doble Factor de Autenticación.
- **Cuando**: Escanea el QR o copia la clave secreta por pulsación larga, e introduce el código TOTP válido.
- **Entonces**: Se activa 2FA, se muestran 8 códigos de respaldo de un solo uso con advertencia de guardado, y en el próximo login se requiere TOTP o código de recuperación para obtener el token de acceso.

### Criterio 4: Desactivación de Doble Factor
- **Dado que**: El usuario tiene 2FA habilitado en su cuenta.
- **Cuando**: Pulsa desactivar 2FA y confirma en el diálogo de confirmación simple.
- **Entonces**: Se desactiva 2FA, se eliminan todos los códigos de respaldo asociados y el botón de Ajustes pasa a permitir nueva configuración.

### Criterio 5: Actualización de versión critica bloqueante
- **Dado que**: La versión del cliente es inferior a `min_supported_version_code` del backend.
- **Cuando**: La aplicación inicia en `SplashScreen`.
- **Entonces**: Se muestra el diálogo de actualización obligatoria, se bloquea la navegación a cualquier otra pantalla, se inhabilitan toques exteriores y botón atrás, permitiendo únicamente pulsar en "Actualizar ahora" hacia la Play Store.

- - -

## 6. Resolución de Decisiones Acordadas

- [x] **Decisión 1 (UX Verificación)**: Pantalla intermedia dedicada (`VerificationScreen`) en el grafo de navegación con botón de reenvío de código y opción clara de cerrar sesión para volver a login.
- [x] **Decisión 2 (Deep Links)**: Soporte completo para deep links `mirailink://verify?token=...` y `mirailink://reset-password?token=...` con autollenado de token y preparado para URLs web directas.
- [x] **Decisión 3 (Seguridad al Desactivar 2FA)**: Diálogo de confirmación modal simple ("¿Estas seguro de desactivar el doble factor?") sin requerir código extra, aprovechando la sesión autenticada del usuario, purgando e invalidando todos los códigos de respaldo en base de datos.
- [x] **Decisión 4 (Migraciones en Arranque)**: Ejecución automática y transparente de la comprobación y aplicación de migraciones SQL pendientes en `src/server.js` al iniciar el backend.
