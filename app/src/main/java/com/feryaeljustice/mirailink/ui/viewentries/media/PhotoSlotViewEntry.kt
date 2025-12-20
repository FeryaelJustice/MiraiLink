package com.feryaeljustice.mirailink.ui.viewentries.media

import android.net.Uri
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class PhotoSlotViewEntry(
    val url: String? = null, // Foto del backend (remota)
    @Contextual val uri: Uri? = null, // Foto nueva (local)
    @Suppress("ktlint:standard:value-parameter-comment")
    val position: Int = -1, // 0 a 3, pero luego tratamos el indice con -1 por ponerlo aqui en -1
)
