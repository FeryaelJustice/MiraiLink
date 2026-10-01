# Actividad del análisis de cámara

[Índice](indice.md) | [Guía maestra](../../guia-maestra.md)

Tipo: actividad representada como flowchart. Revisión: 2026-10-01. Fuentes: [app/src/main/java/com/feryaeljustice/mirailink/data/studio/StudioPhotoAnalyzer.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/studio/StudioPhotoAnalyzer.kt).

```mermaid
flowchart TD
    Frame["ImageProxy recibido"] --> Ocupado{"isProcessing"}
    Ocupado -->|sí| Descartar["Cerrar frame"]
    Ocupado -->|no| Disponible{"mediaImage existe"}
    Disponible -->|no| Descartar
    Disponible -->|sí| Luminancia["Muestrear plano Y"]
    Luminancia --> Deteccion["ML Kit detecta rostro"]
    Deteccion -->|éxito| Resultado["Callback luminancia y rostro"]
    Deteccion -->|error| Nulo["Callback luminancia y null"]
    Resultado --> Fin["finally: cerrar proxy y liberar guarda"]
    Nulo --> Fin
```

No representa identificación de una persona. La guarda reduce procesamiento simultáneo, y finally libera el frame del camino asíncrono. Permisos/captura ocurren antes de este analizador.
