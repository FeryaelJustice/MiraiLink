package com.feryaeljustice.mirailink.data.telemetry

import android.util.Log
import com.feryaeljustice.mirailink.domain.telemetry.CrashReporter

class MiraiUncaughtExceptionHandler(
    private val defaultHandler: Thread.UncaughtExceptionHandler?,
    private val crashReporter: CrashReporter,
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(
        thread: Thread,
        throwable: Throwable,
    ) {
        try {
            crashReporter.log("Uncaught exception on thread: ${thread.name}")
            crashReporter.recordException(
                throwable = throwable,
                reason = "Uncaught exception on thread ${thread.name}",
                metadata = mapOf("thread_name" to thread.name, "is_fatal" to true),
            )
        } catch (t: Throwable) {
            Log.e("MiraiCrashHandler", "Failed to report uncaught exception", t)
        } finally {
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }

    companion object {
        fun install(crashReporter: CrashReporter) {
            val currentHandler = Thread.getDefaultUncaughtExceptionHandler()
            if (currentHandler !is MiraiUncaughtExceptionHandler) {
                Thread.setDefaultUncaughtExceptionHandler(
                    MiraiUncaughtExceptionHandler(currentHandler, crashReporter),
                )
            }
        }
    }
}
