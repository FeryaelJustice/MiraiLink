# Contraseñas, acceso y sesión desde Android

[Guía maestra](../guia-maestra.md) | [Cobertura](cobertura.md)

Revisión de fuentes: 2026-10-01. Las observaciones estáticas no certifican el servidor desplegado ni el comportamiento en dispositivo.


## Qué sabe Android de una contraseña

La contraseña se captura y valida en [AuthViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt), se envía en el request del servicio [UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt) y puede guardarse por solicitud al proveedor de credenciales del sistema mediante [CredentialHelper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/util/CredentialHelper.kt). Esa solicitud puede cancelarse o fallar sin completar el guardado. El cliente no calcula ni almacena el hash PostgreSQL.

La sesión persistida contiene token, userId y verificación, no la contraseña. [SessionManager.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datastore/SessionManager.kt) mantiene cachés síncronas para [AuthInterceptor.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/interceptor/AuthInterceptor.kt). El caché temporal facilita peticiones intermedias antes de persistir una sesión completa; no es prueba de verificación de cuenta.

## Acceso, verificación y 2FA

El login distingue una sesión final de una respuesta con `requires2FA` y `challengeToken`. El token de challenge se envía al último paso 2FA, no se usa como Bearer de API. El ViewModel conserva estado pendiente mientras se verifica la cuenta; `completeAuth` comunica la sesión a la raíz y solicita guardado de credenciales. Cancelar verificación elimina el token temporal.

Registro y recuperación usan endpoints distintos. La solicitud de correo puede devolver una respuesta neutral: una respuesta HTTP correcta no acredita entrega SMTP ni existencia de la cuenta. No mostrar como hecho que el correo fue entregado si el servidor no aporta esa garantía.

La app usa `TwoFactorApiService` para setup/status/verify/disable y último paso. Base32, URI de configuración y códigos de recuperación son material sensible devuelto para configurar el autenticador; no pertenecen a logs ni a ejemplos públicos.

## Cifrado y transporte

[EncryptedJsonSerializer.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datastore/serializer/EncryptedJsonSerializer.kt) cifra JSON con AES/GCM y clave [KeystoreAesGcmProvider.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datastore/crypto/KeystoreAesGcmProvider.kt). El IV va delante del ciphertext. Archivo vacío usa el valor predeterminado; payload corrupto o clave inválida puede fallar y no hay un corruption handler general que garantice restauración automática.

La seguridad de transporte depende de la URL y de [network_security_config.xml](../../app/src/main/res/xml/network_security_config.xml). El cliente API debug tiene logging BODY: documentar el riesgo de datos personales en trazas, no asumir que todo logging está saneado. El cliente de imágenes incorpora allowlist; no autentica hosts arbitrarios con el token API.

Para bcrypt, JWT, TOTP, revocación y base de datos, leer [autenticación del backend](https://github.com/FeryaelJustice/MiraiLink-Backend/blob/codex/documentacion-integral/docs/estudio/autenticacion-y-seguridad.md). Diagrama: [secuencia de acceso](diagramas/acceso.md).
