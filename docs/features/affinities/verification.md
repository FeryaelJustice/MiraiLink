# Estado de Afinidades al cerrar la implementacion

Fecha: 2026-10-06. Cambios locales en Android master y backend main. Sin commit, push ni despliegue.

## Evidencia obtenida

- 12 pruebas de verificacion de compras y controlador de suscripciones pasaron con proveedor Google Play simulado. No son compras reales.
- Cuatro pruebas del motor de elegibilidad y ranking pasaron: observacion, resultados recientes, mayoria de edad, privacidad Capsule, gustos, objetivos y filtros mutuos.
- Seis pruebas con PostgreSQL 17 real, local y desechable pasaron. Se aplicaron baseline y todas las migraciones 002 a 018 dentro de un esquema temporal. Cada prueba elimina su esquema al finalizar. Comprueban anonimato Free, reintentos concurrentes, aceptacion gratuita sin match, match posterior sin duplicar chat, traslado del primer mensaje al producirse un match, cuota, caducidad, bloqueo bidireccional y tokens de compra reemplazados.
- La recompilacion final de Android, incluyendo cancelacion de jobs al cambiar de usuario, paso. assembleDebug genero app/build/outputs/apk/debug/app-debug.apk.
- Regresion completa backend: 136 pruebas pasadas y 13 omitidas (incluidas las seis de PostgreSQL que requieren entorno desechable y ya se ejecutaron por separado). npm run lint y el contrato OpenAPI de 77 operaciones pasaron.
- Suite Android: 504 pruebas, cero fallos, cero errores y cero omitidas. Los resultados estan en app/build/test-results/testDebugUnitTest.

## Validacion final pendiente

Selector de planes Demo en Ajustes: assembleDebug y siete pruebas de GlobalMiraiLinkSession pasaron, incluyendo dos nuevas que comprueban transiciones Premium/Plus/Free y aislamiento de suscripciones reales. Revisión visual del selector pendiente. En esta comprobacion adb encontro un dispositivo fisico conectado por dos conexiones Wi-Fi; no se instalo el APK en el telefono.

Actualizacion de interfaz: solicitudes trasladadas a Likes, seccion Afinidades agrupada y tarjetas compactas. Las cuatro nuevas pruebas de AffinityViewModel pasaron: filtrado de solicitudes, retirada tras aceptar aunque falle la recarga, conservacion al fallar el rechazo y ocultacion tras denunciar. assembleDebug paso con estos cambios. Backend: lint y 23 pruebas enfocadas de swipe y motor de afinidades pasaron. Estas comprobaciones no sustituyen la revision visual en dispositivo.

La prueba de integracion PostgreSQL se omite sin AFFINITY_TEST_DB_URL; para repetirla, usar un PostgreSQL localhost desechable y npx vitest run tests/database/affinity-transactions.test.js. No apuntar estas pruebas a produccion.

Revisar UI y lifecycle en dispositivo, incluyendo cambio de usuario, restauracion de borrador, Demo sin compras reales, receptor Free y origen del chat. adb devices -l no encontro dispositivos conectados.

El ultimo lint Android confirma un error (CredManMissingDal), 156 warnings y un hint. CredManMissingDal esta en el manifiesto y tambien estaba presente en el informe anterior a Afinidades. Falta la asociacion de dominio de Credential Manager; no se cambio su configuracion de autenticacion en este trabajo. El locale japones solo traduce Afinidades y conserva explicitamente el fallback espanol del resto de la app, que debe mantenerse sincronizado con values/strings.xml y values/capsule_strings.xml hasta traducir la aplicacion completa. Se corrigieron asi las 660 ausencias de traduccion introducidas al añadir el locale parcial. El APK se regenero con los recursos finales.

El fallo temporal del servicio de revision de permisos esta resuelto: las ejecuciones autorizadas volvieron a funcionar al reintentarlas. No se cambiaron los permisos ni se consumio un reset de uso.

El PostgreSQL desechable esta detenido: pg_ctl status confirma no server running y no hay listener en el puerto 55439. Los esquemas de prueba fueron eliminados durante las pruebas anteriores. No se modifico la base de datos existente del proyecto.

## Produccion

AFFINITIES_ENABLED permanece desactivado por defecto. Configurar y verificar Google Play Developer API, compras de prueba, RTDN, Firebase y los timers del VPS antes de habilitar. Instrucciones y unidades en el checkout backend: docs/features/affinities/deployment.md y deploy/systemd.

El ranking es determinista con intereses reales. No crea likes, matches ni supuestas muestras de interes. Las cuentas en modo Crystal Capsule se excluyen para conservar su privacidad. Las tandas necesitan siete dias observados y los candidatos se revalidan al actuar.

La compilacion y las pruebas automatizadas no acreditan funcionamiento visual, entrega FCM, compras reales ni despliegue VPS. Esas comprobaciones siguen pendientes.
