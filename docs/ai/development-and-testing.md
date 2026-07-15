# Desarrollo y pruebas

## Toolchain actual

| Herramienta | Versión o requisito |
| --- | --- |
| Java | 17 |
| Gradle Wrapper | 9.6.1 |
| Android Gradle Plugin | 9.3.0 |
| Kotlin | 2.4.10 |
| KSP | 2.3.8 |
| compile SDK | 37 |
| target SDK | 37 |
| min SDK | 26 |

La configuración usa built-in Kotlin de AGP, KSP2, configuration cache y la opción experimental de screenshot testing.

## Preparación local

1. Instala Java 17 y Android SDK 37.
2. Conserva `local.properties` solo en local con la ruta del SDK.
3. Proporciona `app/google-services.json` desde un proyecto Firebase autorizado si no está presente en tu checkout.
4. Crea `keystore.properties` local con las propiedades que espera `app/build.gradle.kts`.
5. No publiques ni vuelques esas propiedades en logs.

El script Gradle espera claves de firma release y también `TEST_USER` y `TEST_PASS` para BuildConfig debug. Aunque el archivo se comprueba con `exists()`, sus valores se convierten después con casts no nulos. En la práctica no es opcional para una configuración normal del proyecto.

## Comandos principales

```powershell
# Compilar APK debug
.\gradlew.bat assembleDebug

# Tests unitarios JVM
.\gradlew.bat testDebugUnitTest

# Lint Android
.\gradlew.bat lintDebug

# Tests instrumentados, solo con dispositivo y entorno controlado
.\gradlew.bat connectedDebugAndroidTest

# Screenshot testing
.\gradlew.bat updateDebugScreenshotTest
.\gradlew.bat validateDebugScreenshotTest

# Release y bundle, requieren firma válida
.\gradlew.bat assembleRelease
.\gradlew.bat bundleRelease
```

Consulta las tareas disponibles antes de asumir nombres añadidos por plugins:

```powershell
.\gradlew.bat tasks
```

## Estado de validación del baseline

Comprobado desde `codex/ai-project-initialization` el 2026-07-15.

| Comando | Resultado | Evidencia |
| --- | --- | --- |
| `assembleDebug` | Correcto | APK generado, 26.923.031 bytes |
| `testDebugUnitTest` | Fallo de compilación | Cuatro instancias de SplashScreenViewModel sin `store` ni `isInChristmasMode` |
| `lintDebug` | Fallo | 1 error `CredManMissingDal` y 100 warnings |
| `connectedDebugAndroidTest` | No ejecutado | Incluye login, like y swipe contra servicios externos |
| Android CLI `describe` | Sin resultado | Dos intentos agotaron 30 y 60 segundos tras requerir acceso a su caché |

El código principal compila durante unit tests y el APK debug se construye. Sin embargo, no puede afirmarse que los 258 tests unitarios pasen porque la suite se detiene en `compileDebugUnitTestKotlin`.

### Fallo unitario actual

`SplashScreenViewModel` añadió estos parámetros:

- `FeatureFlagStore store`
- `Boolean isInChristmasMode`

`SplashScreenViewModelTest` construye el ViewModel manualmente en las líneas 62, 94, 126 y 158 sin pasarlos. Gradle reporta los errores de llamada en las líneas 67, 99, 131 y 163.

### Resultado de lint

| Issue | Cantidad | Tipo |
| --- | ---: | --- |
| `CredManMissingDal` | 1 | Error |
| `Typos` | 70 | Warning, principalmente certificados de fuentes |
| `UnusedResources` | 11 | Warning |
| `IconLocation` | 6 | Warning |
| `PluralsCandidate` | 6 | Warning |
| `ObsoleteSdkInt` | 3 | Warning |
| `UnknownIssueId` | 2 | Warning |
| `CredentialManagerMisuse` | 1 | Warning |
| `InsecureBaseConfiguration` | 1 | Warning |

La configuración intenta desactivar `ktlint:standard:function-naming` dentro de Android lint, que no reconoce ese issue ID. Los dos sistemas de análisis deben configurarse por separado.

Gradle también advierte que `-Xexplicit-backing-fields` ya es redundante con Kotlin 2.4.

## Estrategia de pruebas existente

### Unitarias JVM

- 86 archivos Kotlin y 258 métodos `@Test`.
- Cubren mappers, datasources, repositorios, casos de uso y ViewModels.
- Usan JUnit 4, MockK, coroutines-test, Turbine, Truth, Robolectric y Koin Test.
- `MainCoroutineRule` sustituye Main dispatcher.
- Varios tests crean módulos Koin específicos por clase.

### Instrumentación

- 18 archivos Kotlin y 43 métodos `@Test`.
- Prueban DataStore cifrado, SessionManager, módulos Koin y servicios Retrofit con MockWebServer.
- `MiraiLinkTestRunner` fuerza `MiraiLinkApp` como Application.
- Algunos asserts de rutas API están desactualizados frente a los servicios Retrofit actuales.

### E2E Compose

`AppE2ETest`:

- Acepta un banner de consentimiento por texto.
- Completa onboarding.
- Inicia sesión con credenciales de BuildConfig.
- Verifica Home.
- Hace swipe y like.

Estas pruebas dependen de UI de terceros, backend accesible, datos de cuenta y estado previo. No son herméticas y pueden modificar datos externos.

### Screenshot y journey

- Existe una preview screenshot para `HashtagChip` y una referencia PNG.
- El journey Kotzilla solo declara que la app inicia.
- La nota histórica del README sobre rutas largas no se verificó en esta ejecución.

## Checklist para cambios

### Contrato de backend

1. Cambia la interfaz Retrofit.
2. Ajusta request, response o DTO.
3. Ajusta datasource y manejo de error.
4. Ajusta mapper y repositorio.
5. Ajusta caso de uso y ViewModel.
6. Actualiza unit tests y MockWebServer tests con la ruta exacta.
7. Revisa AuthInterceptor si cambia el formato de errores.

### Pantalla nueva

1. Reutiliza atoms y molecules existentes.
2. Crea screen y ViewModel en el flujo apropiado.
3. Expón dependencias mediante casos de uso.
4. Registra ViewModel en `ViewModelModule`.
5. Añade una clave serializable en `AppScreen`.
6. Registra la entry en `NavWrapper`.
7. Añade strings en base, inglés, español y, si procede, values-v23.
8. Añade tests de estado y navegación relevantes.

### Persistencia

1. Cambia el modelo serializable con defaults compatibles.
2. Evalúa migración y corrupción de datos existentes.
3. Mantén el cifrado y las exclusiones de backup.
4. Añade instrumented tests, ya que Android Keystore requiere plataforma.

## Artefactos generados

- APK debug: `app/build/outputs/apk/debug/app-debug.apk`
- Informes lint: `app/build/reports/lint-results-debug.html` y `.sarif`
- Informe de problemas Gradle: `build/reports/problems/problems-report.html`

Todos están ignorados y no deben añadirse al commit documental.
