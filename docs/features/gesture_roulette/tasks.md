# Checklist de Tareas: Ruleta de Gestos en Vivo (Desafio de Reaccion)

- [x] **Fase 1: Capa de Dominio & Modelos**
  - [x] Crear modelos `FaceGestureType`, `GestureChallengeState`, `GestureChallengeSummary` en `domain/model/chat/gesture/`
  - [x] Crear parser/formateador de mensajes `GestureMessagePayload` en `domain/model/chat/gesture/`
  - [x] Implementar `EvaluateFaceGestureUseCase` en `domain/usecase/chat/gesture/`
  - [x] Implementar `CalculateGestureCompatibilityUseCase` en `domain/usecase/chat/gesture/`
  - [x] Escribir tests unitarios para los casos de uso y parser de mensajes (`GestureRouletteTest.kt`)

- [x] **Fase 2: Capa de Datos & Vision con CameraX**
  - [x] Crear `GesturePhotoAnalyzer` en `data/studio/` para consumir frames de CameraX con `FaceDetectorDataSource`
  - [x] Registrar dependencias necesarias en modulos Koin (`UseCaseModule.kt`)

- [x] **Fase 3: Capa de Presentacion (Componentes UI y Animacion de Confeti)**
  - [x] Implementar animacion de confeti en Compose `ConfettiCelebration.kt`
  - [x] Implementar tarjeta modal interactiva `GestureChallengeModal.kt` con visor de camara frontal y temporizador de 15s
  - [x] Implementar tarjeta de resultado de compatibilidad `GestureResultCard.kt`
  - [x] Implementar tarjeta visual de mensaje en chat `GestureChallengeMessageCard.kt`

- [x] **Fase 4: Integracion en ChatViewModel y ChatScreen**
  - [x] Incorporar metodos de envio de reto y resultado (`sendGestureChallengeInvite`, `sendGestureChallengeResult`) en `ChatViewModel.kt`
  - [x] Añadir boton de minijuego, manejo de permisos de camara y overlays en `ChatScreen.kt`
  - [x] Vincular apertura del modal desde boton de chat y desde tarjetas de invitacion

- [x] **Fase 5: Verificacion y Calidad**
  - [x] Ejecutar tests unitarios (`.\gradlew.bat testDebugUnitTest` - 100% exitoso)
  - [x] Compilacion limpia del proyecto (`.\gradlew.bat assembleDebug` - BUILD SUCCESSFUL)

- [x] **Fase 6: Localizacion, FAQs e Instrucciones en la Herramienta**
  - [x] Extraer y localizar todos los textos de la feature en `values/strings.xml`, `values-es/strings.xml` y `values-en/strings.xml`
  - [x] Añadir dialog de ayuda e instrucciones paso a paso directamente en el modal de la ruleta (`GestureChallengeModal.kt`)
  - [x] Integrar categoria y preguntas frecuentes (FAQs) en `FaqCategory.kt`, `FaqRepositoryImpl.kt` (`GESTURE_ROULETTE`)
  - [x] Documentar la seccion de la feature y su funcionamiento en `docs/guia-de-funcionamiento-app.md`
  - [x] Actualizar especificacion SDMD `docs/features/gesture_roulette/spec.md`

