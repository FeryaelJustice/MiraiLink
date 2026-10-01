# Persistencia, estado y demo

[Guía maestra](../guia-maestra.md) | [Cobertura](cobertura.md)

Revisión de fuentes: 2026-10-01. Las observaciones estáticas no certifican el servidor desplegado ni el comportamiento en dispositivo.


[MiraiLinkDemoDatabase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/MiraiLinkDemoDatabase.kt) declara seis entidades y versión 3: perfil propio, perfiles del feed, matches, chats, mensajes y preferencias de categoría. [DemoModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt) usa `fallbackToDestructiveMigration(true)`: una evolución incompatible puede recrear la base demo. No implica una migración conservadora ni una pérdida de datos remotos.

[DemoModeManager.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/demo/DemoModeManager.kt) activa el modo y ejecuta `seedInitialDataIfEmpty`; restaurar demo ejecuta otro método que borra/recrea sus datos. La presencia de `DEMO_TOKEN` distingue la sesión demo. [RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt) conecta los delegados de usuario, swipe, match, chat y exploración a implementaciones remotas o locales.

## Qué persiste y qué no

| Estado | Almacenamiento | Límite |
| --- | --- | --- |
| Sesión y verificación | DataStore cifrado | No sustituye validación JWT del servidor |
| Onboarding y preferencias | AppPrefs cifrado | Revisar campos individuales y migraciones |
| Tema/flags | Preferences DataStore | No es el mismo fichero que sesión |
| Perfiles/chats demo | Room | No se sincronizan con PostgreSQL de usuarios reales |
| Borradores de pantalla | Estado Compose/ViewModel según pantalla | No garantizar conservación tras muerte de proceso |
| Preferencias de búsqueda | DataStore y actualización remota | En modo remoto se intenta guardar servidor antes que local |

[SearchPreferencesRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt) normaliza el país objetivo solo para `specific_country`. Si el servidor devuelve error, retorna sin persistir el cambio local; si se guarda servidor y falla DataStore puede quedar divergencia. Su lectura vuelve a AppPrefs predeterminado ante error del Flow: eso no repara el archivo corrupto.

Las migraciones DataStore desde SharedPreferences y preferencias antiguas están en [DataStoreModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataStoreModule.kt). Los métodos `cleanUp` vacíos no acreditan borrado del origen legacy. Revisar [hallazgos](hallazgos.md) y backup XML antes de describir eliminación segura.

Diagrama de modelos: [clases y persistencia demo](diagramas/modelos.md). El esquema SQL remoto pertenece al [backend](https://github.com/FeryaelJustice/MiraiLink-Backend/blob/codex/documentacion-integral/docs/estudio/base-de-datos.md).
