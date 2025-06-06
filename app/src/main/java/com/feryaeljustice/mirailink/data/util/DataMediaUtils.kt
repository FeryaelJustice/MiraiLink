package com.feryaeljustice.mirailink.data.util

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.net.URI

// Función para crear archivo temporal
fun createImageUri(context: Context): Uri {
    val file = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
    file.createNewFile()
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}

fun deleteTempFile(uriString: String) {
    try {
        val file = File(URI(uriString))
        if (file.exists()) file.delete()
    } catch (e: Exception) {
        Log.w("Cleanup", "No se pudo borrar $uriString: ${e.message}")
    }
}

fun isTempFile(uriString: String): Boolean {
    return uriString.contains("/cache/") // o usa un prefijo como "photo_" para distinguirlo
}