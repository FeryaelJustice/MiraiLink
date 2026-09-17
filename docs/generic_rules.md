# Reglas Generales de Ingenieria y Calidad de Codigo (MiraiLink)

Este documento establece los principios de ingenieria, arquitectura y calidad que deben regir cualquier cambio o nueva funcionalidad en MiraiLink bajo la metodologia SDMD (Spec-Driven Mobile Development).

- - -

## 1. Principios de Diseno y Clean Architecture

- **Separacion Estricta en Capas**:
  - **Data Layer (`data`)**: Responsable de la obtencion y persistencia de datos (fuentes remotas Retrofit / Socket.IO, persistencia local Room Database y preferencias seguras EncryptedDataStore). Implementa los contratos de repositorio definidos en dominio.
  - **Domain Layer (`domain`)**: Logica de negocio pura, agnostica de Android y de frameworks de UI. Contiene modelos inmutables, contratos de repositorio (`domain/repository`) y Casos de Uso (`domain/usecase`) individuales con operador `invoke()`.
  - **Presentation Layer (`ui`)**: Interfaz declarativa en Jetpack Compose con Material 3. La comunicacion se realiza exclusivamente a traves de ViewModels y flujos inmutables de estado (`StateFlow<UiState>`).
  - **Dependency Injection (`di/koin`)**: Modulos desacoplados en Koin. Las dependencias siempre apuntan hacia adentro; la capa de dominio nunca depende de capas externas.

- **Flujo de Datos Unidireccional (UDF)**:
  - La UI emite eventos e intenciones del usuario hacia el ViewModel.
  - El ViewModel procesa la logica a traves de Casos de Uso y expone un unico flujo inmutable de estado (`StateFlow<UiState>`) hacia la UI.
  - Las pantallas de Compose observan el estado reactivamente sin almacenar logica de negocio.

- **Inmutabilidad de Modelos y Estados**:
  - Todos los modelos de dominio y estados de UI deben definirse como clases inmutables (`data class` con atributos `val`).
  - Cualquier transformacion o actualizacion de estado debe realizarse mediante funciones puras o llamadas a `.copy()`.

- **Atomic Design en Componentes de UI**:
  - **Atoms (`ui/components/atoms`)**: Elementos visuales basicos e indivisibles (botones, campos de texto, chips, checkboxes).
  - **Molecules (`ui/components/molecules`)**: Composiciones simples de atomos (selectores de genero, campos de fecha, barra de busqueda).
  - **Organisms (`ui/components/organisms`)**: Componentes de interfaz complejos y autosuficientes (tarjeta de perfil de swipe `UserCard`, lista de chats `ChatList`, barras de navegacion).
  - **Screens (`ui/screens`)**: Pantallas completas orquestadas por un ViewModel.

- - -

## 2. Manejo de Errores Tipado y Resiliencia

- **Resultados Tipados (`MiraiLinkResult`)**:
  - Queda prohibido silenciar o ignorar excepciones con bloques `try-catch` vacios.
  - Toda operacion susceptible de fallo debe retornar un resultado tipado mediante la jerarquia sellada `MiraiLinkResult<T>` (`Success` vs `Error`).
  - Distinguir categoricamente entre errores de red (`Network`), errores locales de persistencia (`Local`), errores de sesion/autenticacion (`Auth`), errores de validacion (`Validation`) y errores no controlados (`Unknown`).

- **Mensajes de Usuario Amigables y Localizados**:
  - Los errores tecnicos o volcados de traza (`stackTrace`) nunca deben mostrarse al usuario final.
  - Los errores deben mapearse a cadenas localizadas en recursos nativos (`strings.xml`) con acciones claras de recuperacion (ej. boton de reintento, solicitud de re-autenticacion).

- - -

## 3. Estrategia de Testing y Verificacion

- **Pruebas Unitarias Obligatorias**:
  - Cada nuevo Caso de Uso debe incluir pruebas unitarias con JUnit 4 y MockK que cubran el camino feliz y todos los casos de borde.
  - Cada ViewModel debe verificarse utilizando Turbine para comprobar las transiciones y emisiones de `StateFlow`.

- **Pruebas de Persistencia e Integracion**:
  - Cualquier cambio en entidades o DAOs de Room debe respaldarse con pruebas sobre la base de datos en memoria (`inMemoryDatabaseBuilder`).

- **Cero Regresiones**:
  - Todo cambio debe compilar limpiamente con `./gradlew assembleDebug` y superar `./gradlew testDebugUnitTest` antes de darse por completado.
