# [BORRADOR] Especificacion Funcional: Sistema Completo de Ajustes de Notificaciones (Push, Correo, SMS)

- **Fecha**: 2026-09-25
- **Estado**: [BORRADOR]
- **Autor / Responsable**: Antigravity & FeryaelJustice
- **Modulo Afectado**: `:app` (`com.feryaeljustice.mirailink`), Firebase Messaging (`FcmService`)
- **Rama Git Planificada**: `feature/notification-preferences-system`

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**:
  - Actualmente MiraiLink cuenta con recepcion basica de notificaciones push de chat mediante Firebase Cloud Messaging (`FcmService`), pero el usuario no tiene ningun control granular sobre qué notificaciones desea recibir.
  - No existe interfaz para activar/desactivar notificaciones de nuevos matches, mensajes, super likes, me gusta de mensajes o promociones.
  - No existen canales ni preferencias preparadas para notificaciones por correo electronico ni SMS.
  - La experiencia visual mostrada en aplicaciones de referencia (Tinder y Bumble) ofrece una navegacion clara por tipos de canales y selectores especificos (por ejemplo, frecuencia de notificaciones de nuevos "Me gusta").
- **Objetivo**:
  - Implementar la arquitectura y las pantallas completas para la seleccion y gestion de preferencias de notificaciones:
    1. **Hub / Seccion de Notificaciones en Ajustes**:
       - "Notificaciones push" (acceso a pantalla detallada con switches).
       - "Correo electronico" (acceso a pantalla de preferencias de email).
       - "SMS" (acceso a pantalla de alertas por SMS).
    2. **Pantalla de Notificaciones Push (Detallada)**:
       - Nuevos matches (Switch).
       - Mensajes (Switch).
       - "Me gusta" de mensajes (Switch).
       - Super Likes (Switch).
       - Ofertas y Promociones (Switch).
       - Nuevos "Me gusta" (Switch + selector de frecuencia: Cada nuevo Like, Cada 10 nuevos Likes, Cada 100 nuevos Likes).
    3. **Pantalla de Correo Electronico**:
       - Resumen de nuevos matches y mensajes.
       - Seguridad de la cuenta y verificaciones.
       - Novedades y promociones.
    4. **Pantalla de SMS**:
       - Codigos de seguridad y alertas de acceso critico.
       - Notificaciones de match urgente.
    5. **Filtrado activo en cliente (`FcmService`)**:
       - Antes de lanzar una notificacion del sistema ante un mensaje push de FCM, validar si la categoria correspondiente esta habilitada por el usuario. Si esta apagada, ignorar la notificacion en silencio.
    6. **Persistencia e Integracion Hibrida**:
       - Almacenamiento local reactivo (DataStore / Room) para que funcione de forma inmediata y persistente en modo Offline Demo y Online.
       - Preparacion de contratos de API para sincronizacion con el backend cuando los endpoints esten disponibles.

- - -

## 2. Situacion Actual

- `FcmService` gestiona el canal `NOTIFICATION_CHANNEL_ID` y muestra mensajes cuando `data["type"] == "new_message"`.
- No hay almacenamiento ni modelo de dominio para preferencias de notificaciones.
- En `SettingsScreen` no existe ningun enlace ni bloque de notificaciones.

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)
- **Modelos de Dominio**:
  - `PushNotificationPreferences` (newMatches, messages, messageLikes, superLikes, promotions, newLikesEnabled, likesFrequency).
  - `EmailNotificationPreferences` (matchesAndMessagesDigest, securityAlerts, promotions).
  - `SmsNotificationPreferences` (securityAlerts, urgentMatches).
  - `NotificationPreferences` (engloba push, email, sms).
- **Capa de Persistencia y Repositorio**:
  - `NotificationPreferencesRepository` con soporte para `Flow<NotificationPreferences>` y metodos de actualizacion granular o por lote.
  - Persistencia local en DataStore/Room que funciona sin depender del backend.
  - Soporte completo en modo Offline Demo.
- **Servicio FCM**:
  - Inyeccion del caso de uso de preferencias en `FcmService`.
  - Comprobacion de bandera antes de emitir notificaciones locales en pantalla.
- **Capa de Presentacion (Compose & Navigation 3)**:
  - Enlaces en `SettingsScreen`:
    - "Notificaciones push" -> `AppScreen.PushNotificationSettingsScreen`
    - "Correo electronico" -> `AppScreen.EmailNotificationSettingsScreen`
    - "SMS" -> `AppScreen.SmsNotificationSettingsScreen`
  - Pantallas de configuracion con Material 3, switches accesibles y selectores de frecuencia.
  - Textos descriptivos de ayuda debajo de cada interruptor, acordes a los referentes analizados.

### 3.2. Fuera del Alcance (Out of Scope)
- Envio real de SMS desde un gateway de telecomunicaciones (solo se prepara la configuracion y almacenamiento del lado del cliente y contratos).
- Envio de emails directos desde el cliente Android (gestionado por backend).

- - -

## 4. Casuisticas y Comportamiento Mobile

- **Modo Online vs Modo Offline Demo**:
  - Las preferencias se guardan localmente de forma inmediata (cache-first / local-first).
  - En modo Online se dispara la sincronizacion idempotente con el servidor en segundo plano; si falla por timeout o falta de red, la eleccion del usuario persiste localmente y se programa reintento.
  - En modo Offline Demo, los switches se pueden activar y desactivar y su estado queda guardado en la sesion demo.
- **Ciclo de Vida y Recuperacion de Estado**:
  - Los interruptores reflejan el estado de `StateFlow`. No hay inconsistencias visuales ante rotacion de pantalla.
- **Ergonomia y Accesibilidad**:
  - Todos los interruptores (`Switch`) y elementos de seleccion tienen un area tactil minima de 48 x 48 dp.
  - Soporte completo para tema claro y tema oscuro.

- - -

## 5. Criterios de Aceptacion (Given - When - Then)

### Criterio 1: Desactivar notificaciones push de mensajes
- **Dado que**: El usuario desactiva el switch "Mensajes" en la pantalla de notificaciones push.
- **Cuando**: Llega un push remoto de FCM con `type == "new_message"`.
- **Entonces**: `FcmService` detecta que `messages == false` y no muestra la notificacion en la barra de estado de Android.

### Criterio 2: Modificar frecuencia de notificaciones de nuevos Likes
- **Dado que**: El usuario tiene activada la opcion de nuevos Likes.
- **Cuando**: Selecciona la opcion "Cada 10 nuevos Likes".
- **Entonces**: La preferencia se actualiza en el repositorio y se persiste en local.

### Criterio 3: Persistencia tras reinicio de la app
- **Dado que**: El usuario cambia varias preferencias de notificaciones push, email y SMS.
- **Cuando**: Cierra y vuelve a abrir la aplicacion o sufre destruccion de proceso por memoria.
- **Entonces**: Al ingresar a la pantalla de Ajustes de Notificaciones, sus opciones continuan exactamente como las configuro.

- - -

## 6. Decisiones Pendientes [PENDIENTE]

- [ ] [PENDIENTE] Confirmar la navegacion del Hub de Notificaciones: ¿Deseas que desde `SettingsScreen` haya botones directos a "Notificaciones push", "Correo electronico" y "SMS" (como en Tinder), o prefieres un elemento previo "Notificaciones" que abra un menu intermedio? (Recomendado: directo en Ajustes como en la captura 2).
- [ ] [PENDIENTE] Para los nuevos Likes y su frecuencia ("Cada nuevo Like", "Cada 10", "Cada 100"): ¿debemos mostrar las opciones desplegadas debajo del switch o en un dialogo selector modal?
- [ ] [PENDIENTE] Valores por defecto al iniciar la app: ¿deben estar todas las notificaciones push activadas por defecto excepto ofertas y promociones?
