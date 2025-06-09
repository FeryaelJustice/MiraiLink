package com.feryaeljustice.mirailink.domain.usecase.feed

import android.util.Log
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.SwipeRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class GetFeedUseCase @Inject constructor(private val repository: SwipeRepository) {
    suspend operator fun invoke(): MiraiLinkResult<List<User>> {
        val feed = repository.getFeed()
        Log.d("GetFeedUseCase", "Feed: $feed")
        return feed
    }
}