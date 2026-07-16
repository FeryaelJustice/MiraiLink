# Estrategia de testing de MiraiLink

Este documento es la entrada canónica para crear, revisar y mantener pruebas en MiraiLink. La estrategia se adaptó del proyecto de referencia MiCursoTestingAndroidAppcademy, analizado en modo estricto de solo lectura el 2026-07-16, y se contrastó con la documentación oficial de Android mediante Android CLI.

## Objetivo

La suite debe detectar regresiones de lógica, contratos de datos, estados de presentación y comportamiento visible sin depender innecesariamente de red, Firebase, credenciales reales o estado previo del dispositivo.

La distribución sigue una pirámide:

1. Muchos tests unitarios JVM para funciones puras, casos de uso, repositorios, mappers y ViewModels.
2. Menos tests instrumentados para APIs Android, DataStore cifrado, Koin, Retrofit y Compose.
3. Pocos E2E para recorridos completos que realmente necesitan la aplicación integrada.

## Tipos de test

| Tipo | Ubicación | Responsabilidad | Dobles preferidos |
| --- | --- | --- | --- |
| Unitario puro | app/src/test | Utilidades, mappers, contratos, casos de uso | Fakes o MockK |
| ViewModel | app/src/test | Secuencias de estado, errores tipados, reintentos, efectos | Casos de uso simulados, Turbine |
| Robolectric | app/src/test | Adaptadores Android pequeños sin dispositivo | Shadows de Android |
| Integración instrumentada | app/src/androidTest | DataStore, Retrofit, Koin y límites Android | MockWebServer, módulos Koin de test |
| Compose aislado | app/src/androidTest | Renderizado de pantalla y una interacción relevante | ViewModel y sesión controlados |
| E2E | app/src/androidTest | Navegación y recorrido completo | Servicios de test explícitos |

## Infraestructura compartida

app/src/sharedTest/kotlin se compila tanto en test como en androidTest.

### MainCoroutineRule

- Instala un Dispatchers.Main determinista.
- Expone testDispatcher para trabajo encolado.
- Expone testDispatcherUnconfined solo cuando el arranque inmediato sea parte del contrato.
- Ambos usan el mismo TestCoroutineScheduler.
- Los tests que crean un ViewModel antes o durante runTest deben usar runTest(mainCoroutineRule.scheduler).
- advanceUntilIdle, runCurrent y advanceTimeBy deben actuar sobre ese scheduler compartido.

No se debe crear un dispatcher con otro scheduler dentro del mismo test.

### ScreenTestSupport

- setMiraiLinkContent renderiza la pantalla con el tema de producción y color dinámico desactivado.
- testSession crea una sesión relajada con StateFlow reales para identidad, autenticación, verificación y foto.
- Las pantallas reciben el ViewModel por su parámetro público. No se inicia Koin ni MainActivity en tests aislados.

## Convenciones

Cada test nuevo debe:

- Usar Given, When y Then cuando haya preparación real.
- Tener un nombre que describa comportamiento y resultado.
- Documentar la clase y cada método.
- Probar comportamiento observable, no detalles privados.
- Usar Truth para aserciones de valores.
- Usar Turbine para secuencias de Flow.
- Verificar interacciones solo cuando la delegación sea el contrato.
- Incluir éxito, error y cancelación en límites asíncronos cuando proceda.
- Comprobar la acción de recuperación en ViewModels que heredan de RetryableViewModel.
- Mantener datos, tiempo, zona horaria y locale deterministas.
- Evitar Thread.sleep, red real, Firebase real y credenciales reales.

## Compose

Cada pantalla debe tener al menos:

- Un test de composición con estado estable.
- Un test de su interacción primaria o navegación.
- Un test de error si la pantalla representa un UiError.
- Dobles con MutableStateFlow reales para toda propiedad recogida por Compose.
- createAndroidComposeRule<ComponentActivity>() desde androidx.compose.ui.test.junit4.v2.

Los tests de pantalla no deben iniciar toda la aplicación. Los recorridos completos permanecen en AppE2ETest.

## Red y persistencia

- Las interfaces Retrofit se prueban con MockWebServer y rutas exactas.
- Los datasources prueban códigos HTTP, serialización y clasificación de error.
- Los repositorios prueban mapeo y propagación de MiraiLinkResult.
- DataStore cifrado y Android Keystore se prueban en instrumentación.
- No se acepta una prueba contra el backend real como sustituto de un contrato hermético.

## APIs difíciles de aislar

CredentialHelper construye internamente CredentialManager y puede abrir UI del proveedor. Su comportamiento se cubre indirectamente desde AuthViewModel con un doble. Para probar el adaptador en aislamiento habría que inyectar una interfaz de credenciales. No se fuerza un test automático que pueda abrir UI o depender de credenciales del dispositivo.

applyTelemetryConsent accede a singletons estáticos de Firebase. Debe verificarse en un proyecto Firebase de test o después de introducir adaptadores inyectables. Los ViewModels y trackers consumidores sí deben usar dobles.

Estas exclusiones son deliberadas: un test no hermético que siempre pasa o bloquea el dispositivo no aporta cobertura real.

## Comandos

    # Unitarios y Robolectric
    .\gradlew.bat testDebugUnitTest

    # Compilar instrumentación
    .\gradlew.bat compileDebugAndroidTestKotlin

    # Instrumentados con emulador o dispositivo
    .\gradlew.bat connectedDebugAndroidTest

    # Screenshot
    .\gradlew.bat validateDebugScreenshotTest

Los tests añadidos en codex/comprehensive-testing no se ejecutaron ni compilaron por instrucción expresa del propietario. Solo se realizó revisión estática, inventario de cobertura y git diff check.

## Fuentes oficiales consultadas

- [Test strategy](https://developer.android.com/training/testing/fundamentals/strategies)
- [What to test](https://developer.android.com/training/testing/fundamentals/what-to-test)
- [Test doubles](https://developer.android.com/training/testing/fundamentals/test-doubles)
- [Testing Kotlin coroutines](https://developer.android.com/kotlin/coroutines/test)
- [Common patterns in Compose testing](https://developer.android.com/develop/ui/compose/testing/common-patterns)
