# [APROBADO] Especificacion Funcional: Ruleta de Gestos en Vivo (Minijuego Visual para Romper el Hielo)

- **Fecha**: 2026-10-04
- **Estado**: [APROBADO]
- **Modulo Afectado**: `:app` (`com.feryaeljustice.mirailink`)
- **Rama Git**: `feature/gesture-roulette`

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**: En muchas ocasiones las parejas en match no saben como iniciar la conversacion o el chat se estanca tras los primeros saludos ("hola, que tal"). La falta de estimulo visual o dinamico genera abandono temprano de la conversacion.
- **Objetivo**: Proveer un minijuego interactivo de 15 segundos ("Desafio de Reaccion") accesible directamente dentro de la pantalla de chat (`ChatScreen`), donde un usuario invita a su match a replicar una bateria de 4 gestos faciales rapidos (guiño izquierdo, sonrisa sorpresa, guiño derecho, ladeo de cabeza) detectados 100% en local con CameraX y ML Kit Face Detection, culminando en una animacion de confeti en Compose y una tarjeta de compatibilidad divertida compartible en el chat.

- - -

## 2. Situacion Actual

- `ChatScreen` opera actualmente mediante intercambio de mensajes de texto y emojis usando llamadas REST con polling periodico cada 3 segundos.
- Aunque existe `SocketService.kt` en la capa de datos de Android, el backend (`MiraiLink-Backend`) es exclusivamente un servidor Express REST sin servidor Socket.IO activo.
- El proyecto ya dispone de:
  - `libs.versions.toml` con CameraX 1.6.2 y ML Kit Face Detection 16.1.7.
  - `FaceDetectorDataSource.kt` con deteccion de rostro, orientacion Euler (X, Y, Z) y probabilidades de sonrisa (`smilingProbability`) y apertura ocular (`leftEyeOpenProbability`, `rightEyeOpenProbability`).
  - `CameraPreviewView.kt` con enlace seguro de `ProcessCameraProvider` y ciclo de vida Compose.
  - Permiso `android.permission.CAMERA` registrado en `AndroidManifest.xml`.
- No existe ningun minijuego ni componente de confeti ni sistema de invitaciones interactivas dentro de las conversaciones de chat.

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)
- **Implementacion 100% en Android sin cambios en backend**: Se utiliza el canal de mensajes REST/local existente para transmitir invitaciones y resultados. Funciona de manera identica en modo Online y modo Demo Offline.
- **Boton de Accion en Chat**: Icono tematico de dado/juego en la barra de chat para lanzar o invitar a la Ruleta de Gestos.
- **Burbuja Interactiva de Invitacion**: Mensaje de chat especial tipo tarjeta interactiva ("🎯 ¡Te reto a la Ruleta de Gestos!") con boton visible "Aceptar reto" o "Jugar".
- **Tarjeta Modal Interactiva de Minijuego**:
  - Modal centrado superpuesto sobre el chat con vista previa de camara frontal en vivo.
  - Temporizador circular o barra de cuenta atras de 15 segundos.
  - Bateria fija de 4 gestos secuenciales (ej. Guiño Izquierdo -> Sonrisa Amplia -> Guiño Derecho -> Ladeo de Cabeza).
  - Evaluacion en tiempo real frame a frame con CameraX `ImageAnalysis` y `FaceDetectorDataSource` local.
  - Feedback visual instantaneo (check verde, pulsacion haptica del dispositivo) al validar cada gesto antes de pasar al siguiente.
- **Celebracion y Generacion de Compatibilidad**:
  - Animacion de confeti en Compose disparada al completar los 4 gestos o al concluir los 15 segundos con exito.
  - Tarjeta de compatibilidad y chispa generada dinamicamente (ej. "Reflejos Relampago: 4/4 en 8.2s - Chispa de Quimica: 98%").
  - Boton de accion para compartir el resultado en la conversacion activa.
- **Privacidad Absoluta**:
  - Cero transmision de imagenes o video. Todo el procesamiento de biometria facial ocurre y se descarta en la memoria RAM del dispositivo del usuario.

### 3.2. Fuera del Alcance (Out of Scope)
- Modificaciones a nivel de base de datos PostgreSQL en el backend de Node.js.
- Streaming o retransmision P2P de video (WebRTC).
- Minijuegos multijugador simultaneos en pantalla dividida en tiempo real extremo.

- - -

## 4. Casuisticas y Comportamiento Mobile

- **Comportamiento en Modo Online vs Modo Offline Demo**:
  - En ambos modos, el minijuego de camara y deteccion de gestos corre de forma autonoma y local en el dispositivo.
  - Las tarjetas de invitacion y de resultado se envian mediante el flujo estandar de `sendMessage`, persistiendo correctamente en Room (modo Demo) o en la API REST (modo Online).
- **Ciclo de Vida y Recuperacion de Estado**:
  - Si la aplicacion pasa a segundo plano durante el minijuego, la camara se desvincula de inmediato liberando el sensor de hardware (`ProcessCameraProvider.unbindAll()`).
  - Al regresar o rotar pantalla, el juego se gestiona de forma segura mediante estado en ViewModel sin provocar fugas ni cierres forzados.
- **Gestion de Permisos de Camara**:
  - Verificacion de `Manifest.permission.CAMERA` antes de abrir el modal del minijuego.
  - En caso de no estar otorgado, se lanza el launcher de permisos nativo de Compose con mensaje explicativo.
- **Ergonomia, Teclado y Accesibilidad**:
  - Controles tactiles con tamaño minimo de 48 x 48 dp.
  - Boton visible de cierre rapido ("X") para descartar el modal en cualquier instante.
  - Soporte fluido para Light y Dark Theme Material 3.

- - -

## 5. Criterios de Aceptacion (Given - When - Then)

### Criterio 1: Lanzamiento e Invitacion al Desafio
- **Dado que**: El usuario se encuentra en `ChatScreen` con un match.
- **Cuando**: Presiona el boton de minijuego en la barra de chat.
- **Entonces**: Se envia un mensaje interactivo de reto que se renderiza como tarjeta de invitacion con boton para jugar.

### Criterio 2: Deteccion Exitosa de los 4 Gestos
- **Dado que**: El usuario abre la tarjeta modal interactiva del minijuego y arranca el temporizador de 15 segundos.
- **Cuando**: Realiza consecutivamente frente a la camara los gestos solicitados (guiño izquierdo, sonrisa, guiño derecho, ladeo).
- **Entonces**: Cada gesto es detectado en menos de 300ms por ML Kit local, disparando vibracion haptica y avanzando al siguiente gesto de la bateria.

### Criterio 3: Celebracion con Confeti y Tarjeta de Resultado
- **Dado que**: El usuario completa los 4 gestos (o expira el tiempo).
- **Cuando**: Finaliza la ronda de juego.
- **Entonces**: Se detiene el procesamiento de la camara, se activa una animacion de confeti en Compose, y se muestra la tarjeta de compatibilidad calculada con boton "Compartir en chat".

### Criterio 4: Manejo de Permiso de Camara Denegado
- **Dado que**: El usuario no tiene concedido el permiso `android.permission.CAMERA`.
- **Cuando**: Intenta abrir el minijuego.
- **Entonces**: Se solicita el permiso mediante el sistema sin cierres forzados ni pantallas en negro.

- - -

## 6. Decisiones Resueltas

- [x] **Alcance Backend vs Android**: 100% en Android sin cambios en backend. Las invitaciones y resultados se envian a traves de los mensajes estandar del chat con un formato estructurado amigable y compatible con el modo offline demo.
- [x] **Mecanica de Juego**: Bateria fija de 4 gestos variados con objetivo de completarlos dentro del limite de 15 segundos.
- [x] **Formato Visual**: Tarjeta modal interactiva centrada sobre el chat, combinando el visor de camara frontal en vivo, temporizador y tarjetas de gestos claros.

- - -

## 7. Localizacion, FAQs e Instrucciones en la Herramienta

- **Localizacion Completa**:
  - Todas las cadenas de texto del minijuego (instrucciones, prompts de gestos, estados de tarjetas, compatibilidad y FAQs) residen en `res/values/strings.xml`, `res/values-es/strings.xml` y `res/values-en/strings.xml`.
- **Instrucciones en la Propia Herramienta**:
  - La tarjeta modal interactiva (`GestureChallengeModal.kt`) incluye un boton de informacion ("?") en la cabecera que despliega un dialogo explicativo con el paso a paso del reto y garantias de privacidad.
- **Centro de Ayuda y FAQs**:
  - Incorporadas 2 nuevas entradas en `FaqRepositoryImpl.kt` bajo la categoria dedicada `FaqCategory.GESTURE_ROULETTE`:
    - `faq_gesture_roulette`: Explicacion del minijuego y dinamica para romper el hielo.
    - `faq_gesture_roulette_privacy`: Garantia de privacidad y procesamiento 100% local en el dispositivo.

