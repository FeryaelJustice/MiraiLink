package com.feryaeljustice.mirailink.ui.util

import android.content.Context
import androidx.annotation.DrawableRes
import com.feryaeljustice.mirailink.R

object InterestImageFallback {
    /**
     * Resuelve el recurso de imagen fallback en cascada para animes y juegos:
     * 1. Comprueba si existe un recurso drawable con nombre "goku" en el paquete.
     * 2. Si existe (resId != 0), lo retorna.
     * 3. Si no existe o falla, retorna el logotipo oficial de MiraiLink (R.drawable.logomirailink).
     */
    @DrawableRes
    fun getFallbackDrawableRes(context: Context): Int {
        return try {
            val gokuResId = context.resources.getIdentifier("goku", "drawable", context.packageName)
            if (gokuResId != 0) gokuResId else R.drawable.logomirailink
        } catch (_: Exception) {
            R.drawable.logomirailink
        }
    }
}
