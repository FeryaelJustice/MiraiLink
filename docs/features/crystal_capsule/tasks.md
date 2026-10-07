# Checklist de Tareas: Cápsula de Cristal v2 (Rediseño y Dinámica de Conexión)

- [x] **Fase 1: Capa de Dominio y Datos**
  - [x] Adaptar modelos de dominio en `CrystalCapsule.kt` (escala 0..4, soporte para pregunta activa por turnos e histórico de respuestas bilaterales).
  - [x] Actualizar `DemoCapsuleRepository.kt` para otorgar 1 punto solo tras respuesta bilateral a la misma pregunta y gestionar turnos estrictos (1 activa).
  - [x] Actualizar y verificar `DemoCapsuleRepositoryTest.kt` para validar la nueva regla de 4 puntos y turnos.

- [x] **Fase 2: Limpieza Visual de Fotos**
  - [x] Modificar `CrystalPhoto.kt` para retirar las líneas vectoriales de corte en el Canvas conservando el desenfoque suave progresivo.

- [x] **Fase 3: Componentes UI de la Cápsula**
  - [x] Crear componente `CrystalCapsuleIcon.kt` con estado progresivo (vacío en 0/4, llenado gradual y lleno en 4/4).
  - [x] Modificar `ChatTopBar.kt` para integrar el icono a la izquierda del botón de reportar con callback `onCapsuleClick`.
  - [x] Crear `CrystalCapsuleModal.kt` (`ModalBottomSheet` con pasos: Principal, Proponer Pregunta e Historial).
  - [x] Implementar tarjetas explicativas para las acciones del footer (pausar, abandonar, solicitar revelar).

- [x] **Fase 4: Integración en ChatScreen y ChatViewModel**
  - [x] Eliminar `CapsulePanel` fijo de la cabecera en `ChatScreen.kt` y retirar el archivo obsoleto.
  - [x] Conectar la apertura de `CrystalCapsuleModal` al pulsar el icono del top bar.
  - [x] Bloquear controles de chat convencional y ruleta de gestos mientras la cápsula esté activa con indicación clara.
  - [x] Implementar el banner estático de felicitación tras completar la cápsula (4/4) persistente hasta el envío del primer mensaje.
  - [x] Actualizar `ChatViewModel.kt` para gestionar la lógica de preguntas y la persistencia del banner.

- [x] **Fase 5: Verificación y Testing**
  - [x] Ejecutar suite de pruebas unitarias (`./gradlew.bat testDebugUnitTest`).
  - [x] Verificar compilación limpia (`./gradlew.bat assembleDebug`).
  - [x] Validación funcional del flujo completo de preguntas, desbloqueo y chat.

- [x] **Fase 6: Detección Automática de Idioma y Localización**
  - [x] Configurar cabeceras de red HTTP `Accept-Language` y `X-Language` en OkHttpClient (`NetworkModule.kt`).
  - [x] Adaptar `CapsuleAction` con campos opcionales `questionId`, `customQuestion` y `language`.
  - [x] Enriquecer `CapsuleQuestion` y `CompletedCapsuleQuestion` con campos de idioma, `localizedText` y helper `displayText()`.
  - [x] Adaptar `CrystalCapsuleModal.kt` para enviar `questionId` y mostrar texto localizado automáticamente según el idioma del sistema sin selector manual.
  - [x] Actualizar `DemoCapsuleRepository.kt` y suite de pruebas unitarias (`DemoCapsuleRepositoryTest.kt`) validando el flujo multilingüe.

