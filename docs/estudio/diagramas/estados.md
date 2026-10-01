# Estados de sesión y navegación

[Índice](indice.md) | [Guía maestra](../../guia-maestra.md)

Tipo: UML de estados, vista resumida. Revisión: 2026-10-01. Fuentes: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/AuthenticatedNavigationPolicy.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/AuthenticatedNavigationPolicy.kt).

```mermaid
stateDiagram-v2
    [*] --> Arranque
    Arranque --> Onboarding: pendiente
    Arranque --> Acceso: sin sesión válida
    Onboarding --> Acceso
    Acceso --> SegundoFactor: challenge
    SegundoFactor --> Acceso: cancelar o fallo
    SegundoFactor --> Verificacion: cuenta pendiente
    Acceso --> Verificacion: cuenta pendiente
    Acceso --> EvaluarFoto: sesión verificada
    Verificacion --> EvaluarFoto: confirmada
    EvaluarFoto --> FotoObligatoria: sin foto
    EvaluarFoto --> Principal: foto disponible
    FotoObligatoria --> Principal: foto confirmada
    Principal --> Acceso: logout
    Principal --> Principal: restaurar destino guardado
```

Resume gates de sesión. La actualización obligatoria puede bloquear acceso adicionalmente; onboarding/splash y eventos de URI deben contrastarse con NavWrapper. Restaurar navegación no asegura persistencia de cada formulario.
