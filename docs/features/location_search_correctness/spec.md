# [APROBADO] Especificación Funcional: Corrección Integral de Búsqueda Geográfica

- **Fecha**: 2026-09-21
- **Estado**: [APROBADO]
- **Autor / Responsable**: Codex con validación del propietario de MiraiLink
- **Módulos afectados**: `:app` (`com.feryaeljustice.mirailink`) y `MiraiLink-Backend` (`Express 5 + PostgreSQL`)
- **Especificación previa relacionada**: `docs/features/search_distance_filter/spec.md`

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**: El feed real no respeta de forma fiable el radio, el país ni la elección entre residencia y ubicación activa. El minimapa representa visualmente un radio, pero no garantiza que el backend aplique ese mismo origen y límite. Tras guardar preferencias, Home conserva el mazo anterior hasta que el usuario fuerza una recarga manual.
- **Objetivo**: Definir una única semántica geográfica para Android, modo demo y backend, filtrar candidatos con calculos geodésicos correctos, tratar de forma explícita los perfiles sin ubicación y recargar automáticamente el feed después de guardar.

- - -

## 2. Situación Actual Verificada

1. Android serializa `SearchScope.MY_COUNTRY` como `my_country`, mientras el backend solo acepta y evalúa `country`.
2. El backend escoge siempre la ubicación activa del usuario que busca como origen, aunque el modo de ubicación activa este desactivado.
3. El switch de ubicación activa solo cambia las coordenadas de los candidatos. No aplica una regla simétrica al usuario que busca.
4. Existe `POST /user/location/ping`, pero no hay ningún consumidor Android de `SendLocationPingUseCase`. La aplicación no sincroniza la ubicación obtenida con el backend.
5. `GET /user` no devuelve las coordenadas actuales ni las coordenadas de residencia que Android intenta leer para el minimapa.
6. Si el usuario que busca carece de coordenadas, el alcance `radius` queda sin filtro y puede devolver perfiles de cualquier distancia.
7. En modo radio, los candidatos sin las coordenadas seleccionadas quedan excluidos. No existe una política funcional documentada para perfiles sin residencia ni ubicación activa.
8. Los modos por país no aplican el radio, lo cual coincide con el comportamiento solicitado, pero dependen de valores de contrato que actualmente no coinciden entre Android y backend.
9. Guardar preferencias y volver a Home no emite ninguna señal de invalidación. `HomeViewModel` solo recarga al crearse o mediante refresh manual.
10. No hay pruebas de backend dedicadas al feed geográfico, los límites de radio, los modos de país ni los perfiles sin ubicación.

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance

- Unificar los identificadores de alcance entre Android, backend y persistencia.
- Definir una fuente geográfica coherente para el usuario que busca y para cada candidato.
- Sincronizar de forma controlada la ubicación activa cuando exista permiso.
- Aplicar el radio real en kilometros solo al alcance local.
- Ignorar el radio en Todo mi país, Todo el mundo y Pasaporte.
- Filtrar Todo mi país por el país de residencia del usuario que busca.
- Filtrar Pasaporte por el país de residencia seleccionado.
- Definir el comportamiento de candidatos y buscadores sin coordenadas o sin residencia.
- Actualizar distancia y estado de viajero sin exponer coordenadas privadas en respuestas públicas.
- Recargar automáticamente el mazo de Home después de guardar preferencias correctamente.
- Mantener paridad con el modo demo offline.
- Incorporar pruebas Android y backend para Mallorca, Toulouse, París, fronteras del radio y datos ausentes.

### 3.2. Fuera del Alcance

- Seguimiento GPS continuo en segundo plano.
- Mostrar coordenadas exactas de otros usuarios.
- Cambiar el diseño visual general del minimapa salvo que una corrección sea necesaria para representar el contrato aprobado.
- Convertir funciones beta en funciones de pago o implementar suscripciones Premium.
- Cambiar el algoritmo de afinidad por intereses más alla de lo necesario para que el filtro geográfico duro sea correcto.

- - -

## 4. Contrato Funcional Propuesto

### 4.1. Alcance Radio local

- El radio se aplica como filtro duro, no solo como factor de ordenación.
- La distancia se calcula sobre la esfera terrestre en kilometros y se compara con el radio guardado.
- Un perfil situado en París no puede aparecer para un origen en Toulouse con un radio de 50 km o 100 km.
- Si la opción de ubicación activa está desactivada, se usan coordenadas de residencia para ambos lados.
- Si la opción de ubicación activa está activada, se usa una ubicación activa actualizada durante las últimas 24 horas y se recurre a residencia cuando no exista o haya caducado.
- La regla de selección se aplica simetricamente al usuario que busca y al candidato.
- Un candidato sin residencia útil ni ubicación activa válida puede aparecer como excepción, sin distancia y con menor prioridad.

### 4.2. Todo mi país

- El radio queda desactivado funcionalmente.
- El país objetivo es el país de residencia del usuario que busca.
- Los candidatos se comparan por su país de residencia, no por GPS ni por el país inferido de la última conexión.

### 4.3. Pasaporte por país específico

- El radio queda desactivado funcionalmente.
- Los candidatos se comparan por su país de residencia con el país seleccionado.
- La ubicación activa no cambia el país de residencia usado por Pasaporte.

### 4.4. Todo el mundo

- No se aplica filtro por radio ni por país.
- Cuando sea posible, se calcula y muestra la distancia.
- La distancia puede actuar como una señal secundaria de ordenación, pero nunca excluye perfiles.

### 4.5. Ausencia de datos geográficos

- Un candidato sin residencia útil ni ubicación activa válida puede aparecer en todos los alcances como excepción.
- Ese candidato no muestra una distancia inventada y queda por debajo de candidatos que si cumplen de forma verificable el criterio geográfico.
- Si el usuario que busca no tiene ninguna coordenada útil, Radio local no se convierte silenciosamente en Todo el mundo.
- En ese caso, Home muestra un estado explicativo con acciones para conceder permiso de ubicación o configurar la residencia.
- Todo el mundo puede seguir funcionando sin origen geográfico.
- Todo mi país requiere que el usuario que busca tenga un país de residencia. Si falta, se ofrece la acción de configurar residencia.
- Pasaporte puede seguir funcionando porque usa el país objetivo seleccionado y la residencia de los candidatos.

### 4.6. Guardado y recarga

- El guardado sigue siendo explícito.
- La navegación hacia atrás sin guardar no aplica cambios.
- Tras un guardado remoto y local correcto, volver a Home invalida el mazo existente y solicita un feed nuevo automáticamente.
- La recarga no debe requerir swipe manual ni recrear toda la sesión.
- Si el guardado remoto falla, no se navega como si se hubiera aplicado y se ofrece reintento.

- - -

## 5. Comportamiento Mobile

- **Online**: Las preferencias se consideran guardadas solo cuando backend y persistencia local quedan sincronizados. La ubicación activa se envía únicamente con permiso y bajo el ciclo de vida aprobado.
- **Offline demo**: Se aplica la misma matriz de modos y fuentes geograficas sobre datos Room. No se solicitan permisos reales para los perfiles simulados.
- **Permisos denegados**: La búsqueda por residencia sigue disponible si existen datos de residencia. No se inventan coordenadas de Palma ni de otro lugar como origen real.
- **Vigencia de ubicación activa**: Una ubicación activa conserva validez durante 24 horas desde `last_location_updated_at`. Una ubicación caducada no se usa para filtrar ni calcular distancias como ubicación activa.
- **Proceso y rotación**: El borrador de preferencias sobrevive a cambios de configuración. La invalidación del feed se conserva hasta que Home consume la recarga.
- **Privacidad**: Las coordenadas exactas no se incorporan a DTO públicos. Solo se devuelve distancia derivada y el estado de viajero cuando proceda.
- **Errores**: Se diferencian fallo de permisos, falta de datos geográficos y fallo de red, con acción de reintento cuando corresponda.

- - -

## 6. Criterios de Aceptación

### Criterio 1: Radio real entre Toulouse y París

- **Dado que**: El origen aprobado está en Toulouse y un candidato está en París.
- **Cuando**: El alcance es Radio local con 50 km o 100 km.
- **Entonces**: El candidato de París no aparece en el feed.

### Criterio 2: Mallorca y Francia no se mezclan en Todo mi país

- **Dado que**: El usuario tiene residencia en Francia y existe un candidato con residencia en España.
- **Cuando**: Selecciona Todo mi país.
- **Entonces**: El candidato de España no aparece, con independencia del radio configurado y de la ubicación activa de ambos.

### Criterio 3: Pasaporte ignora el radio

- **Dado que**: El usuario selecciona Francia como país de Pasaporte y hay perfiles residentes en Toulouse y París.
- **Cuando**: Guarda la preferencia aunque el slider conserve 50 km.
- **Entonces**: Ambos perfiles franceses pueden aparecer porque el radio no participa en este alcance.

### Criterio 4: Selección simétrica de fuente geográfica

- **Dado que**: El buscador o un candidato tiene residencia y ubicación activa distintas.
- **Cuando**: Se activa o desactiva la opción de ubicación activa.
- **Entonces**: La fuente aprobada se aplica de forma coherente a ambos lados del calculo y la distancia devuelta coincide con esa fuente.

### Criterio 5: Recarga automática

- **Dado que**: Home ya contiene tarjetas cargadas.
- **Cuando**: El usuario guarda preferencias distintas y vuelve mediante la flecha.
- **Entonces**: Home descarta el mazo anterior, muestra el estado de carga adecuado y solicita un feed nuevo sin gesto manual.

### Criterio 6: Error de guardado

- **Dado que**: El backend no puede guardar las preferencias.
- **Cuando**: El usuario pulsa Guardar.
- **Entonces**: La pantalla no confirma ni aplica silenciosamente una configuración solo local y ofrece una recuperación comprensible.

### Criterio 7: Paridad online y demo

- **Dado que**: Existen candidatos equivalentes en backend y en datos demo.
- **Cuando**: Se aplica cada alcance.
- **Entonces**: Las reglas de inclusión y exclusión son equivalentes.

### Criterio 8: Candidato sin ubicación

- **Dado que**: Un candidato no tiene residencia útil ni ubicación activa válida.
- **Cuando**: Se solicita el feed en cualquier alcance.
- **Entonces**: Puede aparecer como excepción, sin distancia y con menor prioridad que los candidatos que cumplen el criterio geográfico de forma verificable.

### Criterio 9: Buscador sin origen en Radio local

- **Dado que**: El usuario que busca no tiene coordenadas de residencia ni ubicación activa válida.
- **Cuando**: Selecciona Radio local.
- **Entonces**: No recibe silenciosamente un feed mundial y ve un estado con acciones para conceder permiso o configurar residencia.

### Criterio 10: Caducidad de ubicación activa

- **Dado que**: Existe una ubicación activa con más de 24 horas de antiguedad y también existe una residencia valida.
- **Cuando**: La opción de ubicación activa está habilitada.
- **Entonces**: El calculo usa la residencia como respaldo tanto para el buscador como para el candidato.

- - -

## 7. Decisiones Cerradas

- [x] La ubicación activa es valida durante 24 horas. Después se recurre a residencia.
- [x] Los candidatos sin datos geográficos pueden aparecer en todos los alcances, sin distancia y con menor prioridad.
- [x] Radio local no degrada silenciosamente a Todo el mundo cuando falta el origen. Se muestra un estado con acciones para conceder permiso o configurar residencia.
- [x] Todo el mundo no filtra geograficamente, pero muestra distancia cuando sea posible y puede usarla como señal secundaria de ordenación.

- - -

## 8. Bloqueo SDMD

No se generará el plan técnico ni código de producción hasta resolver las decisiones pendientes y recibir aprobación explícita de esta especificación.
