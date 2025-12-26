package com.feryaeljustice.mirailink.domain.usecase.ai

import com.feryaeljustice.mirailink.domain.repository.AiRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class GenerateContentUseCase(
    private val aiRepository: AiRepository,
) {
    suspend operator fun invoke(prompt: String): MiraiLinkResult<String> =
        try {
            val response = aiRepository.generateContent(prompt)
            MiraiLinkResult.success(response)
        } catch (e: Exception) {
            MiraiLinkResult.Error(message = e.message ?: "unknown error GenerateContentUseCase", exception = e)
        }
}
