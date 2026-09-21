# Desglose de Tareas: Correccion Integral de Busqueda Geografica

- **Especificacion**: `docs/features/location_search_correctness/spec.md`
- **Plan tecnico**: `docs/features/location_search_correctness/plan.md`
- **Estado**: En progreso, implementacion funcional completada y pendiente de validacion manual
- **Ramas**: `codex/location-search-correctness` en Android y backend
- **Politica de Git**: Sin commits hasta que el propietario revise el resultado

- - -

## Reglas de Ejecucion

- Ejecutar las fases en orden.
- No marcar una tarea como completada hasta que su codigo y sus pruebas focalizadas pasen.
- Si una prueba falla por una causa preexistente, documentar evidencia y ejecutar la comprobacion focalizada mas estrecha disponible.
- No ejecutar `db:reset`.
- No usar una base de datos compartida o de produccion para pruebas destructivas.
- No exponer coordenadas, tokens ni otros datos personales en logs.
- Mantener intactos los cambios previos sobre los que parte la rama backend.

- - -

## Fase 1: Contrato Geografico Backend y Pruebas Base

- [ ] **1.1** Crear helpers puros o una capa interna para seleccionar coordenadas vigentes, resolver alcance y construir la expresion geografica con fuentes SQL fijas.
- [ ] **1.2** Añadir pruebas de Haversine con coordenadas Toulouse, Paris y Mallorca.
- [ ] **1.3** Añadir pruebas de vigencia que acepten una ubicacion activa de hasta 24 horas y rechacen una mas antigua.
- [ ] **1.4** Añadir pruebas del modo `radius` para borde incluido, exterior excluido y candidato sin coordenadas incluido al final.
- [ ] **1.5** Añadir pruebas de `country` para Francia, España, radio ignorado y candidato sin pais al final.
- [ ] **1.6** Añadir pruebas de `specific_country` para Toulouse y Paris dentro de Francia sin aplicar radio.
- [ ] **1.7** Añadir pruebas de `world` sin exclusiones y con distancia opcional.
- [ ] **1.8** Ejecutar la suite backend focalizada de geografia antes de modificar el controlador.

- - -

## Fase 2: Feed Backend Correcto y Seguro

- [ ] **2.1** Definir validacion Zod del query del feed, incluyendo valores canonicos y limites admitidos.
- [ ] **2.2** Implementar la seleccion simetrica de residencia o ubicacion activa vigente para buscador y candidatos.
- [ ] **2.3** Parametrizar coordenadas, radio, paises, limite y offset en PostgreSQL sin interpolar entradas del usuario.
- [ ] **2.4** Aplicar el filtro duro de radio y la excepcion controlada de candidatos con geografia desconocida.
- [ ] **2.5** Aplicar `country` y `specific_country` por residencia, sin radio y sin aceptar paises conocidos incorrectos.
- [ ] **2.6** Mantener `world` sin exclusiones geograficas y calcular distancia cuando sea posible.
- [ ] **2.7** Ordenar candidatos con geografia verificada antes de las excepciones desconocidas.
- [ ] **2.8** Calcular `is_traveler` con ubicacion activa vigente y separacion superior a 10 km, independientemente del switch del buscador.
- [ ] **2.9** Devolver `LOCATION_REQUIRED` cuando `radius` no tenga origen util.
- [ ] **2.10** Devolver `RESIDENCE_COUNTRY_REQUIRED` cuando `country` no tenga pais de residencia.
- [ ] **2.11** Conservar exclusiones de cuenta propia, likes y dislikes y la carga localizada de fotos e intereses.
- [ ] **2.12** Ejecutar pruebas focalizadas del controlador y corregir cualquier regresion.

- - -

## Fase 3: Contratos Privados y Documentacion Backend

- [ ] **3.1** Extender `GET /user` con coordenadas de residencia, coordenadas activas y `last_location_updated_at` solo para el propietario autenticado.
- [ ] **3.2** Confirmar mediante pruebas que feed y perfil ajeno no exponen coordenadas ni timestamp.
- [ ] **3.3** Verificar la validacion existente de coordenadas de residencia y completar cualquier hueco del multipart.
- [ ] **3.4** Actualizar OpenAPI con campos privados, query canonico y errores 422.
- [ ] **3.5** Actualizar referencia API, referencia de codigo y documentacion geografica del backend.
- [ ] **3.6** Ejecutar `npm test`, `npm run lint` y `npm run check:routes`.

- - -

## Fase 4: Contrato y Persistencia Android

- [ ] **4.1** Implementar `SearchScope.toWireValue()` y `SearchScope.fromWireValue()`.
- [ ] **4.2** Aceptar `my_country` como valor local legado y migrar las escrituras nuevas a `country`.
- [ ] **4.3** Sustituir todos los usos remotos de `enum.name.lowercase()` y `valueOf()` relacionados con el alcance.
- [ ] **4.4** Limpiar el pais objetivo al guardar un alcance distinto de Pasaporte.
- [ ] **4.5** Reordenar el guardado online para actualizar backend antes de DataStore.
- [ ] **4.6** Mantener el borrador y los valores guardados anteriores si falla backend.
- [ ] **4.7** Conservar guardado local directo y determinista en modo demo.
- [ ] **4.8** Añadir pruebas unitarias de conversion, migracion local, exito remoto y fallo remoto.
- [ ] **4.9** Ejecutar las pruebas Android focalizadas de preferencias.

- - -

## Fase 5: Coordenadas de Residencia Extremo a Extremo

- [ ] **5.1** Añadir latitud y longitud de residencia al estado de edicion de perfil.
- [ ] **5.2** Conservar las coordenadas asociadas a la sugerencia geografica seleccionada.
- [ ] **5.3** Invalidar coordenadas anteriores cuando el usuario cambie manualmente pais, region o ciudad sin seleccionar una ubicacion resoluble.
- [ ] **5.4** Propagar las coordenadas por ViewModel, caso de uso, repositorios remoto y demo y datasource.
- [ ] **5.5** Añadir `residence_latitude` y `residence_longitude` al multipart Android.
- [ ] **5.6** Mostrar una validacion accionable si la residencia escrita no puede resolverse cuando sea necesaria para Radio local.
- [ ] **5.7** Añadir pruebas de preservacion, cambio e invalidacion de coordenadas de residencia.
- [ ] **5.8** Ejecutar las pruebas focalizadas de perfil y compilar Kotlin debug.

- - -

## Fase 6: Ubicacion Activa de Primer Plano

- [ ] **6.1** Crear una abstraccion testeable para una captura unica mediante proveedores Android disponibles.
- [ ] **6.2** Usar la ubicacion conocida mas reciente como respaldo y solicitar una lectura actual cuando sea posible.
- [ ] **6.3** Mantener la peticion de permisos contextual y nunca solicitar permiso automaticamente al arrancar.
- [ ] **6.4** Con permiso ya concedido y sesion autenticada y verificada, enviar un ping al entrar en Home.
- [ ] **6.5** Enviar un nuevo ping al volver la app a primer plano con proteccion contra duplicados de la misma sesion.
- [ ] **6.6** Conectar el boton de actualizar ubicacion de preferencias con el ping remoto y el minimapa.
- [ ] **6.7** No ejecutar captura ni ping reales en modo demo.
- [ ] **6.8** Añadir pruebas de permiso ausente, proveedor ausente, respaldo conocido, lectura actual, duplicados y error de red.
- [ ] **6.9** Ejecutar pruebas focalizadas de ubicacion y compilar Kotlin debug.

- - -

## Fase 7: Recarga Reactiva y Concurrencia en Home

- [ ] **7.1** Inyectar el flujo de preferencias en `HomeViewModel`.
- [ ] **7.2** Ignorar la primera emision para evitar una carga duplicada durante la inicializacion.
- [ ] **7.3** Invalidar el mazo y cargar feed cuando cambien preferencias guardadas.
- [ ] **7.4** Cancelar o versionar cargas concurrentes para impedir que una respuesta antigua reemplace la mas reciente.
- [ ] **7.5** Mantener el refresh manual como accion adicional, no necesaria tras guardar.
- [ ] **7.6** Añadir pruebas de recarga automatica, ausencia de doble carga y carrera entre respuestas.
- [ ] **7.7** Ejecutar `HomeViewModelTest` y pruebas relacionadas.

- - -

## Fase 8: Estados Geograficos Accionables

- [ ] **8.1** Mapear `LOCATION_REQUIRED` y `RESIDENCE_COUNTRY_REQUIRED` a errores o estados tipados Android.
- [ ] **8.2** Mostrar para Radio local sin origen acciones para conceder permiso o configurar residencia.
- [ ] **8.3** Mostrar para Todo mi pais sin residencia una accion para configurar residencia.
- [ ] **8.4** Mantener Pasaporte y Todo el mundo disponibles de acuerdo con la especificacion.
- [ ] **8.5** Añadir recursos localizados en español e ingles.
- [ ] **8.6** Verificar objetivos tactiles de 48 dp, TalkBack, tema claro y tema oscuro.
- [ ] **8.7** Añadir pruebas de estado y acciones de recuperacion.

- - -

## Fase 9: Paridad del Modo Demo

- [ ] **9.1** Aplicar el mapeo canonico de alcances en demo.
- [ ] **9.2** Aplicar la seleccion simetrica de fuente y la caducidad de 24 horas.
- [ ] **9.3** Incluir candidatos sin geografia al final y sin distancia inventada.
- [ ] **9.4** Aplicar pais propio y Pasaporte solo sobre residencia.
- [ ] **9.5** Asegurar que Todo el mundo nunca filtra por geografia.
- [ ] **9.6** Añadir timestamps deterministas o reloj inyectable para las pruebas.
- [ ] **9.7** Cubrir Toulouse, Paris, Mallorca, datos ausentes y ubicacion caducada.
- [ ] **9.8** Ejecutar las pruebas focalizadas del repositorio demo.

- - -

## Fase 10: Verificacion Integral

- [ ] **10.1** Ejecutar `npm test` en backend.
- [ ] **10.2** Ejecutar `npm run lint` en backend.
- [ ] **10.3** Ejecutar `npm run check:routes` en backend.
- [ ] **10.4** Ejecutar `.\gradlew.bat testDebugUnitTest` en Android.
- [ ] **10.5** Ejecutar `.\gradlew.bat assembleDebug` en Android.
- [ ] **10.6** Ejecutar `.\gradlew.bat lintDebug` y separar incidencias nuevas de bloqueos preexistentes.
- [ ] **10.7** Confirmar que no se han incluido secretos, logs locales ni archivos generados.
- [ ] **10.8** Revisar el diff completo de ambos repositorios sin crear commits.

- - -

## Fase 11: Verificacion en Dispositivo o Emulador

- [ ] **11.1** Validar permiso denegado con residencia disponible.
- [ ] **11.2** Validar Radio local sin permiso ni residencia y sus acciones de recuperacion.
- [ ] **11.3** Validar ping con permiso concedido y retorno desde segundo plano.
- [ ] **11.4** Validar que Paris no aparece desde Toulouse con 50 km ni 100 km.
- [ ] **11.5** Validar que Todo mi pais Francia excluye España e ignora el radio.
- [ ] **11.6** Validar que Pasaporte Francia permite Toulouse y Paris.
- [ ] **11.7** Validar que guardar y volver recarga tarjetas sin swipe manual.
- [ ] **11.8** Validar candidato sin ubicacion al final y sin distancia.
- [ ] **11.9** Validar rotacion, modo claro, modo oscuro y accesibilidad basica.
- [ ] **11.10** Documentar claramente cualquier escenario que no pueda verificarse por falta de dispositivo, cuentas o backend operativo.

- - -

## Criterio de Finalizacion

La correccion solo se considera terminada cuando las fases aplicables estan marcadas, los contratos Android y backend coinciden, las pruebas automatizadas relevantes pasan y la validacion manual confirma el comportamiento real o deja documentado el limite exacto de verificacion.

## Evidencia de esta ejecucion

- Backend: `npm test` correcto, 48 tests superados y 1 omitido.
- Backend: `npm run lint` correcto.
- Backend: `npm run check:routes` correcto, 46 operaciones documentadas.
- Android: pruebas focalizadas de Home, perfil, preferencias, errores de red y repositorios demo correctas.
- Android: `assembleDebug` correcto.
- Android: `compileDebugKotlin` correcto con compatibilidad desde API 26.
- Android: suite completa con 364 tests correctos y 2 fallos ajenos en `DemoModeManagerTest`.
- Android: el primer lint detecto una llamada API 30 introducida y otra ya existente. Ambas rutas usan ahora el lector compatible comun. No se completo un segundo informe integral de lint durante esta ejecucion.
- Pendiente: recorrido real con dos cuentas y ubicaciones Toulouse, Paris y Mallorca en backend operativo.
- Pendiente: validacion visual y de ciclo de vida en dispositivo o emulador.
