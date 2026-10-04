# [APROBADO] Especificacion Funcional: Sistema de Deshacer en Discovery e Integridad Temporal (TrustedTime & Timezones)

- **Fecha**: 2026-10-04
- **Estado**: [APROBADO]
- **Autor / Responsable**: Antigravity & ArisGuimera SDMD Protocol
- **Modulo Afectado**: `:app` (`com.feryaeljustice.mirailink`) y `MiraiLink-Backend`

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**:
  1. En la pantalla de descubrimiento (Discovery / Home), el boton de rebobinado / deshacer (`returnSwipeBtn`) solo operaba de forma parcial y volatil en memoria local (`HomeViewModel.kt`), sin conexion real con el backend. Cuando el usuario hace swipe (like o dislike), el backend registra el voto en la base de datos (e incluso crea un match si es reciproco), pero al pulsar deshacer no se revertia la operacion en el servidor ni se restauraba el estado en base de datos.
  2. No existia un control de cuota por nivel de suscripcion conectado al backend: se debe garantizar 1 deshacer por dia para usuarios Free, 3 deshaceres por dia para MiraiLink Plus y 6 deshaceres por dia para MiraiLink Premium.
  3. Vulnerabilidad ante manipulacion de reloj y zona horaria: si un usuario cambia manualmente la hora, fecha, zona horaria o idioma de su dispositivo en los Ajustes de Android, podria burlar los limites diarios si el calculo dependiera de `System.currentTimeMillis()` o del reloj local.
  4. En la base de datos PostgreSQL, multiples columnas historicas usan `TIMESTAMP` sin zona horaria en lugar de `TIMESTAMPTZ`, lo que introduce inconsistencias potenciales al comparar fechas entre servidores, usuarios en distintas regiones y conversiones horarias.
  5. Si el usuario reinicia la aplicacion o sufre cierre de sesion, la cola en memoria se pierde y antes no era posible rebobinar swipes de sesiones previas.

- **Objetivo**:
  1. Implementar el flujo completo de deshacer swipe (Rewind) en Discovery con sincronizacion cliente-servidor y soporte completo en Modo Demo (Room offline), incluyendo la capacidad de deshacer swipes realizados en sesiones anteriores persistidos en base de datos.
  2. Aplicar y verificar de forma estricta las cuotas por nivel de suscripcion mediante ventana deslizante de 24 horas:
     - Free: 1 deshacer cada 24 horas.
     - Plus: 3 deshaceres cada 24 horas.
     - Premium: 6 deshaceres cada 24 horas.
  3. Blindar la integridad temporal en Android mediante Google Play Services `TrustedTime API` (`com.google.android.gms:play-services-time:16.0.1`), complementada con fallbacks robustos (delta con tiempo del servidor via cabecera HTTP `Date` + reloj monotonico de hardware `SystemClock.elapsedRealtime()`).
  4. Homogeneizar la base de datos PostgreSQL a `TIMESTAMPTZ` con migracion limpia para entornos existentes y actualizacion de `db.sql` para nuevas instalaciones, asegurando que el servidor sea la unica fuente de verdad temporal.

- - -

## 2. Situacion Actual

- En Android (`HomeViewModel.kt`):
  - Existia un metodo preliminar `canUndo()` que solo comprobaba si la lista en memoria `swipeHistory` no estaba vacia y si `now - lastUndoTime >= TIME_24_HOURS` usando `System.currentTimeMillis()`.
  - Al pulsar el boton de rebobinar, solo se reinsertaba el usuario en `_userQueue` local. El backend nunca se enteraba, dejando el registro previo en las tablas `likes` o `dislikes`.
  - La muerte del proceso por el sistema operativo o cerrar la app reiniciaba `swipeHistory` y `lastUndoTime`.
- En el Backend (`MiraiLink-Backend`):
  - No existia endpoint `POST /api/swipe/undo` ni `GET /api/swipe/undo-quota`.
  - No existia tabla para auditar ni limitar los deshaceres consumidos por usuario (`user_swipe_undos`).
  - Las tablas de `db.sql` definian columnas de tiempo como `TIMESTAMP` sin zona horaria, en vez de `TIMESTAMPTZ` (excepto `app_versions` y `user_subscriptions`).
- En Modo Demo (`DemoSwipeRepositoryImpl.kt`):
  - No existia soporte de deshacer implementado en el repositorio ni en Room DAO para revertir likes/matches locales guardados en `MiraiLinkDemoDatabase`.

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)

- **Backend (`MiraiLink-Backend`)**:
  - Migracion SQL `010_timezone_and_swipe_undo_quota.sql` para migrar columnas existentes a `TIMESTAMPTZ` y crear la tabla `user_swipe_undos`.
  - Actualizacion de `db.sql` base para nuevas inicializaciones.
  - Endpoint `POST /api/swipe/undo`: revierte el swipe mas reciente del usuario (sea de la sesion actual o de sesiones previas en la base de datos). Si fue like y creo un match, elimina el match asociado. Valida cuota segun el plan activo (Free: 1, Plus: 3, Premium: 6 en ventana deslizante de 24 horas), registra la transaccion en `user_swipe_undos` y devuelve el perfil completo del usuario restaurado junto con la cuota actualizada.
  - Endpoint `GET /api/swipe/undo-quota`: consulta cuota actual (maximo, usados, restantes, fecha/hora de reseteo y si hay swipes disponibles para deshacer en base de datos).
  - Middleware / cabeceras HTTP: garantizar que todas las respuestas del servidor incluyan la cabecera estandar `Date` en UTC.
- **Android (`:app`)**:
  - Incorporacion de `com.google.android.gms:play-services-time:16.0.1` en `libs.versions.toml` y `build.gradle.kts`.
  - Creacion de `TrustedTimeProvider` en la capa de datos/infraestructura con estrategia jerarquica:
    1. Google Play Services `TrustedTimeClient`.
    2. Fallback por sincronizacion con cabecera HTTP `Date` del servidor + monotonic hardware clock `SystemClock.elapsedRealtime()`.
    3. Fallback de monotonic clock local si no hay conectividad ni Play Services.
  - Modificacion de `SwipeApiService` y `SwipeRepository` / `SwipeRepositoryImpl` / `DelegatingSwipeRepository` / `DemoSwipeRepositoryImpl`.
  - Actualizacion de `MiraiLinkDemoDatabase` con tabla `demo_swipe_history` para persistir el historial de swipes en Room entre reinicios de la aplicacion en Modo Demo.
  - Actualizacion de `HomeViewModel` y `HomeScreen`:
    - Estado reactivo de cuota de deshacer (`UndoQuota`).
    - Consulta de cuota y disponibilidad de rebobinado al iniciar la pantalla (permitiendo rebobinar swipes de sesiones previas).
    - Si la cuota restante es 0 y el usuario intenta deshacer, navegacion automatica al Paywall con mensaje explicativo sobre los planes Plus y Premium.
    - Soporte visual en `returnSwipeBtn`: visible y operativo siempre que existan swipes deshacibles en cola local o en base de datos.
    - Permitir deshacer de forma consecutiva tantas tarjetas como cuota restante tenga el usuario.

### 3.2. Fuera del Alcance (Out of Scope)

- Deshacer mensajes de chat enviados tras producirse un match y comenzada la conversacion.
- Planes con deshaceres ilimitados (el limite maximo por diseno de negocio es 6 al dia en el plan Premium).

- - -

## 4. Casuisticas y Comportamiento Mobile

- **Comportamiento en Modo Online vs Modo Offline Demo**:
  - **Modo Online**: Al pulsar deshacer, se invoca `SwipeRepository.undoSwipe()`. El backend valida en PostgreSQL con `NOW() - INTERVAL '24 hours'` y revierte el like/dislike mas reciente. Devuelve el perfil completo del usuario deshecho, que se posiciona al frente del feed. Si la cuota esta agotada (HTTP 403 `DAILY_UNDO_LIMIT_REACHED` o cuota restante 0), se emite evento de navegacion a suscripciones.
  - **Modo Demo**: Se utiliza `TrustedTimeProvider` local y la base de datos Room (`demo_swipe_history`). Se descuenta la cuota del usuario simulado y se remueven los votos y matches de prueba incluso tras reiniciar la aplicacion.
- **Ciclo de Vida y Recuperacion de Estado**:
  - Si la app sufre muerte de proceso (*Low Memory Killer*) o se cierra: al reabrir, el ViewModel consulta `getUndoQuota()` al backend. Si hay votos previos en la base de datos y cuota disponible, el boton de rebobinado aparece activo y al pulsarlo se recupera el usuario descartado previamente.
- **Resistencia a Trampas de Hora y Zona Horaria**:
  - Si el usuario cambia la fecha del telefono en Ajustes de Android adelantando 1 dia:
    - El backend sigue denegando la accion porque el servidor utiliza su propio reloj UTC (`NOW()`).
    - En el cliente Android, el `TrustedTimeProvider` ignora el cambio manual porque `TrustedTimeClient` y `SystemClock.elapsedRealtime()` son inmunes a manipulaciones del reloj del sistema.
  - Si el usuario cambia la region o idioma en el dispositivo, solo cambia la localizacion de textos (`es`, `en`, etc.); la evaluacion de cuotas y marcas de tiempo no se ve afectada en ningun aspecto.
- **Ergonomia y Accesibilidad**:
  - El boton de rebobinado `returnSwipeBtn` respeta el area tactil minima de $48 \times 48\text{ dp}$ (con boton de $72\text{ dp}$ centrado).
  - Iconografia clara Material 3 y retroalimentacion accesible mediante `contentDescription`.

- - -

## 5. Criterios de Aceptacion (Formato Given - When - Then)

### Criterio 1: Deshacer con exito en plan Free dentro del limite diario
- **Dado que**: Un usuario en plan Free acaba de realizar un swipe (like o dislike) y tiene su cuota diaria de 1 deshacer intacta (0 usados en las ultimas 24h).
- **Cuando**: Pulsa el boton de rebobinado en Discovery.
- **Entonces**: La peticion a `/api/swipe/undo` responde 200 OK, el usuario descartado vuelve a aparecer al frente de la pila de tarjetas, se revierte su like/dislike en la base de datos, y la cuota restante pasa a 0.

### Criterio 2: Rebobinado de swipes de sesiones anteriores
- **Dado que**: Un usuario dio swipe a un perfil, cerro la aplicacion completamente y la vuelve a abrir horas despues.
- **Cuando**: Abre la pantalla de Discovery y pulsa el boton de rebobinar teniendo cuota disponible.
- **Entonces**: El backend localiza su ultimo swipe registrado en base de datos, revierte el voto, devuelve el perfil completo del usuario y la UI de Android lo inserta al frente de la pila de tarjetas.

### Criterio 3: Bloqueo de deshacer al agotar la cuota y direccionamiento a Paywall
- **Dado que**: Un usuario en plan Free ya consumio su 1 deshacer en las ultimas 24 horas (cuota restante = 0).
- **Cuando**: Intenta pulsar el boton de deshacer tras un nuevo swipe.
- **Entonces**: El sistema deniega la operacion, no se revierte el swipe y se dispara el evento hacia el Paywall indicando las ventajas de los planes Plus (3/dia) y Premium (6/dia).

### Criterio 4: Cuotas ampliadas y rebobinado consecutivo para Plus (3) y Premium (6)
- **Dado que**: Un usuario cuenta con suscripcion activa MiraiLink Plus o MiraiLink Premium y ha realizado varios swipes.
- **Cuando**: Pulsa repetidamente el boton de deshacer.
- **Entonces**: El plan Plus permite hasta 3 operaciones de deshacer consecutivas exitosas, y el plan Premium permite hasta 6 operaciones de deshacer consecutivas exitosas en un periodo de 24 horas antes de bloquearse.

### Criterio 5: Integridad temporal frente a manipulacion del reloj del dispositivo
- **Dado que**: Un usuario ha consumido su limite diario de deshaceres y cambia manualmente la fecha y hora de su sistema Android en Ajustes para adelantar 24 horas.
- **Cuando**: Abre la aplicacion e intenta ejecutar un nuevo deshacer.
- **Entonces**: El `TrustedTimeProvider` en Android detecta la discordancia o mantiene el tiempo real mediante `TrustedTimeClient` / `elapsedRealtime`, y el backend en PostgreSQL valida con su reloj UTC autentico, rechazando la solicitud con `DAILY_UNDO_LIMIT_REACHED` hasta que transcurran realmente las 24 horas reales.

### Criterio 6: Reversion limpia de matches reciprocos
- **Dado que**: El usuario dio like a un perfil con el que genero un match instantaneo.
- **Cuando**: Pulsa el boton de deshacer antes de interactuar en el chat.
- **Entonces**: Se elimina tanto el registro en `likes` como el registro correspondiente en la tabla `matches`, impidiendo inconsistencias o chats huerfanos.

- - -

## 6. Decisiones Acordadas (Resueltas)

- [x] **Decision 1 (UX del boton de deshacer cuando la cuota es 0)**:
  El boton se mantiene visible y habilitado siempre que haya al menos un swipe registrable (en cola local o en base de datos). Si la cuota restante es 0, al pulsarlo se muestra el Paywall invitando a mejorar a Plus o Premium.
- [x] **Decision 2 (Ventana de reseteo de cuota diaria)**:
  Ventana deslizante de 24 horas en PostgreSQL (`created_at >= NOW() - INTERVAL '24 hours'`), exactamente igual que el limite de likes, siendo inmune a saltos de zona horaria y adelantos del reloj.
- [x] **Decision 3 (Profundidad del historial de deshacer)**:
  Se permite deshacer de forma consecutiva tantas tarjetas como cuota disponible tenga el usuario (hasta 1 en Free, hasta 3 en Plus, hasta 6 en Premium).
- [x] **Decision 4 (Soporte de sesiones previas)**:
  El backend busca el ultimo swipe en base de datos si la cola local no lo tiene o si la app se cerro, devolviendo el perfil del usuario para restaurarlo al inicio de la pila de tarjetas.
- [x] **Decision 5 (Migracion de columnas TIMESTAMP a TIMESTAMPTZ)**:
  Se ejecuta la migracion transaccional `010_timezone_and_swipe_undo_quota.sql` en PostgreSQL convirtiendo todas las columnas naive a `TIMESTAMPTZ USING <columna> AT TIME ZONE 'UTC'`, junto con la creacion de `user_swipe_undos`.
