# [APROBADO] Especificacion Funcional: Mirai Studio y HUD Scanner Biometrico

- **Fecha**: 2026-10-01
- **Estado**: [APROBADO]
- **Autor / Responsable**: Pair Programming (Antigravity & User)
- **Modulo Afectado**: `:app` (`com.feryaeljustice.mirailink`)

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**: Los usuarios de MiraiLink a menudo suben fotos oscuras, sobreexpuestas, excesivamente comprimidas, borrosas o capturas de pantalla de baja calidad tomadas con barras de estado y bordes negros. Esto devalua el aspecto visual del perfil y reduce el interes de otros usuarios. Ademas, los sistemas tradicionales o bien rechazan imagenes sin explicacion o no brindan ninguna guia interactiva al usuario.
- **Objetivo**: Desarrollar e integrar **Mirai Studio (HUD Scanner)**, una experiencia interactiva futurista con estetica cyberpunk/sci-fi accesible tanto desde Ajustes como integrada en el flujo de seleccion de fotos de perfil (Camara y Galeria). El escaner realiza un analisis 100% local y offline mediante Google ML Kit Face Detection y un motor de metricas de calidad de imagen (luminancia, contraste, resolucion, nitidez y deteccion de capturas de pantalla). Proporciona retroalimentacion grafica en tiempo real mediante un HUD animado en Jetpack Compose, asignando insignias holograficas y recomendaciones no bloqueantes ("Soft Warning"), con soporte inclusivo para retratos humanos, cosplays, fotos de cuerpo entero o avatares de anime.

- - -

## 2. Situacion Actual

- En `ProfileScreen.kt`, al tocar una ranura de `EditablePhotoGrid`, se despliega un dialogo basico ("Actualizar o Borrar") y despues "Galeria o Camara".
- La seleccion desde Galeria (`ActivityResultContracts.GetContent()`) inserta la imagen de forma directa sin ningun tipo de verificacion de resolucion, iluminacion ni nitidez.
- La captura de Camara delega en la aplicacion de camara por defecto del sistema mediante `ActivityResultContracts.TakePicture()`, sin preview embebido ni guias de encuadre en tiempo real.
- En `SettingsScreen.kt` no existe un punto de entrada para probar la camara o explorar la herramienta de calibracion fotografica.
- En la seccion de Preguntas Frecuentes (`FaqRepositoryImpl.kt`), no se explican los estandares de calidad visual para las fotos de perfil.

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)

1. **Punto de Entrada Dual (Modo Dual Completo)**:
   - **Acceso desde Ajustes (`SettingsScreen`)**: Nueva tarjeta de accion "Mirai Studio (HUD Scanner)" en la seccion Multimedia / Cuenta, que navega a la pantalla completa `AppScreen.MiraiStudioScreen`. Permite al usuario interactuar libremente con la camara en vivo o cargar fotos de la galeria para ver su analisis HUD y, opcionalmente, exportar/guardar la foto en su perfil.
   - **Acceso desde Edicion de Perfil (`ProfileScreen`)**:
     - Al pulsar "Camara" en una ranura de foto: Abre el visor en vivo con CameraX y HUD Scanner. Al capturar, muestra el veredicto del analisis antes de confirmar su asignacion a la ranura.
     - Al pulsar "Galeria": Al elegir una imagen, se despliega la pantalla/modal de inspeccion HUD de Mirai Studio, mostrando el escaneo biometrico/visual y el veredicto antes de confirmarla.

2. **Doble Modo de Analisis Inteligente (Rostro vs Ilustracion / Anime / Distancia)**:
   - **Modo Biometrico de Rostro (si se detecta cara humana o cosplay)**:
     - Deteccion de puntos faciales con Google ML Kit Face Detection.
     - Insignias biometricas: "Mirada Directa" (angulos yaw/pitch centrados), "Sonrisa Autentica" (probabilidad de sonrisa), "Encuadre Centrado" (posicion respecto a regla de los tercios) y "Ojos Abiertos".
   - **Modo Calidad Visual General (si es avatar anime, mascota, paisaje o plano general)**:
     - No penaliza ni rechaza la foto por no contener un rostro humano.
     - Evalua metricas universales: "Iluminacion Optima", "Alta Nitidez", "Resolucion Apta".

3. **Detector de Capturas de Pantalla (Anti-Screenshot Scanner)**:
   - Analiza si la imagen proviene de una captura de pantalla (deteccion por nombre de archivo con patron `Screenshot_` o `Captura_`, relacion de aspecto extrema identica a la relacion de pantalla del dispositivo con barras negras/grises, o baja densidad efectiva).
   - Muestra una sugerencia amigable: "Detectamos una posible captura de pantalla. Para un perfil mas atractivo, te recomendamos subir la imagen original recortada sin barras del sistema".

4. **Politica de Calidad "Advertencia Informativa (Soft Warning)"**:
   - Si la imagen es muy oscura, borrosa o de muy baja resolucion, el HUD resalta las metricas deficientes en color ambar/rojo con recomendaciones concretas de correccion.
   - **Sin bloqueo estricto**: El usuario siempre tiene la libertad de pulsar "Usar de todos modos" o "Repetir / Elegir otra".

5. **Diseno Grafico e Interfaz HUD de Ciencia Ficcion (Jetpack Compose)**:
   - Reticula animada de escaneo con laser de barrido continuo (animacion infinita con `rememberInfiniteTransition`).
   - Lineas de guia de regla de los tercios y esquinas bracket sci-fi (`[` y `]`).
   - Caja delimitadora y puntos de anclaje facial cuando se detecta rostro.
   - Indicadores circulares y medidores de nivel de luminancia en porcentaje.
   - Tarjetas de insignias holograficas con brillo neon en los colores tematicos de MiraiLink (cyan, violeta y magenta).

6. **Estandares Documentados y Educacion**:
   - Categoria en FAQ: `FaqCategory.PHOTOS_AND_STUDIO` ("Fotos y Mirai Studio") con preguntas claras sobre los estandares de calidad recomendados (resolucion, luz, encuadre, nitidez y recorte de capturas de pantalla).
   - Acceso rapido a estandares con icono/boton "Estandares de calidad" en el modal de seleccion de origen de foto y en el visor de Mirai Studio.

### 3.2. Fuera del Alcance (Out of Scope)

- Filtros de moderacion NSFW o censura remota en servidores (la herramienta opera de forma local, tecnica y constructiva).
- Procesamiento en la nube o envio de datos biometricos fuera del dispositivo (100% privado y local con ML Kit).
- Algoritmos de KYC o verificacion de identidad policial mediante comparacion con documentos de identidad.

- - -

## 4. Casuisticas y Comportamiento Mobile

- **Modo Online vs Modo Offline Demo**:
  - Mirai Studio opera completamente desconectado sin requerir internet. Funciona con el 100% de sus capacidades tanto en modo normal como en el modo offline sandbox con Room.
- **Ciclo de Vida y Camara (CameraX)**:
  - El ciclo de CameraX (`ProcessCameraProvider`) se vincula de forma segura al `LifecycleOwner` de la vista Compose (`AndroidView` con `PreviewView`).
  - Al pausar la aplicacion (llamada entrante, cambio de app), la camara se desvincula de inmediato liberando el hardware.
  - Al cambiar de camara (frontal/trasera), la re-vinculacion ocurre de forma fluida sin parpadeos ni fugas de memoria.
- **Manejo de Permisos en Tiempo de Ejecucion**:
  - Solicitud de `Manifest.permission.CAMERA` mediante `rememberLauncherForActivityResult`. Si se deniega, se muestra un estado explicativo con boton para abrir la configuracion del sistema o cambiar a modo Galeria.
- **Rendimiento y Estrategia de Fotogramas**:
  - `ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST` para no acumular fotogramas ni generar retraso (*lag*) en la renderizacion del HUD en dispositivos de gama baja o media.
- **Ergonomia y Accesibilidad**:
  - Touch targets de botones principales (capturar, cambiar camara, alternar modo, reintentar, confirmar) superiores a 48 x 48 dp.
  - Textos descriptivos en `contentDescription` para lectores de pantalla TalkBack en cada insignia y boton.

- - -

## 5. Criterios de Aceptacion (Formato Given - When - Then)

### Criterio 1: Apertura y calibracion en tiempo real con CameraX
- **Dado que**: El usuario entra a Mirai Studio desde Ajustes o al pulsar Camara en Edicion de Perfil, con permisos concedidos.
- **Cuando**: Enfoca su rostro o escena en la camara frontal/trasera.
- **Entonces**: Se muestra la previsualizacion en vivo con el HUD animado (laser, regla de tercios, brackets, porcentaje de luz). Si hay rostro, se calculan las metricas biometricas en tiempo real. Al pulsar el boton de disparo, se captura la imagen en alta resolucion y se congela el panel con el veredicto final.

### Criterio 2: Evaluacion de fotos de Galeria (Rostros, Anime, Cosplay o Ilustraciones)
- **Dado que**: El usuario selecciona una imagen de su galeria (ya sea un selfie, un cosplay o una ilustracion/personaje de anime).
- **Cuando**: La imagen es procesada por el motor de calidad de Mirai Studio.
- **Entonces**: El sistema evalua la luminancia, resolucion y nitidez. Si no hay rostro humano, evalua la calidad visual general sin rechazar la foto. Si detecta caracteristicas de captura de pantalla, anade una recomendacion amigable de recorte.

### Criterio 3: Flujo de Advertencia Suave (Soft Warning)
- **Dado que**: La foto analizada presenta deficiencias notables (ejemplo: iluminacion inferior al 20% o resolucion muy baja).
- **Cuando**: Finaliza el analisis y se presenta el informe HUD.
- **Entonces**: Los indicadores afectados se muestran en color ambar con advertencias descriptivas, pero el boton "Usar de todos modos" permanece habilitado junto al boton "Reintentar / Cambiar foto".

### Criterio 4: Informacion de Estandares y Preguntas Frecuentes
- **Dado que**: El usuario navega a la seccion de Preguntas Frecuentes o pulsa el enlace informativo en el modal de foto.
- **Cuando**: Consulta los articulos bajo la categoria "Fotos y Mirai Studio".
- **Entonces**: Lee de forma transparente que parametros evalua el HUD Scanner (iluminacion equilibrada, nitidez, no capturas con barras del sistema, encuadre adecuado) y como optimizar sus fotos para el perfil.

- - -

## 6. Decisiones Tomadas y Aprobadas

- **Aprobado**: Nivel de restriccion: Advertencia informativa (Soft Warning). El usuario tiene la ultima palabra.
- **Aprobado**: Arquitectura de navegacion dual: Pantalla completa en Ajustes (`AppScreen.MiraiStudioScreen`) y modal/flujo interactivo integrado en `ProfileScreen`.
- **Aprobado**: Soporte inclusivo de contenido: Analisis biometrico completo cuando hay rostro, y analisis de calidad visual universal (luz, nitidez, resolucion y deteccion de captura de pantalla) si es ilustracion anime, cosplay o plano general.
- **Aprobado**: Estandares documentados en `FaqCategory.PHOTOS_AND_STUDIO` y enlaces de acceso rapido en los modales de seleccion.

## Correccion solicitada por el usuario - 2026-10-01

- El HUD no ofrece accesos directos a galeria ni preguntas frecuentes, incluidos los de revision y permisos.
- Al guardarse la captura, el HUD presenta esa foto estatica y libera CameraX antes de analizarla. No depende de que los filtros pasen.
- La revision muestra el resumen y permite X para descartar y volver a la camara o ✓ para continuar incluso con advertencias.
- Si el analisis falla, conserva la foto e informa del fallo con las mismas opciones.
