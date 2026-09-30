package com.feryaeljustice.mirailink.domain.usecase.haptics

import com.feryaeljustice.mirailink.domain.model.haptics.HeartbeatAffinity
import com.feryaeljustice.mirailink.domain.model.user.User
import kotlin.math.roundToInt

class CalculateHeartbeatAffinityUseCase {

    operator fun invoke(
        userGameIds: Set<String>,
        userAnimeIds: Set<String>,
        userGoalIds: Set<String>,
        candidateGameIds: Set<String>,
        candidateAnimeIds: Set<String>,
        candidateGoalIds: Set<String>,
    ): HeartbeatAffinity {
        val commonGames = userGameIds.intersect(candidateGameIds).size
        val commonAnimes = userAnimeIds.intersect(candidateAnimeIds).size
        val commonGoals = userGoalIds.intersect(candidateGoalIds).size

        if (userGameIds.isEmpty() && userAnimeIds.isEmpty() && userGoalIds.isEmpty()) {
            return HeartbeatAffinity.DefaultNeutral
        }

        val gameScore = calculateCategoryScore(userGameIds, candidateGameIds)
        val animeScore = calculateCategoryScore(userAnimeIds, candidateAnimeIds)
        val goalScore = calculateCategoryScore(userGoalIds, candidateGoalIds)

        val weightedScore = (gameScore * 0.40f) + (animeScore * 0.40f) + (goalScore * 0.20f)
        val finalRatio = (0.20f + (weightedScore * 0.80f)).coerceIn(0.20f, 1.0f)
        val percentage = (finalRatio * 100f).roundToInt().coerceIn(20, 100)
        val bpm = 60 + (finalRatio * 55f).roundToInt()

        return HeartbeatAffinity(
            ratio = finalRatio,
            percentage = percentage,
            bpm = bpm,
            commonAnimesCount = commonAnimes,
            commonGamesCount = commonGames,
            commonGoalsCount = commonGoals,
        )
    }

    operator fun invoke(
        currentUser: User?,
        candidate: User,
    ): HeartbeatAffinity {
        if (currentUser == null) return HeartbeatAffinity.DefaultNeutral

        return invoke(
            userGameIds = currentUser.games.map { it.id }.toSet(),
            userAnimeIds = currentUser.animes.map { it.id }.toSet(),
            userGoalIds = currentUser.relationshipGoalIds.toSet(),
            candidateGameIds = candidate.games.map { it.id }.toSet(),
            candidateAnimeIds = candidate.animes.map { it.id }.toSet(),
            candidateGoalIds = candidate.relationshipGoalIds.toSet(),
        )
    }

    private fun calculateCategoryScore(userItems: Set<String>, candidateItems: Set<String>): Float {
        if (userItems.isEmpty() || candidateItems.isEmpty()) return 0f
        val common = userItems.intersect(candidateItems).size
        if (common == 0) return 0f

        val jaccard = common.toFloat() / userItems.union(candidateItems).size
        val userCoverage = common.toFloat() / userItems.size
        return ((jaccard * 0.5f) + (userCoverage * 0.5f)).coerceIn(0f, 1f)
    }
}
