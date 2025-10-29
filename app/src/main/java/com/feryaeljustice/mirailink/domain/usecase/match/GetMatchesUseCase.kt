package com.feryaeljustice.mirailink.domain.usecase.match

import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.MatchRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

/**
 * @author Feryael Justice
 * @since 26/10/2024
 */
class GetMatchesUseCase @Inject constructor(
    private val repository: MatchRepository
) {
    suspend operator fun invoke(): MiraiLinkResult<List<User>> {
        return try {
            repository.getMatches()
        } catch (e: Exception) {
            MiraiLinkResult.Error("GetMatchesUseCase error", e)
        }
    }
}