# [APROBADO] Plan Tecnico de Arquitectura: Likes Recibidos, Detalle de Usuario por Username y Perfil Extendido

- **Especificacion funcional asociada**: `docs/features/likes_and_extended_profile_details/spec.md`
- **Estado**: [APROBADO]
- **Fecha**: 2026-09-24
- **Modulos Afectados**: `:app` (`com.feryaeljustice.mirailink`), `MiraiLink-Backend` (Express 5 + PostgreSQL)

- - -

## 1. Hechos Verificados en el Proyecto (Sin Alucinaciones)

Informacion verificada rigurosamente en `gradle/libs.versions.toml`, `app/build.gradle.kts` y `package.json`:

- **Android Stack**:
  - Lenguaje & JVM: Kotlin `2.4.10` / Java 17
  - Build Tool: AGP `9.4.1`, KSP `2.3.8`
  - SDK Targets: Min SDK `26`, Compile SDK `37`, Target SDK `37`
  - UI: Jetpack Compose BOM `2026.09.00` con Material 3 y adaptive `1.3.0`
  - Navegacion: Navigation 3 (`androidx.navigation3:navigation3-core:1.1.7`)
  - Inyeccion de Dependencias: Koin BOM `4.2.2` con `koin-androidx-compose`
  - Persistencia Local: Room `2.8.5` y Encrypted DataStore `1.2.1`
  - Red & Serializacion: Retrofit `3.0.0`, OkHttp `5.5.0`, Kotlinx Serialization `1.11.0`
  - Imagenes: Coil `2.7.0`
  - Testing: JUnit `4.13.2`, MockK `1.14.11`, Turbine `1.2.1`, Coroutines Test `1.10.2`

- **Backend Stack**:
  - Runtime: Node.js con ES Modules (`"type": "module"`)
  - Framework Web: Express `5.2.1`
  - Base de Datos: PostgreSQL driver `pg` `8.23.0`
  - Validacion: Zod `4.6.5`
  - Seguridad & Criptografia: bcrypt `6.0.0`, jsonwebtoken `9.0.3`
  - Testing: Vitest `5.0.1`, Supertest `7.3.0`

- - -

## 2. Impacto Arquitectonico y Contratos en Backend (`MiraiLink-Backend`)

### 2.1. Base de Datos & Migraciones (`src/database/migrations/`)
- **Migracion `007_extended_profile_and_likes.sql`**:
  - Incorporar idioma Japones (`ja`, 'Japanese') en `supported_languages`.
  - Crear tablas de catalogos e i18n (`supported_languages`):
    - `relationship_goals`, `relationship_goal_translations`
    - `family_options`, `family_option_translations`
    - `religions`, `religion_translations`
    - `zodiac_signs`, `zodiac_sign_translations`
    - `political_stances`, `political_stance_translations`
    - `smoking_habits`, `smoking_habit_translations`
    - `drinking_habits`, `drinking_habit_translations`
    - `sexual_orientations`, `sexual_orientation_translations`
    - `education_levels`, `education_level_translations`
    - `spoken_languages`, `spoken_language_translations`
    - `profile_prompts` (8 preguntas gamer precargadas), `profile_prompt_translations`
  - Crear tablas intermedias para relaciones N:M:
    - `user_relationship_goals (user_id, goal_id, PRIMARY KEY)`
    - `user_family_options (user_id, option_id, PRIMARY KEY)` (permite "ya tengo hijos" y "quiero hijos" simultaneamente)
    - `user_spoken_languages (user_id, language_id, PRIMARY KEY)`
    - `user_profile_prompts (id, user_id, prompt_id, answer, created_at, UNIQUE(user_id, prompt_id))` (maximo 3 por usuario)
  - Agregar columnas de atributos directos en `users`:
    - `religion_id UUID REFERENCES religions(id) ON DELETE SET NULL`
    - `zodiac_sign_id UUID REFERENCES zodiac_signs(id) ON DELETE SET NULL`
    - `political_stance_id UUID REFERENCES political_stances(id) ON DELETE SET NULL`
    - `smoking_habit_id UUID REFERENCES smoking_habits(id) ON DELETE SET NULL`
    - `drinking_habit_id UUID REFERENCES drinking_habits(id) ON DELETE SET NULL`
    - `sexual_orientation_id UUID REFERENCES sexual_orientations(id) ON DELETE SET NULL`
    - `education_level_id UUID REFERENCES education_levels(id) ON DELETE SET NULL`
    - `profession VARCHAR(100)`
  - Indices para optimizacion de busqueda:
    - `idx_likes_to_user_created ON likes(to_user_id, created_at DESC)`
    - `idx_users_username ON users(username)`

### 2.2. Validaciones (`src/validation/`)
- `auth.schemas.js`:
  - `registerSchema`:
    - `username`: regex `^[a-zA-Z0-9_.]+$`, longitud 3..30.
    - `email`: formato valido de email.
    - `password`: validacion no trivial.
    - `gender`: enum `['male', 'female', 'non_binary', 'other', 'prefer_not_to_say']` obligatorio.
    - `birthdate`: string ISO fecha (`YYYY-MM-DD`) obligatorio, con refinamiento de edad minima `>= 16` años respecto a la fecha actual.
- `user.schemas.js`:
  - `profileUpdateSchema`:
    - **Eliminar** `birthdate` y `gender` para asegurar inmutabilidad post-registro.
    - Agregar `profession`: string max 100 opcional.
    - Agregar IDs de catalogos opcionales (uuid nullable).
    - Agregar `relationship_goal_ids`: array de UUIDs.
    - Agregar `family_option_ids`: array de UUIDs.
    - Agregar `spoken_language_ids`: array de UUIDs.
    - Agregar `prompts`: array de hasta 3 objetos `{ prompt_id: UUID, answer: string max 300 }`.

### 2.3. Controladores y Rutas (`src/controllers/` y `src/routes/`)
- `swipe.controller.js`:
  - `getReceivedLikes`:
    - Query sobre tabla `likes` filtrando donde `to_user_id = req.user.id` y `from_user_id NOT IN (SELECT to_user_id FROM likes WHERE from_user_id = req.user.id)` (sin match reciproco) y `from_user_id NOT IN (SELECT to_user_id FROM dislikes WHERE from_user_id = req.user.id)` (sin descarte del usuario).
    - JOIN con datos publicos del emisor, su foto principal (posicion 1) y calculo de edad.
    - Ordenado por `likes.created_at DESC`, con paginacion `limit` y `offset`.
- `swipe.routes.js`:
  - `GET /likes-received`: autenticado, valida query paginacion, invoca `getReceivedLikes`.
- `user.controller.js`:
  - `getProfileByUsername`:
    - Consulta usuario publico por `username`.
    - Recupera atributos categorizados localizados segun `req.get('accept-language')` (o fallback a `es`/`en`).
    - Devuelve modelo publico enriquecido incluyendo array de fotos ordenadas, animes, games, curiosidades/prompts, estilo de vida, familia, etc.
  - `updateProfile`:
    - Actualiza campos mutables y tablas intermedias en transaccion.
    - Rechaza explicitamente cualquier intento de alterar `birthdate` o `gender`.
- `user.routes.js`:
  - `GET /by-username/:username`: autenticado, valida parametro, invoca `getProfileByUsername`.

- - -

## 3. Impacto Arquitectonico y Contratos en Android (`MiraiLink`)

### 3.1. Capa de Datos (`data/`)
- **Modelos DTO (`data/model/`)**:
  - `ReceivedLikeDto.kt`: `id`, `fromUserId`, `username`, `nickname`, `birthdate`, `primaryPhotoUrl`, `createdAt`.
  - `ExtendedProfileDto.kt` y sub-DTOs para catalogos traducidos (`CatalogItemDto`), prompts (`PromptAnswerDto`).
  - Actualizar `UserDto.kt` y `RegisterRequestDto.kt` (o llamada en `AuthApiService`).
- **Servicios API (`data/remote/`)**:
  - `SwipeApiService.kt` (o `UserApiService.kt`):
    - `@GET("swipe/likes-received") suspend fun getReceivedLikes(@Query("limit") limit: Int, @Query("offset") offset: Int): Response<List<ReceivedLikeDto>>`
  - `UserApiService.kt`:
    - `@GET("user/by-username/{username}") suspend fun getProfileByUsername(@Path("username") username: String): Response<UserDto>`
- **Modo Demo Offline (`data/local/demo/`)**:
  - Añadir entidades / seeder en `DemoDataSeeder.kt` con datos simulados de likes recibidos y perfiles extendidos con curiosidades gamer.
  - `DemoUserRepositoryImpl.kt`: implementar `getReceivedLikes()` devolviendo flujo con lista simulada, y `getProfileByUsername()` resolviendo el perfil local.
- **Repositorios (`data/repository/`)**:
  - `UserRepositoryImpl.kt`: implementa llamadas remotas con mapeo a modelos de dominio y gestion de errores `MiraiLinkResult<T>`.
  - `SocialRepositoryImpl.kt` / `SwipeRepositoryImpl.kt`: gestiona `getReceivedLikes()`, `likeUser()`, `dislikeUser()`.

### 3.2. Capa de Dominio (`domain/`)
- **Modelos de Dominio (`domain/model/`)**:
  - `ReceivedLike`: emisor del like, fecha, edad, fotos, username.
  - `GamerPromptAnswer`: id de pregunta, pregunta localizada, respuesta del usuario.
  - `ExtendedProfile`: modelo completo del perfil enriquecido.
- **Casos de Uso (`domain/usecase/`)**:
  - `GetReceivedLikesUseCase`: consulta la lista de likes recibidos.
  - `GetUserProfileByUsernameUseCase`: consulta un perfil por su nombre de usuario.
  - `RegisterUseCase`: incluye `birthdate` y `gender` con validacion de 16 años.
  - `SwipeLikeUserUseCase` / `SwipeDislikeUserUseCase`: invoca like o dislike provocando eliminacion de la lista de likes recibidos.

### 3.3. Capa de Presentacion (`ui/`)
- **Navegacion (`ui/navigation/`)**:
  - `AppScreen.ReceivedLikesScreen: AppScreen()` (3er tab).
  - `AppScreen.UserProfileDetailScreen(val username: String): AppScreen()`.
  - En `MiraiLinkBottomBar.kt`: nuevo item en tercera posicion con `ic_heart`.
  - En `NavWrapper.kt`: registrar entradas correspondientes en `entryProvider`, configurar deep link handling para `mirailink.com/user/{username}`.
- **Pantalla "Gente a la que le gustas" (`ui/screens/likes/`)**:
  - `ReceivedLikesViewModel`: gestiona `UiState` (Loading, Success, Empty, Error), paginacion y acciones de like/dislike que remueven reactivamente el elemento de la lista.
  - `ReceivedLikesScreen`: `LazyColumn` con items que muestran avatar circular, nombre, edad y boton de accion rapida de Match.
  - Componente `PremiumLockedState` preparado para alternar con un toggle futuro.
- **Pantalla de Detalle de Usuario (`ui/screens/profile/detail/`)**:
  - `UserProfileDetailViewModel`: recibe `username` (desde `SavedStateHandle`), carga el perfil extendido.
  - `UserProfileDetailScreen`:
    - Carrusel de fotos a altura fija con contador visual y gesto de pulsacion prolongada (`FullscreenImagePreview`).
    - Boton volver desacoplado (`onBackClick`).
    - Boton "Compartir perfil" mediante Android Share Sheet.
    - Secciones categorizadas: Datos Basicos, Facts Gamer (tarjetas tematicas), Estilo de Vida, Familia, Creencias, Educacion & Profesion, Idiomas, Intereses (animes y juegos).
    - Botones flotantes o fijos de Like / Rechazar (visibles cuando el perfil procede de likes o no hay match; ocultos si se accede desde un chat existente con match).
- **Integracion en Chat (`ui/components/topbars/ChatTopBar.kt`)**:
  - Tap simple en avatar -> ejecuta callback `onNavigateToProfile(username)`.
  - Long press en avatar -> mantiene apertura de imagen a pantalla completa.
- **Registro en `AuthScreen.kt`**:
  - Selectores `BirthdateField` y `GenderSelector` en formulario de registro.
  - Comprobacion de edad mínima de 16 años. Si es menor, muestra snackbar localizado: "Debes tener al menos 16 años para registrarte".
  - Retiro completo de `birthdate` y `gender` del formulario de edicion `UserCard.kt` y `EditProfileUiState.kt`.

### 3.4. Inyeccion de Dependencias (`di/koin/`)
- Registrar nuevos casos de uso en `UseCaseModule.kt`.
- Registrar `ReceivedLikesViewModel` y `UserProfileDetailViewModel` en `ViewModelModule.kt`.

- - -

## 4. Estrategia de Testing

- **Pruebas Unitarias Android (`app/src/test/`)**:
  - `GetReceivedLikesUseCaseTest`: prueba de emision y mapeo de datos.
  - `GetUserProfileByUsernameUseCaseTest`: validacion de recuperacion correcta por username.
  - `RegisterUseCaseTest`: prueba de validacion estricta de edad (rechazo con < 16 años, exito con >= 16 años).
  - `ReceivedLikesViewModelTest`: validar remocion inmediata del usuario de la lista tras emitir accion de like o rechazo.
- **Pruebas Unitarias e Integracion Backend (`tests/`)**:
  - `tests/unit/auth.register.test.js`: rechazo de registro si falta fecha, genero o si edad < 16 años.
  - `tests/unit/user.immutable.test.js`: rechazo de cambios a `birthdate` o `gender` en `updateProfile`.
  - `tests/unit/swipe.likesReceived.test.js`: verificacion de exclusion de usuarios ya matheados o descartados.
  - `tests/unit/user.byUsername.test.js`: verificacion de respuesta completa de perfil por username con localizacion `Accept-Language: ja`, `en`, `es`.
- **Verificacion de Compilacion**:
  - Android: `.\gradlew.bat assembleDebug testDebugUnitTest`
  - Backend: `npm test && npm run check:routes`

- - -

## 5. Riesgos Tecnicos y Mitigaciones

- **Riesgo 1**: Incompatibilidad de esquema de base de datos con cuentas ya registradas sin fecha o genero.
  - **Mitigacion**: En la migracion SQL se asignaran valores por defecto a cuentas antiguas o se permitira NULL solo para usuarios historicos existentes, pero se forzara estricto en toda nueva creacion via Zod.
- **Riesgo 2**: Rendimiento de consulta de perfil extendido con multiples joins y traducciones.
  - **Mitigacion**: Uso de agregaciones JSON o consultas paralelas acotadas (`Promise.all`) con cache de catalogos y filtrado directo por idioma indexado.
- **Riesgo 3**: Desincronizacion de estado al dar like rapido desde la lista de likes recibidos.
  - **Mitigacion**: Actualizacion optimista del estado local de la lista en el ViewModel mientras la peticion de red se completa asincronamente.
