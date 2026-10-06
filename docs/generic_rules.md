# Reglas Generales de Ingeniería y Calidad de Código (MiraiLink)

Este documento establece los principios de ingeniería, arquitectura y calidad que deben regir cualquier cambio o nueva funcionalidad en MiraiLink bajo la metodología SDMD (Spec-Driven Mobile Development).

- - -

## 1. Principios de Diseño y Clean Architecture

- **Separación Estricta en Capas**:
  - **Data Layer (`data`)**: Responsable de la obtención y persistencia de datos (fuentes remotas Retrofit / Socket.IO, persistencia local Room Database y preferencias seguras EncryptedDataStore). Implementa los contratos de repositorio definidos en dominio.
  - **Domain Layer (`domain`)**: Lógica de negocio pura, agnóstica de Android y de frameworks de UI. Contiene modelos inmutables, contratos de repositorio (`domain/repository`) y Casos de Uso (`domain/usecase`) individuales con operador `invoke()`.
  - **Presentation Layer (`ui`)**: Interfaz declarativa en Jetpack Compose con Material 3. La comunicación se realiza exclusivamente a través de ViewModels y flujos inmutables de estado (`StateFlow<UiState>`).
  - **Dependency Injection (`di/koin`)**: Módulos desacoplados en Koin. Las dependencias siempre apuntan hacia adentro; la capa de dominio nunca depende de capas externas.

- **Flujo de Datos Unidireccional (UDF)**:
  - La UI emite eventos e intenciones del usuario hacia el ViewModel.
  - El ViewModel procesa la lógica a través de Casos de Uso y expone un único flujo inmutable de estado (`StateFlow<UiState>`) hacia la UI.
  - Las pantallas de Compose observan el estado reactivamente sin almacenar lógica de negocio.

- **Inmutabilidad de Modelos y Estados**:
  - Todos los modelos de dominio y estados de UI deben definirse como clases inmutables (`data class` con atributos `val`).
  - Cualquier transformación o actualización de estado debe realizarse mediante funciones puras o llamadas a `.copy()`.

- **Atomic Design en Componentes de UI**:
  - **Atoms (`ui/components/atoms`)**: Elementos visuales básicos e indivisibles (botones, campos de texto, chips, checkboxes).
  - **Molecules (`ui/components/molecules`)**: Composiciones simples de átomos (selectores de género, campos de fecha, barra de búsqueda).
  - **Organisms (`ui/components/organisms`)**: Componentes de interfaz complejos y autosuficientes (tarjeta de perfil de swipe `UserCard`, lista de chats `ChatList`, barras de navegación).
  - **Screens (`ui/screens`)**: Pantallas completas orquestadas por un ViewModel.

- - -

## 2. Manejo de Errores Tipado y Resiliencia

- **Resultados Tipados (`MiraiLinkResult`)**:
  - Queda prohibido silenciar o ignorar excepciones con bloques `try-catch` vacíos.
  - Toda operación susceptible de fallo debe retornar un resultado tipado mediante la jerarquía sellada `MiraiLinkResult<T>` (`Success` vs `Error`).
  - Distinguir categoricamente entre errores de red (`Network`), errores locales de persistencia (`Local`), errores de sesion/autenticacion (`Auth`), errores de validación (`Validation`) y errores no controlados (`Unknown`).

- **Mensajes de Usuario Amigables y Localizados**:
  - Los errores técnicos o volcados de traza (`stackTrace`) nunca deben mostrarse al usuario final.
  - Los errores deben mapearse a cadenas localizadas en recursos nativos (`strings.xml`) con acciones claras de recuperación (ej. botón de reintento, solicitud de re-autenticacion).

- - -

## 3. Estrategia de Testing y Verificación

- **Pruebas Unitarias Obligatorias**:
  - Cada nuevo Caso de Uso debe incluir pruebas unitarias con JUnit 4 y MockK que cubran el camino feliz y todos los casos de borde.
  - Cada ViewModel debe verificarse utilizando Turbine para comprobar las transiciones y emisiones de `StateFlow`.

- **Pruebas de Persistencia e Integración**:
  - Cualquier cambio en entidades o DAOs de Room debe respaldarse con pruebas sobre la base de datos en memoria (`inMemoryDatabaseBuilder`).

- **Cero Regresiones**:
  - Todo cambio debe compilar limpiamente con `./gradlew assembleDebug` y superar `./gradlew testDebugUnitTest` antes de darse por completado.
