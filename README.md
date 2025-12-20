# MiraiLink

MiraiLink es la app social pensada para fans del anime y los videojuegos. Combina un backend propio en ExpressJS con un cliente Android construido en Jetpack Compose, MVVM, Koin, Version Catalog y Clean Architecture para ofrecer chat en tiempo real, matching, perfiles personalizables y almacenamiento seguro. Usa Android Credentials. Testing al completo (Unitario, Integración con Instrumentación y E2E), incluido el nuevo sistema de testing por Screenshot de Gemini y el testing de journeys con Kotzilla.

## Stack principal

- Kotlin 2.2 con Jetpack Compose (Material 3, navegación declarativa, diseño atómico)
- Navigation 3 (navegación optimizada para compose)
- Arquitectura modular por capas: Data / Domain / UI + módulos de inyección de dependencias
- Koin para DI, Socket.IO para chat, Retrofit + OkHttp para REST, DataStore cifrado para preferencias
- Firebase (Analytics y Crashlytics), Google Mobile Ads, Kotlinx Serialization, Coil
- Kotzilla para testing de journeys y captura de screenshots

## Arquitectura y estructura de carpetas

La base del repositorio sigue Clean Architecture con una separación clara de responsabilidades:

| Ruta                                                     | Descripción                                                                                                                           |
|----------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------|
| `app/src/main/java/com/feryaeljustice/mirailink/data/`   | Implementación de la capa de datos (datasources remotos/locales, repositorios concretos, mapeadores, utilidades de red y telemetría). |
| `app/src/main/java/com/feryaeljustice/mirailink/domain/` | Capa de dominio con modelos de negocio, contratos de repositorios y casos de uso.                                                     |
| `app/src/main/java/com/feryaeljustice/mirailink/ui/`     | Presentación con pantallas Compose, componentes reutilizables (Atoms, Molecules, Organisms), ViewModels y flujo de navegación.        |
| `app/src/main/java/com/feryaeljustice/mirailink/di/`     | Módulos Koin que vinculan interfaces de dominio con implementaciones de datos y gestores del ciclo de vida.                           |
| `app/src/main/res/`                                      | Recursos Android (temas, strings, íconos, layouts XML puntuales si fueran necesarios).                                                |
| `gradle/libs.versions.toml`                              | Catálogo centralizado de versiones (AGP 8.13.0, Kotlin 2.2.20, Compose BOM 2025.09.01, etc.).                                         |
| `build.gradle.kts` y `app/build.gradle.kts`              | Configuración de Gradle, plugins y dependencias.                                                                                      |

### Flujo de datos resumido

1. La UI (Compose) dispara eventos hacia su ViewModel.
2. El ViewModel invoca casos de uso concretos.
3. Los casos de uso coordinan repositorios del dominio.
4. Los repositorios consultan datasources remotos/locales, aplican mappers y devuelven resultados tipados.
5. La información vuelve a la UI reactiva para renderizar estados y side effects controlados.

## Requisitos del entorno

- **IDE**: Android Studio Koala o superior (compatible con AGP 8.13). Alternativamente, Android Studio Iguana/Giraffe con Gradle actualizado y soporte para Kotlin 2.2.20.
- **JDK**: Temurin u OpenJDK 17+ (la configuración por defecto de Android Studio 2024+ ya lo incluye).
- **Android SDK**: API 36 (target) y mínimo API 26. Instala las plataformas y herramientas de compilación correspondientes desde el SDK Manager.
- **Herramientas de línea de comando**: `adb` (incluido con el SDK) para instalar y depurar builds.
- **Backend**: instancia activa del servidor MiraiLink en ExpressJS (configurar endpoints y claves en los datasources remotos o DataStore según tus necesidades).
- **Opcional**: Node.js 18+ si necesitas levantar el backend localmente, Firebase CLI para pruebas de analíticas.

## Puesta en marcha y ejecución

1. Abre el proyecto con Android Studio y permite que Gradle sincronice las dependencias (wrapper incluido).
2. Configura los archivos sensibles (`keystore.properties`, `google-services.json`) si vas a firmar o usar servicios de Firebase/Ads.
3. Verifica que el backend ExpressJS esté accesible desde el emulador/dispositivo (mismo Wi-Fi o tunelización).
4. Lanza los comandos deseados desde la raíz del proyecto:

```powershell
# Limpiar y generar APK debug
.\gradlew.bat clean assembleDebug

# Instalar en un dispositivo/emulador conectado
.\gradlew.bat installDebug

# Instalar y abrir la actividad principal
.\gradlew.bat installDebug
adb shell am start -n com.feryaeljustice.mirailink/.ui.MainActivity
```

En macOS/Linux utiliza `./gradlew` con los mismos tasks. También puedes ejecutar `Run > Run 'app'` desde Android Studio para compilar e instalar automáticamente.

## Pruebas y calidad

- **Unit tests**: `.\gradlew.bat testDebugUnitTest` valida casos de uso, ViewModels y mappers con dobles de prueba. Se usa KoinTest para la inyección de dependencias en tests.
- **Instrumented tests**: `.\gradlew.bat connectedDebugAndroidTest` ejecuta pruebas sobre un dispositivo/emulador conectado. Se usa KoinTest para la inyección de dependencias en tests.
- **Lint y chequeos estáticos**: `.\gradlew.bat lintDebug` revisa estilo, accesibilidad y buenas prácticas en Compose.
- **Builds adicionales**: `.\gradlew.bat assembleRelease` (requiere `keystore.properties`) y `.\gradlew.bat bundleRelease` para generar el AAB de distribución.

## Buenas prácticas clave

- Mantén las capas desacopladas: la UI depende del dominio, nunca directamente de la capa de datos.
- Usa DataStore cifrado para datos sensibles y evita registrar información confidencial en logs.
- Gestiona el ciclo de vida del socket desde la capa de datos, exponiendo estados seguros al dominio/UI.
- Sigue el patrón de diseño atómico en los componentes Compose para facilitar reuso y pruebas visuales.
- Antes de publicar, ejecuta lint, tests unitarios y de instrumentación, y prueba la build final en dispositivos reales.

### Apuntes

- Si se quiere hacer el tema del screenshot testing con el validate y update DebugScreenshotTest se
  debe mover a la raiz del disco duro para evitar problemas de ruta muy larga.
