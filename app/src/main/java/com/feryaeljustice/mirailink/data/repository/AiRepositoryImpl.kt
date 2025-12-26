package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.GeminiDataSource
import com.feryaeljustice.mirailink.domain.repository.AiRepository

class AiRepositoryImpl(
    private val geminiDataSource: GeminiDataSource,
) : AiRepository {
    override suspend fun generateContent(prompt: String): String = geminiDataSource.generateContent(prompt = prompt)
}
