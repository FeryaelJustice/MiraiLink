package com.feryaeljustice.mirailink.data.datasource

import com.google.firebase.ai.GenerativeModel

class GeminiDataSource(
    private val generativeModel: GenerativeModel,
) {
    suspend fun generateContent(prompt: String): String = generativeModel.generateContent(prompt).text ?: ""
}
