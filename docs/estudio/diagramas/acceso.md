# Acceso y token pendiente

[Índice](indice.md) | [Guía maestra](../../guia-maestra.md)

Tipo: UML de secuencia. Revisión: 2026-10-01. Fuentes: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datastore/SessionManager.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/datastore/SessionManager.kt).

```mermaid
sequenceDiagram
    actor Persona
    participant VM as AuthViewModel
    participant API as UserApiService
    participant Servidor as Backend
    participant Sesion as SessionManager
    Persona->>VM: credenciales
    VM->>API: login(request)
    API->>Servidor: POST auth/login
    alt 2FA activado
        Servidor-->>VM: requires2FA y challengeToken
        Persona->>VM: código TOTP o recovery
        VM->>Servidor: último paso con challenge
        Servidor-->>VM: token final o error
    else Sin 2FA
        Servidor-->>VM: token, userId, isVerified
    end
    VM->>Sesion: caché temporal de token final
    alt Cuenta verificada
        VM->>Sesion: callback de sesión y persistencia
        VM-->>Persona: acceso principal según gates
    else Pendiente
        VM-->>Persona: completar verificación
        opt Cancelar
            VM->>Sesion: eliminar caché temporal
        end
    end
```

El challenge no se persiste como access token. El callback conecta la sesión con la raíz; el guardado de contraseña en Credential Manager es otro efecto cancelable. Ver el backend para TTL y procesamiento criptográfico.
