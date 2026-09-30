# Checklist de Tareas: Haptic Heartbeat (Radar de Sincronia Tactil y Sensorial)

- [x] **Fase 1: Permisos y Capa de Hardware Haptico**
  - [x] Declarar `<uses-permission android:name="android.permission.VIBRATE" />` en `app/src/main/AndroidManifest.xml`
  - [x] Crear la interfaz `HapticHeartbeatController` e implementacion `HapticHeartbeatControllerImpl` en `com.feryaeljustice.mirailink.ui.haptics`
  - [x] Registrar `HapticHeartbeatController` en `di/koin/AppModule.kt`
  - [x] Crear pruebas unitarias para `HapticHeartbeatController` en `app/src/test/`
  - [x] Verificar compilacion limpia con `./gradlew assembleDebug`

- [x] **Fase 2: Capa de Dominio y Algoritmo de Sincronia**
  - [x] Crear el modelo inmutable `HeartbeatAffinity` en `domain/model/haptics/`
  - [x] Implementar `CalculateHeartbeatAffinityUseCase` en `domain/usecase/haptics/`
  - [x] Registrar `CalculateHeartbeatAffinityUseCase` en `di/koin/UseCaseModule.kt`
  - [x] Escribir y validar pruebas unitarias `CalculateHeartbeatAffinityUseCaseTest` con JUnit 4
  - [x] Ejecutar `./gradlew testDebugUnitTest`

- [x] **Fase 3: Capa Visual Sensorial (Canvas Overlay & Animaciones)**
  - [x] Implementar el componente Compose `HapticHeartbeatOverlay` con Canvas en `ui/components/haptics/`
  - [x] Sincronizar el ritmo de animacion de ondas concentricas de neon (rosa/magenta y cian/violeta) con los BPM de afinidad
  - [x] Incluir el anillo de progreso circular para el umbral de 1.2s y la visualizacion de porcentaje de sincronia
  - [x] Garantizar la cancelacion limpia de recursos en `onDispose` y ciclo de vida

- [x] **Fase 4: Integracion en Componentes de Swipe y Pantallas**
  - [x] Actualizar `UserSwipeCardStack.kt` para dotar al boton de Like del gesto de pulsacion prolongada (tap instantaneo vs hold radar >= 1.2s)
  - [x] Integrar `HapticHeartbeatOverlay` y calculo de afinidad en `HomeScreen.kt` y `CategoryFeedScreen.kt`
  - [x] Actualizar `UserProfileDetailScreen.kt` para incorporar la experiencia haptica y visual en su boton de Like
  - [x] Verificar compilacion de la UI con `./gradlew assembleDebug`

- [x] **Fase 5: Verificacion y Cierre de Calidad**
  - [x] Ejecutar suite completa de tests unitarios: `./gradlew testDebugUnitTest`
  - [x] Validar estilo de codigo y linter: `./gradlew ktlintCheck` o linter del proyecto
  - [x] Verificacion de compilacion final: `./gradlew assembleDebug`
