# Afinidades y Capsule: reparacion local

Rama: `codex/restore-affinity-capsule-fixes`, coordinada con MiraiLink-Backend.

- Afinidades usa el logo local como respaldo de fotos nulas, vacias o fallidas.
- Restaurar Room reinicia tambien el estado persistido de Afinidades. Se conserva el reparto demo original, sin reservas nuevas por nombres o IDs.
- El modal conserva el formulario ante errores y vuelve al inicio solo cuando se confirma el actionId de ese envio. Las etiquetas de respuestas siguen dependiendo de authorId.
- ViewModels separados por cuenta y modo; las respuestas de red antiguas no deben modificar ni cerrar otra sesion.
- Backend: `db:test-affinities` genera interacciones classic entre usuarios existentes; `db:test-likes` genera likes normales y `db:test-capsules` prepara sesiones con match Capsule sobre cuentas existentes, usando el mismo reparto de personas que Afinidades, sin crear chats ni notificaciones. Ver ../MiraiLink-Backend/docs/affinity-capsule-restoration.md.

Evidencia permanente: revision estatica solamente. No se ejecutaron tests, compilacion, seeds ni flujos en dispositivo. Afinidades conserva sus requisitos reales de perfil completo y filtros. Los comandos de seed no se ejecutan automaticamente ni en produccion.

Backend: reset-interactions incluye todas las interacciones; test-all reconstruye Afinidades, likes normales y sesiones Capsule. reinit-capsules reinicia solo Capsule y vuelve a preparar su escenario. Ninguno crea usuarios auxiliares. Las recomendaciones test se muestran desde el backend de desarrollo sin depender de filtros de ranking de produccion.

Aceptar una invitacion de conversacion en Demo crea solo el chat de Afinidades. El like de Afinidades no marca el voto normal de Room. Discovery mantiene esos perfiles visibles salvo match, bloqueo o voto/like de Discovery. Likes recibidos muestra classic y capsule y devuelve el like usando su identificador y modo original; la demo usa las mismas cuentas existentes.

Backend requiere la migracion 020 para guardar intereses de Afinidades y likes de Discovery independientes de la misma pareja. No se ha aplicado la migracion ni probado el flujo en dispositivo.

Invitaciones de conversacion entrantes: sin Plus se muestra solo el aviso de invitacion y el acceso a Plus, sin mensaje ni acciones aceptar/rechazar. Con Plus se puede consultar y responder. Guards en ViewModel, repositorio Demo y rutas backend; los textos/notificaciones ya no prometen aceptacion gratis.
