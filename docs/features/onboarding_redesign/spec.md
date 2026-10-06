# [APROBADO] Especificación Funcional: Rediseño Inmersivo del Onboarding MiraiLink

- **Fecha**: 2026-10-01
- **Estado**: [APROBADO]
- **Autor / Responsable**: Pair Programming (Antigravity & User)
- **Módulo Afectado**: `:app` (`com.feryaeljustice.mirailink`)
- **Rama Git**: `feature/onboarding-redesign`

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**: El onboarding original de MiraiLink utilizaba capturas de pantalla estáticas del mockup de la app (`onboarding_1.webp`, `onboarding_2.webp`, `onboarding_3.webp`) embebidas dentro de un contenedor rígido con texto justificado monótono y botones básicos. Este enfoque sufre de fatiga visual, se ve desactualizado, genera distorsión en pantallas con diferentes densidades o factores de forma (tablets/foldables) y carece de la personalidad anime/otaku y moderna que define la identidad de MiraiLink.
- **Objetivo**: Desarrollar un nuevo onboarding completamente renovado y expresivo (Material Design 3 + estética Otaku/Anime moderna con gradientes sutiles, micro-interacciones y composiciones vectoriales). Se eliminarán físicamente los archivos WebP antiguos y se sustituirán por hero art visuales nativos en Compose, una jerarquía tipográfica limpia (titular + descripción), indicador de páginas animado (worm/pill indicator), botón de omitir (*Skip*), y navegación fluida con persistencia inmediata al completar el flujo.

- - -

## 2. Situación Actual

- `OnboardingScreen.kt` contiene un `HorizontalPager` de 3 páginas con:
  - Imagen superior del logo (`logomirailink`).
  - Lista de imágenes drawables: `R.drawable.onboarding_1`, `R.drawable.onboarding_2`, `R.drawable.onboarding_3`.
  - Textos largos en bloque justificado: `R.string.onboarding_1`, `R.string.onboarding_2`, `R.string.onboarding_3`.
  - Botones "Anterior" y "Siguiente" / "Empezar" con peso 0.1f.
- En `NavWrapper.kt`:
  - `entry<AppScreen.OnboardingScreen>` invoca `OnboardingScreen(onFinish = { miraiLinkPrefs.markOnboardingCompleted() })`.
- Archivos físicos presentes en disco:
  - `app/src/main/res/drawable/onboarding_1.webp` (~50 KB)
  - `app/src/main/res/drawable/onboarding_2.webp` (~93 KB)
  - `app/src/main/res/drawable/onboarding_3.webp` (~37 KB)

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)
- **Eliminación Física de Recursos Obsoletos**:
  - Borrado completo de `onboarding_1.webp`, `onboarding_2.webp` y `onboarding_3.webp` en `app/src/main/res/drawable/`.
  - Limpieza y actualización de los strings en `values/strings.xml`, `values-es/strings.xml` y `values-en/strings.xml` para titulares y subtítulos.
- **Estructura Visual de Onboarding (3 Pasos Temáticos Anime/MiraiLink)**:
  - **Paso 1: Sincronía y Conexión Otaku**:
    - Composición visual: Ilustración en Compose con orbes de gradiente neón, avatar estilizado flotante, insignia de corazones entrelazados y chispas/destellos tipo anime (*kirakira* ✨).
    - Título: "Encuentra tu Player 2"
    - Subtítulo: Descubre personas afines que comparten tu pasión por el anime, manga y videojuegos con nuestro radar de compatibilidad.
  - **Paso 2: Expresa tu Auténtico Yo (Perfil)**:
    - Composición visual: Mockup estilizado de tarjeta de perfil anime flotante con tags de intereses de ejemplo (ej. Shonen, JRPG, Cyberpunk), avatar circular brillante y efecto de elevación.
    - Título: "Tu Identidad, Tus Pasiones"
    - Subtítulo: Destaca tus animes favoritos, juegos predilectos y estilo de vida para que los demás vean quién eres de verdad.
  - **Paso 3: Conversaciones en Tiempo Real & Privacidad**:
    - Composición visual: Burbujas de chat estilizadas con indicador de presencia luminosa, un escudo de seguridad con destello suave y candado cyberpunk/otaku.
    - Título: "Conversaciones en Tiempo Real"
    - Subtítulo: Conexión instantánea, mensajes en tiempo real y privacidad garantizada para que solo te preocupes de disfrutar de la charla.
  - **Paso 4: Comunidad y Eventos Otaku**:
    - Composición visual: Tarjeta animada de eventos, salones manga y convenciones locales con iconos temáticos, tag de meetups y cosplay.
    - Título: "Comunidad y Eventos Otaku"
    - Subtítulo: Explora categorías temáticas, salones del manga, convenciones y eventos locales para conectar con tu grupo ideal.
- **Optimización de Tiempo de Carga (Splash Screen)**:
    - Evaluación de `checkOnboardingIsCompletedUseCase()` desacoplada del `autologinUseCase()` de red: si el onboarding no está completado en preferencias locales, la app transiciona de inmediato a `InitialNavigationAction.GoToOnboarding` sin bloquearse esperando timeouts de conexión ni respuestas remotas.
- **Componentes y UX Moderna (Android / Material 3)**:
  - **Botón Omitir ("Saltar" / "Skip")**: Ubicado en la parte superior para permitir al usuario salir inmediatamente; al pulsar llama a `onFinish()` completando el onboarding y navegando a Auth.
  - **Indicador de Página (Pill Page Indicator)**: Indicadores de página animados con interpolación de ancho suave que destacan la página actual de forma orgánica.
  - **Botones de Navegación Inferiores Ergonómicos**:
    - Botón de retroceso sutil (icono de flecha o texto secundario accesible >= 48dp).
    - Botón de avance principal con estilo píldora prominente (`Button` elevado o relleno con gradiente/color primario de MiraiLink).
    - En la última página, el botón cambia dinámicamente a "¡Comenzar Aventura!" con llamado a la acción entusiasta.
  - **Soporte Adaptativo Edge-to-Edge & WindowSizeClass**:
    - Respeto total de insets de recorte de pantalla (`displayCutout`) y barras del sistema (`safeDrawingPadding()`).
    - En pantallas compactas (teléfonos verticales): layout vertical optimizado con respiración.
    - En pantallas expandidas / tablets: layout de 2 columnas o centrado responsivo con límite de ancho.

### 3.2. Fuera del Alcance (Out of Scope)
- Modificar el flujo de autenticación o la pantalla de Login/Registro posterior.
- Modificar la lógica interna de `MiraiLinkPrefs` o la arquitectura de persistencia `isOnboardingCompleted()`.
- Incorporar dependencias pesadas de terceros no presentes en el proyecto (ej. librerías de vídeo complejas).

- - -

## 4. Mobile Guidelines & Casuísticas Críticas (SDMD)

- **Ciclo de vida y retención**: `rememberPagerState` retiene la página activa durante rotaciones o cambios de configuración.
- **TalkBack & Accesibilidad**:
  - Cada elemento visual compuesto tiene `contentDescription = null` si es decorativo o una descripción accesible adecuada.
  - Los botones de acción cuentan con touch target mínimo de 48dp x 48dp y soporte para lectores de pantalla.
- **Edge-to-Edge & Teclado**: No hay campos de texto, pero se asegura padding seguro frente a barras de estado, navegación gestual y muescas (`displayCutout`).
- **Tema Claro / Oscuro**: El arte y fondos usan tokens semánticos de `MaterialTheme.colorScheme` para garantizar contraste y visibilidad perfecta en ambos modos.

- - -

## 5. Criterios de Aceptación (Given-When-Then)

- **CA-01: Visualización Inicial**:
  - *Dado* que un usuario abre la app por primera vez y `isOnboardingCompleted()` es falso,
  - *Cuando* se carga `OnboardingScreen`,
  - *Entonces* ve la pantalla 1 ("Encuentra tu Player 2") con su composición artística en Compose, indicador en la primera píldora, botón "Saltar" arriba y botón "Siguiente" abajo.

- **CA-02: Navegación de Páginas**:
  - *Dado* que el usuario está en el paso 1 o 2,
  - *Cuando* pulsa "Siguiente" o desliza el dedo hacia la izquierda,
  - *Entonces* la vista transiciona suavemente a la siguiente página y el indicador de píldora actualiza la posición animada.

- **CA-03: Finalización del Onboarding**:
  - *Dado* que el usuario llega a la página 3,
  - *Cuando* pulsa "¡Comenzar Aventura!" (o pulsa "Saltar" en cualquier paso),
  - *Entonces* se invoca `onFinish()`, `miraiLinkPrefs.markOnboardingCompleted()` se ejecuta y se transiciona a la pantalla de Auth sin posibilidad de volver atrás.

- **CA-04: Eliminación Limpia de Recursos**:
  - *Dado* que se refactoriza el proyecto,
  - *Cuando* se compila la aplicación,
  - *Entonces* `onboarding_1.webp`, `onboarding_2.webp` y `onboarding_3.webp` no existen en disco ni se referencian en ningún punto del código fuente.
