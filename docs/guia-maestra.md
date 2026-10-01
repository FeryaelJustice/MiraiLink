# Guía maestra de estudio: MiraiLink Android

Revisión inicial: 2026-10-01. Esta entrada permite estudiar el proyecto desde sus tecnologías, arquitectura y flujos hasta los casos de error. El código es la fuente principal; los requisitos históricos no acreditan comportamiento implementado.

> **Aviso de mantenimiento:** esta guía es una descripción revisable del código, no una garantía permanente ni una certificación de producción. Debe actualizarse cada vez que cambien funcionalidades, tecnologías, contratos, configuración, persistencia o despliegue. Las guías de clientes (Android, otras apps o web) y backend deben mantenerse conectadas. Para un tema cruzado, analizar siempre ambos extremos antes de documentar o dar un cambio por terminado.

## Cómo mantener conectados los proyectos

1. Localizar el tema en las dos guías maestras y asignar el propietario de cada detalle. El cliente explica lo que envía, recibe, guarda y presenta; el backend explica lo que valida, procesa, autoriza y almacena. Los futuros clientes siguen la misma separación.
2. Trazar el recorrido completo: acción del usuario, contrato cliente/servidor, request y response, validación, efectos, persistencia, errores y recuperación. Revisar DTO, nullabilidad, formatos de fecha, idioma, IDs, paginación, códigos de error y permisos de recursos en ambos extremos.
3. Actualizar juntos los documentos por tema, referencia API/OpenAPI, diagramas, comentarios y pruebas pertinentes. Si solo cambia un extremo, explicar por qué no requiere modificar el otro y cómo se conserva la compatibilidad.
4. Revisar enlaces y la matriz de cobertura. Registrar la revisión de cada extremo y la evidencia: inspección, test, ejecución de integración o dispositivo. Diferenciar una verificación pendiente de una garantía confirmada.
5. Al incorporar otra app o frontend, añadir su guía a la tabla de proyectos y a los temas cruzados. No duplicar detalles internos del servidor en clientes ni detalles de interfaz en el backend.
6. Antes de publicar, sustituir enlaces provisionales de rama por la referencia entregada y comprobar que el destino remoto existe. Mantener enlaces relativos dentro de cada repositorio.

La actualización forma parte del mismo cambio que altera el código. Si una guía contradice la implementación, corregirla o señalar expresamente que describe un objetivo futuro.

## Proyectos conectados

| Proyecto | Guía | Responsabilidad |
| --- | --- | --- |
| Este repositorio | Este documento | Cliente Android, interfaz, estado, contratos remotos y almacenamiento local |
| backend | [Guía del backend](https://github.com/FeryaelJustice/MiraiLink-Backend/blob/codex/documentacion-integral/docs/guia-maestra.md) | Procesamiento de contraseñas, PostgreSQL, autorización y servicios |

Los enlaces entre repositorios apuntan a la rama de revisión codex/documentacion-integral de [MiraiLink-Backend](https://github.com/FeryaelJustice/MiraiLink-Backend). Al integrar las PR, actualizar juntos estos enlaces para usar la rama principal o una referencia estable. Dentro de cada proyecto se conservan enlaces relativos. En local, el proyecto compañero se encuentra como directorio hermano.

## Índice por tema

- [Arquitectura](estudio/arquitectura.md)
- [Tecnologías](estudio/tecnologias.md)
- [Funcionalidades y recorridos](estudio/funcionalidades.md)
- [Autenticación y seguridad](estudio/autenticacion-y-seguridad.md)
- [Configuración y ausencia de valores](estudio/configuracion.md)
- [Integraciones](estudio/integraciones.md)
- [Errores y recuperación](estudio/ciclo-de-vida-y-errores.md)
- [Desarrollo y verificación](estudio/desarrollo-y-verificacion.md)
- [Cobertura y fuentes](estudio/cobertura.md)
- [Hallazgos y límites](estudio/hallazgos.md)
- [Persistencia y demo](estudio/persistencia-y-demo.md)
- [Diagramas: ubicación, tipo y significado](estudio/diagramas/indice.md)
- [Referencia navegable de código propio](estudio/referencia-codigo.md)
- [Comentarios revisados y conservación del código](estudio/comentarios-verificados.md)
- [Índice de lectores de configuración](estudio/lectores-configuracion.md)
- [Contexto técnico para asistentes](ai/README.md)

## Por dónde empezar

- Para entender el sistema: arquitectura, tecnologías, funcionalidades y diagramas de contexto/componentes.
- Para entender contraseñas: autenticación del cliente para captura/envío/sesión y autenticación del backend para bcrypt, códigos, JWT y TOTP. Seguir el enlace del proyecto responsable.
- Para diagnosticar una incidencia: localizar el flujo, leer contratos/configuración/errores y comprobar ambos extremos antes de atribuir la causa.
- Para preparar cambios o despliegues: configuración, desarrollo/verificación, hallazgos y operación del servidor. No utilizar comandos destructivos como comprobación documental.

## Documentación de esta entrega

- [Especificación aprobada](features/documentacion_integral/spec.md)
- [Plan aprobado](features/documentacion_integral/plan.md)
- [Checklist y evidencia](features/documentacion_integral/tasks.md)
