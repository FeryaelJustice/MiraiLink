# [APROBADO] Especificación Funcional: Filtro de Búsqueda por Género y Desbloqueo Premium

- **Fecha**: 2026-10-05
- **Estado**: [APROBADO]
- **Autor / Responsable**: Antigravity & ArisGuimera SDMD Protocol
- **Módulos Afectados**: `:app` (`com.feryaeljustice.mirailink`) y backend `MiraiLink-Backend` (`Express 5 + PostgreSQL`)

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**:
  Actualmente en MiraiLink los algoritmos de recomendación y descubrimiento (Discovery general en Home y Discovery por categorías en Explore) solo contemplan filtros de distancia geográfica y país. No existe un filtro por género a buscar (`target_gender`). Los usuarios reciben perfiles de cualquier género sin distinción, lo cual no se ajusta a las expectativas habituales de una aplicación social y de citas.
  
- **Objetivo**:
  1. Incorporar el filtro de género a buscar en las dos modalidades de Discovery:
     - **Discovery General**: En `SearchPreferencesScreen` / `SearchSettingsSection`.
     - **Discovery por Categorías**: En `CategoryDiscoverySettingsSheet` y feeds temáticos.
  2. Establecer una regla de negocio de monetización:
     - **Plan Gratuito (Free)**: El selector de género aparece bloqueado para edición. Aplica de manera fija e inmutable el comportamiento predeterminado heterosexual: si el usuario es hombre busca mujeres, si es mujer busca hombres, y si es no binario / other busca todos.
     - **Planes de Pago (Plus y Premium)**: Se desbloquea completamente la edición desde el plan MiraiLink Plus (el más accesible) y se mantiene desbloqueado en Premium. El usuario suscrito puede alternar libremente su preferencia entre Hombres, Mujeres o Todos.
  3. Informar de manera transparente:
     - Mensaje explicativo / hint visible en la propia sección de filtros.
     - Sección dedicada en Preguntas Frecuentes (FAQ) bajo `CARDS_AND_MATCHING` y `SUBSCRIPTIONS_AND_PAYMENTS`.
     - Actualización de la documentación de funcionamiento general (`guia-de-funcionamiento-app.md`).
     - Inclusión del nuevo beneficio en la lista de ventajas de MiraiLink Plus en `SubscriptionPaywallScreen`.
  4. Garantizar sincronización y coherencia integral:
     - Persistencia en backend (`user_search_preferences` y `user_category_preferences` en PostgreSQL).
     - Filtrado estricto en la generación de candidatos de `getFeed` (`swipe.controller.js`) y `getCategoryFeedUsers` / calculo de contadores en `explore.service.js`.

- - -

## 2. Situación Actual

- En el cliente Android:
  - `SearchSettingsSection.kt` únicamente expone slider de radio en kilometros (10 a 800 km) y chips de alcance (`SearchScope`).
  - `CategoryDiscoverySettingsSheet.kt` únicamente expone un slider de radio en kilometros (10 a 500 km).
  - `SubscriptionPaywallScreen.kt` incluye ventajas de Plus referentes a anuncios, radio ampliado, likes recibidos e insignia, pero no menciona el filtro de género.
  - No existe ningún modelo o campo para `target_gender` ni en `SearchPreferences` ni en `CategoryPreference`.
- En el backend (`MiraiLink-Backend`):
  - `user_search_preferences` almacena `search_radius_km`, `search_scope`, `search_target_country_id`, `search_match_live_location`, pero no almacena género objetivo.
  - `user_category_preferences` almacena `category_id` y `radius_km`, sin columna de género.
  - La consulta SQL de feed en `swipe.controller.js` y `explore.service.js` ignora `u.gender` de los candidatos, devolviendo cualquier perfil sin importar si el usuario actual busca hombres o mujeres.

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)

- **Backend MiraiLink**:
  - Migración de base de datos `011_search_gender_filter.sql`:
    - Agregar columna `search_gender VARCHAR(20) DEFAULT NULL CHECK (search_gender IN ('male', 'female', 'all'))` a `user_search_preferences`.
    - Agregar columna `target_gender VARCHAR(20) DEFAULT NULL CHECK (target_gender IN ('male', 'female', 'all'))` a `user_category_preferences`.
  - Lógica de resolución de género por defecto en backend:
    - Si el usuario es Free o no tiene selección personalizada:
      - Si `user.gender == 'male'` -> `female`
      - Si `user.gender == 'female'` -> `male`
      - Si `user.gender == 'other'` o equivalente -> `all`
    - Si el usuario es Plus o Premium: permite `female`, `male` o `all`.
    - Protección 403 / reajuste: si un usuario Free intenta guardar un valor distinto al default o un endpoint de actualización recibe intento de cambio sin suscripción Plus/Premium activa (`PREMIUM_GENDER_FILTER_REQUIRED`).
  - Filtrado SQL en Discovery General (`getFeed` en `swipe.controller.js`) y Category Discovery (`getCategoryFeedUsers` y conteo de `getExploreSectionsWithCategories` en `explore.service.js`).
  - Endpoints actualizados:
    - `PUT /api/v1/user/search-settings` (soporta `search_gender`).
    - `PUT /api/v1/explore/categories/:categoryId/settings` (soporta `target_gender`).
- **Android - Capa Data y Dominio**:
  - Actualizar `SearchPreferences` con campo `searchGender: TargetSearchGender`.
  - Actualizar `CategoryPreference` con campo `targetGender: TargetSearchGender?`.
  - Nuevo enum de dominio `TargetSearchGender` (`FEMALE`, `MALE`, `ALL`).
  - Actualizar use cases y repositorios correspondientes (`SaveSearchPreferencesUseCase`, `UpdateCategoryPreferencesUseCase`).
  - Lógica de resolución predeterminada en ViewModel según el género del usuario autenticado.
- **Android - Capa de Presentación (UI)**:
  - `SearchSettingsSection.kt`:
    - Componente selector de género a buscar (Chips segmentados: Mujeres, Hombres, Todos).
    - Si Free: bloqueado con candado/badge "Plus", deshabilitado para edición, con nota informativa y al pulsar abre directamente `onNavigateToPaywall()`.
    - Si Plus o Premium: interactivo y editable.
  - `CategoryDiscoverySettingsSheet.kt`:
    - Componente selector de género en la hoja de ajustes de la categoría.
    - Mismo comportamiento de bloqueo según estado de suscripción.
  - `SubscriptionPaywallScreen.kt`:
    - Nueva ventaja en lista de MiraiLink Plus: "Filtro de género de búsqueda desbloqueado".
  - `FAQScreen.kt` y `FaqRepositoryImpl.kt`:
    - Nuevo ítem de FAQ explicando el funcionamiento del filtro de género por defecto y el desbloqueo con Plus/Premium.
  - Documentación:
    - Actualización de `docs/guia-de-funcionamiento-app.md`.
- **Internacionalización (i18n)**:
  - Textos localizados en español (`values-es`), inglés (`values-en`) y japones (`values-ja`).

### 3.2. Fuera del Alcance (Out of Scope)

- Filtros por rangos de edad (se mantiene para futuros hitos).
- Filtros por altura, habitos o signos zodiacales avanzados.
- Planes adicionales distintos a Free, Plus y Premium.

- - -

## 4. Casuísticas y Comportamiento Mobile

- **Comportamiento en Modo Online vs Modo Offline Demo**:
  - En **Modo Online**: Las preferencias de género se envían al backend y se validan con la suscripción en PostgreSQL. Los feeds devueltos cumplen estrictamente el filtro SQL.
  - En **Modo Offline / Demo**: La base de datos local Room y `DemoUserGenerator` respetan el género predeterminado y permiten alternar el estado Premium en depuración para probar el desbloqueo del filtro.
- **Ciclo de Vida y Recuperación de Estado**:
  - En `SearchPreferencesViewModel` y `CategoryFeedViewModel`, la selección de género se mantiene en `StateFlow` y sobrevive a rotaciones de pantalla.
- **Manejo de Errores y Validaciones**:
  - Si un usuario Free envía una petición manipulada para cambiar el género, el backend responde con error 403 `PREMIUM_GENDER_FILTER_REQUIRED`.
  - La UI muestra de inmediato una indicación clara de que MiraiLink Plus desbloquea esta funcionalidad.
- **Ergonomía y Accesibilidad**:
  - Chips de género con dimensiones minimas de 48 x 48 dp.
  - Descripciones de accesibilidad para lectores de pantalla.
  - Temas claro y oscuro soportados en Jetpack Compose Material 3.

- - -

## 5. Criterios de Aceptación (Formato Given - When - Then)

### Criterio 1: Comportamiento por defecto en usuario masculino con Plan Free
- **Dado que**: Un usuario registrado con género "male" (Hombre) tiene suscripción gratuita (Free).
- **Cuando**: Entra en `SearchPreferencesScreen` o en `CategoryDiscoverySettingsSheet`.
- **Entonces**: El filtro de género muestra "Mujeres" seleccionado, en estado bloqueado (no editable), con una nota explicativa y un aviso de que MiraiLink Plus permite personalizar el filtro. Al pulsar sobre el selector, se abre el Paywall. El feed únicamente devuelve usuarias de género femenino.

### Criterio 2: Comportamiento por defecto en usuario femenino con Plan Free
- **Dado que**: Una usuaria registrada con género "female" (Mujer) tiene suscripción gratuita (Free).
- **Cuando**: Entra en los filtros de Discovery general o de categoría.
- **Entonces**: El filtro de género muestra "Hombres" seleccionado de forma bloqueada, y el feed únicamente devuelve usuarios de género masculino.

### Criterio 3: Comportamiento por defecto en usuario con género 'other' con Plan Free
- **Dado que**: Un usuario registrado con género "other" tiene suscripción gratuita (Free).
- **Cuando**: Entra en los filtros de búsqueda.
- **Entonces**: El filtro de género muestra "Todos" seleccionado de forma bloqueada, y el feed devuelve perfiles sin filtro de género restrictivo.

### Criterio 4: Desbloqueo y personalización con MiraiLink Plus o Premium
- **Dado que**: Un usuario adquiere la suscripción MiraiLink Plus o MiraiLink Premium.
- **Cuando**: Accede a los filtros de búsqueda.
- **Entonces**: El selector de género aparece totalmente desbloqueado y editable, permitiendo elegir entre "Mujeres", "Hombres" o "Todos". Al guardar, el backend persiste el cambio y los candidatos del feed se actualizan de acuerdo a la selección.

### Criterio 5: Filtro independiente por categoría con fallback al general
- **Dado que**: Un usuario Plus personaliza su género general a "Mujeres" pero en una categoría concreta (ej. Gaming) elige "Todos".
- **Cuando**: Navega en el feed general y luego en el feed de esa categoría.
- **Entonces**: El feed general muestra solo mujeres y la categoría muestra perfiles de todos los generos. Si una categoría no tiene preferencia de género definida, hereda la preferencia general.

### Criterio 6: Información clara en FAQ y Paywall
- **Dado que**: Cualquier usuario consulta la sección de Preguntas Frecuentes o el Paywall de suscripciones.
- **Cuando**: Lee las características de la app.
- **Entonces**: Encuentra la explicación detallada del filtro por defecto heterosexual en FAQ y observa "Filtro de género de búsqueda desbloqueado" listado entre las ventajas exclusivas de MiraiLink Plus.

- - -

## 6. Decisiones Consolidadas

1. **Opciones del selector**: Tres opciones: "Mujeres", "Hombres" y "Todos".
2. **Género 'other' en plan Free**: Búsqueda predeterminada de "Todos" (sin exclusión).
3. **Alcance por Categoría vs Global**: Independiente por categoría en `user_category_preferences.target_gender` con fallback a `user_search_preferences.search_gender`.
4. **Interacción con filtro bloqueado en Free**: Chips deshabilitados con distintivo de Plus y apertura directa del Paywall al pulsarlos.
