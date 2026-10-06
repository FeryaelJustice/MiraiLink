# Checklist de Tareas: Haptic Heartbeat (Radar de Sincronía Táctil y Sensorial)

- [x] **Fase 1: Permisos y Capa de Hardware Háptico**
  - [x] Declarar `<uses-permission android:name="android.permission.VIBRATE" />` en `app/src/main/AndroidManifest.xml`
  - [x] Crear la interfaz `HapticHeartbeatController` e implementación `HapticHeartbeatControllerImpl` en `com.feryaeljustice.mirailink.ui.haptics`
  - [x] Registrar `HapticHeartbeatController` en `di/koin/AppModule.kt`
  - [x] Crear pruebas unitarias para `HapticHeartbeatController` en `app/src/test/`
  - [x] Verificar compilación limpia con `./gradlew assembleDebug`

- [x] **Fase 2: Capa de Dominio y Algoritmo de Sincronía**
  - [x] Crear el modelo inmutable `HeartbeatAffinity` en `domain/model/haptics/`
  - [x] Implementar `CalculateHeartbeatAffinityUseCase` en `domain/usecase/haptics/`
  - [x] Registrar `CalculateHeartbeatAffinityUseCase` en `di/koin/UseCaseModule.kt`
  - [x] Escribir y validar pruebas unitarias `CalculateHeartbeatAffinityUseCaseTest` con JUnit 4
  - [x] Ejecutar `./gradlew testDebugUnitTest`

- [x] **Fase 3: Capa Visual Sensorial (Canvas Overlay & Animaciones)**
  - [x] Implementar el componente Compose `HapticHeartbeatOverlay` con Canvas en `ui/components/haptics/`
  - [x] Sincronizar el ritmo de animación de ondas concéntricas de neón (rosa/magenta y cian/violeta) con los BPM de afinidad
  - [x] Incluir el anillo de progreso circular para el umbral de 1.2s y la visualización de porcentaje de sincronía
  - [x] Garantizar la cancelación limpia de recursos en `onDispose` y ciclo de vida

- [x] **Fase 4: Integración en Componentes de Swipe y Pantallas**
  - [x] Actualizar `UserSwipeCardStack.kt` para dotar al botón de Like del gesto de pulsación prolongada (tap instantáneo vs hold radar >= 1.2s)
  - [x] Integrar `HapticHeartbeatOverlay` y calculo de afinidad en `HomeScreen.kt` y `CategoryFeedScreen.kt`
  - [x] Actualizar `UserProfileDetailScreen.kt` para incorporar la experiencia háptica y visual en su botón de Like
  - [x] Verificar compilación de la UI con `./gradlew assembleDebug`

- [x] **Fase 5: Verificación y Cierre de Calidad**
  - [x] Ejecutar suite completa de tests unitarios: `./gradlew testDebugUnitTest`
  - [x] Validar estilo de código y linter: `./gradlew ktlintCheck` o linter del proyecto
  - [x] Verificación de compilación final: `./gradlew assembleDebug`
