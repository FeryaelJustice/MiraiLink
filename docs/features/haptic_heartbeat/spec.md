# [APROBADO] Especificacion Funcional: Haptic Heartbeat (Radar de Sincronia Tactil y Sensorial)

- **Fecha**: 2026-09-30
- **Estado**: [APROBADO]
- **Autor / Responsable**: Pair Programming (Antigravity & User)
- **Modulo Afectado**: `:app` (`com.feryaeljustice.mirailink`)

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**: En las aplicaciones sociales y de citas convencionales, evaluar un perfil y hacer swipe o pulsar like es una accion puramente mecanica, distante y fria que no transmite sensacion fisica ni emocion real de conexion.
- **Objetivo**: Implementar "Haptic Heartbeat", una experiencia multisensorial interactiva donde el usuario, al mantener pulsado el boton de "Me Gusta" de un perfil, siente a traves del motor haptico del dispositivo un patron de latidos cardiacos organicos modulados en ritmo e intensidad segun el grado de afinidad o gustos en comun (animes, videojuegos y preferencias), acompanado de ondas concentricas de neon proyectadas en Canvas sobre un fondo atenuado que palpitan al mismo compas. Al mantener la pulsacion durante al menos 1.2 segundos y soltar, se confirma y envia el Like de forma inmersiva.

- - -

## 2. Situacion Actual

- En la arquitectura actual (`UserSwipeCardStack`, `HomeScreen`, `CategoryFeedScreen` y `UserProfileDetailScreen`), el boton de like ejecuta un tap estandar que dispara `onLike` / `onSwipeRight`.
- En `UserCard` / `PublicUserCard`, la pulsacion prolongada sobre las fotos esta reservada para la previsualizacion de imagen completa (`FullscreenImagePreview`), por lo que el disparador del pulso cardiaco se ubica limpiamente en el boton de Like.
- No existe ningun servicio o utilidad centralizada para emitir patrones hapticos organicos con `Vibrator` / `VibratorManager` ni waveforms moduladas.
- En `AndroidManifest.xml` no se encuentra declarado el permiso `android.permission.VIBRATE`.
- Los perfiles exponen listas de intereses (`games`, `animes`, `relationshipGoals`, etc.), pero no existe un algoritmo local de afinidad o sincronia que compare cuantitativamente los intereses del perfil visible con los del usuario conectado (`currentUser`).

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)
- **Declaracion de permisos**: Anadir `<uses-permission android:name="android.permission.VIBRATE" />` en `AndroidManifest.xml`.
- **Motor Haptico (`HapticHeartbeatController`)**:
  - Soporte de `VibratorManager` (Android 12+ / API 31+) y fallback transparente a `Vibrator` (API 26-30).
  - Patron cardiaco natural doble pulso (sistole/diastole "lub-dub") mediante `VibrationEffect.createWaveform()`.
  - Modulacion dinamica del periodo y amplitud segun el porcentaje de afinidad:
    - Afinidad baja (< 35%): pulso tranquilo a ~60 BPM con amplitud moderada.
    - Afinidad media (35% - 70%): pulso acelerado a ~80-90 BPM con mayor amplitud.
    - Afinidad alta (> 70%): pulso intenso y rapido a ~105-115 BPM con maxima amplitud.
  - Fallback elegante si el dispositivo no soporta modulacion fina de amplitud (`hasAmplitudeControl() == false`), alternando pulsos de duracion variable.
  - Pulso haptico especial de confirmacion al completar y soltar tras el umbral (1.2 segundos).
- **Calculo de Sincronia de Gustos (`HeartbeatAffinityCalculator`)**:
  - Comparacion algoritmica entre el usuario actual (`currentUser`) y el perfil evaluado:
    - Coincidencia de IDs de videojuegos (`games`).
    - Coincidencia de IDs de animes (`animes`).
    - Coincidencia de objetivos de relacion (`relationshipGoals`).
  - Salida normalizada de afinidad entre 0.0 y 1.0 (0% a 100%).
- **Capa Visual Sensorial (`HapticHeartbeatOverlay` con Canvas)**:
  - Deteccion de pulsacion mantenida en el boton de Like (gesto `pointerInput` que detecta inicio de presionado, liberacion o cancelacion).
  - Atenuacion suave de fondo (`dim background`).
  - Ondas concentricas con gradientes neon dinamicos (rosa/magenta hacia violeta/cian electrico) generadas en Canvas que palpitan y se expanden exactamente al compas del pulso haptico.
  - Indicador visual central que refleja el nivel de sincronia y una transicion energetica cuando se alcanza el umbral de 1.2s.
- **Accion de Confirmacion de Like**:
  - Si el usuario mantiene presionado durante >= 1.2s y suelta: se envia el Like (`onLike` / `onSwipeRight`), acompanado de un pulso haptico de exito.
  - Si el usuario suelta antes de 1.2s: se cancela la animacion y vibracion de inmediato sin enviar el like (funciona como radar preliminar).
  - Un tap rapido ordinario sigue funcionando como el Like estandar instantaneo.
- **Pantallas Integradas**:
  - Feed Principal (`HomeScreen`).
  - Feed de Categorias (`CategoryFeedScreen`).
  - Detalle del Perfil (`UserProfileDetailScreen`).

### 3.2. Fuera del Alcance (Out of Scope)
- Sensores biometricos reales de hardware externo (smartwatches WearOS o pulsometros Bluetooth).
- Modificacion de esquemas de red o almacenamiento en base de datos remota para guardar registros hapticos.

- - -

## 4. Casuisticas y Comportamiento Mobile

- **Comportamiento en Modo Online vs Modo Offline Demo**:
  - En ambos modos, la afinidad se calcula en el cliente contrastando los datos en memoria/locales de `currentUser` con el perfil del candidato. No introduce peticiones de red adicionales ni latencia.
- **Ciclo de Vida y Recuperacion de Estado**:
  - Al recibir una llamada, pasar a segundo plano o bloquear la pantalla durante la pulsacion, se invoca inmediatamente `vibrator.cancel()` y se liberan las corrutinas asociadas en `onPauseOrDispose`.
- **Gestion de Bateria y Limites de Hardware**:
  - La vibracion se ejecuta exclusivamente durante la pulsacion con un tope maximo de seguridad de 8 segundos continuos si el usuario no suelta el dedo.
  - Si el dispositivo o entorno de prueba carece de motor de vibracion (`hasVibrator() == false`), se ejecuta la experiencia visual completa en Canvas sin interrupciones ni excepciones.
- **Ergonomia y Gesto Tactil**:
  - El boton de Like mantiene su area tactil minima de 48 x 48 dp (tamano habitual de 72 dp en `SwipeActionButton`).
  - No colisiona con el gesto de swipe horizontal de la tarjeta.

- - -

## 5. Criterios de Aceptacion (Formato Given - When - Then)

### Criterio 1: Modulacion del pulso haptico y visual segun afinidad
- **Dado que**: El usuario esta visualizando una tarjeta de perfil con gustos en comun conocidos.
- **Cuando**: Mantiene presionado el boton de Like.
- **Entonces**: Se despliega el overlay de atenuacion, las ondas de neon palpitan en Canvas y el motor haptico reproduce el patron doble pulso "lub-dub"; la frecuencia (BPM) y la amplitud son directamente proporcionales al grado de coincidencia de gustos.

### Criterio 2: Cancelacion temprana (Radar previo sin consumir Like)
- **Dado que**: El usuario inicio la pulsacion prolongada sobre el boton de Like.
- **Cuando**: Suelta el dedo antes de alcanzar el umbral de 1.2 segundos (o arrastra fuera del boton cancelando el gesto).
- **Entonces**: La vibracion se detiene inmediatamente (`vibrator.cancel()`), el overlay de Canvas desaparece con un desvanecimiento suave y NO se despacha el Like.

### Criterio 3: Confirmacion y envio de Like al superar el umbral
- **Dado que**: El usuario mantiene presionado el boton de Like durante al menos 1.2 segundos.
- **Cuando**: Suelta el dedo tras alcanzar o superar dicho umbral.
- **Entonces**: Se emite un pulso haptico de confirmacion, la tarjeta ejecuta la animacion de swipe a la derecha / Like y se invoca el callback `onLike` correspondiente.

### Criterio 4: Tap simple instantaneo
- **Dado que**: El usuario desea dar un Like directo sin entrar en el radar sensorial.
- **Cuando**: Realiza un toque rapido (tap) sobre el boton de Like.
- **Entonces**: Se ejecuta la accion de Like inmediata habitual sin retraso alguno.

### Criterio 5: Dispositivos sin hardware haptico
- **Dado que**: La aplicacion corre en un dispositivo o emulador sin hardware vibrador o sin modulacion de amplitud.
- **Cuando**: Se mantiene presionado el boton de Like.
- **Entonces**: La experiencia visual de ondas de neon en Canvas funciona con total fluidez a 60 fps y la aplicacion no arroja excepciones.

- - -

## 6. Decisiones Resueltas

- **Accion final al soltar**: Confirmacion y envio de Like automatico si se mantiene pulsado durante al menos 1.2 segundos y se suelta; cancelacion limpia como radar si se suelta antes.
- **Componente activador**: Boton de Like (corazon) en la botonera de acciones de swipe y en el detalle de perfil, preservando el gesto de pulsacion en las fotos para el visor a pantalla completa.
- **Pantallas integradas**: `HomeScreen`, `CategoryFeedScreen` y `UserProfileDetailScreen`.
- **Paleta visual**: Neon dinamico con gradientes rosa/magenta a violeta/cian electrico que intensifica su brillo, escala y frecuencia segun el porcentaje de afinidad de gustos.
