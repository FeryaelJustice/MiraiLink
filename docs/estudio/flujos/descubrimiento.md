# Feed, búsquedas, likes y matches

[Guía maestra](../../guia-maestra.md) | [Mapa de funcionalidades](../funcionalidades.md)

## Responsabilidad y recorrido

Home y preferencias de búsqueda separan borrador de filtro y feed. SwipeApiService usa limit/offset; like retorna información de match y dislike un resultado básico. MatchApiService diferencia matches y unseen, y mark-seen envía un ID. ReceivedLikes y detalle por username permiten otro recorrido hacia perfil.

## Alternativas y efectos

Feed vacío puede ser válido por radio, exclusiones o falta de geografía; no eliminar filtros para ocultarlo. Permiso de ubicación rechazado afecta radius_active, no valida coordenadas ficticias. Preferencia specific_country conserva ID de país; cambiar scope limpia ese destino. El repositorio guarda servidor antes de DataStore y retorna error remoto.

Demo usa sus repositorios locales; un match demo no existe en backend. Ranking, límites por suscripción, freshness y persistencia del match pertenecen al [servidor](https://github.com/FeryaelJustice/MiraiLink-Backend/blob/codex/documentacion-integral/docs/estudio/flujos/descubrimiento.md).

## Fuentes para estudiar

- [SwipeApiService.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt)
- [MatchApiService.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/MatchApiService.kt)
- [SearchPreferencesRepositoryImpl.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt)

Revisión: 2026-10-01, por inspección del código. Consultar [verificación](../desarrollo-y-verificacion.md) para resultados ejecutados.
