/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.feed

import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.SwipeRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class GetFeedUseCase(
    private val repository: SwipeRepository,
) {
    suspend operator fun invoke(): MiraiLinkResult<List<User>> =
        try {
            repository.getFeed()
        } catch (e: Exception) {
            MiraiLinkResult.Error("GetFeedUseCase error", e)
        }
}
