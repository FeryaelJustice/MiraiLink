# [APROBADO] Holo Mirai

Fecha: 2026-10-05. Modulo: `:app`. Aprobacion: conversacion de implementacion Holo Mirai.

## Objetivo y alcance

Dar profundidad sutil por inclinacion a la foto visible de la tarjeta superior de Home y Explorar, conservando fondo natural, controles, carrusel, scroll, undo y latido haptico. Reflejo de borde con el tema Mirai, sin particulas continuas. Edicion, perfiles fuera de descubrimiento, tarjetas inferiores, placeholders y vista ampliada quedan fuera del efecto.

## Comportamiento aprobado

- Retratos con mascara apta: sujeto y fondo se desplazan en sentidos opuestos. Recortes deficientes: parallax de foto completa. Sin sensores: foto estatica.
- Foto inmediata y procesamiento local asincrono. Offline sobre imagenes disponibles, tambien en demo. No modifica ni transmite originales o mascaras.
- Ajuste Perfil Holo-3D activo por defecto; respeta animaciones del sistema y ahorro de bateria.
- Pausa durante interaccion, transicion de foto, salida de tarjeta y vista ampliada. Inclinacion nunca produce likes ni cambia afinidades.
- Detiene recursos al salir de pantalla, perder foco o pasar a segundo plano. Recalibra al recuperar pantalla u orientacion.
- FAQ en Busqueda y tarjetas, textos completos en espanol e ingles. Todo texto de UI se resuelve en Compose con stringResource.

## Criterios de aceptacion

1. Dada una foto apta y sensores, al inclinar el telefono, solo la imagen y el reflejo reaccionan con profundidad limitada y sin silueta duplicada visible.
2. Dado un recorte no apto, al mostrar la foto, se conserva la imagen mediante parallax simple; sin sensor permanece estatica.
3. Dada cualquier presentacion del efecto, al usar swipe, botones, undo, toque lateral, pulsacion larga o scroll, se mantienen las acciones existentes.
4. Dado el ajuste desactivado, ahorro de bateria o animaciones deshabilitadas, no se registra sensor ni se prepara el efecto.
5. Dada una foto ya disponible, sin red, el procesamiento no requiere servidor ni descarga de modelo.
6. Dada una salida de pantalla, cambio de sesion o presion de memoria, se liberan listeners y derivados y se ignoran resultados obsoletos.
7. Dado un cambio de idioma, ajustes, FAQ y accesibilidad usan recursos localizados completos, sin getString en el codigo nuevo.

## Evidencia

Compilacion y pruebas automatizadas no certifican calidad de recorte, consumo, fluidez o inclinacion fisica. La comprobacion en dos dispositivos requiere tres recorridos comparables de 30 swipes con efecto activo e inactivo: menos de 2 puntos adicionales de jank y menos de 20% de degradacion p95.

No quedan decisiones funcionales pendientes. Limitaciones de hardware o pruebas se documentaran sin marcar verificaciones no realizadas como completas.

## Correccion de intensidad aprobada por el usuario

Tras probar la primera entrega, el usuario solicita un movimiento mas notorio sin exagerarlo. Se aumenta un 50 % la amplitud: fondo/foto completa hasta 6 dp y sujeto hasta 3 dp en sentido contrario. Se ajustan proporcionalmente margen de escala y cobertura, manteniendo pausas, gestos, alternativas y limites de inclinacion existentes. Rama de correccion: `codex/holo-mirai-visible-motion`, desde master con la PR #57 integrada.
