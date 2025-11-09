package com.feryaeljustice.mirailink.domain.usecase.match

import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.MatchRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

/**
 * @author Feryael Justice
 * @since 26/10/2024
 */
class GetMatchesUseCase(
    private val repository: MatchRepository,
) {
    suspend operator fun invoke(): MiraiLinkResult<List<User>> =
        try {
            repository.getMatches()
        } catch (e: Exception) {
            MiraiLinkResult.Error("GetMatchesUseCase error", e)
        }
}
