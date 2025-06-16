package com.feryaeljustice.mirailink.ui.viewentities

import android.net.Uri

data class PhotoSlotViewEntity(
    val url: String? = null,     // Foto del backend (remota)
    val uri: Uri? = null,        // Foto nueva (local)
    val position: Int = -1 // 0 a 3, pero luego tratamos el indice con -1 por ponerlo aqui en -1
)