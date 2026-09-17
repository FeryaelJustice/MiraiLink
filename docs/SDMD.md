# Metodologia SDMD: Spec-Driven Mobile Development en MiraiLink

Este documento describe la adopcion formal del estandar **SDMD (Spec-Driven Mobile Development)** en MiraiLink, adaptando los principios de Spec-Driven Development al ecosistema Android y a las particularidades de esta aplicacion (Clean Architecture, Jetpack Compose, Koin, Room Database offline y Socket.IO).

- - -

## 1. Resumen Ejecutivo y Filosofia de Diseno

### 1.1. El Problema: Indeterminismo y el Sesgo del Happy Path
Los Modelos de Lenguaje Grande (LLMs) y asistentes de codificacion presentan tres riesgos criticos en desarrollo movil cuando se les solicita codigo directamente:
1. **Sesgo del Happy Path**: Suponen conectividad ininterrumpida, respuestas instantaneas del servidor y memoria ilimitada, descuidando estados vacios, timeouts y perdida de red.
2. **Desconexion del Ciclo de Vida Movil**: Ignoran el ciclo de vida de Android (destruccion de procesos por escasez de memoria mediante Low Memory Killer, rotacion de pantalla y restauracion de estado).
3. **Alucinacion de Dependencias**: Proponen metodos obsoletos o librerias incompatibles con el Gradle Version Catalog (`libs.versions.toml`), KSP o la version configurada de Kotlin.

### 1.2. La Solucion: El Arnes de Seguridad (Safety Harness)
SDMD establece un protocolo estricto: **el codigo de produccion nunca se genera directamente a partir de una idea**.

Entre la intencion inicial y la primera linea de codigo ejecutable se despliega un arnes de seguridad documental:
- Se inspecciona el repositorio antes de asumir (`architecture.md`, `codebase-map.md`, `libs.versions.toml`).
- Se encapsulan las decisiones funcionales en `spec.md`.
- Se auditan las limitaciones moviles con `mobile_guidelines.md`.
- Se disena la arquitectura tecnica con dependencias reales en `plan.md`.
- Se desglosa en tareas atomicas y testeables en `tasks.md`.

- - -

## 2. Enfoque Adoptado: Spec-Anchor

MiraiLink adopta el modelo **Spec-Anchor** para la gestion de especificaciones:
- Las especificaciones se redactan, aprueban y versionan en Git dentro de `docs/features/<nombre-feature>/`.
- Conveven de forma permanente con el codigo fuente.
- Garantizan memoria historica para futuros desarrolladores y agentes de IA, evitando regresiones arquitectonicas.

- - -

## 3. Diagrama de Flujo Mermaid del Ciclo SDMD

```mermaid
flowchart TD
    subgraph F0["0. Arnes Base del Repositorio"]
        AG["AGENTS.md / CLAUDE.md<br/>(Contexto tecnico y comandos)"]
        GR["docs/generic_rules.md<br/>(Reglas transversales de calidad)"]
        MG["docs/mobile_guidelines.md<br/>(Restricciones criticas de Android)"]
        ST["docs/spec_template.md"]
        PT["docs/plan_template.md"]
        GR --> AG
        MG --> AG
    end

    subgraph F1["1. Idea & Contextualizacion"]
        IDEA["Nueva funcionalidad o requerimiento"] --> PROMPT_INIT["Prompt Inicializador SDMD<br/>(Bloqueo estricto de codigo)"]
    end

    subgraph F2["2. Fase Spec (Borrador -> Aprobado)"]
        PROMPT_INIT --> DRAFT_SPEC["Creacion docs/features/NOMBRE/spec.md"]
        DRAFT_SPEC --> INSPECT["Inspeccion de codigo existente y mobile_guidelines"]
        INSPECT --> Q_ROUNDS["Rondas de preguntas (3 a 5 por turno)<br/>(Resolucion de marcas [PENDIENTE])"]
        Q_ROUNDS --> REVIEW_SPEC{"¿Quedan dudas pendientes?"}
        REVIEW_SPEC -- Si --> Q_ROUNDS
        REVIEW_SPEC -- No --> SPEC_OK["spec.md pasa a [APROBADO]"]
    end

    subgraph F3["3. Fase Plan Tecnico"]
        SPEC_OK --> DRAFT_PLAN["Creacion docs/features/NOMBRE/plan.md"]
        DRAFT_PLAN --> DEP_CHECK["Verificacion real de versiones en libs.versions.toml"]
        DEP_CHECK --> ARCH["Diseno de capas: Data, Domain, UI y Koin DI"]
        ARCH --> REVIEW_PLAN{"¿El plan respeta la arquitectura?"}
        REVIEW_PLAN -- No --> DEP_CHECK
        REVIEW_PLAN -- Si --> PLAN_OK["plan.md pasa a [APROBADO]"]
    end

    subgraph F4["4. Desglose de Tareas"]
        PLAN_OK --> TASKS["Generacion de docs/features/NOMBRE/tasks.md<br/>(Fases ordenadas con checkboxes)"]
    end

    subgraph F5["5. Implementacion Secuencial & Verificacion"]
        TASKS --> EXEC["Ejecucion Tarea por Tarea [x]"]
        EXEC --> TESTS["Ejecucion de pruebas y compilacion<br/>(./gradlew testDebugUnitTest)"]
        TESTS --> TEST_FAIL{"¿Falla compilacion o tests?"}
        TEST_FAIL -- Si --> EXEC
        TEST_FAIL -- No --> HAS_NEXT{"¿Quedan tareas pendientes?"}
        HAS_NEXT -- Si --> EXEC
        HAS_NEXT -- No --> DEVICE_VAL["Verificacion final en dispositivo / emulador"]
    end

    F0 -. Contexto .- F2
    F0 -. Contexto .- F3
```

- - -

## 4. Estructura de Documentos en el Repositorio

```text
MiraiLink/
├── AGENTS.md                      # Contexto global y comandos para asistentes IA
├── CLAUDE.md                      # Puntero de contexto para Claude
├── docs/
│   ├── generic_rules.md           # Reglas universales de codigo y Clean Architecture
│   ├── mobile_guidelines.md       # Restricciones criticas de Android (Lifecycle, Offline, UI)
│   ├── spec_template.md           # Plantilla canonica de especificacion funcional
│   ├── plan_template.md           # Plantilla canonica de plan tecnico
│   ├── PROMPTS.md                 # Prompts inicializadores canonicos para desarrolladores e IA
│   ├── SDMD.md                    # Esta guia metodologica
│   └── features/                  # Directorio persistente de features (Spec-Anchor)
│       └── <nombre-feature>/
│           ├── spec.md            # Especificacion aprobada
│           ├── plan.md            # Plan tecnico aprobado
│           └── tasks.md           # Checklist secuencial de tareas
```

- - -

## 5. Guia de Ejecucion Paso a Paso

1. **Paso 0 (Arnes Base)**: Mantener actualizadas las directrices en `docs/generic_rules.md` y `docs/mobile_guidelines.md`.
2. **Paso 1 (Contextualizacion)**: Al recibir un nuevo requerimiento, bloquear cualquier intento de generacion de codigo de produccion.
3. **Paso 2 (Especificacion)**: Copiar `docs/spec_template.md` a `docs/features/<feature>/spec.md`. Marcar dudas con `[PENDIENTE]` y ejecutar rondas de 3 a 5 preguntas concretas.
4. **Paso 3 (Plan Tecnico)**: Copiar `docs/plan_template.md` a `docs/features/<feature>/plan.md`. Comprobar versiones en `gradle/libs.versions.toml` y definir contratos de DAOs, repositorios con `Flow`, modelos y ViewModels.
5. **Paso 4 (Tareas)**: Crear `docs/features/<feature>/tasks.md` estructurado en fases con tests asociados.
6. **Paso 5 (Implementacion)**: Resolver una tarea a la vez. Compilar (`./gradlew assembleDebug`) y pasar tests unitarios (`./gradlew testDebugUnitTest`) antes de marcar `[x]`.
7. **Paso 6 (Verificacion)**: Probar en emulador o dispositivo fisico, validando rotacion, falta de conexion y ergonomia tactil.
