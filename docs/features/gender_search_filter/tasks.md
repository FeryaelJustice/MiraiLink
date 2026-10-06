# Desglose de Tareas: Filtro de Búsqueda por Género y Desbloqueo Premium

- **Plan técnico asociado**: `docs/features/gender_search_filter/plan.md`
- **Especificación funcional**: `docs/features/gender_search_filter/spec.md`
- **Estado**: Completado con éxito
- **Módulos**: `:app` (`com.feryaeljustice.mirailink`) y `MiraiLink-Backend` (`Express 5 + PostgreSQL`)

- - -

## Fase 1: Backend Database & Endpoints (`MiraiLink-Backend`)

- [x] **Tarea 1.1**: Crear migraciones `011_search_gender_filter.sql` y `012_restrict_user_gender_and_search_defaults.sql` añadiendo columnas `search_gender`, `target_gender`, restringiendo `gender` exclusivamente a `('male', 'female')` y fijando el valor predeterminado en `'all'`.
- [x] **Tarea 1.2**: Actualizar esquemas de validación Zod en `src/validation/user.schemas.js`, `src/validation/explore.schemas.js` y `src/validation/auth.schemas.js` para admitir `'male'`, `'female'`, `'all'` en búsqueda y exclusivamente `'male'` o `'female'` en perfil.
- [x] **Tarea 1.3**: Actualizar `src/controllers/user.controller.js` (`updateSearchSettings` y `getProfile`) permitiendo `'all'` libremente a usuarios Free y requiriendo Plus/Premium únicamente para filtrar por `'male'` o `'female'`.
- [x] **Tarea 1.4**: Actualizar `src/controllers/swipe.controller.js` (`getFeed`) buscando por defecto a `'all'` mezclados en Free y aplicando el filtro seleccionado únicamente cuando el usuario cuenta con suscripción Plus/Premium.
- [x] **Tarea 1.5**: Actualizar `src/services/explore.service.js` aplicando el filtro de género en `getCategoryFeedUsers` y contadores `active_count` con `'all'` por defecto para cuentas Free.
- [x] **Tarea 1.6**: Actualizar `src/controllers/explore.controller.js` (`updateCategorySettings`) validando permisos Plus/Premium únicamente al intentar guardar `'male'` o `'female'`.
- [x] **Tarea 1.7**: Escribir y ejecutar pruebas en Backend con Vitest (110 tests pasando) para validar filtros de género, `'all'` en cuentas Free y protección de `'male'` / `'female'`.

- - -

## Fase 2: Capa de Dominio y Datos (Android)

- [x] **Tarea 2.1**: Actualizar enums de dominio: `TargetSearchGender.kt` con `defaultFor(...) == ALL` y `Gender.kt` con exclusivamente `Male` y `Female`.
- [x] **Tarea 2.2**: Extender `SearchPreferences.kt` con `searchGender: TargetSearchGender` y actualizar `CategoryPreference.kt` con `targetGender: TargetSearchGender?`.
- [x] **Tarea 2.3**: Actualizar DTOs de request y response en `data/model/` (`UpdateSearchSettingsRequest`, `UpdateCategorySettingsRequest`, `UserProfileResponse`).
- [x] **Tarea 2.4**: Actualizar mapeadores en `data/mappers/` y repositorios `SearchPreferencesRepositoryImpl` y `ExploreRepositoryImpl`.
- [x] **Tarea 2.5**: Actualizar `FaqRepositoryImpl.kt` reflejando la búsqueda de Todos por defecto en cuentas Free y el desbloqueo de selección hombre/mujer en Plus/Premium.

- - -

## Fase 3: Capa de Presentación UI (Android)

- [x] **Tarea 3.1**: Actualizar strings localizados en `res/values/strings.xml`, `values-es/strings.xml` y `values-en/strings.xml` (eliminando `gender_other`, actualizando disclaimers de plan Free y FAQ).
- [x] **Tarea 3.2**: Actualizar `SearchSettingsSection.kt` integrando el selector de género: chip "Todos" completamente gratuito y activo por defecto; chips "Mujeres" y "Hombres" con badge `(Plus)` y redirección a Paywall en cuentas Free.
- [x] **Tarea 3.3**: Actualizar `SearchPreferencesViewModel.kt` para gestionar el estado reactivo draft de `searchGender`.
- [x] **Tarea 3.4**: Actualizar `CategoryDiscoverySettingsSheet.kt` y `CategoryFeedViewModel.kt` con lógica idéntica (chip "Todos" libre y predeterminado, "Mujeres" y "Hombres" bloqueados en Free).
- [x] **Tarea 3.5**: Actualizar `SubscriptionPaywallScreen.kt` agregando el beneficio del filtro de género a la columna de ventajas de MiraiLink Plus.

- - -

## Fase 4: Documentación

- [x] **Tarea 4.1**: Actualizar `docs/guia-de-funcionamiento-app.md` documentando el sistema de generos binario (Hombre y Mujer), la búsqueda de Todos por defecto en Free y el filtrado específico en Plus/Premium.

- - -

## Fase 5: Verificación Integral, Tests y Builds

- [x] **Tarea 5.1**: Ejecutar suite de pruebas unitarias en Android (`.\gradlew.bat testDebugUnitTest`).
- [x] **Tarea 5.2**: Verificar compilación limpia de Android (`.\gradlew.bat assembleDebug`).
- [x] **Tarea 5.3**: Ejecutar suite de pruebas en Backend (`npm test`).
- [x] **Tarea 5.4**: Elaborar walkthrough final documentando las modificaciones en ambas ramas.
