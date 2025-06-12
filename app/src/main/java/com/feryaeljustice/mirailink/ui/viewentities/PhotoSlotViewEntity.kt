package com.feryaeljustice.mirailink.ui.viewentities

data class PhotoSlotViewEntity(
    val url: String? = null,
    val position: Int = -1 // 0 a 3, pero luego tratamos el indice con -1 por ponerlo aqui en -1
)