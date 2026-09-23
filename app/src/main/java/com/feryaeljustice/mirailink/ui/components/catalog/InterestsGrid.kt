package com.feryaeljustice.mirailink.ui.components.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.ui.viewentries.catalog.AnimeViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.catalog.GameViewEntry

/**
 * Cuadricula compacta de 3 columnas para mostrar animes o videojuegos favoritos.
 * Emplea filas ponderadas para evitar conflictos de scroll anidado dentro de tarjetas scrolleables.
 *
 * @param items Lista de elementos a mostrar.
 * @param modifier Modificador de Compose.
 * @param emptyText Texto que se muestra si la lista esta vacia.
 * @param onRemoveItem Callback opcional para eliminar un item (modo edicion).
 * @param onItemClick Callback opcional al pulsar sobre un elemento.
 */
@Composable
fun InterestsGrid(
    items: List<InterestItemData>,
    modifier: Modifier = Modifier,
    emptyText: String? = null,
    onRemoveItem: ((InterestItemData) -> Unit)? = null,
    onItemClick: ((InterestItemData) -> Unit)? = null,
) {
    if (items.isEmpty()) {
        if (!emptyText.isNullOrBlank()) {
            Text(
                text = emptyText,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier.padding(vertical = 4.dp),
            )
        }
        return
    }

    val rows = items.chunked(3)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                rowItems.forEach { item ->
                    InterestCard(
                        title = item.name,
                        imageUrl = item.imageUrl,
                        modifier = Modifier.weight(1f),
                        onRemoveClick = onRemoveItem?.let { { it(item) } },
                        onClick = onItemClick?.let { { it(item) } },
                    )
                }
                // Rellenar espacios vacios en la ultima fila para mantener la alineacion de 3 columnas
                repeat(3 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

/**
 * Modelo ligero desacoplado para representar cualquier interes en la cuadricula.
 */
data class InterestItemData(
    val id: String,
    val name: String,
    val imageUrl: String?,
)

fun AnimeViewEntry.toInterestItemData() = InterestItemData(
    id = id,
    name = name,
    imageUrl = imageUrl,
)

fun GameViewEntry.toInterestItemData() = InterestItemData(
    id = id,
    name = name,
    imageUrl = imageUrl,
)
