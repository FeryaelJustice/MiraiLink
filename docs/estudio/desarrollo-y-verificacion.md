# Desarrollo, comprobaciones y evidencia

[Guía maestra](../guia-maestra.md) | [Cobertura](cobertura.md)

Revisión de fuentes: 2026-10-01. Las observaciones estáticas no certifican el servidor desplegado ni el comportamiento en dispositivo.


## Comandos pertinentes

`assembleDebug`, `testDebugUnitTest` y `lintDebug` validan fuentes/comentarios y build; las suites instrumentadas se revisan antes de ejecutar porque pueden escribir en el backend real. El CLI compartido `android` sirve para información y dispositivo cuando haga falta; una compilación no verifica gestos, permisos, IME o restauración visual.

Regenerar anexos: `node scripts/documentacion-inventario.mjs android`. El script limita lectura a fuentes versionadas y excluye media/secretos; no ejecuta SQL ni llama a proveedores.

## Evidencia de esta entrega

Los resultados finales se registran en [checklist](../features/documentacion_integral/tasks.md). Diagramas derivan de fuentes estáticas; renderizado se registrará por separado. No se ejecutan migraciones, seeds, resets ni operaciones de producción para redactar documentos.

Pruebas en mocks son evidencia del contrato probado, no validación de SMTP, Google Play, Firebase, base desplegada o dispositivo real. Un test omitido debe registrarse como omitido.

## Mantenimiento

Ante cambios de funciones, requests, configuración o esquema, regenerar referencias y después revisar manualmente contenido, diagramas y protocolos. Un generador no valida el significado de una decisión de negocio. Seguir las instrucciones cruzadas del maestro.

## Resultados ejecutados el 2026-10-01

| Comprobación | Resultado | Alcance |
| --- | --- | --- |
| assembleDebug | Correcto | Compilación y APK debug |
| testDebugUnitTest | 441 tests, 128 suites, sin fallos ni omitidos | JVM, mocks y Robolectric; no dispositivo |
| lintDebug | Fallo: 78 errores, 167 warnings | [IDs y ubicaciones](lint-hallazgos.md) |
| Conservación de fuentes | 38 archivos Kotlin, solo comentarios | Comparación léxica con HEAD |

La ejecución conjunta de los tres comandos termina con error por lint. Se repitieron assembleDebug y testDebugUnitTest después de los últimos comentarios. Kotzilla también emitió un diagnóstico de problemas de rendimiento/crashes históricos del servicio; no es evidencia de una reproducción local y no sustituye lint ni tests.

### Documentación y conservación del comportamiento

- Guías maestras conectadas con disclaimer y protocolo de mantenimiento conjunto.
- Inventarios de fuentes, lectores de configuración, flujos y referencias revisados estáticamente.
- Scripts de comprobación de enlaces locales ejecutados sin destinos rotos en el conjunto de estudio. Los enlaces entre repositorios utilizan la rama de revisión; al integrar las PR deben actualizarse juntos hacia una referencia estable.
- git diff --check correcto al cierre.
- Fuentes ejecutables modificadas comparadas contra HEAD sin cambios fuera de comentarios; ver [lista de comentarios](comentarios-verificados.md). Los scripts nuevos son herramientas de documentación y no forman parte del runtime de la app/servidor.

### Diagramas y límites

Diagramas Mermaid y PlantUML contrastados con fuentes. No se verificó su renderizado con un motor Mermaid/PlantUML en este entorno; se entregan fuentes editables y se registra esta limitación. No se han ejecutado pruebas de dispositivo, integración con proveedores o base desplegada. No se ha ejecutado seed, reset ni migración sobre datos reales.
