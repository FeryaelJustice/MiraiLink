# Política de Privacidad / Privacy Policy - MiraiLink

**Última actualización / Last updated:** 10 de octubre de 2026 / October 10, 2026  
**Aplicación / Application:** MiraiLink (`com.feryaeljustice.mirailink`)  
**Desarrollador / Developer:** FeryaelJustice  
**Contacto / Contact:** [nano9gs@hotmail.es](mailto:nano9gs@hotmail.es)  
**Repositorio Oficial / Official Repository:** [https://github.com/FeryaelJustice/MiraiLink](https://github.com/FeryaelJustice/MiraiLink)

---

## Índice / Table of Contents

1. [Español - Política de Privacidad](#español---política-de-privacidad)
   - [1. Información General y Responsable del Tratamiento](#1-información-general-y-responsable-del-tratamiento)
   - [2. Restricción de Edad (Exclusivo para Mayores de 18 Años)](#2-restricción-de-edad-exclusivo-para-mayores-de-18-años)
   - [3. Información que Recopilamos y Tratamos](#3-información-que-recopilamos-y-tratamos)
   - [4. Finalidades del Tratamiento de los Datos](#4-finalidades-del-tratamiento-de-los-datos)
   - [5. Permisos Solicitados en el Dispositivo Android](#5-permisos-solicitados-en-el-dispositivo-android)
   - [6. Procesamiento Local en el Dispositivo (Google ML Kit)](#6-procesamiento-local-en-el-dispositivo-google-ml-kit)
   - [7. Proveedores de Servicios y Terceros](#7-proveedores-de-servicios-y-terceros)
   - [8. Seguridad, Criptografía y Almacenamiento](#8-seguridad-criptografía-y-almacenamiento)
   - [9. Política de Eliminación de Cuenta y Supresión de Datos](#9-política-de-eliminación-de-cuenta-y-supresión-de-datos)
   - [10. Derechos del Usuario (RGPD / CCPA / ARCO)](#10-derechos-del-usuario-rgpd--ccpa--arco)
   - [11. Conservación de Datos](#11-conservación-de-datos)
   - [12. Modificaciones a esta Política](#12-modificaciones-a-esta-política)
   - [13. Contacto y Reclamaciones](#13-contacto-y-reclamaciones)
2. [English - Privacy Policy](#english---privacy-policy)
   - [1. General Information & Data Controller](#1-general-information--data-controller)
   - [2. Age Restriction (Strictly 18+)](#2-age-restriction-strictly-18)
   - [3. Information We Collect and Process](#3-information-we-collect-and-process)
   - [4. Purposes of Data Processing](#4-purposes-of-data-processing)
   - [5. Device Permissions Requested](#5-device-permissions-requested)
   - [6. On-Device Processing (Google ML Kit)](#6-on-device-processing-google-ml-kit)
   - [7. Third-Party Services and SDKs](#7-third-party-services-and-sdks)
   - [8. Security, Cryptography, and Storage](#8-security-cryptography-and-storage)
   - [9. Account and Data Deletion Policy](#9-account-and-data-deletion-policy)
   - [10. User Rights (GDPR / CCPA / International)](#10-user-rights-gdpr--ccpa--international)
   - [11. Data Retention](#11-data-retention)
   - [12. Changes to This Privacy Policy](#12-changes-to-this-privacy-policy)
   - [13. Contact Information](#13-contact-information)

---

# Español - Política de Privacidad

## 1. Información General y Responsable del Tratamiento

La presente Política de Privacidad describe de manera transparente cómo se recopilan, utilizan, almacenan y protegen los datos personales de los usuarios al utilizar la aplicación móvil **MiraiLink** (paquete Android: `com.feryaeljustice.mirailink`) y sus servicios de backend y APIs asociados.

MiraiLink es una plataforma social y de citas diseñada para conectar a personas afines a través de intereses culturales compartidos en anime, manga y videojuegos.

El responsable del tratamiento de los datos personales es:
- **Titular / Desarrollador:** FeryaelJustice
- **Correo Electrónico de Contacto:** [nano9gs@hotmail.es](mailto:nano9gs@hotmail.es)
- **Repositorio Público del Proyecto:** [https://github.com/FeryaelJustice/MiraiLink](https://github.com/FeryaelJustice/MiraiLink)

---

## 2. Restricción de Edad (Exclusivo para Mayores de 18 Años)

MiraiLink está dirigida estricta y exclusivamente a personas **mayores de 18 años** (o la mayoría de edad legal en su jurisdicción).

- Se requiere que el usuario declare su fecha de nacimiento durante el registro para validar que cuenta con al menos 18 años de edad cumplidos.
- No recopilamos conscientemente datos personales de menores de 18 años.
- Si detectamos o se nos informa que una cuenta pertenece a un menor de 18 años, dicha cuenta y sus datos asociados serán eliminados de manera inmediata y definitiva de nuestros servidores.

---

## 3. Información que Recopilamos y Tratamos

En función del uso que hagas de la aplicación y de los servicios backend, recopilamos las siguientes categorías de información:

### 3.1. Datos de Identidad y Autenticación
- **Nombre de usuario (`username`) y apodo (`nickname`):** Identificadores públicos en la plataforma.
- **Correo electrónico (`email`):** Utilizado para el registro, verificación de cuenta mediante enlace o código, recuperación de contraseña y comunicaciones esenciales de seguridad.
- **Número de teléfono (`phone_number`):** Opcional, cuando se utiliza como método alternativo de autenticación o verificación.
- **Credenciales y seguridad:** Contraseñas protegidas mediante hash unidireccional con sal (bcrypt). Claves secretas de doble factor (2FA TOTP) y códigos de recuperación de emergencia almacenados mediante hash seguro.
- **Proveedor de autenticación (`auth_provider`):** Indicador de autenticación por email, teléfono o Google Sign-In.

### 3.2. Datos de Perfil e Intereses
- **Biografía (`bio`):** Descripción textual voluntaria redactada por el usuario.
- **Fecha de nacimiento y edad:** Para verificación de mayoría de edad y visualización de la edad en el perfil.
- **Género y orientación de búsqueda:** Género declarado (ej. masculino, femenino) y preferencias para el motor de descubrimiento.
- **Intereses culturales (Anime y Videojuegos):** Selección de títulos y franquicias del catálogo oficial vinculadas a tu perfil para calcular afinidades y coincidencias.

### 3.3. Fotografías y Medios
- **Fotografías de perfil:** Hasta 4 imágenes subidas voluntariamente por el usuario.
- **Validación de seguridad:** Las imágenes subidas pasan por una verificación binaria en el servidor (validación de magic bytes) para garantizar que son archivos de imagen legítimos y libres de código malicioso.

### 3.4. Datos de Ubicación Geográfica
- **Ubicación de residencia declarada:** Ciudad, región, código de país y coordenadas aproximadas de residencia (`residence_latitude`, `residence_longitude`).
- **Ubicación en tiempo real / actual:** Coordenadas GPS (`current_latitude`, `current_longitude`) capturadas con el consentimiento del usuario para la búsqueda de personas cercanas y filtros de distancia (`search_radius_km`).
- **Historial de ubicaciones:** Registro acotado de ubicaciones recientes (máximo 50 registros) para optimizar la precisión de exploración geográfica.
- **Privacidad estricta de la ubicación:** **Tus coordenadas GPS exactas nunca se muestran a otros usuarios**. Los demás usuarios solo ven la distancia calculada aproximada en kilómetros o la ciudad de residencia que hayas configurado.

### 3.5. Interacciones Sociales y Emparejamiento
- **Acciones de descubrimiento:** Votos de agrado ("Like"), descarte ("Dislike") e historial de reversión de votos ("Swipe Undo" sujeto a cuotas diarias).
- **Coincidencias ("Matches"):** Vínculos mutuos entre perfiles.
- **Dinámicas de la Cápsula de Cristal ("Affinity Capsule"):** Respuestas a preguntas y misiones interactivas compartidas voluntariamente entre usuarios emparejados.

### 3.6. Mensajería y Comunicaciones Privadas
- **Mensajes de chat:** Contenido de los mensajes de texto intercambiados en salas privadas, marcas de tiempo de envío y confirmaciones de lectura (`last_read_at`, `is_read`).
- **Sincronización en tiempo real:** Comunicación instantánea gestionada a través del protocolo WebSocket (Socket.IO).

### 3.7. Datos Técnicos, de Dispositivo y Diagnóstico
- **Tokens de notificación push:** Identificadores FCM (`push_tokens`) para entrega de alertas sobre nuevos mensajes y matches.
- **Identificador de publicidad (`AD_ID`):** Identificador publicitario de Google utilizado para la muestra de anuncios y cumplimiento de consentimiento.
- **Información de red y logs:** Dirección IP, versión del sistema operativo Android, versión de la app MiraiLink y registros de fallos (Crashlytics).

### 3.8. Compras y Suscripciones
- Si adquieres suscripciones premium o compras dentro de la aplicación, las transacciones se gestionan enteramente a través de **Google Play Billing**. MiraiLink no almacena ni tiene acceso a tus números de tarjeta de crédito o datos bancarios.

### 3.9. Moderación y Soporte
- Reportes entre usuarios (usuario reportado, motivo) y mensajes enviados al buzón de sugerencias o soporte (`feedback`).

---

## 4. Finalidades del Tratamiento de los Datos

Utilizamos la información recopilada para las siguientes finalidades legítimas:

1. **Gestión de la cuenta:** Crear, autenticar y mantener activa tu cuenta de usuario, incluyendo protección mediante 2FA.
2. **Descubrimiento y afinidad:** Facilitar el emparejamiento entre personas según proximidad geográfica, preferencias de género e intereses compartidos en anime y videojuegos.
3. **Comunicación en tiempo real:** Gestionar el envío de mensajes privados, indicadores de lectura y notificaciones push.
4. **Seguridad y prevención de fraudes:** Detectar accesos no autorizados, abuso de la API, suplantación de identidad o comportamientos dañinos mediante rate limiting, lista negra de tokens y moderación de reportes.
5. **Publicidad y monetización:** Mostrar anuncios no intrusivos mediante Google AdMob, respetando las preferencias de consentimiento configuradas.
6. **Mantenimiento y optimización técnica:** Identificar bloqueos y fallos en la aplicación mediante Firebase Crashlytics y Kotzilla SDK.

---

## 5. Permisos Solicitados en el Dispositivo Android

La aplicación solicita los siguientes permisos en Android, cada uno con una justificación clara:

| Permiso | Tipo | Justificación |
| :--- | :--- | :--- |
| `android.permission.INTERNET` | Normal | Necesario para comunicar la aplicación con los servidores de la API, Socket.IO y servicios en la nube. |
| `android.permission.CAMERA` | Peligroso (Runtime) | Permite tomar fotografías directamente con la cámara para utilizarlas en tu perfil de usuario. |
| `android.permission.ACCESS_FINE_LOCATION` | Peligroso (Runtime) | Obtención de la posición GPS precisa para calcular distancias exactas con otros perfiles cercanos. |
| `android.permission.ACCESS_COARSE_LOCATION` | Peligroso (Runtime) | Obtención de la ubicación estimada basada en red celular/Wi-Fi como alternativa o complemento a la ubicación GPS. |
| `android.permission.POST_NOTIFICATIONS` | Peligroso (Android 13+) | Envío de notificaciones push de mensajes recibidos, nuevos matches e información del sistema. |
| `com.google.android.gms.permission.AD_ID` | Normal | Identificador para mediación y entrega de anuncios a través de Google AdMob. |
| `android.permission.VIBRATE` | Normal | Retroalimentación háptica en interacciones de la interfaz y recepción de mensajes. |
| `android.permission.SYSTEM_ALERT_WINDOW` | Especial | Utilizado únicamente en entornos específicos de depuración y soporte de superposiciones del sistema. |

Puedes revocar los permisos concedidos en cualquier momento desde los **Ajustes del Sistema Android > Aplicaciones > MiraiLink > Permisos**.

---

## 6. Procesamiento Local en el Dispositivo (Google ML Kit)

MiraiLink incorpora funciones interactivas avanzadas impulsadas por **Google ML Kit**:
- **Holo Mirai (Selfie Segmentation):** Segmenta la silueta del usuario para efectos visuales de paralaje y previsualización de perfil holográfico.
- **Ruleta de Gestos (Face Detection & Gesture Analysis):** Detecta expresiones y gestos del usuario para mecánicas interactivas y dinámicas lúdicas.

**Garantía de Privacidad en Dispositivo:**
- Todo el procesamiento de imágenes, rostros y siluetas se realiza **100% de manera local en tu propio dispositivo** mediante los modelos integrados de ML Kit.
- **Ninguna transmisión de vídeo en directo, fotografía procesada ni dato biométrico o vectorial se envía, graba ni almacena en ningún servidor externo ni se comparte con otros usuarios.**
- Las funciones pueden operar sin conexión para fotografías ya existentes en el dispositivo.

---

## 7. Proveedores de Servicios y Terceros

Para el correcto funcionamiento de MiraiLink, integramos bibliotecas y servicios de terceros que pueden procesar ciertos datos bajo sus respectivas políticas de privacidad:

- **Google Play Services y Google Sign-In (Google LLC):** Servicios base de la plataforma Android y autenticación federada.  
  [Política de Privacidad de Google](https://policies.google.com/privacy)
- **Google AdMob y Google UMP (Google LLC):** Publicidad móvil y gestión de consentimiento según la normativa europea (RGPD / Directiva ePrivacy).  
  [Privacidad y Términos de Google Ads](https://policies.google.com/technologies/ads)
- **Firebase (Google LLC):**
  - *Firebase Cloud Messaging (FCM):* Envío de notificaciones push.
  - *Firebase Crashlytics:* Registro anónimo de excepciones y bloqueos técnicos.
  - *Firebase Analytics:* Métricas agregadas de uso para análisis de rendimiento.
  - *Firebase App Check y Play Integrity:* Verificación de seguridad y legitimidad de la app.  
  [Privacidad en Firebase](https://firebase.google.com/support/privacy)
- **Google Play Billing (Google LLC):** Procesamiento seguro de suscripciones y transacciones financieras.
- **Kotzilla SDK:** Monitorización de salud de la inyección de dependencias y rendimiento de pantallas.  
  [Política de Privacidad de Kotzilla](https://kotzilla.io/privacy-policy)
- **OpenStreetMap / Carto:** Proveedores de mapas y nombres de localidades utilizados para visualización geográfica.  
  [Política de OpenStreetMap](https://wiki.osmfoundation.org/wiki/Privacy_Policy)

---

## 8. Seguridad, Criptografía y Almacenamiento

Implementamos rigurosas medidas técnicas y organizativas para proteger tus datos:

1. **Cifrado en tránsito:** Toda la comunicación entre la aplicación Android y el backend se realiza exclusivamente a través de protocolos seguros con cifrado TLS/HTTPS y WSS (WebSockets seguros).
2. **Cifrado local en el dispositivo:** La aplicación utiliza **Jetpack Security (EncryptedSharedPreferences)** con claves maestras gestionadas por el **Android Keystore** de hardware para almacenar tokens de autenticación y claves sensibles de sesión.
3. **Seguridad en el Servidor y Base de Datos:**
   - Base de datos PostgreSQL con consultas SQL parametrizadas, protegiendo contra inyecciones SQL.
   - Contraseñas almacenadas como hashes irreversibles con bcrypt.
   - Lista negra de tokens (`token_blacklist`) que invalida inmediatamente los tokens JWT al cerrar sesión o eliminar la cuenta.
   - Códigos de recuperación 2FA protegidos mediante hash bcrypt.
   - Secretos TOTP de doble factor cifrados en reposo.
   - Protección contra abusos mediante limitación de tasa de solicitudes (Rate Limiting) y cabeceras HTTP de seguridad reforzadas con Helmet.

---

## 9. Política de Eliminación de Cuenta y Supresión de Datos

Cumpliendo plenamente con la normativa de Google Play Store sobre eliminación de cuentas, MiraiLink ofrece métodos sencillos, transparentes y accesibles para eliminar tu cuenta y tus datos personales:

### 9.1. Eliminación Directa desde la Aplicación (Autoservicio)
Puedes eliminar tu cuenta en cualquier momento dentro de la aplicación móvil siguiendo estos pasos:
1. Abre **MiraiLink** e inicia sesión en tu cuenta.
2. Dirígete a la pestaña de **Ajustes** (icono de engranaje).
3. Desplázate hacia la sección de cuenta y pulsa en **Eliminar cuenta**.
4. Confirma la acción en el diálogo de confirmación.

**Consecuencias de la eliminación:**
- Tu cuenta se marca de inmediato como eliminada (`is_deleted = TRUE`).
- El token de sesión actual se revoca y se añade a la lista negra criptográfica.
- Todas las sesiones activas de la Cápsula de Cristal vinculadas a tu cuenta se destruyen.
- Tu perfil desaparece de manera inmediata del carrusel de descubrimiento, de los listados de búsqueda, de los matches y de los chats de otros usuarios.
- Tus fotos de perfil y tokens de notificación asociados se desvinculan.

### 9.2. Solicitud de Eliminación por Correo Electrónico (Vía Web / Externa)
Si has desinstalado la aplicación, no puedes acceder a tu dispositivo o deseas solicitar el borrado manual y completo de todos tus datos personales de nuestros registros:
- Envía un correo electrónico a **[nano9gs@hotmail.es](mailto:nano9gs@hotmail.es)** con el asunto **"Solicitud de Eliminación de Cuenta - MiraiLink"**.
- Indica tu nombre de usuario (`username`) o el correo electrónico registrado con el que creaste tu cuenta.
- Tu solicitud será procesada en un plazo máximo de 30 días hábiles, eliminando o anonimizando tus datos de nuestras bases de datos principales.

---

## 10. Derechos del Usuario (RGPD / CCPA / ARCO)

De conformidad con las leyes internacionales de protección de datos (como el Reglamento General de Protección de Datos de la UE y leyes equivalentes), tienes los siguientes derechos:
- **Acceso:** Solicitar una copia de los datos personales que conservamos sobre ti.
- **Rectificación:** Modificar tus datos incorrectos o incompletos directamente desde la pantalla de edición de perfil o mediante solicitud.
- **Supresión ("Derecho al olvido"):** Solicitar el borrado íntegro de tu cuenta e información personal.
- **Oposición y Limitación:** Oponerte a determinados tratamientos o solicitar la limitación temporal del uso de tus datos.
- **Portabilidad:** Solicitar la entrega de tus datos personales en un formato estructurado y de uso común.
- **Retirada del Consentimiento:** Puedes revocar en cualquier instante el consentimiento prestado para ubicación, cámara o publicidad personalizada.

Para ejercer cualquiera de estos derechos, contáctanos en [nano9gs@hotmail.es](mailto:nano9gs@hotmail.es).

---

## 11. Conservación de Datos

- Los datos personales se conservan únicamente mientras tu cuenta se encuentre activa en MiraiLink.
- Al solicitar la eliminación de tu cuenta, los datos de perfil son deshabilitados inmediatamente y suprimidos según nuestras políticas de depuración.
- Determinados registros técnicos de auditoría o seguridad (como tokens revocados o IDs anonimizados de reportes por infracciones de términos) podrán conservarse durante el tiempo mínimo indispensable establecido por las obligaciones legales vigentes.

---

## 12. Modificaciones a esta Política

Podemos actualizar esta Política de Privacidad de forma periódica para reflejar mejoras en la aplicación, nuevas funciones o exigencias regulatorias.
Cualquier cambio relevante se notificará a través de la aplicación o actualizando la fecha de "Última actualización" en la cabecera de este documento en el repositorio público oficial.

---

## 13. Contacto y Reclamaciones

Si tienes dudas, consultas o sugerencias respecto a esta Política de Privacidad o al tratamiento de tus datos personales, puedes contactar al desarrollador en:
- **Responsable:** FeryaelJustice
- **Correo Electrónico:** [nano9gs@hotmail.es](mailto:nano9gs@hotmail.es)
- **Repositorio GitHub:** [https://github.com/FeryaelJustice/MiraiLink](https://github.com/FeryaelJustice/MiraiLink)

---
---

# English - Privacy Policy

## 1. General Information & Data Controller

This Privacy Policy describes in a clear and transparent manner how personal data is collected, used, stored, and safeguarded when using the mobile application **MiraiLink** (Android package: `com.feryaeljustice.mirailink`) and its associated backend APIs and services.

MiraiLink is a social and dating platform tailored to connect people who share cultural affinities in anime, manga, and gaming.

The data controller responsible for the processing of your personal data is:
- **Developer / Controller:** FeryaelJustice
- **Contact Email:** [nano9gs@hotmail.es](mailto:nano9gs@hotmail.es)
- **Official Public Repository:** [https://github.com/FeryaelJustice/MiraiLink](https://github.com/FeryaelJustice/MiraiLink)

---

## 2. Age Restriction (Strictly 18+)

MiraiLink is strictly intended for individuals **aged 18 and older** (or the age of legal majority in your jurisdiction).

- Users are required to input their birthdate upon registration to ensure they meet the minimum age of 18.
- We do not knowingly collect personal data from minors under 18 years old.
- If we become aware that a registered account belongs to a person under 18, the account and all associated personal data will be immediately and permanently deleted from our servers.

---

## 3. Information We Collect and Process

Depending on your use of the application and its backend services, we collect and process the following categories of information:

### 3.1. Identity and Authentication Data
- **Username (`username`) and nickname (`nickname`):** Your visible public handles across the service.
- **Email address (`email`):** Used for account registration, email verification, password reset, and essential security notices.
- **Phone number (`phone_number`):** Optional, where supported as an alternate authentication method.
- **Credentials and Security:** Passwords hashed with salted bcrypt. Two-Factor Authentication secrets (2FA TOTP) and emergency recovery codes hashed using strong cryptographic standards.
- **Authentication Provider (`auth_provider`):** Designates whether the account was registered via email, phone, or Google Sign-In.

### 3.2. Profile and Cultural Affinity Data
- **Biography (`bio`):** Free-form text written voluntarily by the user.
- **Date of birth and age:** Used for mandatory age verification and showing age on your public card.
- **Gender and discovery preferences:** User-declared gender and match discovery criteria.
- **Anime and Gaming Interests:** Catalog titles and video game franchises selected to calculate thematic affinity scores and mutual interests.

### 3.3. Photos and Media
- **Profile Photos:** Up to 4 photos voluntarily uploaded to showcase your profile.
- **Binary Signature Validation:** Server-side magic-byte inspection ensures that uploaded files are authentic, non-malicious image files.

### 3.4. Location Data
- **Residence Location:** City, region, country code, and approximate residential coordinates (`residence_latitude`, `residence_longitude`).
- **Live / Current Location:** Real-time GPS coordinates (`current_latitude`, `current_longitude`) gathered with explicit user consent to calculate distances and search filters (`search_radius_km`).
- **Location History:** A capped record of recent locations (up to 50 points) to optimize distance filtering.
- **Strict Location Privacy Guarantee:** **Your exact GPS coordinates are NEVER exposed to other users**. Other users only see the estimated distance in kilometers or the specified city of residence.

### 3.5. Social Interactions and Matching
- **Swipe Decisions:** Likes, dislikes, and rewind/undo interactions (subject to daily quotas).
- **Mutual Matches:** Mutual connections established between users.
- **Affinity Capsule ("Cápsula de Cristal"):** Responses to icebreaker questions and interactive missions shared mutually within the session.

### 3.6. Private Messaging
- **Chat Messages:** Content of private messages exchanged, transmission timestamps (`sent_at`), client message IDs (`client_message_id`), and read status (`last_read_at`, `is_read`).
- **Real-Time Delivery:** Instant message relay managed over WebSocket connections (Socket.IO).

### 3.7. Technical, Device, and Diagnostic Data
- **Push Notification Tokens:** Firebase Cloud Messaging tokens (`push_tokens`) for delivering alerts for messages and matches.
- **Advertising ID (`AD_ID`):** Google Advertising identifier used for ad serving and consent compliance.
- **Network and System Telemetry:** IP address, Android OS version, MiraiLink client version, and crash logs (Firebase Crashlytics).

### 3.8. In-App Purchases and Subscriptions
- In-app purchases and subscriptions are handled entirely by **Google Play Billing**. MiraiLink does not collect or store credit card numbers or financial account details.

### 3.9. Moderation and User Feedback
- User safety reports (reported user, description) and feedback submissions.

---

## 4. Purposes of Data Processing

We process personal data for the following legitimate purposes:

1. **Account Provision and Maintenance:** To register, authenticate, and secure your account (including 2FA protection).
2. **Discovery and Matchmaking:** To connect users based on shared anime/gaming interests, gender preferences, and geographic distance.
3. **Real-Time Communication:** To deliver private instant messages, read receipts, and push notifications.
4. **App Security and Abuse Prevention:** To guard against unauthorized access, spam, brute-force attempts, and harassment via rate limiting, JWT blacklisting, and user reporting mechanisms.
5. **Monetization and Advertising:** To display ads via Google AdMob in accordance with user consent selections.
6. **Performance and Diagnostics:** To diagnose crashes, bugs, and optimize app performance using Firebase Crashlytics and Kotzilla SDK.

---

## 5. Device Permissions Requested

The application requests the following Android permissions, each justified by specific functionality:

| Permission | Category | Purpose |
| :--- | :--- | :--- |
| `android.permission.INTERNET` | Normal | Required for communication with backend APIs, Socket.IO, and cloud services. |
| `android.permission.CAMERA` | Runtime (Dangerous) | Capturing profile photos directly within the app. |
| `android.permission.ACCESS_FINE_LOCATION` | Runtime (Dangerous) | Accessing precise GPS location to calculate accurate distances to other users. |
| `android.permission.ACCESS_COARSE_LOCATION` | Runtime (Dangerous) | Accessing approximate cell/Wi-Fi location as a fallback or power-efficient location source. |
| `android.permission.POST_NOTIFICATIONS` | Runtime (Android 13+) | Delivering notifications for new messages, matches, and system updates. |
| `com.google.android.gms.permission.AD_ID` | Normal | Google Advertising ID for ad mediation via Google AdMob. |
| `android.permission.VIBRATE` | Normal | Haptic feedback on UI interactions and incoming messages. |
| `android.permission.SYSTEM_ALERT_WINDOW` | Special | Reserved for debug configurations and overlay handling where applicable. |

You can review or revoke any runtime permission at any time in **Android Settings > Apps > MiraiLink > Permissions**.

---

## 6. On-Device Processing (Google ML Kit)

MiraiLink includes interactive features powered by **Google ML Kit**:
- **Holo Mirai (Selfie Segmentation):** Segments the user profile silhouette to generate parallax visual effects and hologram previews.
- **Gesture Roulette (Face Detection & Gesture Analysis):** Detects expressions and gestures for interactive in-app mini-games.

**On-Device Privacy Guarantee:**
- All image, facial, and silhouette processing is conducted **100% locally on your device** using embedded ML Kit models.
- **No live video feeds, raw frames, biometric landmarks, or facial embeddings are recorded, saved on remote servers, or transmitted to any third party.**
- Features operate offline for images already stored on the device.

---

## 7. Third-Party Services and SDKs

To provide our features, MiraiLink integrates trusted third-party SDKs that process data subject to their respective privacy terms:

- **Google Play Services & Google Sign-In (Google LLC):** Authentication and platform services.  
  [Google Privacy Policy](https://policies.google.com/privacy)
- **Google AdMob & Google UMP (Google LLC):** Mobile advertising and user consent management (GDPR / ePrivacy).  
  [Google Ads Privacy & Terms](https://policies.google.com/technologies/ads)
- **Firebase (Google LLC):**
  - *Firebase Cloud Messaging (FCM):* Push notification routing.
  - *Firebase Crashlytics:* Anonymized crash logs and diagnostics.
  - *Firebase Analytics:* Aggregated usage metrics.
  - *Firebase App Check & Play Integrity:* Anti-tampering and app attestation.  
  [Firebase Privacy Information](https://firebase.google.com/support/privacy)
- **Google Play Billing (Google LLC):** In-app purchase and subscription processing.
- **Kotzilla SDK:** Dependency injection telemetry and screen performance analysis.  
  [Kotzilla Privacy Policy](https://kotzilla.io/privacy-policy)
- **OpenStreetMap / Carto:** Geocoding and map tile visualization.  
  [OpenStreetMap Privacy Policy](https://wiki.osmfoundation.org/wiki/Privacy_Policy)

---

## 8. Security, Cryptography, and Storage

We employ strict security measures to protect your personal data:

1. **Encryption in Transit:** All network traffic between the Android client and the backend uses TLS/HTTPS and secure WebSockets (WSS).
2. **On-Device Encrypted Storage:** The Android client uses **Jetpack Security (EncryptedSharedPreferences)** with hardware-backed keys managed by **Android Keystore** to store authentication tokens and sensitive configuration data.
3. **Server-Side Security:**
   - PostgreSQL database with parameterized queries to prevent SQL injection.
   - Passwords hashed using bcrypt.
   - Stateless JWT tokens paired with a token blacklist (`token_blacklist`) to enforce immediate revocation on logout or deletion.
   - Two-Factor recovery codes hashed with bcrypt; TOTP secrets encrypted at rest.
   - Rate limiting and security HTTP headers enforced via Helmet.

---

## 9. Account and Data Deletion Policy

In full compliance with Google Play account deletion requirements, MiraiLink provides straightforward, accessible mechanisms to delete your account and personal data:

### 9.1. In-App Account Deletion (Self-Service)
You can delete your account directly inside the app at any time:
1. Open **MiraiLink** and log into your account.
2. Go to the **Settings** screen (gear icon).
3. Scroll to the account section and tap **Delete Account**.
4. Confirm the prompt to finalize deletion.

**What happens upon deletion:**
- Your user record is marked as deleted (`is_deleted = TRUE`).
- Your session token is immediately invalidated and added to the token blacklist.
- Active Affinity Capsule sessions are permanently removed.
- Your profile is immediately hidden from discovery, matches, search results, and chats.
- Photos and push notification tokens are dissociated.

### 9.2. External / Web-Based Deletion Request
If you uninstalled the application or cannot access your account:
- Send an email to **[nano9gs@hotmail.es](mailto:nano9gs@hotmail.es)** with the subject line **"Account Deletion Request - MiraiLink"**.
- Provide your registered username (`username`) or email address.
- Your deletion request will be processed within 30 business days, ensuring your data is erased or anonymized from primary storage.

---

## 10. User Rights (GDPR / CCPA / International)

Under applicable privacy regulations, you have the right to:
- **Access:** Request a copy of the personal data we hold about you.
- **Rectification:** Update or correct inaccurate personal details.
- **Erasure ("Right to be Forgotten"):** Request the permanent deletion of your account and data.
- **Restriction / Objection:** Restrict or object to specific processing operations.
- **Data Portability:** Receive your data in a structured, commonly used electronic format.
- **Withdraw Consent:** Revoke permissions (location, camera, ad consent) at any time through device settings.

To exercise any of these rights, contact [nano9gs@hotmail.es](mailto:nano9gs@hotmail.es).

---

## 11. Data Retention

- We retain personal data for as long as your account remains active.
- Upon an account deletion request, user data is promptly deactivated and scheduled for purging according to operational policies.
- Certain technical records (such as invalidated token records or anonymized moderation logs) may be retained for the minimum statutory periods required by applicable law.

---

## 12. Changes to This Privacy Policy

We may update this Privacy Policy from time to time. Any material updates will be communicated through the app or by updating the "Last updated" date at the top of this document in the official public repository.

---

## 13. Contact Information

For any inquiries, feedback, or requests regarding this Privacy Policy or data privacy practices, please contact:
- **Data Controller:** FeryaelJustice
- **Email:** [nano9gs@hotmail.es](mailto:nano9gs@hotmail.es)
- **GitHub Repository:** [https://github.com/FeryaelJustice/MiraiLink](https://github.com/FeryaelJustice/MiraiLink)
