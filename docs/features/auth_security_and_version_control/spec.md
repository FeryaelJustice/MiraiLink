# [APROBADO] Especificacion Funcional: Sistema de Verificacion de Correo, Recuperacion de Contrasena, Autenticacion 2FA y Control de Versiones

- **Fecha**: 2026-09-27
- **Estado**: [APROBADO]
- **Autor / Responsable**: Antigravity & FeryaelJustice
- **Modulos Afectados**: `:app` (`com.feryaeljustice.mirailink`), `MiraiLink-Backend` (Express 5 + PostgreSQL)

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**:
  - **Verificacion de cuenta**: El registro crea cuentas sin exigir verificacion real previa al uso normal, el servicio de correo falla si no hay SMTP configurado provocando bloqueos, y no existe un flujo robusto y bloqueante ni soporte para deep links / futura web.
  - **Recuperacion de contrasena**: El flujo actual en UI no valida doble campo de contrasena ("nueva contrasena" y "repetir contrasena"), y no ofrece soporte comodo para resolver mediante enlace directo o codigo.
  - **Doble Factor de Autenticacion (2FA / TOTP)**: La implementacion previa presenta discrepancias criticas de contrato entre backend y Android (deserializacion de JSON con nombres de campos desiguales, falta del secret en texto plano para copia manual con pulsacion larga, flujo de login que no intercepta adecuadamente con challenge token y codigos de respaldo descartables de un solo uso). Al desactivar 2FA, se deben purgar/invalidar los codigos antiguos.
  - **Control de Versiones de la Aplicacion**: La comprobacion de version actual en `SplashScreenViewModel` continua la navegacion normal en paralelo e ignora el bloqueo forzado (`mustUpdate`), permitiendo que el usuario cierre el dialogo o pase a la aplicacion principal. Ademas, el dialogo no bloquea eventos de retroceso o pulsaciones fuera del contenedor en actualizaciones criticas.
  - **Resiliencia de Backend sin SMTP**: En entornos de desarrollo o pruebas sin servidor de correo activo, los envios deben simularse limpiamente en logs de consola sin arrojar excepciones 500 no controladas.
- **Objetivo**:
  - Implementar un sistema unificado y desacoplado de correo en backend con simulador visual por logs para desarrollo y soporte SMTP configurable.
  - Crear un flujo integral de verificacion de cuenta (codigo de 6 digitos y hash para deep link / web) con pantalla intermedia bloqueante (`VerificationScreen`), que al completarse inicie sesion de forma directa.
  - Implementar el restablecimiento seguro de contrasena con doble confirmacion de clave y retorno a login sin inicio automatico.
  - Perfeccionar el flujo 2FA en Ajustes (generacion de QR, frase secreta copiable con pulsacion prolongada, activacion con codigo TOTP, visualizacion clara de codigos de recuperacion de uso unico, invalidacion al desactivar con confirmacion y pantalla bloqueante en login que acepte TOTP o codigo de recuperacion con opcion de cerrar sesion).
  - Robustecer el control de versiones en 3 estados estrictos: sin actualizacion, actualizacion sugerida (descartable) y actualizacion obligatoria (estrictamente bloqueante, no descartable).
  - Coordinar la creacion de ramas dedicadas en Git para Android y Backend, junto con la comprobacion automatica de migraciones SQL al inicio del backend.

- - -

## 2. Situacion Actual

- **Backend**:
  - `src/utils/mailer.js`: Contiene un fallback basico si faltan variables, pero `sendVerificationEmail` solo cubre el asunto de verificacion, no restablecimiento. Si ocurre un fallo en transporte SMTP, puede interrumpir la llamada.
  - `src/controllers/auth.controller.js`:
    - `setup2FA` devuelve `{ otpauthUrl, recoveryCodes }`, omitiendo la clave secreta en texto base32 (`base32`), lo que impide la copia manual.
    - `login` devuelve `{ requires2FA: true, challengeToken, expiresIn }` cuando 2FA esta activo, pero `LoginResponse` en Android solo espera `{ token: String }`, provocando fallos de deserializacion.
    - `loginVerify2FALastStep` valida `challengeToken` y `code`, pero Android enviaba `userId` y `code`.
  - Migraciones de base de datos: Se ejecutan manualmente via `npm run db:migrate`. El servidor `src/server.js` no verifica migraciones al arrancar.
- **Android**:
  - `SplashScreenViewModel.kt`: Lanza la comprobacion de version pero no suspende la navegacion; lanza `onboardingDeferred` y `autologinDeferred` en paralelo y sobreescribe el estado de la UI con `SplashUiState.Navigate`, descartando el dialogo de actualizacion.
  - `MiraiLinkDialog.kt`: No configura `DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)` para modales criticos.
  - `VerificationDialog.kt` y `VerificationScreen.kt`: Solo existe como dialogo flotante en `AuthScreen.kt`; no hay pantalla intermedia formal en el grafo de navegacion ni gestion de deep links.
  - `RecoverPasswordScreen.kt`: Solo dispone de un campo de contrasena nueva, sin confirmacion redundante para evitar errores tipograficos.
  - `ConfigureTwoFactorScreen.kt` y `TwoFactorSetupDialog.kt`: Despliega codigos de respaldo antes de verificar el codigo; falta la accion de copiar con pulsacion prolongada la clave en texto y el aviso de respaldo tras la confirmacion.

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)

1. **Ramas de Trabajo Git y Migraciones**:
   - Creacion de rama `feature/auth-security-and-version-control` en `MiraiLink` y `MiraiLink-Backend`.
   - Comprobacion/ejecucion automatica de migraciones pendientes en el arranque del servidor backend.
2. **Servicio de Correos Desacoplado y Resiliente (Backend)**:
   - Modulo unificado de envio de correos con modo simulador en consola cuando falte configuracion SMTP o cuando falle la conexion.
   - Plantillas claras para: Verificacion de Cuenta y Restablecimiento de Contrasena.
   - Logs estructurados con el codigo y el enlace directo generado para pruebas locales rapidas.
3. **Flujo de Verificacion de Cuenta (Backend y Android)**:
   - Registro con `is_verified = FALSE` por defecto.
   - Pantalla intermedia dedicada bloqueante (`VerificationScreen`) en el grafo de navegacion que intercepta a usuarios no verificados.
   - Soporte para codigo de 6 digitos y para hash/token directo mediante deep link (`mirailink://verify?token=...` / enlace web futuro).
   - Accion de reenvio de codigo con control de tiempo/rate-limit.
   - Inicio de sesion automatico tras verificacion exitosa.
4. **Flujo de Recuperacion de Contrasena (Backend y Android)**:
   - Solicitud con correo electronico.
   - Pantalla de restablecimiento con codigo/token (o enlace directo con autofill) y doble campo obligatorio: "Nueva contrasena" y "Confirmar contrasena" con validacion de coincidencia en tiempo real.
   - Al completar con exito: mostrar mensaje amigable de exito ("Su contrasena se ha restablecido correctamente") y redirigir al Login (sin inicio de sesion automatico).
5. **Flujo Completo de Doble Factor (2FA / TOTP) (Backend y Android)**:
   - Pantalla de configuracion en Ajustes:
     - Generacion de QR en backend/Android y presentacion de la clave secreta manual (`base32`) con accion de copia al mantener pulsado.
     - Introduccion del codigo de 6 digitos para activar 2FA.
     - Generacion y despliegue modal de 8 codigos de recuperacion con advertencia enfatizada de seguridad y boton de copiar todos los codigos.
     - Boton de desactivar 2FA cuando este activo, con dialogo de confirmacion simple ("¿Estas seguro de que deseas desactivar el doble factor?") sin requerir codigo extra.
     - Al desactivar 2FA, purgar e invalidar permanentemente todos los codigos de recuperacion asociados.
   - En Inicio de Sesion:
     - Deteccion de 2FA activo: respuesta con `challengeToken`.
     - Pantalla/modal bloqueante en Android pidiendo codigo TOTP de aplicacion o codigo de respaldo.
     - Cada codigo de recuperacion utilizado queda invalidado permanentemente (`used = TRUE`).
     - Boton accesible de "Cerrar sesion" o "Cancelar" para regresar a la pantalla de Login limpia sin inconsistencias de estado.
6. **Sistema de Control de Versiones (Backend y Android)**:
   - Evaluacion estricta de 3 estados:
     - **Estado 1 (Actualizado)**: Version actual >= `latestVersionCode`. Sin dialogos.
     - **Estado 2 (Actualizacion flexible)**: `minVersionCode` <= Version actual < `latestVersionCode`. Dialogo informativo descartable; permite continuar usando la aplicacion.
     - **Estado 3 (Actualizacion critica obligatoria)**: Version actual < `minVersionCode`. Dialogo estrictamente bloqueante:
       - No descartable por toque exterior ni boton atras de Android (`DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)`).
       - Detiene la navegacion en Splash; no permite transicionar a Auth o Home.
       - Accion unica: redirigir a Google Play Store / URL de actualizacion.

### 3.2. Fuera del Alcance (Out of Scope)

- Integracion con proveedores externos de SMS para 2FA (se mantiene exclusivamente TOTP estandar RFC 6238 y codigos de respaldo).
- Desarrollo del frontend web completo (se garantiza la compatibilidad de rutas, endpoints y tokens para consumo web futuro).

- - -

## 4. Casuisticas y Comportamiento Mobile

- **Modo Online vs Modo Offline Demo**:
  - En Modo Demo, el usuario de prueba cuenta con estado verificado y 2FA simulable localmente sin conexion de red.
  - La comprobacion de versiones omite el bloqueo si no hay red o si el backend no esta disponible, permitiendo continuar en modo offline/contingencia.
- **Ciclo de Vida y Recuperacion de Estado**:
  - `TwoFactorChallenge` y codigos ingresados se preservan ante rotaciones de pantalla mediante `rememberSaveable` o `SavedStateHandle`.
  - La pantalla bloqueante de actualizacion obligatoria persiste intacta ante cambios de configuracion y recreacion de la actividad.
- **Manejo del Teclado y Ergonomia**:
  - `imePadding()` aplicado en todas las pantallas y dialogos para evitar que el teclado oculte campos de codigo o contrasena.
  - Objetivos tactiles de al menos 48 x 48 dp en botones de copia, envio y cierre.

- - -

## 5. Criterios de Aceptacion (Formato Given - When - Then)

### Criterio 1: Verificacion de cuenta obligatoria tras registro
- **Dado que**: Un usuario se registra con un nuevo correo electronico.
- **Cuando**: Se completa la llamada de registro.
- **Entonces**: La cuenta se crea con `is_verified = false`, el backend imprime en logs el codigo/enlace simulado sin fallar, y Android presenta la pantalla intermedia dedicada `VerificationScreen` impidiendo acceder a la app hasta validar el codigo o enlace. Al validar, inicia sesion directamente.

### Criterio 2: Restablecimiento de contrasena con doble comprobacion
- **Dado que**: Un usuario solicita recuperar su contrasena en `RecoverPasswordScreen`.
- **Cuando**: Recibe el codigo/enlace e introduce la nueva contrasena y la confirmacion.
- **Entonces**: Si no coinciden, se muestra error de validacion local. Si coinciden y el codigo es valido, se actualiza la contrasena en base de datos, se muestra mensaje de confirmacion y se redirige a la pantalla de inicio de sesion (sin iniciar sesion directamente).

### Criterio 3: Activacion y uso de Doble Factor con codigos de recuperacion
- **Dado que**: El usuario accede a Ajustes -> Doble Factor de Autenticacion.
- **Cuando**: Escanea el QR o copia la clave secreta por pulsacion larga, e introduce el codigo TOTP valido.
- **Entonces**: Se activa 2FA, se muestran 8 codigos de respaldo de un solo uso con advertencia de guardado, y en el proximo login se requiere TOTP o codigo de recuperacion para obtener el token de acceso.

### Criterio 4: Desactivacion de Doble Factor
- **Dado que**: El usuario tiene 2FA habilitado en su cuenta.
- **Cuando**: Pulsa desactivar 2FA y confirma en el dialogo de confirmacion simple.
- **Entonces**: Se desactiva 2FA, se eliminan todos los codigos de respaldo asociados y el boton de Ajustes pasa a permitir nueva configuracion.

### Criterio 5: Actualizacion de version critica bloqueante
- **Dado que**: La version del cliente es inferior a `min_supported_version_code` del backend.
- **Cuando**: La aplicacion inicia en `SplashScreen`.
- **Entonces**: Se muestra el dialogo de actualizacion obligatoria, se bloquea la navegacion a cualquier otra pantalla, se inhabilitan toques exteriores y boton atras, permitiendo unicamente pulsar en "Actualizar ahora" hacia la Play Store.

- - -

## 6. Resolucion de Decisiones Acordadas

- [x] **Decision 1 (UX Verificacion)**: Pantalla intermedia dedicada (`VerificationScreen`) en el grafo de navegacion con boton de reenvio de codigo y opcion clara de cerrar sesion para volver a login.
- [x] **Decision 2 (Deep Links)**: Soporte completo para deep links `mirailink://verify?token=...` y `mirailink://reset-password?token=...` con autollenado de token y preparado para URLs web directas.
- [x] **Decision 3 (Seguridad al Desactivar 2FA)**: Dialogo de confirmacion modal simple ("¿Estas seguro de desactivar el doble factor?") sin requerir codigo extra, aprovechando la sesion autenticada del usuario, purgando e invalidando todos los codigos de respaldo en base de datos.
- [x] **Decision 4 (Migraciones en Arranque)**: Ejecucion automatica y transparente de la comprobacion y aplicacion de migraciones SQL pendientes en `src/server.js` al iniciar el backend.
