# Perfil, residencia, fotos y atributos

[Guía maestra](../../guia-maestra.md) | [Mapa de funcionalidades](../funcionalidades.md)

## Responsabilidad y recorrido

ProfileViewModel gestiona borrador, selecciones, fotos y guardado; updateProfile envía multipart con IDs canónicos, intereses y atributos. El contrato incluye campos legacy por compatibilidad, cuya aceptación depende del backend. Una selección de residencia se confirma al tocar una sugerencia; texto libre no equivale a una ciudad canónica seleccionada.

## Alternativas y efectos

Campo vacío, país cambiado y región/city inválida deben seguir sus eventos de limpieza/selección. Los IDs y las etiquetas son distintos: el backend determina relaciones. PhotoCarouselController distingue toque derecha/izquierda y pulsación larga; no cambiar esa semántica por documentar. Media ausente puede ser un archivo servidor faltante o un host bloqueado, no necesariamente un error de guardado del perfil.

Guardar usa resultado remoto antes de cerrar/avisar éxito. La foto individual y el perfil multipart son caminos distintos; posiciones de UI y backend requieren revisar conversión.

## Fuentes para estudiar

- [ProfileViewModel.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt)
- [UserApiService.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt)
- [UserRemoteDataSource.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt)

Revisión: 2026-10-01, por inspección del código. Consultar [verificación](../desarrollo-y-verificacion.md) para resultados ejecutados.
