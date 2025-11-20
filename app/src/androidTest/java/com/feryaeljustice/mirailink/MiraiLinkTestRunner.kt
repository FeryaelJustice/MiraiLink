package com.feryaeljustice.mirailink

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class MiraiLinkTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        classLoader: ClassLoader?,
        className: String?,
        context: Context?,
    ): Application = super.newApplication(classLoader, MiraiLinkApp::class.java.name, context)
}
