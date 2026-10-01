# Exploración por categorías

[Guía maestra](../../guia-maestra.md) | [Mapa de funcionalidades](../funcionalidades.md)

## Responsabilidad y recorrido

ExploreApiService consulta hub, feed y settings por categoryId. Categories llevan ID, texto localizado e información del backend; el cliente no debe convertir label traducido en clave persistida. DelegatingExploreRepository permite demo y remoto.

## Alternativas y efectos

Categoría inexistente, feed vacío y radio no permitido son casos distintos. El feed paginado transmite limit/offset. El cliente debe presentar el resultado de guardar preferencias de categoría; el recuento del hub puede estar cacheado en servidor y no promete coincidencia instantánea con el feed.

## Fuentes para estudiar

- [ExploreApiService.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/ExploreApiService.kt)
- [DelegatingExploreRepository.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingExploreRepository.kt)

Revisión: 2026-10-01, por inspección del código. Consultar [verificación](../desarrollo-y-verificacion.md) para resultados ejecutados.
