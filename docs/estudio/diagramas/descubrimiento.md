# Búsqueda, feed y acción de like

[Índice](indice.md) | [Recorrido de descubrimiento](../flujos/descubrimiento.md).

Tipo: secuencia. Fuentes: [SearchPreferencesRepositoryImpl.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt), [SwipeApiService.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt) y [MatchApiService.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/MatchApiService.kt). Revisión: 2026-10-01.

```mermaid
sequenceDiagram
    actor Persona
    participant UI as Preferencias y feed
    participant Repo as Repositorios
    participant API as Backend
    participant Local as DataStore
    Persona->>UI: guardar filtros
    UI->>Repo: saveSearchPreferences
    Repo->>API: guardar preferencias
    alt Fallo remoto
        API-->>Repo: error
        Repo-->>UI: error sin confirmar guardado local
    else Éxito remoto
        API-->>Repo: preferencias guardadas
        Repo->>Local: persistir selección
        Repo-->>UI: resultado local
    end
    UI->>API: feed con limit y offset
    API-->>UI: candidatos o lista vacía
    Persona->>UI: like
    UI->>API: usuario objetivo
    API-->>UI: like con resultado de match o error
    opt Match
        UI-->>Persona: presentar resultado
    end
```

El backend decide ranking, exclusiones y límites. La escritura remota y local no comparten transacción. Un match no implica creación inmediata de chat; la creación pertenece al envío de mensajes en el servidor. Los repositorios demo siguen otro recorrido local.
