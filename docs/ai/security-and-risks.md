# Seguridad, riesgos y deuda técnica

Esta lista documenta hallazgos, no implementa correcciones. Las prioridades indican impacto potencial y urgencia de revisión.

## Prioridad crítica

### Material de firma versionado

`mirailinkkeystore.jks` está versionado. El archivo local de propiedades está ignorado, pero un keystore de aplicación debe tratarse como secreto aunque no se confirme aquí si sigue activo.

Acción recomendada:

- Confirmar si firma alguna versión distribuida.
- Si es activo o pudo exponerse, rotar la clave mediante el proceso de Play App Signing aplicable.
- Eliminarlo del historial de Git de forma coordinada, no solo del último commit.
- Guardar la clave en un secret manager o almacén seguro de CI.

### App Check de debug en cualquier variante

`DebugAppCheckProviderFactory` se instala siempre desde `MainActivity` y su dependencia usa `implementation`. Una release puede arrancar con el provider de debug.

Acción recomendada: separar dependencias y código por source set, usar debug provider solo en debug y un provider de producción en release.

## Prioridad alta

### Tokens expuestos en logs

- `AuthInterceptor` registra el token Bearer.
- `MainActivity` registra el token FCM.
- `FcmService` registra tokens y el objeto RemoteMessage.
- OkHttp registra bodies completos en debug.

Los logs pueden terminar en bug reports, herramientas de desarrollo o servicios de soporte. Eliminar los valores y conservar solo metadatos no sensibles.

### Tráfico claro permitido globalmente

La base de `network_security_config` permite cleartext y solo endurece el dominio de producción. Lint lo detecta como `InsecureBaseConfiguration`.

Acción recomendada: denegar HTTP en base y mover localhost y emuladores a una configuración debug específica.

### Credential Manager incompleto

Lint bloquea con `CredManMissingDal` y advierte que no se maneja `NoCredentialException`. Esto afecta la asociación app y web y la robustez de autofill.

### Configuración remota no inicializada

`RemoteConfigManager.initialize()` no tiene call site. Gemini y el feature flag navideño consumen valores potencialmente vacíos o por defecto interno antes de configurar los defaults XML.

### Configuración sensible versionada

`app/google-services.json` está versionado. Sus identificadores no equivalen por sí solos a una credencial de servidor, pero deben tener restricciones de API, package y certificado. Los backups y el log Kotlin versionados también deben revisarse por información accidental antes de cada publicación.

## Prioridad media

### Deep links declarados pero no aplicados

Los intent filters aceptan rutas de login, verificación y recuperación, pero la Activity no lee la URI. El usuario puede abrir la app sin llegar al destino esperado. También falta verificar Digital Asset Links y las combinaciones de elementos data.

### Socket.IO no usado por la UI

El repositorio promete chat en tiempo real, pero la pantalla consulta REST cada 3 segundos. Esto aumenta red y batería y deja código de socket sin lifecycle real.

### Chat de grupo incompleto

La creación existe, pero el setup posterior está marcado TODO y comienza polling con un ID vacío.

### Arquitectura con dependencias invertidas

- Dominio importa Android, Retrofit, Firebase y view entries.
- Data importa tipos de UI.
- ViewModels importan mappers de Data.

Esto dificulta tests puros, modularización y reemplazo de plataforma.

### Binding AI duplicado

`AiRepository` está registrado en `aiModule` y `repositoryModule`. El grafo depende del comportamiento de override u orden de Koin.

### `SYSTEM_ALERT_WINDOW` sin uso localizado

Es un permiso de alto impacto y no se encontró ninguna API asociada. Debe retirarse si no hay una función demostrable que lo necesite.

### Cámara obligatoria

El manifest marca la cámara como feature requerida. Esto excluye dispositivos compatibles sin cámara. Si solo es una fuente opcional de foto, debería ser `required=false` y mantenerse el selector de galería.

### Inicialización y cadencia de Ads

Mobile Ads se inicializa desde tres caminos y una parte ocurre antes de terminar UMP. La Activity intenta mostrar interstitials a los 10 segundos y cada 5 minutos. Revisar consentimiento, experiencia, políticas y lifecycle.

### Notificaciones sin destino de conversación

El PendingIntent de una notificación abre la Activity sin conversation ID. Además, si el token FCM aparece antes de login y no entra autenticación en 1,5 segundos, se descarta sin cola local.

### Errores de DataStore cifrado

El serializer lanza excepciones para payload corto, error GCM, clave inválida o JSON corrupto. No se encontró un corruption handler ni recuperación controlada. Una restauración incompatible o cambio de clave puede impedir leer estado.

### AuthInterceptor consume cada body

El interceptor carga todo el response body como String para buscar flags y luego lo reconstruye. Esto añade memoria y coste a archivos o respuestas grandes y presupone que todos los cuerpos pueden leerse de esa forma.

## Deuda funcional

- Undo de Home solo vive en memoria y tiene un TODO para persistencia.
- El chat de grupo está sin terminar.
- Remote Config no se inicializa.
- Deep links no se enrutan.
- El socket no se consume.
- La cola FCM pendiente no está implementada.
- Messages mantiene datos placeholder iniciales antes de reemplazarlos por red.

## Deuda de calidad

- Unit tests no compilan por el constructor de Splash.
- Instrumented API tests contienen endpoints antiguos.
- Lint tiene 1 error y 100 warnings.
- Android lint recibe un ID de regla de ktlint que no conoce.
- El flag de compilador `-Xexplicit-backing-fields` es redundante.
- Hay 11 recursos sin uso y 6 bitmaps en una carpeta densityless inadecuada según lint.

## Riesgos de documentación

- AGENTS ordena Hilt aunque el proyecto usa Koin.
- README y varios archivos AI citan SDK y versiones antiguas.
- La frase chat en tiempo real oculta que la UI usa polling.
- La frase testing al completo no coincide con el baseline actual.

Usa [`ai-context-audit.md`](ai-context-audit.md) para mantener estas discrepancias visibles hasta que los archivos históricos se actualicen de forma coordinada.
