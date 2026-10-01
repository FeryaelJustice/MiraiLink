# Polling y envío de chat

[Índice](indice.md) | [Guía maestra](../../guia-maestra.md)

Tipo: UML de secuencia. Revisión: 2026-10-01. Fuentes: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt).

```mermaid
sequenceDiagram
    participant UI as ChatScreen
    participant VM as ChatViewModel
    participant API as REST
    UI->>VM: iniciar chat
    loop cada tres segundos mientras job activo
        VM->>API: history(userId)
        API-->>VM: lista o error
        VM-->>UI: reemplazar lista o mostrar error
    end
    UI->>VM: enviar texto
    VM->>API: send(toUserId,text)
    alt Éxito
        API-->>VM: confirmación
        VM-->>UI: añadir mensaje local
    else Fallo
        API-->>VM: error
        VM-->>UI: error y acción de reintento
    end
    UI->>VM: parar o reset
    VM->>VM: cancelar pollingJob
```

Las llamadas a getMessages lanzan jobs hijos independientes y pueden solaparse si son lentas. El envío visible no es optimista antes del resultado REST ni usa una outbox persistente.
