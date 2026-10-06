# [APROBADO] Especificación Funcional: Ruleta de Gestos en Vivo (Minijuego Visual para Romper el Hielo)

- **Fecha**: 2026-10-04
- **Estado**: [APROBADO]
- **Módulo Afectado**: `:app` (`com.feryaeljustice.mirailink`)
- **Rama Git**: `feature/gesture-roulette`

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**: En muchas ocasiones las parejas en match no saben como iniciar la conversación o el chat se estanca tras los primeros saludos ("hola, que tal"). La falta de estimulo visual o dinámico genera abandono temprano de la conversación.
- **Objetivo**: Proveer un minijuego interactivo de 15 segundos ("Desafío de Reacción") accesible directamente dentro de la pantalla de chat (`ChatScreen`), donde un usuario invita a su match a replicar una batería de 4 gestos faciales rápidos (guiño izquierdo, sonrisa sorpresa, guiño derecho, ladeo de cabeza) detectados 100% en local con CameraX y ML Kit Face Detection, culminando en una animación de confeti en Compose y una tarjeta de compatibilidad divertida compartible en el chat.

- - -

## 2. Situación Actual

- `ChatScreen` opera actualmente mediante intercambio de mensajes de texto y emojis usando llamadas REST con polling periódico cada 3 segundos.
- Aunque existe `SocketService.kt` en la capa de datos de Android, el backend (`MiraiLink-Backend`) es exclusivamente un servidor Express REST sin servidor Socket.IO activo.
- El proyecto ya dispone de:
  - `libs.versions.toml` con CameraX 1.6.2 y ML Kit Face Detection 16.1.7.
  - `FaceDetectorDataSource.kt` con detección de rostro, orientación Euler (X, Y, Z) y probabilidades de sonrisa (`smilingProbability`) y apertura ocular (`leftEyeOpenProbability`, `rightEyeOpenProbability`).
  - `CameraPreviewView.kt` con enlace seguro de `ProcessCameraProvider` y ciclo de vida Compose.
  - Permiso `android.permission.CAMERA` registrado en `AndroidManifest.xml`.
- No existe ningún minijuego ni componente de confeti ni sistema de invitaciones interactivas dentro de las conversaciones de chat.

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)
- **Implementación 100% en Android sin cambios en backend**: Se utiliza el canal de mensajes REST/local existente para transmitir invitaciones y resultados. Funciona de manera idéntica en modo Online y modo Demo Offline.
- **Botón de Acción en Chat**: Icono temático de dado/juego en la barra de chat para lanzar o invitar a la Ruleta de Gestos.
- **Burbuja Interactiva de Invitación**: Mensaje de chat especial tipo tarjeta interactiva ("🎯 ¡Te reto a la Ruleta de Gestos!") con botón visible "Aceptar reto" o "Jugar".
- **Tarjeta Modal Interactiva de Minijuego**:
  - Modal centrado superpuesto sobre el chat con vista previa de cámara frontal en vivo.
  - Temporizador circular o barra de cuenta atrás de 15 segundos.
  - Batería fija de 4 gestos secuenciales (ej. Guiño Izquierdo -> Sonrisa Amplia -> Guiño Derecho -> Ladeo de Cabeza).
  - Evaluación en tiempo real frame a frame con CameraX `ImageAnalysis` y `FaceDetectorDataSource` local.
  - Feedback visual instantáneo (check verde, pulsación háptica del dispositivo) al validar cada gesto antes de pasar al siguiente.
- **Celebración y Generación de Compatibilidad**:
  - Animación de confeti en Compose disparada al completar los 4 gestos o al concluir los 15 segundos con éxito.
  - Tarjeta de compatibilidad y chispa generada dinámicamente (ej. "Reflejos Relampago: 4/4 en 8.2s - Chispa de Química: 98%").
  - Botón de acción para compartir el resultado en la conversación activa.
- **Privacidad Absoluta**:
  - Cero transmisión de imágenes o video. Todo el procesamiento de biometria facial ocurre y se descarta en la memoria RAM del dispositivo del usuario.

### 3.2. Fuera del Alcance (Out of Scope)
- Modificaciones a nivel de base de datos PostgreSQL en el backend de Node.js.
- Streaming o retransmisión P2P de video (WebRTC).
- Minijuegos multijugador simultaneos en pantalla dividida en tiempo real extremo.

- - -

## 4. Casuísticas y Comportamiento Mobile

- **Comportamiento en Modo Online vs Modo Offline Demo**:
  - En ambos modos, el minijuego de cámara y detección de gestos corre de forma autónoma y local en el dispositivo.
  - Las tarjetas de invitación y de resultado se envían mediante el flujo estándar de `sendMessage`, persistiendo correctamente en Room (modo Demo) o en la API REST (modo Online).
- **Ciclo de Vida y Recuperación de Estado**:
  - Si la aplicación pasa a segundo plano durante el minijuego, la cámara se desvincula de inmediato liberando el sensor de hardware (`ProcessCameraProvider.unbindAll()`).
  - Al regresar o rotar pantalla, el juego se gestiona de forma segura mediante estado en ViewModel sin provocar fugas ni cierres forzados.
- **Gestión de Permisos de Cámara**:
  - Verificación de `Manifest.permission.CAMERA` antes de abrir el modal del minijuego.
  - En caso de no estar otorgado, se lanza el launcher de permisos nativo de Compose con mensaje explicativo.
- **Ergonomía, Teclado y Accesibilidad**:
  - Controles táctiles con tamaño mínimo de 48 x 48 dp.
  - Botón visible de cierre rápido ("X") para descartar el modal en cualquier instante.
  - Soporte fluido para Light y Dark Theme Material 3.

- - -

## 5. Criterios de Aceptación (Given - When - Then)

### Criterio 1: Lanzamiento e Invitación al Desafío
- **Dado que**: El usuario se encuentra en `ChatScreen` con un match.
- **Cuando**: Presiona el botón de minijuego en la barra de chat.
- **Entonces**: Se envía un mensaje interactivo de reto que se renderiza como tarjeta de invitación con botón para jugar.

### Criterio 2: Detección Exitosa de los 4 Gestos
- **Dado que**: El usuario abre la tarjeta modal interactiva del minijuego y arranca el temporizador de 15 segundos.
- **Cuando**: Realiza consecutivamente frente a la cámara los gestos solicitados (guiño izquierdo, sonrisa, guiño derecho, ladeo).
- **Entonces**: Cada gesto es detectado en menos de 300ms por ML Kit local, disparando vibración háptica y avanzando al siguiente gesto de la batería.

### Criterio 3: Celebración con Confeti y Tarjeta de Resultado
- **Dado que**: El usuario completa los 4 gestos (o expira el tiempo).
- **Cuando**: Finaliza la ronda de juego.
- **Entonces**: Se detiene el procesamiento de la cámara, se activa una animación de confeti en Compose, y se muestra la tarjeta de compatibilidad calculada con botón "Compartir en chat".

### Criterio 4: Manejo de Permiso de Cámara Denegado
- **Dado que**: El usuario no tiene concedido el permiso `android.permission.CAMERA`.
- **Cuando**: Intenta abrir el minijuego.
- **Entonces**: Se solicita el permiso mediante el sistema sin cierres forzados ni pantallas en negro.

- - -

## 6. Decisiones Resueltas

- [x] **Alcance Backend vs Android**: 100% en Android sin cambios en backend. Las invitaciones y resultados se envían a través de los mensajes estándar del chat con un formato estructurado amigable y compatible con el modo offline demo.
- [x] **Mecánica de Juego**: Batería fija de 4 gestos variados con objetivo de completarlos dentro del límite de 15 segundos.
- [x] **Formato Visual**: Tarjeta modal interactiva centrada sobre el chat, combinando el visor de cámara frontal en vivo, temporizador y tarjetas de gestos claros.

- - -

## 7. Localización, FAQs e Instrucciones en la Herramienta

- **Localización Completa**:
  - Todas las cadenas de texto del minijuego (instrucciones, prompts de gestos, estados de tarjetas, compatibilidad y FAQs) residen en `res/values/strings.xml`, `res/values-es/strings.xml` y `res/values-en/strings.xml`.
- **Instrucciones en la Propia Herramienta**:
  - La tarjeta modal interactiva (`GestureChallengeModal.kt`) incluye un botón de información ("?") en la cabecera que despliega un diálogo explicativo con el paso a paso del reto y garantías de privacidad.
- **Centro de Ayuda y FAQs**:
  - Incorporadas 2 nuevas entradas en `FaqRepositoryImpl.kt` bajo la categoría dedicada `FaqCategory.GESTURE_ROULETTE`:
    - `faq_gesture_roulette`: Explicación del minijuego y dinámica para romper el hielo.
    - `faq_gesture_roulette_privacy`: Garantía de privacidad y procesamiento 100% local en el dispositivo.

