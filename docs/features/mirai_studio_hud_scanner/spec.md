# [APROBADO] Especificación Funcional: Mirai Studio y HUD Scanner Biométrico

- **Fecha**: 2026-10-01
- **Estado**: [APROBADO]
- **Autor / Responsable**: Pair Programming (Antigravity & User)
- **Módulo Afectado**: `:app` (`com.feryaeljustice.mirailink`)

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**: Los usuarios de MiraiLink a menudo suben fotos oscuras, sobreexpuestas, excesivamente comprimidas, borrosas o capturas de pantalla de baja calidad tomadas con barras de estado y bordes negros. Esto devalua el aspecto visual del perfil y reduce el interés de otros usuarios. Además, los sistemas tradicionales o bien rechazan imágenes sin explicación o no brindan ninguna guía interactiva al usuario.
- **Objetivo**: Desarrollar e integrar **Mirai Studio (HUD Scanner)**, una experiencia interactiva futurista con estética cyberpunk/sci-fi accesible tanto desde Ajustes como integrada en el flujo de selección de fotos de perfil (Cámara y Galería). El escaner realiza un análisis 100% local y offline mediante Google ML Kit Face Detection y un motor de métricas de calidad de imagen (luminancia, contraste, resolución, nitidez y detección de capturas de pantalla). Proporciona retroalimentación gráfica en tiempo real mediante un HUD animado en Jetpack Compose, asignando insignias holográficas y recomendaciones no bloqueantes ("Soft Warning"), con soporte inclusivo para retratos humanos, cosplays, fotos de cuerpo entero o avatares de anime.

- - -

## 2. Situación Actual

- En `ProfileScreen.kt`, al tocar una ranura de `EditablePhotoGrid`, se despliega un diálogo básico ("Actualizar o Borrar") y después "Galería o Cámara".
- La selección desde Galería (`ActivityResultContracts.GetContent()`) inserta la imagen de forma directa sin ningún tipo de verificación de resolución, iluminación ni nitidez.
- La captura de Cámara delega en la aplicación de cámara por defecto del sistema mediante `ActivityResultContracts.TakePicture()`, sin preview embebido ni guías de encuadre en tiempo real.
- En `SettingsScreen.kt` no existe un punto de entrada para probar la cámara o explorar la herramienta de calibración fotográfica.
- En la sección de Preguntas Frecuentes (`FaqRepositoryImpl.kt`), no se explican los estándares de calidad visual para las fotos de perfil.

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)

1. **Punto de Entrada Dual (Modo Dual Completo)**:
   - **Acceso desde Ajustes (`SettingsScreen`)**: Nueva tarjeta de acción "Mirai Studio (HUD Scanner)" en la sección Multimedia / Cuenta, que navega a la pantalla completa `AppScreen.MiraiStudioScreen`. Permite al usuario interactuar libremente con la cámara en vivo o cargar fotos de la galería para ver su análisis HUD y, opcionalmente, exportar/guardar la foto en su perfil.
   - **Acceso desde Edición de Perfil (`ProfileScreen`)**:
     - Al pulsar "Cámara" en una ranura de foto: Abre el visor en vivo con CameraX y HUD Scanner. Al capturar, muestra el veredicto del análisis antes de confirmar su asignación a la ranura.
     - Al pulsar "Galería": Al elegir una imagen, se despliega la pantalla/modal de inspección HUD de Mirai Studio, mostrando el escaneo biometrico/visual y el veredicto antes de confirmarla.

2. **Doble Modo de Análisis Inteligente (Rostro vs Ilustración / Anime / Distancia)**:
   - **Modo Biométrico de Rostro (si se detecta cara humana o cosplay)**:
     - Detección de puntos faciales con Google ML Kit Face Detection.
     - Insignias biométricas: "Mirada Directa" (ángulos yaw/pitch centrados), "Sonrisa Auténtica" (probabilidad de sonrisa), "Encuadre Centrado" (posición respecto a regla de los tercios) y "Ojos Abiertos".
   - **Modo Calidad Visual General (si es avatar anime, mascota, paisaje o plano general)**:
     - No penaliza ni rechaza la foto por no contener un rostro humano.
     - Evalúa métricas universales: "Iluminación Optima", "Alta Nitidez", "Resolución Apta".

3. **Detector de Capturas de Pantalla (Anti-Screenshot Scanner)**:
   - Analiza si la imagen proviene de una captura de pantalla (detección por nombre de archivo con patrón `Screenshot_` o `Captura_`, relación de aspecto extrema idéntica a la relación de pantalla del dispositivo con barras negras/grises, o baja densidad efectiva).
   - Muestra una sugerencia amigable: "Detectamos una posible captura de pantalla. Para un perfil más atractivo, te recomendamos subir la imagen original recortada sin barras del sistema".

4. **Política de Calidad "Advertencia Informativa (Soft Warning)"**:
   - Si la imagen es muy oscura, borrosa o de muy baja resolución, el HUD resalta las métricas deficientes en color ambar/rojo con recomendaciones concretas de corrección.
   - **Sin bloqueo estricto**: El usuario siempre tiene la libertad de pulsar "Usar de todos modos" o "Repetir / Elegir otra".

5. **Diseño Gráfico e Interfaz HUD de Ciencia Ficción (Jetpack Compose)**:
   - Reticula animada de escaneo con laser de barrido continuo (animación infinita con `rememberInfiniteTransition`).
   - Líneas de guía de regla de los tercios y esquinas bracket sci-fi (`[` y `]`).
   - Caja delimitadora y puntos de anclaje facial cuando se detecta rostro.
   - Indicadores circulares y medidores de nivel de luminancia en porcentaje.
   - Tarjetas de insignias holográficas con brillo neón en los colores temáticos de MiraiLink (cyan, violeta y magenta).

6. **Estándares Documentados y Educación**:
   - Categoría en FAQ: `FaqCategory.PHOTOS_AND_STUDIO` ("Fotos y Mirai Studio") con preguntas claras sobre los estándares de calidad recomendados (resolución, luz, encuadre, nitidez y recorte de capturas de pantalla).
   - Acceso rápido a estándares con icono/boton "Estándares de calidad" en el modal de selección de origen de foto y en el visor de Mirai Studio.

### 3.2. Fuera del Alcance (Out of Scope)

- Filtros de moderación NSFW o censura remota en servidores (la herramienta opera de forma local, técnica y constructiva).
- Procesamiento en la nube o envío de datos biométricos fuera del dispositivo (100% privado y local con ML Kit).
- Algoritmos de KYC o verificación de identidad policial mediante comparación con documentos de identidad.

- - -

## 4. Casuísticas y Comportamiento Mobile

- **Modo Online vs Modo Offline Demo**:
  - Mirai Studio opera completamente desconectado sin requerir internet. Funciona con el 100% de sus capacidades tanto en modo normal como en el modo offline sandbox con Room.
- **Ciclo de Vida y Cámara (CameraX)**:
  - El ciclo de CameraX (`ProcessCameraProvider`) se vincula de forma segura al `LifecycleOwner` de la vista Compose (`AndroidView` con `PreviewView`).
  - Al pausar la aplicación (llamada entrante, cambio de app), la cámara se desvincula de inmediato liberando el hardware.
  - Al cambiar de cámara (frontal/trasera), la re-vinculacion ocurre de forma fluida sin parpadeos ni fugas de memoria.
- **Manejo de Permisos en Tiempo de Ejecución**:
  - Solicitud de `Manifest.permission.CAMERA` mediante `rememberLauncherForActivityResult`. Si se deniega, se muestra un estado explicativo con botón para abrir la configuración del sistema o cambiar a modo Galería.
- **Rendimiento y Estrategia de Fotogramas**:
  - `ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST` para no acumular fotogramas ni generar retraso (*lag*) en la renderización del HUD en dispositivos de gama baja o media.
- **Ergonomía y Accesibilidad**:
  - Touch targets de botones principales (capturar, cambiar cámara, alternar modo, reintentar, confirmar) superiores a 48 x 48 dp.
  - Textos descriptivos en `contentDescription` para lectores de pantalla TalkBack en cada insignia y botón.

- - -

## 5. Criterios de Aceptación (Formato Given - When - Then)

### Criterio 1: Apertura y calibración en tiempo real con CameraX
- **Dado que**: El usuario entra a Mirai Studio desde Ajustes o al pulsar Cámara en Edición de Perfil, con permisos concedidos.
- **Cuando**: Enfoca su rostro o escena en la cámara frontal/trasera.
- **Entonces**: Se muestra la previsualización en vivo con el HUD animado (laser, regla de tercios, brackets, porcentaje de luz). Si hay rostro, se calculan las métricas biométricas en tiempo real. Al pulsar el botón de disparo, se captura la imagen en alta resolución y se congela el panel con el veredicto final.

### Criterio 2: Evaluación de fotos de Galería (Rostros, Anime, Cosplay o Ilustraciones)
- **Dado que**: El usuario selecciona una imagen de su galería (ya sea un selfie, un cosplay o una ilustracion/personaje de anime).
- **Cuando**: La imagen es procesada por el motor de calidad de Mirai Studio.
- **Entonces**: El sistema evalúa la luminancia, resolución y nitidez. Si no hay rostro humano, evalúa la calidad visual general sin rechazar la foto. Si detecta características de captura de pantalla, añade una recomendación amigable de recorte.

### Criterio 3: Flujo de Advertencia Suave (Soft Warning)
- **Dado que**: La foto analizada presenta deficiencias notables (ejemplo: iluminación inferior al 20% o resolución muy baja).
- **Cuando**: Finaliza el análisis y se presenta el informe HUD.
- **Entonces**: Los indicadores afectados se muestran en color ambar con advertencias descriptivas, pero el botón "Usar de todos modos" permanece habilitado junto al botón "Reintentar / Cambiar foto".

### Criterio 4: Información de Estándares y Preguntas Frecuentes
- **Dado que**: El usuario navega a la sección de Preguntas Frecuentes o pulsa el enlace informativo en el modal de foto.
- **Cuando**: Consulta los articulos bajo la categoría "Fotos y Mirai Studio".
- **Entonces**: Lee de forma transparente que parámetros evalúa el HUD Scanner (iluminación equilibrada, nitidez, no capturas con barras del sistema, encuadre adecuado) y como optimizar sus fotos para el perfil.

- - -

## 6. Decisiones Tomadas y Aprobadas

- **Aprobado**: Nivel de restricción: Advertencia informativa (Soft Warning). El usuario tiene la última palabra.
- **Aprobado**: Arquitectura de navegación dual: Pantalla completa en Ajustes (`AppScreen.MiraiStudioScreen`) y modal/flujo interactivo integrado en `ProfileScreen`.
- **Aprobado**: Soporte inclusivo de contenido: Análisis biométrico completo cuando hay rostro, y análisis de calidad visual universal (luz, nitidez, resolución y detección de captura de pantalla) si es ilustración anime, cosplay o plano general.
- **Aprobado**: Estándares documentados en `FaqCategory.PHOTOS_AND_STUDIO` y enlaces de acceso rápido en los modales de selección.

## Corrección solicitada por el usuario - 2026-10-01

- El HUD no ofrece accesos directos a galería ni preguntas frecuentes, incluidos los de revisión y permisos.
- Al guardarse la captura, el HUD presenta esa foto estática y libera CameraX antes de analizarla. No depende de que los filtros pasen.
- La revisión muestra el resumen y permite X para descartar y volver a la cámara o ✓ para continuar incluso con advertencias.
- Si el análisis falla, conserva la foto e informa del fallo con las mismas opciones.
