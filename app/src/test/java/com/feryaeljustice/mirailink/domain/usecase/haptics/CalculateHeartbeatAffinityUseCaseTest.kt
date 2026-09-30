package com.feryaeljustice.mirailink.domain.usecase.haptics

import com.feryaeljustice.mirailink.domain.model.catalog.Anime
import com.feryaeljustice.mirailink.domain.model.catalog.Game
import com.feryaeljustice.mirailink.domain.model.haptics.HeartbeatAffinity
import com.feryaeljustice.mirailink.domain.model.user.User
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CalculateHeartbeatAffinityUseCaseTest {

    private val useCase = CalculateHeartbeatAffinityUseCase()

    private fun createTestUser(
        id: String,
        games: List<String> = emptyList(),
        animes: List<String> = emptyList(),
        goals: List<String> = emptyList(),
    ): User {
        return User(
            id = id,
            username = "user_$id",
            nickname = "Nick $id",
            email = null,
            phoneNumber = null,
            bio = null,
            gender = null,
            birthdate = null,
            photos = emptyList(),
            games = games.map { Game(id = it, name = it, imageUrl = null) },
            animes = animes.map { Anime(id = it, name = it, imageUrl = null) },
            relationshipGoalIds = goals,
        )
    }

    @Test
    fun `when currentUser is null returns DefaultNeutral`() {
        val candidate = createTestUser("candidate_1", games = listOf("game_1"))
        val result = useCase(currentUser = null, candidate = candidate)

        assertThat(result).isEqualTo(HeartbeatAffinity.DefaultNeutral)
        assertThat(result.ratio).isEqualTo(0.20f)
        assertThat(result.bpm).isEqualTo(71)
    }

    @Test
    fun `when user has no items in any category returns DefaultNeutral`() {
        val result = useCase(
            userGameIds = emptySet(),
            userAnimeIds = emptySet(),
            userGoalIds = emptySet(),
            candidateGameIds = setOf("game_1"),
            candidateAnimeIds = setOf("anime_1"),
            candidateGoalIds = setOf("goal_1"),
        )

        assertThat(result).isEqualTo(HeartbeatAffinity.DefaultNeutral)
    }

    @Test
    fun `when zero items match returns base ratio 0_20 and 60 BPM`() {
        val result = useCase(
            userGameIds = setOf("game_1", "game_2"),
            userAnimeIds = setOf("anime_1"),
            userGoalIds = setOf("goal_1"),
            candidateGameIds = setOf("game_3", "game_4"),
            candidateAnimeIds = setOf("anime_2"),
            candidateGoalIds = setOf("goal_2"),
        )

        assertThat(result.ratio).isEqualTo(0.20f)
        assertThat(result.percentage).isEqualTo(20)
        assertThat(result.bpm).isEqualTo(71) // 60 + (0.2 * 55) = 71
        assertThat(result.commonGamesCount).isEqualTo(0)
        assertThat(result.commonAnimesCount).isEqualTo(0)
        assertThat(result.commonGoalsCount).isEqualTo(0)
    }

    @Test
    fun `when 100 percent match across all categories returns 1_0 ratio and 115 BPM`() {
        val currentUser = createTestUser(
            id = "current",
            games = listOf("zelda", "elden_ring"),
            animes = listOf("frieren", "evangelion"),
            goals = listOf("long_term"),
        )
        val candidate = createTestUser(
            id = "candidate",
            games = listOf("zelda", "elden_ring"),
            animes = listOf("frieren", "evangelion"),
            goals = listOf("long_term"),
        )

        val result = useCase(currentUser = currentUser, candidate = candidate)

        assertThat(result.ratio).isEqualTo(1.0f)
        assertThat(result.percentage).isEqualTo(100)
        assertThat(result.bpm).isEqualTo(115) // 60 + 55 = 115
        assertThat(result.commonGamesCount).isEqualTo(2)
        assertThat(result.commonAnimesCount).isEqualTo(2)
        assertThat(result.commonGoalsCount).isEqualTo(1)
    }

    @Test
    fun `when partial match returns proportional intermediate ratio and BPM`() {
        val result = useCase(
            userGameIds = setOf("zelda", "mario"),
            userAnimeIds = setOf("naruto", "bleach"),
            userGoalIds = setOf("friendship"),
            candidateGameIds = setOf("zelda", "halo"),
            candidateAnimeIds = setOf("naruto", "one_piece"),
            candidateGoalIds = setOf("casual"),
        )

        assertThat(result.ratio).isGreaterThan(0.20f)
        assertThat(result.ratio).isLessThan(1.0f)
        assertThat(result.bpm).isGreaterThan(71)
        assertThat(result.bpm).isLessThan(115)
        assertThat(result.commonGamesCount).isEqualTo(1)
        assertThat(result.commonAnimesCount).isEqualTo(1)
        assertThat(result.commonGoalsCount).isEqualTo(0)
    }
}
