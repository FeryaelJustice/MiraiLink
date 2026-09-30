# [APROBADO] Plan Tecnico de Arquitectura: Rediseño Inmersivo del Onboarding MiraiLink

- **Especificacion funcional asociada**: `docs/features/onboarding_redesign/spec.md`
- **Estado**: [APROBADO]
- **Fecha**: 2026-10-01
- **Modulo**: `:app` (`com.feryaeljustice.mirailink`)
- **Rama Git**: `feature/onboarding-redesign`

- - -

## 1. Hechos Verificados en el Proyecto (Sin Alucinaciones)

Informacion verificada rigurosamente en `gradle/libs.versions.toml`, `app/build.gradle.kts` y manifiesto:

- **Lenguaje & JVM**: Kotlin `2.4.10` / Java 17 toolchain
- **Compilador & Build Tool**: AGP `9.4.1`, Gradle wrapper, KSP `2.3.8`
- **SDK Targets**: Min SDK `30`, Compile SDK `37`, Target SDK `37`
- **Librerias Verificadas en el Classpath**:
  - UI: Jetpack Compose BOM `2026.09.00` con Material 3 (`androidx.compose.material3`)
  - Iconos Material Icons Extended: disponibles para composición vectorial (`androidx.compose.material.icons.rounded.*`)
  - Animaciones y Graficos: `androidx.compose.animation:animation`, Canvas API (`androidx.compose.ui.graphics.drawscope`), `androidx.compose.foundation.pager.HorizontalPager` y `rememberPagerState`.
  - Inyeccion de Dependencias: Koin BOM `4.2.2` con `koin-android` y `koin-compose-viewmodel`
  - Persistencia: `MiraiLinkPrefs` con `markOnboardingCompleted()` e `isOnboardingCompleted()`
- **Eliminación Física de Recursos Obsoletos**:
  - `app/src/main/res/drawable/onboarding_1.webp`
  - `app/src/main/res/drawable/onboarding_2.webp`
  - `app/src/main/res/drawable/onboarding_3.webp`

- - -

## 2. Impacto Arquitectonico y Contratos por Capas

### 2.1. Recursos de Cadenas Localizadas (`res/values/strings.xml`, `res/values-es/strings.xml`, `res/values-en/strings.xml`)
Se actualizarán los strings para separar titular y descripción concisa:
- `onboarding_skip`: "Omitir" / "Skip"
- `onboarding_start_adventure`: "¡Comenzar Aventura!" / "Start Adventure!"
- `onboarding_title_1`: "Encuentra tu Player 2" / "Find your Player 2"
- `onboarding_desc_1`: "Descubre personas afines que comparten tu pasión por el anime, manga y videojuegos con nuestro radar de compatibilidad." / "Discover like-minded people who share your passion for anime, manga, and gaming with our compatibility radar."
- `onboarding_title_2`: "Tu Identidad, Tus Pasiones" / "Your Identity, Your Passions"
- `onboarding_desc_2`: "Destaca tus animes favoritos, juegos predilectos y estilo de vida para que los demás vean quién eres de verdad." / "Highlight your favorite anime, top games, and lifestyle so others see the real you."
- `onboarding_title_3`: "Conversaciones en Tiempo Real" / "Real-Time Conversations"
- `onboarding_desc_3`: "Conexión instantánea, mensajes en tiempo real y privacidad garantizada para que solo te preocupes de disfrutar de la charla." / "Instant connection, real-time messaging, and guaranteed privacy so you can simply enjoy chatting."

### 2.2. Capa de Presentación (`ui/screens/onboarding/`)

#### 1. Modelado Interno de la Página (`OnboardingPageData`)
```kotlin
data class OnboardingStep(
    val titleRes: Int,
    val descriptionRes: Int,
    val illustrationType: OnboardingIllustrationType,
)

enum class OnboardingIllustrationType {
    RADAR_CONNECTION, // Paso 1: Orbes, corazones entrelazados y destellos kirakira ✨
    PROFILE_SHOWCASE, // Paso 2: Tarjeta flotante con tags gamer/otaku y avatar brillante
    INSTANT_CHAT,     // Paso 3: Burbujas de chat cyberpunk con candado de privacidad y pulsos
}
```

#### 2. Componentes Visuales Nativos (`OnboardingIllustrations.kt`)
Se implementan composables con gráficos vectoriales y Canvas:
- `OnboardingConnectionIllustration()`:
  - Fondo con halo circular suave mediante `Brush.radialGradient` (tonos magenta a violeta translúcido).
  - Dos avatares/orbes interactuando con partículas brillantes (`Canvas` con estrellas de 4 puntas *kirakira* anime).
  - Corazón y ondas sutiles de conexión.
- `OnboardingProfileIllustration()`:
  - Tarjeta elevada con borde suave y sombra estilizada.
  - Avatar circular superior con halo degradado.
  - Mock de barra de nombre ("Akari, 22") y chips de intereses animados ("Anime", "JRPG", "Cyberpunk").
- `OnboardingChatIllustration()`:
  - Dos globos de mensaje asimétricos tipo chat moderno (uno recibido con color suave, otro enviado con primario).
  - Puntos de escritura animados / pulso de conexión en tiempo real.
  - Insignia de candado/escudo protector con brillo de seguridad.

#### 3. Componente Indicador de Páginas (`OnboardingPillIndicator.kt`)
- `OnboardingPageIndicator(pageCount: Int, currentPage: Int, modifier: Modifier)`:
  - Cada punto se anima con `animateDpAsState` (ancho de 8.dp a 24.dp para la página activa).
  - Color `primary` para activo y `outlineVariant` / `surfaceVariant` para inactivos con esquinas redondeadas (`CircleShape`).

#### 4. Pantalla Principal (`OnboardingScreen.kt`)
- Barra superior con botón "Omitir" accesible (touch target >= 48dp, `TextButton` o `MiraiLinkTextButton`).
- Logo de MiraiLink con presencia elegante.
- `HorizontalPager` animado con swipe fluido.
- Jerarquía de texto:
  - Título: `MaterialTheme.typography.headlineMedium` con `fontWeight = Bold`, centrado.
  - Descripción: `MaterialTheme.typography.bodyMedium` con `color = onSurfaceVariant`, alineado al centro, espaciado cómodo.
- Barra inferior:
  - Indicador de página (`OnboardingPageIndicator`).
  - Botón de retroceso (si `currentPage > 0`).
  - Botón principal de avance en píldora con ancho adaptable y animación de texto entre "Siguiente" y "¡Comenzar Aventura!".
- Respeto a WindowInsets y Edge-to-Edge (`WindowInsets.safeDrawing`, `displayCutout`).

- - -

## 3. Plan de Testing y Verificación

1. **Test de Compilación y Limpieza**:
   - Verificar que no queden referencias a `R.drawable.onboarding_*` ni en código ni en previews.
   - Ejecutar `./gradlew assembleDebug` para confirmar compilación limpia con AGP 9.
2. **Tests Unitarios del Repositorio de Onboarding**:
   - Ejecutar `OnboardingRepositoryImplTest` y `CheckOnboardingIsCompletedTest` para asegurar que la lógica de persistencia sigue impecable.
3. **Validación de UI & Ergonomía**:
   - Verificar que los botones cumplen con tamaño accesible (touch target >= 48dp).
   - Verificar comportamiento del botón "Omitir" invocando `onFinish()`.
