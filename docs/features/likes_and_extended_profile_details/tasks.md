# Checklist de Tareas: Likes Recibidos, Detalle de Usuario por Username y Perfil Extendido

- **Fase 1: Backend - Migracion de Base de Datos y Datos Semilla**
  - [ ] 1.1 Crear archivo de migracion `src/database/migrations/007_extended_profile_and_likes.sql`
  - [ ] 1.2 Agregar idioma japones (`ja`) en tabla `supported_languages`
  - [ ] 1.3 Crear tablas de catalogos localizados (`relationship_goals`, `family_options`, `religions`, `zodiac_signs`, `political_stances`, `smoking_habits`, `drinking_habits`, `sexual_orientations`, `education_levels`, `spoken_languages`, `profile_prompts`) con sus tablas de traduccion (`_translations`)
  - [ ] 1.4 Crear tablas intermedias para relaciones N:M (`user_relationship_goals`, `user_family_options`, `user_spoken_languages`, `user_profile_prompts`)
  - [ ] 1.5 Agregar columnas de atributos directos en `users` (`profession`, IDs de catalogos directos) e indices
  - [ ] 1.6 Sembrar datos iniciales de catalogo y las 8 preguntas gamer en `es`, `en`, `ja`

- **Fase 2: Backend - Validaciones, DTOs, Controladores y Rutas**
  - [ ] 2.1 Actualizar `src/validation/auth.schemas.js`: exigir `birthdate` (edad minima 16 años) y `gender` en `registerSchema`
  - [ ] 2.2 Actualizar `src/controllers/auth.controller.js`: insertar `birthdate` y `gender` en `register`
  - [ ] 2.3 Actualizar `src/validation/user.schemas.js`: eliminar `birthdate` y `gender` de `profileUpdateSchema`, añadir campos de perfil extendido
  - [ ] 2.4 Actualizar `src/controllers/user.controller.js`: rechazar mutacion de `birthdate`/`gender`, implementar `getProfileByUsername` con atributos localizados
  - [ ] 2.5 Actualizar `src/controllers/swipe.controller.js`: implementar `getReceivedLikes` (filtrando usuarios sin match previo, ordenados desc por fecha)
  - [ ] 2.6 Exponer rutas en `src/routes/swipe.routes.js` (`GET /likes-received`) y `src/routes/user.routes.js` (`GET /by-username/:username`)
  - [ ] 2.7 Actualizar `src/dto/user.dto.js`: incluir `username` en proyeccion publica
  - [ ] 2.8 Escribir tests unitarios en Vitest para registro con edad 16+, likes recibidos y consulta por username

- **Fase 3: Android - Capa de Datos y Dominio (Data & Domain Layers)**
  - [ ] 3.1 Crear DTOs en `data/model/`: `ReceivedLikeDto`, DTOs de catalogo y prompts extendidos
  - [ ] 3.2 Actualizar `UserApiService` y `SwipeApiService` con endpoints remotos
  - [ ] 3.3 Implementar modelos de dominio en `domain/model/`: `ReceivedLike`, `GamerPromptAnswer`, `ExtendedProfile`
  - [ ] 3.4 Implementar casos de uso: `GetReceivedLikesUseCase`, `GetUserProfileByUsernameUseCase`, actualizar `RegisterUseCase` con validacion de 16 años
  - [ ] 3.5 Actualizar repositorios (`UserRepositoryImpl`, `SocialRepositoryImpl` o `SwipeRepositoryImpl`) con mapeos reactivos
  - [ ] 3.6 Actualizar modo Demo Offline: añadir seeder simulado en `DemoDataSeeder.kt` y soporte en `DemoUserRepositoryImpl.kt`

- **Fase 4: Android - Capa de Presentacion y UI**
  - [ ] 4.1 Crear recurso vectorial `ic_heart.xml` en `res/drawable/`
  - [ ] 4.2 Agregar strings localizados en `res/values/strings.xml`, `res/values-es/strings.xml`, `res/values-en/strings.xml` (y soporte ja)
  - [ ] 4.3 Actualizar `MiraiLinkBottomBar.kt`: añadir 3er tab "Gente a la que le gustas"
  - [ ] 4.4 Implementar `ReceivedLikesViewModel` y `ReceivedLikesScreen` con lista lazy, foto circular, nombre, edad, boton de match rapido y componente `PremiumLockedState`
  - [ ] 4.5 Implementar `UserProfileDetailViewModel` y `UserProfileDetailScreen` con carrusel fijo, pantalla completa en pulsacion larga, scroll categorizado, boton de compartir enlace universal y botones condicionales de like/rechazar
  - [ ] 4.6 Actualizar `ChatTopBar.kt`: tap simple en foto navega a `UserProfileDetailScreen` por username, mantener pulsado abre foto completa
  - [ ] 4.7 Actualizar `AuthScreen.kt` y `AuthViewModel.kt`: incluir selectores obligatorios de fecha (con snackbar de error si edad < 16) y genero en registro
  - [ ] 4.8 Limpiar modo edicion de perfil: retirar `birthdate` y `gender` de `UserCard.kt`, `EditProfileUiState.kt` y viewmodels de edicion
  - [ ] 4.9 Actualizar `NavWrapper.kt` y `AppScreen.kt`: registrar destinos de navegacion y gestion de deep links `mirailink.com/user/{username}`

- **Fase 5: Verificacion y Pruebas Finales**
  - [ ] 5.1 Ejecutar suite de pruebas en backend: `npm test && npm run check:routes`
  - [ ] 5.2 Ejecutar pruebas unitarias en Android: `.\gradlew.bat testDebugUnitTest`
  - [ ] 5.3 Compilacion completa de Android: `.\gradlew.bat assembleDebug`
  - [ ] 5.4 Validar que no se haya realizado ningun commit en git en ninguno de los dos repositorios
