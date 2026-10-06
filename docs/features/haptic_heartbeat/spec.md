# [APROBADO] Especificación Funcional: Haptic Heartbeat (Radar de Sincronía Táctil y Sensorial)

- **Fecha**: 2026-09-30
- **Estado**: [APROBADO]
- **Autor / Responsable**: Pair Programming (Antigravity & User)
- **Módulo Afectado**: `:app` (`com.feryaeljustice.mirailink`)

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**: En las aplicaciones sociales y de citas convencionales, evaluar un perfil y hacer swipe o pulsar like es una acción puramente mecánica, distante y fria que no transmite sensación física ni emoción real de conexión.
- **Objetivo**: Implementar "Haptic Heartbeat", una experiencia multisensorial interactiva donde el usuario, al mantener pulsado el botón de "Me Gusta" de un perfil, siente a través del motor háptico del dispositivo un patrón de latidos cardiacos orgánicos modulados en ritmo e intensidad según el grado de afinidad o gustos en común (animes, videojuegos y preferencias), acompanado de ondas concéntricas de neón proyectadas en Canvas sobre un fondo atenuado que palpitan al mismo compas. Al mantener la pulsación durante al menos 1.2 segundos y soltar, se confirma y envía el Like de forma inmersiva.

- - -

## 2. Situación Actual

- En la arquitectura actual (`UserSwipeCardStack`, `HomeScreen`, `CategoryFeedScreen` y `UserProfileDetailScreen`), el botón de like ejecuta un tap estándar que dispara `onLike` / `onSwipeRight`.
- En `UserCard` / `PublicUserCard`, la pulsación prolongada sobre las fotos está reservada para la previsualización de imagen completa (`FullscreenImagePreview`), por lo que el disparador del pulso cardíaco se ubica limpiamente en el botón de Like.
- No existe ningún servicio o utilidad centralizada para emitir patrones hápticos orgánicos con `Vibrator` / `VibratorManager` ni waveforms moduladas.
- En `AndroidManifest.xml` no se encuentra declarado el permiso `android.permission.VIBRATE`.
- Los perfiles exponen listas de intereses (`games`, `animes`, `relationshipGoals`, etc.), pero no existe un algoritmo local de afinidad o sincronía que compare cuantitativamente los intereses del perfil visible con los del usuario conectado (`currentUser`).

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)
- **Declaración de permisos**: Añadir `<uses-permission android:name="android.permission.VIBRATE" />` en `AndroidManifest.xml`.
- **Motor Háptico (`HapticHeartbeatController`)**:
  - Soporte de `VibratorManager` (Android 12+ / API 31+) y fallback transparente a `Vibrator` (API 26-30).
  - Patrón cardíaco natural doble pulso (sistole/diastole "lub-dub") mediante `VibrationEffect.createWaveform()`.
  - Modulación dinámica del periodo y amplitud según el porcentaje de afinidad:
    - Afinidad baja (< 35%): pulso tranquilo a ~60 BPM con amplitud moderada.
    - Afinidad media (35% - 70%): pulso acelerado a ~80-90 BPM con mayor amplitud.
    - Afinidad alta (> 70%): pulso intenso y rápido a ~105-115 BPM con maxima amplitud.
  - Fallback elegante si el dispositivo no soporta modulación fina de amplitud (`hasAmplitudeControl() == false`), alternando pulsos de duración variable.
  - Pulso háptico especial de confirmación al completar y soltar tras el umbral (1.2 segundos).
- **Calculo de Sincronía de Gustos (`HeartbeatAffinityCalculator`)**:
  - Comparación algorítmica entre el usuario actual (`currentUser`) y el perfil evaluado:
    - Coincidencia de IDs de videojuegos (`games`).
    - Coincidencia de IDs de animes (`animes`).
    - Coincidencia de objetivos de relación (`relationshipGoals`).
  - Salida normalizada de afinidad entre 0.0 y 1.0 (0% a 100%).
- **Capa Visual Sensorial (`HapticHeartbeatOverlay` con Canvas)**:
  - Detección de pulsación mantenida en el botón de Like (gesto `pointerInput` que detecta inicio de presionado, liberación o cancelación).
  - Atenuación suave de fondo (`dim background`).
  - Ondas concéntricas con gradientes neón dinámicos (rosa/magenta hacia violeta/cian eléctrico) generadas en Canvas que palpitan y se expanden exactamente al compas del pulso háptico.
  - Indicador visual central que refleja el nivel de sincronía y una transición energética cuando se alcanza el umbral de 1.2s.
- **Acción de Confirmación de Like**:
  - Si el usuario mantiene presionado durante >= 1.2s y suelta: se envía el Like (`onLike` / `onSwipeRight`), acompanado de un pulso háptico de éxito.
  - Si el usuario suelta antes de 1.2s: se cancela la animación y vibración de inmediato sin enviar el like (funciona como radar preliminar).
  - Un tap rápido ordinario sigue funcionando como el Like estándar instantáneo.
- **Pantallas Integradas**:
  - Feed Principal (`HomeScreen`).
  - Feed de Categorías (`CategoryFeedScreen`).
  - Detalle del Perfil (`UserProfileDetailScreen`).

### 3.2. Fuera del Alcance (Out of Scope)
- Sensores biométricos reales de hardware externo (smartwatches WearOS o pulsometros Bluetooth).
- Modificación de esquemas de red o almacenamiento en base de datos remota para guardar registros hápticos.

- - -

## 4. Casuísticas y Comportamiento Mobile

- **Comportamiento en Modo Online vs Modo Offline Demo**:
  - En ambos modos, la afinidad se calcula en el cliente contrastando los datos en memoria/locales de `currentUser` con el perfil del candidato. No introduce peticiones de red adicionales ni latencia.
- **Ciclo de Vida y Recuperación de Estado**:
  - Al recibir una llamada, pasar a segundo plano o bloquear la pantalla durante la pulsación, se invoca inmediatamente `vibrator.cancel()` y se liberan las corrutinas asociadas en `onPauseOrDispose`.
- **Gestión de Batería y Límites de Hardware**:
  - La vibración se ejecuta exclusivamente durante la pulsación con un tope máximo de seguridad de 8 segundos continuos si el usuario no suelta el dedo.
  - Si el dispositivo o entorno de prueba carece de motor de vibración (`hasVibrator() == false`), se ejecuta la experiencia visual completa en Canvas sin interrupciones ni excepciones.
- **Ergonomía y Gesto Táctil**:
  - El botón de Like mantiene su área táctil mínima de 48 x 48 dp (tamaño habitual de 72 dp en `SwipeActionButton`).
  - No colisiona con el gesto de swipe horizontal de la tarjeta.

- - -

## 5. Criterios de Aceptación (Formato Given - When - Then)

### Criterio 1: Modulación del pulso háptico y visual según afinidad
- **Dado que**: El usuario está visualizando una tarjeta de perfil con gustos en común conocidos.
- **Cuando**: Mantiene presionado el botón de Like.
- **Entonces**: Se despliega el overlay de atenuación, las ondas de neón palpitan en Canvas y el motor háptico reproduce el patrón doble pulso "lub-dub"; la frecuencia (BPM) y la amplitud son directamente proporcionales al grado de coincidencia de gustos.

### Criterio 2: Cancelación temprana (Radar previo sin consumir Like)
- **Dado que**: El usuario inicio la pulsación prolongada sobre el botón de Like.
- **Cuando**: Suelta el dedo antes de alcanzar el umbral de 1.2 segundos (o arrastra fuera del botón cancelando el gesto).
- **Entonces**: La vibración se detiene inmediatamente (`vibrator.cancel()`), el overlay de Canvas desaparece con un desvanecimiento suave y NO se despacha el Like.

### Criterio 3: Confirmación y envío de Like al superar el umbral
- **Dado que**: El usuario mantiene presionado el botón de Like durante al menos 1.2 segundos.
- **Cuando**: Suelta el dedo tras alcanzar o superar dicho umbral.
- **Entonces**: Se emite un pulso háptico de confirmación, la tarjeta ejecuta la animación de swipe a la derecha / Like y se invoca el callback `onLike` correspondiente.

### Criterio 4: Tap simple instantáneo
- **Dado que**: El usuario desea dar un Like directo sin entrar en el radar sensorial.
- **Cuando**: Realiza un toque rápido (tap) sobre el botón de Like.
- **Entonces**: Se ejecuta la acción de Like inmediata habitual sin retraso alguno.

### Criterio 5: Dispositivos sin hardware háptico
- **Dado que**: La aplicación corre en un dispositivo o emulador sin hardware vibrador o sin modulación de amplitud.
- **Cuando**: Se mantiene presionado el botón de Like.
- **Entonces**: La experiencia visual de ondas de neón en Canvas funciona con total fluidez a 60 fps y la aplicación no arroja excepciones.

- - -

## 6. Decisiones Resueltas

- **Acción final al soltar**: Confirmación y envío de Like automático si se mantiene pulsado durante al menos 1.2 segundos y se suelta; cancelación limpia como radar si se suelta antes.
- **Componente activador**: Botón de Like (corazón) en la botonera de acciones de swipe y en el detalle de perfil, preservando el gesto de pulsación en las fotos para el visor a pantalla completa.
- **Pantallas integradas**: `HomeScreen`, `CategoryFeedScreen` y `UserProfileDetailScreen`.
- **Paleta visual**: Neón dinámico con gradientes rosa/magenta a violeta/cian eléctrico que intensifica su brillo, escala y frecuencia según el porcentaje de afinidad de gustos.
