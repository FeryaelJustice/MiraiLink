/**
 * @author Feryael Justice
 * @date 29/07/2024
 */
package com.feryaeljustice.mirailink.data.util

import android.util.Log
import com.feryaeljustice.mirailink.domain.util.Logger
import javax.inject.Inject

class AndroidLogger
    @Inject
    constructor() : Logger {
        override fun d(
            tag: String,
            message: String,
        ) {
            Log.d(tag, message)
        }
    }
