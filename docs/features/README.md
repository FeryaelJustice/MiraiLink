# Directorio de Funcionalidades (Spec-Anchor)

Este directorio alberga las especificaciones funcionales, planes de arquitectura y listas de tareas para cada funcionalidad desarrollada en MiraiLink bajo el enfoque **Spec-Anchor** de la metodologia SDMD (Spec-Driven Mobile Development).

- - -

## ¿Por que Spec-Anchor?

A diferencia del enfoque efimero (*Spec-First*, donde los documentos se eliminan tras codificar) o rigido (*Spec-as-Source*, donde el codigo se deriva matematicamente de la spec), el enfoque **Spec-Anchor** mantiene las especificaciones vivas, versionadas y persistentes junto al codigo fuente.

Esto proporciona:
1. **Memoria Historica**: Cualquier desarrollador o agente de IA puede comprender el porqué de cada decision de diseno, los casos borde contemplados y las restricciones acordadas.
2. **Prevencion de Regresiones**: Impide que refactorizaciones futuras rompan supuestos de negocio o criterios de aceptacion criticos.
3. **Punto de Anclaje para Nuevas Iteraciones**: Facilita la ampliacion de funcionalidades existentes consultando su especificacion base.

- - -

## Estructura por Funcionalidad

Cada nueva funcionalidad debe crearse dentro de una subcarpeta dedicada con su nombre en kebab-case:

```text
docs/features/<nombre-funcionalidad>/
├── spec.md      # Especificacion funcional (copiada de docs/spec_template.md)
├── plan.md      # Plan tecnico de arquitectura (copiado de docs/plan_template.md)
└── tasks.md     # Desglose de tareas atomicas de implementacion con checkboxes
```

- - -

## Estados de los Documentos

- **`spec.md`**:
  - `[BORRADOR]`: Documento en construccion. Se resuelven dudas mediante rondas de preguntas con el usuario. Queda estrictamente prohibido generar codigo de produccion en este estado.
  - `[APROBADO]`: Todas las etiquetas `[PENDIENTE]` han sido resueltas y el usuario ha validado el alcance y los criterios de aceptacion.

- **`plan.md`**:
  - `[BORRADOR]`: Diseno de contratos, entidades, DAOs, ViewModels y estrategia de testing basado en dependencias reales verificadas en `libs.versions.toml`.
  - `[APROBADO]`: Arquitectura revisada y aprobada por el desarrollador.

- **`tasks.md`**:
  - Checklist dividido en fases secuenciales. Cada tarea se implementa, compila, prueba y se marca con `[x]` antes de pasar a la siguiente.
