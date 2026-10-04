# [APROBADO] Plan Tecnico de Arquitectura: Sistema de Deshacer en Discovery e Integridad Temporal (TrustedTime & Timezones)

- **Especificacion funcional asociada**: `docs/features/discovery_undo_and_trusted_time/spec.md`
- **Estado**: [APROBADO]
- **Fecha**: 2026-10-04
- **Modulo**: `:app` (`com.feryaeljustice.mirailink`) y `MiraiLink-Backend`

- - -

## 1. Hechos Verificados en el Proyecto (Sin Alucinaciones)

Informacion verificada rigurosamente en `gradle/libs.versions.toml`, `app/build.gradle.kts` y `MiraiLink-Backend/package.json`:

- **Lenguaje & JVM**: Kotlin `2.4.10` / Java 17 / Node.js 20+
- **Compilador & Build Tool**: AGP `9.4.1`, KSP `2.3.8`
- **SDK Targets**: Min SDK `26`, Compile SDK `36`, Target SDK `36`
- **Librerias Verificadas en el Classpath (Android)**:
  - UI: Compose BOM `2026.09.00` con Material 3
  - Navegacion: Navigation 3 (`androidx.navigation3:navigation3-core:1.1.7`)
  - Inyeccion de Dependencias: Koin BOM `4.2.2` con Koin Annotations
  - Persistencia Local: Room Database `2.8.5` (KSP) y DataStore `1.2.1`
  - Red & Sockets: Retrofit `3.0.0`, OkHttp `5.5.0`, Socket.IO Client `2.1.2`
  - Serializacion: Kotlinx Serialization `1.11.0`
  - Nueva dependencia oficial a anadir: `com.google.android.gms:play-services-time:16.0.1`
- **Librerias y Entorno Verificados (Backend)**:
  - Express `5.2.1`, PostgreSQL (`pg` `8.23.0`), Zod `4.6.5`
  - Testing: Vitest `5.0.1`

- - -

## 2. Impacto Arquitectonico y Contratos por Capas

### 2.1. Backend (`MiraiLink-Backend`)

#### Base de Datos (PostgreSQL)
1. **Migracion `010_timezone_and_swipe_undo_quota.sql`**:
   - Transforma todas las columnas `TIMESTAMP` historicas sin zona horaria a `TIMESTAMPTZ USING <columna> AT TIME ZONE 'UTC'`.
   - Crea tabla de registro de deshaceres:
     ```sql
     CREATE TABLE IF NOT EXISTS user_swipe_undos (
         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
         user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
         target_user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
         action_undone VARCHAR(10) NOT NULL, -- 'like' o 'dislike'
         created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
     );
     CREATE INDEX IF NOT EXISTS idx_user_swipe_undos_user_created 
     ON user_swipe_undos(user_id, created_at DESC);
     ```
2. **Actualizacion de `db.sql`**:
   - Actualiza el esquema de arranque inicial con `TIMESTAMPTZ` en todas las marcas de fecha/hora y anade `user_swipe_undos`.

#### Constantes y Controladores
1. `src/consts/subscriptionConsts.js`:
   ```javascript
   export const UNDO_DAILY_LIMITS = {
       FREE: 1,
       PLUS: 3,
       PREMIUM: 6,
   };
   ```
2. `src/controllers/swipe.controller.js`:
   - `getUndoQuota(req, res, next)`:
     - Comprueba el plan de suscripcion activo del usuario (`FREE`, `PLUS` o `PREMIUM`).
     - Calcula cuota consumida con:
       ```sql
       SELECT COUNT(*)::int AS count, MIN(created_at) AS oldest_undo
       FROM user_swipe_undos
       WHERE user_id = $1 AND created_at >= NOW() - INTERVAL '24 hours'
       ```
     - Determina si existe algun swipe previo en `likes` o `dislikes` que pueda revertirse.
     - Devuelve `{ tier, maxUndos, usedUndos, remainingUndos, resetsAt, hasUndoableSwipe, canUndo }`.
   - `undoSwipe(req, res, next)`:
     - Valida que `remainingUndos > 0`. Si no, retorna error HTTP 403 `DAILY_UNDO_LIMIT_REACHED`.
     - Si no se pasa `toUserId`, localiza el swipe mas reciente en PostgreSQL:
       ```sql
       SELECT 'like' AS action, to_user_id, created_at FROM likes WHERE from_user_id = $1
       UNION ALL
       SELECT 'dislike' AS action, to_user_id, created_at FROM dislikes WHERE from_user_id = $1
       ORDER BY created_at DESC LIMIT 1
       ```
     - Ejecuta transaccion atomica:
       - Si accion es `like`: elimina de `likes` y elimina de `matches` si habia match asociado.
       - Si accion es `dislike`: elimina de `dislikes`.
       - Inserta en `user_swipe_undos`.
     - Obtiene los datos completos del perfil deshecho (mismo DTO publico que `getFeed`) para que la app lo muestre inmediatamente en el mazo de tarjetas.
     - Retorna `{ message, actionUndone, user, quota }`.
3. `src/routes/swipe.routes.js`:
   - `POST /undo`
   - `GET /undo-quota`

### 2.2. Capa de Datos (Android `:app`)

#### Integridad Temporal (`data/time/`)
- `TrustedTimeProvider`:
  - Contrato:
    ```kotlin
    interface TrustedTimeProvider {
        fun getCurrentTimeMillis(): Long
        fun isTimeTrusted(): Boolean
        fun syncWithServerDate(serverTimestampMillis: Long)
    }
    ```
  - Implementacion `TrustedTimeProviderImpl`:
    - Intenta consultar `TrustedTimeClient.computeCurrentUnixEpochMillis()` de Google Play Services.
    - Fallback: Guarda el delta entre la cabecera HTTP `Date` de las respuestas del servidor y `SystemClock.elapsedRealtime()`.
    - Proporciona tiempo UTC exacto y resistente a manipulaciones manuales del reloj del usuario.
- `ServerTimeInterceptor`:
  - Interceptor OkHttp que lee la cabecera HTTP `Date`, parsea su instante UTC y alimenta `TrustedTimeProvider.syncWithServerDate()`.

#### Modelos y Servicios Remotos (`data/remote/` & `data/model/`)
- `UndoQuotaDto`, `UndoSwipeResponseDto`.
- `SwipeApiService`:
  - `@GET("swipe/undo-quota") suspend fun getUndoQuota(): UndoQuotaDto`
  - `@POST("swipe/undo") suspend fun undoSwipe(@Body request: UndoSwipeRequest?): UndoSwipeResponseDto`

#### Persistencia en Modo Demo (`data/local/demo/`)
- Nueva entidad `DemoSwipeHistoryEntity` en `MiraiLinkDemoDatabase`:
  ```kotlin
  @Entity(tableName = "demo_swipe_history")
  data class DemoSwipeHistoryEntity(
      @PrimaryKey(autoGenerate = true) val id: Long = 0,
      val userId: String,
      val action: String, // "like" o "dislike"
      val timestamp: Long,
  )
  ```
- Modificaciones en `DemoUserDao` y `DemoMatchDao`:
  - `unmarkLiked(userId: String)`
  - `unmarkDisliked(userId: String)`
  - `deleteMatch(userId: String)`
  - `insertSwipeHistory(entry: DemoSwipeHistoryEntity)`
  - `getLatestSwipeHistory(): DemoSwipeHistoryEntity?`
  - `deleteSwipeHistory(id: Long)`
- Actualizacion de `MiraiLinkDemoDatabase` a version 4 con migracion destructiva de fallback para demo.

#### Repositorio (`data/repository/`)
- Actualizacion de `SwipeRepositoryImpl`, `DelegatingSwipeRepository` y `DemoSwipeRepositoryImpl` implementando:
  - `suspend fun undoSwipe(targetUserId: String? = null): MiraiLinkResult<UndoSwipeResult>`
  - `suspend fun getUndoQuota(): MiraiLinkResult<UndoQuota>`

### 2.3. Capa de Dominio (Android `:app`)

- `domain/model/swipe/UndoQuota.kt`:
  ```kotlin
  data class UndoQuota(
      val tier: SubscriptionPlanType,
      val maxUndos: Int,
      val usedUndos: Int,
      val remainingUndos: Int,
      val resetsAt: String?,
      val hasUndoableSwipe: Boolean,
      val canUndo: Boolean,
  )
  ```
- `domain/model/swipe/UndoSwipeResult.kt`:
  ```kotlin
  data class UndoSwipeResult(
      val user: User,
      val actionUndone: String,
      val quota: UndoQuota,
  )
  ```
- `domain/usecase/swipe/UndoSwipeUseCase.kt`
- `domain/usecase/swipe/GetUndoQuotaUseCase.kt`

### 2.4. Capa de Presentacion (Android `:app`)

- `HomeViewModel.kt`:
  - Expone `val undoQuota: StateFlow<UndoQuota?>`.
  - Carga la cuota de deshacer al iniciar (`reload()`) y tras cada swipe/undo.
  - Al pulsar `undoSwipe()`:
    - Si `undoQuota.value?.remainingUndos == 0`: emite `HomeEvent.NavigateToPaywall`.
    - Si tiene cuota disponible: invoca `undoSwipeUseCase()`.
    - Al recibir exito: restaura el usuario al inicio de `_userQueue`, refresca las tarjetas visibles y actualiza `undoQuota`.
    - Soporta deshaceres consecutivos mientras `remainingUndos > 0`.
- `HomeScreen.kt`:
  - Conecta el estado de `canUndo` y la navegacion al Paywall si se pulsa sin cuota.
- `UserSwipeCardStack.kt`:
  - Mantiene `returnSwipeBtn` visible si hay historial local en la sesion o si el servidor/Room indica que existe un swipe previo que se puede revertir (`hasUndoableSwipe == true`).

### 2.5. Inyeccion de Dependencias (Koin)

- Registro de `TrustedTimeProvider`, `ServerTimeInterceptor`, `UndoSwipeUseCase` y `GetUndoQuotaUseCase` en `dataModule` y `domainModule` (o `appModule`).

- - -

## 3. Estrategia de Testing

- **Backend (`MiraiLink-Backend`)**:
  - `tests/integration/swipe-undo.test.js`:
    - Verifica cuota de 1 para Free, 3 para Plus, 6 para Premium.
    - Verifica que tras 1 undo en Free, el segundo intento da 403 `DAILY_UNDO_LIMIT_REACHED`.
    - Verifica que deshacer un like elimina el like y el match asociado.
    - Verifica que deshacer un dislike elimina el dislike.
    - Verifica recuperacion de swipes de sesiones anteriores.
- **Android (`MiraiLink`)**:
  - `TrustedTimeProviderTest`: Comprueba el fallback al reloj monotonico del servidor y la deteccion del tiempo.
  - `UndoSwipeUseCaseTest`: Comprueba la gestion de cuotas y resultados.
  - `HomeViewModelTest`: Comprueba la emision de eventos al Paywall ante cuota agotada y la restauracion del usuario en la cola.
  - Tests instrumentados de Room para `demo_swipe_history`.

- - -

## 4. Riesgos Tecnicos y Mitigaciones

- **Riesgo 1**: Inconsistencia temporal si el dispositivo no tiene Google Play Services (dispositivos sin GMS o emuladores minimales).
  - *Mitigacion*: `TrustedTimeProvider` cuenta con un fallback primario de reloj monotonico sincronizado con la cabecera HTTP `Date` de cada respuesta del backend.
- **Riesgo 2**: Carrera de concurrencia al pulsar repetidamente el boton de rebobinado.
  - *Mitigacion*: Control de estado en ViewModel (`isUndoing` o mutex) y transaccion SQL aislada en PostgreSQL para decrementar cuotas y revertir votos de forma atomica.
- **Riesgo 3**: Deshacer un match cuando ya se enviaron mensajes.
  - *Mitigacion*: Si ya existen mensajes de chat entre ambos usuarios, la transaccion de deshacer preserva la integridad o notifica que el chat ya se inicio.
