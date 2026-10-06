# [APROBADO] Plan Técnico de Arquitectura: Filtro de Búsqueda por Género y Desbloqueo Premium

- **Especificación funcional asociada**: `docs/features/gender_search_filter/spec.md`
- **Estado**: [APROBADO]
- **Fecha**: 2026-10-05
- **Módulos Afectados**: `:app` (`com.feryaeljustice.mirailink`) y backend `MiraiLink-Backend` (`Express 5 + PostgreSQL`)

- - -

## 1. Hechos Verificados en el Proyecto (Sin Alucinaciones)

Información verificada rigurosamente en `gradle/libs.versions.toml`, `app/build.gradle.kts` y `MiraiLink-Backend/package.json`:

- **Android SDK**: Min SDK `26`, Compile SDK `37`, Target SDK `37`
- **Android Toolchain**: Kotlin `2.4.20`, AGP `9.4.1`, Gradle wrapper, KSP `2.3.12`
- **Librerías en Classpath**:
  - UI: Compose BOM `2026.09.00` con Material 3
  - Inyección de dependencias: Koin BOM `4.2.2` con Koin Annotations
  - Persistencia: Room `2.8.5`, Encrypted DataStore `1.2.1`
  - Red: Retrofit `3.0.0`, OkHttp `5.5.0`
  - Google Play Billing: `billing-ktx:9.1.0`
- **Backend**: Node.js ESM, Express 5, PostgreSQL (`pg`), Zod `3.x`, Vitest

- - -

## 2. Impacto Arquitectónico y Contratos por Capas

### 2.1. Backend (`MiraiLink-Backend`)

#### Base de Datos
- **Migración `011_search_gender_filter.sql`**:
  ```sql
  BEGIN;
  ALTER TABLE user_search_preferences 
  ADD COLUMN search_gender VARCHAR(20) DEFAULT NULL 
  CHECK (search_gender IN ('male', 'female', 'all'));

  ALTER TABLE user_category_preferences 
  ADD COLUMN target_gender VARCHAR(20) DEFAULT NULL 
  CHECK (target_gender IN ('male', 'female', 'all'));
  COMMIT;
  ```

#### Capa de Servicios y Controladores
- **Resolución de Género Objetivo**:
  ```js
  function resolveTargetGender(userGender, preferenceGender, isPlusOrPremium) {
      if (isPlusOrPremium && preferenceGender) {
          return preferenceGender; // 'female', 'male', 'all'
      }
      if (userGender === 'male') return 'female';
      if (userGender === 'female') return 'male';
      return 'all';
  }
  ```
- **Controlador `swipe.controller.js`**:
  - Obtiene `u.gender` del usuario actual y `p.search_gender`.
  - Verifica si el usuario posee suscripción activa Plus o Premium.
  - Calcula `effectiveTargetGender`.
  - Incorpora a `candidate_geo` la condición:
    ```sql
    AND (
        $genderParam = 'all' OR
        ($genderParam = 'female' AND u.gender = 'female') OR
        ($genderParam = 'male' AND u.gender = 'male')
    )
    ```
- **Servicio `explore.service.js`**:
  - Aplica la misma clausula en `getCategoryFeedUsers` (usando `ucp.target_gender` con fallback a `p.search_gender`).
  - Aplica el filtro en `getExploreSectionsWithCategories` para que `active_count` coincida con las personas que el usuario realmente puede ver.
  - `updateCategorySettings`: Si `target_gender` cambia y no es Plus/Premium, rechaza con 403 `PREMIUM_GENDER_FILTER_REQUIRED`.
- **Controlador `user.controller.js`**:
  - `updateSearchSettings`: Valida permiso Plus/Premium si `search_gender` no es nulo o difiere del default. Persiste en `user_search_preferences`.

---

### 2.2. Capa de Dominio Android (`domain/`)

- **Modelo `TargetSearchGender`**:
  ```kotlin
  enum class TargetSearchGender(val wireValue: String) {
      FEMALE("female"),
      MALE("male"),
      ALL("all");

      companion object {
          fun fromWire(value: String?): TargetSearchGender? =
              entries.firstOrNull { it.wireValue.equals(value, ignoreCase = true) }

          fun defaultFor(userGender: String?): TargetSearchGender = when (userGender?.lowercase()) {
              "male" -> FEMALE
              "female" -> MALE
              else -> ALL
          }
      }
  }
  ```
- **Modelos de Negocio Modificados**:
  - `SearchPreferences`: Agrega `searchGender: TargetSearchGender = TargetSearchGender.ALL`.
  - `CategoryPreference`: Agrega `targetGender: TargetSearchGender? = null`.
- **Casos de Uso**:
  - `SaveSearchPreferencesUseCase` y `UpdateCategoryPreferencesUseCase` propagan el nuevo campo.

---

### 2.3. Capa de Datos Android (`data/`)

- **DTOs & Requests**:
  - `UpdateSearchSettingsRequest`: Agrega `@SerialName("search_gender") val searchGender: String? = null`.
  - `UpdateCategorySettingsRequest`: Agrega `@SerialName("target_gender") val targetGender: String? = null`.
  - `UserProfileResponse`: Mapea `search_gender` en `toDomain()`.
  - `CategoryPreferenceDto`: Mapea `target_gender`.
- **Mapeadores**:
  - `SearchSettingsMappers.kt`: Conversión entre `TargetSearchGender` y Strings serializados.
- **FAQ**:
  - `FaqRepositoryImpl`: Agrega ítem `faq_gender_filter` bajo `CARDS_AND_MATCHING` o `SUBSCRIPTIONS_AND_PAYMENTS`.

---

### 2.4. Capa de Presentación Android (`ui/`)

- **`SearchSettingsSection.kt`**:
  - Incorpora sección "Género a buscar" con chips (Mujeres, Hombres, Todos).
  - Bloqueado en Free mostrando el género por defecto según el perfil y badge "Plus". Al hacer click abre el Paywall.
  - Interactivo en Plus y Premium.
- **`CategoryDiscoverySettingsSheet.kt`**:
  - Agrega selector análogo de género para la categoría.
  - Bloqueo en Free con banner informativo y llamada al Paywall.
  - Interactivo en Plus y Premium.
- **`SubscriptionPaywallScreen.kt`**:
  - Muestra "Filtro de género de búsqueda desbloqueado" en la columna de beneficios Plus.
- **Recursos i18n (`res/values/strings.xml`, `values-es`, `values-ja`)**:
  - Claves para labels, descripciones, notas de bloqueo y FAQ.

- - -

## 3. Estrategia de Testing

- **Backend**:
  - Unit tests en Vitest para `swipe.controller.test.js`, `explore.service.test.js` y `user.controller.test.js`.
  - Validar status 403 ante manipulación de payload en Free y status 200 en Plus/Premium.
  - Validar que los feeds y contadores devueltos correspondan al género filtrado.
- **Android**:
  - Unit tests en `SearchPreferencesViewModelTest` y `CategoryFeedViewModelTest`.
  - Tests en `FaqRepositoryImplTest`.
  - Compilación de validación con `.\gradlew.bat testDebugUnitTest assembleDebug`.

- - -

## 4. Riesgos Técnicos y Mitigaciones

- **Riesgo 1**: Incompatibilidad con perfiles existentes que tengan `search_gender = NULL`.
  - **Mitigación**: `COALESCE` en SQL y fallback automático al género opuesto según `users.gender`.
- **Riesgo 2**: Inconsistencia entre el contador `active_count` de categorías y los perfiles reales del feed.
  - **Mitigación**: La misma clausula WHERE de género se inyecta en el conteo de `getExploreSectionsWithCategories` y en `getCategoryFeedUsers`.
