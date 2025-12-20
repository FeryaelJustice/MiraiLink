package com.feryaeljustice.mirailink.data.util

import android.util.Log
import com.feryaeljustice.mirailink.domain.util.Logger

class AndroidLogger : Logger {
    override fun d(
        tag: String,
        message: String,
    ) {
        Log.d(tag, message)
    }
}
