# Verificación Holo Mirai

[Spec](spec.md) | [Plan](plan.md) | [Tareas](tasks.md) | [Implementación y privacidad](implementation.md)

Fecha: 2026-10-05. Rama `codex/holo-mirai-parallax`, base `master` en `5fd9b7c`. Se mantiene el módulo Android, Koin, Coil 2 y las versiones existentes; se añade únicamente ML Kit Selfie Segmentation beta6. No hay cambios de contratos ni persistencia del backend.

## Evidencia y alcance

La compilación, las pruebas JVM y la instrumentación con servicios simulados no acreditan calidad visual de retratos reales, sensores físicos, latencia de segmentación ni FPS. No se conectaron móviles físicos durante esta entrega. Los objetivos de rendimiento y la aceptación visual física quedan pendientes; la PR se prepara como borrador, sin integración ni publicación.

Las pruebas de Compose se ejecutan en una `ComponentActivity` con imágenes generadas localmente, preferencia, sensores, segmentador, sesión y hápticos controlados. No se lanza `MainActivity` ni una suite E2E contra usuarios reales. Las FAQ usan su repositorio local y ViewModel reales. La aplicación de instrumentación inicializa sus servicios habituales de Koin y telemetría; esto no equivale a una ejecución totalmente libre de comunicaciones del SDK.

## Verificaciones realizadas

| Comprobación | Evidencia y límite |
| --- | --- |
| Build debug y compilación de instrumentación | Compilan. No constituyen revisión visual física. |
| Suite JVM | 492 pruebas, 0 fallos. Reglas de activación, calibración, límites, ausencia de sensor, matrices relativas a postura vertical, preferencias antiguas y escritura atómica, errores y reintento, máscaras inválidas y cobertura, limpieza de suscripciones. |
| Instrumentación Holo | 19 pruebas, 0 fallos, en API 35: 10 de gestos/presentación, 5 de procesamiento, 1 de políticas y 3 de localización. |
| Compose aislado | Swipe activo/desactivado, botones durante preparación, undo, navegación entre fotos, vista ampliada, arrastre cancelado, gesto vertical, latido, pausa/resume y recorte decorativo en ambos temas. El gesto vertical comprueba ausencia de voto; no acredita por sí solo el recorrido de scroll de una biografía larga. |
| Procesamiento Android | Cinco pruebas: clave por contenido/tamaño/generación, un único trabajo real bajo cancelación y consumidor pendiente descartado, fallos y presión de memoria, copia software <= 1.024 y propiedad del original, invocación del SDK incluido con un bitmap local sintético. Esta última permite fallback y no certifica reconocimiento de una persona. |
| Políticas del sistema | Opciones reales del emulador para animaciones deshabilitadas y ahorro de batería, restauradas en `finally`; lectura actual de la escala de animación y limpieza de receiver/observer. |
| FAQ y localización | Cinco preguntas en la categoría existente, recursos por referencia y 15 claves completas y únicas en `values`, `values-es`, `values-en`. Pruebas instrumentadas de FAQ ES/EN y cambio de idioma del ajuste. |
| Strings y accesibilidad | Sin `context.getString` ni `resources.getString` en el código nuevo de Holo. Una única descripción para la foto y un único interruptor accionable. TalkBack físico pendiente. |
| Offline | Las pruebas usan originales locales sin descargar perfiles. El modelo se incluye en la app; los originales remotos ausentes siguen necesitando la carga existente. Falta recorrido offline de producción con una caché real. |
| Lint | Ejecutado: 5 errores y 151 advertencias. Los errores globales preexistentes son `CredManMissingDal` en el manifiesto y `StringFormatInvalid` en `faq_google_play_billing_a`. Ambas fuentes también están en `master`. No hay errores Holo; dos advertencias nuevas sugieren wrappers KTX equivalentes para bitmaps. No se ocultan mediante baseline ni se inventa asociación con un dominio. |

## Entorno y comandos

Android CLI actualizado a `1.0.16500706`. El AVD inicial Pixel_10 con Android 17 preliminar fallaba antes de abrir Compose, por `InputManager.getInstance` en la librería de instrumentación existente. Se instaló la imagen estable Android 15/API 35 y creó `Holo_Mirai_API35`, iniciado sin ventana. La creación usó `avdmanager` porque el CLI no permite elegir imagen/API en su comando de creación.

La selección de clases separadas por coma no ejecutó la clase de procesamiento en este entorno. Para el resultado final se selecciona todo el paquete Holo y se cuentan las clases en el informe XML.

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:compileDebugAndroidTestKotlin :app:connectedDebugAndroidTest :app:lintDebug '-Pandroid.testInstrumentationRunnerArguments.package=com.feryaeljustice.mirailink.ui.holo' --continue --console=plain -x :app:kotzillaBuildReportDebug
```

La tarea de reporte remoto de Kotzilla se excluyó del pase final para no confundir datos históricos del servicio con medidas de esta feature. Un build previo sí ejecutó ese reporte: sus incidencias históricas no acreditan ni refutan el rendimiento de Holo.

Informes locales regenerables: `app/build/reports/tests/testDebugUnitTest/index.html`, `app/build/reports/androidTests/connected/debug/index.html`, `app/build/reports/lint-results-debug.html`; texto actual de lint en `app/build/intermediates/lint_intermediate_text_report/debug/lintReportDebug/lint-results-debug.txt`. No se versionan logs del dispositivo ni ficheros de configuración privada.

El comando agregado termina con error por lint; sus tareas de build, JVM, compilación de instrumentación e instrumentación sí completan correctamente, usando `--continue`.

## Capturas sintéticas revisadas

Se repitió únicamente `HoloGestureTest#segmentedLayerIsDecorativeAndClearedInBothThemes` mediante instrumentación directa después de instalar los APK sin abrir componentes de producción. Resultado: `OK (1 test)`. Se exportaron y revisaron visualmente las siguientes capturas: controles y texto siguen presentes y coherentes con el tema. El original y el recorte son bitmaps sólidos generados por el test; no representan un retrato ni validan calidad de segmentación, duplicados visuales de personas o movimiento de sensores reales.

- [Fixture clara](evidence/holo_light.png)
- [Fixture oscura](evidence/holo_dark.png)

## Aceptación pendiente antes de integrar o publicar

- Revisar retratos, cosplay, cabello fino, gafas, grupos, avatares anime, paisajes y fotos pequeñas en claro/oscuro. Comprobar ausencia de siluetas duplicadas y bordes vacíos, no solo ausencia de anuncios accesibles duplicados.
- Probar sensores reales en vertical y horizontal, remapeo, recalibración y ausencia de sensor compatible; pérdida de foco por otra ventana, navegación y liberación de recursos fuera de la pantalla.
- Validar scroll de biografía, carrusel, swipe, undo, fullscreen y sensación háptica en Home y cada feed de Explorar con Holo activo, apagado y preparando.
- Verificar preferencias después de reiniciar la app, carga offline desde la caché real, limpieza al logout/cambio de usuario/demo y TalkBack con tamaño de fuente grande.
- Comparar Holo activado y desactivado en dos móviles físicos, tres recorridos de 30 swipes por estado. Registrar modelo, Android, resolución, tema, temperatura, ahorro de batería, jank y frame p95. Criterios: menos de 2 puntos adicionales de jank y menos del 20 % de degradación de p95. No hay medición ni estimación de cumplimiento.
- Resolver los bloqueos globales de lint y revisar declaración de datos/consentimientos de los SDK antes de release. Fotos locales y telemetría del SDK son cuestiones distintas.
