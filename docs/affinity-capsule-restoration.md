# Afinidades y Capsule: reparacion local

Rama: `codex/restore-affinity-capsule-fixes`, coordinada con MiraiLink-Backend.

- Afinidades usa el logo local como respaldo de fotos nulas, vacias o fallidas.
- Restaurar Room reinicia tambien el estado persistido de Afinidades. Se conserva el reparto demo original, sin reservas nuevas por nombres o IDs.
- El modal conserva el formulario ante errores y vuelve al inicio cuando llega una revision confirmada. Las etiquetas de respuestas siguen dependiendo de authorId.
- ViewModels separados por cuenta y modo; las respuestas de red antiguas no deben modificar ni cerrar otra sesion.
- Backend: `db:test-affinities` genera interacciones classic entre usuarios existentes; `db:test-likes` genera likes normales y `db:test-capsules` prepara sesiones con match Capsule sobre cuentas existentes, usando el mismo reparto de personas que Afinidades, sin crear chats ni notificaciones. Ver ../MiraiLink-Backend/docs/affinity-capsule-restoration.md.

Evidencia permanente: revision estatica solamente. No se ejecutaron tests, compilacion, seeds ni flujos en dispositivo. Afinidades conserva sus requisitos reales de perfil completo y filtros. Los comandos de seed no se ejecutan automaticamente ni en produccion.

Backend: reset-interactions incluye todas las interacciones; test-all reconstruye Afinidades, likes normales y sesiones Capsule. reinit-capsules reinicia solo Capsule y vuelve a preparar su escenario. Ninguno crea usuarios auxiliares. Las recomendaciones test se muestran desde el backend de desarrollo sin depender de filtros de ranking de produccion.
