# Prompts Inicializadores y de Flujo SDMD (MiraiLink)

Este documento recopila las instrucciones y plantillas de prompts para iniciar y ejecutar el ciclo de vida SDMD en MiraiLink, tanto para desarrolladores como para asistentes de IA.

- - -

## 1. Traductor Automático de Intención (Prompt Interceptor)

Cuando el usuario exprese intenciones informales como:
- *"Quiero hacer ahora esta característica: ..."*
- *"Quiero resolver esto: ..."*
- *"Quiero implementar ..."*
- *"Añade la función de ..."*

Cualquier asistente de IA configurado en el proyecto debe interceptar dicha petición y traducirla internamente al **Prompt Inicializador SDMD**, bloqueando la generación de código hasta contar con una especificación aprobada.

- - -

## 2. Prompt Inicializador SDMD (Fase Spec)

```text
Actúa como un arquitecto de software mobile senior siguiendo la metodología SDMD.
Quiero implementar la funcionalidad: [DESCRIPCION_DE_LA_FUNCIONALIDAD].

Instrucciones estrictas:
1. Revisa AGENTS.md, docs/generic_rules.md y docs/mobile_guidelines.md.
2. Inspecciona el código existente del proyecto en app/ para entender la arquitectura actual (Compose, Koin, Room, Navigation 3).
3. Copia docs/spec_template.md en docs/features/<nombre_feature>/spec.md y rellena un primer borrador con lo que puedas comprobar en el código.
4. No asumas decisiones no especificadas: marca cualquier duda con la etiqueta [PENDIENTE].
5. Hazme un máximo de 3 a 5 preguntas concretas por turno para resolver las decisiones pendientes, prestando atención a:
   - Modo Online (API/WebSockets) vs Modo Offline Demo (Room Database local).
   - Comportamiento sin conexión y reintentos.
   - Ciclo de vida de Android, muerte de procesos y SavedStateHandle.
   - Ergonomía táctil (48x48 dp) y padding de teclado virtual (imePadding).
6. ESTÁ ESTRICTAMENTE PROHIBIDO generar código de producción o planes técnicos en esta fase.
```

- - -

## 3. Prompt para el Plan Técnico de Arquitectura (Fase Plan)

```text
La especificación en docs/features/<nombre_feature>/spec.md ha sido [APROBADO].
Ahora genera el plan técnico en docs/features/<nombre_feature>/plan.md copiando docs/plan_template.md.

Instrucciones estrictas:
1. Inspecciona gradle/libs.versions.toml y app/build.gradle.kts para verificar las dependencias reales. No inventes librerías ni versiones.
2. Define las entidades, DAOs de Room (si aplica al sandbox offline), contratos de repositorio reactivos con Flow, Casos de Uso y ViewModels.
3. Específica los módulos Koin afectados en di/koin/.
4. Disena la estrategia de testing (pruebas unitarias con MockK/Turbine y pruebas instrumentadas de Room).
5. NO generes código de producción todavía. Espera mi aprobación del plan.
```

- - -

## 4. Prompt para el Desglose de Tareas (Fase Tasks)

```text
El plan técnico en docs/features/<nombre_feature>/plan.md ha sido aprobado.
Genera ahora docs/features/<nombre_feature>/tasks.md desglosando el plan en fases secuenciales con checkboxes atómicos.
Incluye la verificación de compilación (./gradlew assembleDebug) y ejecución de tests unitarios (./gradlew testDebugUnitTest) en cada fase.
```

- - -

## 5. Prompt para la Implementación Secuencial (Fase Código)

```text
Implementa únicamente la siguiente tarea pendiente en docs/features/<nombre_feature>/tasks.md.
Una vez implementada:
1. Ejecuta la compilación y los tests asociados.
2. No avances a la siguiente tarea hasta que la actual compile y pase los tests limpiamente.
3. Marca la tarea con [x] en tasks.md cuando este verificada.
```
