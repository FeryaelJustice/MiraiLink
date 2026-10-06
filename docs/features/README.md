# Directorio de Funcionalidades (Spec-Anchor)

Este directorio alberga las especificaciones funcionales, planes de arquitectura y listas de tareas para cada funcionalidad desarrollada en MiraiLink bajo el enfoque **Spec-Anchor** de la metodología SDMD (Spec-Driven Mobile Development).

- - -

## ¿Por que Spec-Anchor?

A diferencia del enfoque efimero (*Spec-First*, donde los documentos se eliminan tras codificar) o rígido (*Spec-as-Source*, donde el código se deriva matematicamente de la spec), el enfoque **Spec-Anchor** mantiene las especificaciones vivas, versionadas y persistentes junto al código fuente.

Esto proporciona:
1. **Memoria Histórica**: Cualquier desarrollador o agente de IA puede comprender el porqué de cada decisión de diseño, los casos borde contemplados y las restricciones acordadas.
2. **Prevención de Regresiones**: Impide que refactorizaciones futuras rompan supuestos de negocio o criterios de aceptación críticos.
3. **Punto de Anclaje para Nuevas Iteraciones**: Facilita la ampliación de funcionalidades existentes consultando su especificación base.

- - -

## Estructura por Funcionalidad

Cada nueva funcionalidad debe crearse dentro de una subcarpeta dedicada con su nombre en kebab-case:

```text
docs/features/<nombre-funcionalidad>/
├── spec.md      # Especificación funcional (copiada de docs/spec_template.md)
├── plan.md      # Plan técnico de arquitectura (copiado de docs/plan_template.md)
└── tasks.md     # Desglose de tareas atómicas de implementación con checkboxes
```

- - -

## Estados de los Documentos

- **`spec.md`**:
  - `[BORRADOR]`: Documento en construcción. Se resuelven dudas mediante rondas de preguntas con el usuario. Queda estrictamente prohibido generar código de producción en este estado.
  - `[APROBADO]`: Todas las etiquetas `[PENDIENTE]` han sido resueltas y el usuario ha validado el alcance y los criterios de aceptación.

- **`plan.md`**:
  - `[BORRADOR]`: Diseño de contratos, entidades, DAOs, ViewModels y estrategia de testing basado en dependencias reales verificadas en `libs.versions.toml`.
  - `[APROBADO]`: Arquitectura revisada y aprobada por el desarrollador.

- **`tasks.md`**:
  - Checklist dividido en fases secuenciales. Cada tarea se implementa, compila, prueba y se marca con `[x]` antes de pasar a la siguiente.
