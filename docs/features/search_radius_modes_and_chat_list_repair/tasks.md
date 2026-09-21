# [PENDIENTE DE APROBACION] Checklist de Tareas: Modos de Radio y Reparacion de Chats

- **Fecha**: 2026-09-21
- **Especificacion**: `docs/features/search_radius_modes_and_chat_list_repair/spec.md`
- **Plan**: `docs/features/search_radius_modes_and_chat_list_repair/plan.md`
- **Estado**: [PENDIENTE DE APROBACION]

## Fase 1. Contrato y persistencia de busqueda

- [ ] Definir los alcances `radius_residence` y `radius_active` en Android y eliminar el switch beta de las decisiones nuevas.
- [ ] Migrar preferencias DataStore existentes desde `radius` y `matchByLiveLocation` sin perder radio, pais ni pasaporte.
- [ ] Actualizar DTO, Retrofit, datasource y repositorio de preferencias.
- [ ] Actualizar validacion, guardado y respuesta de preferencias en backend.
- [ ] Actualizar OpenAPI para los alcances nuevos.
- [ ] Escribir pruebas Android de migracion y serializacion de preferencias.
- [ ] Escribir pruebas Vitest de validacion y guardado de preferencias.

## Fase 2. Filtro geografico real y paridad demo

- [ ] Resolver origen y coordenadas de candidato para radio por residencia.
- [ ] Resolver origen y coordenadas de candidato para radio por ubicacion activa fresca durante 24 horas.
- [ ] Devolver error tipado recuperable cuando el buscador usa radio activo sin ubicacion valida.
- [ ] Mantener candidatos sin coordenada aplicable como excepcion sin distancia y con prioridad baja.
- [ ] Aplicar las mismas reglas en `DemoSwipeRepositoryImpl`.
- [ ] Añadir pruebas geodesicas Palma, Inca y Valencia para 250 y 260 km.
- [ ] Añadir pruebas de ubicacion activa caducada, residencia ausente y candidato sin coordenadas.

## Fase 3. Preferencias y minimapa

- [ ] Sustituir el switch por cinco chips mutuamente excluyentes y localizar sus etiquetas y estados accesibles.
- [ ] Mantener el slider habilitado solo en los dos chips de radio.
- [ ] Pasar al minimapa el centro seleccionado por el ViewModel, sin logica de fuente geografica en Compose.
- [ ] Implementar conversion de kilometros geodesicos a pixeles Web Mercator.
- [ ] Elegir zoom y mosaico OSM dinamicos para que el circulo completo quepa con margen entre 10 y 300 km.
- [ ] Conservar centrado, wrapping horizontal y limite vertical de tiles.
- [ ] Mostrar estado recuperable cuando falta ubicacion activa y validar retrato y paisaje.
- [ ] Escribir pruebas de la utilidad de escala y pruebas UI focalizadas de chips, slider y estado de error.

## Fase 4. Contrato de chats y conversaciones

- [ ] Ampliar `GET /chats` para devolver identidad segura del otro miembro en chats privados.
- [ ] Definir la respuesta de grupos con `chatId`, nombre e identidad visual de grupo sin exponer datos privados de miembros.
- [ ] Actualizar DTO Android, dominio, mappers y entradas de vista para distinguir chat privado y grupo.
- [ ] Adaptar la ruta de conversación y repositorio para abrir por `chatId`.
- [ ] Conservar desde Matches la creación o recuperación de chat privado antes de navegar.
- [ ] Corregir `ChatList` y `MessageListItem`: foto, nombre, fallback, accesibilidad y toque solo cuando el destino sea válido.
- [ ] Adaptar demo a las nuevas entradas de conversación si existen conversaciones de grupo.
- [ ] Escribir pruebas backend de respuesta privada y grupo, y pruebas Android de mapper, ViewModel y navegación.

## Fase 5. Integracion y verificacion

- [ ] Mantener la recarga automática de Home al guardar cualquiera de los cinco chips.
- [ ] Ejecutar pruebas Android focalizadas de preferencias, mapa, demo y mensajes.
- [ ] Ejecutar `./gradlew.bat compileDebugKotlin` y `./gradlew.bat assembleDebug`.
- [ ] Ejecutar `npm run lint`, `npm run test:unit` y `npm run check:routes` en backend.
- [ ] Ejecutar `git diff --check` en ambos repositorios.
- [ ] Realizar prueba manual en dispositivo o emulador: Palma 250 y 260 km, ubicación activa sin permiso, recarga al volver, chat privado y grupo si existe.
- [ ] Documentar validaciones realizadas y bloqueos externos o preexistentes, sin hacer commit ni push.

## Bloqueo SDMD

No se modificara codigo de produccion hasta recibir aprobacion explicita de este checklist.
