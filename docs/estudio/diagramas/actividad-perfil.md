# Actividad de edición y guardado del perfil

[Índice](indice.md) | [Recorrido de perfil](../flujos/perfil.md).

Tipo: actividad representada como flowchart. Fuente: [ProfileViewModel.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt) y [UserRemoteDataSource.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt). Revisión: 2026-10-01.

```mermaid
flowchart TD
    Perfil["Cargar perfil y catálogos"] --> Borrador["Editar borrador de campos, atributos e intereses"]
    Borrador --> Residencia{"¿Se cambia residencia?"}
    Residencia -->|sí| Sugerencia["Seleccionar explícitamente sugerencia e IDs canónicos"]
    Residencia -->|no| Guardar["Acción guardar"]
    Sugerencia --> Guardar
    Guardar --> Payload["Construir multipart y campos de contrato"]
    Payload --> Repo["Repositorio y datasource remoto o demo"]
    Repo --> Resultado{"Resultado"}
    Resultado -->|error| Mostrar["Mantener edición y presentar error"]
    Mostrar --> Borrador
    Resultado -->|éxito| Confirmar["Actualizar estado visible y comunicar éxito"]
```

Vista del recorrido de edición, sin afirmar atomicidad entre archivos y PostgreSQL. La confirmación de IDs de residencia y la carga individual de foto requieren sus propios handlers; ver el flujo y sus enlaces al servidor.
