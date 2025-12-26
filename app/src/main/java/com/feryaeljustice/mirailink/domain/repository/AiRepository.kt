package com.feryaeljustice.mirailink.domain.repository

interface AiRepository {
    suspend fun generateContent(prompt: String): String
}
