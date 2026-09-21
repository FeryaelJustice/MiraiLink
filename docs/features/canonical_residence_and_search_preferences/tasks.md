# Checklist de tareas: Residencia Canonica y Preferencias de Busqueda Separadas

- **Especificación**: `spec.md`
- **Plan técnico**: `plan.md`
- **Estado**: [EN CURSO]

## Fase 0: Preparación y seguridad

- [ ] Crear rama aislada desde el estado actual sin alterar cambios ajenos.
- [ ] Inventariar estado Git de Android y backend y confirmar que solo se preparan archivos de esta funcionalidad.
- [ ] Registrar la versión y atribución del dataset GeoNames seleccionada.

## Fase 1: Catálogo y migración backend

- [ ] Añadir fuente GeoNames e importador reproducible de países, regiones, ciudades y traducciones ES/EN.
- [ ] Crear migración de catálogo geográfico, referencias de residencia y `user_search_preferences`.
- [ ] Migrar datos heredados inequívocos y dejar sin referencias los ambiguos.
- [ ] Eliminar columnas legacy de residencia textual y preferencias de `users`.
- [ ] Actualizar esquema base, reset y seed para obtener el mismo resultado en una base nueva.
- [ ] Añadir índices, restricciones jerárquicas y validaciones de coordenadas.

## Fase 2: API, DTOs y algoritmo backend

- [ ] Añadir rutas de catálogo geográfico localizado con fallback español.
- [ ] Actualizar perfil, DTOs, validación Zod y OpenAPI para UUID de residencia y etiquetas localizadas.
- [ ] Mover lectura y escritura de preferencias a `user_search_preferences`.
- [ ] Actualizar el feed para usar IDs de país y coordenadas persistidas de residencia.
- [ ] Añadir pruebas de migración, catálogo, perfil, preferencias y radio.
- [ ] Ejecutar lint, tests y comprobación de rutas backend.

## Fase 3: Datos, dominio y demo Android

- [ ] Crear modelos de catálogo geográfico y contratos de repositorio.
- [ ] Actualizar DTOs, mapeadores, requests Retrofit y repositorios de usuario y preferencias.
- [ ] Crear datasource, servicio API, repositorio e inyección de catálogo geográfico.
- [ ] Migrar entidades, DAOs, seed y repositorios Room demo a IDs geográficos y preferencias separadas.
- [ ] Actualizar filtro demo para comparar UUID de país y coordenadas de ciudad.

## Fase 4: Perfil, selector y minimapa Android

- [ ] Sustituir el selector basado en `Geocoder` por selector dependiente de catálogo remoto o Room.
- [ ] Guardar IDs y coordenadas canónicas de la ciudad en el estado y en `PUT /user`.
- [ ] Implementar reparación controlada de perfiles canónicos sin coordenadas.
- [ ] Actualizar preferencias de búsqueda para país objetivo UUID y carga de origen canónico.
- [ ] Verificar supervivencia a rotación, pérdida de red y guardado fallido.

## Fase 5: Validación y entrega

- [ ] Añadir y ejecutar pruebas Android de mapeo, selector, ViewModel y filtro demo.
- [ ] Ejecutar `compileDebugKotlin` y `testDebugUnitTest`.
- [ ] Verificar en emulador o dispositivo el selector, minimapa, radio desde residencia, idioma y estados de error.
- [ ] Ejecutar revisión de cambios, actualizar documentación API y checklist SDMD.
- [ ] Preparar commit, push y pull request solo si el propietario lo solicita.
