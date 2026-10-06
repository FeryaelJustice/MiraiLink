# ViewEntities info

In this app we use sealed classes for ui state, if we use livedata or state flows
as screen ui states we would create a view entity (not entry) for that screen, with
the loading, error and data vars there.

### Explanation by OpenAI's ChatGPT w/ GPT-5:

```
Qué es cada cosa
ViewEntity = modelo de pantalla/feature. Representa el estado completo que la UI necesita para renderizar una vista (o subárbol) entera: datos ya formateados + flags de UI (loading, error, permisos, etc.). Suele vivir en el ViewModel como StateFlow<ViewEntity>.

ViewEntry = modelo de ítem. Representa una pieza dentro de esa pantalla: una fila de lista, un chip, una tarjeta, un campo de formulario, una foto en un carrusel. Se agrupan dentro de la ViewEntity.

Piensa: Entity = todo el “escenario”, Entry = cada “actor” en el escenario.

Cuándo creo uno u otro
ViewEntity: cuando modelas una pantalla o sección que necesita estado global (loading, acciones, permisos, errores) + colecciones de entries.

ViewEntry: cuando modelas ítems repetibles o componentes reusables dentro de esa pantalla (celdas de Recycler, tarjetas, filas de ajustes, fotos, etc.).

Reglas rápidas
Dominas el formato en la UI: nada de timestamps crudos ni URLs sin procesar. Aquí ya van strings listos, Uri listos, colores/strings de recursos resueltos.

Domain ≠ UI: mapea tus User, Photo, etc. del dominio → UserViewEntity / UserListEntry en la capa UI. Así no filtras tipos de dominio (y puedes testear formateos).

Inmutables (data class). Identificadores estables para diffing en listas.

ViewEntity puede contener varios ViewEntry y subestados.

Ejemplos reales
1) Pantalla de perfil
kotlin
Copiar
Editar
// ViewEntity (estado de la pantalla)
data class UserProfileViewEntity(
    val isLoading: Boolean,
    val error: String?,
    val header: UserHeaderEntry,            // entry
    val photos: List<UserPhotoEntry>,       // entries
    val actions: List<UserActionEntry>      // entries (seguir, mensaje, bloquear)
)

// Entries
data class UserHeaderEntry(
    val displayName: String,                // “Goku (Lv. 35)”
    val subtitle: String,                   // “Madrid · 3 km”
    val avatarUri: Uri?,                    // ya resuelto (placeholder si null)
    val isVerified: Boolean
)

data class UserPhotoEntry(
    val id: String,
    val thumbnailUri: Uri,                  // miniatura para grid
    val fullUri: Uri?                       // opcional para visor
)

data class UserActionEntry(
    val id: String,
    val label: String,
    val enabled: Boolean
)
UserProfileViewEntity = ViewEntity (toda la pantalla).

UserHeaderEntry, UserPhotoEntry, UserActionEntry = ViewEntry (piezas).

2) Lista de usuarios (explorar / matches)
kotlin
Copiar
Editar
data class UserListViewEntity(
    val isLoading: Boolean,
    val filtersSummary: String,
    val items: List<UserListEntry>
)

data class UserListEntry(
    val id: String,
    val name: String,
    val age: String,                        // “28”
    val distance: String,                   // “3 km”
    val avatarUri: Uri?,                    // con fallback
    val isOnline: Boolean
)
La pantalla (entidad) tiene filtros, estado, etc.

Cada fila de la lista es un Entry.

3) Editar perfil (form)
kotlin
Copiar
Editar
data class EditProfileViewEntity(
    val isSaving: Boolean,
    val fields: List<FormFieldEntry>,       // entradas de formulario
    val canSave: Boolean
)

sealed class FormFieldEntry {
    data class TextFieldEntry(val key: String, val label: String, val value: String, val error: String?) : FormFieldEntry()
    data class PhotoFieldEntry(val key: String, val label: String, val currentPhotoUri: Uri?): FormFieldEntry()
    data class SwitchFieldEntry(val key: String, val label: String, val checked: Boolean): FormFieldEntry()
}
El formulario completo = ViewEntity.

Cada campo = ViewEntry.

4) Galería/visor de fotos
GalleryViewEntity (índice actual, total, barra de acciones).

PhotoSlideEntry (cada página del pager con fullUri, placeholder, isSelected).

Bonus: “userInfoPhoto con URIs”
Si lo muestras en miniatura dentro de un grid → UserPhotoEntry (Entry).

Si es la pantalla de foto con toolbar, zoom, compartir → PhotoViewerViewEntity con una lista de PhotoSlideEntry (entries) y estado global (index, loading HD, etc.).

Checklist para nombrar
Termina en ViewEntity si describe una pantalla/feature completa.

Termina en Entry si es un ítem renderizable dentro de esa pantalla (lista, chip, card, field).

Con esto decides en 2 segundos: ¿es la “foto completa” de la UI? → ViewEntity. ¿Es una fichita dentro? → ViewEntry.
```