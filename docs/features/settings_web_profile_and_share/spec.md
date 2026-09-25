# [BORRADOR] Especificacion Funcional: Perfil Web, Nombre de Usuario Inmutable y Compartir Perfil en Ajustes

- **Fecha**: 2026-09-25
- **Estado**: [BORRADOR]
- **Autor / Responsable**: Antigravity & FeryaelJustice
- **Modulo Afectado**: `:app` (`com.feryaeljustice.mirailink`)
- **Rama Git Planificada**: `feature/settings-web-profile-and-share`

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**:
  - En la pantalla de Ajustes (`SettingsScreen`), el usuario actualmente solo visualiza acciones tecnicas (feedback, 2FA, logout, eliminar cuenta, politicas y FAQ).
  - No existe visibilidad directa del nombre de usuario inmutable (`username`), el cual es unico y no modificable en MiraiLink (a diferencia del `nickname` que si se puede editar).
  - El usuario no dispone de una forma comoda y directa de compartir su enlace publico de perfil (`https://mirailink.xyz/user/<username>`).
  - No puede previsualizar como ven su perfil el resto de usuarios sin interactuar consigo mismo (modo solo lectura sin botones de swipe/like/dislike).
- **Objetivo**:
  - Incluir en `SettingsScreen` un bloque visual "Perfil web" (inspirado en la interfaz de Tinder):
    1. **Nombre de usuario**: Indicador de solo lectura con `@username`.
    2. **Ver perfil**: Accion tactil que abre la pantalla de detalle de perfil (`UserProfileDetailScreen(username, canInteract = false)`), cargando su perfil tal como lo ve cualquier persona a traves de deep link, con la barra inferior de like/dislike oculta.
    3. **Compartir mi URL**: Accion tactil que abre la hoja nativa de compartir de Android (`Intent.ACTION_SEND`) con la URL canonica del perfil.
  - Asegurar compatibilidad completa con el modo Offline Demo (usando el usuario de prueba) y en modo Online conectado.

- - -

## 2. Situacion Actual

- `User` cuenta con `username: String` inmutable y `nickname: String`.
- `AppScreen.UserProfileDetailScreen` ya implementa el parametro `canInteract: Boolean = true`, y oculta `BottomInteractionBar` cuando `canInteract == false`.
- `NavWrapper.kt` ya soporta deep links con la ruta `mirailink.com/user/<username>` que abren `UserProfileDetailScreen(username, canInteract = false)`.
- Falta la exposicion del nombre de usuario en `SettingsViewModel` y la seccion interactiva "Perfil web" en `SettingsScreen`.

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)
- Consulta del usuario actual en `SettingsViewModel` mediante `GetCurrentUserUseCase`.
- Exposicion del `username` en el estado de `SettingsViewModel`.
- Nuevo componente `WebProfileSection` en `SettingsScreen`:
  - Fila "Nombre de usuario": etiqueta izquierda, valor `@username` a la derecha.
  - Fila clickeable "Ver perfil": flecha indicadora, navega a `UserProfileDetailScreen(username, canInteract = false)`.
  - Fila clickeable "Compartir mi URL": icono de compartir, lanza el `Intent.createChooser` del sistema.
- Cadenas de texto localizadas en `values/strings.xml`, `values-es/strings.xml` y `values-en/strings.xml`.

### 3.2. Fuera del Alcance (Out of Scope)
- Modificacion o edicion del `username` (es permanentemente inmutable por diseno).
- Registro o creacion de URLs con dominios personalizados.

- - -

## 4. Casuisticas y Comportamiento Mobile

- **Modo Online vs Modo Offline Demo**:
  - En Online: `GetCurrentUserUseCase` obtiene los datos de la sesion activa.
  - En Demo: `DemoUserRepositoryImpl` provee el usuario demo con su `username` correspondiente (`feryaeljustice` o similar).
- **Ciclo de Vida y Recuperacion de Estado**:
  - Si el usuario rota la pantalla o el proceso se suspende, el `username` se preserva o se rehidrata reactivamente desde el ViewModel.
- **Ergonomia y Accesibilidad**:
  - Filas tactiles con un minimo de 48 x 48 dp.
  - Soporte completo para tema claro y tema oscuro Material 3.

- - -

## 5. Criterios de Aceptacion (Given - When - Then)

### Criterio 1: Visualizacion del nombre de usuario inmutable
- **Dado que**: El usuario accede a la pantalla de Ajustes.
- **Cuando**: Carga la informacion de la sesion.
- **Entonces**: Se muestra la seccion "Perfil web" con el nombre de usuario inmutable `@username`.

### Criterio 2: Visualizar perfil propio en modo solo lectura
- **Dado que**: El usuario pulsa sobre "Ver perfil" en Ajustes.
- **Cuando**: Se ejecuta la navegacion.
- **Entonces**: Se abre `UserProfileDetailScreen` con sus fotos, biografia y datos, sin la barra de interacciones (sin botones de Like ni Dislike).

### Criterio 3: Compartir enlace de perfil web
- **Dado que**: El usuario pulsa sobre "Compartir mi URL".
- **Cuando**: Se dispara el evento de compartir.
- **Entonces**: Se despliega el selector nativo de Android con el texto y enlace `https://mirailink.xyz/user/<username>`.

- - -

## 6. Decisiones Pendientes [PENDIENTE]

- [ ] [PENDIENTE] Confirmar si la URL canonica para compartir debe ser `https://mirailink.xyz/user/<username>` o `https://mirailink.com/user/<username>`.
- [ ] [PENDIENTE] Confirmar la posicion exacta en `SettingsScreen`: ¿en la parte superior de la pantalla antes de "Ajustes de busqueda" / "Cuenta", tal como aparece en Tinder?
