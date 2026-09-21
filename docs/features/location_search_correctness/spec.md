# [APROBADO] Especificacion Funcional: Correccion Integral de Busqueda Geografica

- **Fecha**: 2026-09-21
- **Estado**: [APROBADO]
- **Autor / Responsable**: Codex con validacion del propietario de MiraiLink
- **Modulos afectados**: `:app` (`com.feryaeljustice.mirailink`) y `MiraiLink-Backend` (`Express 5 + PostgreSQL`)
- **Especificacion previa relacionada**: `docs/features/search_distance_filter/spec.md`

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**: El feed real no respeta de forma fiable el radio, el pais ni la eleccion entre residencia y ubicacion activa. El minimapa representa visualmente un radio, pero no garantiza que el backend aplique ese mismo origen y limite. Tras guardar preferencias, Home conserva el mazo anterior hasta que el usuario fuerza una recarga manual.
- **Objetivo**: Definir una unica semantica geografica para Android, modo demo y backend, filtrar candidatos con calculos geodesicos correctos, tratar de forma explicita los perfiles sin ubicacion y recargar automaticamente el feed despues de guardar.

- - -

## 2. Situacion Actual Verificada

1. Android serializa `SearchScope.MY_COUNTRY` como `my_country`, mientras el backend solo acepta y evalua `country`.
2. El backend escoge siempre la ubicacion activa del usuario que busca como origen, aunque el modo de ubicacion activa este desactivado.
3. El switch de ubicacion activa solo cambia las coordenadas de los candidatos. No aplica una regla simetrica al usuario que busca.
4. Existe `POST /user/location/ping`, pero no hay ningun consumidor Android de `SendLocationPingUseCase`. La aplicacion no sincroniza la ubicacion obtenida con el backend.
5. `GET /user` no devuelve las coordenadas actuales ni las coordenadas de residencia que Android intenta leer para el minimapa.
6. Si el usuario que busca carece de coordenadas, el alcance `radius` queda sin filtro y puede devolver perfiles de cualquier distancia.
7. En modo radio, los candidatos sin las coordenadas seleccionadas quedan excluidos. No existe una politica funcional documentada para perfiles sin residencia ni ubicacion activa.
8. Los modos por pais no aplican el radio, lo cual coincide con el comportamiento solicitado, pero dependen de valores de contrato que actualmente no coinciden entre Android y backend.
9. Guardar preferencias y volver a Home no emite ninguna señal de invalidacion. `HomeViewModel` solo recarga al crearse o mediante refresh manual.
10. No hay pruebas de backend dedicadas al feed geografico, los limites de radio, los modos de pais ni los perfiles sin ubicacion.

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance

- Unificar los identificadores de alcance entre Android, backend y persistencia.
- Definir una fuente geografica coherente para el usuario que busca y para cada candidato.
- Sincronizar de forma controlada la ubicacion activa cuando exista permiso.
- Aplicar el radio real en kilometros solo al alcance local.
- Ignorar el radio en Todo mi pais, Todo el mundo y Pasaporte.
- Filtrar Todo mi pais por el pais de residencia del usuario que busca.
- Filtrar Pasaporte por el pais de residencia seleccionado.
- Definir el comportamiento de candidatos y buscadores sin coordenadas o sin residencia.
- Actualizar distancia y estado de viajero sin exponer coordenadas privadas en respuestas publicas.
- Recargar automaticamente el mazo de Home despues de guardar preferencias correctamente.
- Mantener paridad con el modo demo offline.
- Incorporar pruebas Android y backend para Mallorca, Toulouse, Paris, fronteras del radio y datos ausentes.

### 3.2. Fuera del Alcance

- Seguimiento GPS continuo en segundo plano.
- Mostrar coordenadas exactas de otros usuarios.
- Cambiar el diseno visual general del minimapa salvo que una correccion sea necesaria para representar el contrato aprobado.
- Convertir funciones beta en funciones de pago o implementar suscripciones Premium.
- Cambiar el algoritmo de afinidad por intereses mas alla de lo necesario para que el filtro geografico duro sea correcto.

- - -

## 4. Contrato Funcional Propuesto

### 4.1. Alcance Radio local

- El radio se aplica como filtro duro, no solo como factor de ordenacion.
- La distancia se calcula sobre la esfera terrestre en kilometros y se compara con el radio guardado.
- Un perfil situado en Paris no puede aparecer para un origen en Toulouse con un radio de 50 km o 100 km.
- Si la opcion de ubicacion activa esta desactivada, se usan coordenadas de residencia para ambos lados.
- Si la opcion de ubicacion activa esta activada, se usa una ubicacion activa actualizada durante las ultimas 24 horas y se recurre a residencia cuando no exista o haya caducado.
- La regla de seleccion se aplica simetricamente al usuario que busca y al candidato.
- Un candidato sin residencia util ni ubicacion activa valida puede aparecer como excepcion, sin distancia y con menor prioridad.

### 4.2. Todo mi pais

- El radio queda desactivado funcionalmente.
- El pais objetivo es el pais de residencia del usuario que busca.
- Los candidatos se comparan por su pais de residencia, no por GPS ni por el pais inferido de la ultima conexion.

### 4.3. Pasaporte por pais especifico

- El radio queda desactivado funcionalmente.
- Los candidatos se comparan por su pais de residencia con el pais seleccionado.
- La ubicacion activa no cambia el pais de residencia usado por Pasaporte.

### 4.4. Todo el mundo

- No se aplica filtro por radio ni por pais.
- Cuando sea posible, se calcula y muestra la distancia.
- La distancia puede actuar como una señal secundaria de ordenacion, pero nunca excluye perfiles.

### 4.5. Ausencia de datos geograficos

- Un candidato sin residencia util ni ubicacion activa valida puede aparecer en todos los alcances como excepcion.
- Ese candidato no muestra una distancia inventada y queda por debajo de candidatos que si cumplen de forma verificable el criterio geografico.
- Si el usuario que busca no tiene ninguna coordenada util, Radio local no se convierte silenciosamente en Todo el mundo.
- En ese caso, Home muestra un estado explicativo con acciones para conceder permiso de ubicacion o configurar la residencia.
- Todo el mundo puede seguir funcionando sin origen geografico.
- Todo mi pais requiere que el usuario que busca tenga un pais de residencia. Si falta, se ofrece la accion de configurar residencia.
- Pasaporte puede seguir funcionando porque usa el pais objetivo seleccionado y la residencia de los candidatos.

### 4.6. Guardado y recarga

- El guardado sigue siendo explicito.
- La navegacion hacia atras sin guardar no aplica cambios.
- Tras un guardado remoto y local correcto, volver a Home invalida el mazo existente y solicita un feed nuevo automaticamente.
- La recarga no debe requerir swipe manual ni recrear toda la sesion.
- Si el guardado remoto falla, no se navega como si se hubiera aplicado y se ofrece reintento.

- - -

## 5. Comportamiento Mobile

- **Online**: Las preferencias se consideran guardadas solo cuando backend y persistencia local quedan sincronizados. La ubicacion activa se envia unicamente con permiso y bajo el ciclo de vida aprobado.
- **Offline demo**: Se aplica la misma matriz de modos y fuentes geograficas sobre datos Room. No se solicitan permisos reales para los perfiles simulados.
- **Permisos denegados**: La busqueda por residencia sigue disponible si existen datos de residencia. No se inventan coordenadas de Palma ni de otro lugar como origen real.
- **Vigencia de ubicacion activa**: Una ubicacion activa conserva validez durante 24 horas desde `last_location_updated_at`. Una ubicacion caducada no se usa para filtrar ni calcular distancias como ubicacion activa.
- **Proceso y rotacion**: El borrador de preferencias sobrevive a cambios de configuracion. La invalidacion del feed se conserva hasta que Home consume la recarga.
- **Privacidad**: Las coordenadas exactas no se incorporan a DTO publicos. Solo se devuelve distancia derivada y el estado de viajero cuando proceda.
- **Errores**: Se diferencian fallo de permisos, falta de datos geograficos y fallo de red, con accion de reintento cuando corresponda.

- - -

## 6. Criterios de Aceptacion

### Criterio 1: Radio real entre Toulouse y Paris

- **Dado que**: El origen aprobado esta en Toulouse y un candidato esta en Paris.
- **Cuando**: El alcance es Radio local con 50 km o 100 km.
- **Entonces**: El candidato de Paris no aparece en el feed.

### Criterio 2: Mallorca y Francia no se mezclan en Todo mi pais

- **Dado que**: El usuario tiene residencia en Francia y existe un candidato con residencia en Espana.
- **Cuando**: Selecciona Todo mi pais.
- **Entonces**: El candidato de Espana no aparece, con independencia del radio configurado y de la ubicacion activa de ambos.

### Criterio 3: Pasaporte ignora el radio

- **Dado que**: El usuario selecciona Francia como pais de Pasaporte y hay perfiles residentes en Toulouse y Paris.
- **Cuando**: Guarda la preferencia aunque el slider conserve 50 km.
- **Entonces**: Ambos perfiles franceses pueden aparecer porque el radio no participa en este alcance.

### Criterio 4: Seleccion simetrica de fuente geografica

- **Dado que**: El buscador o un candidato tiene residencia y ubicacion activa distintas.
- **Cuando**: Se activa o desactiva la opcion de ubicacion activa.
- **Entonces**: La fuente aprobada se aplica de forma coherente a ambos lados del calculo y la distancia devuelta coincide con esa fuente.

### Criterio 5: Recarga automatica

- **Dado que**: Home ya contiene tarjetas cargadas.
- **Cuando**: El usuario guarda preferencias distintas y vuelve mediante la flecha.
- **Entonces**: Home descarta el mazo anterior, muestra el estado de carga adecuado y solicita un feed nuevo sin gesto manual.

### Criterio 6: Error de guardado

- **Dado que**: El backend no puede guardar las preferencias.
- **Cuando**: El usuario pulsa Guardar.
- **Entonces**: La pantalla no confirma ni aplica silenciosamente una configuracion solo local y ofrece una recuperacion comprensible.

### Criterio 7: Paridad online y demo

- **Dado que**: Existen candidatos equivalentes en backend y en datos demo.
- **Cuando**: Se aplica cada alcance.
- **Entonces**: Las reglas de inclusion y exclusion son equivalentes.

### Criterio 8: Candidato sin ubicacion

- **Dado que**: Un candidato no tiene residencia util ni ubicacion activa valida.
- **Cuando**: Se solicita el feed en cualquier alcance.
- **Entonces**: Puede aparecer como excepcion, sin distancia y con menor prioridad que los candidatos que cumplen el criterio geografico de forma verificable.

### Criterio 9: Buscador sin origen en Radio local

- **Dado que**: El usuario que busca no tiene coordenadas de residencia ni ubicacion activa valida.
- **Cuando**: Selecciona Radio local.
- **Entonces**: No recibe silenciosamente un feed mundial y ve un estado con acciones para conceder permiso o configurar residencia.

### Criterio 10: Caducidad de ubicacion activa

- **Dado que**: Existe una ubicacion activa con mas de 24 horas de antiguedad y tambien existe una residencia valida.
- **Cuando**: La opcion de ubicacion activa esta habilitada.
- **Entonces**: El calculo usa la residencia como respaldo tanto para el buscador como para el candidato.

- - -

## 7. Decisiones Cerradas

- [x] La ubicacion activa es valida durante 24 horas. Despues se recurre a residencia.
- [x] Los candidatos sin datos geograficos pueden aparecer en todos los alcances, sin distancia y con menor prioridad.
- [x] Radio local no degrada silenciosamente a Todo el mundo cuando falta el origen. Se muestra un estado con acciones para conceder permiso o configurar residencia.
- [x] Todo el mundo no filtra geograficamente, pero muestra distancia cuando sea posible y puede usarla como señal secundaria de ordenacion.

- - -

## 8. Bloqueo SDMD

No se generara el plan tecnico ni codigo de produccion hasta resolver las decisiones pendientes y recibir aprobacion explicita de esta especificacion.
