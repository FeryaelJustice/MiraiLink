# Hallazgos del cliente y límites de evidencia

[Guía maestra](../guia-maestra.md) | [Cobertura](cobertura.md)

Revisión de fuentes: 2026-10-01. Las observaciones estáticas no certifican el servidor desplegado ni el comportamiento en dispositivo.


| Hallazgo | Evidencia | Implicación |
| --- | --- | --- |
| Versiones/documentos antiguos | Build min SDK 30, versión 3.0.0, catálogo actual | README/contextos antiguos no deben usarse como ficha vigente |
| Hilt descrito, Koin real | RepositoryModule y Application | La guía vigente se corrige; no migrar DI |
| Chat REST con polling | ChatViewModel, getMessages lanza job | No asegurar WebSocket activo, exclusión mutua ni entrega en tiempo real |
| Grupos incompletos en UI | startGroupMessagesPolling TODO | Endpoints no prueban feature completa |
| Logging BODY debug | NetworkModule | Posible dato sensible en trazas; no cambiar runtime aquí |
| Demo con migración destructiva | DemoModule | No afirmar migración conservadora ni sincronización de cuenta real |
| Búsqueda guarda servidor antes que local | SearchPreferencesRepositoryImpl | Fallo local posterior puede dejar divergencia |
| DataStore corruption/legacy cleanup | Serializer y cleanUp vacíos | No afirmar reparación automática o borrado legacy |
| Claims anteriores sobre links/App Check | MainActivity actual | Ahora hay handleDeepLink y selección por BuildConfig.DEBUG; notas viejas eran históricas |
| Binding AI duplicado | AiModule y RepositoryModule | Revisar orden/binding antes de refactorizar |
| Billing no certificado por servidor | Handler backend verifySubscription | UI premium no acredita validación oficial de compra |

Son observaciones del código local. No se han reproducido todas las incidencias ni auditado producción. Las correcciones funcionales están fuera del alcance aprobado. Revisar también [hallazgos backend](https://github.com/FeryaelJustice/MiraiLink-Backend/blob/codex/documentacion-integral/docs/estudio/hallazgos.md).

## Hallazgos adicionales de arranque e integraciones

- FCM: notification_fcm no coincide con fcm_default_channel creado por el receptor; falta prueba visual de entrega. El PendingIntent abre la Activity sin destino de conversación.
- FCM: rotación sin sesión no guarda un token pendiente en el else; el receptor registra token y mensaje en logs.
- IA: cada prompt se envía aislado; reintentar un error vuelve a añadir el mensaje visible. No hay historial remoto observado.
- Billing: fallback puede escoger otra oferta/producto y la restauración confunde error/desconexión con lista vacía.
- Lint: 78 errores y 167 warnings en la ejecución del 2026-10-01, incluido CredManMissingDal en el manifest. Ver [evidencia detallada](desarrollo-y-verificacion.md).
