# Directrices Críticas de Mobile (Mobile Guidelines - SDMD)

Este documento define las restricciones físicas de hardware y de sistema operativo Android que cualquier agente o desarrollador debe auditar obligatoriamente antes de cerrar una especificación funcional (`spec.md`) o un plan técnico (`plan.md`) en MiraiLink.

- - -

## 1. Ciclo de Vida (Lifecycle) y Muerte de Procesos

- **Destrucción de Procesos en Segundo Plano (Low Memory Killer)**:
  - El sistema operativo Android puede eliminar el proceso de la aplicación en cualquier momento para liberar memoria RAM cuando pasa a segundo plano.
  - El estado crítico de formularios, filtros y datos de pantalla debe persistirse mediante `SavedStateHandle` en los ViewModels o delegarse a la capa de persistencia local (Room / EncryptedDataStore).

- **Cambios de Configuración (Configuration Changes)**:
  - Las pantallas deben sobrevivir intactas a rotaciones de pantalla, transiciones a modo multi-ventana y cambios de idioma del sistema sin reiniciar flujos de usuario ni perder datos introducidos.

- **Cancelación y Fugas de Memoria en Corrutinas**:
  - Toda tarea asíncrona debe estar vinculada al `viewModelScope` o al ciclo de vida del composable correspondiente (`LaunchedEffect` con claves de control adecuadas).
  - Nunca lanzar corrutinas en `GlobalScope` para evitar fugas de memoria o llamadas huerfanas tras la destrucción de una pantalla.

- - -

## 2. Conectividad y Estrategia Offline-First

- **Modo Hibrido (Online vs Offline Demo)**:
  - MiraiLink cuenta con un modo Sandbox Offline respaldado por Room Database. Cualquier funcionalidad orientada al usuario debe contemplar su funcionamiento tanto en el modo online conectado al backend como en el modo offline demo local sin conexión.

- **Respuesta Local Inmediata (Cache-First)**:
  - La interfaz de usuario debe mostrar inmediatamente los datos disponibles localmente en la base de datos o en memoria mientras se sincroniza en segundo plano con el servidor.

- **Taxonomía de Errores de Red**:
  - Distinguir nitidamente entre:
    - Ausencia total de conectividad (`error_no_connection`).
    - Tiempo de espera agotado (`error_timeout`).
    - Error de respuesta del servidor (`error_server`).
    - Fallos de serialización (`error_serialization`).
  - Proporcionar siempre un botón o acción de reintento (`action_retry`) sin forzar al usuario a abandonar la pantalla.

- **Mensajería Optimista**:
  - Los mensajes en el chat deben generarse con un identificador UUID local y mostrarse de inmediato en la conversación con estado "enviando", actualizandose a "enviado" una vez confirmado por el WebSocket / API.

- - -

## 3. Ergonomía de UI, Accesibilidad y Compose

- **Areas Táctiles Minimas (Touch Targets)**:
  - Todos los botones, iconos interactivos, chips y elementos clickeables deben tener una dimension mínima accesible de 48 x 48 dp (o espaciado equivalente mediante padding).

- **Gestión del Teclado en Pantalla (IME Padding)**:
  - Los campos de texto y botones de envío o guardado deben incorporar soporte para `WindowInsets` mediante `imePadding()` para garantizar que el teclado virtual no oculte la entrada activa ni impida la lectura del contenido.

- **Soporte de Temas (Light y Dark Mode)**:
  - Todas las pantallas y componentes deben renderizarse correctamente en tema claro y tema oscuro respetando la paleta de colores de Material 3 definida en `ui/theme`.

- **Accesibilidad y TalkBack**:
  - Todo elemento interactivo o imagen informativa debe proporcionar una descripción de contenido clara (`contentDescription`) extraida de `strings.xml`. Elementos puramente decorativos deben marcarse con `contentDescription = null`.

- **Soporte Edge-to-Edge**:
  - Respetar los márgenes de las barras del sistema (barra de estado y barra de navegación por gestos) utilizando `safeDrawingPadding()` o paddings basados en `WindowInsetsCompat`.

- - -

## 4. Persistencia, Seguridad y Respaldos

- **Aislamiento de Material Sensible**:
  - Los tokens de acceso, claves de cifrado y contraseñas nunca deben guardarse en SharedPreferences en texto plano. Se debe usar exclusivamente `EncryptedDataStore` respaldado por Android Keystore.
  - Prohibido imprimir tokens o datos personales en los logs de Android (`Log.d`, `println`).

- **Exclusión de Copias de Seguridad Automáticas (Auto Backup)**:
  - Los archivos de credenciales, tokens temporales y cachés locales no deben incluirse en las copias de seguridad automáticas en la nube de Android (Google Drive Auto Backup).

- **Evolución y Migraciones de Base de Datos**:
  - Toda modificación en las entidades o tablas de Room (`local/demo`) debe contar con una estrategia de versionado y migración probada mediante tests instrumentados para evitar cierres inesperados (`IllegalStateException`).

## 5. Jetpack Compose Guidelines
### Convenciones de Diseño de Componentes y Firmas
- **Regla del Modifier**: Todo `@Composable` que emita UI propia debe exponer un parámetro `modifier: Modifier = Modifier`.
- **Posición del Modifier**: Debe ser estrictamente el **primer parámetro opcional** de la función Composable:
  1. Primero: Parámetros obligatorios (datos necesarios, estados obligatorios).
  2. Segundo (primer opcional): `modifier: Modifier = Modifier`.
  3. Tercero: Resto de parámetros opcionales con valores por defecto (estilos, configuraciones secundarias).
  4. Al final: Lambdas de eventos o trailing lambdas de contenido (`content: @Composable () -> Unit`, `onClick: () -> Unit`).