package com.feryaeljustice.mirailink.domain.util

import com.feryaeljustice.mirailink.ui.viewentries.user.MinimalUserInfoViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry
import com.google.common.truth.Truth.assertThat
import org.junit.Test

/** Display-name contract tests for full and minimal user view entries. */
class UserUtilsTest {
    /** Verifies full user entries prefer a non-blank nickname. */
    @Test
    fun `full user display name prefers nickname`() {
        assertThat(user(nickname = "Mira").nicknameElseUsername()).isEqualTo("Mira")
    }

    /** Verifies full user entries fall back for both empty and whitespace nicknames. */
    @Test
    fun `full user display name falls back to username`() {
        assertThat(user(nickname = "").nicknameElseUsername()).isEqualTo("mirai")
        assertThat(user(nickname = "   ").nicknameElseUsername()).isEqualTo("mirai")
    }

    /** Verifies minimal entries follow the same display-name contract. */
    @Test
    fun `minimal user display name follows nickname fallback contract`() {
        val withNickname =
            MinimalUserInfoViewEntry(id = "1", username = "mirai", nickname = "Mira")
        val withoutNickname =
            MinimalUserInfoViewEntry(id = "1", username = "mirai", nickname = "")

        assertThat(withNickname.nicknameElseUsername()).isEqualTo("Mira")
        assertThat(withoutNickname.nicknameElseUsername()).isEqualTo("mirai")
    }

    /** Builds the smallest valid full user entry for display-name tests. */
    private fun user(nickname: String) =
        UserViewEntry(
            id = "1",
            username = "mirai",
            nickname = nickname,
            email = null,
            phoneNumber = null,
            bio = null,
            gender = null,
            birthdate = null,
            games = emptyList(),
            animes = emptyList(),
        )
}
