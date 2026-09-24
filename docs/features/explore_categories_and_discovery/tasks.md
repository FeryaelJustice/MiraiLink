# Checklist de Tareas: Seccion Explorar (Explore Hub), Feeds Tematicos y Ajustes por Categoria

- **Fase 1: Backend - Migracion de Base de Datos y Sembrado de Categorias**
  - [x] 1.1 Crear migracion `src/database/migrations/008_explore_categories_and_preferences.sql` en `MiraiLink-Backend`
  - [x] 1.2 Definir tabla `explore_categories` con soporte para grupos de seccion (`otaku`, `gaming`, `connections`), iconos y tipo de filtro
  - [x] 1.3 Definir tabla `explore_category_translations` para titulos y descripciones en `es`, `en`, `ja`
  - [x] 1.4 Definir tabla `user_category_preferences` con `PRIMARY KEY (user_id, category_id)` para garantizar idempotencia en `UPSERT`
  - [x] 1.5 Sembrar catalogo inicial de categorias tematicas con traducciones completas

- **Fase 2: Backend - Validaciones, Servicio de Cache, Controladores y Rutas**
  - [x] 2.1 Crear esquemas de validacion Zod en `src/validation/explore.schemas.js`
  - [x] 2.2 Implementar `src/services/explore.service.js` con logica de conteo agregado de personas y cache en memoria con TTL de 5 minutos
  - [x] 2.3 Implementar `src/controllers/explore.controller.js` (`getCategories`, `getCategoryFeed`, `getCategorySettings`, `updateCategorySettings`)
  - [x] 2.4 Crear rutas en `src/routes/explore.routes.js` y montar en `src/app.js` bajo `/api/explore`
  - [x] 2.5 Implementar pruebas en `tests/explore.test.js` y verificar con `npm test`

- **Fase 3: Android - Recursos Graficos y Capa de Dominio**
  - [x] 3.1 Crear icono vectorial `app/src/main/res/drawable/ic_explore.xml` (brujula)
  - [x] 3.2 Agregar cadenas localizadas en `res/values/strings.xml`, `res/values-es/strings.xml`, `res/values-en/strings.xml` (y variantes si aplica)
  - [x] 3.3 Definir modelos de dominio en `domain/model/explore/`: `ExploreCategory.kt`, `ExploreSectionGroup.kt`, `ExploreSection.kt`, `CategoryPreference.kt`
  - [x] 3.4 Definir contrato `domain/repository/ExploreRepository.kt`
  - [x] 3.5 Implementar casos de uso: `GetExploreSectionsUseCase`, `GetCategoryFeedUseCase`, `GetCategoryPreferencesUseCase`, `UpdateCategoryPreferencesUseCase`

- **Fase 4: Android - Capa de Datos (Data Layer, Retrofit y Room Demo)**
  - [x] 4.1 Definir DTOs serializables en `data/model/explore/` y `ExploreApiService.kt`
  - [x] 4.2 Implementar `ExploreRepositoryImpl.kt` para comunicacion remota
  - [x] 4.3 Crear entidad Room `DemoCategoryPreferenceEntity.kt` y `DemoCategoryDao.kt`
  - [x] 4.4 Actualizar `MiraiLinkDemoDatabase.kt` a version 3 con la nueva entidad y DAO
  - [x] 4.5 Implementar `DemoExploreRepositoryImpl.kt` y actualizar `DemoDataSeeder.kt` con datos sembrados locales
  - [x] 4.6 Implementar `DelegatingExploreRepository.kt` para conmutacion transparente

- **Fase 5: Android - Inyeccion de Dependencias (Koin)**
  - [x] 5.1 Actualizar `NetworkModule.kt`: registrar `ExploreApiService`
  - [x] 5.2 Actualizar `DemoModule.kt`: registrar `DemoCategoryDao` y `DemoExploreRepositoryImpl`
  - [x] 5.3 Actualizar `RepositoryModule.kt`: registrar `ExploreRepositoryImpl` y `DelegatingExploreRepository`
  - [x] 5.4 Actualizar `UseCaseModule.kt`: registrar casos de uso de exploracion
  - [x] 5.5 Actualizar `ViewModelModule.kt`: registrar `ExploreViewModel` y `CategoryFeedViewModel`

- **Fase 6: Android - Capa de Presentacion, UI y Navegacion**
  - [x] 6.1 Actualizar `AppScreen.kt`: agregar `AppScreen.ExploreScreen` y `AppScreen.CategoryFeedScreen`
  - [x] 6.2 Actualizar `MiraiLinkBottomBar.kt`: registrar `AppScreen.ExploreScreen` como segundo tab con `ic_explore`
  - [x] 6.3 Implementar componentes atomicos/moleculares para Explore: tarjetas de categoria con contador e icono, cabeceras de seccion y carrusel destacado
  - [x] 6.4 Implementar `ExploreViewModel.kt` y `ExploreScreen.kt` (carrusel Bumble superior + cuadricula 2 columnas Tinder)
  - [x] 6.5 Implementar `CategoryDiscoverySettingsSheet.kt` (slider de distancia 10-500 km y guardado)
  - [x] 6.6 Implementar `CategoryFeedViewModel.kt` y `CategoryFeedScreen.kt` con top bar (titulo, atras, ajustes) y swipe stack
  - [x] 6.7 Actualizar `NavWrapper.kt`: conectar navegacion a `ExploreScreen` y `CategoryFeedScreen`

- **Fase 7: Verificacion, Tests y Documentacion**
  - [x] 7.1 Ejecutar suite de pruebas en backend: `npm test` en `MiraiLink-Backend`
  - [x] 7.2 Implementar y ejecutar pruebas unitarias en Android con MockK y Turbine
  - [x] 7.3 Ejecutar compilacion limpia en Android: `.\gradlew.bat assembleDebug`
  - [x] 7.4 Actualizar documentacion en ambos repositorios (`README.md`, `README.en.md`, `docs/`)
