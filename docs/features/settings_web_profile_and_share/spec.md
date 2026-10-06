# [BORRADOR] Especificación Funcional: Perfil Web, Nombre de Usuario Inmutable y Compartir Perfil en Ajustes

- **Fecha**: 2026-09-25
- **Estado**: [BORRADOR]
- **Autor / Responsable**: Antigravity & FeryaelJustice
- **Módulo Afectado**: `:app` (`com.feryaeljustice.mirailink`)
- **Rama Git Planificada**: `feature/settings-web-profile-and-share`

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**:
  - En la pantalla de Ajustes (`SettingsScreen`), el usuario actualmente solo visualiza acciones técnicas (feedback, 2FA, logout, eliminar cuenta, políticas y FAQ).
  - No existe visibilidad directa del nombre de usuario inmutable (`username`), el cual es único y no modificable en MiraiLink (a diferencia del `nickname` que si se puede editar).
  - El usuario no dispone de una forma comoda y directa de compartir su enlace publico de perfil (`https://mirailink.xyz/user/<username>`).
  - No puede previsualizar como ven su perfil el resto de usuarios sin interactuar consigo mismo (modo solo lectura sin botones de swipe/like/dislike).
- **Objetivo**:
  - Incluir en `SettingsScreen` un bloque visual "Perfil web" (inspirado en la interfaz de Tinder):
    1. **Nombre de usuario**: Indicador de solo lectura con `@username`.
    2. **Ver perfil**: Acción táctil que abre la pantalla de detalle de perfil (`UserProfileDetailScreen(username, canInteract = false)`), cargando su perfil tal como lo ve cualquier persona a través de deep link, con la barra inferior de like/dislike oculta.
    3. **Compartir mi URL**: Acción táctil que abre la hoja nativa de compartir de Android (`Intent.ACTION_SEND`) con la URL canónica del perfil.
  - Asegurar compatibilidad completa con el modo Offline Demo (usando el usuario de prueba) y en modo Online conectado.

- - -

## 2. Situación Actual

- `User` cuenta con `username: String` inmutable y `nickname: String`.
- `AppScreen.UserProfileDetailScreen` ya implementa el parámetro `canInteract: Boolean = true`, y oculta `BottomInteractionBar` cuando `canInteract == false`.
- `NavWrapper.kt` ya soporta deep links con la ruta `mirailink.com/user/<username>` que abren `UserProfileDetailScreen(username, canInteract = false)`.
- Falta la exposición del nombre de usuario en `SettingsViewModel` y la sección interactiva "Perfil web" en `SettingsScreen`.

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)
- Consulta del usuario actual en `SettingsViewModel` mediante `GetCurrentUserUseCase`.
- Exposición del `username` en el estado de `SettingsViewModel`.
- Nuevo componente `WebProfileSection` en `SettingsScreen`:
  - Fila "Nombre de usuario": etiqueta izquierda, valor `@username` a la derecha.
  - Fila clickeable "Ver perfil": flecha indicadora, navega a `UserProfileDetailScreen(username, canInteract = false)`.
  - Fila clickeable "Compartir mi URL": icono de compartir, lanza el `Intent.createChooser` del sistema.
- Cadenas de texto localizadas en `values/strings.xml`, `values-es/strings.xml` y `values-en/strings.xml`.

### 3.2. Fuera del Alcance (Out of Scope)
- Modificación o edición del `username` (es permanentemente inmutable por diseño).
- Registro o creación de URLs con dominios personalizados.

- - -

## 4. Casuísticas y Comportamiento Mobile

- **Modo Online vs Modo Offline Demo**:
  - En Online: `GetCurrentUserUseCase` obtiene los datos de la sesión activa.
  - En Demo: `DemoUserRepositoryImpl` provee el usuario demo con su `username` correspondiente (`feryaeljustice` o similar).
- **Ciclo de Vida y Recuperación de Estado**:
  - Si el usuario rota la pantalla o el proceso se suspende, el `username` se preserva o se rehidrata reactivamente desde el ViewModel.
- **Ergonomía y Accesibilidad**:
  - Filas táctiles con un mínimo de 48 x 48 dp.
  - Soporte completo para tema claro y tema oscuro Material 3.

- - -

## 5. Criterios de Aceptación (Given - When - Then)

### Criterio 1: Visualización del nombre de usuario inmutable
- **Dado que**: El usuario accede a la pantalla de Ajustes.
- **Cuando**: Carga la información de la sesión.
- **Entonces**: Se muestra la sección "Perfil web" con el nombre de usuario inmutable `@username`.

### Criterio 2: Visualizar perfil propio en modo solo lectura
- **Dado que**: El usuario pulsa sobre "Ver perfil" en Ajustes.
- **Cuando**: Se ejecuta la navegación.
- **Entonces**: Se abre `UserProfileDetailScreen` con sus fotos, biografía y datos, sin la barra de interacciones (sin botones de Like ni Dislike).

### Criterio 3: Compartir enlace de perfil web
- **Dado que**: El usuario pulsa sobre "Compartir mi URL".
- **Cuando**: Se dispara el evento de compartir.
- **Entonces**: Se despliega el selector nativo de Android con el texto y enlace `https://mirailink.xyz/user/<username>`.

- - -

## 6. Decisiones Pendientes [PENDIENTE]

- [ ] [PENDIENTE] Confirmar si la URL canónica para compartir debe ser `https://mirailink.xyz/user/<username>` o `https://mirailink.com/user/<username>`.
- [ ] [PENDIENTE] Confirmar la posición exacta en `SettingsScreen`: ¿en la parte superior de la pantalla antes de "Ajustes de búsqueda" / "Cuenta", tal como aparece en Tinder?
