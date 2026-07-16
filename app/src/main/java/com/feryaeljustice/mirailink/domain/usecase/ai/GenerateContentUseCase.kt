package com.feryaeljustice.mirailink.domain.usecase.ai

import com.feryaeljustice.mirailink.domain.error.UnknownError
import com.feryaeljustice.mirailink.domain.repository.AiRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import java.util.concurrent.CancellationException

class GenerateContentUseCase(
    private val aiRepository: AiRepository,
) {
    suspend operator fun invoke(prompt: String): MiraiLinkResult<String> =
        try {
            val response = aiRepository.generateContent(prompt)
            MiraiLinkResult.success(response)
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (e: Exception) {
            MiraiLinkResult.Error(UnknownError)
        }
}
