package com.feryaeljustice.mirailink.domain.usecase.match

import android.util.Log
import com.feryaeljustice.mirailink.domain.model.User
import com.feryaeljustice.mirailink.domain.repository.MatchRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import javax.inject.Inject

class GetMatchesUseCase @Inject constructor(
    private val repository: MatchRepository
) {
    suspend operator fun invoke(): MiraiLinkResult<List<User>> {
        val matches = repository.getMatches()
        Log.d("GetMatchesUseCase", "Matches: $matches")
        return matches
    }
}