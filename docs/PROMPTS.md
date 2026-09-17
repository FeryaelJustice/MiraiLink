# Prompts Inicializadores y de Flujo SDMD (MiraiLink)

Este documento recopila las instrucciones y plantillas de prompts para iniciar y ejecutar el ciclo de vida SDMD en MiraiLink, tanto para desarrolladores como para asistentes de IA.

- - -

## 1. Traductor Automatico de Intencion (Prompt Interceptor)

Cuando el usuario exprese intenciones informales como:
- *"Quiero hacer ahora esta caracteristica: ..."*
- *"Quiero resolver esto: ..."*
- *"Quiero implementar ..."*
- *"Anade la funcion de ..."*

Cualquier asistente de IA configurado en el proyecto debe interceptar dicha peticion y traducirla internamente al **Prompt Inicializador SDMD**, bloqueando la generacion de codigo hasta contar con una especificacion aprobada.

- - -

## 2. Prompt Inicializador SDMD (Fase Spec)

```text
Actua como un arquitecto de software mobile senior siguiendo la metodologia SDMD.
Quiero implementar la funcionalidad: [DESCRIPCION_DE_LA_FUNCIONALIDAD].

Instrucciones estrictas:
1. Revisa AGENTS.md, docs/generic_rules.md y docs/mobile_guidelines.md.
2. Inspecciona el codigo existente del proyecto en app/ para entender la arquitectura actual (Compose, Koin, Room, Navigation 3).
3. Copia docs/spec_template.md en docs/features/<nombre_feature>/spec.md y rellena un primer borrador con lo que puedas comprobar en el codigo.
4. No asumas decisiones no especificadas: marca cualquier duda con la etiqueta [PENDIENTE].
5. Hazme un maximo de 3 a 5 preguntas concretas por turno para resolver las decisiones pendientes, prestando atencion a:
   - Modo Online (API/WebSockets) vs Modo Offline Demo (Room Database local).
   - Comportamiento sin conexion y reintentos.
   - Ciclo de vida de Android, muerte de procesos y SavedStateHandle.
   - Ergonomia tactil (48x48 dp) y padding de teclado virtual (imePadding).
6. ESTA ESTRICTAMENTE PROHIBIDO generar codigo de produccion o planes tecnicos en esta fase.
```

- - -

## 3. Prompt para el Plan Tecnico de Arquitectura (Fase Plan)

```text
La especificacion en docs/features/<nombre_feature>/spec.md ha sido [APROBADO].
Ahora genera el plan tecnico en docs/features/<nombre_feature>/plan.md copiando docs/plan_template.md.

Instrucciones estrictas:
1. Inspecciona gradle/libs.versions.toml y app/build.gradle.kts para verificar las dependencias reales. No inventes librerias ni versiones.
2. Define las entidades, DAOs de Room (si aplica al sandbox offline), contratos de repositorio reactivos con Flow, Casos de Uso y ViewModels.
3. Especifica los modulos Koin afectados en di/koin/.
4. Disena la estrategia de testing (pruebas unitarias con MockK/Turbine y pruebas instrumentadas de Room).
5. NO generes codigo de produccion todavia. Espera mi aprobacion del plan.
```

- - -

## 4. Prompt para el Desglose de Tareas (Fase Tasks)

```text
El plan tecnico en docs/features/<nombre_feature>/plan.md ha sido aprobado.
Genera ahora docs/features/<nombre_feature>/tasks.md desglosando el plan en fases secuenciales con checkboxes atomicos.
Incluye la verificacion de compilacion (./gradlew assembleDebug) y ejecucion de tests unitarios (./gradlew testDebugUnitTest) en cada fase.
```

- - -

## 5. Prompt para la Implementacion Secuencial (Fase Codigo)

```text
Implementa unicamente la siguiente tarea pendiente en docs/features/<nombre_feature>/tasks.md.
Una vez implementada:
1. Ejecuta la compilacion y los tests asociados.
2. No avances a la siguiente tarea hasta que la actual compile y pase los tests limpiamente.
3. Marca la tarea con [x] en tasks.md cuando este verificada.
```
