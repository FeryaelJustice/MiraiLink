# [BORRADOR] Especificación Funcional: Sistema Completo de Ajustes de Notificaciones (Push, Correo, SMS)

- **Fecha**: 2026-09-25
- **Estado**: [BORRADOR]
- **Autor / Responsable**: Antigravity & FeryaelJustice
- **Módulo Afectado**: `:app` (`com.feryaeljustice.mirailink`), Firebase Messaging (`FcmService`)
- **Rama Git Planificada**: `feature/notification-preferences-system`

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**:
  - Actualmente MiraiLink cuenta con recepción básica de notificaciones push de chat mediante Firebase Cloud Messaging (`FcmService`), pero el usuario no tiene ningún control granular sobre qué notificaciones desea recibir.
  - No existe interfaz para activar/desactivar notificaciones de nuevos matches, mensajes, super likes, me gusta de mensajes o promociones.
  - No existen canales ni preferencias preparadas para notificaciones por correo electrónico ni SMS.
  - La experiencia visual mostrada en aplicaciones de referencia (Tinder y Bumble) ofrece una navegación clara por tipos de canales y selectores específicos (por ejemplo, frecuencia de notificaciones de nuevos "Me gusta").
- **Objetivo**:
  - Implementar la arquitectura y las pantallas completas para la selección y gestión de preferencias de notificaciones:
    1. **Hub / Sección de Notificaciones en Ajustes**:
       - "Notificaciones push" (acceso a pantalla detallada con switches).
       - "Correo electrónico" (acceso a pantalla de preferencias de email).
       - "SMS" (acceso a pantalla de alertas por SMS).
    2. **Pantalla de Notificaciones Push (Detallada)**:
       - Nuevos matches (Switch).
       - Mensajes (Switch).
       - "Me gusta" de mensajes (Switch).
       - Super Likes (Switch).
       - Ofertas y Promociones (Switch).
       - Nuevos "Me gusta" (Switch + selector de frecuencia: Cada nuevo Like, Cada 10 nuevos Likes, Cada 100 nuevos Likes).
    3. **Pantalla de Correo Electrónico**:
       - Resumen de nuevos matches y mensajes.
       - Seguridad de la cuenta y verificaciones.
       - Novedades y promociones.
    4. **Pantalla de SMS**:
       - Códigos de seguridad y alertas de acceso crítico.
       - Notificaciones de match urgente.
    5. **Filtrado activo en cliente (`FcmService`)**:
       - Antes de lanzar una notificación del sistema ante un mensaje push de FCM, validar si la categoría correspondiente está habilitada por el usuario. Si está apagada, ignorar la notificación en silencio.
    6. **Persistencia e Integración Hibrida**:
       - Almacenamiento local reactivo (DataStore / Room) para que funcione de forma inmediata y persistente en modo Offline Demo y Online.
       - Preparación de contratos de API para sincronización con el backend cuando los endpoints estén disponibles.

- - -

## 2. Situación Actual

- `FcmService` gestiona el canal `NOTIFICATION_CHANNEL_ID` y muestra mensajes cuando `data["type"] == "new_message"`.
- No hay almacenamiento ni modelo de dominio para preferencias de notificaciones.
- En `SettingsScreen` no existe ningún enlace ni bloque de notificaciones.

- - -

## 3. Alcance de la Funcionalidad

### 3.1. Dentro del Alcance (In Scope)
- **Modelos de Dominio**:
  - `PushNotificationPreferences` (newMatches, messages, messageLikes, superLikes, promotions, newLikesEnabled, likesFrequency).
  - `EmailNotificationPreferences` (matchesAndMessagesDigest, securityAlerts, promotions).
  - `SmsNotificationPreferences` (securityAlerts, urgentMatches).
  - `NotificationPreferences` (engloba push, email, sms).
- **Capa de Persistencia y Repositorio**:
  - `NotificationPreferencesRepository` con soporte para `Flow<NotificationPreferences>` y métodos de actualización granular o por lote.
  - Persistencia local en DataStore/Room que funciona sin depender del backend.
  - Soporte completo en modo Offline Demo.
- **Servicio FCM**:
  - Inyección del caso de uso de preferencias en `FcmService`.
  - Comprobación de bandera antes de emitir notificaciones locales en pantalla.
- **Capa de Presentación (Compose & Navigation 3)**:
  - Enlaces en `SettingsScreen`:
    - "Notificaciones push" -> `AppScreen.PushNotificationSettingsScreen`
    - "Correo electrónico" -> `AppScreen.EmailNotificationSettingsScreen`
    - "SMS" -> `AppScreen.SmsNotificationSettingsScreen`
  - Pantallas de configuración con Material 3, switches accesibles y selectores de frecuencia.
  - Textos descriptivos de ayuda debajo de cada interruptor, acordes a los referentes analizados.

### 3.2. Fuera del Alcance (Out of Scope)
- Envío real de SMS desde un gateway de telecomunicaciones (solo se prepara la configuración y almacenamiento del lado del cliente y contratos).
- Envío de emails directos desde el cliente Android (gestionado por backend).

- - -

## 4. Casuísticas y Comportamiento Mobile

- **Modo Online vs Modo Offline Demo**:
  - Las preferencias se guardan localmente de forma inmediata (cache-first / local-first).
  - En modo Online se dispara la sincronización idempotente con el servidor en segundo plano; si falla por timeout o falta de red, la elección del usuario persiste localmente y se programa reintento.
  - En modo Offline Demo, los switches se pueden activar y desactivar y su estado queda guardado en la sesión demo.
- **Ciclo de Vida y Recuperación de Estado**:
  - Los interruptores reflejan el estado de `StateFlow`. No hay inconsistencias visuales ante rotación de pantalla.
- **Ergonomía y Accesibilidad**:
  - Todos los interruptores (`Switch`) y elementos de selección tienen un área táctil mínima de 48 x 48 dp.
  - Soporte completo para tema claro y tema oscuro.

- - -

## 5. Criterios de Aceptación (Given - When - Then)

### Criterio 1: Desactivar notificaciones push de mensajes
- **Dado que**: El usuario desactiva el switch "Mensajes" en la pantalla de notificaciones push.
- **Cuando**: Llega un push remoto de FCM con `type == "new_message"`.
- **Entonces**: `FcmService` detecta que `messages == false` y no muestra la notificación en la barra de estado de Android.

### Criterio 2: Modificar frecuencia de notificaciones de nuevos Likes
- **Dado que**: El usuario tiene activada la opción de nuevos Likes.
- **Cuando**: Selecciona la opción "Cada 10 nuevos Likes".
- **Entonces**: La preferencia se actualiza en el repositorio y se persiste en local.

### Criterio 3: Persistencia tras reinicio de la app
- **Dado que**: El usuario cambia varias preferencias de notificaciones push, email y SMS.
- **Cuando**: Cierra y vuelve a abrir la aplicación o sufre destrucción de proceso por memoria.
- **Entonces**: Al ingresar a la pantalla de Ajustes de Notificaciones, sus opciones continuan exactamente como las configuro.

- - -

## 6. Decisiones Pendientes [PENDIENTE]

- [ ] [PENDIENTE] Confirmar la navegación del Hub de Notificaciones: ¿Deseas que desde `SettingsScreen` haya botones directos a "Notificaciones push", "Correo electrónico" y "SMS" (como en Tinder), o prefieres un elemento previo "Notificaciones" que abra un menu intermedio? (Recomendado: directo en Ajustes como en la captura 2).
- [ ] [PENDIENTE] Para los nuevos Likes y su frecuencia ("Cada nuevo Like", "Cada 10", "Cada 100"): ¿debemos mostrar las opciones desplegadas debajo del switch o en un diálogo selector modal?
- [ ] [PENDIENTE] Valores por defecto al iniciar la app: ¿deben estar todas las notificaciones push activadas por defecto excepto ofertas y promociones?
