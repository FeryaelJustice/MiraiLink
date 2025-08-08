package com.feryaeljustice.mirailink.ui.utils.extensions

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.openPlayStore(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}