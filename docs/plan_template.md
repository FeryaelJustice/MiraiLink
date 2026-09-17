# [BORRADOR | APROBADO] Plan Tecnico de Arquitectura: [Nombre de la Funcionalidad]

- **Especificacion funcional asociada**: `docs/features/<nombre_feature>/spec.md`
- **Estado**: [BORRADOR | APROBADO]
- **Fecha**: YYYY-MM-DD
- **Modulo**: `:app` (`com.feryaeljustice.mirailink`)

- - -

## 1. Hechos Verificados en el Proyecto (Sin Alucinaciones)

Informacion verificada rigurosamente en `gradle/libs.versions.toml` y `app/build.gradle.kts`:

- **Lenguaje & JVM**: Kotlin `2.4.10` / Java 17
- **Compilador & Build Tool**: AGP `9.4.0`, Gradle `9.6.1`, KSP `2.4.10-2.0.2`
- **SDK Targets**: Min SDK `26`, Compile SDK `37`, Target SDK `37`
- **Librerias Verificadas en el Classpath**:
  - UI: Compose BOM `2026.08.00` con Material 3
  - Navegacion: Navigation 3 (`androidx.navigation3:navigation3-core:1.1.7`)
  - Inyeccion de Dependencias: Koin BOM `4.2.2` con Koin Annotations
  - Persistencia Local: Room Database `2.8.4` (KSP) y Encrypted DataStore `1.2.1`
  - Red & Sockets: Retrofit `3.0.0`, OkHttp `5.5.0`, Socket.IO Client `2.1.2`
  - Serializacion: Kotlinx Serialization `1.11.0`

> **Regla estricta**: Prohibido inventar metodos inexistentes o sugerir librerias que no existan en el catalogo de versiones sin aprobacion previa.

- - -

## 2. Impacto Arquitectonico y Contratos por Capas

### 2.1. Capa de Datos (`data/`)
- **Entidades / DAOs**: [Nuevas tablas o modificaciones en `data/local/demo/` si aplica al Modo Offline]
- **DataSources**: [Contratos en `data/datasource/` o `data/remote/` para API / WebSockets]
- **Mapeadores**: [Nuevos mappers DTO <-> Entity <-> Domain Model en `data/mappers/`]
- **Contratos de Repositorio**: [Implementaciones en `data/repository/` exponiendo flujos reactivos `Flow<T>` o `MiraiLinkResult<T>`]

### 2.2. Capa de Dominio (`domain/`)
- **Modelos de Negocio**: [Modelos inmutables en `domain/model/` libres de frameworks de Android]
- **Interfaces de Repositorio**: [Contratos en `domain/repository/`]
- **Casos de Uso (`usecase/`)**: [Nuevos casos de uso con operador `invoke()` y tipado seguro]

### 2.3. Capa de Presentacion (`ui/`)
- **ViewModel & Maquina de Estados**: [ViewModel en `ui/screens/` exponiendo `StateFlow<UiState>` y canal de eventos/efectos]
- **Componentes Atomic Design**:
  - Atoms / Molecules / Organisms reutilizables en `ui/components/`
  - Pantalla Composable en `ui/screens/`
- **Navegacion**: [Claves serializables de Navigation 3 en `ui/navigation/`]

### 2.4. Inyeccion de Dependencias (`di/koin/`)
- [Nuevas definiciones o dependencias agregadas a los modulos Koin en `di/koin/`]

- - -

## 3. Estrategia de Testing

- **Pruebas Unitarias (`app/src/test/`)**:
  - Casos de uso con MockK y assertions JUnit 4.
  - ViewModels con TestDispatcher y Turbine para validar emisiones de estado.
  - Mappers de transformacion de datos.
- **Pruebas de Base de Datos e Instrumentadas (`app/src/androidTest/`)**:
  - Verificacion de DAOs en Room Database en memoria.
- **Verificacion Visual (Screenshot Testing)**:
  - Validacion de Compose Previews mediante `./gradlew validateDebugScreenshotTest` si se introducen nuevos componentes visuales clave.

- - -

## 4. Riesgos Tecnicos y Mitigaciones

- **Riesgo 1**: [ej. Bloqueo de hilo principal ante operaciones de I/O] -> **Mitigacion**: Uso de `Dispatchers.IO` en repositorios.
- **Riesgo 2**: [ej. Muerte de proceso y perdida de formulario] -> **Mitigacion**: Persistencia temporal en `SavedStateHandle`.
