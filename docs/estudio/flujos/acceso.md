# Acceso, registro, verificación y recuperación

[Guía maestra](../../guia-maestra.md) | [Mapa de funcionalidades](../funcionalidades.md)

## Responsabilidad y recorrido

AuthViewModel valida campos, llama casos de uso y distingue sesión final, 2FA y verificación pendiente. UserApiService ofrece register/login/autologin/logout y solicitudes/confirmaciones de códigos. Autologin valida sesión remota; DEMO_TOKEN tiene otro camino.

## Alternativas y efectos

Credenciales incorrectas son error; requires2FA abre el último paso con challenge. La cuenta sin verificar conserva acceso pendiente hasta confirmación o cancelación. CredentialHelper puede no devolver credenciales si el proveedor/usuario cancela. Logout debe coordinar invalidación remota y limpieza local; el fallo de red no garantiza revocación servidor. Una solicitud de email neutral no prueba entrega.

## Responsabilidad externa

El servidor calcula hashes, TTL y JWT. Seguir [su flujo](https://github.com/FeryaelJustice/MiraiLink-Backend/blob/codex/documentacion-integral/docs/estudio/flujos/acceso.md), no inferir SQL desde el DTO.

## Fuentes para estudiar

- [AuthViewModel.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt)
- [UserApiService.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt)
- [CredentialHelper.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/domain/util/CredentialHelper.kt)

Revisión: 2026-10-01, por inspección del código. Consultar [verificación](../desarrollo-y-verificacion.md) para resultados ejecutados.
